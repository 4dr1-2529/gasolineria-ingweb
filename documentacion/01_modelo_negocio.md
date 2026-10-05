# Modelo de negocio · Estación Nexo

## Principio general

Estación Nexo es un sistema web para la gestión de una estación de servicio. Gestiona categorías, combustibles, compras, ventas, inventario, movimientos de caja, empleados, usuarios y asistencia.

La versión actual de desarrollo se construye con Spring Boot 3.5.0, Java 17, patrón MVC (Controller → Service → ServiceImpl), clases Java, vistas JSP/JSTL y datos en memoria (listas `List<T>`). La aplicación no consume base de datos todavía: no hay esquema persistente, ni capa de Repositorio, ni mapeo ORM. Tampoco hay autenticación real, Spring Security ni interfaces REST: las vistas se consultan por rutas HTTP internas y los registros viven mientras la aplicación está en ejecución.

La versión V1 (31 archivos HTML + CSS) es la maqueta navegable de la que nacen las 30 interfaces P01–P30. La versión V2 replica esos módulos con lógica real sobre datos en memoria.

## Objetivos

**Objetivo general**

Definir y desarrollar Estación Nexo como un sistema web para gestionar las operaciones principales de una estación de servicio, integrando el catálogo de combustibles, compras, ventas, control de inventario, movimientos económicos, administración de empleados y usuarios, y control de asistencia.

**Objetivos específicos**

1. Gestionar las categorías y combustibles de la estación, manteniendo su información, estado y relación dentro del catálogo.
2. Gestionar empleados y usuarios asociados, manteniendo la información necesaria para identificar a los responsables de las operaciones del sistema.
3. Registrar y consultar compras y ventas de combustibles, relacionando cada operación con sus detalles y con el producto correspondiente.
4. Controlar las existencias de combustible mediante entradas y salidas de inventario, evitando que el stock llegue a valores negativos.
5. Controlar los movimientos económicos originados por compras y ventas y mantener la trazabilidad de ingresos, egresos y saldo de caja.
6. Controlar el registro de asistencia del personal, permitiendo registrar la entrada, salida e historial de cada empleado de acuerdo con las reglas del sistema.

## Narrativa del negocio

**CATÁLOGO — Categoria → Producto/Combustible.** Las categorías agrupan los combustibles (Gasolinas: Regular y Premium; Diésel: Diésel). Cada producto guarda su unidad de medida (litro), su precio por litro, su stock y su estado (Activo/Inactivo).

**ABASTECIMIENTO — Compra → DetalleCompra → Entrada de inventario → Egreso.** Una compra a proveedor (el proveedor es un texto de la compra, no una entidad) contiene una o más líneas con producto, litros y precio de compra. Al confirmarse crea sus detalles, suma una entrada de inventario por línea y produce un único egreso de caja por el importe total.

**OPERACIÓN — Venta → DetalleVenta → Salida de inventario → Ingreso.** Una venta contiene sus líneas con producto y litros al precio vigente. Al confirmarse descuenta el stock, registra una salida de inventario y produce un único ingreso de caja por el total cobrado. Solo puede venderse lo que existe y solo productos activos.

**CONTROL — Inventario → existencias → movimientos.** El inventario expresa las existencias en litros por producto y el libro de entradas y salidas. Las entradas manuales suman; las salidas manuales restan comprobando stock. Las existencias son el saldo físico que respalda cada venta.

**PERSONAL — Empleado → Usuario → Asistencia.** Empleado conserva la identidad y el cargo; Usuario es la cuenta de acceso que pertenece a un empleado (cada empleado tiene como máximo una cuenta); Asistencia guarda la jornada de cada empleado: fecha, entrada, salida, estado (Presente/Falta) y observación opcional.

**FINANZAS — Ingresos + Egresos → Saldo.** Los movimientos de caja nacen únicamente de operaciones: los ingresos de las ventas y los egresos de las compras. No hay altas manuales de caja. El saldo del día es apertura + ingresos − egresos, y los conceptos económicos clasifican cada movimiento (CE01 Venta de combustible — Ingreso; CE02 Compra de combustible — Egreso).

## Roles

- **Administrador:** categorías, combustibles, compras, conceptos, empleados, usuarios, control de asistencia y supervisión de la operación.
- **Operador / Vendedor:** consultas de existencias, ventas, salidas de inventario y su propia asistencia.

