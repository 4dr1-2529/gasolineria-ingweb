# Funcionalidades F01–F38 · Estación Nexo

Estación Nexo define **38 funcionalidades**: capacidades reales del sistema, no campos ni enlaces. Cada funcionalidad se asocia a al menos una interfaz y, cuando el negocio la restringe, a una o más de las cinco reglas (RN01–RN05).

**Revisión de coherencia (resultado).** Cada una de las cinco entidades mantenibles por interfaz —Categoría, Combustible, Concepto económico, Empleado y Usuario— tiene consulta, registro, edición y cambio de estado. Las acciones de mutación se expresan como *registrar*, *editar* y *cambiar estado*; no se usa «eliminar» porque el sistema conserva historial y aplica baja lógica (RN02). La revisión no encontró funcionalidades redundantes ni duplicadas: se mantienen F01–F38 completas.

**Estado en la versión V2 (Spring Boot, datos en memoria).** Operan con lógica real: F04, F05, F09, F10, F13–F22, F26–F32 y F35–F38 (25 funcionalidades). Existen sólo en la maqueta V1: F01–F03 (acceso y tablero), F06 (combustibles por categoría), F07, F08, F11, F12 (edición y estado de catálogo), F23–F25 (gestión de conceptos), F33 y F34 (portada y contacto).

## Clasificación

### A. CRUD — mantenimiento de entidades (15)

Acciones del sistema: **listar · registrar · editar · cambiar estado/desactivar**. Criterio: toda entidad con interfaz propia cubre las cuatro acciones; donde la edición y el cambio de estado comparten formulario, una sola funcionalidad representa ambas.

| Entidad | Registrar | Listar | Editar | Cambiar estado |
|---|---|---|---|---|
| Categoria | F04 | F05 | F07 | F08 |
| Producto / Combustible | F09 | F10 | F11 | F12 |
| ConceptoMovimiento | F23 | F24 | F25 | F25 |
| Empleado | F29 | F30 | F31 | F31 |
| Usuario | F32 | F32 | F32 | F32 |

Notas: F25 agrupa edición y cambio de estado del concepto (comparten formulario P27); F31 hace lo propio con el empleado (P25, cuyo formulario incluye el estado); F32 gestiona la cuenta completa: el listado P19 consulta y el formulario P26 registra, edita y cambia el estado.

### B. Procesos de negocio (6)

| Proceso | Funcionalidades |
|---|---|
| Compra (abastecimiento) | F13 Registrar compra de combustible |
| Venta (operación) | F20 Registrar venta |
| Inventario (movimiento físico) | F16 Registrar entrada de combustible, F17 Registrar salida de combustible |
| Asistencia (jornada del personal) | F35 Registrar asistencia |
| Contacto público | F34 Enviar mensaje de contacto |

Finanzas no tiene proceso propio de alta: los movimientos de caja nacen de la compra (F13) y de la venta (F20) conforme a RN04, sus consultas son F26–F28 y sus clasificadores se mantienen con F23–F25.

### C. Consultas (15)

| Funcionalidad | Qué consulta |
|---|---|
| F03 Consultar dashboard | Indicadores y series de la operación |
| F06 Consultar combustibles por categoría | Combustibles que agrupa cada categoría |
| F14 Consultar compras | Historial de abastecimiento |
| F15 Consultar detalle de compra | Líneas, entradas y egreso de una compra |
| F18 Consultar existencias | Saldo físico en litros por producto |
| F19 Consultar movimientos de inventario | Libro de entradas y salidas |
| F21 Consultar ventas | Historial de ventas con su ingreso |
| F22 Consultar detalle de venta | Líneas, salida y ingreso de una venta |
| F26 Consultar ingresos de caja | Ingresos del día y su origen |
| F27 Consultar egresos de caja | Egresos del día y su origen |
| F28 Consultar movimientos y saldo de caja | Movimientos y conciliación apertura + ingresos − egresos |
| F33 Consultar la portada pública | Presentación pública de la estación |
| F36 Consultar mi asistencia | Historial propio de marcaciones |
| F37 Consultar mi resumen de asistencia | Días presentes y faltas del período |
| F38 Consultar asistencia del personal | Asistencia de todo el personal con filtro |

