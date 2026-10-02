# Reglas de negocio RN01–RN10 · Punto 5.9

Las diez reglas están DEFINIDAS y representadas mediante textos y escenarios. Ninguna se aplica automáticamente en esta maqueta. Todas tienen funcionalidades e interfaces asociadas; ninguna queda sin representación.

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

**Cantidad:** el documento oficial exige un mínimo de 5 reglas; este proyecto entrega **10** (RN01–RN10). No se crearon reglas para alcanzar la cifra.

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
- **Interfaces relacionadas:** P08, P11, P13, P14, P24.
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
- **Interfaces relacionadas:** P06, P07, P08, P24.
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
- **Interfaces relacionadas:** P05, P06, P07, P19, P22, P25, P26, P27.
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
- **Funcionalidades relacionadas:** F13, F14, F15, F16, F19, F27, F28.
- **Interfaces relacionadas:** P12, P14, P15, P16, P20, P21, P23, P30.
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
- **Funcionalidades relacionadas:** F20, F21, F22, F26, F28.
- **Interfaces relacionadas:** P08, P09, P10, P15, P16, P24, P30.
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
- **Funcionalidades relacionadas:** F09, F11, F13, F16, F17, F20.
- **Interfaces relacionadas:** P07, P08, P12, P13, P20, P23, P24.
- **Entidades relacionadas:** Producto Compra DetalleCompra Venta DetalleVenta MovimientoInventario MovimientoCaja.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN07 · Asistencia propia

- **Código:** RN07.
- **Nombre:** Asistencia propia.
- **Enunciado formal:** Cada registro de asistencia pertenece a un solo empleado y, en «Mi asistencia», sólo se consulta la marcación del usuario autenticado.
- **Condición:** `asistencia.id_empleado == usuario.empleado.id_empleado`.
- **Caso de cumplimiento:** Ana Torres (atorres) consulta únicamente sus marcaciones.
- **Caso de violación:** Que un empleado vea o registre la asistencia de otro.
- **Justificación:** Cada empleado es responsable de su propia jornada; nadie debe consultar ni registrar la marcación ajena.
- **Impacto:** Restringe la consulta y el registro de asistencia al empleado autenticado en la interfaz de asistencia.
- **Validación técnica sugerida:** Spring Security resolverá el usuario autenticado → empleado; el servidor filtrará por id_empleado y rechazará cualquier otro. En la maqueta, P28 no ofrece selector de empleado.
- **Funcionalidades relacionadas:** F35, F36, F37.
- **Interfaces relacionadas:** P28.
- **Entidades relacionadas:** Empleado Asistencia.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN08 · Una asistencia por empleado y día

- **Código:** RN08.
- **Nombre:** Una asistencia por empleado y día.
- **Enunciado formal:** Un empleado sólo puede tener un registro de asistencia por fecha.
- **Condición:** `count(asistencia where id_empleado = e ∧ fecha = d) = 1`.
- **Caso de cumplimiento:** Una única fila de Ana Torres para el 10/09/2026.
- **Caso de violación:** Dos filas de Ana Torres para el 10/09/2026.
- **Justificación:** Evitar duplicados que confundan el control diario de asistencia.
- **Impacto:** Impide registrar dos veces la asistencia de un mismo empleado en una fecha.
- **Validación técnica sugerida:** Restricción única (id_empleado, fecha) en servidor; la maqueta sólo muestra el aviso.
- **Funcionalidades relacionadas:** F35, F36, F38.
- **Interfaces relacionadas:** P28, P29.
- **Entidades relacionadas:** Empleado Asistencia.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN09 · Orden de las horas

- **Código:** RN09.
- **Nombre:** Orden de las horas.
- **Enunciado formal:** La hora de salida de una asistencia debe ser posterior a su hora de entrada.
- **Condición:** `hora_salida > hora_entrada`.
- **Caso de cumplimiento:** Entrada 08:00 y salida 17:00.
- **Caso de violación:** Entrada 08:00 y salida 08:00 o 07:45.
- **Justificación:** Una salida anterior o igual a la entrada no describe una jornada real.
- **Impacto:** Rechaza marcaciones cuyas horas no siguen el orden de la jornada.
- **Validación técnica sugerida:** HTML5 `min` estático en P28 (`min="08:01"` con hora de entrada 08:00) más comparación en servidor al confirmar.
- **Funcionalidades relacionadas:** F36.
- **Interfaces relacionadas:** P28.
- **Entidades relacionadas:** Asistencia.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN10 · Estado derivado de las horas

- **Código:** RN10.
- **Nombre:** Estado derivado de las horas.
- **Enunciado formal:** El estado de una asistencia se calcula a partir de sus horas: Presente cuando hay entrada y salida dentro de la jornada; Falta cuando no hay marcación.
- **Condición:** `estado = f(hora_entrada, hora_salida)`.
- **Caso de cumplimiento:** 08:00–17:00 → Presente.
- **Caso de violación:** Mostrar Falta a una asistencia con horas completas.
- **Justificación:** El estado no se digita: se deriva de las horas para que el resumen diario sea confiable.
- **Impacto:** Determina Presente o Falta a partir de hora_entrada y hora_salida.
- **Validación técnica sugerida:** Calcular el estado en servidor; la maqueta sólo lo muestra.
- **Funcionalidades relacionadas:** F35, F36, F38.
- **Interfaces relacionadas:** P28, P29.
- **Entidades relacionadas:** Asistencia.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## Cobertura de las reglas

| Regla | Funcionalidades | Interfaces | Entidades |
|---|---|---|---|
| RN01 | F17, F18, F19, F20 | P08, P11, P13, P14, P24 | Producto, Venta, DetalleVenta, MovimientoInventario |
| RN02 | F09, F10, F11, F12, F20 | P06, P07, P08, P24 | Producto, Venta, DetalleVenta |
| RN03 | F07, F08, F11, F12, F25, F31, F32 | P05, P06, P07, P19, P22, P25, P26, P27 | Categoria, Producto, Empleado, Usuario, ConceptoMovimiento |
| RN04 | F13, F14, F15, F16, F19, F27, F28 | P12, P14, P15, P16, P20, P21, P23, P30 | Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja, ConceptoMovimiento |
| RN05 | F20, F21, F22, F26, F28 | P08, P09, P10, P15, P16, P24, P30 | Venta, DetalleVenta, Producto, MovimientoInventario, MovimientoCaja, ConceptoMovimiento |
| RN06 | F09, F11, F13, F16, F17, F20 | P07, P08, P12, P13, P20, P23, P24 | Producto, Compra, DetalleCompra, Venta, DetalleVenta, MovimientoInventario, MovimientoCaja |
| RN07 | F35, F36, F37 | P28 | Empleado, Asistencia |
| RN08 | F35, F36, F38 | P28, P29 | Empleado, Asistencia |
| RN09 | F36 | P28 | Asistencia |
| RN10 | F35, F36, F38 | P28, P29 | Asistencia |

**Reglas sin funcionalidad: 0. Reglas sin interfaz: 0.**