En la versión actual no hay autenticación: los roles describen quién realiza cada acción en la operación, no permisos verificados por sesión.

## Baja lógica

La eliminación física no existe en Estación Nexo. Los registros se desactivan:

> Un combustible que ya tiene ventas registradas no se elimina físicamente. Se marca como Inactivo para impedir nuevas operaciones, pero sus ventas e inventario histórico permanecen disponibles.

**DESACTIVAR** significa, en el sistema:

- **desaparece de nuevas operaciones:** un producto Inactivo no puede venderse (la venta lo rechaza) y un empleado Inactivo no puede registrar asistencia;
- **permanece en consultas históricas:** los listados siguen mostrando el registro con su estado Inactivo y sus detalles, movimientos y caja conservan la referencia;
- **no rompe relaciones existentes:** los detalles de venta, los movimientos de inventario y los movimientos de caja que ya apuntan al registro siguen resolviendo correctamente.

Aplica a categorías, combustibles, conceptos económicos, empleados y usuarios: las cinco entidades con estado y con historial. En la versión actual ningún servicio expone operaciones de borrado.

## Roles de la operación

La cadena de valor central, sin módulos ajenos al negocio, es:

**Categoría → Combustible → Compra/abastecimiento → Inventario (L) → Venta → Ingreso/Egreso (S/) → Caja → Personal**

## Datos del corte

Datos de demostración al 10/09/2026, hora de referencia de Perú. Saldo físico de apertura: 6,700 L.

| Combustible | Apertura | Entrada C001 | Salida por venta | Existencia | Precio / L |
|---|---:|---:|---:|---:|---:|
| Gasolina Regular | 1,800 L | 100 L | 10 L | 1,990 L | S/ 5.00 |
| Gasolina Premium | 900 L | 100 L | 20 L | 980 L | S/ 6.00 |
| Diésel | 3,900 L | 100 L | 50 L | 3,950 L | S/ 4.00 |
| **Total** | **6,700 L** | **300 L** | **80 L** | **6,920 L** | — |

Compra C001 (10/09/2026 07:00, Petroandes S.A., confirmada): Regular 100 L × 4.50 = S/ 450.00; Premium 100 L × 5.40 = S/ 540.00; Diésel 100 L × 3.60 = S/ 360.00. **Total: S/ 1,350.00.** Efectos: MI001, MI002 y MI003 (entradas) + MC001 (egreso).

Ventas del día: V001 (10 L × 5.00 = S/ 50.00 → MC003), V002 (20 L × 6.00 = S/ 120.00 → MC004), V003 (50 L × 4.00 = S/ 200.00 → MC005). Total: 3 ventas, 80 L, S/ 370.00.

Caja del 10/09/2026: apertura S/ 4,410.00; ingresos S/ 370.00; egresos S/ 1,350.00 (MC001, compra C001); **saldo S/ 3,430.00**.

Asistencia del 10/09/2026: Ana Torres 08:00–17:00 Presente; Luis Rojas 08:00–17:00 Presente; Elena Díaz 08:15–17:00 Presente. En «Mi asistencia» de Ana Torres se muestran además 09/09/2026 07:58–17:00 Presente y 08/09/2026 sin marcación (Falta).

## Alcance actual

La versión V2 en desarrollo opera los nueve módulos internos con datos en memoria:

- **Categorías, Combustibles, Empleados, Usuarios:** alta y consulta con persistencia mientras la aplicación corre; empleado y usuario además se editan, incluido su estado.
- **Compras:** registro con proveedor, fecha y líneas; crea detalles, entradas de inventario y egreso de caja en un solo guardado.
- **Ventas:** registro con producto, cantidad y operador; comprueba producto activo y stock, y crea venta, detalle, salida de inventario e ingreso de caja en un solo guardado.
- **Inventario:** existencias por producto, entradas y salidas manuales con comprobación de stock, y libro de movimientos.
- **Finanzas:** consulta de ingresos, egresos, detalle de movimientos y saldo conciliado; sin altas manuales.
- **Asistencia:** entrada y salida del empleado actual con validación de jornada, más historial, resumen y control del personal.

Quedan fuera de la versión V2 actual las páginas públicas (portada y contacto), el acceso con credenciales, el tablero y la gestión de conceptos económicos; esas capacidades existen sólo en la maqueta V1.
