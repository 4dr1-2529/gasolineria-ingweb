# Funcionalidades oficiales F01–F30

Se cuentan capacidades del negocio, no campos, enlaces o botones. Registro de detalle y efecto de caja pertenecen a F16. F28 agrupa la gestión de usuarios; F30 agrupa salida e historial por el alcance oficial.

## F01 · Iniciar sesión

- **Descripción:** Iniciar sesión en el módulo Acceso.
- **Actor:** Administrador, Operador / Vendedor o Empleado.
- **Entrada:** Usuario y contraseña de demostración.
- **Proceso:** Representar acceso y navegar a dashboard sin comprobar credenciales.
- **Resultado:** Dashboard visible; sin persistencia ni validación de negocio.
- **Interfaz:** P02 ([login.html](../login.html)).
- **Entidades:** Usuario.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F02 · Cerrar sesión

- **Descripción:** Cerrar sesión en el módulo Acceso.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Enlace Cerrar sesión.
- **Proceso:** Navegar a login; no existe sesión que invalidar.
- **Resultado:** Login visible; sin persistencia ni validación de negocio.
- **Interfaz:** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** Asistencia, MovimientoCaja, Producto, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F03 · Consultar dashboard

- **Descripción:** Consultar dashboard en el módulo Dashboard.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Fecha de corte y datos de ejemplo.
- **Proceso:** Presentar cinco KPI y cinco series estáticas.
- **Resultado:** Resumen de ventas, litros, caja y personal; sin persistencia ni validación de negocio.
- **Interfaz:** P03 ([dashboard.html](../dashboard.html)).
- **Entidades:** Asistencia, MovimientoCaja, Producto, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F04 · Registrar categoría

- **Descripción:** Registrar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Nombre, descripción, estado.
- **Proceso:** Representar captura de nueva categoría.
- **Resultado:** Propuesta de categoría; sin persistencia ni validación de negocio.
- **Interfaz:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F05 · Consultar categorías

- **Descripción:** Consultar categorías en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categorías del ejemplo.
- **Proceso:** Mostrar listado, descripción y estado.
- **Resultado:** Catálogo visible; sin persistencia ni validación de negocio.
- **Interfaz:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F06 · Editar categoría

- **Descripción:** Editar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categoría y campos modificados.
- **Proceso:** Representar edición de categoría seleccionada.
- **Resultado:** Propuesta de actualización; sin persistencia ni validación de negocio.
- **Interfaz:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F07 · Activar/desactivar categoría

- **Descripción:** Activar/desactivar categoría en el módulo Categorías.
- **Actor:** Administrador.
- **Entrada:** Categoría y nuevo estado.
- **Proceso:** Representar activación o desactivación conservando historial.
- **Resultado:** Propuesta de cambio de estado; sin persistencia ni validación de negocio.
- **Interfaz:** P05 ([categorias.html](../categorias.html)).
- **Entidades:** Categoria.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F08 · Registrar combustible

- **Descripción:** Registrar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Nombre, categoría, unidad, precio, stock, estado.
- **Proceso:** Representar captura de combustible.
- **Resultado:** Propuesta de producto; sin persistencia ni validación de negocio.
- **Interfaz:** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F09 · Consultar combustibles

- **Descripción:** Consultar combustibles en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Productos del ejemplo.
- **Proceso:** Mostrar categoría, precio, stock y estado.
- **Resultado:** Catálogo de combustibles visible; sin persistencia ni validación de negocio.
- **Interfaz:** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F10 · Editar combustible

