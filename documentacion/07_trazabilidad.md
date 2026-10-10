# Trazabilidad integral · Estación Nexo

Cadena de trazabilidad: **Proceso → Regla de negocio → Funcionalidad → Interfaz → Entidad** (y su archivo HTML). *Proceso* es el bloque narrativo del modelo de negocio: Catálogo, Abastecimiento, Operación, Control (inventario), Personal, Finanzas, Acceso y Público. Las relaciones se refieren a lo que el sistema representa y, en la versión V2, a lo que ejecuta sobre datos en memoria.

Hay **47 pares** funcionalidad–interfaz, **38 funcionalidades**, **30 interfaces**, **12 entidades** y **5 reglas de negocio (RN01–RN05)**; ninguna funcionalidad carece de interfaz y ninguna regla se queda sin funcionalidad. Los 31 archivos HTML de `diseno-original/` cubren esas 30 interfaces porque publicidad.html es la segunda presentación de P01.

## Cadenas principales

- **Venta** → RN01/RN03/RN04 → F20 → P24/P09/P10 → Venta / DetalleVenta / Producto / MovimientoInventario / MovimientoCaja
- **Compra** → RN03/RN04 → F13 → P23/P20/P21 → Compra / DetalleCompra / Producto / MovimientoInventario / MovimientoCaja
- **Asistencia** → RN05 → F35-F38 → P28/P29 → Empleado / Usuario / Asistencia

En la venta, el detalle y la salida de inventario se crean una sola vez (RN03) y el ingreso de caja queda vinculado a la venta (RN04). En la compra, los detalles y las entradas por línea se crean juntos con ella (RN03) y el egreso queda vinculado a la compra (RN04). En asistencia, cada empleado registra su propia jornada sin duplicados (RN05).

## Trazabilidad completa (47 pares)

