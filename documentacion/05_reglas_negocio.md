# Reglas de negocio RN01–RN05

Estación Nexo se rige por **cinco reglas de negocio**: RN01 a RN05. No existen reglas adicionales ni códigos superiores a RN05; el proyecto no define ninguna otra regla. Las cinco se aplican hoy en la versión Spring Boot (datos en memoria) y se representan además en la maqueta V1.

Cada regla se define con: código, nombre, enunciado, condición, justificación, ámbito, funcionalidades relacionadas, interfaces relacionadas, entidades relacionadas, caso de cumplimiento, caso de violación y validación en el sistema.

**Criterio para contar una regla.** Una regla es una restricción del negocio, no una pantalla ni un campo. Por eso F01 (iniciar sesión), F02 (cerrar sesión), F03 (consultar el tablero) y los formularios de alta sin restricción (F04, F09, F23, F29) no dependen de ninguna regla: son acceso, consulta o alta de un registro nuevo. Las reglas gobiernan existencias, conservación de registros, integridad de operaciones, caja y asistencia.

---

## RN01 · Control de existencias

- **Código:** RN01.
- **Nombre:** Control de existencias.
- **Enunciado:** El stock de un combustible nunca puede ser negativo. Las operaciones de salida o venta solo pueden realizarse cuando la cantidad solicitada no supera el stock disponible y las entradas válidas incrementan las existencias.
- **Condición:** `cantidad_solicitada <= stock_disponible` y `stock_final >= 0`.
- **Justificación:** El inventario en litros es el respaldo físico de cada venta. Un stock negativo declararía litros que la estación no tiene y rompería la conciliación entre inventario y caja.
- **Ámbito:** Módulos Inventario y Ventas; atributo `Producto.stock` y los movimientos que lo modifican.
- **Funcionalidades relacionadas:** F16, F17, F18, F19, F20.
- **Interfaces relacionadas:** P08, P11, P12, P13, P14, P24.
- **Entidades relacionadas:** Producto, MovimientoInventario, Venta, DetalleVenta.
- **Caso de cumplimiento:** Con 1,990 L de Gasolina Regular, registrar una venta de 10 L deja el stock en 1,980 L; registrar una entrada de 100 L lo deja en 2,090 L.
- **Caso de violación:** Solicitar 2,000 L con 1,990 L disponibles: la venta se rechaza y no cambia ningún dato (ni venta, ni detalle, ni stock, ni caja).
- **Cómo se valida en el sistema:** En la versión Spring Boot, `VentaServiceImpl` y `MovimientoInventarioServiceImpl` comparan la cantidad con el stock **antes** de crear cualquier registro y devuelven el mensaje «No hay stock suficiente…» sin descontar nada; las entradas suman litros una sola vez. La auditoría de flujo comprueba el inventario total: 6,920 L en la semilla, 7,020 L tras registrar la compra de 100 L y 7,010 L tras registrar la venta de 10 L.

---

## RN02 · Conservación de registros

- **Código:** RN02.
- **Nombre:** Conservación de registros.
- **Enunciado:** Los registros que tienen relación con operaciones del sistema no se eliminan físicamente. Cuando dejan de estar disponibles se cambia su estado a Inactivo, manteniendo la información histórica y sus relaciones.
- **Condición:** `registro.relacionado ⇒ acción = cambiar estado (Activo/Inactivo), nunca eliminar`.
- **Efecto de la desactivación:** el registro inactivo desaparece de las nuevas operaciones, permanece en las consultas históricas y no rompe las relaciones existentes.
- **Justificación:** Una categoría con ventas, un combustible con movimientos o un empleado con asistencia sostienen datos de los que dependen detalles, movimientos y caja. Borrarlos dejaría registros huérfanos y destruiría el historial.
- **Ámbito:** Catálogo y personal: Categoria, Producto, ConceptoMovimiento, Empleado y Usuario.
- **Funcionalidades relacionadas:** F07, F08, F11, F12, F20, F25, F31, F32.
- **Interfaces relacionadas:** P05, P06, P07, P08, P19, P22, P24, P25, P26, P27.
- **Entidades relacionadas:** Categoria, Producto, ConceptoMovimiento, Empleado, Usuario.
- **Caso de cumplimiento:** Desactivar Gasolina Premium (que ya tiene ventas): el producto queda Inactivo en el catálogo y sus ventas, detalles y movimientos siguen consultables.
- **Caso de violación:** Eliminar físicamente un combustible con detalles de venta: los detalles y los movimientos de inventario quedarían sin producto que los explique.
- **Cómo se valida en el sistema:** La versión Spring Boot no tiene ninguna operación de borrado: los controladores no exponen métodos de eliminación y ningún servicio elimina registros de sus listas. Los listados muestran el estado de cada registro; un producto Inactivo se rechaza en la venta («El producto seleccionado está inactivo…»); solo los empleados Activos registran asistencia; el estado de empleado y usuario se cambia en `/empleados/editar` y `/usuarios/editar`.