- **Descripción:** Editar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Combustible y datos modificados.
- **Proceso:** Representar edición; stock referencial se gestiona por movimientos en etapa futura.
- **Resultado:** Propuesta de actualización de combustible; sin persistencia ni validación de negocio.
- **Interfaz:** P07 ([combustible-form.html](../combustible-form.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F11 · Activar/desactivar combustible

- **Descripción:** Activar/desactivar combustible en el módulo Combustibles.
- **Actor:** Administrador.
- **Entrada:** Combustible y nuevo estado.
- **Proceso:** Representar activación o desactivación.
- **Resultado:** Propuesta de estado de producto; sin persistencia ni validación de negocio.
- **Interfaz:** P06 ([combustibles.html](../combustibles.html)).
- **Entidades:** Categoria, Producto.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F12 · Consultar existencias

- **Descripción:** Consultar existencias en el módulo Inventario.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Producto.
- **Proceso:** Mostrar existencia actual en litros.
- **Resultado:** Disponibilidad física visible; sin persistencia ni validación de negocio.
- **Interfaz:** P08 ([ventas.html](../ventas.html)), P11 ([inventario.html](../inventario.html)), P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** DetalleVenta, MovimientoCaja, MovimientoInventario, Producto, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F13 · Registrar entrada de combustible

- **Descripción:** Registrar entrada de combustible en el módulo Inventario.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Producto, litros, fecha, motivo, responsable.
- **Proceso:** Representar una entrada y la proyección del stock.
- **Resultado:** Propuesta de abastecimiento; sin persistencia ni validación de negocio.
- **Interfaz:** P12 ([inventario-entrada.html](../inventario-entrada.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F14 · Registrar salida de combustible

- **Descripción:** Registrar salida de combustible en el módulo Inventario.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Producto, litros, fecha, motivo, responsable.
- **Proceso:** Representar salida con disponibilidad; en futuro comprobar RN01 y descontar.
- **Resultado:** Propuesta de retiro; sin persistencia ni validación de negocio.
- **Interfaz:** P13 ([inventario-salida.html](../inventario-salida.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F15 · Consultar movimientos de inventario

- **Descripción:** Consultar movimientos de inventario en el módulo Inventario.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Movimientos del ejemplo.
- **Proceso:** Mostrar entradas y salidas físicas.
- **Resultado:** Historial de litros visible; sin persistencia ni validación de negocio.
- **Interfaz:** P14 ([inventario-movimientos.html](../inventario-movimientos.html)).
- **Entidades:** MovimientoInventario, Producto, Usuario.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F16 · Registrar venta

- **Descripción:** Registrar venta en el módulo Ventas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Combustible, cantidad, precio, operador, estado.
- **Proceso:** Representar detalle y total; en futuro confirmar venta, stock e ingreso de forma atómica.
- **Resultado:** Propuesta de venta sin modificar los totales de referencia; sin persistencia ni validación de negocio.
- **Interfaz:** P08 ([ventas.html](../ventas.html)).
- **Entidades:** DetalleVenta, MovimientoCaja, MovimientoInventario, Producto, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F17 · Consultar ventas

- **Descripción:** Consultar ventas en el módulo Ventas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Ventas confirmadas del ejemplo.
- **Proceso:** Mostrar código, fecha, litros, total, operador y estado.
- **Resultado:** Historial de ventas visible; sin persistencia ni validación de negocio.
- **Interfaz:** P09 ([ventas-historial.html](../ventas-historial.html)).
- **Entidades:** DetalleVenta, Producto, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F18 · Consultar detalle de venta

- **Descripción:** Consultar detalle de venta en el módulo Ventas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Enlace a código de venta.
- **Proceso:** Navegar al detalle anclado de la venta seleccionada.
- **Resultado:** Detalle, total e ingreso asociado visibles; sin persistencia ni validación de negocio.
- **Interfaz:** P10 ([venta-detalle.html](../venta-detalle.html)).
- **Entidades:** DetalleVenta, MovimientoCaja, Producto, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F19 · Registrar concepto económico

- **Descripción:** Registrar concepto económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Nombre, tipo, estado.
- **Proceso:** Representar captura de concepto económico.
- **Resultado:** Propuesta de concepto; sin persistencia ni validación de negocio.
- **Interfaz:** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F20 · Consultar conceptos económicos

- **Descripción:** Consultar conceptos económicos en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Conceptos del ejemplo.
- **Proceso:** Mostrar catálogo de ingresos y egresos.
- **Resultado:** Conceptos visibles; sin persistencia ni validación de negocio.
- **Interfaz:** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F21 · Editar/activar/desactivar concepto económico

- **Descripción:** Editar/activar/desactivar concepto económico en el módulo Finanzas.
- **Actor:** Administrador.
- **Entrada:** Concepto, nombre, tipo, estado.
- **Proceso:** Representar edición y cambio de estado sin eliminación.
- **Resultado:** Propuesta de actualización de concepto; sin persistencia ni validación de negocio.
- **Interfaz:** P17 ([conceptos.html](../conceptos.html)).
- **Entidades:** ConceptoMovimiento.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F22 · Registrar ingreso económico

- **Descripción:** Registrar ingreso económico en el módulo Finanzas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Tipo ingreso, concepto, monto, fecha, descripción, origen, responsable.
- **Proceso:** Representar ingreso manual; ingresos de ventas se generarán por F16.
- **Resultado:** Propuesta de ingreso; sin persistencia ni validación de negocio.
- **Interfaz:** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** ConceptoMovimiento, MovimientoCaja, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F23 · Registrar egreso económico

- **Descripción:** Registrar egreso económico en el módulo Finanzas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Tipo egreso, concepto, monto, fecha, descripción, origen, responsable.
- **Proceso:** Representar gasto con concepto de egreso.
- **Resultado:** Propuesta de egreso; sin persistencia ni validación de negocio.
- **Interfaz:** P16 ([movimiento-economico.html](../movimiento-economico.html)).
- **Entidades:** ConceptoMovimiento, MovimientoCaja, Usuario, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F24 · Consultar movimientos económicos

- **Descripción:** Consultar movimientos económicos en el módulo Finanzas.
- **Actor:** Administrador y Operador / Vendedor.
- **Entrada:** Movimientos económicos del ejemplo.
- **Proceso:** Mostrar ingresos, egresos y saldo en soles.
- **Resultado:** Historial económico visible; sin persistencia ni validación de negocio.
- **Interfaz:** P15 ([finanzas.html](../finanzas.html)).
- **Entidades:** ConceptoMovimiento, MovimientoCaja, Venta.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F25 · Registrar empleado

- **Descripción:** Registrar empleado en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** DNI, nombres, apellidos, cargo, teléfono, estado.
- **Proceso:** Representar alta de empleado.
- **Resultado:** Propuesta de empleado; sin persistencia ni validación de negocio.
- **Interfaz:** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F26 · Consultar empleados

- **Descripción:** Consultar empleados en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** Empleados del ejemplo.
- **Proceso:** Mostrar datos del personal y estado.
- **Resultado:** Listado de empleados visible; sin persistencia ni validación de negocio.
- **Interfaz:** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F27 · Editar/activar/desactivar empleado

- **Descripción:** Editar/activar/desactivar empleado en el módulo Empleados.
- **Actor:** Administrador.
- **Entrada:** Empleado y campos modificados o estado.
- **Proceso:** Representar edición o cambio de estado preservando historial.
- **Resultado:** Propuesta de actualización de empleado; sin persistencia ni validación de negocio.
- **Interfaz:** P18 ([empleados.html](../empleados.html)).
- **Entidades:** Empleado.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F28 · Gestionar usuarios

- **Descripción:** Gestionar usuarios en el módulo Usuarios.
- **Actor:** Administrador.
- **Entrada:** Empleado, username, contraseña visual, rol, estado.
- **Proceso:** Representar registro, consulta, edición y estado de cuentas con máximo una por empleado.
- **Resultado:** Gestión visual de usuarios; sin persistencia ni validación de negocio.
- **Interfaz:** P19 ([usuarios.html](../usuarios.html)).
- **Entidades:** Empleado, Usuario.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F29 · Registrar entrada de asistencia

- **Descripción:** Registrar entrada de asistencia en el módulo Asistencia.
- **Actor:** Empleado; Administrador consulta historial.
- **Entrada:** Empleado, fecha, hora de entrada.
- **Proceso:** Representar apertura; Spring Boot comprobará ausencia de asistencia abierta.
- **Resultado:** Propuesta de entrada; sin persistencia ni validación de negocio.
- **Interfaz:** P20 ([asistencia.html](../asistencia.html)).
- **Entidades:** Asistencia, Empleado.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## F30 · Registrar salida y consultar historial de asistencia

- **Descripción:** Registrar salida y consultar historial de asistencia en el módulo Asistencia.
- **Actor:** Empleado; Administrador consulta historial.
- **Entrada:** Empleado, fecha, hora de salida e historial.
- **Proceso:** Representar cierre posterior a la entrada y consulta del historial.
- **Resultado:** Propuesta de salida e historial visible; sin persistencia ni validación de negocio.
- **Interfaz:** P20 ([asistencia.html](../asistencia.html)).
- **Entidades:** Asistencia, Empleado.
- **Reglas:** No aplica.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.
