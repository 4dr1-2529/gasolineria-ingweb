# Funcionalidades oficiales F01–F32

Se cuentan capacidades del negocio, no campos, enlaces ni botones. Hay **32 funcionalidades** repartidas en diez módulos; cada una se asocia a al menos una interfaz y, cuando aplica, a una o más reglas de negocio. Registro de detalle y efecto de caja de la venta pertenecen a F20; los efectos de la compra pertenecen a F13 y F16. F32 agrupa toda la gestión de usuarios.

| Módulo | Funcionalidades |
|---|---|
| Acceso | F01 Iniciar sesión, F02 Cerrar sesión |
| Dashboard | F03 Consultar dashboard |
| Categorías | F04 Registrar categoría, F05 Consultar categorías, F06 Consultar combustibles por categoría, F07 Editar categoría, F08 Activar/desactivar categoría |
| Combustibles | F09 Registrar combustible, F10 Consultar combustibles, F11 Editar combustible, F12 Activar/desactivar combustible |
| Compras | F13 Registrar compra de combustible, F14 Consultar compras, F15 Consultar detalle de compra |
| Inventario | F16 Registrar entrada de combustible, F17 Registrar salida de combustible, F18 Consultar existencias, F19 Consultar movimientos de inventario |
| Ventas | F20 Registrar venta, F21 Consultar ventas, F22 Consultar detalle de venta |
| Finanzas | F23 Registrar concepto económico, F24 Consultar conceptos económicos, F25 Editar/activar/desactivar concepto económico, F26 Registrar ingreso económico, F27 Registrar egreso económico, F28 Consultar movimientos y saldo de caja |
| Empleados | F29 Registrar empleado, F30 Consultar empleados, F31 Editar/activar/desactivar empleado |
| Usuarios | F32 Gestionar usuarios |

## Índice

