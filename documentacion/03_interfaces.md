# Catálogo oficial de interfaces P01–P20

20 interfaces y 21 archivos HTML: publicidad.html es una segunda presentación de P01, no P21. Acciones de registro, edición y marcación son visuales. Los enlaces sí permiten recorrer archivos.

## P01 · Inicio / Publicidad

- **Objetivo:** Presentar la estación y acceder al login.
- **Actor:** Visitante.
- **Archivo:** [index.html](../index.html) y [publicidad.html](../publicidad.html).
- **Entidades:** No aplica; contenido público.
- **Funcionalidades:** No aplica.
- **Reglas:** No aplica.
- **Campos / contenido:** Combustibles, beneficios, acceso, contacto.
- **Acciones:** Navegar a acceso y contacto; formulario visual en P04.
- **Origen:** Navegación pública.
- **Destino:** login.html, contacto.html, publicidad.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P02 · Login

- **Objetivo:** Representar el acceso de demostración.
- **Actor:** Administrador, Operador / Vendedor o Empleado.
- **Archivo:** [login.html](../login.html).
- **Entidades:** Usuario.
- **Funcionalidades:** F01.
- **Reglas:** No aplica.
- **Campos / contenido:** Usuario, contraseña, recordarme.
- **Acciones:** Iniciar sesión.
- **Origen:** index.html o cierre de sesión.
- **Destino:** dashboard.html, index.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P03 · Dashboard

- **Objetivo:** Consultar un panorama de la operación.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [dashboard.html](../dashboard.html).
- **Entidades:** Venta Producto MovimientoCaja Asistencia.
- **Funcionalidades:** F02, F03.
- **Reglas:** No aplica.
- **Campos / contenido:** Cinco indicadores y cinco gráficos diarios.
- **Acciones:** Cerrar sesión; Consultar dashboard.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P04 · Contacto

- **Objetivo:** Mostrar los canales ficticios de contacto.
- **Actor:** Visitante.
- **Archivo:** [contacto.html](../contacto.html).
- **Entidades:** No aplica; contenido público.
- **Funcionalidades:** No aplica.
- **Reglas:** No aplica.
- **Campos / contenido:** Nombre, correo, asunto, mensaje.
- **Acciones:** Navegar a acceso y contacto; formulario visual en P04.
- **Origen:** Navegación pública.
- **Destino:** index.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P05 · Categorías

- **Objetivo:** Organizar las familias de combustibles.
- **Actor:** Administrador.
- **Archivo:** [categorias.html](../categorias.html).
- **Entidades:** Categoria.
- **Funcionalidades:** F04, F05, F06, F07.
- **Reglas:** RN03.
- **Campos / contenido:** Nombre, descripción, estado.
- **Acciones:** Registrar categoría; Consultar categorías; Editar categoría; Activar/desactivar categoría.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P06 · Combustibles

- **Objetivo:** Consultar el catálogo de combustibles.
- **Actor:** Administrador.
- **Archivo:** [combustibles.html](../combustibles.html).
- **Entidades:** Producto Categoria.
- **Funcionalidades:** F09, F11.
- **Reglas:** RN02, RN03.
- **Campos / contenido:** Nombre, categoría, unidad, precio, stock, estado.
- **Acciones:** Consultar combustibles; Activar/desactivar combustible.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P07 · Formulario combustible

- **Objetivo:** Representar el registro y la edición de combustible.
- **Actor:** Administrador.
- **Archivo:** [combustible-form.html](../combustible-form.html).
- **Entidades:** Producto Categoria.
- **Funcionalidades:** F08, F10.
- **Reglas:** RN02, RN03.
- **Campos / contenido:** Nombre, categoría, unidad, precio, stock, estado.
- **Acciones:** Registrar combustible; Editar combustible.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P08 · Registrar venta

- **Objetivo:** Representar una venta con disponibilidad y detalle.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [ventas.html](../ventas.html).
- **Entidades:** Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja.
- **Funcionalidades:** F12, F16.
- **Reglas:** RN01, RN02, RN04.
- **Campos / contenido:** Combustible, cantidad, precio unitario, subtotal, total, operador, estado.
- **Acciones:** Consultar existencias; Registrar venta.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P09 · Historial de ventas

- **Objetivo:** Consultar las ventas confirmadas de ejemplo.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [ventas-historial.html](../ventas-historial.html).
- **Entidades:** Venta DetalleVenta Producto Usuario.
- **Funcionalidades:** F17.
- **Reglas:** No aplica.
- **Campos / contenido:** Código, fecha, combustible, litros, total, operador, estado.
- **Acciones:** Consultar ventas.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P10 · Detalle de venta

- **Objetivo:** Consultar cada detalle y su ingreso asociado.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [venta-detalle.html](../venta-detalle.html).
- **Entidades:** Venta DetalleVenta Producto Usuario MovimientoCaja.
- **Funcionalidades:** F18.
- **Reglas:** RN04.
- **Campos / contenido:** Código, fecha, operador, combustible, cantidad, precio, subtotal, total, estado.
- **Acciones:** Consultar detalle de venta.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P11 · Existencias

