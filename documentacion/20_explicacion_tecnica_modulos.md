# 20 · Explicación técnica por módulo (sustentación V2)

Documento de apoyo para la sustentación: describe cada módulo con los **nombres reales del código** (archivos, clases, métodos, rutas, atributos y vistas) y explica cómo se carga una interfaz desde que el usuario escribe una URL hasta que el navegador recibe el HTML. Todo lo aquí afirmado fue verificado leyendo el código y ejecutando la regresión de la ETAPA 10 (126/126 pruebas).

---

## 1. Ciclo completo de una petición (URL → HTML)

1. **El navegador** escribe `http://localhost:8080/categorias/list` o pulsa un enlace `<a>`.
2. **Tomcat** (embebido en Spring Boot, puerto 8080, context path `/`) recibe la petición y la entrega al servlet raíz de la aplicación.
3. **La cadena de filtros de Spring Security** (`SecurityFilterChain` del bean `SecurityConfig.cadenaFiltros`, en `src/main/java/com/example/nexo/config/SecurityConfig.java`) se ejecuta antes que nada:
   - **Autenticación**: si la ruta no está permitida para anónimos y no hay sesión válida, `ExceptionTranslationFilter` redirige a `/login` (302). Si la sesión existe, se reconstruye el `Authentication` desde la cookie `JSESSIONID`.
   - **Autorización**: `authorizeHttpRequests` compara la ruta con los `requestMatchers` (ver §2). Si el rol no alcanza, responde 403 con la página de aviso (forward a `/sin-permisos` → `error/permisos.jsp`).
   - **CSRF**: `CsrfFilter` exige el token `_csrf` en todo POST; sin él responde **403** (pruebas C10, P09, E09, U13, K08, D11, V10, I11, AS09).
4. **`DispatcherServlet`** (se inicializa solo en la primera petición, como se ve en el log) busca el handler con `HandlerMapping`: coincide la anotación `@RequestMapping("/categorias")` del `CategoriaController` más `@GetMapping("/list")`.
5. **El controller** ejecuta el método, pidiendo datos al **service** (interfaz) cuya implementación es el `ServiceImpl` correspondiente. El `ServiceImpl` trabaja sobre una `List<T>` en memoria (los datos de la semilla).
6. El controller coloca los datos en el **`Model`** (`model.addAttribute(...)`) y devuelve el **nombre de vista** `"categoria/lista"`.
7. **`InternalResourceViewResolver`** (configuración por convención de Spring Boot para JSP: prefijo `/WEB-INF/views/`, sufijo `.jsp`) convierte el nombre en el forward `/WEB-INF/views/categoria/lista.jsp`.
8. **El JSP** se ejecuta en Tomcat: usa JSTL (`c:forEach`, `c:out`, `fmt:formatNumber`, `fn:length`) para recorrer el modelo y produce el HTML final.
9. **El navegador** recibe el HTML con la tabla ya poblada desde la memoria del servidor.

**En POST** el camino añade dos pasos: el filtro CSRF valida el token de la sesión, y si el `ServiceImpl` devuelve un mensaje de error, el controller hace `redirect:` con atributos *flash* (el objeto digitado y el mensaje), el navegador repite el GET del formulario y el JSP repuebla los campos (conservación de lo digitado).

**Cierre de sesión**: el formulario `form:form action="/logout"` de la cabecera de todas las vistas envía POST con token; el `LogoutFilter` de Spring Security invalida la sesión y redirige a `/login?logout` (verificado en vivo: después del logout, `/ventas/list` responde 302 a `/login`).

---

## 2. Seguridad: qué rol puede abrir cada ruta

Definido en `SecurityConfig.cadenaFiltros`:

| Regla | Rutas | Resultado comprobado |
|---|---|---|
| Permitido sin sesión | `/css/**`, `/login`, `/error`, `/WEB-INF/**` | A01 (200) |
| Anónimo en ruta protegida | cualquier otra | 302 a `/login` (A02, A03, A04) |
| **ADMIN** | `/categorias/**`, `/combustibles/crear|editar|estado`, `/inventario/entrada/crear`, `/empleados/**`, `/usuarios/**`, `/compras/**`, `/finanzas/**`, `/asistencia/control` | Operador recibe 403 con aviso (C11, P10, E08, U02/U14, D10, K07, F05, AS08) |
| **OPERADOR o EMPLEADO** | `/ventas/crear`, `/inventario/salida/crear` | El Administrador recibe 403 (V08, I10) |
| Resto autenticado | `/ventas/list`, `/compras/list`, `/inventario/**` de consulta, `/asistencia/mi`, etc. | 200 para cualquier sesión válida (V09) |
| Inicio según rol | `/` → `HomeController.inicio` | Admin → `/categorias/list`; staff → `/ventas/list` (A07, A08) |

El login (`POST /login`) lo procesa el filtro propio de Spring Security (`UsernamePasswordAuthenticationFilter`), no un controller. La autenticación usa `NexoUserDetailsService.loadUserByUsername` → `UsuarioService.buscarUsuarioPorUsername` y valida la contraseña con `BCryptPasswordEncoder.matches` contra el hash guardado en `UsuarioServiceImpl`. Contraseña mala → redirect `/login?error=credenciales` (A06); cuenta inactiva → `/login?error=inactivo` (U11); el propio `/login` también exige CSRF (A05).

---

## 3. Módulo por módulo

### 3.1 Acceso — F01, F02 (P02, P03)

- **URL**: `GET/POST /login`, `POST /logout`, `GET /`.
- **Controller**: `AuthController.login(@RequestParam error, @RequestParam mensaje, Model)` → vista `login` con `model:error`; `AuthController.sinPermisos()` (`@RequestMapping("/sin-permisos")`, acepta GET y POST) → vista `error/permisos`; `HomeController.inicio(Authentication)` → `redirect:/categorias/list` o `redirect:/ventas/list` según el rol del `Authentication`.
- **Service**: `UsuarioService` / `UsuarioServiceImpl` (usuarios en memoria) y `NexoUserDetailsService` (puente hacia Spring Security).
- **Validación**: la realza Spring Security (usuario existe, cuenta activa, BCrypt correcto). El JSP `login.jsp` añade validaciones nativas HTML5 (`required`, `minlength`, `maxlength`).
- **Colección**: `UsuarioServiceImpl.usuarios` (semilla: `atorres`, `lrojas`, `ediaz`).
- **Vista**: `login.jsp` (pública) y `error/permisos.jsp` («Esta sección no te corresponde»).
- **Pruebas ETAPA 10**: A01–A09 y verificación directa de logout.

### 3.2 Categorías — F04–F08 (P05, P22) · ADMIN

- **URL**: `GET /categorias/list`, `GET/POST /categorias/crear`, `GET/POST /categorias/editar?id=`, `POST /categorias/estado`. **No existe** `/categorias/eliminar` → 404 (C09).
- **Controller** (`CategoriaController`): `listarCategorias`, `mostrarFormularioCrear`, `crearCategoria`, `mostrarFormularioEditar`, `editarCategoria`, `cambiarEstado`.
- **Service/Impl**: `CategoriaService` / `CategoriaServiceImpl` — `listaCategorias`, `crearCategoria`, `editarCategoria`, `cambiarEstadoCategoria`, `buscarCategoriaPorId`.
- **Validaciones** (devueltas como mensaje y mostradas como `notice error`): «El nombre de la categoría es obligatorio», «debe tener entre 3 y 40 caracteres», «La descripción no puede superar los 200 caracteres», «El estado debe ser Activo o Inactivo», «Ya existe una categoría con el nombre …» (C01–C03).
- **Colección**: `List<Categoria> categorias` — semilla: *Gasolinas*, *Diésel*.
- **Model**: `categorias` (lista), `categoria` (objeto del formulario), `mensaje`, `mensajeTipo`.
- **JSP**: `categoria/lista.jsp` («N categorías en memoria», botón Cambiar estado, RN02), `categoria/crear.jsp`, `categoria/editar.jsp`.
- **Respuesta**: error → `redirect:/categorias/crear` conservando lo digitado (C04); éxito → `redirect:/categorias/list` con «La categoría se registró/editó correctamente».
- **Pruebas ETAPA 10**: C01–C12.