---

## RN03 · Integridad de operaciones

- **Código:** RN03.
- **Nombre:** Integridad de operaciones.
- **Enunciado:** Toda compra debe contener sus detalles y generar una entrada de inventario. Toda venta debe contener sus detalles y generar una salida de inventario. Cada efecto debe producirse una sola vez.
- **Condición:** `compra ⇒ (detalles ≥ 1 ∧ una entrada por línea)` y `venta ⇒ (detalles ≥ 1 ∧ una salida)`; ningún efecto duplicado.
- **Justificación:** Sin detalles no se sabe qué se compró ni qué se vendió. Sin el movimiento físico correspondiente, el inventario deja de coincidir con la operación; un efecto repetido duplicaría litros que no entraron ni salieron.
- **Ámbito:** Módulos Compras, Ventas e Inventario.
- **Funcionalidades relacionadas:** F13, F14, F15, F16, F19, F20, F21, F22.
- **Interfaces relacionadas:** P08, P09, P10, P12, P14, P20, P21, P23, P24.
- **Entidades relacionadas:** Compra, DetalleCompra, Venta, DetalleVenta, Producto, MovimientoInventario.
- **Caso de cumplimiento:** La compra C001 de 3 líneas crea 3 DetalleCompra y 3 entradas de inventario (MI001, MI002, MI003); la venta V001 crea su detalle y una única salida (MI004).
- **Caso de violación:** Confirmar una compra sin detalles, o con las entradas duplicadas (600 L en lugar de 300 L), o una venta con salida de inventario registrada dos veces.
- **Cómo se valida en el sistema:** `CompraServiceImpl` crea en un solo guardado la compra, sus detalles y la entrada de inventario de cada línea (`registrarEntradaPorCompra`); `VentaServiceImpl` crea en un solo guardado la venta, su detalle y la salida de inventario, y si alguna comprobación falla rechaza la operación sin crear nada ni tocar el stock. Los códigos de movimientos se generan secuenciales y sin repetirse (los flujos de auditoría verifican MC007, MC008, C002 y V004 sin duplicados).

---

## RN04 · Trazabilidad económica

- **Código:** RN04.
- **Nombre:** Trazabilidad económica.
- **Enunciado:** Toda compra genera un único egreso de caja y toda venta genera un único ingreso de caja. Cada movimiento económico debe quedar relacionado con la operación que lo originó.
- **Condición:** `compra ⇒ egresos vinculados(id_compra) = 1` y `venta ⇒ ingresos vinculados(id_venta) = 1`; todo movimiento nacido de una operación guarda su referencia.
- **Justificación:** La caja debe contar la misma historia que el inventario. Sin el vínculo con la operación de origen no se puede explicar un saldo ni auditar de dónde salió cada sol.
- **Ámbito:** Módulos Finanzas, Compras y Ventas; entidad MovimientoCaja con sus vínculos a Venta y Compra.
- **Funcionalidades relacionadas:** F13, F14, F15, F20, F21, F22, F26, F27, F28.
- **Interfaces relacionadas:** P08, P09, P10, P15, P16, P20, P21, P23, P24, P30.
- **Entidades relacionadas:** MovimientoCaja, ConceptoMovimiento, Compra, Venta.
- **Caso de cumplimiento:** La compra C001 (S/ 1,350.00) produce un solo egreso, MC001, vinculado a la compra; la venta V001 (S/ 50.00) produce un solo ingreso, MC003, vinculado a la venta.
- **Caso de violación:** Dos egresos de S/ 1,350.00 para la misma compra; una venta confirmada sin ingreso de caja; un movimiento de caja sin operación que lo explique.
- **Cómo se valida en el sistema:** El `MovimientoCaja` de ingreso o egreso se crea dentro del mismo guardado de la venta o de la compra, con `id_venta` o `id_compra` que identifica el origen. Finanzas es de solo consulta: `FinanzasController` sólo expone GET (`/finanzas/list`, `/ingresos`, `/egresos`, `/detalle`), por lo que no existen altas manuales de caja. El saldo se concilia como apertura + ingresos − egresos = 4,410.00 + 370.00 − 1,350.00 = **3,430.00** (suite de Finanzas: 158/158 comprobaciones).

