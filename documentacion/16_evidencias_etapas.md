# Evidencias de las etapas de corrección (V2)

Documento nuevo de trabajo: registra, etapa por etapa, qué se corrigió y con
qué pruebas se verificó. No sustituye a `00_auditoria.md` (que mide la maqueta V1)
ni modifica matrices para ocultar pendientes: cada etapa declara lo probado y lo
que queda por probar.

---

## ETAPA 2 · Autenticación, roles y gestión de usuarios (F01, F02, F32)

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 14 pruebas HTTP reales.

### Correcciones aplicadas (código, sin commit)

| Archivo | Cambio |
|---|---|
| `pom.xml` | `spring-boot-starter-security` añadido (opción A acordada) |
| `config/SecurityConfig.java` | **nuevo** · cadena de filtros: `/css/**`, `/login`, `/error` y `/WEB-INF/**` públicos; secciones del Administrador (`/categorias/**`, `/combustibles/crear`, `/inventario/entrada/crear`, `/empleados/**`, `/usuarios/**`, `/compras/**`, `/finanzas/**`, `/asistencia/control`) con `hasRole("ADMIN")`; registro de ventas y salidas con `hasAnyRole("OPERADOR", "EMPLEADO")`; resto autenticado; login de formulario con página propia; logout con invalidación de sesión y borrado de cookie; aviso de permisos (nunca 500); BCrypt |
| `service/NexoUserDetailsService.java` | **nuevo** · conecta los usuarios en memoria con Spring Security; búsqueda normalizada; cuenta `Inactivo` queda deshabilitada; rol del catálogo traducido a `ROLE_ADMIN` / `ROLE_OPERADOR` / `ROLE_EMPLEADO` |
| `controller/AuthController.java` | **nuevo** · sirve `/login` (avisos de credenciales e inactivo) y `/sin-permisos` |
| `controller/HomeController.java` | **nuevo** · `/` manda al inicio del rol: Administrador → `/categorias/list` (P05); el resto → `/ventas/list` (P09) |
| `service/UsuarioServiceImpl.java` | contraseña guardada como hash BCrypt; conservación del hash al editar sin contraseña; validaciones en servidor: username único ignorando mayúsculas y espacios, patrón `[a-z0-9._-]{4,20}`, contraseña 8–40, rol y estado del catálogo, empleado sin cuenta previa |
| `controller/UsuarioController.java` | mensajes flash de éxito/error; el error devuelve al formulario conservando lo digitado |
| `views/login.jsp`, `views/error/permisos.jsp` | **nuevas** |
| 27 JSPs | menú por rol (JSTL sobre `userPrincipal.authorities`; en Spring Security 6 no existe el taglib `sec:`), bloque de sesión + cierre (F02), avisos `notice ${mensajeTipo}`, taglib `form` en 17 vistas, POSTs de asistencia con `form:form` (CSRF ×2) |
| `views/usuario/crear.jsp`, `editar.jsp` | bloque de aviso; contraseña opcional al editar |
| `views/{usuario,categoria,empleado,producto}/lista.jsp` | bloque de aviso flash añadido (antes el mensaje de éxito se perdía en el destino del redirect) |
| `css/estilos.css` | estilos `.sesion`, `.notice.error/ok`, `.login-pagina` |
| `application.properties` | `server.servlet.session.timeout=30m` |

### Defecto crítico encontrado y corregido durante la etapa

**Bucle infinito en `/login` (302 → /login).** Causa raíz, confirmada con trazas
de `FilterChainProxy`: el `forward` del controller al JSP **vuelve a pasar por la
cadena de filtros** (`HeaderWriterRequestDispatcher.forward` → `DelegatingFilterProxy`);
`/WEB-INF/views/login.jsp` no coincidía con ninguna regla y caía en
`anyRequest().authenticated()` → `AccessDeniedException` → redirect a `/login` →
bucle. El JSP nunca llegaba a Jasper (0 traducciones en el directorio de trabajo).
Corregido con `.requestMatchers("/css/**", "/login", "/error", "/WEB-INF/**").permitAll()`.

### Pruebas ejecutadas (14/14 PASS)

Batería `etapa2_pruebas.ps1` contra la aplicación real en `http://localhost:8080`
(datos en memoria, reinicio previo para partir del estado semilla):