### 3.3 Combustibles — F09–F12 y F06 (P06, P07) · crear/editar/estado = ADMIN

- **URL**: `GET /combustibles/list` (cualquier sesión), `GET/POST /combustibles/crear`, `GET/POST /combustibles/editar?id=`, `POST /combustibles/estado`. Sin `/combustibles/eliminar` → 404 (P08).
- **Controller** (`ProductoController`): `listarProductos`, `mostrarFormularioCrear`, `crearProducto`, `mostrarFormularioEditar`, `editarProducto`, `cambiarEstado`.
- **Service/Impl**: `ProductoService` / `ProductoServiceImpl` — `listaProductos`, `crearProducto`, `editarProducto`, `cambiarEstadoProducto`, `buscarProductoPorId`.
- **Validaciones**: nombre obligatorio y único («Ya existe un combustible con el nombre …»), `idCategoria` debe existir, `unidadMedida` debe ser exactamente «Litro», `precioActual` > 0 (P01–P04). El id se autogenera: el cuarto producto es `PR04`.
- **Model**: `productos`, `categorias`, `nombresCategorias` (mapa id→nombre que muestra la columna categoría de la lista → F06), `producto`, `mensaje`, `mensajeTipo`.
- **JSP**: `producto/lista.jsp`, `producto/crear.jsp`, `producto/editar.jsp`.
- **RN02 y selectores**: un producto desactivado se conserva en el catálogo con badge *Inactivo* y **desaparece de los selectores** de venta: `GET /ventas/crear` ya no ofrece «Nafta ETAPA10» (P07).
- **Pruebas ETAPA 10**: P01–P11.

### 3.4 Empleados — F29–F31 (P18, P25) · ADMIN

- **URL**: `GET /empleados/list`, `GET/POST /empleados/crear`, `GET/POST /empleados/editar?id=`, `POST /empleados/estado`. Sin eliminar → 404 (E07).
- **Controller** (`EmpleadoController`): `listarEmpleados`, `mostrarFormularioCrear`, `crearEmpleado`, `mostrarFormularioEditar`, `editarEmpleado`, `cambiarEstado`.
- **Service/Impl**: `EmpleadoService` / `EmpleadoServiceImpl` — `crearEmpleado`, `editarEmpleado`, `cambiarEstadoEmpleado`.
- **Validaciones**: «El DNI debe tener exactamente 8 dígitos», «Ya existe un empleado con el DNI …», «Los nombres/apellidos deben tener entre 3 y 60 caracteres», «El teléfono debe seguir el formato 000 000 000» (E01–E05).
- **Colección**: `List<Empleado> empleados` — semilla: 00000001 Ana Torres, 00000002 Luis Rojas, 00000003 Elena Díaz.
- **Model / JSP**: `empleados`, `empleado` → `empleado/lista.jsp`, `empleado/crear.jsp`, `empleado/editar.jsp`.
- **Pruebas ETAPA 10**: E01–E09.

### 3.5 Usuarios — F32 (P19, P26) · ADMIN

- **URL**: `GET /usuarios/list`, `GET/POST /usuarios/crear`, `GET/POST /usuarios/editar?id=`. La baja se hace con `estado=Inactivo` en editar (F32); no hay `/usuarios/eliminar` → 404 (U12).
- **Controller** (`UsuarioController`): `listarUsuarios`, `mostrarFormularioCrear`, `crearUsuario`, `mostrarFormularioEditar`, `editarUsuario`.
- **Service/Impl**: `UsuarioService` / `UsuarioServiceImpl` — `crearUsuario`, `editarUsuario`, `buscarUsuarioPorUsername`.
- **Validaciones**: username de 4–20 y único («El nombre de usuario «…» ya está registrado»), contraseña obligatoria de 8–40 **al crear**, «El rol seleccionado no es válido» — el rol se valida contra el catálogo fijo {Administrador, Operador / Vendedor, Empleado}, lo que **impide elevar el rol manipulando el formulario** (U05), «Debe seleccionar el empleado», «Ese empleado ya tiene una cuenta de usuario» (U03–U07).
- **Contraseña**: se guarda como hash BCrypt; **editar con la contraseña en blanco conserva el hash** (U09: la cuenta sigue abriendo sesión tras editarla sin clave). `NexoUserDetailsService` traduce el rol del catálogo a `ROLE_ADMIN` / `ROLE_OPERADOR` / `ROLE_EMPLEADO`.
- **Model / JSP**: `usuarios`, `nombresEmpleados`, `empleados`, `usuario` → `usuario/lista.jsp`, `usuario/crear.jsp`, `usuario/editar.jsp`.
- **Pruebas ETAPA 10**: U01–U15.

