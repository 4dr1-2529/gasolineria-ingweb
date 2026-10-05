# Catálogo de interfaces P01–P30 · Estación Nexo

Estación Nexo define **30 interfaces** (P01–P30) y tiene **31 archivos HTML** en la raíz: publicidad.html es una segunda presentación de P01, no una interfaz adicional. Además, [bpmn.html](bpmn.html) es un diagrama del proceso y queda fuera de este catálogo. Cada interfaz indica propósito, actor, archivo, funcionalidades, entidades, reglas y su contenido concreto. Cada interfaz tiene al menos una funcionalidad y ninguna funcionalidad carece de interfaz.

De las 30 interfaces, **24 operan en la versión Spring Boot** con sus rutas y vistas JSP; **6 existen sólo en la maqueta V1**: P01 (portada), P02 (login), P03 (tablero), P04 (contacto), P17 y P27 (gestión de conceptos económicos, que en V2 son datos internos de Finanzas).

**Interfaces sin regla de negocio: 6** — P01 y P04 (contenido público), P02 y P03 (acceso y tablero) y P17 y P18 (consultas de catálogo y personal). Las 24 restantes se rigen por RN01–RN05.

## Índice

| Código | Interfaz | Archivo V1 | Funcionalidades | Reglas | Versión V2 |
|---|---|---|---|---|---|
| [P01](#p01--inicio--publicidad) | Inicio / Publicidad | index.html, publicidad.html | F33 | No aplica | Sólo V1 |
| [P02](#p02--login) | Login | login.html | F01 | No aplica | Sólo V1 |
| [P03](#p03--dashboard) | Dashboard | dashboard.html | F02, F03 | No aplica | Sólo V1 |
| [P04](#p04--contacto) | Contacto | contacto.html | F34 | No aplica | Sólo V1 |
| [P05](#p05--categorías) | Categorías | categorias.html | F04, F05, F06, F07, F08 | RN02 | /categorias/list |
| [P06](#p06--combustibles) | Combustibles | combustibles.html | F10, F12 | RN02 | /combustibles/list |
| [P07](#p07--formulario-combustible) | Formulario combustible | combustible-form.html | F09, F11 | RN02 | /combustibles/crear |
| [P08](#p08--registrar-venta) | Registrar venta | ventas.html | F18, F20 | RN01, RN02, RN03, RN04 | /ventas/crear |
| [P09](#p09--historial-de-ventas) | Historial de ventas | ventas-historial.html | F21 | RN03, RN04 | /ventas/list |
| [P10](#p10--detalle-de-venta) | Detalle de venta | venta-detalle.html | F22 | RN03, RN04 | /ventas/detalle |
| [P11](#p11--existencias) | Existencias | inventario.html | F18 | RN01 | /inventario/list |
| [P12](#p12--entrada-de-combustible) | Entrada de combustible | inventario-entrada.html | F16 | RN01, RN03 | /inventario/entrada/crear |
| [P13](#p13--salida-de-combustible) | Salida de combustible | inventario-salida.html | F17, F18 | RN01 | /inventario/salida/crear |
| [P14](#p14--movimientos-de-inventario) | Movimientos de inventario | inventario-movimientos.html | F19 | RN01, RN03 | /inventario/entradas y /inventario/salidas |
| [P15](#p15--resumen-financiero) | Resumen financiero | finanzas.html | F28 | RN04 | /finanzas/list |
| [P16](#p16--ingresos-y-egresos-de-caja) | Ingresos y egresos de caja | movimiento-economico.html | F26, F27 | RN04 | /finanzas/ingresos y /finanzas/egresos |
| [P17](#p17--conceptos-económicos) | Conceptos económicos | conceptos.html | F24 | No aplica | Sólo V1 |
| [P18](#p18--empleados) | Empleados | empleados.html | F30 | No aplica | /empleados/list |
| [P19](#p19--usuarios) | Usuarios | usuarios.html | F32 | RN02 | /usuarios/list |
| [P20](#p20--compras) | Compras | compras.html | F13, F14 | RN03, RN04 | /compras/list |
| [P21](#p21--detalle-de-compra) | Detalle de compra | compra-detalle.html | F15 | RN03, RN04 | /compras/detalle |
| [P22](#p22--formulario-categoría) | Formulario categoría | categoria-form.html | F04, F07, F08 | RN02 | /categorias/crear |
| [P23](#p23--formulario-compra) | Formulario compra | compra-form.html | F13 | RN03, RN04 | /compras/crear |
| [P24](#p24--formulario-venta) | Formulario venta | venta-form.html | F20 | RN01, RN02, RN03, RN04 | /ventas/crear |
| [P25](#p25--formulario-empleado) | Formulario empleado | empleado-form.html | F29, F31 | RN02 | /empleados/crear y /empleados/editar |
| [P26](#p26--formulario-usuario) | Formulario usuario | usuario-form.html | F32 | RN02 | /usuarios/crear y /usuarios/editar |
| [P27](#p27--formulario-concepto) | Formulario concepto | concepto-form.html | F23, F25 | RN02 | Sólo V1 |
| [P28](#p28--mi-asistencia) | Mi asistencia | mi-asistencia.html | F35, F36, F37 | RN05 | /asistencia/mi |
| [P29](#p29--control-de-asistencia) | Control de asistencia | control-asistencia.html | F38 | RN05 | /asistencia/control |
| [P30](#p30--detalle-de-movimiento-de-caja) | Detalle de movimiento de caja | movimiento-detalle.html | F28 | RN04 | /finanzas/detalle |

## P01 · Inicio / Publicidad

- **Propósito:** Presentar la estación con sus precios de referencia, sus beneficios y la navegación pública, y enlazar al acceso y al contacto. La interfaz publicidad.html repite esta presentación.
- **Actor:** Visitante.
- **Archivo:** [index.html](../index.html), [publicidad.html](../publicidad.html). Versión V2: no existe portada; cada módulo se abre por su ruta.
- **Funcionalidades:** F33.
- **Entidades:** No aplica — contenido público.
- **Reglas:** No aplica.
- **Contenido:** Precios de referencia por litro (S/ 5.00, S/ 6.00, S/ 4.00), beneficios del surtidor digital, contacto y acceso al sistema.

## P02 · Login

- **Propósito:** Representar el acceso con usuario y contraseña. En la maqueta V1 el envío navega al tablero sin comprobar credenciales.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [login.html](../login.html). Versión V2: no existe este módulo — no hay autenticación ni sesión.
- **Funcionalidades:** F01.
- **Entidades:** Usuario.
- **Reglas:** No aplica.
- **Contenido:** Campos de usuario y contraseña, opción «recordarme» y enlace de registro visual.

## P03 · Dashboard

- **Propósito:** Consultar un panorama de la operación del día y de la semana con cinco indicadores y cinco series diarias, y navegar a cada módulo.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [dashboard.html](../dashboard.html). Versión V2: no existe tablero; el resumen económico vive en /finanzas/list.
- **Funcionalidades:** F02, F03.
- **Entidades:** Venta, DetalleVenta, Producto, MovimientoCaja.
- **Reglas:** No aplica.
- **Contenido:** Indicadores de ventas, litros, ingresos (S/ 370.00), egresos (S/ 1,350.00) y saldo (S/ 3,430.00); series diarias del 04 al 10 de septiembre de 2026; últimas ventas; enlaces a los once módulos y cierre de sesión.

## P04 · Contacto

- **Propósito:** Mostrar los canales de contacto y recibir un mensaje con nombre, correo, asunto y texto.
- **Actor:** Visitante.
- **Archivo:** [contacto.html](../contacto.html). Versión V2: no existe este módulo; el mensaje no se almacena.
- **Funcionalidades:** F34.
- **Entidades:** No aplica — contenido público.
- **Reglas:** No aplica.
- **Contenido:** Formulario de contacto y datos de la estación.

## P05 · Categorías

- **Propósito:** Listar las familias de combustible con su nombre, descripción y estado, y representar el alta, la edición y el cambio de estado de una categoría. En V2, /categorias/list muestra las familias y enlaza al catálogo de combustibles.
- **Actor:** Administrador.
- **Archivo:** [categorias.html](../categorias.html). Versión V2: `GET /categorias/list` → categoria/lista.jsp.
- **Funcionalidades:** F04, F05, F06, F07, F08.
- **Entidades:** Categoria, Producto (los combustibles que cada familia agrupa).
- **Reglas:** RN02 — la categoría se edita y se desactiva, nunca se elimina.
- **Contenido:** Código, nombre, descripción, combustibles asociados y estado; enlace al formulario de categoría.

## P06 · Combustibles

- **Propósito:** Listar el catálogo de combustibles con su categoría, precio por litro, existencias y estado, y representar el cambio de estado de un combustible.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [combustibles.html](../combustibles.html). Versión V2: `GET /combustibles/list` → producto/lista.jsp.
- **Funcionalidades:** F10, F12.
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02 — un producto Inactivo permanece en la lista con su estado y no se elimina.
- **Contenido:** Código, nombre, categoría, unidad, precio por litro, stock y estado; enlace al formulario de combustible.

## P07 · Formulario combustible

- **Propósito:** Dar de alta un combustible con categoría, nombre, unidad, precio por litro, stock inicial y estado; en la maqueta V1 el mismo formulario edita el producto existente.
- **Actor:** Administrador.
- **Archivo:** [combustible-form.html](../combustible-form.html). Versión V2: `GET/POST /combustibles/crear` → producto/crear.jsp (alta con seis campos obligatorios; sin edición).
- **Funcionalidades:** F09, F11.
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02 — el alta y la edición conservan el registro; la baja es por estado.
- **Contenido:** Categoría, nombre, unidad, precio por litro, stock y estado.

## P08 · Registrar venta

- **Propósito:** Representar una venta con el producto, la cantidad, las existencias al confirmar y el importe, enlazando al formulario de venta y al historial.
- **Actor:** Operador / Vendedor.
- **Archivo:** [ventas.html](../ventas.html). Versión V2: `GET /ventas/crear` → venta/crear.jsp (el alta efectiva, con comprobación de existencias, ocurre en el formulario de venta).
- **Funcionalidades:** F18, F20.
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN01 (no vende más de lo existente), RN02 (sólo productos activos), RN03 (detalle y salida una sola vez) y RN04 (ingreso único de caja).
- **Contenido:** Producto, cantidad, existencias al confirmar e importe; enlaces al formulario de venta, al historial y al detalle.

## P09 · Historial de ventas

- **Propósito:** Listar las ventas confirmadas con su ingreso de caja asociado y enlazar al detalle de cada una.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [ventas-historial.html](../ventas-historial.html). Versión V2: `GET /ventas/list` → venta/lista.jsp.
- **Funcionalidades:** F21.
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoCaja.
- **Reglas:** RN03, RN04 — cada fila muestra una venta con sus detalles y su único ingreso.
- **Contenido:** Código, fecha, combustible, litros, total, ingreso asociado, operador y estado.

## P10 · Detalle de venta

- **Propósito:** Consultar una venta con sus líneas, su salida de inventario y su ingreso de caja, de forma trazable.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [venta-detalle.html](../venta-detalle.html). Versión V2: `GET /ventas/detalle` → venta/detalle.jsp.
- **Funcionalidades:** F22.
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03, RN04.
- **Contenido:** Cabecera de la venta (V001, V002, V003), detalle de líneas, total, movimiento de inventario y movimiento de caja; acumulado del día de 80 litros y S/ 370.00.

## P11 · Existencias

- **Propósito:** Consultar la disponibilidad física en litros por producto con su umbral visual y el total del día.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario.html](../inventario.html). Versión V2: `GET /inventario/list` → inventario/lista.jsp (marca «Stock bajo» por debajo de 1,000 L).
- **Funcionalidades:** F18.
- **Entidades:** Producto.
- **Reglas:** RN01 — la consulta sostiene la comprobación de existencias antes de operar.
- **Contenido:** Existencia por producto, umbral visual y total físico del día (6,700 + 300 − 80 = 6,920 litros); enlaces a entrada, salida y libro de movimientos.

## P12 · Entrada de combustible

- **Propósito:** Registrar una entrada manual de inventario con producto, litros, motivo y responsable; las entradas originadas en una compra se crean con la compra y se consultan en el libro.
- **Actor:** Administrador.
- **Archivo:** [inventario-entrada.html](../inventario-entrada.html). Versión V2: `GET/POST /inventario/entrada/crear` → inventario/entrada.jsp; el listado de entradas, `/inventario/entradas` → inventario/entradas.jsp.
- **Funcionalidades:** F16.
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN01 — la entrada incrementa las existencias; RN03 — la entrada de una compra se produce una sola vez con la compra.
- **Contenido:** Producto, cantidad en litros, fecha y hora, motivo y responsable.

## P13 · Salida de combustible

- **Propósito:** Registrar un retiro físico de combustible con comprobación de stock antes de descontar, mostrando la existencia actual y la proyectada.
- **Actor:** Operador / Vendedor.
- **Archivo:** [inventario-salida.html](../inventario-salida.html). Versión V2: `GET/POST /inventario/salida/crear` → inventario/salida.jsp; el listado de salidas, `/inventario/salidas` → inventario/salidas.jsp.
- **Funcionalidades:** F17, F18.
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** RN01 — la salida se rechaza si supera el stock disponible.
- **Contenido:** Producto, cantidad en litros, motivo y responsable, con existencia actual y proyectada.

## P14 · Movimientos de inventario

- **Propósito:** Consultar el libro de entradas y salidas con el saldo inicial del día, para verificar la trazabilidad física del combustible.
- **Actor:** Administrador.
- **Archivo:** [inventario-movimientos.html](../inventario-movimientos.html). Versión V2: `GET /inventario/entradas` y `GET /inventario/salidas` → inventario/entradas.jsp e inventario/salidas.jsp.
- **Funcionalidades:** F19.
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN01, RN03 — permite comprobar que el stock nunca queda negativo y que cada compra y venta produjeron sus efectos una sola vez.
- **Contenido:** Libro de entradas y salidas con saldo inicial, entradas de la compra C001 (MI001–MI003) y salidas por venta (MI004–MI006).

## P15 · Resumen financiero

- **Propósito:** Consultar la conciliación de caja del día y el listado de movimientos con su detalle.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [finanzas.html](../finanzas.html). Versión V2: `GET /finanzas/list` → finanzas/lista.jsp.
- **Funcionalidades:** F28.
- **Entidades:** MovimientoCaja, ConceptoMovimiento, Venta, Compra.
- **Reglas:** RN04 — la conciliación sólo cierra si cada compra y cada venta tienen su único movimiento.
- **Contenido:** KPI de ingresos (S/ 370.00), egresos (S/ 1,350.00) y saldo (S/ 3,430.00) sobre una apertura de S/ 4,410.00; movimientos con código, fecha, concepto, tipo, monto, origen y responsable.

## P16 · Ingresos y egresos de caja

- **Propósito:** Consultar en dos paneles de solo lectura los ingresos y los egresos del día con el detalle de cada movimiento.
- **Actor:** Administrador.
- **Archivo:** [movimiento-economico.html](../movimiento-economico.html). Versión V2: `GET /finanzas/ingresos` y `GET /finanzas/egresos` → finanzas/ingresos.jsp y finanzas/egresos.jsp.
- **Funcionalidades:** F26, F27.
- **Entidades:** MovimientoCaja, ConceptoMovimiento.
- **Reglas:** RN04 — todos los movimientos mostrados nacen de una venta o de una compra y llevan su vínculo; no hay altas manuales.
- **Contenido:** Panel «Ingresos de caja (F26)» con MC003, MC004 y MC005 (total S/ 370.00) y panel «Egresos de caja (F27)» con MC001 (S/ 1,350.00); sin formulario.

## P17 · Conceptos económicos

- **Propósito:** Listar los conceptos que clasifican los movimientos de caja con su tipo y estado: sólo CE01 Venta de combustible (Ingreso) y CE02 Compra de combustible (Egreso).
- **Actor:** Administrador.
- **Archivo:** [conceptos.html](../conceptos.html). Versión V2: sin pantalla propia — los conceptos se usan como columna de los movimientos en /finanzas/list.
- **Funcionalidades:** F24.
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Contenido:** Nombre, tipo (ingreso o egreso) y estado; enlace al formulario de concepto.

## P18 · Empleados

- **Propósito:** Listar el personal con su identidad, cargo, teléfono y estado, y enlazar al formulario de empleado y al control de asistencia.
- **Actor:** Administrador.
- **Archivo:** [empleados.html](../empleados.html). Versión V2: `GET /empleados/list` → empleado/lista.jsp.
- **Funcionalidades:** F30.
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Contenido:** DNI, nombres, apellidos, cargo, teléfono y estado; enlaces al formulario y al control de asistencia.

## P19 · Usuarios

- **Propósito:** Listar las cuentas de acceso con su empleado asociado, rol y estado, y enlazar al formulario de cuenta.
- **Actor:** Administrador.
- **Archivo:** [usuarios.html](../usuarios.html). Versión V2: `GET /usuarios/list` → usuario/lista.jsp.
- **Funcionalidades:** F32.
- **Entidades:** Usuario, Empleado.
- **Reglas:** RN02 — la cuenta se edita y se desactiva, nunca se elimina, y conserva la responsabilidad de sus operaciones.
- **Contenido:** Username, empleado asociado, rol y estado; una cuenta como máximo por empleado; enlace al formulario.

## P20 · Compras

- **Propósito:** Registrar y consultar el abastecimiento: listado de compras con su efecto en inventario y caja, y acceso al alta y al detalle.
- **Actor:** Administrador.
- **Archivo:** [compras.html](../compras.html). Versión V2: `GET /compras/list` → compra/lista.jsp.
- **Funcionalidades:** F13, F14.
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03 — cada compra con sus detalles y entradas una sola vez; RN04 — su egreso único.
- **Contenido:** KPI de compras, litros adquiridos y efecto en inventario; listado con código, fecha, proveedor, combustibles, litros, total, estado y detalle; enlace al formulario de compra.

## P21 · Detalle de compra

- **Propósito:** Consultar una compra con sus líneas, el egreso asociado y las entradas de inventario que generó.
- **Actor:** Administrador.
- **Archivo:** [compra-detalle.html](../compra-detalle.html). Versión V2: `GET /compras/detalle` → compra/detalle.jsp.
- **Funcionalidades:** F15.
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03, RN04.
- **Contenido:** Cabecera de la compra C001 con proveedor y egreso asociado (300 litros, S/ 1,350.00), líneas con cantidad, precio de compra y subtotal, total, y tabla de entradas generadas.

## P22 · Formulario categoría

- **Propósito:** Dar de alta una categoría con nombre, descripción y estado; en la maqueta V1 el mismo formulario edita una categoría existente y cambia su estado.
- **Actor:** Administrador.
- **Archivo:** [categoria-form.html](../categoria-form.html). Versión V2: `GET/POST /categorias/crear` → categoria/crear.jsp (alta; el nombre es el campo obligatorio).
- **Funcionalidades:** F04, F07, F08.
- **Entidades:** Categoria.
- **Reglas:** RN02 — guardar actualiza el registro y la baja es por estado, no por borrado.
- **Contenido:** Código, nombre, descripción y estado.

## P23 · Formulario compra

- **Propósito:** Registrar una compra con proveedor, fecha y líneas de producto, guardando en un solo paso la compra, sus detalles, las entradas de inventario y el egreso de caja.
- **Actor:** Administrador.
- **Archivo:** [compra-form.html](../compra-form.html). Versión V2: `GET/POST /compras/crear` → compra/crear.jsp (proveedor de 3 a 60 caracteres, fecha y líneas obligatorias).
- **Funcionalidades:** F13.
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03 — detalles y entradas una sola vez; RN04 — egreso único vinculado a la compra.
- **Contenido:** Proveedor, fecha, líneas con producto y cantidad en litros, y total de la compra.

## P24 · Formulario venta

- **Propósito:** Registrar una venta de combustible comprobando que el producto esté activo y que la cantidad no supere el stock, y guardando en un solo paso venta, detalle, salida de inventario e ingreso de caja.
- **Actor:** Operador / Vendedor.
- **Archivo:** [venta-form.html](../venta-form.html). Versión V2: `GET/POST /ventas/crear` → venta/crear.jsp (producto, cantidad y operador obligatorios; rechazo sin efectos si falla alguna comprobación).
- **Funcionalidades:** F20.
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN01 (stock suficiente), RN02 (producto activo), RN03 (detalle y salida únicos) y RN04 (ingreso único).
- **Contenido:** Producto, cantidad, existencias al confirmar e importe.

## P25 · Formulario empleado

- **Propósito:** Dar de alta un empleado con sus datos de personal; en la maqueta V1 y en V2 el mismo formulario edita al empleado y cambia su estado.
- **Actor:** Administrador.
- **Archivo:** [empleado-form.html](../empleado-form.html). Versión V2: `GET/POST /empleados/crear` y `GET/POST /empleados/editar` → empleado/crear.jsp y empleado/editar.jsp (seis campos obligatorios, incluido el estado).
- **Funcionalidades:** F29, F31.
- **Entidades:** Empleado.
- **Reglas:** RN02 — la edición y la desactivación conservan el registro y su historial.
- **Contenido:** DNI, nombres, apellidos, cargo, teléfono y estado.

## P26 · Formulario usuario

- **Propósito:** Dar de alta una cuenta de acceso; en la maqueta V1 y en V2 el mismo formulario edita la cuenta y cambia su estado.
- **Actor:** Administrador.
- **Archivo:** [usuario-form.html](../usuario-form.html). Versión V2: `GET/POST /usuarios/crear` y `GET/POST /usuarios/editar` → usuario/crear.jsp y usuario/editar.jsp.
- **Funcionalidades:** F32.
- **Entidades:** Usuario, Empleado.
- **Reglas:** RN02 — la cuenta se desactiva, no se elimina.
- **Contenido:** Username, contraseña, empleado asociado, rol y estado; una cuenta como máximo por empleado.

## P27 · Formulario concepto

- **Propósito:** Dar de alta un concepto económico con nombre, tipo y estado; en la maqueta V1 el mismo formulario edita el concepto y cambia su estado.
- **Actor:** Administrador.
- **Archivo:** [concepto-form.html](../concepto-form.html). Versión V2: sin formulario propio — la gestión de conceptos no está implementada en V2.
- **Funcionalidades:** F23, F25.
- **Entidades:** ConceptoMovimiento.
- **Reglas:** RN02 — el concepto se edita y se desactiva, nunca se elimina.
- **Contenido:** Nombre, tipo (ingreso o egreso) y estado.

## P28 · Mi asistencia

- **Propósito:** Permite al empleado registrar su entrada, registrar su salida y consultar su historial y su resumen de asistencia, sin selector de empleado: opera sobre el empleado actual.
- **Actor:** Operador / Vendedor (empleado actual de la demostración).
- **Archivo:** [mi-asistencia.html](../mi-asistencia.html). Versión V2: `GET /asistencia/mi` con `POST /asistencia/entrada` y `POST /asistencia/salida` → asistencia/mi.jsp.
- **Funcionalidades:** F35, F36, F37.
- **Entidades:** Asistencia, Empleado.
- **Reglas:** RN05 — empleado activo, jornada propia, un registro por jornada y salida posterior a la entrada.
- **Contenido:** Fecha, hora de entrada, hora de salida, estado (Presente o Falta) y observación opcional; historial propio y bloque «Resumen del periodo» con días presentes y faltas.

## P29 · Control de asistencia

- **Propósito:** Consultar la asistencia de todo el personal con filtro por empleado, una fila por empleado y fecha.
- **Actor:** Administrador.
- **Archivo:** [control-asistencia.html](../control-asistencia.html). Versión V2: `GET /asistencia/control` → asistencia/control.jsp (selector «Todo el personal»).
- **Funcionalidades:** F38.
- **Entidades:** Asistencia, Empleado.
- **Reglas:** RN05 — un solo registro por empleado y fecha, con estado calculado a partir de las horas.
- **Contenido:** Empleado, fecha, hora de entrada, hora de salida, estado (Presente o Falta) y observación; una sola fila por empleado y día.

## P30 · Detalle de movimiento de caja

- **Propósito:** Consultar un movimiento de caja con su concepto, su origen (venta o compra) y el responsable que lo originó.
- **Actor:** Administrador.
- **Archivo:** [movimiento-detalle.html](../movimiento-detalle.html). Versión V2: `GET /finanzas/detalle` → finanzas/detalle.jsp.
- **Funcionalidades:** F28.
- **Entidades:** MovimientoCaja, ConceptoMovimiento, Venta, Compra.
- **Reglas:** RN04 — el movimiento identifica siempre la operación que lo originó.
- **Contenido:** Código, fecha, concepto, tipo, monto, origen (venta o compra) y responsable del movimiento.

## Contadores

| Indicador | Valor |
|---|---:|
| Interfaces totales | 30 |
| Archivos HTML en la raíz | 31 |
| Funcionalidades totales | 38 |
| Entidades | 12 |
| Reglas de negocio | 5 |
| Interfaces con al menos una funcionalidad | 30 |
| Interfaces sin funcionalidad | 0 |
| Interfaces con al menos una regla | 24 |
| Interfaces sin regla | 6 |
| Interfaces con ruta en la versión V2 | 24 |
| Interfaces sólo en la maqueta V1 | 6 |