| Prueba | Resultado | Evidencia |
|---|---|---|
| T1 ruta protegida sin sesión | PASS | `GET /ventas/list` → 302 → `/login` |
| T2 pantalla de login | PASS | `GET /login` = 200 con token `_csrf` |
| T3 login Administrador | PASS | `ediaz` entra y aterriza en `/categorias/list` (inicio por rol) |
| T4 Administrador ve usuarios | PASS | `GET /usuarios/list` = 200 con las cuentas |
| T5 duplicado con mayúsculas/espacios | PASS | `" ATORRES "` rechazado en el servidor con aviso y devuelto al formulario |
| T6 crear empleado de prueba | PASS | DNI `99999999` visible en la lista |
| T7 creación válida de cuenta | PASS | 302 → `/usuarios/list` con aviso flash «registrado correctamente» y la cuenta presente |
| T8 roles del Empleado | PASS | inicio = `/ventas/list`; `/ventas/crear` = 200; `/categorias/list` → 403 con aviso de permisos |
| T9 cierre de sesión | PASS | antes 200; logout → `/login?logout`; la sesión vieja vuelve a 302 → `/login` |
| T10 cuenta inactiva | PASS | desactivada, el login responde `error=inactivo` con aviso propio |
| T11 roles del Operador | PASS | inicio = `/ventas/list`; `/asistencia/mi` = 200; `/asistencia/control` → 403 con aviso |
| T12 contraseña incorrecta | PASS | `error=credenciales`, sin detalle del motivo |
| T13 CSRF | PASS | POST sin token rechazado y la sesión no se autentica (`/usuarios/list` sigue pidiendo login) |
| T14 contraseñas ocultas | PASS | ni la contraseña demo ni el hash aparecen en pantalla |

Además: `mvn compile` = EXIT 0 y 0 marcadores `System.out.println` de diagnóstico en el código.

### Reglas de acceso aplicadas (actores de `03_interfaces.md`)

- **Administrador:** P05, P07, P12, P15, P16, P18–P23, P25, P26, P29, P30.
- **Operador / Vendedor o Empleado:** P08, P24 (registrar venta) y P13 (salida).
- **Cualquier usuario autenticado:** P06, P09, P10, P11, P14, P28.

### Límites declarados de esta etapa

- La rúbrica oficial del examen parcial no está en el repositorio: el contraste
  formal queda pendiente hasta recibirla (ETAPA 8).
- Los módulos de catálogo (categorías, combustibles) y el resto de CRUD aún no
  tienen validaciones de negocio en el servidor; les toca a las etapas 4–6.

---

## ETAPA 3 · Asistencia con identidad de sesión (F35–F38, P28, P29, RN05)

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 10 pruebas HTTP reales.

### Correcciones aplicadas (código, sin commit)

| Archivo | Cambio |
|---|---|
| `NexoApplication.java` | `TimeZone.setDefault(America/Lima)` antes de arrancar: asistencia, ventas y compras generan fechas y horas a la hora de Perú |
| `service/AsistenciaService.java` | eliminado el empleado de demostración: `listaAsistenciasDeEmpleado(id)`, `buscarAsistenciaAbierta(id)`, `registrarEntrada(id)`, `registrarSalida(id)` y `buscarEmpleado(id)` reciben el empleado resuelto por el controller |
| `service/AsistenciaServiceImpl.java` | eliminada la constante `EMPLEADO_ACTUAL_DEMO = 1`; toda operación trabaja sobre el `idEmpleado` que llega del servidor; si es null la lista sale vacía y se avisa (sin excepción) |
| `controller/AsistenciaController.java` | resuelve la identidad desde la sesión: `Authentication.getName()` → `UsuarioService` → `idEmpleado`; el navegador **no envía** ningún identificador; si la cuenta no tiene empleado asociado se muestra un aviso claro |
| `views/asistencia/mi.jsp` | eliminado el aviso temporal «empleado de demostración»; enlaces a Control y Personal visibles sólo para Administrador; pie con la zona horaria del servidor |

### Reglas verificadas (RN05)

- Cada usuario ve y registra **únicamente** su propia asistencia.
- Una sola asistencia abierta por jornada y una sola marcación por empleado y día.
- La hora de salida debe ser posterior a la entrada; sin asistencia abierta se avisa, sin error.
- El estado (`Presente` / `Falta`) se calcula en servidor a partir de las horas; nunca se digita.
- El `idEmpleado` enviado en el formulario se ignora por completo.

### Pruebas ejecutadas (10/10 PASS)

Batería `etapa3_pruebas.ps1` contra la aplicación real (reinicio previo al estado semilla):

| Prueba | Resultado | Evidencia |
|---|---|---|
| A1 identidad propia | PASS | `atorres` ve sólo «Asistencia de Ana Torres» con sus 3 marcaciones semilla |
| A2 otra identidad | PASS | `lrojas` ve sólo «Asistencia de Luis Rojas» (1 fila), sin rastro de Ana Torres |
| A3 entrada en la sesión | PASS | la entrada registrada aparece en la lista del usuario autenticado (3 → 4 filas) |
| A4 RN05 · una sola abierta | PASS | segunda entrada rechazada con aviso y la lista no crece |
| A5 salida | PASS | cierra la asistencia abierta; repetir avisa «No hay una asistencia abierta» |
| A6 zona horaria Perú | PASS | hora registrada 13:55 = America/Lima 13:55 (dif. 0 min) |
| A7 id no del navegador | PASS | `lrojas` envía `idEmpleado=1` en el POST: su lista crece a 2 y sigue siendo suya |
| A8 control del Administrador | PASS | `/asistencia/control` ve las 7 marcaciones del personal; el filtro `idEmpleado=2` reduce a 2 |
| A9 Operador sin control | PASS | `atorres` en `/asistencia/control` → 403 con aviso de permisos |
| A10 sin sesión | PASS | `/asistencia/mi` anónimo → 302 → `/login` |