| Proceso | Regla | Funcionalidad | Interfaz | Entidades | Archivo HTML |
|---|---|---|---|---|---|
| Acceso | No aplica | F01 Iniciar sesión | P02 | Usuario | [login.html](../diseno-original/login.html) |
| Acceso | No aplica | F02 Cerrar sesión | P03 | No aplica | [dashboard.html](../diseno-original/dashboard.html) |
| Operación | No aplica | F03 Consultar dashboard | P03 | Venta DetalleVenta Producto MovimientoCaja | [dashboard.html](../diseno-original/dashboard.html) |
| Catálogo | No aplica | F04 Registrar categoría | P05 | Categoria | [categorias.html](../diseno-original/categorias.html) |
| Catálogo | No aplica | F04 Registrar categoría | P22 | Categoria | [categoria-form.html](../diseno-original/categoria-form.html) |
| Catálogo | No aplica | F05 Consultar categorías | P05 | Categoria | [categorias.html](../diseno-original/categorias.html) |
| Catálogo | No aplica | F06 Consultar combustibles por categoría | P05 | Categoria Producto | [categorias.html](../diseno-original/categorias.html) |
| Catálogo | RN02 | F07 Editar categoría | P05 | Categoria | [categorias.html](../diseno-original/categorias.html) |
| Catálogo | RN02 | F07 Editar categoría | P22 | Categoria | [categoria-form.html](../diseno-original/categoria-form.html) |
| Catálogo | RN02 | F08 Activar/desactivar categoría | P05 | Categoria | [categorias.html](../diseno-original/categorias.html) |
| Catálogo | RN02 | F08 Activar/desactivar categoría | P22 | Categoria | [categoria-form.html](../diseno-original/categoria-form.html) |
| Catálogo | No aplica | F09 Registrar combustible | P07 | Producto Categoria | [combustible-form.html](../diseno-original/combustible-form.html) |
| Catálogo | No aplica | F10 Consultar combustibles | P06 | Producto Categoria | [combustibles.html](../diseno-original/combustibles.html) |
| Catálogo | RN02 | F11 Editar combustible | P07 | Producto Categoria | [combustible-form.html](../diseno-original/combustible-form.html) |
| Catálogo | RN02 | F12 Activar/desactivar combustible | P06 | Producto Categoria | [combustibles.html](../diseno-original/combustibles.html) |
| Abastecimiento | RN03, RN04 | F13 Registrar compra de combustible | P20 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | [compras.html](../diseno-original/compras.html) |
| Abastecimiento | RN03, RN04 | F13 Registrar compra de combustible | P23 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | [compra-form.html](../diseno-original/compra-form.html) |
| Abastecimiento | RN03, RN04 | F14 Consultar compras | P20 | Compra DetalleCompra Producto | [compras.html](../diseno-original/compras.html) |
| Abastecimiento | RN03, RN04 | F15 Consultar detalle de compra | P21 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | [compra-detalle.html](../diseno-original/compra-detalle.html) |
| Control (inventario) | RN01, RN03 | F16 Registrar entrada de combustible | P12 | MovimientoInventario Producto Usuario Compra | [inventario-entrada.html](../diseno-original/inventario-entrada.html) |
| Control (inventario) | RN01 | F17 Registrar salida de combustible | P13 | MovimientoInventario Producto Usuario | [inventario-salida.html](../diseno-original/inventario-salida.html) |
| Control (inventario) | RN01 | F18 Consultar existencias | P08 | Producto | [ventas.html](../diseno-original/ventas.html) |
| Control (inventario) | RN01 | F18 Consultar existencias | P11 | Producto | [inventario.html](../diseno-original/inventario.html) |
| Control (inventario) | RN01 | F18 Consultar existencias | P13 | Producto | [inventario-salida.html](../diseno-original/inventario-salida.html) |
| Control (inventario) | RN01, RN03 | F19 Consultar movimientos de inventario | P14 | MovimientoInventario Producto Usuario Compra | [inventario-movimientos.html](../diseno-original/inventario-movimientos.html) |
| Operación | RN01, RN02, RN03, RN04 | F20 Registrar venta | P08 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | [ventas.html](../diseno-original/ventas.html) |
| Operación | RN01, RN02, RN03, RN04 | F20 Registrar venta | P24 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | [venta-form.html](../diseno-original/venta-form.html) |
| Operación | RN03, RN04 | F21 Consultar ventas | P09 | Venta DetalleVenta Producto Usuario MovimientoCaja | [ventas-historial.html](../diseno-original/ventas-historial.html) |
| Operación | RN03, RN04 | F22 Consultar detalle de venta | P10 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | [venta-detalle.html](../diseno-original/venta-detalle.html) |
| Finanzas | No aplica | F23 Registrar concepto económico | P27 | ConceptoMovimiento | [concepto-form.html](../diseno-original/concepto-form.html) |
| Finanzas | No aplica | F24 Consultar conceptos económicos | P17 | ConceptoMovimiento | [conceptos.html](../diseno-original/conceptos.html) |
| Finanzas | RN02 | F25 Editar/activar/desactivar concepto económico | P27 | ConceptoMovimiento | [concepto-form.html](../diseno-original/concepto-form.html) |
| Finanzas | RN04 | F26 Consultar ingresos de caja | P16 | MovimientoCaja ConceptoMovimiento | [movimiento-economico.html](../diseno-original/movimiento-economico.html) |
| Finanzas | RN04 | F27 Consultar egresos de caja | P16 | MovimientoCaja ConceptoMovimiento | [movimiento-economico.html](../diseno-original/movimiento-economico.html) |
| Finanzas | RN04 | F28 Consultar movimientos y saldo de caja | P15 | MovimientoCaja ConceptoMovimiento Venta Compra | [finanzas.html](../diseno-original/finanzas.html) |
| Finanzas | RN04 | F28 Consultar movimientos y saldo de caja | P30 | MovimientoCaja ConceptoMovimiento Venta Compra | [movimiento-detalle.html](../diseno-original/movimiento-detalle.html) |
| Personal | No aplica | F29 Registrar empleado | P25 | Empleado | [empleado-form.html](../diseno-original/empleado-form.html) |
| Personal | No aplica | F30 Consultar empleados | P18 | Empleado | [empleados.html](../diseno-original/empleados.html) |
| Personal | RN02 | F31 Editar/activar/desactivar empleado | P25 | Empleado | [empleado-form.html](../diseno-original/empleado-form.html) |
| Personal | RN02 | F32 Gestionar usuarios | P19 | Usuario Empleado | [usuarios.html](../diseno-original/usuarios.html) |
| Personal | RN02 | F32 Gestionar usuarios | P26 | Usuario Empleado | [usuario-form.html](../diseno-original/usuario-form.html) |
| Público | No aplica | F33 Consultar la portada pública | P01 | No aplica | [index.html](../diseno-original/index.html) y [publicidad.html](../diseno-original/publicidad.html) |
| Público | No aplica | F34 Enviar mensaje de contacto | P04 | No aplica | [contacto.html](../diseno-original/contacto.html) |
| Personal (asistencia) | RN05 | F35 Registrar asistencia | P28 | Asistencia Empleado Usuario | [mi-asistencia.html](../diseno-original/mi-asistencia.html) |
| Personal (asistencia) | RN05 | F36 Consultar mi asistencia | P28 | Asistencia Empleado | [mi-asistencia.html](../diseno-original/mi-asistencia.html) |
| Personal (asistencia) | RN05 | F37 Consultar mi resumen de asistencia | P28 | Asistencia Empleado | [mi-asistencia.html](../diseno-original/mi-asistencia.html) |
| Personal (asistencia) | RN05 | F38 Consultar asistencia del personal | P29 | Asistencia Empleado | [control-asistencia.html](../diseno-original/control-asistencia.html) |