### 3.6 Compras — F13–F15 (P20, P21, P23) · ADMIN

- **URL**: `GET /compras/list`, `GET/POST /compras/crear`, `GET /compras/detalle?id=`.
- **Controller** (`CompraController`): `listarCompras`, `mostrarFormulario`, `crearCompra`, `mostrarDetalle`.
- **Service/Impl**: `CompraService` / `CompraServiceImpl` — `crearCompra(Compra, DetalleCompra, LocalDate)`, `listaCompras`, `listaDetallesPorCompra`.
- **Validaciones**: «La fecha de la compra es obligatoria», «El proveedor debe tener entre 3 y 60 caracteres», «El producto seleccionado no existe/está inactivo: sólo los combustibles activos pueden comprarse», «La cantidad de litros y el precio de compra son obligatorios», «… deben ser mayor que cero», «El responsable seleccionado no existe/está inactivo: elija una cuenta activa» (D02–D05).
- **Efectos atómicos** (RN03/RN04, D08): si todo es válido, `crearCompra` genera **la compra** (C002), **una entrada de inventario por cada línea** y **un único egreso de caja** por el total (concepto CE02 «Compra de combustible»). Si algo falla, **no se crea nada** (D07: sin línea no hay entrada).
- **Model**: `compras`, `detalles`, `combustiblesPorCompra`, `litrosPorCompra`, `totalMonto`, `totalLitros`, `productos`, `responsables`, `compra`, `detalle`, `fechaCompra` → `compra/lista.jsp`, `compra/crear.jsp`, `compra/detalle.jsp`.
- **Pruebas ETAPA 10**: D01–D11.

### 3.7 Ventas — F20–F22 (P08, P09, P10, P24) · crear = OPERADOR/EMPLEADO

- **URL**: `GET /ventas/list` (cualquier sesión), `GET/POST /ventas/crear`, `GET /ventas/detalle?id=`.
- **Controller** (`VentaController`): `listarVentas`, `mostrarFormulario`, `crearVenta`, `mostrarDetalle`.
- **Service/Impl**: `VentaService` / `VentaServiceImpl` — `crearVenta(Venta, DetalleVenta)`, `listaDetallesPorVenta`.
- **Validaciones**: producto existe y activo («… sólo los combustibles activos pueden venderse»), «La cantidad de litros es obligatoria», **RN01** «No hay stock suficiente» (V03: 99999 L rechazados con el stock intacto), «El operador seleccionado está inactivo: elija una cuenta activa» (V04, V05).
- **Efectos atómicos** (RN03/RN04, V06): venta **V004**, **una única salida** en el libro de inventario y **un único ingreso** de caja (concepto CE01), con el descuento exacto del stock (2090 → 2080 L). Un rechazo no deja efectos parciales (V02: ventas/libro/caja sin mover).
- **Model**: `ventas`, `detalles`, `combustiblesPorVenta`, `litrosPorVenta`, `operadores`, `totalMonto`, `totalLitros`, `venta`, `detalle`, `productos` → `venta/lista.jsp`, `venta/crear.jsp`, `venta/detalle.jsp`.
- **Pruebas ETAPA 10**: V01–V11.

### 3.8 Inventario — F16–F19 (P08, P11, P12, P13, P14)