Además: `mvn compile` = EXIT 0 y el seed documentado de la V1 (5 marcaciones) se conserva sin cambios.

### Límites declarados de esta etapa

- Las horas se toman del reloj del servidor en `America/Lima`; no hay turno programado ni horario configurable (fuera del alcance de la rúbrica actual).
- El filtro del control es por empleado (P29); no incluye rango de fechas porque P29 sólo pide «consultar la asistencia de todo el personal».

---

## ETAPA 4 · Catálogo completo de familias y combustibles (F04–F12, RN01, RN02)

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 18 pruebas HTTP reales.

### Correcciones aplicadas (código, sin commit)

| Archivo | Cambio |
|---|---|
| `model/Categoria.java` | añadidos `descripcion` y `estado` (el modelo oficial `02_modelo_entidad_relacion.md` los exige; antes sólo existían `id` y `nombre`) |
| `service/CategoriaService.java` y `CategoriaServiceImpl.java` | F04 valida en servidor: nombre obligatorio 3–40, único ignorando mayúsculas/espacios, descripción ≤ 200, estado del catálogo; F07 `editarCategoria` conservando el id; F08 `cambiarEstadoCategoria` (RN02: nunca se elimina); `buscarCategoriaPorId` |
| `service/ProductoService.java` y `ProductoServiceImpl.java` | F09 valida en servidor: nombre 3–40 único, categoría existente (y **Activa** en el alta; en la edición se permite conservar la actual inactiva), unidad Litro, precio > 0, stock ≥ 0 (RN01), estado del catálogo; F11 `editarProducto` conservando el código; F12 `cambiarEstadoProducto` (RN02); `buscarProductoPorId` |
| `controller/CategoriaController.java` | mensajes flash de éxito/error; el error devuelve al formulario conservando lo digitado; nuevas rutas `GET/POST /categorias/editar` y `POST /categorias/estado` |
| `controller/ProductoController.java` | mensajes flash; `GET/POST /combustibles/editar` y `POST /combustibles/estado`; el alta sólo ofrece categorías Activas |
| `controller/AuthController.java` | `/sin-permisos` ahora acepta cualquier método (antes un POST denegado se reenviaba con POST y devolvía 405 en vez del aviso) |
| `config/SecurityConfig.java` | `/combustibles/editar` y `/combustibles/estado` añadidos al bloque del Administrador |
| `views/categoria/crear.jsp` | añadidos campos descripción y estado (maqueta V1 P22); bloque de aviso flash |
| `views/categoria/editar.jsp` | **nueva** · F07 con id oculto (no se digita) |
| `views/categoria/lista.jsp` | columnas Descripción y Estado; acciones Editar y Desactivar/Activar (F07/F08) |
| `views/producto/crear.jsp` | bloque de aviso flash; el selector sólo lista familias Activas |
| `views/producto/editar.jsp` | **nueva** · F11 con código de sólo lectura |
| `views/producto/lista.jsp` | acciones Editar y Desactivar/Activar, visibles sólo para Administrador (el Operador sólo consulta, P06) |
| `css/estilos.css` | estilo `.inline-accion` para los botones de estado en las tablas |

### Reglas verificadas

- **RN01**: el stock nunca se acepta negativo, ni en el alta ni en la edición; se rechaza en el servidor con aviso.
- **RN02**: no existe ninguna ruta de eliminación de categorías ni de combustibles; el estado se cambia y el registro permanece con su historial.
- Una categoría Inactiva no aparece en el selector de altas de combustibles, y el servidor rechaza el alta aunque el navegador mande su id (defensa en profundidad).
- Unicidad de nombres ignorando mayúsculas y espacios en ambos catálogos.
- CSRF obligatorio en todos los POST; el Operador no administra el catálogo.

### Pruebas ejecutadas (18/18 PASS)

Batería `etapa4_pruebas.ps1` contra la aplicación real (reinicio previo al estado semilla):

| Prueba | Resultado | Evidencia |
|---|---|---|
| C1 alta de categoría válida | PASS | 302 y la familia aparece con su descripción |
| C2 duplicado con mayúsculas/espacios | PASS | rechazado con aviso; la lista no crece |
| C3 nombre corto | PASS | 2 caracteres rechazados con aviso |
| C4 edición válida | PASS | nombre y descripción actualizados; el id se conserva |
| C5 edición duplicada | PASS | no puede renombrarse con el nombre de otra familia |
| C6 desactivar categoría (F08/RN02) | PASS | queda Inactiva y **sigue** en la lista |
| C7 alta de combustible | PASS | nace el código PR04 con precio 4.50 |
| C8 selector sin inactivas | PASS | el formulario de alta omite la familia Inactiva |
| C9 el servidor bloquea familia inactiva | PASS | el alta con `idCategoria=3` (Inactiva) se rechaza aunque el navegador la mande |
| C10 precio cero | PASS | rechazado con «mayor a cero» |
| C11 stock negativo (RN01) | PASS | alta con −5 rechazada |
| C12 duplicado de combustible | PASS | «  GASOLINA regular  » rechazado |
| C13 edición válida (F11) | PASS | PR01 pasa a 5.80 y conserva su código |
| C14 edición con stock negativo (RN01) | PASS | rechazada; PR01 conserva 1990 |
| C15 desactivar PR03 (F12/RN02) | PASS | queda Inactivo y sigue listado |
| C16 Operador sin catálogo | PASS | 403 en crear categoría y editar combustible, con aviso |
| C17 CSRF | PASS | POST sin token = 403 y la categoría no se creó |
| C18 anónimo | PASS | POST sin sesión → 302 → `/login` |

