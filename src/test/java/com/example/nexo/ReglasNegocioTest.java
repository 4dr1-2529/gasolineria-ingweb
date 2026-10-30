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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import com.example.nexo.asistencia.model.Asistencia;
import com.example.nexo.asistencia.repository.AsistenciaRepository;
import com.example.nexo.asistencia.service.AsistenciaServiceImpl;
import com.example.nexo.auth.service.ActorActual;
import com.example.nexo.categoria.model.Categoria;
import com.example.nexo.categoria.service.CategoriaServiceImpl;
import com.example.nexo.compra.model.*;
import com.example.nexo.compra.service.CompraServiceImpl;
import com.example.nexo.empleado.model.Empleado;
import com.example.nexo.empleado.repository.EmpleadoRepository;
import com.example.nexo.inventario.model.MovimientoInventario;
import com.example.nexo.inventario.service.MovimientoInventarioServiceImpl;
import com.example.nexo.producto.model.Producto;
import com.example.nexo.producto.service.ProductoServiceImpl;
import com.example.nexo.usuario.model.Usuario;
import com.example.nexo.usuario.repository.*;
import com.example.nexo.venta.model.*;
import com.example.nexo.venta.service.VentaServiceImpl;
import com.example.nexo.shared.validation.ReglaNegocioException;
@SpringBootTest @ActiveProfiles("demo") @Transactional
class ReglasNegocioTest {
 @Autowired CategoriaServiceImpl categorias; @Autowired ProductoServiceImpl productos;
 @Autowired EmpleadoRepository empleados; @Autowired UsuarioRepository usuarios; @Autowired RolRepository roles;
 @Autowired AsistenciaRepository asistencias; @Autowired ActorActual actor;
 @Autowired MovimientoInventarioServiceImpl inventario; @Autowired VentaServiceImpl ventas; @Autowired CompraServiceImpl compras;
 @Autowired AsistenciaServiceImpl asistenciaReal;
 @Autowired org.springframework.jdbc.core.JdbcTemplate jdbc;
 Long empleadoId;String cuenta;
 static void autenticar(String nombre,String rol){SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(nombre,"",List.of(new SimpleGrantedAuthority("ROLE_"+rol))));}
 @BeforeEach void preparar(){
  autenticar("ediaz","ADMIN");
  String sufijo=UUID.randomUUID().toString().replace("-","").substring(0,10);cuenta="test"+sufijo;
  var e=new Empleado(null,String.format("%08d",80000000+new java.security.SecureRandom().nextInt(19000000)),"Persona","Prueba","Vendedor","000 000 000","Activo");empleadoId=empleados.saveAndFlush(e).getId();
  var u=new Usuario(null,empleadoId,cuenta,usuarios.findByUsername("lrojas").orElseThrow().getPassword(),"Vendedor","Activo");u.setEmpleado(e);u.setPerfil(roles.findByNombre("Vendedor").orElseThrow());usuarios.saveAndFlush(u);
 }
 @AfterEach void limpiar(){
  SecurityContextHolder.clearContext();
  if(!TransactionSynchronizationManager.isActualTransactionActive()){
   jdbc.update("delete from asistencia where id_empleado=?",empleadoId);jdbc.update("delete from usuario where username=?",cuenta);jdbc.update("delete from empleado where id_empleado=?",empleadoId);
  }
 }
 AsistenciaServiceImpl asistencia(String instante){autenticar(cuenta,"VENDEDOR");return new AsistenciaServiceImpl(asistencias,empleados,actor,Clock.fixed(Instant.parse(instante),ZoneOffset.UTC));}
 Venta venta(){return new Venta(null,2L,null,null,null);}
 DetalleVenta detalle(String cantidad){return new DetalleVenta(null,null,"PR01",new BigDecimal(cantidad),null,null);}
 Compra compra(){var c=new Compra();c.setIdProveedor(1L);return c;}
 @Test void nombresEquivalentesNoDuplicanCategorias(){long n=categorias.listaCategorias().size();assertNotNull(categorias.crearCategoria(new Categoria(null,"  ＧＡＳＯＬＩＮＡＳ\u00a0 ","","Activo")));assertEquals(n,categorias.listaCategorias().size());}
 @Test void productoPuedeRepetirNombreEnOtraCategoria(){assertNull(productos.crearProducto(new Producto(null,2L,"Gasolina Regular","L",new BigDecimal("5"),BigDecimal.ZERO,"Activo")));}
 @Test void duplicadoDentroDeCategoriaSeRechaza(){assertNotNull(productos.crearProducto(new Producto(null,1L," GASOLINA   REGULAR ","L",new BigDecimal("5"),BigDecimal.ZERO,"Activo")));}
 @Test void categoriaInactivaBloqueaVentaSinEfectos(){long n=ventas.listaVentas().size(),m=inventario.listaMovimientos().size();var stock=productos.buscarProductoPorId("PR01").getStock();categorias.cambiarEstadoCategoria(1L,"Inactivo");assertThrows(ReglaNegocioException.class,()->ventas.crearVenta(venta(),detalle("10")));assertEquals(0,stock.compareTo(productos.buscarProductoPorId("PR01").getStock()));assertEquals(n,ventas.listaVentas().size());assertEquals(m,inventario.listaMovimientos().size());}
 @Test void categoriaInactivaBloqueaCompraYMovimiento(){long n=compras.listaCompras().size();categorias.cambiarEstadoCategoria(1L,"Inactivo");assertThrows(ReglaNegocioException.class,()->compras.crearCompra(compra(),new DetalleCompra(null,null,"PR01",BigDecimal.TEN,BigDecimal.ONE,null)));assertThrows(ReglaNegocioException.class,()->inventario.registrarEntrada(new MovimientoInventario(null,"PR01",1L,"Entrada",BigDecimal.TEN,null,"Ajuste de prueba")));assertEquals(n,compras.listaCompras().size());}
 @Test void noPuedeCambiarProductoHaciaCategoriaInactiva(){categorias.cambiarEstadoCategoria(2L,"Inactivo");assertNotNull(productos.editarProducto(new Producto("PR01",2L,"Gasolina Regular","L",new BigDecimal("5"),new BigDecimal("1990"),"Activo")));assertEquals(1L,productos.buscarProductoPorId("PR01").getIdCategoria());}
 @Test void stockInsuficienteNoCreaVenta(){long n=ventas.listaVentas().size();var stock=productos.buscarProductoPorId("PR01").getStock();assertThrows(ReglaNegocioException.class,()->ventas.crearVenta(venta(),detalle(stock.add(BigDecimal.ONE).toPlainString())));assertEquals(n,ventas.listaVentas().size());assertEquals(0,stock.compareTo(productos.buscarProductoPorId("PR01").getStock()));}
 @Test void ventaConservaMilesimasDeLitro(){var stock=productos.buscarProductoPorId("PR01").getStock();assertNull(ventas.crearVenta(venta(),detalle("1.125")));assertEquals(0,stock.subtract(new BigDecimal("1.125")).compareTo(productos.buscarProductoPorId("PR01").getStock()));}
 @Test void rechazaPrecisionExcesivaSinRedondear(){assertThrows(ReglaNegocioException.class,()->ventas.crearVenta(venta(),detalle("1.1234")));assertThrows(ReglaNegocioException.class,()->compras.crearCompra(compra(),new DetalleCompra(null,null,"PR01",BigDecimal.TEN,new BigDecimal("1.001"),null)));}
 @Test void fechaDeMovimientoSeGeneraEnServicio(){var m=new MovimientoInventario(null,"PR01",1L,"Salida",BigDecimal.ONE,LocalDateTime.of(2000,1,1,0,0),"Ajuste de prueba");assertNull(inventario.registrarSalida(m));assertEquals(LocalDate.now(com.example.nexo.shared.config.TiempoConfig.LIMA),m.getFechaHora().toLocalDate());assertEquals(3L,m.getIdUsuario());}
 @Test void jornadaUsaFechaLimaAunqueRelojUseUtc(){var s=asistencia("2026-11-11T02:00:00Z");s.registrarEntrada(empleadoId);var a=s.buscarAsistenciaAbierta(empleadoId);assertEquals(LocalDate.of(2026,11,10),a.getFecha());assertEquals(LocalTime.of(21,0),a.getHoraEntrada());}
 @Test void segundaEntradaNoDuplicaJornada(){var s=asistencia("2026-11-10T14:00:00Z");s.registrarEntrada(empleadoId);assertTrue(s.registrarEntrada(empleadoId).contains("abierta"));assertEquals(1,s.listaAsistenciasDeEmpleado(empleadoId).size());}
 @Test void jornadaAnteriorAbiertaBloqueaNuevaEntrada(){var s=asistencia("2026-11-10T14:00:00Z");var a=new Asistencia(null,empleadoId,LocalDate.of(2026,11,9),LocalTime.of(8,0),null,"Presente",null);a.setEmpleado(empleados.findById(empleadoId).orElseThrow());asistencias.saveAndFlush(a);assertTrue(s.registrarEntrada(empleadoId).contains("abierta"));assertTrue(s.registrarSalida(empleadoId).contains("otra fecha"));assertNull(s.buscarAsistenciaAbierta(empleadoId).getHoraSalida());}
 @Test void empleadoInactivoNoMarcaEntradaNiSalida(){var s=asistencia("2026-11-10T14:00:00Z");s.registrarEntrada(empleadoId);empleados.findById(empleadoId).orElseThrow().setEstado("Inactivo");empleados.flush();assertThrows(org.springframework.security.access.AccessDeniedException.class,()->s.registrarEntrada(empleadoId));assertThrows(org.springframework.security.access.AccessDeniedException.class,()->s.registrarSalida(empleadoId));assertNull(s.buscarAsistenciaAbierta(empleadoId).getHoraSalida());}
 @Test void salidaSinEntradaYSinAvanceHorarioSeRechazan(){var s=asistencia("2026-11-10T14:00:00Z");assertTrue(s.registrarSalida(empleadoId).contains("No hay"));s.registrarEntrada(empleadoId);assertTrue(s.registrarSalida(empleadoId).contains("posterior"));}
 @Test @Transactional(propagation=Propagation.NOT_SUPPORTED)
 void entradasConcurrentesNoDuplicanJornada() throws Exception {
  var pool=Executors.newFixedThreadPool(8);try {var tareas=new ArrayList<Callable<String>>();for(int i=0;i<24;i++)tareas.add(()->{autenticar(cuenta,"VENDEDOR");try{return asistenciaReal.registrarEntrada(empleadoId);}finally{SecurityContextHolder.clearContext();}});for(var f:pool.invokeAll(tareas))f.get();assertEquals(1,asistencias.findByEmpleadoIdOrderByFechaDesc(empleadoId).size());}finally{pool.shutdownNow();}
 }
}
