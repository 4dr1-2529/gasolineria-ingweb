package com.example.nexo.shared.service;

import java.math.*;
import java.time.*;
import java.util.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.nexo.producto.repository.ProductoRepository;
import com.example.nexo.venta.repository.*;
import com.example.nexo.finanzas.repository.*;
import com.example.nexo.shared.config.TiempoConfig;

@Service @Transactional(readOnly=true)
public class DashboardService {
    private final ProductoRepository productos;private final VentaRepository ventas;private final DetalleVentaRepository detalles;
    private final MovimientoCajaRepository movimientos;private final CajaDiariaRepository cajas;private final Clock reloj;
    public DashboardService(ProductoRepository productos,VentaRepository ventas,DetalleVentaRepository detalles,MovimientoCajaRepository movimientos,CajaDiariaRepository cajas,Clock reloj){this.productos=productos;this.ventas=ventas;this.detalles=detalles;this.movimientos=movimientos;this.cajas=cajas;this.reloj=reloj.withZone(TiempoConfig.LIMA);}
    public static class Dia {
        private final LocalDate fecha;private BigDecimal ventas=BigDecimal.ZERO,litros=BigDecimal.ZERO,ingresos=BigDecimal.ZERO,egresos=BigDecimal.ZERO,apertura=BigDecimal.ZERO;
        Dia(LocalDate fecha){this.fecha=fecha;}
        public LocalDate getFecha(){return fecha;} public BigDecimal getVentas(){return ventas;} public BigDecimal getLitros(){return litros;}
        public BigDecimal getIngresos(){return ingresos;} public BigDecimal getEgresos(){return egresos;} public BigDecimal getSaldo(){return apertura.add(ingresos).subtract(egresos);}
        BigDecimal valor(int indice){return switch(indice){case 0->ventas;case 1->litros;case 2->ingresos;case 3->egresos;default->getSaldo();};}
    }
    public static class Punto {
        private final int x,y;Punto(int x,int y){this.x=x;this.y=y;}public int getX(){return x;}public int getY(){return y;}
    }
    public static class Serie {
        private final String nombre,unidad,formula,fuente,objetivo;private final List<Punto> puntos=new ArrayList<>();private final BigDecimal minimo,maximo;
        Serie(String nombre,String unidad,String formula,String fuente,String objetivo,List<Dia> dias,int indice){
            this.nombre=nombre;this.unidad=unidad;this.formula=formula;this.fuente=fuente;this.objetivo=objetivo;
            minimo=dias.stream().map(d->d.valor(indice)).min(BigDecimal::compareTo).orElse(BigDecimal.ZERO).min(BigDecimal.ZERO);
            maximo=dias.stream().map(d->d.valor(indice)).max(BigDecimal::compareTo).orElse(BigDecimal.ZERO).max(BigDecimal.ZERO);
            double rango=maximo.subtract(minimo).doubleValue();if(rango==0)rango=1;
            for(int i=0;i<dias.size();i++)puntos.add(new Punto(60+(dias.size()<2?0:680*i/(dias.size()-1)),140-(int)(120*dias.get(i).valor(indice).subtract(minimo).doubleValue()/rango)));
        }
        public String getNombre(){return nombre;}public String getUnidad(){return unidad;}public String getFormula(){return formula;}public String getFuente(){return fuente;}public String getObjetivo(){return objetivo;}
        public List<Punto> getPuntos(){return puntos;}public String getPolilinea(){return puntos.stream().map(p->p.x+","+p.y).collect(java.util.stream.Collectors.joining(" "));}
        public BigDecimal getMinimo(){return minimo;}public BigDecimal getMaximo(){return maximo;}
    }
    public Map<String,Object> datos(){
        var auth=org.springframework.security.core.context.SecurityContextHolder.getContext().getAuthentication();
        boolean admin=auth!=null && auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_ADMIN"));
        boolean vendedor=auth!=null && auth.getAuthorities().stream().anyMatch(a->a.getAuthority().equals("ROLE_VENDEDOR"));
        Map<String,Object> resultado=new HashMap<>();LocalDate hoy=LocalDate.now(reloj);
        resultado.put("hoy",hoy);resultado.put("admin",admin);resultado.put("verVentas",admin||vendedor);
        resultado.put("stock",productos.findAll().stream().filter(p->"Activo".equals(p.getEstado()) && "Activo".equals(p.getCategoria().getEstado())).map(p->p.getStock()).reduce(BigDecimal.ZERO,BigDecimal::add));
        Map<LocalDate,Dia> dias=new TreeMap<>();
        if(admin||vendedor){
            for(var v:ventas.findAll())if("Confirmada".equals(v.getEstado())){var d=dias.computeIfAbsent(v.getFechaHora().toLocalDate(),Dia::new);d.ventas=d.ventas.add(v.getTotal());}
            for(var detalle:detalles.findAll())if("Confirmada".equals(detalle.getVenta().getEstado())){var d=dias.computeIfAbsent(detalle.getVenta().getFechaHora().toLocalDate(),Dia::new);d.litros=d.litros.add(detalle.getCantidad());}
        }
        if(admin){
            for(var c:cajas.findAll())dias.computeIfAbsent(c.getFecha(),Dia::new).apertura=c.getSaldoApertura();
            for(var m:movimientos.findAll()){var d=dias.computeIfAbsent(m.getFechaHora().toLocalDate(),Dia::new);if("Ingreso".equals(m.getTipo()))d.ingresos=d.ingresos.add(m.getMonto());else d.egresos=d.egresos.add(m.getMonto());}
        }
        Dia actual=dias.getOrDefault(hoy,new Dia(hoy));resultado.put("actual",actual);
        List<Dia> datos=new ArrayList<>(dias.values());resultado.put("dias",datos);List<Serie> series=new ArrayList<>();
        String[] nombres={"Ventas diarias","Litros vendidos por día","Ingresos diarios","Egresos diarios","Saldo de caja por día"};
        String[] formulas={"SUM(Venta.total) por fecha de confirmación","SUM(DetalleVenta.cantidad_litros) por fecha de venta","SUM(monto) con tipo INGRESO por fecha","SUM(monto) con tipo EGRESO por fecha","Apertura + ingresos del día − egresos del día"};
        String[] fuentes={"Venta / Ventas","DetalleVenta + Venta / Ventas","MovimientoCaja / Finanzas","MovimientoCaja / Finanzas","CajaDiaria + MovimientoCaja / Finanzas"};
        String[] objetivos={"Consultar el importe vendido","Consultar el volumen vendido","Rastrear ingresos de ventas","Rastrear pagos de compras","Conciliar la caja diaria"};
        if(!datos.isEmpty())for(int i=0;i<(admin?5:vendedor?2:0);i++)series.add(new Serie(nombres[i],i==1?"L":"S/",formulas[i],fuentes[i],objetivos[i],datos,i));
        resultado.put("series",series);return resultado;
    }
}