Además: `mvn compile` = EXIT 0 y los datos semilla (2 familias, 3 combustibles) se conservan al reiniciar.

### Defecto encontrado y corregido durante la etapa

**405 en `/sin-permisos` al denegar un POST.** Cuando un POST autenticado se rechazaba
(CSRF o rol), el `accessDeniedPage` reenviaba la petición a `/sin-permisos` **conservando
el método POST**, y esa ruta sólo admitía `@GetMapping` → `HttpRequestMethodNotSupportedException`
(405) en lugar del aviso de permisos. Corregido con `@RequestMapping("/sin-permisos")`.

### Límites declarados de esta etapa

- El stock «de referencia» del alta es el saldo inicial que luego mueven entradas, salidas y ventas (etapas 5–7); aquí sólo se impide que sea negativo.
- El precio y la unidad se editan manualmente; no hay histórico de precios (fuera del alcance de la rúbrica actual).

---

## ETAPA 5 · Empleados (F29–F31) y conceptos económicos (F23–F25)

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 19 pruebas HTTP reales.

### Correcciones aplicadas (código, sin commit)

| Archivo | Cambio |
|---|---|
| `service/EmpleadoService.java` y `EmpleadoServiceImpl.java` | F29/F31 ahora validan en servidor: DNI obligatorio de exactamente 8 dígitos y **único**, nombres y apellidos de 3 a 60, cargo de 3 a 40, teléfono con formato `000 000 000`, estado del catálogo; métodos pasan de `void` a devolver el mensaje de error (o `null` si todo va bien); nuevo `cambiarEstadoEmpleado` (F31, RN02); `buscarEmpleadoPorId` ya no lanza excepción con id nulo |
| `controller/EmpleadoController.java` | mensajes flash de éxito/error en crear y editar (el error devuelve al formulario conservando lo digitado); aviso en pantalla al pedir un id inexistente (nunca 500); nueva ruta `POST /empleados/estado` |
| `service/MovimientoCajaService.java` y `MovimientoCajaServiceImpl.java` | F23–F25: CRUD de conceptos con validaciones (nombre obligatorio 3–40 único ignorando mayúsculas y espacios, tipo `Ingreso`/`Egreso`, estado `Activo`/`Inactivo`); `buscarConceptoPorId`, `crearConcepto` (código siguiente CE03…), `editarConcepto` conservando el código, `cambiarEstadoConcepto` (RN02); la clase sigue **sin** crear movimientos a mano (sólo se derivan de compras y ventas, RN04) |
| `controller/FinanzasController.java` | nuevas rutas F23–F25: `GET /finanzas/conceptos/list`, `GET/POST /finanzas/conceptos/crear`, `GET/POST /finanzas/conceptos/editar`, `POST /finanzas/conceptos/estado` (todas dentro de `/finanzas/**` = sólo Administrador) |
| `views/empleado/crear.jsp` y `empleado/editar.jsp` | añadido el bloque de aviso flash (antes no mostraban los errores del servidor) |
| `views/empleado/lista.jsp` | acciones con botones reales: Editar y Desactivar/Activar (F31, RN02); el estado se resalta con `badge muted` |
| `views/finanzas/conceptos.jsp` | **nueva** · F24/P17: catálogo con Código, Concepto, Tipo, Estado y acciones Editar + Activar/Desactivar; aviso RN02 con el número de movimientos que dependen del catálogo |
| `views/finanzas/concepto-crear.jsp` | **nueva** · F23: nombre (3–40), tipo (Ingreso/Egreso), estado (Activo/Inactivo) |
| `views/finanzas/concepto-editar.jsp` | **nueva** · F25: código de sólo lectura + nombre, tipo y estado; aviso RN02 |
| `views/finanzas/lista.jsp`, `ingresos.jsp`, `egresos.jsp`, `detalle.jsp` | enlaces al nuevo CRUD de conceptos en el nav y en los botones de acción del resumen |

### Reglas verificadas