P01 → presentación pública → sin entidad persistente → F33 → index.html y publicidad.html. P04 → contacto público → sin entidad persistente → F34 → contacto.html. La relación nueva del modelo es Empleado 1:N Asistencia y las 12 entidades son Categoria, Producto, Compra, DetalleCompra, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, MovimientoCaja y Asistencia.

## Cadenas por proceso

- **Catálogo → RN02:** Categoría → Producto con F04–F12 en P05, P06, P07 y P22 (categorias.html, categoria-form.html, combustibles.html, combustible-form.html). Los productos con historial se desactivan, nunca se eliminan.
- **Abastecimiento → RN03, RN04:** F13–F15 en P20, P21 y P23; las entradas generadas (MI001–MI003) se consultan en P12 y P14, y el egreso MC001 en P16 y P30.
- **Operación → RN01, RN02, RN03, RN04:** F18–F22 en P08, P09, P10, P11, P13, P14, P24; la salida por venta se consulta en P14 y el ingreso (MC003–MC005) en P16 y P30.
- **Control (inventario) → RN01, RN03:** F16–F19 en P12, P13, P14, P11, P08; el libro de movimientos concilia con las existencias.
- **Finanzas → RN04:** F26–F28 en P15, P16, P30; sólo consulta, sin altas manuales de caja.
- **Personal → RN02:** F29–F32 en P18, P19, P25, P26 (empleados.html, empleado-form.html, usuarios.html, usuario-form.html). F01 y F02 no aplican reglas: son acceso.
- **Personal (asistencia) → RN05:** F35–F38 en P28 y P29 (mi-asistencia.html, control-asistencia.html). Cada empleado tiene un solo registro por fecha y el estado se calcula a partir de las horas.
- **Público → sin reglas:** F33 y F34 en P01 y P04 (index.html, publicidad.html, contacto.html).

## Contadores

| Indicador | Valor |
|---|---:|
| Funcionalidades totales | 38 |
| Funcionalidades sin interfaz | 0 |
| Funcionalidades sin regla | 14 |
| Interfaces totales | 30 |
| Interfaces sin funcionalidad | 0 |
| Interfaces sin regla | 6 |
| Reglas totales | 5 |
| Reglas sin funcionalidad | 0 |
| Reglas sin interfaz | 0 |
| Entidades totales | 12 |
| Pares funcionalidad–interfaz | 47 |
| Archivos HTML en `diseno-original/` | 31 |

## Cifras del corte (10 de septiembre de 2026)

| Indicador | Valor |
|---|---|
| Diccionario de datos | 74 atributos, 12 PK, 15 FK, 15 relaciones |
| Caja del día (soles) | 4,410.00 + 370.00 − 1,350.00 = 3,430.00 |
| Stock de combustible | 6,700 + 300 − 80 = 6,920 L |

El saldo de caja del día es S/ 3,430.00: apertura S/ 4,410.00, ingresos S/ 370.00 y egresos S/ 1,350.00. El stock total es 6,920 L (Regular 1,990 · Premium 980 · Diésel 3,950).