| Código | Nombre | Módulo | Interfaz(es) | Reglas |
|---|---|---|---|---|
| [F01](#f01--iniciar-sesion) | Iniciar sesión | Acceso | P02 | No aplica |
| [F02](#f02--cerrar-sesion) | Cerrar sesión | Acceso | P03 | No aplica |
| [F03](#f03--consultar-dashboard) | Consultar dashboard | Dashboard | P03 | No aplica |
| [F04](#f04--registrar-categoria) | Registrar categoría | Categorías | P05 | No aplica |
| [F05](#f05--consultar-categorias) | Consultar categorías | Categorías | P05 | No aplica |
| [F06](#f06--consultar-combustibles-por-categoria) | Consultar combustibles por categoría | Categorías | P05 | No aplica |
| [F07](#f07--editar-categoria) | Editar categoría | Categorías | P05 | RN03 |
| [F08](#f08--activardesactivar-categoria) | Activar/desactivar categoría | Categorías | P05 | RN03 |
| [F09](#f09--registrar-combustible) | Registrar combustible | Combustibles | P07 | RN02, RN06 |
| [F10](#f10--consultar-combustibles) | Consultar combustibles | Combustibles | P06 | RN02 |
| [F11](#f11--editar-combustible) | Editar combustible | Combustibles | P07 | RN02, RN03, RN06 |
| [F12](#f12--activardesactivar-combustible) | Activar/desactivar combustible | Combustibles | P06 | RN02, RN03 |
| [F13](#f13--registrar-compra-de-combustible) | Registrar compra de combustible | Compras | P20 | RN04, RN06 |
| [F14](#f14--consultar-compras) | Consultar compras | Compras | P20 | RN04 |
| [F15](#f15--consultar-detalle-de-compra) | Consultar detalle de compra | Compras | P21 | RN04 |
| [F16](#f16--registrar-entrada-de-combustible) | Registrar entrada de combustible | Inventario | P12 | RN04, RN06 |
| [F17](#f17--registrar-salida-de-combustible) | Registrar salida de combustible | Inventario | P13 | RN01, RN06 |
| [F18](#f18--consultar-existencias) | Consultar existencias | Inventario | P08, P11, P13 | RN01 |
| [F19](#f19--consultar-movimientos-de-inventario) | Consultar movimientos de inventario | Inventario | P14 | RN01, RN04 |
| [F20](#f20--registrar-venta) | Registrar venta | Ventas | P08 | RN01, RN02, RN05, RN06 |
| [F21](#f21--consultar-ventas) | Consultar ventas | Ventas | P09 | RN05 |
| [F22](#f22--consultar-detalle-de-venta) | Consultar detalle de venta | Ventas | P10 | RN05 |
| [F23](#f23--registrar-concepto-economico) | Registrar concepto económico | Finanzas | P17 | No aplica |
| [F24](#f24--consultar-conceptos-economicos) | Consultar conceptos económicos | Finanzas | P17 | No aplica |
| [F25](#f25--editaractivardesactivar-concepto-economico) | Editar/activar/desactivar concepto económico | Finanzas | P17 | RN03 |
| [F26](#f26--registrar-ingreso-economico) | Registrar ingreso económico | Finanzas | P16 | RN06 |
| [F27](#f27--registrar-egreso-economico) | Registrar egreso económico | Finanzas | P16 | RN06 |
| [F28](#f28--consultar-movimientos-y-saldo-de-caja) | Consultar movimientos y saldo de caja | Finanzas | P15 | RN04, RN05 |
| [F29](#f29--registrar-empleado) | Registrar empleado | Empleados | P18 | No aplica |
| [F30](#f30--consultar-empleados) | Consultar empleados | Empleados | P18 | No aplica |
| [F31](#f31--editaractivardesactivar-empleado) | Editar/activar/desactivar empleado | Empleados | P18 | RN03 |
| [F32](#f32--gestionar-usuarios) | Gestionar usuarios | Usuarios | P19 | RN03 |

## F01 · Iniciar sesión

- **Descripción:** Iniciar sesión en el módulo Acceso.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Usuario y contraseña de demostración.
- **Proceso:** Representar el acceso y navegar al dashboard sin comprobar credenciales.
- **Resultado:** Dashboard visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P02 ([login.html](../login.html)).
- **Entidades:** Usuario.
- **Reglas:** No aplica.
- **Justificación:** Es el punto de entrada al panel interno; la validación de credenciales queda para Spring Security en una etapa posterior.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F02 · Cerrar sesión

- **Descripción:** Cerrar sesión en el módulo Acceso.
- **Actor:** Administrador.
- **Entrada:** Enlace “Cerrar sesión”.
- **Proceso:** Navegar al login; no existe sesión que invalidar.
- **Resultado:** Login visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** Venta, DetalleVenta, Producto, MovimientoCaja.
- **Reglas:** No aplica.
- **Justificación:** La maqueta es estática: cerrar sesión es únicamente navegación.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F03 · Consultar dashboard

- **Descripción:** Consultar dashboard en el módulo Dashboard.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Fecha de corte y datos de ejemplo.
- **Proceso:** Presentar cinco indicadores y cinco series temporales estáticas con eje X de fechas (04–10 sep. 2026).
- **Resultado:** Resumen de ventas, litros, ingresos, egresos y saldo de caja; sin persistencia ni validación de negocio.
- **Interfaz(es):** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** Venta, DetalleVenta, Producto, MovimientoCaja.
- **Reglas:** No aplica.
- **Justificación:** Las cinco métricas comparten el mismo eje temporal para poder compararse entre sí.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F04 · Registrar categoría

- **Descripción:** Registrar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Nombre, descripción y estado.
- **Proceso:** Representar la captura de una nueva categoría.
- **Resultado:** Propuesta de categoría; sin persistencia ni validación de negocio.
- **Interfaz(es):** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Justificación:** Alta de un registro nuevo; no toca registros existentes y por eso no aplica ninguna regla.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F05 · Consultar categorías

- **Descripción:** Consultar categorías en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categorías del ejemplo.
- **Proceso:** Mostrar listado, descripción y estado.
- **Resultado:** Catálogo visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Justificación:** Consulta de solo lectura, sin restricción de negocio.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F06 · Consultar combustibles por categoría

- **Descripción:** Consultar combustibles por categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categorías del ejemplo.
- **Proceso:** Mostrar en cada categoría los combustibles que agrupa, con enlace a su ficha.
- **Resultado:** Relación categoría–combustible visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica.
- **Justificación:** Hace explícito el primer eslabón de la cadena de negocio: categoría → combustible.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F07 · Editar categoría

- **Descripción:** Editar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categoría seleccionada y campos modificados.
- **Proceso:** Representar la edición de la categoría seleccionada.
- **Resultado:** Propuesta de edición; sin persistencia ni validación de negocio.
- **Interfaz(es):** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** RN03.
- **Justificación:** La edición modifica el registro, nunca lo elimina.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F08 · Activar/desactivar categoría

- **Descripción:** Activar/desactivar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categoría seleccionada y estado nuevo.
- **Proceso:** Representar el cambio de estado de una categoría.
- **Resultado:** Categoría con nuevo estado en la maqueta; sin persistencia ni validación de negocio.
- **Interfaz(es):** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** RN03.
- **Justificación:** El cambio de estado sustituye a la eliminación porque la categoría puede tener historial.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F09 · Registrar combustible

- **Descripción:** Registrar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Categoría, nombre, unidad, precio, stock inicial y estado.
- **Proceso:** Representar el alta de un nuevo combustible.
- **Resultado:** Propuesta de producto; sin persistencia ni validación de negocio.
- **Interfaz(es):** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02, RN06.
- **Justificación:** El estado que se fija aquí determina si el producto podrá venderse (RN02) y los numéricos deben ser positivos (RN06).
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F10 · Consultar combustibles

- **Descripción:** Consultar combustibles en el módulo Combustibles.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Combustibles del ejemplo.
- **Proceso:** Mostrar catálogo con categoría, precio por litro, existencias y estado.
- **Resultado:** Catálogo visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02.
- **Justificación:** El listado es la referencia desde la que se decide qué producto es apto para una nueva venta.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F11 · Editar combustible

- **Descripción:** Editar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Combustible seleccionado y campos modificados.
- **Proceso:** Representar la edición del combustible seleccionado.
- **Resultado:** Propuesta de edición; sin persistencia ni validación de negocio.
- **Interfaz(es):** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02, RN03, RN06.
- **Justificación:** La edición conserva el registro y debe respetar precios y existencias positivos.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F12 · Activar/desactivar combustible

- **Descripción:** Activar/desactivar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Combustible seleccionado y estado nuevo.
- **Proceso:** Representar el cambio de estado de un combustible.
- **Resultado:** Producto con nuevo estado en la maqueta; sin persistencia ni validación de negocio.
- **Interfaz(es):** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Producto, Categoria.
- **Reglas:** RN02, RN03.
- **Justificación:** Un producto inactivo deja de estar disponible para nuevas ventas sin perder su historial.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F13 · Registrar compra de combustible

- **Descripción:** Registrar compra de combustible en el módulo Compras.
- **Actor:** Administrador.
- **Entrada:** Proveedor, fecha, combustible, litros y precio de compra.
- **Proceso:** Representar el alta de una compra; la maqueta la muestra ya confirmada como C001.
- **Resultado:** Propuesta de compra; sin persistencia ni validación de negocio. Al confirmarse generará entradas y un egreso.
- **Interfaz(es):** P20 ([compras.html](../compras.html)).
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN04, RN06.
- **Justificación:** Es el único punto del negocio donde un movimiento físico y uno de dinero nacen a la vez.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F14 · Consultar compras

- **Descripción:** Consultar compras en el módulo Compras.
- **Actor:** Administrador.
- **Entrada:** Compras del ejemplo.
- **Proceso:** Mostrar listado con fecha, proveedor, litros, total, estado y acceso al detalle.
- **Resultado:** Listado de compras visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P20 ([compras.html](../compras.html)).
- **Entidades:** Compra, DetalleCompra, Producto.
- **Reglas:** RN04.
- **Justificación:** La consulta permite comprobar que cada compra tiene sus efectos de inventario y de caja.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F15 · Consultar detalle de compra

- **Descripción:** Consultar detalle de compra en el módulo Compras.
- **Actor:** Administrador.
- **Entrada:** Compra seleccionada (C001).
- **Proceso:** Mostrar las líneas de la compra, su total, el egreso asociado y las entradas generadas.
- **Resultado:** Detalle y trazabilidad de la compra visibles; sin persistencia ni validación de negocio.
- **Interfaz(es):** P21 ([compra-detalle.html](../compra-detalle.html)).
- **Entidades:** Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN04.
- **Justificación:** Es la interfaz que hace visible de forma explícita la relación compra → entradas → egreso.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F16 · Registrar entrada de combustible

- **Descripción:** Registrar entrada de combustible en el módulo Inventario.
- **Actor:** Administrador.
- **Entrada:** Compra o motivo, producto, litros, fecha y responsable.
- **Proceso:** Representar el alta de una entrada de inventario.
- **Resultado:** Propuesta de entrada; sin persistencia ni validación de negocio.
- **Interfaz(es):** P12 ([inventario-entrada.html](../inventario-entrada.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN04, RN06.
- **Justificación:** Una entrada originada en una compra debe arrastrar su egreso (RN04) y expresar litros positivos (RN06).
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F17 · Registrar salida de combustible

- **Descripción:** Registrar salida de combustible en el módulo Inventario.
- **Actor:** Operador / Vendedor.
- **Entrada:** Producto, cantidad, motivo y responsable.
- **Proceso:** Representar el alta de una salida de inventario con comprobación de stock en la maqueta.
- **Resultado:** Propuesta de salida; sin persistencia ni validación de negocio.
- **Interfaz(es):** P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** RN01, RN06.
- **Justificación:** La salida no puede exceder las existencias y la cantidad debe ser positiva.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F18 · Consultar existencias

- **Descripción:** Consultar existencias en el módulo Inventario.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Existencias de ejemplo al corte del 10/09/2026.
- **Proceso:** Mostrar el saldo físico en litros por producto y el total antes de operar.
- **Resultado:** Existencias visibles; sin persistencia ni validación de negocio.
- **Interfaz(es):** P08 ([ventas.html](../ventas.html)), P11 ([inventario.html](../inventario.html)), P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** Producto.
- **Reglas:** RN01.
- **Justificación:** Es la consulta que sostiene RN01: sin existencias visibles no se puede decidir si se puede vender o retirar.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F19 · Consultar movimientos de inventario

- **Descripción:** Consultar movimientos de inventario en el módulo Inventario.
- **Actor:** Administrador.
- **Entrada:** Movimientos de ejemplo.
- **Proceso:** Mostrar el libro de entradas y salidas con su saldo inicial, sus entradas y sus salidas.
- **Resultado:** Trazabilidad física visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P14 ([inventario-movimientos.html](../inventario-movimientos.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario, Compra.
- **Reglas:** RN01, RN04.
- **Justificación:** Permite conciliar el saldo físico y ver los efectos de la compra C001.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F20 · Registrar venta

- **Descripción:** Registrar venta en el módulo Ventas.
- **Actor:** Operador / Vendedor.
- **Entrada:** Producto, cantidad, descuento y datos del formulario.
- **Proceso:** Representar el alta de una venta con existencias suficientes y producto activo.
- **Resultado:** Propuesta de venta; sin persistencia ni validación de negocio. Al confirmarse generará salida de inventario y un ingreso.
- **Interfaz(es):** P08 ([ventas.html](../ventas.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN01, RN02, RN05, RN06.
- **Justificación:** Concentra las cuatro reglas de la operación de venta en una sola interfaz.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F21 · Consultar ventas

- **Descripción:** Consultar ventas en el módulo Ventas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Ventas confirmadas de ejemplo.
- **Proceso:** Mostrar el listado con fecha, combustible, litros, total, operador, estado e ingreso asociado.
- **Resultado:** Historial visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P09 ([ventas-historial.html](../ventas-historial.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoCaja.
- **Reglas:** RN05.
- **Justificación:** La columna de ingreso hace comprobable que cada venta tiene un único movimiento económico.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F22 · Consultar detalle de venta

- **Descripción:** Consultar detalle de venta en el módulo Ventas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Venta seleccionada (V001, V002 o V003).
- **Proceso:** Mostrar cada detalle con su salida de inventario y su ingreso económico.
- **Resultado:** Detalle y trazabilidad de la venta visibles; sin persistencia ni validación de negocio.
- **Interfaz(es):** P10 ([venta-detalle.html](../venta-detalle.html)).
- **Entidades:** Venta, DetalleVenta, Producto, Usuario, MovimientoInventario, MovimientoCaja.
- **Reglas:** RN05.
- **Justificación:** Vincula la venta con MI004 y con MC003, el ingreso único de la venta.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F23 · Registrar concepto económico

- **Descripción:** Registrar concepto económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Nombre, tipo (ingreso o egreso) y estado.
- **Proceso:** Representar el alta de un concepto de movimiento.
- **Resultado:** Propuesta de concepto; sin persistencia ni validación de negocio.
- **Interfaz(es):** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Justificación:** Alta de un registro nuevo; no toca registros existentes y por eso no aplica ninguna regla.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F24 · Consultar conceptos económicos

- **Descripción:** Consultar conceptos económicos en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Conceptos del ejemplo.
- **Proceso:** Mostrar el catálogo con tipo y estado.
- **Resultado:** Catálogo visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Justificación:** Consulta de solo lectura, sin restricción de negocio.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F25 · Editar/activar/desactivar concepto económico

- **Descripción:** Editar/activar/desactivar concepto económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Concepto seleccionado y campos modificados.
- **Proceso:** Representar la edición y el cambio de estado del concepto.
- **Resultado:** Propuesta de edición; sin persistencia ni validación de negocio.
- **Interfaz(es):** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** RN03.
- **Justificación:** Un concepto referenciado por movimientos de caja no se elimina, se desactiva.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F26 · Registrar ingreso económico

- **Descripción:** Registrar ingreso económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Concepto de ingreso, monto, descripción, fecha y responsable.
- **Proceso:** Representar el alta manual de un ingreso que no nace de una venta.
- **Resultado:** Propuesta de ingreso; sin persistencia ni validación de negocio.
- **Interfaz(es):** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento.
- **Reglas:** RN06.
- **Justificación:** El monto debe ser un importe positivo con dos decimales como máximo.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F27 · Registrar egreso económico

- **Descripción:** Registrar egreso económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Concepto de egreso, monto, descripción, fecha y responsable.
- **Proceso:** Representar el alta manual de un egreso que no nace de una compra.
- **Resultado:** Propuesta de egreso; sin persistencia ni validación de negocio.
- **Interfaz(es):** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento.
- **Reglas:** RN06.
- **Justificación:** El monto debe ser un importe positivo con dos decimales como máximo.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F28 · Consultar movimientos y saldo de caja

- **Descripción:** Consultar movimientos y saldo de caja en el módulo Finanzas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Movimientos de ejemplo y apertura del día.
- **Proceso:** Mostrar el listado de ingresos y egresos con su conciliación: apertura + ingresos − egresos = saldo.
- **Resultado:** Estado de caja visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P15 ([finanzas.html](../finanzas.html)).
- **Entidades:** MovimientoCaja, ConceptoMovimiento, Venta, Compra.
- **Reglas:** RN04, RN05.
- **Justificación:** Aquí se comprueban a la vez el egreso único de la compra (RN04) y el ingreso único de cada venta (RN05).
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F29 · Registrar empleado

- **Descripción:** Registrar empleado en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** DNI, nombres, apellidos, cargo, teléfono y estado.
- **Proceso:** Representar el alta de un empleado.
- **Resultado:** Propuesta de empleado; sin persistencia ni validación de negocio.
- **Interfaz(es):** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Justificación:** Alta de un registro nuevo; no toca registros existentes y por eso no aplica ninguna regla.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F30 · Consultar empleados

- **Descripción:** Consultar empleados en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** Empleados del ejemplo.
- **Proceso:** Mostrar listado con identidad, cargo, teléfono y estado.
- **Resultado:** Listado visible; sin persistencia ni validación de negocio.
- **Interfaz(es):** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Justificación:** Consulta de solo lectura, sin restricción de negocio.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F31 · Editar/activar/desactivar empleado

- **Descripción:** Editar/activar/desactivar empleado en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** Empleado seleccionado y campos modificados.
- **Proceso:** Representar la edición y el cambio de estado del empleado.
- **Resultado:** Propuesta de edición; sin persistencia ni validación de negocio.
- **Interfaz(es):** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** RN03.
- **Justificación:** Un empleado con usuario o con historial de ventas se desactiva, no se borra.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F32 · Gestionar usuarios

- **Descripción:** Gestionar usuarios en el módulo Usuarios.
- **Actor:** Administrador.
- **Entrada:** Usuario, empleado asociado, rol, contraseña y estado.
- **Proceso:** Representar la creación, edición y desactivación de cuentas de acceso.
- **Resultado:** Propuesta de usuario; sin persistencia ni validación de negocio.
- **Interfaz(es):** P19 ([usuarios.html](../usuarios.html)).
- **Entidades:** Usuario, Empleado.
- **Reglas:** RN03.
- **Justificación:** Cada cuenta pertenece a un empleado y conserva su historial mediante el estado.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## Contadores

| Indicador | Valor |
|---|---:|
| Funcionalidades totales | 32 |
| Funcionalidades con al menos una interfaz | 32 |
| Funcionalidades sin interfaz | 0 |
| Funcionalidades con al menos una regla | 22 |
| Funcionalidades sin regla (altas nuevas, consultas y gestión sin restricción) | 10 |

Funcionalidades sin regla: F01, F02, F03, F04, F05, F06, F23, F24, F29 y F30.