- **RN02**: ni empleados ni conceptos tienen ruta de eliminación; sólo cambio de estado, y el registro permanece en la lista con su historial (E7, E16).
- DNI de empleado único e idéntico al del modelo (8 dígitos), teléfono con el formato de la V1.
- Nombre de concepto único ignorando mayúsculas y espacios; tipo restringido al catálogo `Ingreso`/`Egreso`.
- Editar un concepto **no rompe el historial**: los movimientos de caja se generan con su propio `tipo`/`monto` y sólo leen el nombre del concepto al mostrarse, de modo que renombrar CE02 actualiza el resumen de caja sin alterar importes (E14 comprueba el resumen con el nombre nuevo).
- Ids inexistentes (empleado 99, concepto CE99) → 302 con aviso, nunca 500 (E8).
- `/empleados/**` y `/finanzas/**` = sólo Administrador, incluidos los nuevos POST de estado (E9, E17); CSRF obligatorio (E18); anónimos al login (E19).

### Pruebas ejecutadas (19/19 PASS)

Batería `etapa5_pruebas.ps1` contra la aplicación real (reinicio previo al estado semilla):

| Prueba | Resultado | Evidencia |
|---|---|---|
| E1 alta de empleado válida (F29) | PASS | 302 y María Paredes aparece en la lista |
| E2 DNI duplicado | PASS | rechazado con aviso (DNI de Ana Torres) |
| E3 DNI de 5 dígitos | PASS | rechazado con aviso |
| E4 teléfono `123` | PASS | rechazado con «formato 000» |
| E5 edición válida (F31) | PASS | cargo actualizado; el id 1 se conserva |
| E6 edición con DNI de otro empleado | PASS | rechazado con aviso |
| E7 desactivar empleado (RN02) | PASS | María queda Inactiva y **sigue** en la lista |
| E8 ids inexistentes (99 y CE99) | PASS | ambos 302 con aviso; nunca 500 |
| E9 Operador sin empleados | PASS | crear=403 y estado=403 |
| E10 alta de concepto válida (F23) | PASS | nace CE03 y se ve en el catálogo |
| E11 duplicado con mayúsculas/espacios | PASS | rechazado; la lista no crece (3 filas) |
| E12 nombre corto | PASS | 2 caracteres rechazados |
| E13 tipo fuera de catálogo | PASS | `Mixto` rechazado |
| E14 edición válida (F25) | PASS | CE02 renombrado, código conservado y el resumen de caja refleja el nuevo nombre |
| E15 edición con nombre de otro concepto | PASS | CE01 no puede adoptar el nombre de CE02 |
| E16 desactivar concepto (RN02) | PASS | CE03 queda Inactivo y sigue listado |
| E17 Operador sin conceptos | PASS | list=403 y estado=403 |
| E18 CSRF | PASS | POST sin token = 403 y el concepto no se creó |
| E19 anónimo | PASS | POST sin sesión → 302 → `/login` |

Además: `mvn compile` = EXIT 0 y los datos semilla (3 empleados, 2 conceptos) se conservan al reiniciar.

### Límites declarados de esta etapa

- Los conceptos nuevos sólo clasifican: los movimientos de caja siguen naciendo de compras y ventas confirmadas (RN04) y no hay captura manual de movimientos (P27 es «visual» en la V1 y la V2 no la expone, decisión ya anotada en `03_interfaces.md`).
- Cambiar el **tipo** de un concepto con movimientos asociados reetiqueta el catálogo, pero los movimientos históricos conservan su tipo propio (ingreso/egreso) calculado al confirmar la compra o la venta.
- El teléfono exige el formato `000 000 000` de la V1; no valida operador ni código de país (fuera del alcance de la rúbrica).

---

## ETAPA 6 · Cierre del CRUD: compras, ventas e inventario (F13–F22, RN01–RN03)

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 14 pruebas HTTP reales.

Los módulos de compras, ventas e inventario ya tenían validaciones de servidor (ETAPA 1); esta etapa cerró los tres frentes que faltaban: **RN02 aplicado a las operaciones nuevas** (productos y cuentas Inactivos fuera de los selectores y rechazados en el servidor), **conservación de lo digitado** cuando el servidor rechaza un envío y **etiqueta de error** (`mensajeTipo`) en los mensajes flash.

### Correcciones aplicadas (código, sin commit)

| Archivo | Cambio |
|---|---|
| `service/CompraServiceImpl.java` | RN02: `crearCompra` rechaza un producto Inactivo («sólo los combustibles activos pueden comprarse») y un responsable con cuenta Inactiva; el stock usa la misma referencia del producto ya validada |
| `service/VentaServiceImpl.java` | RN02: `crearVenta` rechaza un operador con cuenta Inactiva (además del producto Inactivo que ya rechazaba) |
| `service/MovimientoInventarioServiceImpl.java` | RN02 (P25/P26): `registrarEntrada` y `registrarSalida` rechazan productos Inactivos y responsables Inactivos; las entradas/salidas automáticas de compras y ventas (RN03) no cambian |
| `controller/CompraController.java` | el formulario sólo ofrece productos Activos y responsables Activos; al rechazar un envío devuelve `compra`, `detalle` y `fecha` conservados; envía `mensajeTipo` (error/ok) |
| `controller/VentaController.java` | el selector de operadores excluye cuentas Inactivas; al rechazar devuelve `venta` y `detalle`; envía `mensajeTipo` |
| `controller/InventarioController.java` | entrada y salida ofrecen `productosActivos` para el selector (la tabla de existencias sigue mostrando todos con su estado); al rechazar devuelve `movimiento`; envía `mensajeTipo` |
| `views/compra/crear.jsp` | conserva fecha, producto (opción seleccionada), litros y precio digitados; comentario RN02 en el selector |
| `views/venta/crear.jsp` | conserva el producto seleccionado y los litros digitados |
| `views/inventario/entrada.jsp` y `salida.jsp` | el selector usa `productosActivos`; conservan litros, fecha, motivo y responsable; comentario RN02 |

