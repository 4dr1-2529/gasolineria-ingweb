# 21 · Matriz de correspondencia: código real ↔ funcionalidades ↔ interfaces ↔ reglas ↔ pruebas

Matriz completa de trazabilidad de la V2. La columna **Funcionalidad/Interfaz/Regla** proviene de la matriz oficial [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md) (38 F, 30 P, 47 asociaciones, RN01–RN05); las columnas **Ruta, Controller, Service, JSP y Prueba** provienen del código leído en `src/main/java/com/example/nexo/**` y `src/main/webapp/WEB-INF/views/**` y de la regresión ETAPA 10 (126/126 pruebas HTTP). Los 38 nombres de funcionalidad y las 30 interfaces existen intactos; F03, F33 y F34 conservan su estado real de fuera del alcance V2 (sólo V1).

Convención: «filtro S.S.» = la petición la procesa el motor de Spring Security (no hay método de controller).

| # | Funcionalidad | Interfaz | Ruta (método) | Controller · método | Service / ServiceImpl | JSP | RN | Prueba ETAPA 10 |
|---|---|---|---|---|---|---|---|---|
| F01 | Iniciar sesión | P02 | GET y POST /login | `AuthController.login` (GET; el POST lo procesa el filtro S.S. con `NexoUserDetailsService` + BCrypt) | `UsuarioService` / `UsuarioServiceImpl.buscarUsuarioPorUsername` | login.jsp | — | A01, A05–A09 |
| F02 | Cerrar sesión | P03 | POST /logout | filtro S.S. (LogoutFilter); el `form:form` vive en la cabecera de todas las vistas | — | cabecera de las 34 JSP | — | verificación directa: 302 `/login?logout`, sesión invalidada |
| F03 | Consultar dashboard | P03 | sin ruta en V2 | — | — | dashboard.html (sólo V1) | — | fuera del alcance V2 |
| F04 | Registrar categoría | P05, P22 | GET+POST /categorias/crear | `CategoriaController.mostrarFormularioCrear` / `crearCategoria` | `CategoriaService` / `CategoriaServiceImpl.crearCategoria` | categoria/crear.jsp | — | C01–C05 |
| F05 | Consultar categorías | P05 | GET /categorias/list | `CategoriaController.listarCategorias` | `CategoriaService.listaCategorias` | categoria/lista.jsp | — | S01, C05 |
| F06 | Consultar combustibles por categoría | P05 | GET /combustibles/list (columna categoría vía `nombresCategorias`) | `ProductoController.listarProductos` | `ProductoService.listaProductos` + `CategoriaService.listaCategorias` | producto/lista.jsp | — | S02, P05–P11 |
| F07 | Editar categoría | P05, P22 | GET+POST /categorias/editar | `CategoriaController.mostrarFormularioEditar` / `editarCategoria` | `CategoriaService.editarCategoria` | categoria/editar.jsp | RN02 | C06, C07 |
| F08 | Activar/desactivar categoría | P05, P22 | POST /categorias/estado | `CategoriaController.cambiarEstado` | `CategoriaService.cambiarEstadoCategoria` | categoria/lista.jsp | RN02 | C08 (+C09: eliminar → 404) |
| F09 | Registrar combustible | P07 | GET+POST /combustibles/crear | `ProductoController.mostrarFormularioCrear` / `crearProducto` | `ProductoService.crearProducto` | producto/crear.jsp | — | P01–P05 |
| F10 | Consultar combustibles | P06 | GET /combustibles/list | `ProductoController.listarProductos` | `ProductoService.listaProductos` | producto/lista.jsp | — | S02, P05–P11 |
| F11 | Editar combustible | P07 | GET+POST /combustibles/editar | `ProductoController.mostrarFormularioEditar` / `editarProducto` | `ProductoService.editarProducto` | producto/editar.jsp | RN02 | P06 |
| F12 | Activar/desactivar combustible | P06 | POST /combustibles/estado | `ProductoController.cambiarEstado` | `ProductoService.cambiarEstadoProducto` | producto/lista.jsp | RN02 | P07, P08 (+P08: eliminar → 404) |
| F13 | Registrar compra de combustible | P20, P23 | GET+POST /compras/crear | `CompraController.mostrarFormulario` / `crearCompra` | `CompraService` / `CompraServiceImpl.crearCompra(compra, detalle, fecha)` | compra/crear.jsp | RN03, RN04 | D02–D08 |
| F14 | Consultar compras | P20 | GET /compras/list | `CompraController.listarCompras` | `CompraService.listaCompras` | compra/lista.jsp | RN03, RN04 | D01, D08, D11 |
| F15 | Consultar detalle de compra | P21 | GET /compras/detalle | `CompraController.mostrarDetalle` | `CompraService.listaDetallesPorCompra` | compra/detalle.jsp | RN03, RN04 | D09 |
| F16 | Registrar entrada de combustible | P12 | GET+POST /inventario/entrada/crear (ADMIN); entrada automática al confirmar F13 | `InventarioController.mostrarFormularioEntrada` / `crearEntrada` | `MovimientoInventarioService.registrarEntrada` | inventario/entrada.jsp | RN01, RN03 | I02–I04 (+D08: una entrada por línea) |
| F17 | Registrar salida de combustible | P13 | GET+POST /inventario/salida/crear (OPERADOR/EMPLEADO); salida automática al confirmar F20 | `InventarioController.mostrarFormularioSalida` / `crearSalida` | `MovimientoInventarioService.registrarSalida` | inventario/salida.jsp | RN01 | I05–I08 (+V06: una salida por venta) |
| F18 | Consultar existencias | P08, P11, P13 | GET /inventario/list | `InventarioController.listarInventario` | `MovimientoInventarioService.listaMovimientos` + `ProductoService.listaProductos` | inventario/lista.jsp | RN01 | S05, I01 |
| F19 | Consultar movimientos de inventario | P14 | GET /inventario/entradas y GET /inventario/salidas | `InventarioController.listarEntradas` / `listarSalidas` (comparten el privado `mostrarMovimientos`) | `MovimientoInventarioService.listaEntradas` / `listaSalidas` | inventario/entradas.jsp, inventario/salidas.jsp | RN01, RN03 | consulta incluida en I01 (el libro de /inventario/list usa el mismo servicio) |
| F20 | Registrar venta | P08, P24 | GET+POST /ventas/crear (OPERADOR/EMPLEADO) | `VentaController.mostrarFormulario` / `crearVenta` | `VentaService` / `VentaServiceImpl.crearVenta(venta, detalle)` | venta/crear.jsp | RN01, RN02, RN03, RN04 | V02–V06 |
| F21 | Consultar ventas | P09 | GET /ventas/list | `VentaController.listarVentas` | `VentaService.listaVentas` | venta/lista.jsp | RN03, RN04 | V01, V09 |
| F22 | Consultar detalle de venta | P10 | GET /ventas/detalle | `VentaController.mostrarDetalle` | `VentaService.listaDetallesPorVenta` | venta/detalle.jsp | RN03, RN04 | V07 |
| F23 | Registrar concepto económico | P27 | GET+POST /finanzas/conceptos/crear | `FinanzasController.mostrarFormularioCrearConcepto` / `crearConcepto` | `MovimientoCajaService.crearConcepto` | finanzas/concepto-crear.jsp | — | K01–K03 |
| F24 | Consultar conceptos económicos | P17 | GET /finanzas/conceptos/list | `FinanzasController.listarConceptos` | `MovimientoCajaService.listaConceptos` | finanzas/conceptos.jsp | — | K04–K06, K09 |
| F25 | Editar/activar/desactivar concepto económico | P27 | GET+POST /finanzas/conceptos/editar; POST /finanzas/conceptos/estado | `FinanzasController.editarConcepto` / `cambiarEstadoConcepto` | `MovimientoCajaService.editarConcepto` / `cambiarEstadoConcepto` | finanzas/concepto-editar.jsp | RN02 | K04–K06 (+K06: eliminar → 404) |
| F26 | Consultar ingresos de caja | P16 | GET /finanzas/ingresos | `FinanzasController.listarIngresos` | `MovimientoCajaService.listaIngresos` | finanzas/ingresos.jsp | RN04 | F02 |
| F27 | Consultar egresos de caja | P16 | GET /finanzas/egresos | `FinanzasController.listarEgresos` | `MovimientoCajaService.listaEgresos` | finanzas/egresos.jsp | RN04 | F03 |
| F28 | Consultar movimientos y saldo de caja | P15, P30 | GET /finanzas/list; GET /finanzas/detalle | `FinanzasController.resumen` / `detalle` | `MovimientoCajaService.apertura/totalIngresos/totalEgresos/saldoActual` | finanzas/lista.jsp, finanzas/detalle.jsp | RN04 | S06, F01, F04, F06 |
| F29 | Registrar empleado | P25 | GET+POST /empleados/crear | `EmpleadoController.mostrarFormularioCrear` / `crearEmpleado` | `EmpleadoService.crearEmpleado` | empleado/crear.jsp | — | E01–E04 |
| F30 | Consultar empleados | P18 | GET /empleados/list | `EmpleadoController.listarEmpleados` | `EmpleadoService.listaEmpleados` | empleado/lista.jsp | — | S03 |
| F31 | Editar/activar/desactivar empleado | P25 | GET+POST /empleados/editar; POST /empleados/estado | `EmpleadoController.editarEmpleado` / `cambiarEstado` | `EmpleadoService.editarEmpleado` / `cambiarEstadoEmpleado` | empleado/editar.jsp | RN02 | E05–E07 (+E07: eliminar → 404) |
| F32 | Gestionar usuarios | P19, P26 | GET+POST /usuarios/crear y /usuarios/editar | `UsuarioController.crearUsuario` / `editarUsuario` | `UsuarioService` / `UsuarioServiceImpl` (+ `NexoUserDetailsService` para el acceso) | usuario/crear.jsp, usuario/editar.jsp, usuario/lista.jsp | RN02 | U01–U15 (+U12: eliminar → 404) |
| F33 | Consultar la portada pública | P01 | sin ruta en V2 | — | — | index.html (sólo V1) | — | fuera del alcance V2 |
| F34 | Enviar mensaje de contacto | P04 | sin ruta en V2 | — | — | contacto.html (sólo V1) | — | fuera del alcance V2 |
| F35 | Registrar asistencia | P28 | POST /asistencia/entrada | `AsistenciaController.registrarEntrada` (identidad vía `idEmpleadoDeLaSesion`) | `AsistenciaService.registrarEntrada` | asistencia/mi.jsp | RN05 | AS02, AS03 |
| F36 | Consultar mi asistencia | P28 | GET /asistencia/mi | `AsistenciaController.mostrarMiAsistencia` | `AsistenciaService.listaAsistenciasDeEmpleado` | asistencia/mi.jsp | RN05 | AS01 |
| F37 | Consultar mi resumen de asistencia | P28 | GET /asistencia/mi (`asistenciaHoy`, `fechaHoy`) | `AsistenciaController.mostrarMiAsistencia` | `AsistenciaService` (estado del día: «Falta»/«Presente») | asistencia/mi.jsp | RN05 | AS01–AS06 |
| F38 | Consultar asistencia del personal | P29 | GET /asistencia/control (ADMIN) | `AsistenciaController.mostrarControlAsistencia` | `AsistenciaService.listaAsistencias` | asistencia/control.jsp | RN05 | AS07, AS08, AS10 |

## Comprobaciones de cierre de esta matriz

- **38 filas F** — F01 a F38, ninguna eliminada; F03, F33 y F34 marcadas como fuera del alcance V2 con su estado real (definidas y maquetadas en V1).
- **Interfaces citadas**: P01–P30 sin alterar (las 30 de [03_interfaces.md](03_interfaces.md)).
- **Reglas**: 24 funcionalidades con al menos una regla y 14 sin restricción de negocio, tal como en la matriz oficial; RN01–RN05 con casos de cumplimiento y violación probados (bloques C/P/E/U/K/D/V/I/F/AS y cierre R01–R05).
- **Cobertura de pruebas**: 35/38 funcionalidades con pruebas HTTP reales en ETAPA 10; F02 con verificación directa de logout; F03/F33/F34 no tienen implementación V2 que probar (por diseño).

*Verificación de los conteos (38 F, 30 P, 5 RN, 47 asociaciones) realizada el 09/10/2026 sobre los archivos originales; ver informe [22_informe_regresion_etapa10.md](22_informe_regresion_etapa10.md).*