---

## RN05 · Control de asistencia del personal

- **Código:** RN05.
- **Nombre:** Control de asistencia del personal.
- **Enunciado:** Solo empleados activos pueden registrar asistencia. Un empleado registra su propia jornada, no puede tener dos asistencias abiertas o dos registros para la misma jornada y la salida debe ser posterior a la entrada.
- **Condición:** `empleado.estado = Activo ∧ asistencia.id_empleado = empleado responsable ∧ asistencias abiertas ≤ 1 ∧ un registro por jornada ∧ hora_salida > hora_entrada`.
- **Justificación:** La asistencia registra quién trabajó y cuándo. Dos jornadas abiertas, dos registros del mismo día o salidas imposibles falsearían el resumen diario del personal.
- **Ámbito:** Módulo Asistencia; entidades Empleado, Usuario y Asistencia.
- **Funcionalidades relacionadas:** F35, F36, F37, F38.
- **Interfaces relacionadas:** P28, P29.
- **Entidades relacionadas:** Empleado, Usuario, Asistencia.
- **Caso de cumplimiento:** Ana Torres (Activo) registra la entrada a las 08:00 y la salida a las 17:00 del 10/09/2026: un solo registro de la jornada, con estado Presente.
- **Caso de violación:** Registrar una segunda entrada el mismo día; que un empleado Inactivo marque asistencia; registrar una salida a las 07:45 con entrada a las 08:00.
- **Cómo se valida en el sistema:** `AsistenciaServiceImpl` rechaza a los empleados no Activos («Sólo los empleados activos pueden registrar asistencia»), rechaza una segunda entrada de la misma jornada («Ya registraste la entrada de hoy…»), exige una asistencia abierta para registrar la salida, compara las horas («La hora de salida debe ser posterior a la hora de entrada») y calcula el estado (Presente/Falta) en servidor a partir de las horas. «Mi asistencia» (P28) no tiene selector de empleado: opera sobre el empleado actual de la demostración, y «Control de asistencia» (P29) filtra por empleado.

---

## Cobertura de las reglas

| Regla | Funcionalidades | Interfaces | Entidades |
|---|---|---|---|
| RN01 | F16, F17, F18, F19, F20 | P08, P11, P12, P13, P14, P24 | Producto, MovimientoInventario, Venta, DetalleVenta |
| RN02 | F07, F08, F11, F12, F20, F25, F31, F32 | P05, P06, P07, P08, P19, P22, P24, P25, P26, P27 | Categoria, Producto, ConceptoMovimiento, Empleado, Usuario |
| RN03 | F13, F14, F15, F16, F19, F20, F21, F22 | P08, P09, P10, P12, P14, P20, P21, P23, P24 | Compra, DetalleCompra, Venta, DetalleVenta, Producto, MovimientoInventario |
| RN04 | F13, F14, F15, F20, F21, F22, F26, F27, F28 | P08, P09, P10, P15, P16, P20, P21, P23, P24, P30 | MovimientoCaja, ConceptoMovimiento, Compra, Venta |
| RN05 | F35, F36, F37, F38 | P28, P29 | Empleado, Usuario, Asistencia |

**Reglas: 5. Reglas sin funcionalidad: 0. Reglas sin interfaz: 0.**
