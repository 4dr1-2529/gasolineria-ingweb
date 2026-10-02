# Trazabilidad integral

Modelo de negocio → entidad → funcionalidad → regla → interfaz → archivo HTML. Las relaciones se refieren a representación estática, no a ejecución de lógica. Hay **47 pares** funcionalidad–interfaz, **38 funcionalidades** y **30 interfaces**; las **12 entidades** y las **10 reglas de negocio (RN01–RN10)** completan la cadena. Los 31 archivos HTML de la raíz cubren esas 30 interfaces porque publicidad.html es la segunda presentación de P01.

| Modelo de negocio | Entidades | Funcionalidad | Regla | Interfaz | Archivo HTML |
|---|---|---|---|---|---|
| Acceso | Usuario | F01 Iniciar sesión | No aplica | P02 | [login.html](../login.html) |
| Acceso | Venta DetalleVenta Producto MovimientoCaja | F02 Cerrar sesión | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Dashboard | Venta DetalleVenta Producto MovimientoCaja | F03 Consultar dashboard | No aplica | P03 | [dashboard.html](../dashboard.html) |
| Categorías | Categoria | F04 Registrar categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F04 Registrar categoría | No aplica | P22 | [categoria-form.html](../categoria-form.html) |
| Categorías | Categoria | F05 Consultar categorías | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F06 Consultar combustibles por categoría | No aplica | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F07 Editar categoría | RN03 | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F07 Editar categoría | RN03 | P22 | [categoria-form.html](../categoria-form.html) |
| Categorías | Categoria | F08 Activar/desactivar categoría | RN03 | P05 | [categorias.html](../categorias.html) |
| Categorías | Categoria | F08 Activar/desactivar categoría | RN03 | P22 | [categoria-form.html](../categoria-form.html) |
| Combustibles | Producto Categoria | F09 Registrar combustible | RN02, RN06 | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F10 Consultar combustibles | RN02 | P06 | [combustibles.html](../combustibles.html) |
| Combustibles | Producto Categoria | F11 Editar combustible | RN02, RN03, RN06 | P07 | [combustible-form.html](../combustible-form.html) |
| Combustibles | Producto Categoria | F12 Activar/desactivar combustible | RN02, RN03 | P06 | [combustibles.html](../combustibles.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F13 Registrar compra de combustible | RN04, RN06 | P20 | [compras.html](../compras.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F13 Registrar compra de combustible | RN04, RN06 | P23 | [compra-form.html](../compra-form.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F14 Consultar compras | RN04 | P20 | [compras.html](../compras.html) |
| Compras | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | F15 Consultar detalle de compra | RN04 | P21 | [compra-detalle.html](../compra-detalle.html) |
| Inventario | MovimientoInventario Producto Usuario Compra | F16 Registrar entrada de combustible | RN04, RN06 | P12 | [inventario-entrada.html](../inventario-entrada.html) |
| Inventario | MovimientoInventario Producto Usuario | F17 Registrar salida de combustible | RN01, RN06 | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F18 Consultar existencias | RN01 | P08 | [ventas.html](../ventas.html) |
| Inventario | Producto | F18 Consultar existencias | RN01 | P11 | [inventario.html](../inventario.html) |
| Inventario | MovimientoInventario Producto Usuario | F18 Consultar existencias | RN01 | P13 | [inventario-salida.html](../inventario-salida.html) |
| Inventario | MovimientoInventario Producto Usuario Compra | F19 Consultar movimientos de inventario | RN01, RN04 | P14 | [inventario-movimientos.html](../inventario-movimientos.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F20 Registrar venta | RN01, RN02, RN05, RN06 | P08 | [ventas.html](../ventas.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F20 Registrar venta | RN01, RN02, RN05, RN06 | P24 | [venta-form.html](../venta-form.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoCaja | F21 Consultar ventas | RN05 | P09 | [ventas-historial.html](../ventas-historial.html) |
| Ventas | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | F22 Consultar detalle de venta | RN05 | P10 | [venta-detalle.html](../venta-detalle.html) |
| Finanzas | ConceptoMovimiento | F23 Registrar concepto económico | No aplica | P27 | [concepto-form.html](../concepto-form.html) |
| Finanzas | ConceptoMovimiento | F24 Consultar conceptos económicos | No aplica | P17 | [conceptos.html](../conceptos.html) |
| Finanzas | ConceptoMovimiento | F25 Editar/activar/desactivar concepto económico | RN03 | P27 | [concepto-form.html](../concepto-form.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento | F26 Consultar ingresos de caja | RN05 | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento | F27 Consultar egresos de caja | RN04 | P16 | [movimiento-economico.html](../movimiento-economico.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Venta Compra | F28 Consultar movimientos y saldo de caja | RN04, RN05 | P15 | [finanzas.html](../finanzas.html) |
| Finanzas | MovimientoCaja ConceptoMovimiento Venta Compra | F28 Consultar movimientos y saldo de caja | RN04, RN05 | P30 | [movimiento-detalle.html](../movimiento-detalle.html) |
| Empleados | Empleado | F29 Registrar empleado | No aplica | P25 | [empleado-form.html](../empleado-form.html) |
| Empleados | Empleado | F30 Consultar empleados | No aplica | P18 | [empleados.html](../empleados.html) |
| Empleados | Empleado | F31 Editar/activar/desactivar empleado | RN03 | P25 | [empleado-form.html](../empleado-form.html) |
| Usuarios | Usuario Empleado | F32 Gestionar usuarios | RN03 | P19 | [usuarios.html](../usuarios.html) |
| Usuarios | Usuario Empleado | F32 Gestionar usuarios | RN03 | P26 | [usuario-form.html](../usuario-form.html) |
| Portada y contacto | No aplica | F33 Consultar la portada pública | No aplica | P01 | [index.html](../index.html) y [publicidad.html](../publicidad.html) |
| Portada y contacto | No aplica | F34 Enviar mensaje de contacto | No aplica | P04 | [contacto.html](../contacto.html) |
| Asistencia | Asistencia Empleado | F35 Registrar asistencia | RN07, RN08, RN10 | P28 | [mi-asistencia.html](../mi-asistencia.html) |
| Asistencia | Asistencia Empleado | F36 Consultar mi asistencia | RN07, RN08, RN09, RN10 | P28 | [mi-asistencia.html](../mi-asistencia.html) |
| Asistencia | Asistencia Empleado | F37 Consultar mi resumen de asistencia | RN07 | P28 | [mi-asistencia.html](../mi-asistencia.html) |
| Asistencia | Asistencia Empleado | F38 Consultar asistencia del personal | RN08, RN10 | P29 | [control-asistencia.html](../control-asistencia.html) |

P01 → presentación pública → sin entidad persistente → F33 → index.html y publicidad.html. P04 → contacto público → sin entidad persistente → F34 → contacto.html. La relación nueva del modelo es Empleado 1:N Asistencia y las 12 entidades son Categoria, Producto, Compra, DetalleCompra, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, MovimientoCaja y Asistencia.

## Cadenas principales

### Compra y abastecimiento (RN04)

Compra → Compra / DetalleCompra / Producto / MovimientoInventario / MovimientoCaja → F13, F14, F15, F16, F19, F27, F28 → RN04, RN06 → P12, P14, P15, P16, P20, P21, P23, P30 → compras.html, compra-form.html, compra-detalle.html, inventario-entrada.html, inventario-movimientos.html, finanzas.html, movimiento-economico.html, movimiento-detalle.html.

Las entradas MI001–MI003 son efecto de F16 y se consultan en F15 y en F19; el egreso MC001 es efecto de F13 y se consulta en F27 y en F28.

### Venta e ingreso (RN05)

Venta → Venta / DetalleVenta / Producto / MovimientoInventario / MovimientoCaja → F18, F20, F21, F22, F26, F28 → RN01, RN02, RN05, RN06 → P08, P09, P10, P11, P13, P14, P15, P16, P24, P30 → ventas.html, venta-form.html, ventas-historial.html, venta-detalle.html, inventario.html, inventario-salida.html, inventario-movimientos.html, finanzas.html, movimiento-economico.html, movimiento-detalle.html.

La salida por venta es efecto de F20 y se consulta en F19. El ingreso asociado es efecto de F20 y se consulta en F21 y en F26; F26 muestra los ingresos del día (MC003, MC004, MC005) y F27 muestra el egreso de la compra (MC001).

### Catálogo (RN02, RN03, RN06)

Categoría → Producto → F04–F12 → RN02, RN03, RN06 → P05, P06, P07, P22 → categorias.html, categoria-form.html, combustibles.html, combustible-form.html. Los productos con historial se desactivan, nunca se eliminan.

### Control de acceso y personal (RN03)

Empleado → Usuario → F29–F32 → RN03 → P18, P19, P25, P26 → empleados.html, empleado-form.html, usuarios.html, usuario-form.html. F01 y F02 no aplican reglas de negocio: son navegación.

### Asistencia (RN07, RN08, RN09, RN10)

Empleado → Asistencia → F35, F36, F37, F38 → RN07, RN08, RN09, RN10 → P28, P29 → mi-asistencia.html, control-asistencia.html. Cada empleado tiene un solo registro de asistencia por fecha (RN08) y el estado se calcula a partir de las horas (RN10).

### Portada y contacto (sin reglas)

Portada y contacto → sin entidad persistente → F33, F34 → P01, P04 → index.html, publicidad.html, contacto.html. Son contenido público y no añaden reglas de negocio.

## Contadores

| Indicador | Valor |
|---|---:|
| Funcionalidades totales | 38 |
| Funcionalidades sin interfaz | 0 |
| Funcionalidades sin regla | 12 |
| Interfaces totales | 30 |
| Interfaces sin funcionalidad | 0 |
| Reglas totales | 10 |
| Reglas sin funcionalidad | 0 |
| Reglas sin interfaz | 0 |
| Entidades totales | 12 |
| Pares funcionalidad–interfaz | 47 |
| Archivos HTML en la raíz | 31 |

## Cifras del corte (10 de septiembre de 2026)

| Indicador | Valor |
|---|---|
| Diccionario de datos | 74 atributos, 12 PK, 15 FK, 15 relaciones |
| Caja del día (soles) | 4,410.00 + 370.00 − 1,350.00 = 3,430.00 |
| Stock de combustible | 6,700 + 300 − 80 = 6,920 L |

El saldo de caja del día es S/ 3,430.00: apertura S/ 4,410.00, ingresos S/ 370.00 y egresos S/ 1,350.00. El stock total es 6,920 L (Regular 1,990 · Premium 980 · Diésel 3,950).