### Reglas verificadas

- **RN02 en operaciones nuevas:** un producto Inactivo no aparece en los selectores de compra, venta, entrada y salida, y aunque se mande a mano en un POST el servidor lo rechaza (G3, G7, G12). Una cuenta Inactiva desaparece de los selectores de responsable/operador y el servidor la rechaza (G4, G8). Al reactivar la cuenta, vuelve a estar disponible (G14).
- **RN01/RN03 sin regresiones:** la venta con litros mayores al stock sigue rechazándose (G9) y la compra confirmada sigue generando su entrada de inventario única «Compra C001» (G1, G10).
- **Conservación de lo digitado:** tras un rechazo, el formulario devuelve proveedor, fecha, producto, litros, precio, motivo y responsable tal como se enviaron (G2, G9, G11); el litro se muestra con la normalización del servidor (2 decimales).
- **F32 (usuarios):** `/usuarios/editar` permite la baja y la reactivación de la cuenta de un Operador; el cambio de estado es lo único necesario para sacarlo de las operaciones (G4, G14).
- `/compras/**` e `/inventario/entrada/crear` siguen siendo del Administrador; `/ventas/crear` e `/inventario/salida/crear` del personal de operación (las pruebas usan cada sesión en su ruta).

### Pruebas ejecutadas (14/14 PASS)

Batería `etapa6_pruebas.ps1` contra la aplicación real (reinicio previo al estado semilla):

| Prueba | Resultado | Evidencia |
|---|---|---|
| G1 alta de compra válida (F13) | PASS | lista con «Distribuidora Norte» Confirmada e inventario con «Compra C001» |
| G2 rechazo conserva lo digitado | PASS | aviso «entre 3 y 60» y el formulario devuelve XY, 2026-10-10, PR02 seleccionado, 123 L y 7.25 |
| G3 producto Inactivo en compra | PASS | PR04 desactivado; la compra rechazada no aparece en la lista |
| G4 responsable Inactivo en compra | PASS | atorres desactivado; compra rechazada y sin registro |
| G5 selector de compra sólo Activos | PASS | PR04 ausente, PR01 presente |
| G6 selector de responsables sin Inactivos | PASS | Ana Torres no aparece |
| G7 producto Inactivo en venta | PASS | el servidor responde «pueden venderse» |
| G8 operador Inactivo en venta | PASS | el servidor responde «elija una cuenta activa» |
| G9 rechazo por stock conserva (RN01) | PASS | aviso «No hay stock suficiente», PR02 seleccionado y 999999 L conservados |
| G10 entrada manual válida (F16) | PASS | el movimiento «Reposicion etapa 6» aparece en el libro |
| G11 rechazo de entrada conserva | PASS | aviso «entre 5 y 200»; conserva 333.00 L, fecha, motivo y PR02 |
| G12 salida con producto Inactivo | PASS | el servidor responde «productos inactivos» |
| G13 selectores de inventario | PASS | sin PR04 en ambos selectores; la tabla de existencias lo muestra Inactivo |
| G14 reactivación y venta válida (F32) | PASS | Ana Torres vuelve al selector y la venta se registra con su firma |

Además: `mvn compile` = EXIT 0 y el estado semilla se conserva al reiniciar (las mutaciones de la batería viven sólo en la sesión de prueba).

### Límites declarados de esta etapa

- F03 (tablero), F33 (portada pública) y F34 (mensaje de contacto) **no existen en la versión 2 por decisión documentada** en `04_funcionalidades.md` («la versión V2 no tiene tablero», «la versión V2 no incluye portada», «la versión V2 no incluye esta interfaz»): el resumen económico vive en `/finanzas/list` y el acceso público se limita al login. Si la rúbrica exige estas pantallas, se implementarán en la etapa 8 con ese alcance.
- Un usuario desactivado **con la sesión abierta no se desconecta automáticamente** (Spring Security conserva la sesión); la baja impide nuevos inicios de sesión y que su cuenta se firme en operaciones nuevas (G4, G8).
- La conservación de lo digitado usa `BigDecimal` normalizado por el servidor (por ejemplo, 333.00 L): se muestra el valor ya validado, no el texto crudo del navegador.

---

## ETAPA 7 · Reglas de negocio RN01–RN05: cumplimiento y violación

**Fecha:** 09/10/2026 · **Estado:** completada y verificada con 13 pruebas HTTP reales (7 de cumplimiento y 6 de violación).

