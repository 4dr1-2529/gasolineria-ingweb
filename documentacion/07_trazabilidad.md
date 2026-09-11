# Trazabilidad integral

Modelo de negocio → entidad → funcionalidad → regla → interfaz → archivo HTML. Las relaciones se refieren a representación estática, no a ejecución de lógica.

| Modelo de negocio | Entidades | Funcionalidad | Regla | Interfaz | Archivo HTML |
|---|---|---|---|---|---|
| Acceso | Usuario | F01 Iniciar sesión | No aplica | P02 | [login.html](../login.html) |
| Acceso | Venta Producto MovimientoCaja Asistencia | F02 Cerrar sesión | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Dashboard | Venta Producto MovimientoCaja Asistencia | F03 Consultar dashboard | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Categorías | Categoria | F04 Registrar categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F05 Consultar categorías | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F06 Editar categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F07 Activar/desactivar categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Combustibles | Producto Categoria | F08 Registrar combustible | No aplica | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F09 Consultar combustibles | No aplica | P06 | [combustibles.html](../combustibles.html) |
| Combustibles | Producto Categoria | F10 Editar combustible | No aplica | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F11 Activar/desactivar combustible | No aplica | P06 | [combustibles.html](../combustibles.html) |
| Inventario | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F12 Consultar existencias | No aplica | P08 | [ventas.html](../ventas.html) |
| Inventario | Producto | F12 Consultar existencias | No aplica | P11 | [inventario.html](../inventario.html) |
| Inventario | Producto Usuario MovimientoInventario | F12 Consultar existencias | No aplica | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | Producto Usuario MovimientoInventario | F13 Registrar entrada de combustible | No aplica | P12 | [inventario-entrada.html](../inventario-entrada.html) |
| Inventario | Producto Usuario MovimientoInventario | F14 Registrar salida de combustible | No aplica | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | MovimientoInventario Producto Usuario | F15 Consultar movimientos de inventario | No aplica | P14 | [inventario-movimientos.html](../inventario-movimientos.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F16 Registrar venta | No aplica | P08 | [ventas.html](../ventas.html) |
| Ventas | Venta DetalleVenta Producto Usuario | F17 Consultar ventas | No aplica | P09 | [ventas-historial.html](../ventas-historial.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoCaja | F18 Consultar detalle de venta | No aplica | P10 | [venta-detalle.html](../venta-detalle.html) |
| Finanzas | ConceptoMovimiento | F19 Registrar concepto económico | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | ConceptoMovimiento | F20 Consultar conceptos económicos | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | ConceptoMovimiento | F21 Editar/activar/desactivar concepto económico | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Usuario Venta | F22 Registrar ingreso económico | No aplica | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Usuario Venta | F23 Registrar egreso económico | No aplica | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Venta | F24 Consultar movimientos económicos | No aplica | P15 | [finanzas.html](../finanzas.html) |
| Empleados | Empleado | F25 Registrar empleado | No aplica | P18 | [empleados.html](../empleados.html) |
| Empleados | Empleado | F26 Consultar empleados | No aplica | P18 | [empleados.html](../empleados.html) |
| Empleados | Empleado | F27 Editar/activar/desactivar empleado | No aplica | P18 | [empleados.html](../empleados.html) |
| Usuarios | Usuario Empleado | F28 Gestionar usuarios | No aplica | P19 | [usuarios.html](../usuarios.html) |
| Asistencia | Asistencia Empleado | F29 Registrar entrada de asistencia | No aplica | P20 | [asistencia.html](../asistencia.html) |
| Asistencia | Asistencia Empleado | F30 Registrar salida y consultar historial de asistencia | No aplica | P20 | [asistencia.html](../asistencia.html) |

P01 → presentación pública → sin entidad ni función adicional → index.html y publicidad.html. P04 → contacto público → sin entidad persistente ni función adicional → contacto.html.

## Cadena principal

Venta → Venta / DetalleVenta / Producto / MovimientoInventario / MovimientoCaja → F12, F16, F17, F18 → RN01, RN02, RN04 → P08, P09, P10, P11, P14, P15 → ventas.html, ventas-historial.html, venta-detalle.html, inventario.html, inventario-movimientos.html, finanzas.html.

La salida por venta es efecto de F16 y se consulta en F15. El ingreso asociado es efecto de F16 y se consulta en F24; F22 representa ingresos manuales, sin duplicar los de ventas.
