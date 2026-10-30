# Archivos propuestos para revisión

Este manifiesto describe el árbol local revisado. El usuario autorizó expresamente el commit y el push normal a main.

Archivos con diferencias respecto de HEAD: 78. Archivos nuevos no ignorados: 75. Total propuesto: 153.

La publicación está autorizada con la prueba completa de reinicio del WAR pendiente, tal como consta en el informe 27.

## Configuración y ejecución (5)

- `.gitignore` — modificado
- `README.md` — modificado
- `compose.yaml` — nuevo
- `pom.xml` — modificado
- `src/main/resources/application.properties` — modificado

## Documentación y evidencias (18)

- `documentacion/20_explicacion_tecnica_modulos.md` — modificado
- `documentacion/24_auditoria_modelo_actualizado.md` — nuevo
- `documentacion/25_inventario_tecnico_y_rutas.md` — nuevo
- `documentacion/26_resultado_revision_20261030.md` — nuevo
- `documentacion/27_persistencia_real_y_validacion.md` — nuevo
- `documentacion/28_archivos_propuestos_publicacion.md` — nuevo
- `documentacion/evidencias/archivos_revision_20261030.txt` — nuevo
- `documentacion/evidencias/resultados_mysql_esquema.json` — nuevo
- `documentacion/evidencias/resultados_mysql_reinicio.json` — nuevo
- `documentacion/evidencias/resultados_persistencia_20261030.json` — nuevo
- `documentacion/evidencias/resultados_reinicio_aplicacion_bloqueado.json` — nuevo
- `documentacion/evidencias/resultados_revision_20261030.json` — nuevo
- `documentacion/evidencias/resultados_secretos_publicacion.json` — nuevo
- `documentacion/evidencias/verificar_esquema_mysql.py` — nuevo
- `documentacion/evidencias/verificar_reinicio_aplicacion.py` — nuevo
- `documentacion/evidencias/verificar_secretos_publicacion.py` — nuevo
- `documentacion/persistencia/README.md` — nuevo
- `documentacion/persistencia/V1__modelo_objetivo.sql` — nuevo

## Código Java y JPA (82)