Cada una de las cinco reglas de negocio de `05_reglas_negocio.md` se ejercitó sobre la aplicación real con su **caso de cumplimiento** y su **caso de violación**, verificando no sólo el mensaje del servidor sino también el efecto (o la ausencia de efecto) sobre los datos en memoria: stock, libro de movimientos, caja, listados y marcaciones.

### Qué se verificó por regla

| RN | Cumplimiento | Violación |
|---|---|---|
| **RN01 · Stock nunca negativo** | Salida manual dentro del stock: 2.030 → 1.980 L, con su fila en el libro (H5) | Salida de 99.999 L y venta de 999.999 L rechazadas; stock y listados intactos (H6, H7) |
| **RN02 · Conservación de registros** | Desactivar PR01 (que ya tiene ventas) lo muestra con badge Inactivo y conserva ventas, libro y caja; se reactiva sin pérdida (H8) | No existe ninguna ruta de borrado: `POST …/eliminar` de producto, empleado y concepto devuelven 404 y los registros siguen en sus listas (H9) |
| **RN03 · Integridad de operaciones** | 1 venta ⇒ 1 salida («Venta V004») y 1 compra ⇒ 1 entrada («Compra C002»): libro 6 → 8 filas, totales 350 L/90 L (H1) | Venta sin cantidad rechazada sin efectos parciales: ni venta, ni movimiento, ni caja (H4) |
| **RN04 · Unicidad de movimientos de caja** | Cada venta aporta 1 ingreso (50.00) y cada compra 1 egreso (230.00); conciliación exacta: 4.410 + 420 − 1.580 = **3.250.00**; caja 4 → 6 filas (H2) | No hay ruta para crear/duplicar movimientos a mano: `POST /finanzas/movimientos/crear` → 404 y la caja queda en 6 filas (H3) |
| **RN05 · Asistencia propia, una por jornada, salida posterior** | Entrada con `idEmpleado=2` manipulado: se ignora y se registra para la sesión (atorres 3 → 4 filas; lrojas intacto en 1); salida posterior registrada en la misma fila de hoy (H11, H13) | Salida sin entrada previa rechazada sin filas nuevas (H10); segunda entrada en la misma jornada rechazada (H12) |

### Pruebas ejecutadas (13/13 PASS)

Batería `etapa7_pruebas.ps1` contra la aplicación real (reinicio previo al estado semilla; sesiones: ediaz Administrador, atorres y lrojas Operadores):

| Prueba | Resultado | Evidencia numérica |
|---|---|---|
| H1 RN03 cumplimiento | PASS | libro 8 filas, «Venta V004», «Compra C002», entradas 350 L, salidas 90 L |
| H2 RN04 cumplimiento | PASS | S/ 420.00 ingresos, S/ 1.580.00 egresos, S/ 3.250.00 saldo, caja 6 filas |
| H3 RN04 violación | PASS | POST 404 y caja sigue en 6 filas |
| H4 RN03 violación | PASS | aviso «La cantidad de litros es obligatoria»; ventas 4, libro 8, caja 6 |
| H5 RN01 cumplimiento | PASS | stock 2.030 → 1.980, libro 9 filas, «salidas 140 L» |
| H6 RN01 violación (salida) | PASS | aviso «No hay stock suficiente»; stock 1.980 y libro 9 sin cambios |
| H7 RN01 violación (venta) | PASS | aviso «No hay stock suficiente»; stock 1.980, ventas 4 filas |
| H8 RN02 cumplimiento | PASS | badge Inactivo en PR01, historial intacto (libro 9, caja «S/ 420.00»), reactivado |
| H9 RN02 violación | PASS | 404/404/404 en rutas de borrado; PR01, Torres y CE01 presentes |
| H10 RN05 violación | PASS | «No hay una asistencia abierta»; lrojas sigue en 1 fila |
| H11 RN05 cumplimiento + identidad | PASS | «Entrada registrada a las»; atorres 3 → 4 filas; lrojas intacto |
| H12 RN05 violación | PASS | «Ya registraste la entrada de hoy»; sigue en 4 filas |
| H13 RN05 cumplimiento | PASS | «Salida registrada a las» en la misma fila de hoy |

### Cifras de la semilla usadas como línea base (verificadas antes de las pruebas)

- Inventario: 6 movimientos semilla (entradas 300 L, salidas 80 L), saldo inicial 6.700 L; stock PR01 = 1.990 L.
- Caja: 4 movimientos semilla (ingresos S/ 370 por V001–V003, egresos S/ 1.350 por C001), apertura S/ 4.410, saldo S/ 3.430.
- Asistencia: 5 marcaciones semilla (atorres 3, lrojas 1, ediaz 1), ninguna de hoy.
- Formato de números del JVM: en-US (`S/ 4,410.00`) verificado empíricamente antes de fijar las cifras esperadas.

### Límites declarados de esta etapa

- RN01 se ejerce sobre salidas y ventas (las únicas operaciones que descuentan stock); las compras y entradas sólo suman, por lo que no pueden violarla.
- RN05 «una por jornada» y «salida posterior» se prueban en la jornada real del sistema (America/Lima); no se manipula el reloj del servidor para simular días distintos.
- El caso de violación de RN02 no es un código del sistema sino su **inexistencia comprobada**: ninguna ruta ni servicio elimina registros (verificado por HTTP y por inspección de controladores).

