# Reglas de negocio RN01–RN06 · Punto 5.9

Las seis reglas están DEFINIDAS y representadas mediante textos y escenarios. Ninguna se aplica automáticamente en esta maqueta. Todas tienen funcionalidades e interfaces asociadas; ninguna queda sin representación.

## 5.9 · Definición oficial y correspondencia

**Qué es una regla de negocio** (definición del documento oficial del proyecto): declaración formal que define o restringe algún aspecto del negocio. No es lógica de presentación (colores o botones) ni lógica de infraestructura (cómo se conecta a la base de datos); son condiciones, políticas y restricciones que el sistema debe cumplir. Las reglas de este proyecto cumplen las cuatro propiedades exigidas:

- **Atómicas:** cada regla expresa una condición o restricción única.
- **Declarativas:** dicen qué debe cumplirse, no cómo implementarlo técnicamente.
- **Estables:** cambian sólo cuando cambia la política del negocio, no por mejoras técnicas.
- **Obligatorias:** el sistema debe hacerlas cumplir en todos los casos correspondientes.

**Lo que NO es regla de negocio.** Los ejemplos del documento oficial —«El sistema mostrará la lista de las compras de un usuario», «El usuario podrá hacer login» y «Al generar una venta se descuenta stock»— describen una interfaz o un comportamiento, no una restricción; ninguno se usó aquí. En este proyecto, F01 (iniciar sesión), F02 (cerrar sesión) y F03 (consultar el tablero) son acceso y consulta: por eso figuran como funcionalidades sin regla en [04_funcionalidades.md](04_funcionalidades.md).

**Correspondencia con los seis puntos que exige el documento oficial:**

| # | Exigencia oficial | Dónde está en este documento |
|---|---|---|
| 1 | Nombre de la regla (claro y descriptivo) | **Nombre** |
| 2 | Enunciado formal en lenguaje de negocio | **Enunciado formal** (acompañado de **Condición**, su expresión formal) |
| 3 | Justificación de negocio | **Justificación** |
| 4 | Impacto en el sistema (módulos/funcionalidades afectados) | **Impacto** + **Funcionalidades relacionadas** + **Interfaces relacionadas** |
| 5 | Validación técnica sugerida | **Validación técnica sugerida** |
| 6 | Caso de prueba: cumplimiento y violación | **Caso de cumplimiento** y **Caso de violación** |

Cada regla añade **Entidades relacionadas** y **Estado** para cerrar la trazabilidad con [04_funcionalidades.md](04_funcionalidades.md), [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md), [07_trazabilidad.md](07_trazabilidad.md) y el BPMN de [09_bpmn.md](09_bpmn.md).

**Cantidad:** el documento oficial exige un mínimo de 5 reglas; este proyecto entrega **6** (RN01–RN06). No se crearon reglas para alcanzar la cifra.

## RN01 · Existencia suficiente

- **Código:** RN01.
- **Nombre:** Existencia suficiente.
- **Enunciado formal:** No se puede vender ni retirar más combustible que el stock disponible.
- **Condición:** `cantidad_solicitada <= stock_disponible`.
- **Caso de cumplimiento:** Vender 10 L con 1,990 L disponibles.
- **Caso de violación:** Retirar 2,000 L con 1,990 L disponibles.
- **Justificación:** Evitar existencias negativas y compromisos imposibles.
- **Impacto:** Bloquea ventas y retiros superiores al stock.
- **Validación técnica sugerida:** Consultar y bloquear el stock dentro de la transacción; comparar cantidad positiva con existencia antes de descontar.
- **Funcionalidades relacionadas:** F17, F18, F19, F20.
- **Interfaces relacionadas:** P08, P11, P13, P14.
- **Entidades relacionadas:** Producto Venta DetalleVenta MovimientoInventario.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN02 · Combustible activo

- **Código:** RN02.
- **Nombre:** Combustible activo.
- **Enunciado formal:** Solo combustibles activos pueden utilizarse en nuevas ventas.
- **Condición:** `producto.estado == Activo`.
- **Caso de cumplimiento:** Vender Gasolina Regular con estado Activo.
- **Caso de violación:** Confirmar una venta de un producto con estado Inactivo.
- **Justificación:** Mantener el catálogo operativo y preservar el historial.
- **Impacto:** Excluye productos inactivos de nuevas ventas.
- **Validación técnica sugerida:** Verificar estado del producto en el servidor al confirmar la venta.
- **Funcionalidades relacionadas:** F09, F10, F11, F12, F20.
- **Interfaces relacionadas:** P06, P07, P08.
- **Entidades relacionadas:** Producto Venta DetalleVenta.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN03 · Conservación del historial

- **Código:** RN03.
- **Nombre:** Conservación del historial.
- **Enunciado formal:** Los registros con historial asociado no deben eliminarse físicamente; deben cambiar de estado.
- **Condición:** `registro.historial > 0 ⇒ acción = actualizar estado, nunca eliminar`.
- **Caso de cumplimiento:** Desactivar la categoría Gasolinas que ya tiene ventas.
- **Caso de violación:** Eliminar un producto con detalles de venta.
- **Justificación:** Conservar la trazabilidad de la operación.
- **Impacto:** Sustituye eliminación por activación o desactivación.
- **Validación técnica sugerida:** Mantener claves foráneas y rechazar eliminación de registros referenciados; actualizar estado.
- **Funcionalidades relacionadas:** F07, F08, F11, F12, F25, F31, F32.
- **Interfaces relacionadas:** P05, P06, P07, P17, P18, P19.
- **Entidades relacionadas:** Categoria Producto Empleado Usuario ConceptoMovimiento.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN04 · Compra y abastecimiento