- `src/main/java/com/example/nexo/asistencia/controller/AsistenciaController.java` — modificado
- `src/main/java/com/example/nexo/asistencia/model/Asistencia.java` — modificado
- `src/main/java/com/example/nexo/asistencia/repository/AsistenciaRepository.java` — nuevo
- `src/main/java/com/example/nexo/asistencia/service/AsistenciaService.java` — modificado
- `src/main/java/com/example/nexo/asistencia/service/AsistenciaServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/auth/config/AdministradorInicial.java` — nuevo
- `src/main/java/com/example/nexo/auth/config/CuentaVigenteFilter.java` — nuevo
- `src/main/java/com/example/nexo/auth/config/RegistroAcceso.java` — nuevo
- `src/main/java/com/example/nexo/auth/config/SecurityConfig.java` — modificado
- `src/main/java/com/example/nexo/auth/service/ActorActual.java` — nuevo
- `src/main/java/com/example/nexo/auth/service/NexoUserDetailsService.java` — modificado
- `src/main/java/com/example/nexo/auth/service/ResponsableSesion.java` — nuevo
- `src/main/java/com/example/nexo/categoria/controller/CategoriaController.java` — modificado
- `src/main/java/com/example/nexo/categoria/model/Categoria.java` — modificado
- `src/main/java/com/example/nexo/categoria/repository/CategoriaRepository.java` — nuevo
- `src/main/java/com/example/nexo/categoria/service/CategoriaService.java` — modificado
- `src/main/java/com/example/nexo/categoria/service/CategoriaServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/compra/controller/CompraController.java` — modificado
- `src/main/java/com/example/nexo/compra/model/Compra.java` — modificado
- `src/main/java/com/example/nexo/compra/model/DetalleCompra.java` — modificado
- `src/main/java/com/example/nexo/compra/repository/CompraRepository.java` — nuevo
- `src/main/java/com/example/nexo/compra/repository/DetalleCompraRepository.java` — nuevo
- `src/main/java/com/example/nexo/compra/service/CompraService.java` — modificado
- `src/main/java/com/example/nexo/compra/service/CompraServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/empleado/controller/EmpleadoController.java` — modificado
- `src/main/java/com/example/nexo/empleado/model/Empleado.java` — modificado
- `src/main/java/com/example/nexo/empleado/repository/EmpleadoRepository.java` — nuevo
- `src/main/java/com/example/nexo/empleado/service/EmpleadoService.java` — modificado
- `src/main/java/com/example/nexo/empleado/service/EmpleadoServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/finanzas/controller/CajaDiariaController.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/controller/FinanzasController.java` — modificado
- `src/main/java/com/example/nexo/finanzas/model/CajaDiaria.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/model/ConceptoMovimiento.java` — modificado
- `src/main/java/com/example/nexo/finanzas/model/MovimientoCaja.java` — modificado
- `src/main/java/com/example/nexo/finanzas/repository/CajaDiariaRepository.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/repository/ConceptoMovimientoRepository.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/repository/MovimientoCajaRepository.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/service/CajaDiariaService.java` — nuevo
- `src/main/java/com/example/nexo/finanzas/service/MovimientoCajaServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/inventario/controller/InventarioController.java` — modificado
- `src/main/java/com/example/nexo/inventario/model/MovimientoInventario.java` — modificado
- `src/main/java/com/example/nexo/inventario/repository/MovimientoInventarioRepository.java` — nuevo
- `src/main/java/com/example/nexo/inventario/service/MovimientoInventarioService.java` — modificado
- `src/main/java/com/example/nexo/inventario/service/MovimientoInventarioServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/producto/controller/ProductoController.java` — modificado
- `src/main/java/com/example/nexo/producto/model/Producto.java` — modificado
- `src/main/java/com/example/nexo/producto/repository/ProductoRepository.java` — nuevo
- `src/main/java/com/example/nexo/producto/service/ProductoService.java` — modificado
- `src/main/java/com/example/nexo/producto/service/ProductoServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/proveedor/controller/ProveedorController.java` — nuevo
- `src/main/java/com/example/nexo/proveedor/model/Proveedor.java` — nuevo
- `src/main/java/com/example/nexo/proveedor/repository/ProveedorRepository.java` — nuevo
- `src/main/java/com/example/nexo/proveedor/service/ProveedorService.java` — nuevo
- `src/main/java/com/example/nexo/shared/config/DatosDemostracion.java` — nuevo
- `src/main/java/com/example/nexo/shared/config/TiempoConfig.java` — nuevo
- `src/main/java/com/example/nexo/shared/controller/DashboardController.java` — nuevo
- `src/main/java/com/example/nexo/shared/controller/ErroresOperacion.java` — nuevo
- `src/main/java/com/example/nexo/shared/controller/HomeController.java` — modificado
- `src/main/java/com/example/nexo/shared/controller/PerfilVista.java` — nuevo
- `src/main/java/com/example/nexo/shared/persistence/Codigos.java` — nuevo
- `src/main/java/com/example/nexo/shared/persistence/TipoMovimientoConverter.java` — nuevo
- `src/main/java/com/example/nexo/shared/service/DashboardService.java` — nuevo
- `src/main/java/com/example/nexo/shared/service/OperacionCombustible.java` — nuevo
- `src/main/java/com/example/nexo/shared/validation/Importes.java` — nuevo
- `src/main/java/com/example/nexo/shared/validation/ReglaNegocioException.java` — nuevo
- `src/main/java/com/example/nexo/shared/validation/TextoNormalizado.java` — nuevo
- `src/main/java/com/example/nexo/shared/web/FormularioUnico.java` — nuevo
- `src/main/java/com/example/nexo/shared/web/LineasOperacion.java` — nuevo
- `src/main/java/com/example/nexo/usuario/controller/UsuarioController.java` — modificado
- `src/main/java/com/example/nexo/usuario/model/Rol.java` — nuevo
- `src/main/java/com/example/nexo/usuario/model/Usuario.java` — modificado
- `src/main/java/com/example/nexo/usuario/repository/RolRepository.java` — nuevo
- `src/main/java/com/example/nexo/usuario/repository/UsuarioRepository.java` — nuevo
- `src/main/java/com/example/nexo/usuario/service/UsuarioService.java` — modificado
- `src/main/java/com/example/nexo/usuario/service/UsuarioServiceImpl.java` — modificado
- `src/main/java/com/example/nexo/venta/controller/VentaController.java` — modificado
- `src/main/java/com/example/nexo/venta/model/DetalleVenta.java` — modificado
- `src/main/java/com/example/nexo/venta/model/Venta.java` — modificado
- `src/main/java/com/example/nexo/venta/repository/DetalleVentaRepository.java` — nuevo
- `src/main/java/com/example/nexo/venta/repository/VentaRepository.java` — nuevo
- `src/main/java/com/example/nexo/venta/service/VentaService.java` — modificado
- `src/main/java/com/example/nexo/venta/service/VentaServiceImpl.java` — modificado

