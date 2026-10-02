# 5.12 · Recomendaciones

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.12 Recomendaciones** — «Principales recomendaciones para quienes intenten desarrollar un proyecto similar para la misma oportunidad de mejora o en el mismo contexto. Deben ser tres recomendaciones como máximo».

Se entregan **3 recomendaciones** (el máximo oficial), cada una derivada de una de las tres [conclusiones](11_conclusiones.md). Ninguna introduce tecnologías fuera del alcance: se mantienen HTML5 + CSS3 + Bootstrap 5.3.3 CSS y la prohibición de JavaScript, backend y base de datos en esta etapa.

> **[PENDIENTE — decisión humana]**: validación de la redacción de estas recomendaciones por el equipo (clasificación oficial en [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md), ítem (e)). Hasta esa validación, este punto se considera **Entregado, no cerrado**.

---

## Recomendación 1 · Cuadre el modelo y los números del ejemplo antes de maquetar

**Deriva de:** Conclusión 1.

Defina primero la cadena de valor (categoría → combustible → compra → inventario en litros → venta → ingreso/egresos → tablero) y las reglas que la sostienen (RN04 y RN05 en particular), y exija que los datos ficticios cierren con fórmulas explícitas antes de escribir una sola página: `apertura + entradas − salidas = existencias` en litros y `apertura + ingresos − egresos = saldo` en soles, con un único corte de fecha y hora. Si una cifra del dashboard no se deduce de las tablas, el error es del modelo, no de la maqueta. Fijar la compra como el único momento en que nacen juntos el movimiento físico y el económico evita después discusiones de doble contabilidad.

**Acción concreta:** una hoja de conciliación como la de [01_modelo_negocio.md](01_modelo_negocio.md) §Datos y conciliación, revisada antes de cada exposición.

---

## Recomendación 2 · Declare los estados reales y planifique la implementación de las reglas junto con la especificación

**Deriva de:** Conclusión 2.

Distinga y comunique sin ambigüedad los estados **DEFINIDA**, **MAQUETADA** e **IMPLEMENTADA** de cada funcionalidad, y no presente la maqueta como sistema operativo: aquí nada autentica, guarda ni valida. Aproveche que las reglas ya están escritas con caso de cumplimiento y caso de violación (RN01–RN10 en [05_reglas_negocio.md](05_reglas_negocio.md)) para planificar la etapa de implementación con esas mismas pruebas: control de stock en transacción y concurrencia (RN01), productos inactivos (RN02), desactivar en lugar de eliminar (RN03), unicidad de egreso por compra (RN04), unicidad de ingreso por venta (RN05), valores positivos con dos decimales (RN06), asistencia propia del empleado autenticado (RN07), una asistencia por empleado y día (RN08), orden de las horas (RN09) y estado derivado de las horas (RN10).

**Acción concreta:** llevar a la exposición la lista 0/38 implementadas y el plan de pruebas de las diez reglas, en lugar de prometer funciones que la maqueta no ejecuta.

---

## Recomendación 3 · Mantenga trazabilidad con identificadores únicos y audítela de forma automática, sin añadir JavaScript

**Deriva de:** Conclusión 3.

Asigne códigos estables desde el inicio (funcionalidad `Fnn`, interfaz `Pnn`, regla `RNnn`, entidad con nombre fijo) y mantenga la cadena modelo → regla → funcionalidad → interfaz → entidad → HTML viva en la documentación. Después audítela con una verificación mecánica de los propios archivos: enlaces rotos, identificadores inexistentes, funcionalidades huérfanas, reglas sin interfaz y desbordamiento horizontal en varios viewports. Esas comprobaciones se hacen sobre texto y capturas del navegador: **no requieren** añadir JavaScript, APIs ni base de datos al entregable, de modo que el límite tecnológico del curso se conserva íntegro.
**Acción concreta:** repetir los controles de [00_auditoria.md](00_auditoria.md) después de cada cambio de documentación o de interfaz (0 enlaces rotos, 0 IDs inexistentes, 0 funcionalidades sin interfaz como condición de salida).

---

> **[Revisión humana]**: recomendaciones derivadas del alcance realmente desarrollado (maqueta estática + documentación). El equipo debe validar la redacción final; no se recomienda ninguna tecnología prohibida en esta etapa ni ninguna fuera del proyecto.