- **Objetivo:** Consultar la disponibilidad física en litros.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario.html](../inventario.html).
- **Entidades:** Producto.
- **Funcionalidades:** F12.
- **Reglas:** RN01.
- **Campos / contenido:** Producto, stock, estado, alerta.
- **Acciones:** Consultar existencias.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P12 · Entrada combustible

- **Objetivo:** Representar un abastecimiento físico.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario-entrada.html](../inventario-entrada.html).
- **Entidades:** Producto Usuario MovimientoInventario.
- **Funcionalidades:** F13.
- **Reglas:** No aplica.
- **Campos / contenido:** Producto, cantidad, fecha, motivo, responsable.
- **Acciones:** Registrar entrada de combustible.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P13 · Salida combustible

- **Objetivo:** Representar un retiro físico justificado.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario-salida.html](../inventario-salida.html).
- **Entidades:** Producto Usuario MovimientoInventario.
- **Funcionalidades:** F12, F14.
- **Reglas:** RN01.
- **Campos / contenido:** Producto, cantidad, fecha, motivo, responsable.
- **Acciones:** Consultar existencias; Registrar salida de combustible.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P14 · Movimientos inventario

- **Objetivo:** Consultar la trazabilidad física del combustible.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario-movimientos.html](../inventario-movimientos.html).
- **Entidades:** MovimientoInventario Producto Usuario.
- **Funcionalidades:** F15.
- **Reglas:** No aplica.
- **Campos / contenido:** Código, tipo, producto, cantidad, fecha, responsable, motivo.
- **Acciones:** Consultar movimientos de inventario.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P15 · Resumen financiero

- **Objetivo:** Consultar los movimientos de dinero.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [finanzas.html](../finanzas.html).
- **Entidades:** MovimientoCaja ConceptoMovimiento Venta.
- **Funcionalidades:** F24.
- **Reglas:** RN04.
- **Campos / contenido:** Ingresos, egresos, saldo, movimientos recientes.
- **Acciones:** Consultar movimientos económicos.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P16 · Movimiento económico

- **Objetivo:** Representar un ingreso manual o un gasto.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [movimiento-economico.html](../movimiento-economico.html).
- **Entidades:** MovimientoCaja ConceptoMovimiento Usuario Venta.
- **Funcionalidades:** F22, F23.
- **Reglas:** RN04.
- **Campos / contenido:** Tipo, concepto, monto, fecha, descripción, origen, responsable.
- **Acciones:** Registrar ingreso económico; Registrar egreso económico.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P17 · Conceptos económicos

- **Objetivo:** Clasificar los movimientos económicos.
- **Actor:** Administrador.
- **Archivo:** [conceptos.html](../conceptos.html).
- **Entidades:** ConceptoMovimiento.
- **Funcionalidades:** F19, F20, F21.
- **Reglas:** RN03.
- **Campos / contenido:** Nombre, tipo, estado.
- **Acciones:** Registrar concepto económico; Consultar conceptos económicos; Editar/activar/desactivar concepto económico.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P18 · Empleados

- **Objetivo:** Representar la administración del personal.
- **Actor:** Administrador.
- **Archivo:** [empleados.html](../empleados.html).
- **Entidades:** Empleado.
- **Funcionalidades:** F25, F26, F27.
- **Reglas:** RN03.
- **Campos / contenido:** DNI, nombres, apellidos, cargo, teléfono, estado.
- **Acciones:** Registrar empleado; Consultar empleados; Editar/activar/desactivar empleado.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P19 · Usuarios

- **Objetivo:** Representar las cuentas y sus roles conceptuales.
- **Actor:** Administrador.
- **Archivo:** [usuarios.html](../usuarios.html).
- **Entidades:** Usuario Empleado.
- **Funcionalidades:** F28.
- **Reglas:** RN03.
- **Campos / contenido:** Empleado, username, rol, estado.
- **Acciones:** Gestionar usuarios.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P20 · Asistencia

- **Objetivo:** Representar la entrada, salida e historial del personal.
- **Actor:** Empleado; Administrador consulta historial.
- **Archivo:** [asistencia.html](../asistencia.html).
- **Entidades:** Asistencia Empleado.
- **Funcionalidades:** F29, F30.
- **Reglas:** RN05, RN06.
- **Campos / contenido:** Empleado, fecha, hora entrada, hora salida, estado.
- **Acciones:** Registrar entrada de asistencia; Registrar salida y consultar historial de asistencia.
- **Origen:** Dashboard y navegación común; enlaces de su módulo.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, ventas.html, inventario.html, finanzas.html, empleados.html, usuarios.html, asistencia.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## Navegación específica

Combustibles → formulario de registro y edición. Ventas → historial → detalle anclado V001/V002/V003. Inventario → entrada / salida / movimientos. Finanzas → movimiento económico / conceptos. Categorías, empleados y usuarios reúnen listado y formularios visuales. Asistencia reúne marcación y consulta. Cerrar sesión navega a login sin gestionar una sesión real.