- **URL**: `GET /inventario/list` (existencias + libro), `GET /inventario/entradas` y `GET /inventario/salidas` (consultas filtradas), `GET/POST /inventario/entrada/crear` (**ADMIN**), `GET/POST /inventario/salida/crear` (**OPERADOR/EMPLEADO**).
- **Controller** (`InventarioController`): `listarInventario`, `listarEntradas`, `listarSalidas` (ambos delegan en el privado `mostrarMovimientos(model, lista, vista)`), `mostrarFormularioEntrada`, `crearEntrada`, `mostrarFormularioSalida`, `crearSalida`.
- **Service/Impl**: `MovimientoInventarioService` / `MovimientoInventarioServiceImpl` — `registrarEntrada`, `registrarSalida`, `listaMovimientos`, `listaEntradas`, `listaSalidas`.
- **Validaciones**: producto existe/activo («no se registran movimientos sobre productos inactivos»), responsable existe/activo («elija una cuenta activa»), «La cantidad de litros es obligatoria», **RN01** «No hay stock suficiente para la salida», «La fecha y hora de la operación son obligatorias», «El motivo debe tener entre 5 y 200 caracteres» (I03–I08).
- **Model**: `movimientos`, `productos`, `productosActivos` (selector sólo de activos), `responsables`, `operadores`, `saldoInicial`, `totalFisico`, `totalLitros`, `bajos`, `movimiento` → `inventario/lista.jsp`, `inventario/entradas.jsp`, `inventario/salidas.jsp`, `inventario/entrada.jsp`, `inventario/salida.jsp`.
- **Pruebas ETAPA 10**: I01–I11.

### 3.9 Finanzas — F23–F28 (P15, P16, P17, P27, P30) · ADMIN

- **URL**: `GET /finanzas/list` (resumen y saldo), `GET /finanzas/ingresos`, `GET /finanzas/egresos`, `GET /finanzas/detalle?id=`, `GET/POST /finanzas/conceptos/crear`, `GET/POST /finanzas/conceptos/editar?id=`, `POST /finanzas/conceptos/estado`. Sin ruta manual para crear movimientos de caja → 404 (F06).
- **Controller** (`FinanzasController`): `resumen`, `listarIngresos`, `listarEgresos`, `detalle`, `listarConceptos`, `mostrarFormularioCrearConcepto`, `crearConcepto`, `mostrarFormularioEditarConcepto`, `editarConcepto`, `cambiarEstadoConcepto`.
- **Service/Impl**: `MovimientoCajaService` / `MovimientoCajaServiceImpl` — `crearConcepto`, `editarConcepto`, `cambiarEstadoConcepto`, `listaIngresos`, `listaEgresos`, `apertura()`, `totalIngresos()`, `totalEgresos()`, `saldoActual()` (= `APERTURA(4410.00) + ingresos − egresos`).
- **Validaciones de conceptos**: «El nombre del concepto es obligatorio/… entre 3 y 40 caracteres», «Ya existe un concepto con el nombre …», «El tipo debe ser Ingreso o Egreso» (K01–K03).
- **RN04 verificada**: saldo final exacto S/ 3,000.00 = 4,410.00 + 420.00 − 1,830.00 con 6 movimientos = 2 compras + 4 ventas; renombrar un concepto **no corrompe** los movimientos (K04: siguen 4).
- **Model**: `movimientos`, `apertura`, `ingresos`, `egresos`, `saldo`, `origenesIngresos`, `origenesEgresos`, `conceptos`, `concepto`, `conceptosPorId`, `responsables` → `finanzas/lista.jsp`, `finanzas/ingresos.jsp`, `finanzas/egresos.jsp`, `finanzas/detalle.jsp`, `finanzas/conceptos.jsp`, `finanzas/concepto-crear.jsp`, `finanzas/concepto-editar.jsp`.
- **Pruebas ETAPA 10**: K01–K09, F01–F06.

### 3.10 Asistencia — F35–F38 (P28, P29) · RN05

