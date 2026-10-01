# Catálogo oficial de interfaces P01–P21

21 interfaces y 22 archivos HTML: publicidad.html es una segunda presentación de P01, no una interfaz adicional. Las acciones de registro, edición y activación son visuales; los enlaces sí permiten recorrer todos los archivos. Cada interfaz, excepto las públicas P01 y P04, tiene al menos una funcionalidad; ninguna funcionalidad carece de interfaz.

## Índice

| Código | Interfaz | Archivo(s) | Funcionalidades | Reglas |
|---|---|---|---|---|
| [P01](#p01--inicio--publicidad) | Inicio / Publicidad | index.html, publicidad.html | No aplica | No aplica |
| [P02](#p02--login) | Login | login.html | F01 | No aplica |
| [P03](#p03--dashboard) | Dashboard | dashboard.html | F02, F03 | No aplica |
| [P04](#p04--contacto) | Contacto | contacto.html | No aplica | No aplica |
| [P05](#p05--categorias) | Categorías | categorias.html | F04, F05, F06, F07, F08 | RN03 |
| [P06](#p06--combustibles) | Combustibles | combustibles.html | F10, F12 | RN02, RN03 |
| [P07](#p07--formulario-de-combustible) | Formulario de combustible | combustible-form.html | F09, F11 | RN02, RN03, RN06 |
| [P08](#p08--registrar-venta) | Registrar venta | ventas.html | F18, F20 | RN01, RN02, RN05, RN06 |
| [P09](#p09--historial-de-ventas) | Historial de ventas | ventas-historial.html | F21 | RN05 |
| [P10](#p10--detalle-de-venta) | Detalle de venta | venta-detalle.html | F22 | RN05 |
| [P11](#p11--existencias) | Existencias | inventario.html | F18 | RN01 |
| [P12](#p12--entrada-de-combustible) | Entrada de combustible | inventario-entrada.html | F16 | RN04, RN06 |
| [P13](#p13--salida-de-combustible) | Salida de combustible | inventario-salida.html | F17, F18 | RN01, RN06 |
| [P14](#p14--movimientos-de-inventario) | Movimientos de inventario | inventario-movimientos.html | F19 | RN01, RN04 |
| [P15](#p15--resumen-financiero) | Resumen financiero | finanzas.html | F28 | RN04, RN05 |
| [P16](#p16--movimiento-economico) | Movimiento económico | movimiento-economico.html | F26, F27 | RN06 |
| [P17](#p17--conceptos-economicos) | Conceptos económicos | conceptos.html | F23, F24, F25 | RN03 |
| [P18](#p18--empleados) | Empleados | empleados.html | F29, F30, F31 | RN03 |
| [P19](#p19--usuarios) | Usuarios | usuarios.html | F32 | RN03 |
| [P20](#p20--compras) | Compras | compras.html | F13, F14 | RN04, RN06 |
| [P21](#p21--detalle-de-compra) | Detalle de compra | compra-detalle.html | F15 | RN04 |

## P01 · Inicio / Publicidad

- **Objetivo:** Presentar la estación y acceder al login.
- **Actor:** Visitante.
- **Archivo:** [index.html](../index.html), [publicidad.html](../publicidad.html).
- **Entidades:** No aplica; contenido público.
- **Funcionalidades:** No aplica.
- **Reglas:** No aplica.
- **Campos / contenido:** Precios de referencia, beneficios, acceso y contacto.
- **Acciones:** Navegar a contacto, a la sección de beneficios y al acceso.
- **Origen:** Navegación pública.
- **Destino:** login.html, contacto.html, publicidad.html#combustibles-beneficios.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P02 · Login

- **Objetivo:** Representar el acceso de demostración.
- **Actor:** Administrador y Operador / Vendedor.
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

- **Objetivo:** Consultar un panorama de la operación del día y de la semana.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [dashboard.html](../dashboard.html).
- **Entidades:** Venta DetalleVenta Producto MovimientoCaja.
- **Funcionalidades:** F02, F03.
- **Reglas:** No aplica.
- **Campos / contenido:** Cinco indicadores (ventas, stock, ingresos, egresos, saldo) y cinco series temporales diarias 04–10 sep. 2026, más las últimas ventas.
- **Acciones:** Cerrar sesión; navegar a cada módulo.
- **Origen:** Tras iniciar sesión y navegación común.
- **Destino:** index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P04 · Contacto

- **Objetivo:** Mostrar los canales ficticios de contacto.
- **Actor:** Visitante.
- **Archivo:** [contacto.html](../contacto.html).
- **Entidades:** No aplica; contenido público.
- **Funcionalidades:** No aplica.
- **Reglas:** No aplica.
- **Campos / contenido:** Nombre, correo, asunto y mensaje.
- **Acciones:** Enviar mensaje (visual).
- **Origen:** Navegación pública.
- **Destino:** index.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P05 · Categorías

- **Objetivo:** Organizar las familias de combustible y mostrar qué combustibles agrupa cada una.
- **Actor:** Administrador.
- **Archivo:** [categorias.html](../categorias.html).
- **Entidades:** Categoria.
- **Funcionalidades:** F04, F05, F06, F07, F08.
- **Reglas:** RN03.
- **Campos / contenido:** Código, nombre, descripción, combustibles asociados y estado.
- **Acciones:** Registrar, editar y cambiar estado de la categoría.
- **Origen:** Navegación interna y dashboard.
- **Destino:** combustibles.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P06 · Combustibles

- **Objetivo:** Consultar el catálogo de combustibles con su categoría, precio y existencias.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [combustibles.html](../combustibles.html).
- **Entidades:** Producto Categoria.
- **Funcionalidades:** F10, F12.
- **Reglas:** RN02, RN03.
- **Campos / contenido:** Código, nombre, categoría, unidad, precio por litro, stock y estado.
- **Acciones:** Editar el combustible; cambiar su estado.
- **Origen:** Navegación interna y dashboard.
- **Destino:** combustible-form.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P07 · Formulario de combustible

- **Objetivo:** Dar de alta o editar un combustible.
- **Actor:** Administrador.
- **Archivo:** [combustible-form.html](../combustible-form.html).
- **Entidades:** Producto Categoria.
- **Funcionalidades:** F09, F11.
- **Reglas:** RN02, RN03, RN06.
- **Campos / contenido:** Categoría, nombre, unidad, precio por litro, stock y estado.
- **Acciones:** Guardar (visual) y cancelar.
- **Origen:** combustibles.html y navegación interna.
- **Destino:** combustibles.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P08 · Registrar venta

- **Objetivo:** Representar una venta de combustible con disponibilidad y detalle.
- **Actor:** Operador / Vendedor.
- **Archivo:** [ventas.html](../ventas.html).
- **Entidades:** Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja.
- **Funcionalidades:** F18, F20.
- **Reglas:** RN01, RN02, RN05, RN06.
- **Campos / contenido:** Producto, cantidad, descuento, existencias al confirmar e importe.
- **Acciones:** Registrar venta (visual) y cancelar el escenario.
- **Origen:** Navegación interna y dashboard.
- **Destino:** ventas-historial.html, venta-detalle.html, inventario.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P09 · Historial de ventas

- **Objetivo:** Consultar las ventas confirmadas y su ingreso económico asociado.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [ventas-historial.html](../ventas-historial.html).
- **Entidades:** Venta DetalleVenta Producto Usuario MovimientoCaja.
- **Funcionalidades:** F21.
- **Reglas:** RN05.
- **Campos / contenido:** Código, fecha, combustible, litros, total, ingreso asociado, operador y estado.
- **Acciones:** Ver detalle de la venta.
- **Origen:** ventas.html y navegación interna.
- **Destino:** venta-detalle.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P10 · Detalle de venta

- **Objetivo:** Consultar cada venta con sus detalles, su salida de inventario y su ingreso.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [venta-detalle.html](../venta-detalle.html).
- **Entidades:** Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja.
- **Funcionalidades:** F22.
- **Reglas:** RN05.
- **Campos / contenido:** Cabecera de la venta (V001, V002, V003), detalle, total, movimiento de inventario y movimiento de caja.
- **Acciones:** Volver al historial.
- **Origen:** ventas-historial.html y navegación interna.
- **Destino:** ventas-historial.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P11 · Existencias

- **Objetivo:** Consultar la disponibilidad física en litros.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [inventario.html](../inventario.html).
- **Entidades:** Producto.
- **Funcionalidades:** F18.
- **Reglas:** RN01.
- **Campos / contenido:** Existencia por producto, umbral visual y total físico del día.
- **Acciones:** Navegar a entrada, salida y libro de movimientos.
- **Origen:** Navegación interna y dashboard.
- **Destino:** inventario-entrada.html, inventario-salida.html, inventario-movimientos.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P12 · Entrada de combustible

- **Objetivo:** Representar la entrada de combustible por compra o por ajuste.
- **Actor:** Administrador.
- **Archivo:** [inventario-entrada.html](../inventario-entrada.html).
- **Entidades:** MovimientoInventario Producto Usuario Compra.
- **Funcionalidades:** F16.
- **Reglas:** RN04, RN06.
- **Campos / contenido:** Producto, cantidad en litros, fecha y hora, motivo y responsable.
- **Acciones:** Registrar entrada (visual).
- **Origen:** inventario.html y navegación interna.
- **Destino:** inventario-movimientos.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P13 · Salida de combustible

- **Objetivo:** Representar un retiro físico justificado con comprobación de stock.
- **Actor:** Operador / Vendedor.
- **Archivo:** [inventario-salida.html](../inventario-salida.html).
- **Entidades:** MovimientoInventario Producto Usuario.
- **Funcionalidades:** F17, F18.
- **Reglas:** RN01, RN06.
- **Campos / contenido:** Producto, cantidad en litros, motivo y responsable, con existencia actual y proyectada.
- **Acciones:** Registrar salida (visual).
- **Origen:** inventario.html y navegación interna.
- **Destino:** inventario-movimientos.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P14 · Movimientos de inventario

- **Objetivo:** Consultar la trazabilidad física del combustible.
- **Actor:** Administrador.
- **Archivo:** [inventario-movimientos.html](../inventario-movimientos.html).
- **Entidades:** MovimientoInventario Producto Usuario Compra.
- **Funcionalidades:** F19.
- **Reglas:** RN01, RN04.
- **Campos / contenido:** Libro de entradas y salidas con saldo inicial, entradas de la compra y salidas por venta.
- **Acciones:** Navegar a entrada y salida.
- **Origen:** inventario.html y navegación interna.
- **Destino:** inventario-entrada.html, inventario-salida.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P15 · Resumen financiero

- **Objetivo:** Consultar los movimientos de dinero y la conciliación de caja.
- **Actor:** Administrador y Operador / Vendedor.
- **Archivo:** [finanzas.html](../finanzas.html).
- **Entidades:** MovimientoCaja ConceptoMovimiento Venta Compra.
- **Funcionalidades:** F28.
- **Reglas:** RN04, RN05.
- **Campos / contenido:** KPI de ingresos, egresos y saldo con su apertura, más el listado de movimientos con código, fecha, concepto, tipo, monto, origen y responsable.
- **Acciones:** Registrar un movimiento nuevo.
- **Origen:** Navegación interna y dashboard.
- **Destino:** movimiento-economico.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P16 · Movimiento económico

- **Objetivo:** Representar un ingreso manual o un gasto manual.
- **Actor:** Administrador.
- **Archivo:** [movimiento-economico.html](../movimiento-economico.html).
- **Entidades:** MovimientoCaja ConceptoMovimiento.
- **Funcionalidades:** F26, F27.
- **Reglas:** RN06.
- **Campos / contenido:** Concepto, tipo, monto, descripción, fecha y responsable.
- **Acciones:** Registrar el movimiento (visual).
- **Origen:** finanzas.html y navegación interna.
- **Destino:** finanzas.html, conceptos.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P17 · Conceptos económicos

- **Objetivo:** Clasificar los movimientos económicos por tipo.
- **Actor:** Administrador.
- **Archivo:** [conceptos.html](../conceptos.html).
- **Entidades:** ConceptoMovimiento.
- **Funcionalidades:** F23, F24, F25.
- **Reglas:** RN03.
- **Campos / contenido:** Nombre, tipo (ingreso o egreso) y estado.
- **Acciones:** Registrar, editar, activar o desactivar el concepto.
- **Origen:** Navegación interna y dashboard.
- **Destino:** movimiento-economico.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P18 · Empleados

- **Objetivo:** Representar la administración del personal.
- **Actor:** Administrador.
- **Archivo:** [empleados.html](../empleados.html).
- **Entidades:** Empleado.
- **Funcionalidades:** F29, F30, F31.
- **Reglas:** RN03.
- **Campos / contenido:** DNI, nombres, apellidos, cargo, teléfono y estado.
- **Acciones:** Registrar, editar, activar o desactivar al empleado.
- **Origen:** Navegación interna y dashboard.
- **Destino:** usuarios.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P19 · Usuarios

- **Objetivo:** Representar las cuentas de acceso y sus roles conceptuales.
- **Actor:** Administrador.
- **Archivo:** [usuarios.html](../usuarios.html).
- **Entidades:** Usuario Empleado.
- **Funcionalidades:** F32.
- **Reglas:** RN03.
- **Campos / contenido:** Usuario, empleado asociado, rol y estado; una cuenta como máximo por empleado.
- **Acciones:** Registrar, editar, activar o desactivar la cuenta.
- **Origen:** Navegación interna y dashboard.
- **Destino:** empleados.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P20 · Compras

- **Objetivo:** Registrar y consultar el abastecimiento de combustible.
- **Actor:** Administrador.
- **Archivo:** [compras.html](../compras.html).
- **Entidades:** Compra DetalleCompra Producto MovimientoInventario MovimientoCaja.
- **Funcionalidades:** F13, F14.
- **Reglas:** RN04, RN06.
- **Campos / contenido:** KPI de compras, litros adquiridos y efecto en inventario; listado con código, fecha, proveedor, combustibles, litros, total, estado y detalle; formulario de registro.
- **Acciones:** Nueva compra (visual); ver detalle de la compra.
- **Origen:** Navegación interna y dashboard.
- **Destino:** compra-detalle.html, inventario.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## P21 · Detalle de compra

- **Objetivo:** Consultar cada compra y sus efectos en inventario y caja.
- **Actor:** Administrador.
- **Archivo:** [compra-detalle.html](../compra-detalle.html).
- **Entidades:** Compra DetalleCompra Producto MovimientoInventario MovimientoCaja.
- **Funcionalidades:** F15.
- **Reglas:** RN04.
- **Campos / contenido:** Cabecera de la compra C001 con proveedor y egreso asociado, líneas con cantidad, precio de compra y subtotal, total, y tabla de entradas generadas.
- **Acciones:** Volver a compras; ver movimientos de inventario.
- **Origen:** compras.html y navegación interna.
- **Destino:** compras.html, inventario-movimientos.html, index.html, dashboard.html, categorias.html, combustibles.html, compras.html, inventario.html, ventas.html, finanzas.html, empleados.html, usuarios.html, contacto.html, login.html.
- **Estado:** DEFINIDA + MAQUETADA; lógica de negocio no IMPLEMENTADA.

## Contadores

| Indicador | Valor |
|---|---:|
| Interfaces totales | 21 |
| Archivos HTML | 22 |
| Interfaces con al menos una funcionalidad (excluyendo P01 y P04, públicas) | 19 |
| Interfaces públicas sin funcionalidad | 2 |
| Funcionalidades sin interfaz | 0 |

