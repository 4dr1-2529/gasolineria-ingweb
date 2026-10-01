# Trazabilidad integral

Modelo de negocio → entidad → funcionalidad → regla → interfaz → archivo HTML. Las relaciones se refieren a representación estática, no a ejecución de lógica. Hay 34 pares funcionalidad–interfaz, 32 funcionalidades y 21 interfaces.

| Modelo de negocio | Entidades | Funcionalidad | Regla | Interfaz | Archivo HTML |
|---|---|---|---|---|---|
| Acceso | Usuario | F01 Iniciar sesión | No aplica | P02 | [login.html](../login.html) |
| Acceso | Venta DetalleVenta Producto MovimientoCaja | F02 Cerrar sesión | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Dashboard | Venta DetalleVenta Producto MovimientoCaja | F03 Consultar dashboard | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Categorías | Categoria | F04 Registrar categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F05 Consultar categorías | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F06 Consultar combustibles por categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F07 Editar categoría | RN03 | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F08 Activar/desactivar categoría | RN03 | P05 | [categorias.html](../categorias.html) |
| Combustibles | Producto Categoria | F09 Registrar combustible | RN02, RN06 | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F10 Consultar combustibles | RN02 | P06 | [combustibles.html](../combustibles.html) |
| Combustibles | Producto Categoria | F11 Editar combustible | RN02, RN03, RN06 | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F12 Activar/desactivar combustible | RN02, RN03 | P06 | [combustibles.html](../combustibles.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F13 Registrar compra de combustible | RN04, RN06 | P20 | [compras.html](../compras.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F14 Consultar compras | RN04 | P20 | [compras.html](../compras.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F15 Consultar detalle de compra | RN04 | P21 | [compra-detalle.html](../compra-detalle.html) |
| Inventario | MovimientoInventario Producto Usuario Compra | F16 Registrar entrada de combustible | RN04, RN06 | P12 | [inventario-entrada.html](../inventario-entrada.html) |
| Inventario | MovimientoInventario Producto Usuario | F17 Registrar salida de combustible | RN01, RN06 | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F18 Consultar existencias | RN01 | P08 | [ventas.html](../ventas.html) |
| Inventario | Producto | F18 Consultar existencias | RN01 | P11 | [inventario.html](../inventario.html) |
| Inventario | MovimientoInventario Producto Usuario | F18 Consultar existencias | RN01 | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | MovimientoInventario Producto Usuario Compra | F19 Consultar movimientos de inventario | RN01, RN04 | P14 | [inventario-movimientos.html](../inventario-movimientos.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F20 Registrar venta | RN01, RN02, RN05, RN06 | P08 | [ventas.html](../ventas.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoCaja | F21 Consultar ventas | RN05 | P09 | [ventas-historial.html](../ventas-historial.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F22 Consultar detalle de venta | RN05 | P10 | [venta-detalle.html](../venta-detalle.html) |
| Finanzas | ConceptoMovimiento | F23 Registrar concepto económico | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | ConceptoMovimiento | F24 Consultar conceptos económicos | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | ConceptoMovimiento | F25 Editar/activar/desactivar concepto económico | RN03 | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento | F26 Registrar ingreso económico | RN06 | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento | F27 Registrar egreso económico | RN06 | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Venta Compra | F28 Consultar movimientos y saldo de caja | RN04, RN05 | P15 | [finanzas.html](../finanzas.html) |
| Empleados | Empleado | F29 Registrar empleado | No aplica | P18 | [empleados.html](../empleados.html) |
| Empleados | Empleado | F30 Consultar empleados | No aplica | P18 | [empleados.html](../empleados.html) |
| Empleados | Empleado | F31 Editar/activar/desactivar empleado | RN03 | P18 | [empleados.html](../empleados.html) |
| Usuarios | Usuario Empleado | F32 Gestionar usuarios | RN03 | P19 | [usuarios.html](../usuarios.html) |

P01 → presentación pública → sin entidad ni función adicional → index.html y publicidad.html. P04 → contacto público → sin entidad persistente ni función adicional → contacto.html.

## Cadenas principales

### Compra y abastecimiento (RN04)

Compra → Compra / DetalleCompra / Producto / MovimientoInventario / MovimientoCaja → F13, F14, F15, F16, F19, F28 → RN04, RN06 → P12, P14, P15, P20, P21 → compras.html, compra-detalle.html, inventario-entrada.html, inventario-movimientos.html, finanzas.html.

Las entradas MI001–MI003 son efecto de F16 y se consultan en F15 y en F19; el egreso MC001 es efecto de F13 y se consulta en F28. F27 representa egresos manuales, sin duplicar los de compra.

### Venta e ingreso (RN05)

Venta → Venta / DetalleVenta / Producto / MovimientoInventario / MovimientoCaja → F18, F20, F21, F22, F28 → RN01, RN02, RN05, RN06 → P08, P09, P10, P11, P13, P14, P15 → ventas.html, ventas-historial.html, venta-detalle.html, inventario.html, inventario-movimientos.html, finanzas.html.

La salida por venta es efecto de F20 y se consulta en F19. El ingreso asociado es efecto de F20 y se consulta en F21 y en F28; F26 representa ingresos manuales, sin duplicar los de ventas.

### Catálogo (RN02, RN03, RN06)

Categoría → Producto → F04–F12 → RN02, RN03, RN06 → P05, P06, P07 → categorias.html, combustibles.html, combustible-form.html. Los productos con historial se desactivan, nunca se eliminan.

### Control de acceso y personal (RN03)

Empleado → Usuario → F29–F32 → RN03 → P18, P19 → empleados.html, usuarios.html. F01 y F02 no aplican reglas de negocio: son navegación.

## Contadores

| Indicador | Valor |
|---|---:|
| Funcionalidades totales | 32 |
| Funcionalidades sin interfaz | 0 |
| Interfaces totales | 21 |
| Interfaces sin funcionalidad (excluyendo P01 y P04, públicas) | 0 |
| Reglas totales | 6 |
| Reglas sin funcionalidad | 0 |
| Reglas sin interfaz | 0 |
| Entidades totales | 11 |
| Pares funcionalidad–interfaz | 34 |
