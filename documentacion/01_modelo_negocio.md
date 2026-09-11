# Modelo de negocio · G1

## Contexto y alcance
Estación Nexo representa un sistema de venta de gasolina, manejo de inventario, transacciones de ingreso y egreso y marcación de asistencia del personal. El alcance actual es una maqueta académica estática; ningún registro se persiste. Los datos son ficticios y el corte es el 10/09/2026 a las 12:00, hora de referencia de Perú.

## Roles conceptuales
- Administrador: categorías, productos, conceptos, empleados, usuarios y supervisión de la operación.
- Operador / Vendedor: ventas, consulta de disponibilidad y movimientos autorizados de combustible y caja.
- Empleado: marcación de asistencia.
No existe autorización real. Todos los HTML internos son accesibles directamente.

## Proceso de venta y flujo completo
Inicio → Login → Dashboard → seleccionar combustible → consultar disponibilidad → representar venta → registrar detalle → descontar inventario → generar ingreso económico → consultar historial.
En Spring Boot, la confirmación comprobará RN01 y RN02 y se realizará en una transacción: crear Venta y DetalleVenta, descontar Producto.stock, registrar MovimientoInventario de salida y crear un único MovimientoCaja de ingreso (RN04). Ante un fallo, se revertirá toda la operación. La maqueta muestra el resultado de tres ventas ya confirmadas y un escenario nuevo sin confirmar.

## Inventario: movimiento físico
Abastecimiento → indicar producto, litros, fecha, motivo y responsable → registrar entrada → aumentar existencias.
Retiro → seleccionar producto, cantidad y motivo → comprobar stock → registrar salida → disminuir existencias.
Venta → salida física asociada mediante el motivo de MovimientoInventario. El modelo base no incluye id_venta en esa entidad; el motivo documenta la referencia del ejemplo. Stock es el saldo físico en litros. Las cantidades se presentan positivas; el tipo determina suma o resta. Las cargas iniciales son saldos físicos, no compras del día.

## Finanzas: movimiento económico
Venta confirmada → ingreso automático de igual importe y vinculado a id_venta.
Otros ingresos → registro manual de ingreso con concepto de ingreso e id_venta nulo.
Gastos → registro manual de egreso con concepto de egreso e id_venta nulo.
Saldo demostrativo = saldo de apertura + ingresos − egresos. Un movimiento físico no determina por sí solo un movimiento de dinero. No se modelan contabilidad tributaria, compras, clientes ni proveedores como entidades adicionales.

## Empleados y usuarios
Empleado conserva la identidad y situación laboral. Usuario representa el acceso conceptual: cada empleado puede tener cero o una cuenta; cada cuenta pertenece a un empleado. Un empleado sin usuario también puede tener asistencia. El rol pertenece a Usuario. Se desactivan registros con historial en lugar de eliminarlos (RN03).

## Asistencia
Empleado → marcar entrada → registro abierto (hora_salida nula) → marcar salida posterior → registro cerrado → consultar historial. RN05 impide dos registros abiertos; RN06 exige salida posterior. El modelo base y los ejemplos cubren jornadas dentro de una misma fecha. Turnos que cruzan medianoche requieren una decisión de diseño posterior.

## Datos y conciliación del ejemplo
| Combustible | Apertura | Entradas | Salidas por venta | Existencia | Precio / L |
|---|---:|---:|---:|---:|---:|
| Gasolina Regular | 0 L | 2,000 L | 10 L | 1,990 L | S/ 5.00 |
| Gasolina Premium | 0 L | 1,000 L | 20 L | 980 L | S/ 6.00 |
| Diésel | 0 L | 4,000 L | 50 L | 3,950 L | S/ 4.00 |

V001: 10 × 5.00 = S/ 50.00 → MC001. V002: 20 × 6.00 = S/ 120.00 → MC002. V003: 50 × 4.00 = S/ 200.00 → MC003. Total: 3 ventas, 80 L, S/ 370.00. MC004 es mantenimiento por S/ 50.00. Apertura de caja S/ 0.00; saldo S/ 320.00. Dos asistencias abiertas y una cerrada equivalen a tres entradas y una salida: cuatro marcaciones, dos personas presentes.
Las gráficas 04–09 de septiembre son una serie ilustrativa fuera del historial del día; la del 10 coincide con el detalle. Los formularios representan propuestas y no alteran estas cifras.

## Estados del avance
DEFINIDA: requisitos y comportamiento documentados. MAQUETADA: representación HTML/CSS navegable. IMPLEMENTADA: lógica operativa con persistencia y validación; pendiente. La navegación entre archivos está disponible, pero no equivale a implementar autenticación, ventas o marcaciones.