- **URL**: `GET /asistencia/mi`, `POST /asistencia/entrada`, `POST /asistencia/salida` (cualquier sesión), `GET /asistencia/control` (**ADMIN**).
- **Controller** (`AsistenciaController`): `mostrarMiAsistencia(Authentication, Model)`, `registrarEntrada(Authentication, RedirectAttributes)`, `registrarSalida(...)`, `mostrarControlAsistencia(...)`. El privado `idEmpleadoDeLaSesion(Authentication)` resuelve el empleado desde el usuario autenticado: **el valor `idEmpleado` que llegue en el POST se ignora** (AS02: con `idEmpleado=2` en el navegador se registró la jornada de atorres).
- **Service/Impl**: `AsistenciaService` / `AsistenciaServiceImpl` — `registrarEntrada(idEmpleado)`, `registrarSalida(idEmpleado)`, `listaAsistenciasDeEmpleado`, `buscarAsistenciaAbierta`, `buscarEmpleado`.
- **Validaciones (RN05)**: empleado existe y activo; «Ya registraste la entrada de hoy: no puede haber una segunda asistencia abierta» (AS03); «No hay una asistencia abierta para registrar la salida» — también tras cerrar la jornada (AS04, AS06); «La hora de salida debe ser posterior a la hora de entrada». Las horas se calculan en **America/Lima** (ZonaId del sistema), no en la zona del servidor.
- **Resumen (F37)**: `asistenciaHoy` devuelve «Falta» (día sin marcación) o «Presente» (jornada con horas).
- **Model**: `empleadoActual`, `asistencias`, `asistenciaHoy`, `fechaHoy`, `empleadosPorId`, `mensaje` → `asistencia/mi.jsp`, `asistencia/control.jsp`.
- **Pruebas ETAPA 10**: AS01–AS10 (el control terminó con 6 marcaciones: 5 de semilla + 1 de hoy).

---

## 4. Datos en memoria (semilla al arrancar)

| Servicio (colección) | Semilla |
|---|---|
| `CategoriaServiceImpl.categorias` | Gasolinas, Diésel |
| `ProductoServiceImpl.productos` | PR01 1990 L / S/ 5.00, PR02 980 L / S/ 6.00, PR03 3950 L / S/ 4.00 |
| `EmpleadoServiceImpl.empleados` | 3 (Torres, Rojas, Díaz) |
| `UsuarioServiceImpl.usuarios` | atorres, lrojas (Operador/Vendedor), ediaz (Administrador) — hash BCrypt de `NexoDemo2026` |
| `CompraServiceImpl.compras/detalles` | C001 + 3 detalles |
| `VentaServiceImpl.ventas/detalles` | V001–V003 |
| `MovimientoInventarioServiceImpl.movimientos` | MI001–MI006 (entradas 300 L, salidas 80 L) |
| `MovimientoCajaServiceImpl.movimientos/conceptos` | 4 movimientos; CE01 Ingreso, CE02 Egreso; apertura S/ 4,410.00 |
| `AsistenciaServiceImpl.asistencias` | 5 marcaciones (ninguna de hoy) |

**No hay base de datos, JPA, Repository, DAO ni API REST**: cada `ServiceImpl` declara sus `List<T>` como atributos de instancia con la semilla cargada en el constructor. Reiniciar la aplicación restablece el estado inicial (así se hizo antes de la ronda final de la ETAPA 10).

---

## 5. Patrón único de respuesta (error y éxito)

Todos los POST siguen el mismo esqueleto en el controller:

```java
String error = servicio.xxx(objeto);
if (error != null) {
    redirect.addFlashAttribute("objeto", objeto);   // repuebla el formulario
    redirect.addFlashAttribute("mensaje", error);   // texto del servidor
    redirect.addFlashAttribute("mensajeTipo", "error");
    return "redirect:/<ruta del formulario>";
}
redirect.addFlashAttribute("mensaje", "... correctamente.");
redirect.addFlashAttribute("mensajeTipo", "ok");
return "redirect:/<ruta de la lista>";
```

El JSP del formulario o de la lista muestra `<p class="notice ${mensajeTipo}"><c:out value="${mensaje}"/></p>`. Por eso **nunca hay páginas de error crudas**: los rechazos vuelven al formulario con los datos conservados (C04) o a la lista con el aviso (C07, D09, F04), y las rutas inexistentes de borrado devuelven el 404 estándar de Tomcat (RN02).

---

*Fuentes: `src/main/java/com/example/nexo/{config,controller,service}` y `src/main/webapp/WEB-INF/views/**` leídos directamente; rutas, mensajes y efectos contrastados con la batería de la ETAPA 10.*
