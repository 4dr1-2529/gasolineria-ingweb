# Reglas de negocio RN01–RN06

Las seis reglas están DEFINIDAS y representadas mediante textos y escenarios. Ninguna se aplica automáticamente en esta maqueta.

## RN01 · Existencia suficiente

- **Código:** RN01.
- **Nombre:** Existencia suficiente.
- **Enunciado:** No se puede vender ni retirar más combustible que el stock disponible.
- **Justificación:** Evitar existencias negativas y compromisos imposibles.
- **Impacto:** Bloquea ventas y retiros superiores al stock.
- **Validación futura:** Consultar y bloquear el stock dentro de la transacción; comparar cantidad positiva con existencia antes de descontar.
- **Caso válido:** Vender 10 L con 100 L disponibles.
- **Caso inválido:** Retirar 101 L con 100 L disponibles.
- **Entidades relacionadas:** Producto Venta DetalleVenta MovimientoInventario.
- **Funcionalidades relacionadas:** F12, F14, F16.
- **Interfaces relacionadas:** P08, P11, P13.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN02 · Combustible activo

- **Código:** RN02.
- **Nombre:** Combustible activo.
- **Enunciado:** Solo combustibles activos pueden utilizarse en nuevas ventas.
- **Justificación:** Mantener el catálogo operativo y preservar el historial.
- **Impacto:** Excluye productos inactivos de nuevas ventas.
- **Validación futura:** Verificar estado del producto en el servidor al confirmar la venta.
- **Caso válido:** Vender Gasolina Regular activa.
- **Caso inválido:** Confirmar venta de un producto inactivo.
- **Entidades relacionadas:** Producto Venta DetalleVenta.
- **Funcionalidades relacionadas:** F08, F09, F10, F11, F16.
- **Interfaces relacionadas:** P06, P07, P08.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN03 · Conservación del historial

- **Código:** RN03.
- **Nombre:** Conservación del historial.
- **Enunciado:** Los registros con historial asociado no deben eliminarse físicamente; deben cambiar de estado.
- **Justificación:** Conservar la trazabilidad de la operación.
- **Impacto:** Sustituye eliminación por activación o desactivación.
- **Validación futura:** Mantener claves foráneas y rechazar eliminación de registros referenciados; actualizar estado.
- **Caso válido:** Desactivar un empleado con asistencias.
- **Caso inválido:** Eliminar un producto con detalles de venta.
- **Entidades relacionadas:** Categoria Producto Empleado Usuario ConceptoMovimiento.
- **Funcionalidades relacionadas:** F04, F05, F06, F07, F08, F09, F10, F11, F19, F20, F21, F25, F26, F27, F28.
- **Interfaces relacionadas:** P05, P06, P07, P17, P18, P19.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN04 · Venta e ingreso económico

- **Código:** RN04.
- **Nombre:** Venta e ingreso económico.
- **Enunciado:** Cada venta confirmada debe generar un único ingreso económico asociado.
- **Justificación:** Evitar ingresos duplicados y diferencias entre ventas y caja.
- **Impacto:** Relaciona la venta confirmada con un solo movimiento de ingreso.
- **Validación futura:** Confirmación atómica de Venta, DetalleVenta, stock, MovimientoInventario y MovimientoCaja; unicidad de id_venta no nulo e idempotencia.
- **Caso válido:** V001 por S/ 50.00 asociada solamente a MC001 por S/ 50.00.
- **Caso inválido:** Dos ingresos para V001, o venta confirmada sin ingreso.
- **Entidades relacionadas:** Venta DetalleVenta MovimientoCaja ConceptoMovimiento Producto MovimientoInventario.
- **Funcionalidades relacionadas:** F16, F18, F22, F23, F24.
- **Interfaces relacionadas:** P08, P10, P15, P16.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN05 · Una asistencia abierta

- **Código:** RN05.
- **Nombre:** Una asistencia abierta.
- **Enunciado:** Un empleado no puede marcar una nueva entrada si todavía tiene una asistencia sin salida.
- **Justificación:** Evitar turnos abiertos duplicados.
- **Impacto:** Impide nuevas entradas hasta cerrar la anterior.
- **Validación futura:** En una transacción, comprobar ausencia de asistencia abierta con bloqueo/concurrencia controlada.
- **Caso válido:** Ana registra entrada tras cerrar su jornada anterior.
- **Caso inválido:** Luis registra otra entrada cuando la de las 08:00 sigue abierta.
- **Entidades relacionadas:** Empleado Asistencia.
- **Funcionalidades relacionadas:** F29, F30.
- **Interfaces relacionadas:** P20.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.

## RN06 · Orden temporal

- **Código:** RN06.
- **Nombre:** Orden temporal.
- **Enunciado:** La hora de salida debe ser posterior a la hora de entrada.
- **Justificación:** Mantener una secuencia cronológica coherente.
- **Impacto:** Impide cerrar con hora igual o anterior.
- **Validación futura:** Comparar hora_salida > hora_entrada; para esta etapa se representan jornadas dentro del mismo día.
- **Caso válido:** Entrada 08:00 y salida 16:00.
- **Caso inválido:** Entrada 08:00 y salida 07:59 o 08:00.
- **Entidades relacionadas:** Empleado Asistencia.
- **Funcionalidades relacionadas:** F30.
- **Interfaces relacionadas:** P20.
- **Estado:** DEFINIDA + MAQUETADA mediante avisos; no IMPLEMENTADA.