### D. Control de acceso (2)

| Funcionalidad | Qué hace |
|---|---|
| F01 Iniciar sesión | Representa el acceso con usuario y contraseña |
| F02 Cerrar sesión | Representa el cierre del panel |

## Índice

| Código | Nombre | Tipo | Módulo / Proceso | Interfaz(es) | Reglas |
|---|---|---|---|---|---|
| [F01](#f01--iniciar-sesión) | Iniciar sesión | Acceso | Acceso | P02 | No aplica |
| [F02](#f02--cerrar-sesión) | Cerrar sesión | Acceso | Acceso | P03 | No aplica |
| [F03](#f03--consultar-dashboard) | Consultar dashboard | Consulta | Operación | P03 | No aplica |
| [F04](#f04--registrar-categoría) | Registrar categoría | CRUD — Registrar | Catálogo | P05, P22 | No aplica |
| [F05](#f05--consultar-categorías) | Consultar categorías | CRUD — Listar | Catálogo | P05 | No aplica |
| [F06](#f06--consultar-combustibles-por-categoría) | Consultar combustibles por categoría | Consulta | Catálogo | P05 | No aplica |
| [F07](#f07--editar-categoría) | Editar categoría | CRUD — Editar | Catálogo | P05, P22 | RN02 |
| [F08](#f08--activardesactivar-categoría) | Activar/desactivar categoría | CRUD — Cambiar estado | Catálogo | P05, P22 | RN02 |
| [F09](#f09--registrar-combustible) | Registrar combustible | CRUD — Registrar | Catálogo | P07 | No aplica |
| [F10](#f10--consultar-combustibles) | Consultar combustibles | CRUD — Listar | Catálogo | P06 | No aplica |
| [F11](#f11--editar-combustible) | Editar combustible | CRUD — Editar | Catálogo | P07 | RN02 |
| [F12](#f12--activardesactivar-combustible) | Activar/desactivar combustible | CRUD — Cambiar estado | Catálogo | P06 | RN02 |
| [F13](#f13--registrar-compra-de-combustible) | Registrar compra de combustible | Proceso | Abastecimiento | P20, P23 | RN03, RN04 |
| [F14](#f14--consultar-compras) | Consultar compras | Consulta | Abastecimiento | P20 | RN03, RN04 |
| [F15](#f15--consultar-detalle-de-compra) | Consultar detalle de compra | Consulta | Abastecimiento | P21 | RN03, RN04 |
| [F16](#f16--registrar-entrada-de-combustible) | Registrar entrada de combustible | Proceso | Control (inventario) | P12 | RN01, RN03 |
| [F17](#f17--registrar-salida-de-combustible) | Registrar salida de combustible | Proceso | Control (inventario) | P13 | RN01 |
| [F18](#f18--consultar-existencias) | Consultar existencias | Consulta | Control (inventario) | P08, P11, P13 | RN01 |
| [F19](#f19--consultar-movimientos-de-inventario) | Consultar movimientos de inventario | Consulta | Control (inventario) | P14 | RN01, RN03 |
| [F20](#f20--registrar-venta) | Registrar venta | Proceso | Operación | P08, P24 | RN01, RN02, RN03, RN04 |
| [F21](#f21--consultar-ventas) | Consultar ventas | Consulta | Operación | P09 | RN03, RN04 |
| [F22](#f22--consultar-detalle-de-venta) | Consultar detalle de venta | Consulta | Operación | P10 | RN03, RN04 |
| [F23](#f23--registrar-concepto-económico) | Registrar concepto económico | CRUD — Registrar | Finanzas | P27 | No aplica |
| [F24](#f24--consultar-conceptos-económicos) | Consultar conceptos económicos | CRUD — Listar | Finanzas | P17 | No aplica |
| [F25](#f25--editaractivardesactivar-concepto-económico) | Editar/activar/desactivar concepto económico | CRUD — Editar y estado | Finanzas | P27 | RN02 |
| [F26](#f26--consultar-ingresos-de-caja) | Consultar ingresos de caja | Consulta | Finanzas | P16 | RN04 |
| [F27](#f27--consultar-egresos-de-caja) | Consultar egresos de caja | Consulta | Finanzas | P16 | RN04 |
| [F28](#f28--consultar-movimientos-y-saldo-de-caja) | Consultar movimientos y saldo de caja | Consulta | Finanzas | P15, P30 | RN04 |
| [F29](#f29--registrar-empleado) | Registrar empleado | CRUD — Registrar | Personal | P25 | No aplica |
| [F30](#f30--consultar-empleados) | Consultar empleados | CRUD — Listar | Personal | P18 | No aplica |
| [F31](#f31--editaractivardesactivar-empleado) | Editar/activar/desactivar empleado | CRUD — Editar y estado | Personal | P25 | RN02 |
| [F32](#f32--gestionar-usuarios) | Gestionar usuarios | CRUD — Registrar, editar y estado | Personal | P19, P26 | RN02 |
| [F33](#f33--consultar-la-portada-pública) | Consultar la portada pública | Consulta | Público | P01 | No aplica |
| [F34](#f34--enviar-mensaje-de-contacto) | Enviar mensaje de contacto | Proceso | Público | P04 | No aplica |
| [F35](#f35--registrar-asistencia) | Registrar asistencia | Proceso | Personal (asistencia) | P28 | RN05 |
| [F36](#f36--consultar-mi-asistencia) | Consultar mi asistencia | Consulta | Personal (asistencia) | P28 | RN05 |
| [F37](#f37--consultar-mi-resumen-de-asistencia) | Consultar mi resumen de asistencia | Consulta | Personal (asistencia) | P28 | RN05 |
| [F38](#f38--consultar-asistencia-del-personal) | Consultar asistencia del personal | Consulta | Personal (asistencia) | P29 | RN05 |

## F01 · Iniciar sesión

- **Tipo:** Acceso.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Presenta el formulario de acceso con usuario y contraseña y representa el ingreso al panel interno; en la maqueta V1 el envío navega al dashboard sin comprobar credenciales.
- **Resultado:** Panel visible. La versión V2 no incluye este módulo: no hay sesión ni autenticación y cada módulo se consulta por su ruta.
- **Interfaces:** P02 ([login.html](../login.html)).
- **Entidades:** Usuario.
- **Reglas:** No aplica — es acceso, no una restricción del negocio.

## F02 · Cerrar sesión

- **Tipo:** Acceso.
- **Actor:** Administrador.
- **Descripción:** Representa el cierre del panel: en la maqueta V1 el enlace «Cerrar sesión» devuelve al login; no hay sesión que invalidar.
- **Resultado:** Login visible; sin cambios en ningún registro.
- **Interfaces:** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** No aplica.
- **Reglas:** No aplica — es navegación.

## F03 · Consultar dashboard

- **Tipo:** Consulta.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Presenta el resumen de la operación: cinco indicadores (ventas, litros, ingresos, egresos y saldo) y cinco series diarias del 04 al 10 de septiembre de 2026 con las últimas ventas.
- **Resultado:** Panorama del día en pantalla. La versión V2 no tiene tablero: cada módulo se consulta por su ruta y el resumen económico vive en `/finanzas/list`.
- **Interfaces:** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** Venta, DetalleVenta, Producto, MovimientoCaja.
- **Reglas:** No aplica — es una consulta de resumen.

## F04 · Registrar categoría

- **Tipo:** CRUD — Registrar.
- **Actor:** Administrador.
- **Descripción:** Da de alta una categoría con nombre, descripción y estado; en la versión V2 el alta ocurre en `/categorias/crear` y el nombre es el campo que exige el formulario.
- **Resultado:** Categoría nueva visible en el listado mientras la aplicación corre.
- **Interfaces:** P05 ([categorias.html](../categorias.html)), P22 ([categoria-form.html](../categoria-form.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica — alta de un registro nuevo que no toca registros existentes.

## F05 · Consultar categorías

- **Tipo:** CRUD — Listar.
- **Actor:** Administrador.
- **Descripción:** Muestra las familias del catálogo con su nombre, descripción y estado; en la versión V2, `/categorias/list`.
- **Resultado:** Catálogo de familias visible.
- **Interfaces:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica — consulta de solo lectura.

## F06 · Consultar combustibles por categoría

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Muestra, dentro de cada categoría, los combustibles que agrupa con enlace a su ficha. La maqueta V1 lo presenta en categorias.html; la versión V2 lista las familias en `/categorias/list` y enlaza al catálogo `/combustibles/list`.
- **Resultado:** La relación categoría–combustible visible.
- **Interfaces:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica — consulta de solo lectura.

## F07 · Editar categoría

- **Tipo:** CRUD — Editar.
- **Actor:** Administrador.
- **Descripción:** Modifica el nombre y la descripción de una categoría existente desde su formulario. La versión V2 no expone edición de categorías: `/categorias/crear` cubre el alta y `/categorias/list` la consulta.
- **Resultado:** Categoría actualizada en la maqueta V1.
- **Interfaces:** P05 ([categorias.html](../categorias.html)), P22 ([categoria-form.html](../categoria-form.html)).
- **Entidades:** Categoria.
- **Reglas:** RN02 — la edición actualiza el registro y nunca lo elimina.

## F08 · Activar/desactivar categoría

- **Tipo:** CRUD — Cambiar estado.
- **Actor:** Administrador.
- **Descripción:** Cambia el estado de una categoría entre Activo e Inactivo. En la versión V2 el estado se observa en el listado; el cambio de estado se representa en la maqueta V1.
- **Resultado:** Categoría con su nuevo estado: Inactiva fuera de las operaciones nuevas y presente en las consultas históricas.
- **Interfaces:** P05 ([categorias.html](../categorias.html)), P22 ([categoria-form.html](../categoria-form.html)).
- **Entidades:** Categoria.
- **Reglas:** RN02 — la desactivación sustituye a la eliminación.

## F09 · Registrar combustible

- **Tipo:** CRUD — Registrar.
- **Actor:** Administrador.
- **Descripción:** Da de alta un combustible con categoría, nombre, unidad, precio por litro, stock inicial y estado; en la versión V2, `/combustibles/crear` exige esos seis campos.
- **Resultado:** Combustible nuevo en el catálogo con su stock inicial, disponible para compras y ventas.
- **Interfaces:** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** No aplica — alta de un registro nuevo.

## F10 · Consultar combustibles

- **Tipo:** CRUD — Listar.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Lista el catálogo con categoría, precio por litro, existencias y estado; en la versión V2, `/combustibles/list`.
- **Resultado:** Catálogo visible para decidir qué combustible operar.
- **Interfaces:** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** No aplica — consulta de solo lectura.

## F11 · Editar combustible

- **Tipo:** CRUD — Editar.
- **Actor:** Administrador.
- **Descripción:** Modifica nombre, categoría, precio y otros datos de un combustible existente desde su formulario (maqueta V1: combustible-form.html). La versión V2 no expone edición de combustibles: sólo alta y consulta.
- **Resultado:** Combustible actualizado en la maqueta V1.
- **Interfaces:** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02 — la edición conserva el registro con su historial.

## F12 · Activar/desactivar combustible

- **Tipo:** CRUD — Cambiar estado.
- **Actor:** Administrador.
- **Descripción:** Cambia el estado de un combustible entre Activo e Inactivo. Un producto Inactivo no puede venderse y conserva sus ventas, detalles y movimientos.
- **Resultado:** Producto con su nuevo estado; desaparece de las operaciones nuevas y permanece en las consultas.
- **Interfaces:** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02 — la desactivación sustituye a la eliminación.

## F13 · Registrar compra de combustible

- **Tipo:** Proceso.
- **Actor:** Administrador.
- **Descripción:** Registra una compra con proveedor (3 a 60 caracteres) y una o más líneas de producto con litros y precio de compra; la fecha y la hora las toma el servidor (America/Lima) y no se digitan en el formulario. En la versión V2, `/compras/crear` crea en un solo guardado la compra, sus detalles, una entrada de inventario por línea y el egreso de caja; si alguna comprobación falla, no se guarda nada.
- **Resultado:** Compra Confirmada con todos sus efectos: detalles, entradas de inventario (MI…) y un único egreso (MC…).
- **Interfaces:** P20 ([compras.html](../compras.html)), P23 ([compra-form.html](../compra-form.html)).
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03 — detalles y entradas una sola vez; RN04 — egreso único vinculado a la compra.

## F14 · Consultar compras

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Lista las compras con fecha, proveedor, litros, total, estado y acceso al detalle; en la versión V2, `/compras/list`.
- **Resultado:** Historial de abastecimiento visible, con el efecto de cada compra comprobable.
- **Interfaces:** P20 ([compras.html](../compras.html)).
- **Entidades:** Compra, DetalleCompra, Producto.
- **Reglas:** RN03, RN04 — la consulta permite verificar que cada compra tiene sus efectos completos y su egreso único.

## F15 · Consultar detalle de compra

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Muestra las líneas de una compra, su total, el egreso asociado y las entradas de inventario generadas; en la versión V2, `/compras/detalle`.
- **Resultado:** Trazabilidad completa de la compra: qué se compró, qué entró y qué salió de caja.
- **Interfaces:** P21 ([compra-detalle.html](../compra-detalle.html)).
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03, RN04.

## F16 · Registrar entrada de combustible

- **Tipo:** Proceso.
- **Actor:** Administrador.
- **Descripción:** Registra una entrada manual de inventario con producto, litros, motivo y responsable. En la versión V2, `/inventario/entrada/crear` suma los litros al stock una sola vez; las entradas originadas en una compra se crean junto con ella.
- **Resultado:** Movimiento de entrada registrado y existencias incrementadas.
- **Interfaces:** P12 ([inventario-entrada.html](../inventario-entrada.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN01 — la entrada incrementa las existencias; RN03 — la entrada de una compra se produce una sola vez con la compra.

## F17 · Registrar salida de combustible

- **Tipo:** Proceso.
- **Actor:** Operador / Vendedor.
- **Descripción:** Registra una salida manual de inventario con producto, cantidad, motivo y responsable. En la versión V2, `/inventario/salida/crear` rechaza la salida si la cantidad supera el stock disponible.
- **Resultado:** Movimiento de salida y existencias descontadas; sin ningún cambio cuando no hay stock suficiente.
- **Interfaces:** P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** RN01 — la salida no puede dejar el stock negativo.

## F18 · Consultar existencias

- **Tipo:** Consulta.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Muestra el saldo físico en litros por producto con su umbral visual (stock bajo por debajo de 1,000 L) y el total. En la versión V2 vive en `/inventario/list` y acompaña a las pantallas de venta y de salida.
- **Resultado:** Existencias visibles antes de operar.
- **Interfaces:** P08 ([ventas.html](../ventas.html)), P11 ([inventario.html](../inventario.html)), P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** Producto.
- **Reglas:** RN01 — la consulta sostiene la comprobación de existencias.

## F19 · Consultar movimientos de inventario

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Muestra el libro de entradas y salidas con el saldo inicial del día, las entradas y las salidas. En la versión V2, `/inventario/entradas` y `/inventario/salidas`.
- **Resultado:** Trazabilidad física del combustible, conciliable con el stock.
- **Interfaces:** P14 ([inventario-movimientos.html](../inventario-movimientos.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN01 — permite comprobar que el stock nunca queda negativo; RN03 — permite ver los efectos de cada compra y cada venta una sola vez.

## F20 · Registrar venta

- **Tipo:** Proceso.
- **Actor:** Operador / Vendedor.
- **Descripción:** Registra una venta con producto, cantidad y operador. En la versión V2, `/ventas/crear` comprueba que el producto exista y esté Activo, que la cantidad sea mayor que cero y que no supere el stock, y en un solo guardado crea la venta, su detalle, la salida de inventario y el ingreso de caja; si alguna comprobación falla, la venta se rechaza sin crear nada.
- **Resultado:** Venta Confirmada con todos sus efectos: detalle, salida de inventario (MI…) y un único ingreso (MC…).
- **Interfaces:** P08 ([ventas.html](../ventas.html)), P24 ([venta-form.html](../venta-form.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN01 — no vende más de lo existente; RN02 — sólo productos activos; RN03 — detalle y salida una sola vez; RN04 — ingreso único vinculado a la venta.

## F21 · Consultar ventas

- **Tipo:** Consulta.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Lista las ventas con fecha, combustible, litros, total, operador, estado e ingreso asociado; en la versión V2, `/ventas/list`.
- **Resultado:** Historial de ventas visible, con el ingreso de cada venta comprobable.
- **Interfaces:** P09 ([ventas-historial.html](../ventas-historial.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoCaja.
- **Reglas:** RN03, RN04 — cada venta del listado tiene sus detalles y su ingreso único.

## F22 · Consultar detalle de venta

- **Tipo:** Consulta.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Muestra cada venta con sus líneas, su salida de inventario y su ingreso de caja; en la versión V2, `/ventas/detalle`.
- **Resultado:** Trazabilidad completa de la venta: qué se vendió, qué salió y qué entró de caja.
- **Interfaces:** P10 ([venta-detalle.html](../venta-detalle.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN03, RN04.

## F23 · Registrar concepto económico

- **Tipo:** CRUD — Registrar.
- **Actor:** Administrador.
- **Descripción:** Da de alta un concepto con nombre, tipo (Ingreso o Egreso) y estado (maqueta V1: concepto-form.html). En la versión V2 los conceptos son datos internos de Finanzas (CE01, CE02) y no tienen formulario propio.
- **Resultado:** Concepto nuevo en el catálogo de la maqueta V1.
- **Interfaces:** P27 ([concepto-form.html](../concepto-form.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica — alta de un registro nuevo.

## F24 · Consultar conceptos económicos

- **Tipo:** CRUD — Listar.
- **Actor:** Administrador.
- **Descripción:** Lista los conceptos con su tipo y estado (maqueta V1: conceptos.html). En la versión V2 no hay pantalla del catálogo: los conceptos aparecen como columna de cada movimiento en `/finanzas/list`.
- **Resultado:** Catálogo de conceptos visible.
- **Interfaces:** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica — consulta de solo lectura.

## F25 · Editar/activar/desactivar concepto económico

- **Tipo:** CRUD — Editar y cambiar estado.
- **Actor:** Administrador.
- **Descripción:** Modifica nombre y tipo de un concepto y cambia su estado entre Activo e Inactivo (maqueta V1: concepto-form.html). La versión V2 no expone esta gestión.
- **Resultado:** Concepto actualizado o desactivado; un concepto con movimientos se desactiva y conserva su historial.
- **Interfaces:** P27 ([concepto-form.html](../concepto-form.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** RN02 — la desactivación sustituye a la eliminación.

## F26 · Consultar ingresos de caja

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Muestra el panel de solo lectura con los ingresos del día y su origen (MC003, MC004 y MC005 → S/ 370.00); en la versión V2, `/finanzas/ingresos`.
- **Resultado:** Ingresos del día visibles, cada uno vinculado a su venta.
- **Interfaces:** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento.
- **Reglas:** RN04 — todos los ingresos mostrados nacen de una venta y llevan su vínculo.

## F27 · Consultar egresos de caja

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Muestra el panel de solo lectura con los egresos del día y su origen (MC001 → S/ 1,350.00, de la compra C001); en la versión V2, `/finanzas/egresos`.
- **Resultado:** Egresos del día visibles, cada uno vinculado a su compra.
- **Interfaces:** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento.
- **Reglas:** RN04 — todos los egresos mostrados nacen de una compra y llevan su vínculo.

## F28 · Consultar movimientos y saldo de caja

- **Tipo:** Consulta.
- **Actor:** Administrador y Operador / Vendedor.
- **Descripción:** Lista los movimientos con código, fecha, concepto, tipo, monto, origen y responsable, y concilia el saldo del día: apertura + ingresos − egresos. En la versión V2, `/finanzas/list` y `/finanzas/detalle`.
- **Resultado:** Estado de caja visible: apertura S/ 4,410.00 + ingresos S/ 370.00 − egresos S/ 1,350.00 = saldo S/ 3,430.00.
- **Interfaces:** P15 ([finanzas.html](../finanzas.html)), P30 ([movimiento-detalle.html](../movimiento-detalle.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento, Venta, Compra.
- **Reglas:** RN04 — la conciliación sólo cierra si cada compra y cada venta tienen su único movimiento.

## F29 · Registrar empleado

- **Tipo:** CRUD — Registrar.
- **Actor:** Administrador.
- **Descripción:** Da de alta un empleado con DNI, nombres, apellidos, cargo, teléfono y estado; en la versión V2, `/empleados/crear` exige esos seis campos.
- **Resultado:** Empleado nuevo en la plantilla, disponible para cuentas de usuario y asistencia.
- **Interfaces:** P25 ([empleado-form.html](../empleado-form.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica — alta de un registro nuevo.

## F30 · Consultar empleados

- **Tipo:** CRUD — Listar.
- **Actor:** Administrador.
- **Descripción:** Lista el personal con DNI, nombres, apellidos, cargo, teléfono y estado; en la versión V2, `/empleados/list`.
- **Resultado:** Plantilla visible, con enlace al formulario y al control de asistencia.
- **Interfaces:** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica — consulta de solo lectura.

## F31 · Editar/activar/desactivar empleado

- **Tipo:** CRUD — Editar y cambiar estado.
- **Actor:** Administrador.
- **Descripción:** Modifica los datos del empleado y su estado en un mismo formulario; en la versión V2, `/empleados/editar` guarda los cambios en memoria.
- **Resultado:** Empleado actualizado; uno Inactivo queda fuera del registro de asistencia y conserva su historial.
- **Interfaces:** P25 ([empleado-form.html](../empleado-form.html)).
- **Entidades:** Empleado.
- **Reglas:** RN02 — la edición y la desactivación conservan el registro.

## F32 · Gestionar usuarios

- **Tipo:** CRUD — Registrar, editar y cambiar estado.
- **Actor:** Administrador.
- **Descripción:** Gestiona la cuenta de acceso de un empleado: el listado P19 (`/usuarios/list`) consulta las cuentas, y el formulario P26 (`/usuarios/crear` y `/usuarios/editar`) registra, modifica y cambia el estado de la cuenta con su empleado asociado, username, contraseña y rol.
- **Resultado:** Cuenta gestionada; una cuenta Inactiva no opera y conserva su historial y la responsabilidad de sus operaciones.
- **Interfaces:** P19 ([usuarios.html](../usuarios.html)), P26 ([usuario-form.html](../usuario-form.html)).
- **Entidades:** Usuario, Empleado.
- **Reglas:** RN02 — la desactivación sustituye a la eliminación.

## F33 · Consultar la portada pública

- **Tipo:** Consulta.
- **Actor:** Visitante.
- **Descripción:** Presenta la estación con sus precios de referencia, beneficios y navegación pública (maqueta V1: index.html y publicidad.html). La versión V2 no incluye portada.
- **Resultado:** Portada visible; no modifica ningún registro.
- **Interfaces:** P01 ([index.html](../index.html), [publicidad.html](../publicidad.html)).
- **Entidades:** No aplica — contenido público.
- **Reglas:** No aplica.

## F34 · Enviar mensaje de contacto

- **Tipo:** Proceso.
- **Actor:** Visitante.
- **Descripción:** Envía el formulario de contacto con nombre, correo, asunto y mensaje (maqueta V1: contacto.html). La versión V2 no incluye esta interfaz y el mensaje no se almacena.
- **Resultado:** Mensaje representado en la maqueta; sin persistencia ni respuesta.
- **Interfaces:** P04 ([contacto.html](../contacto.html)).
- **Entidades:** No aplica.
- **Reglas:** No aplica.

## F35 · Registrar asistencia

- **Tipo:** Proceso.
- **Actor:** Operador / Vendedor (empleado actual).
- **Descripción:** Registra la entrada y la salida de la jornada del empleado. En la versión V2, `/asistencia/entrada` y `/asistencia/salida` rechazan a los empleados Inactivos, una segunda entrada del mismo día y las salidas anteriores a la entrada, y calculan el estado (Presente o Falta) en servidor.
- **Resultado:** Jornada registrada con su fecha, horas y estado.
- **Interfaces:** P28 ([mi-asistencia.html](../mi-asistencia.html)).
- **Entidades:** Asistencia, Empleado, Usuario.
- **Reglas:** RN05 — empleado activo, jornada propia, un solo registro por jornada y salida posterior a la entrada.

## F36 · Consultar mi asistencia

- **Tipo:** Consulta.
- **Actor:** Operador / Vendedor (empleado actual).
- **Descripción:** Lista las jornadas propias con fecha, hora de entrada, hora de salida y estado. En la versión V2, `/asistencia/mi` no tiene selector de empleado: sólo muestra las marcaciones del empleado actual.
- **Resultado:** Historial propio visible.
- **Interfaces:** P28 ([mi-asistencia.html](../mi-asistencia.html)).
- **Entidades:** Asistencia, Empleado.
- **Reglas:** RN05 — cada jornada aparece una vez y con sus horas consistentes.

## F37 · Consultar mi resumen de asistencia

- **Tipo:** Consulta.
- **Actor:** Operador / Vendedor (empleado actual).
- **Descripción:** Muestra los totales del período —días presentes y faltas— en la misma pantalla del historial; en la versión V2, el bloque «Resumen del periodo» de `/asistencia/mi`.
- **Resultado:** Resumen propio visible, calculado sólo sobre las marcaciones del empleado actual.
- **Interfaces:** P28 ([mi-asistencia.html](../mi-asistencia.html)).
- **Entidades:** Asistencia, Empleado.
- **Reglas:** RN05 — el resumen se calcula sobre registros válidos de un solo empleado.

## F38 · Consultar asistencia del personal

- **Tipo:** Consulta.
- **Actor:** Administrador.
- **Descripción:** Lista la asistencia de todo el personal con filtro por empleado; en la versión V2, `/asistencia/control` presenta una fila por empleado y fecha con horas y estado.
- **Resultado:** Control diario del personal visible, con la fila de cada empleado.
- **Interfaces:** P29 ([control-asistencia.html](../control-asistencia.html)).
- **Entidades:** Asistencia, Empleado.
- **Reglas:** RN05 — un solo registro por empleado y fecha, con estado derivado de las horas.

## Contadores

| Indicador | Valor |
|---|---:|
| Funcionalidades totales | 38 |
| Tipo CRUD | 15 |
| Tipo Proceso | 6 |
| Tipo Consulta | 15 |
| Tipo Acceso | 2 |
| Funcionalidades con al menos una interfaz | 38 |
| Funcionalidades sin interfaz | 0 |
| Funcionalidades con al menos una regla | 24 |
| Funcionalidades sin regla | 14 |
| Operan en la versión V2 | 25 |
| Sólo en la maqueta V1 | 13 |

Funcionalidades sin regla: F01, F02, F03, F04, F05, F06, F09, F10, F23, F24, F29, F30, F33 y F34 — son acceso, consultas o altas sin restricción de negocio.
