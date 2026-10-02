# Modelo de negocio · G1

## Contexto y alcance
Estación Nexo representa un sistema de venta de gasolina: categorías y tipos de combustible, compra o abastecimiento de combustible, inventario en litros, ventas, ingresos y egresos en soles, tablero de control, gestión de usuarios y empleados y control de asistencia del personal. El alcance actual es una maqueta académica estática; ningún registro se persiste. Los datos son ficticios y el corte es el 10/09/2026 a las 12:00, hora de referencia de Perú.

La cadena de valor central es única y sin módulos ajenos al negocio:

**Categoría de combustible → Combustible → Compra/abastecimiento → Inventario (L) → Venta → Ingreso/Egreso (S/) → Dashboard → Usuarios/Empleados**

## Roles conceptuales
- Administrador: categorías, productos, compras, conceptos económicos, empleados, usuarios, control de asistencia del personal y supervisión de la operación.
- Operador / Vendedor: consultas de disponibilidad, ventas y movimientos autorizados de inventario y caja; registra y consulta su propia asistencia.
No existe autorización real. Todos los HTML internos son accesibles directamente.

## Proceso de compra y abastecimiento
Inicio → Login → Compras → indicar proveedor, fecha, combustible, litros y precio de compra → confirmar compra → registrar una entrada de inventario por cada línea → aumentar existencias en litros → registrar un único egreso económico por el importe total.
En Spring Boot la confirmación será atómica y aplicará RN04 y RN06: crear Compra y sus DetalleCompra, crear un MovimientoInventario de entrada por cada línea, aumentar Producto.stock y crear un único MovimientoCaja de egreso. Ante un fallo se revertirá todo. La maqueta muestra la compra C001 ya confirmada.

## Proceso de venta y flujo completo
Inicio → Login → Dashboard → seleccionar combustible → consultar disponibilidad → representar venta → registrar detalle → descontar inventario → generar ingreso económico → consultar historial.
En Spring Boot, la confirmación comprobará RN01, RN02, RN05 y RN06 y se realizará en una transacción: crear Venta y DetalleVenta, descontar Producto.stock, registrar MovimientoInventario de salida y crear un único MovimientoCaja de ingreso (RN05). Ante un fallo, se revertirá toda la operación. La maqueta muestra el resultado de tres ventas ya confirmadas y un escenario nuevo sin confirmar.

## Inventario: movimiento físico
Entrada por compra → Seleccionar la compra → recibir litros → registrar entrada → aumentar existencias (RN04).
Retiro → Seleccionar producto, cantidad y motivo → comprobar stock → registrar salida → disminuir existencias.
Venta → Salida física asociada mediante el motivo de MovimientoInventario. El modelo base no incluye id_venta en esa entidad; el motivo documenta la referencia del ejemplo. Stock es el saldo físico en litros. Las cantidades se presentan positivas; el tipo determina suma o resta. El saldo inicial del día es el cierre físico del día anterior.

## Finanzas: movimiento económico
Venta confirmada → ingreso automático de igual importe y vinculado a id_venta (RN05).
Compra confirmada → egreso automático por el importe total de la compra y vinculado a id_compra (RN04).
Sólo existen dos conceptos económicos: **CE01 Venta de combustible (Ingreso)** y **CE02 Compra de combustible (Egreso)**. No hay altas de movimientos de caja por fuera de una venta o de una compra: P16 «Ingresos y egresos de caja» es una consulta de solo lectura que muestra los ingresos del día (F26: MC003, MC004 y MC005) y los egresos del día (F27: MC001).
Saldo demostrativo = saldo de apertura + ingresos − egresos. Un movimiento físico no determina por sí solo un movimiento de dinero, y un movimiento de dinero no siempre nace de un movimiento físico: la compra es el único caso en que ambos ocurren a la vez. No se modelan contabilidad tributaria, clientes ni proveedores como entidades; el proveedor es un atributo de texto de Compra.

## Usuarios y empleados
Empleado conserva la identidad y situación laboral. Usuario representa el acceso conceptual: cada empleado puede tener cero o una cuenta; cada cuenta pertenece a un empleado. El rol pertenece a Usuario. Se desactivan registros con historial en lugar de eliminarlos (RN03).

## Asistencia del personal
Relación Empleado 1:N Asistencia: un empleado tiene muchos registros de asistencia y cada registro pertenece a un solo empleado. La entidad Asistencia guarda fecha, hora de entrada, hora de salida, estado (Presente o Falta) y una observación opcional. Sólo puede haber un registro por empleado y por fecha (RN08), la hora de salida debe ser posterior a la de entrada (RN09) y el estado se calcula a partir de las horas (RN10).

P28 «Mi asistencia» es la interfaz del empleado autenticado: registrar su marcación (F35), consultar su historial (F36) y ver su resumen (F37); cada quien sólo ve y registra la suya (RN07), sin selector de empleado. P29 «Control de asistencia» es la consulta del administrador sobre la asistencia de todo el personal (F38). Todas las reglas están definidas y maquetadas con avisos; ninguna está implementada.

## Datos y conciliación del ejemplo
Saldo físico de apertura al 10/09/2026: 6,700 L.

| Combustible | Apertura | Entrada C001 | Salida por venta | Existencia | Precio / L |
|---|---:|---:|---:|---:|---:|
| Gasolina Regular | 1,800 L | 100 L | 10 L | 1,990 L | S/ 5.00 |
| Gasolina Premium | 900 L | 100 L | 20 L | 980 L | S/ 6.00 |
| Diésel | 3,900 L | 100 L | 50 L | 3,950 L | S/ 4.00 |
| **Total** | **6,700 L** | **300 L** | **80 L** | **6,920 L** | — |

Compra C001 (10/09/2026 07:00, Petroandes S.A., confirmada): Regular 100 L × 4.50 = S/ 450.00; Premium 100 L × 5.40 = S/ 540.00; Diésel 100 L × 3.60 = S/ 360.00. **Total: S/ 1,350.00.** Efectos: MI001, MI002 y MI003 (entradas) + MC001 (egreso).

V001: 10 × 5.00 = S/ 50.00 → MC003. V002: 20 × 6.00 = S/ 120.00 → MC004. V003: 50 × 4.00 = S/ 200.00 → MC005. Total de ventas: 3 ventas, 80 L, S/ 370.00.

Caja del 10/09/2026: apertura S/ 4,410.00; ingresos S/ 370.00 (MC003 S/ 50.00 + MC004 S/ 120.00 + MC005 S/ 200.00); egresos S/ 1,350.00 (MC001, compra C001); **saldo S/ 3,430.00**.

Asistencia del 10/09/2026: Ana Torres 08:00–17:00 Presente; Luis Rojas 08:00–17:00 Presente; Elena Díaz 08:15–17:00 Presente. En «Mi asistencia» de Ana Torres se muestran además 09/09/2026 07:58–17:00 Presente y 08/09/2026 sin marcación (Falta).

## Estados del avance
DEFINIDA: requisitos y comportamiento documentados. MAQUETADA: representación HTML/CSS navegable. IMPLEMENTADA: lógica operativa con persistencia y validación; pendiente. La navegación entre archivos está disponible, pero no equivale a implementar autenticación, compras o ventas.