- **Código:** RN04.
- **Nombre:** Compra y abastecimiento.
- **Enunciado formal:** Cada compra confirmada debe generar una entrada de inventario por cada línea recibida y un único egreso económico por el importe total.
- **Condición:** `compra.confirmada ⇒ (Σ entradas por línea = Σ litros comprados) ∧ (egresos vinculados = 1)`.
- **Caso de cumplimiento:** C001 de 300 L y S/ 1,350.00 genera MI001, MI002, MI003 y un único egreso MC001 de S/ 1,350.00.
- **Caso de violación:** Confirmar C001 con dos egresos de S/ 1,350.00, o con las entradas del inventario pero sin ningún egreso.
- **Justificación:** Que el inventario y la caja cuenten la misma historia del abastecimiento.
- **Impacto:** Relaciona la compra confirmada con sus movimientos físicos y con un solo movimiento económico.
- **Validación técnica sugerida:** Confirmación atómica de Compra, DetalleCompra, MovimientoInventario y MovimientoCaja; unicidad de id_compra en MovimientoCaja e idempotencia.
- **Funcionalidades relacionadas:** F13, F14, F15, F16, F19, F28.
- **Interfaces relacionadas:** P12, P14, P15, P20, P21.
- **Entidades relacionadas:** Compra DetalleCompra Producto MovimientoInventario MovimientoCaja ConceptoMovimiento.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN05 · Venta e ingreso económico

- **Código:** RN05.
- **Nombre:** Venta e ingreso económico.
- **Enunciado formal:** Cada venta confirmada debe generar un único ingreso económico asociado.
- **Condición:** `venta.confirmada ⇒ (ingresos vinculados = 1)`.
- **Caso de cumplimiento:** V001 por S/ 50.00 asociada solamente a MC003 por S/ 50.00.
- **Caso de violación:** Dos ingresos para V001, o una venta confirmada sin ingreso.
- **Justificación:** Evitar ingresos duplicados y diferencias entre ventas y caja.
- **Impacto:** Relaciona la venta confirmada con un solo movimiento de ingreso.
- **Validación técnica sugerida:** Confirmación atómica de Venta, DetalleVenta, stock, MovimientoInventario y MovimientoCaja; unicidad de id_venta no nulo e idempotencia.
- **Funcionalidades relacionadas:** F20, F21, F22, F28.
- **Interfaces relacionadas:** P08, P09, P10, P15.
- **Entidades relacionadas:** Venta DetalleVenta Producto MovimientoInventario MovimientoCaja ConceptoMovimiento.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN06 · Valores válidos

- **Código:** RN06.
- **Nombre:** Valores válidos.
- **Enunciado formal:** Cantidades, litros e importes deben ser numéricos mayores que cero y con el número de decimales permitido.
- **Condición:** `cantidad > 0 ∧ precio > 0 ∧ importe >= 0 ∧ decimales <= 2`.
- **Caso de cumplimiento:** Registrar 100 L a S/ 4.50 por litro (importe S/ 450.00).
- **Caso de violación:** Registrar una venta de 0 L, un precio negativo o un importe con tres decimales.
- **Justificación:** Impedir datos sin sentido en inventario y en caja.
- **Impacto:** Rechaza el alta o la edición de registros con valores no positivos o mal formados.
- **Validación técnica sugerida:** Validación con Bean Validation en el servidor y redondeo a dos decimales en el cliente y en el servidor.
- **Funcionalidades relacionadas:** F09, F11, F13, F16, F17, F20, F26, F27.
- **Interfaces relacionadas:** P07, P08, P12, P13, P16, P20.
- **Entidades relacionadas:** Producto Compra DetalleCompra Venta DetalleVenta MovimientoInventario MovimientoCaja ConceptoMovimiento.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## Cobertura de las reglas

| Regla | Funcionalidades | Interfaces | Entidades |
|---|---|---|---|
| RN01 | F17, F18, F19, F20 | P08, P11, P13, P14 | Producto, Venta, DetalleVenta, MovimientoInventario |
| RN02 | F09, F10, F11, F12, F20 | P06, P07, P08 | Producto, Venta, DetalleVenta |
| RN03 | F07, F08, F11, F12, F25, F31, F32 | P05, P06, P07, P17, P18, P19 | Categoria, Producto, Empleado, Usuario, ConceptoMovimiento |
| RN04 | F13, F14, F15, F16, F19, F28 | P12, P14, P15, P20, P21 | Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja |
| RN05 | F20, F21, F22, F28 | P08, P09, P10, P15 | Venta, DetalleVenta, Producto, MovimientoInventario, MovimientoCaja |
| RN06 | F09, F11, F13, F16, F17, F20, F26, F27 | P07, P08, P12, P13, P16, P20 | Producto, Compra, DetalleCompra, Venta, DetalleVenta, MovimientoInventario, MovimientoCaja, ConceptoMovimiento |

**Reglas sin funcionalidad: 0. Reglas sin interfaz: 0.**
