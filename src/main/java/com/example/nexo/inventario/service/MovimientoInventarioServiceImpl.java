package com.example.nexo.inventario.service;
import java.math.BigDecimal;
import java.time.*;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.access.prepost.PreAuthorize;
import com.example.nexo.inventario.model.MovimientoInventario;
import com.example.nexo.inventario.repository.MovimientoInventarioRepository;
import com.example.nexo.producto.repository.ProductoRepository;
import com.example.nexo.auth.service.ActorActual;
import com.example.nexo.shared.validation.Importes;
import static com.example.nexo.shared.validation.ReglaNegocioException.exigir;
import com.example.nexo.shared.config.TiempoConfig;
/** Movimientos persistidos; las compras/ventas generan sus orígenes en OperacionCombustible. */
@Service @Transactional
public class MovimientoInventarioServiceImpl implements MovimientoInventarioService {
 private final MovimientoInventarioRepository repo; private final ProductoRepository productos; private final ActorActual actor; private final Clock reloj;
 public MovimientoInventarioServiceImpl(MovimientoInventarioRepository repo,ProductoRepository productos,ActorActual actor,Clock reloj) {this.repo=repo;this.productos=productos;this.actor=actor;this.reloj=reloj.withZone(TiempoConfig.LIMA);}
 public List<MovimientoInventario> listaMovimientos() {return repo.findAll();}
 public List<MovimientoInventario> listaEntradas() {return repo.findAll().stream().filter(m->"Entrada".equals(m.getTipoMovimiento())).toList();}
 public List<MovimientoInventario> listaSalidas() {return repo.findAll().stream().filter(m->"Salida".equals(m.getTipoMovimiento())).toList();}
 public MovimientoInventario buscarMovimientoPorId(String id) {try {return id!=null && id.startsWith("MI")?repo.findById(Long.parseLong(id.substring(2))).orElse(null):null;}catch(NumberFormatException e) {return null;}}
 @PreAuthorize("hasAnyRole('ADMIN','OPERADOR') && @actorActual.estaVigente()")
 public String registrarEntrada(MovimientoInventario m) {registrar(m,true);return null;}
 @PreAuthorize("hasAnyRole('ADMIN','OPERADOR') && @actorActual.estaVigente()")
 public String registrarSalida(MovimientoInventario m) {registrar(m,false);return null;}
 private void registrar(MovimientoInventario m,boolean entrada) {
  exigir(m!=null && Importes.litros(m.getCantidad()),"Los litros deben ser positivos y tener hasta tres decimales.");
  var p=productos.bloquear(m.getIdProducto()).orElse(null);
  exigir(p!=null && "Activo".equals(p.getEstado()) && "Activo".equals(p.getCategoria().getEstado()),"El producto o su categoría no están disponibles.");
  String motivo=m.getMotivo()==null?"":m.getMotivo().strip();exigir(motivo.length()>=5 && motivo.length()<=200,"El motivo debe tener entre 5 y 200 caracteres.");
  BigDecimal stock=entrada?p.getStock().add(m.getCantidad()):p.getStock().subtract(m.getCantidad());
  exigir(stock.signum()>=0,"No hay stock suficiente para la salida.");exigir(stock.compareTo(new BigDecimal("999999999.999"))<=0,"El stock excede DECIMAL(12,3).");
  m.setClave(null);m.setProducto(p);m.setUsuario(actor.usuario());m.setTipoMovimiento(entrada?"Entrada":"Salida");m.setDetalleCompra(null);m.setDetalleVenta(null);
  m.setFechaHora(LocalDateTime.now(reloj));m.setMotivo(motivo);p.setStock(stock);repo.saveAndFlush(m);
 }
}