---

## ETAPA 8 · Contraste con las dos rúbricas

**Fecha:** 09/10/2026 · **Estado:** completada con dos documentos separados; sin inventar nada.

### Trabajo realizado

| Acción | Detalle |
|---|---|
| Rúbrica Avance 1 | leída desde `recurso/Rubrica%20Avance%201.pdf` (5 páginas, 20 puntos: Documento 6 + Maquetado 6 + Oral 8) |
| Verificaciones del repo | etiquetas semánticas 31/31; dashboard con 5 `figure.chart` de serie temporal 04–10 sep; **0** `<script>`/`.js`; **0** menciones de storyboard, retroalimentación, despliegue o nube; objetivos OBJ 3.1–3.3 medibles; RN01–RN05 con casos de cumplimiento y violación |
| Documentos creados | `17_matriz_cumplimiento_avance1.md` (6/6 documento; 7/8 maquetado con «consumo de API» **no cumplido** y honesto; preguntas orales pendientes) y `18_matriz_cumplimiento_examen_parcial.md` (2 cumplidos, 3 parciales, 2 no cumplidos) |
| Regla aplicada | las dos rúbricas en documentos separados, sin mezclarlas; ningún elemento inexistente (API, storyboard, JavaScript, retroalimentación, despliegue cloud) declarado como cumplido |

---

## ETAPA 9 · Organización y limpieza del repositorio

**Fecha:** 09/10/2026 · **Estado:** completada; borrados únicamente los 2 PDF duplicados autorizados.

### Acciones verificadas

| Acción | Evidencia |
|---|---|
| Identidad de duplicados | MD5 idéntico antes de borrar: `recurso/e6920ae9-009e-4ab0-891b-72dad466e347(1).pdf` = `guia3(1).pdf` (1AB60FFB…) y `Proyecto Estructura_v2 %281%29 (1).pdf` = copia de la guía sin « (1)» (ABD23F94…) |
| Borrados | los 2 archivos, ambos tracked en git (recuperables); **9 PDF restantes con 9 MD5 únicos** |
| Coherencia de conteos | ajustadas de 11 a 9 las menciones de PDF en `00_auditoria.md`, `14_bibliografia.md` y `15_anexos.md` |
| Mapa conceptual | `19_organizacion_repositorio.md`: mapa, cambios, archivos no movidos con su razón y propuestas futuras (subcarpeta `recurso/`, `.vscode` en `.gitignore`) |
| Integridad | 0 enlaces locales rotos en los 6 archivos tocados; no se borró HTML V1, JSP, Java, diagramas, matrices, Maven Wrapper ni documentación original |

---

## ETAPA 10 · Regresión integral de cierre

**Fecha:** 09/10/2026 · **Estado:** completada — **126/126 pruebas PASS, 0 errores en la aplicación**.

Informe completo en [22_informe_regresion_etapa10.md](22_informe_regresion_etapa10.md). Resumen:

| Bloque | Pruebas | PASS |
|---|---|---|
| Semilla verificada (S01–S07) | 7 | 7 |
| Acceso, anonimato, login, roles de inicio, CSRF en login (A01–A09) | 9 | 9 |
| Categorías CRUD + RN02 + CSRF + permisos (C01–C12) | 12 | 12 |
| Combustibles CRUD + selectores de venta (P01–P11) | 11 | 11 |
| Empleados CRUD + RN02 (E01–E09) | 9 | 9 |
| Usuarios CRUD + rol fabricado + hash conservado + F32 (U01–U15) | 15 | 15 |
| Conceptos económicos + caja intacta (K01–K09) | 9 | 9 |
| Asistencia + identidad real + unicidad (AS01–AS10) | 10 | 10 |
| Compras + RN03/RN04 atómicas (D01–D11) | 11 | 11 |
| Ventas + RN01 + salida/ingreso únicos (V01–V11) | 11 | 11 |
| Inventario manual + permisos cruzados (I01–I11) | 11 | 11 |
| Finanzas + conciliación exacta (F01–F06) | 6 | 6 |
| Cierre RN01–RN05 con estado de datos (R01–R05) | 5 | 5 |

### Hallazgos y correcciones

- **Aplicación: 0 defectos.** No se modificó código de módulos.
- **Guion de pruebas: 11 aserciones corregidas** (token CSRF faltante en el POST de login, 200 vs 403 en permisos, método GET donde hacía falta POST, seguimiento de redirecciones, anclas con acento y mensaje en la página de formulario en vez de la lista). Detalle en el informe §4.
- **Recuentos verificados sin alterar**: 38 funcionalidades, 30 interfaces, 5 reglas, 47 asociaciones F×P.
- **Documentos actualizados** (autorizado): `README.md` y `10_productos_y_entregables.md` a 35/38 con F03/F33/F34 fuera del alcance V2 y Spring Security reconocido; nuevos `20_explicacion_tecnica_modulos.md` y `21_matriz_correspondencia.md`.
