package com.example.nexo;

import static org.junit.jupiter.api.Assertions.*;
import java.math.BigDecimal;
import java.time.*;
import java.util.*;
import java.util.concurrent.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import com.example.nexo.categoria.model.Categoria;
import com.example.nexo.categoria.repository.CategoriaRepository;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.repository.ProductoRepository;
import com.example.nexo.proveedor.model.Proveedor;
import com.example.nexo.proveedor.repository.ProveedorRepository;
import com.example.nexo.proveedor.service.ProveedorService;
import com.example.nexo.compra.model.*;
import com.example.nexo.compra.service.CompraService;
import com.example.nexo.compra.repository.CompraRepository;
import com.example.nexo.venta.model.*;
import com.example.nexo.venta.service.VentaService;
import com.example.nexo.venta.repository.VentaRepository;
import com.example.nexo.finanzas.repository.*;
import com.example.nexo.finanzas.service.CajaDiariaService;
import com.example.nexo.shared.service.DashboardService;
import com.example.nexo.shared.validation.ReglaNegocioException;

/** Cada llamada de negocio confirma su propia transacción; las verificaciones leen de nuevo MySQL. */
@SpringBootTest @ActiveProfiles("demo")
class PersistenciaMySqlTest {
    @Autowired JdbcTemplate jdbc; @Autowired CategoriaRepository categorias; @Autowired ProductoRepository productos;
    @Autowired ProveedorRepository proveedores; @Autowired ProveedorService servicioProveedores;
    @Autowired CompraService compras; @Autowired CompraRepository compraRepo;
    @Autowired VentaService ventas; @Autowired VentaRepository ventaRepo;
    @Autowired CajaDiariaRepository cajaRepo; @Autowired CajaDiariaService cajas;
    @Autowired MovimientoCajaRepository movimientos; @Autowired DashboardService dashboard;
    String codigo;Long proveedorId;
    @BeforeEach void preparar(){
        assertEquals("nexo_pruebas",jdbc.queryForObject("select database()",String.class),"Estas pruebas mutan solo su BD aislada.");
        ReglasNegocioTest.autenticar("ediaz","ADMIN");
        var caja=cajas.actual();if(caja==null)cajas.abrir(new BigDecimal("100"));
        else if(!"ABIERTA".equals(caja.getEstado()))jdbc.update("update caja_diaria set estado='ABIERTA',saldo_cierre=null,id_usuario_cierre=null,fecha_cierre=null where id_caja=?",caja.getId());
        String sufijo=UUID.randomUUID().toString().replace("-","").substring(0,8);
        var categoria=categorias.saveAndFlush(new Categoria(null,"Prueba "+sufijo,"Integración MySQL","Activo"));
        Producto p=new Producto("P"+sufijo,categoria.getId(),"Combustible "+sufijo,"L",new BigDecimal("5"),new BigDecimal("10"),"Activo");p.setCategoria(categoria);codigo=productos.saveAndFlush(p).getId();
        var proveedor=new Proveedor();proveedor.setRuc(String.format("%011d",70000000000L+new java.security.SecureRandom().nextInt(1000000000)));proveedor.setRazonSocial("Proveedor "+sufijo);proveedor.setEstado("Activo");servicioProveedores.guardar(proveedor);
        proveedorId=servicioProveedores.listar(proveedor.getRuc(),null,null).get(0).getId();
    }
    @AfterEach void limpiar(){SecurityContextHolder.clearContext();}
    DetalleCompra compraDetalle(String litros){return new DetalleCompra(null,null,codigo,new BigDecimal(litros),new BigDecimal("4"),null);}
    DetalleVenta ventaDetalle(String litros){return new DetalleVenta(null,null,codigo,new BigDecimal(litros),null,null);}
    Compra compra(){var c=new Compra();c.setIdProveedor(proveedorId);return c;}
    BigDecimal stock(){return productos.buscarCodigo(codigo).orElseThrow().getStock();}
    @Test void compraVariasLineasPersisteTodosLosEfectos(){
        var c=compra();compras.crearCompra(c,List.of(compraDetalle("1.125"),compraDetalle("2.250")));
        assertEquals(0,stock().compareTo(new BigDecimal("13.375")));
        var persistida=compraRepo.buscarCodigo(c.getId()).orElseThrow();assertEquals(proveedorId,persistida.getIdProveedor());assertEquals(3L,persistida.getIdUsuario());
        assertEquals(2,compras.listaDetallesPorCompra(c.getId()).size());
        assertEquals(2,jdbc.queryForObject("select count(*) from movimiento_inventario m join detalle_compra d on m.id_detalle_compra=d.id_detalle_compra where d.id_compra=?",Integer.class,persistida.getClave()));
        assertEquals(1,jdbc.queryForObject("select count(*) from movimiento_caja where id_compra=?",Integer.class,persistida.getClave()));
        assertEquals(0,persistida.getTotal().compareTo(new BigDecimal("13.50")));
    }
    @Test void ventaPersistePrecioServidorYReferencias(){
        ReglasNegocioTest.autenticar("lrojas","VENDEDOR");Venta v=new Venta();v.setIdUsuario(3L);v.setFechaHora(LocalDateTime.of(2000,1,1,0,0));var d=ventaDetalle("1.125");d.setPrecioUnitario(new BigDecimal("0.01"));ventas.crearVenta(v,d);
        assertEquals(0,stock().compareTo(new BigDecimal("8.875")));var guardada=ventaRepo.buscarCodigo(v.getId()).orElseThrow();assertEquals(2L,guardada.getIdUsuario());assertEquals(LocalDate.now(com.example.nexo.shared.config.TiempoConfig.LIMA),guardada.getFechaHora().toLocalDate());
        assertEquals(0,guardada.getTotal().compareTo(new BigDecimal("5.63")));assertEquals(1,jdbc.queryForObject("select count(*) from movimiento_caja where id_venta=?",Integer.class,guardada.getClave()));
        assertEquals(1,jdbc.queryForObject("select count(*) from movimiento_inventario m join detalle_venta d on m.id_detalle_venta=d.id_detalle_venta where d.id_venta=?",Integer.class,guardada.getClave()));
    }
    @Test void falloEconomicoRevierteVentaCompleta(){
        long previas=ventaRepo.count();long movimientosPrevios=movimientos.count();var v=new Venta();v.setId("VT"+UUID.randomUUID().toString().replace("-","").substring(0,10));
        jdbc.execute("alter table movimiento_caja add constraint ck_nexo_prueba_fallo check (descripcion <> 'Venta "+v.getId()+"')");
        try{assertThrows(RuntimeException.class,()->ventas.crearVenta(v,ventaDetalle("2")));}finally{jdbc.execute("alter table movimiento_caja drop check ck_nexo_prueba_fallo");}
        assertEquals(previas,ventaRepo.count());assertEquals(movimientosPrevios,movimientos.count());assertEquals(0,stock().compareTo(BigDecimal.TEN));assertTrue(ventaRepo.buscarCodigo(v.getId()).isEmpty());
        assertEquals(0,jdbc.queryForObject("select count(*) from detalle_venta where id_producto=(select id_producto from producto where codigo_producto=?)",Integer.class,codigo));
    }
    @Test void falloEconomicoRevierteCompraCompleta(){
        long previas=compraRepo.count();var c=compra();c.setId("CT"+UUID.randomUUID().toString().replace("-","").substring(0,10));jdbc.execute("alter table movimiento_caja add constraint ck_nexo_prueba_fallo check (descripcion <> 'Compra "+c.getId()+"')");
        try{assertThrows(RuntimeException.class,()->compras.crearCompra(c,compraDetalle("2")));}finally{jdbc.execute("alter table movimiento_caja drop check ck_nexo_prueba_fallo");}
        assertEquals(previas,compraRepo.count());assertEquals(0,stock().compareTo(BigDecimal.TEN));assertTrue(compraRepo.buscarCodigo(c.getId()).isEmpty());
        assertEquals(0,jdbc.queryForObject("select count(*) from movimiento_inventario where id_producto=(select id_producto from producto where codigo_producto=?)",Integer.class,codigo));
    }
    @Test void codigoPersistidoImpideDuplicarSolicitud(){
        Venta v=new Venta();ventas.crearVenta(v,ventaDetalle("2"));long n=ventaRepo.count();Venta repetida=new Venta();repetida.setId(v.getId());assertThrows(ReglaNegocioException.class,()->ventas.crearVenta(repetida,ventaDetalle("2")));assertEquals(n,ventaRepo.count());assertEquals(0,stock().compareTo(new BigDecimal("8")));
    }
    @Test void ventasConcurrentesNoDejanStockNegativo() throws Exception{
        ExecutorService pool=Executors.newFixedThreadPool(2);var barrera=new CyclicBarrier(2);
        try{List<Callable<Boolean>> tareas=List.of(()->venderConcurrente(barrera),()->venderConcurrente(barrera));var futures=pool.invokeAll(tareas);int exitos=0;for(var f:futures)if(f.get())exitos++;assertEquals(1,exitos);assertEquals(0,stock().compareTo(new BigDecimal("3")));}finally{pool.shutdownNow();}
    }
    boolean venderConcurrente(CyclicBarrier barrera) throws Exception{
        ReglasNegocioTest.autenticar("lrojas","VENDEDOR");barrera.await();try{ventas.crearVenta(new Venta(),ventaDetalle("7"));return true;}catch(ReglaNegocioException e){return false;}finally{SecurityContextHolder.clearContext();}
    }
    @Test void cajaCerradaRechazaVentaYConservaSaldoCalculado(){
        var c=cajas.actual();BigDecimal esperado=cajas.saldo(c);cajas.cerrar();try{assertEquals(0,cajaRepo.findById(c.getId()).orElseThrow().getSaldoCierre().compareTo(esperado));assertThrows(ReglaNegocioException.class,()->ventas.crearVenta(new Venta(),ventaDetalle("1")));assertEquals(0,stock().compareTo(BigDecimal.TEN));assertThrows(ReglaNegocioException.class,()->cajas.abrir(BigDecimal.ZERO));}finally{jdbc.update("update caja_diaria set estado='ABIERTA',saldo_cierre=null,id_usuario_cierre=null,fecha_cierre=null where id_caja=?",c.getId());}
    }
    @Test void proveedorInactivoNoCompraPeroConservaHistorial(){
        var c=compra();compras.crearCompra(c,compraDetalle("1"));servicioProveedores.cambiarEstado(proveedorId,"Inactivo");assertThrows(ReglaNegocioException.class,()->compras.crearCompra(compra(),compraDetalle("1")));assertTrue(compraRepo.buscarCodigo(c.getId()).isPresent());assertEquals("Inactivo",proveedores.findById(proveedorId).orElseThrow().getEstado());
    }
    @Test void filtrosYDashboardLeenOperacionesPersistidas(){
        var c=compra();compras.crearCompra(c,compraDetalle("1"));assertEquals(1,compras.filtrar(proveedorId,3L,null,null,"Confirmada",codigo).size());
        assertEquals(0,compras.filtrar(proveedorId,1L,null,null,"Confirmada",codigo).size());
        var datos=dashboard.datos();assertEquals(5,((List<?>)datos.get("series")).size());
        var actual=(DashboardService.Dia)datos.get("actual");BigDecimal total=jdbc.queryForObject("select coalesce(sum(monto),0) from movimiento_caja where tipo='EGRESO' and date(fecha_hora)=current_date()",BigDecimal.class);assertEquals(0,total.compareTo(actual.getEgresos()));
        ReglasNegocioTest.autenticar("atorres","OPERADOR");assertTrue(((List<?>)dashboard.datos().get("series")).isEmpty());
    }
}
