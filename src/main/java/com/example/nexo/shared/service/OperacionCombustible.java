package com.example.nexo.shared.service;

import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.nexo.auth.service.ActorActual;
import com.example.nexo.compra.model.*;
import com.example.nexo.compra.repository.*;
import com.example.nexo.venta.model.*;
import com.example.nexo.venta.repository.*;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.repository.ProductoRepository;
import com.example.nexo.proveedor.repository.ProveedorRepository;
import com.example.nexo.inventario.model.MovimientoInventario;
import com.example.nexo.inventario.repository.MovimientoInventarioRepository;
import com.example.nexo.finanzas.model.*;
import com.example.nexo.finanzas.repository.*;
import com.example.nexo.finanzas.service.CajaDiariaService;
import com.example.nexo.shared.persistence.Codigos;
import com.example.nexo.shared.validation.Importes;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
import com.example.nexo.shared.config.TiempoConfig;

/** RN01–RN04: cabecera, líneas, stock y ambos libros comparten una sola transacción. */
@Service @Transactional
public class OperacionCombustible {
    private final CompraRepository compras; private final DetalleCompraRepository detallesCompra;
    private final VentaRepository ventas; private final DetalleVentaRepository detallesVenta;
    private final ProductoRepository productos; private final ProveedorRepository proveedores;
    private final MovimientoInventarioRepository inventario; private final MovimientoCajaRepository movimientos;
    private final ConceptoMovimientoRepository conceptos; private final CajaDiariaService cajas;
    private final ActorActual actor; private final Clock reloj;
    public OperacionCombustible(CompraRepository compras,DetalleCompraRepository detallesCompra,
            VentaRepository ventas,DetalleVentaRepository detallesVenta,ProductoRepository productos,
            ProveedorRepository proveedores,MovimientoInventarioRepository inventario,
            MovimientoCajaRepository movimientos,ConceptoMovimientoRepository conceptos,
            CajaDiariaService cajas,ActorActual actor,Clock reloj) {
        this.compras=compras;this.detallesCompra=detallesCompra;this.ventas=ventas;this.detallesVenta=detallesVenta;
        this.productos=productos;this.proveedores=proveedores;this.inventario=inventario;
        this.movimientos=movimientos;this.conceptos=conceptos;this.cajas=cajas;this.actor=actor;this.reloj=reloj.withZone(TiempoConfig.LIMA);
    }
    private Map<String,Producto> bloquear(List<String> codigos) {
        exigir(codigos.stream().noneMatch(Objects::isNull),"Debes seleccionar un combustible para cada línea.");
        Map<String,Producto> resultado=new TreeMap<>();
        for(String codigo:new TreeSet<>(codigos)) {
            Producto p=productos.bloquear(codigo).orElse(null);
            exigir(p!=null,"El combustible no existe.");
            exigir("Activo".equals(p.getEstado()) && "Activo".equals(p.getCategoria().getEstado()),"El combustible o su categoría están inactivos.");
            resultado.put(codigo,p);
        }
        return resultado;
    }
    private void importe(BigDecimal monto) { exigir(Importes.dinero(monto),"El importe debe ser positivo y ajustarse a DECIMAL(12,2)."); }
    private ConceptoMovimiento concepto(String codigo,String tipo) {
        var c=conceptos.buscarCodigo(codigo).orElse(null);
        exigir(c!=null && "Activo".equals(c.getEstado()) && tipo.equals(c.getTipo()),"El concepto económico de la operación no está disponible.");return c;
    }
    private void economico(CajaDiaria caja,ConceptoMovimiento concepto,Compra compra,Venta venta,BigDecimal monto,LocalDateTime fecha) {
        MovimientoCaja m=new MovimientoCaja();m.setId(Codigos.nuevo("MC",12));m.setCaja(caja);m.setConcepto(concepto);
        m.setUsuario(actor.usuario());m.setCompra(compra);m.setVenta(venta);m.setTipo(compra==null?"Ingreso":"Egreso");
        m.setDescripcion(compra==null?"Venta "+venta.getId():"Compra "+compra.getId());m.setMonto(monto);m.setFechaHora(fecha);movimientos.saveAndFlush(m);
    }
    private MovimientoInventario fisico(Producto p,BigDecimal cantidad,String tipo,LocalDateTime fecha,String motivo) {
        var m=new MovimientoInventario();m.setProducto(p);m.setUsuario(actor.usuario());m.setCantidad(cantidad);
        m.setTipoMovimiento(tipo);m.setFechaHora(fecha);m.setMotivo(motivo);return m;
    }
    @PreAuthorize("hasRole('ADMIN')")
    public void compra(Compra c,List<DetalleCompra> lineas) {
        exigir(c!=null && lineas!=null && !lineas.isEmpty(),"La compra debe tener al menos una línea.");
        exigir(c.getId()==null || compras.buscarCodigo(c.getId()).isEmpty(),"Esta compra ya fue registrada; no se duplicaron sus efectos.");
        CajaDiaria caja=cajas.abiertaBloqueada();
        var proveedor=c.getIdProveedor()==null?null:proveedores.findById(c.getIdProveedor()).orElse(null);
        exigir(proveedor!=null && "Activo".equals(proveedor.getEstado()),"Debes seleccionar un proveedor activo.");
        var concepto=concepto("CE02","Egreso");
        var bloqueados=bloquear(lineas.stream().map(DetalleCompra::getIdProducto).toList());
        BigDecimal total=BigDecimal.ZERO;Map<String,BigDecimal> cantidades=new HashMap<>();
        for(var d:lineas) {
            exigir(Importes.litros(d.getCantidad()),"Los litros deben ser positivos y tener hasta tres decimales.");importe(d.getPrecioCompra());
            BigDecimal subtotal=d.getCantidad().multiply(d.getPrecioCompra()).setScale(2,RoundingMode.HALF_UP);importe(subtotal);
            d.setSubtotal(subtotal);total=total.add(subtotal);cantidades.merge(d.getIdProducto(),d.getCantidad(),BigDecimal::add);
        }
        importe(total);
        cantidades.forEach((codigo,cantidad)->exigir(bloqueados.get(codigo).getStock().add(cantidad).compareTo(new BigDecimal("999999999.999"))<=0,"El stock resultante excede DECIMAL(12,3)."));
        exigir(c.getObservacion()==null || c.getObservacion().length()<=200,"La observación no puede superar 200 caracteres.");
        c.setClave(null);if(c.getId()==null)c.setId(Codigos.nuevo("C",12));c.setUsuario(actor.usuario());c.setProveedorEntidad(proveedor);
        c.setFechaHora(LocalDateTime.now(reloj).withNano(0));c.setTotal(total);c.setEstado("Confirmada");compras.saveAndFlush(c);
        for(var d:lineas) {
            var p=bloqueados.get(d.getIdProducto());d.setId(null);d.setCompra(c);d.setProducto(p);detallesCompra.saveAndFlush(d);
            p.setStock(p.getStock().add(d.getCantidad()));var m=fisico(p,d.getCantidad(),"Entrada",c.getFechaHora(),"Compra "+c.getId());m.setDetalleCompra(d);inventario.save(m);
        }
        economico(caja,concepto,c,null,total,c.getFechaHora());
    }
    @PreAuthorize("hasAnyRole('ADMIN','VENDEDOR')")
    public void venta(Venta v,List<DetalleVenta> lineas) {
        exigir(v!=null && lineas!=null && !lineas.isEmpty(),"La venta debe tener al menos una línea.");
        exigir(v.getId()==null || ventas.buscarCodigo(v.getId()).isEmpty(),"Esta venta ya fue registrada; no se duplicaron sus efectos.");
        CajaDiaria caja=cajas.abiertaBloqueada();var concepto=concepto("CE01","Ingreso");
        var bloqueados=bloquear(lineas.stream().map(DetalleVenta::getIdProducto).toList());
        BigDecimal total=BigDecimal.ZERO;Map<String,BigDecimal> cantidades=new HashMap<>();
        for(var d:lineas) {
            exigir(Importes.litros(d.getCantidad()),"Los litros deben ser positivos y tener hasta tres decimales.");
            var p=bloqueados.get(d.getIdProducto());importe(p.getPrecioActual());
            d.setPrecioUnitario(p.getPrecioActual());BigDecimal subtotal=d.getCantidad().multiply(p.getPrecioActual()).setScale(2,RoundingMode.HALF_UP);importe(subtotal);
            d.setSubtotal(subtotal);total=total.add(subtotal);cantidades.merge(d.getIdProducto(),d.getCantidad(),BigDecimal::add);
        }
        importe(total);
        cantidades.forEach((codigo,cantidad)->exigir(bloqueados.get(codigo).getStock().compareTo(cantidad)>=0,"No hay stock suficiente: la operación completa fue rechazada."));
        v.setClave(null);if(v.getId()==null)v.setId(Codigos.nuevo("V",12));v.setUsuario(actor.usuario());v.setFechaHora(LocalDateTime.now(reloj).withNano(0));v.setTotal(total);v.setEstado("Confirmada");ventas.saveAndFlush(v);
        for(var d:lineas) {
            var p=bloqueados.get(d.getIdProducto());d.setId(null);d.setVenta(v);d.setProducto(p);detallesVenta.saveAndFlush(d);
            p.setStock(p.getStock().subtract(d.getCantidad()));var m=fisico(p,d.getCantidad(),"Salida",v.getFechaHora(),"Venta "+v.getId());m.setDetalleVenta(d);inventario.save(m);
        }
        economico(caja,concepto,null,v,total,v.getFechaHora());
    }
}