## Migraciones y semilla optativa (5)

- `src/main/resources/db/demo.sql` — nuevo
- `src/main/resources/db/migration/V1__modelo_objetivo.sql` — nuevo
- `src/main/resources/db/migration/V2__catalogos_iniciales.sql` — nuevo
- `src/main/resources/db/migration/V3__indices_operativos.sql` — nuevo
- `src/main/resources/db/migration/V4__unicidad_conceptos.sql` — nuevo

## JSP y recursos de interfaz (37)

- `src/main/webapp/WEB-INF/views/asistencia/control.jsp` — modificado
- `src/main/webapp/WEB-INF/views/asistencia/mi.jsp` — modificado
- `src/main/webapp/WEB-INF/views/categoria/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/categoria/editar.jsp` — modificado
- `src/main/webapp/WEB-INF/views/categoria/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/compra/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/compra/detalle.jsp` — modificado
- `src/main/webapp/WEB-INF/views/compra/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/empleado/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/empleado/editar.jsp` — modificado
- `src/main/webapp/WEB-INF/views/empleado/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/concepto-crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/concepto-editar.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/conceptos.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/detalle.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/egresos.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/ingresos.jsp` — modificado
- `src/main/webapp/WEB-INF/views/finanzas/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/inventario/entrada.jsp` — modificado
- `src/main/webapp/WEB-INF/views/inventario/entradas.jsp` — modificado
- `src/main/webapp/WEB-INF/views/inventario/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/inventario/salida.jsp` — modificado
- `src/main/webapp/WEB-INF/views/inventario/salidas.jsp` — modificado
- `src/main/webapp/WEB-INF/views/producto/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/producto/editar.jsp` — modificado
- `src/main/webapp/WEB-INF/views/producto/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/proveedor/formulario.jsp` — nuevo
- `src/main/webapp/WEB-INF/views/proveedor/lista.jsp` — nuevo
- `src/main/webapp/WEB-INF/views/shared/dashboard.jsp` — nuevo
- `src/main/webapp/WEB-INF/views/shared/menu.jspf` — nuevo
- `src/main/webapp/WEB-INF/views/usuario/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/usuario/editar.jsp` — modificado
- `src/main/webapp/WEB-INF/views/usuario/lista.jsp` — modificado
- `src/main/webapp/WEB-INF/views/venta/crear.jsp` — modificado
- `src/main/webapp/WEB-INF/views/venta/detalle.jsp` — modificado
- `src/main/webapp/WEB-INF/views/venta/lista.jsp` — modificado
- `src/main/webapp/js/lineas.js` — nuevo

## Pruebas (6)

- `src/test/java/com/example/nexo/FormularioUnicoTest.java` — nuevo
- `src/test/java/com/example/nexo/JspHttpTest.java` — nuevo
- `src/test/java/com/example/nexo/PersistenciaMySqlTest.java` — nuevo
- `src/test/java/com/example/nexo/ReglasNegocioTest.java` — nuevo
- `src/test/java/com/example/nexo/SeguridadHttpTest.java` — nuevo
- `src/test/resources/application.properties` — nuevo

## Exclusiones verificadas

No se incluyen `target/`, WAR, clases compiladas, logs, copias locales ni archivos `.env`.
`target/migrar_modelos.py` y `target/migrar_catalogos.py` fueron auxiliares de transformación; no se necesitan para compilar el código fuente entregado y permanecen ignorados.
`target/nexo-test-config.json` conserva solo credenciales privadas de la instancia local de pruebas y está excluido de Git. Su contenido no aparece en este manifiesto ni en las evidencias publicables.

## Commit autorizado

`feat: completar integracion MySQL y persistencia de Estacion Nexo`

La autorización expresa permite publicar después de comprobar seguridad, integridad y sincronización remota. No se permite force-push.
