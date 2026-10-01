# 5.11 · Conclusiones

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.11 Conclusiones** — «Principales hallazgos y conclusiones de los alumnos con relación a la pertinencia y/o impacto de su proyecto sobre la oportunidad de mejora en el contexto elegido. Deben ser tres conclusiones como máximo».

Se entregan **3 conclusiones** (el máximo oficial). Se apoyan únicamente en evidencia existente del repositorio: datos del corte del 10/09/2026, documentación 5.1–5.8, BPMN y resultados de la auditoría. **No se afirma ningún resultado de implementación**, porque la etapa Spring Boot no se ha realizado (0 de 32 funcionalidades con lógica real).

---

## Conclusión 1 · El modelo resuelve a nivel de diseño la oportunidad detectada: una sola historia que debe cuadrar en litros y en soles

**Hallazgo.** La oportunidad del diagnóstico (5.3) es que una estación de servicio lleva dos contabilidades que deben cuadrar —litros del tanque y dinero de la caja— en hojas independientes. El modelo propuesto las ata con una única traza de negocio, y con los datos ficticios del corte las dos cuentas cierran aritméticamente:

- **Inventario:** 6,700 L de apertura + 300 L de la compra `C001` − 80 L vendidos = **6,920 L**, que es exactamente la suma de existencias por producto (1,990 + 980 + 3,950).
- **Caja:** S/ 4,410.00 de apertura + S/ 415.00 de ingresos − S/ 1,400.00 de egresos = **S/ 3,425.00**, el saldo del dashboard.

**Pertinencia/impacto.** La compra es el único punto donde nacen a la vez un movimiento físico y uno económico (RN04: tres entradas `MI001`–`MI003` y **un** egreso `MC001`), y cada venta genera exactamente un ingreso (RN05: `MC003`–`MC005`). Esas igualdades son precisamente las que se rompen cuando litros y soles se registran aparte: el modelo propuesto las haría visibles como diferencia de inventario o diferencia de caja en el mismo período, no al cierre del mes.

**Evidencia:** [01_modelo_negocio.md](01_modelo_negocio.md) §Datos y conciliación; [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md); `dashboard.html` y `finanzas.html`.

---

## Conclusión 2 · La documentación y la maqueta estática definen el problema, pero el impacto real depende de la etapa de implementación

**Hallazgo.** El avance entregado define 32 funcionalidades, 21 interfaces, 6 reglas con casos de cumplimiento y de violación y 11 entidades, con las 21 interfaces maquetadas en HTML5 + CSS3 + Bootstrap. Sin embargo, la medición propia marca **0 de 32 funcionalidades con lógica de negocio implementada**: nada autentica, guarda, calcula ni valida reglas (estados DEFINIDA y MAQUETADA, no IMPLEMENTADA). La maqueta ya demostró utilidad correctiva antes de exponer: detectó y corrigió un módulo fuera del alcance del negocio y un hueco real del flujo (compra/abastecimiento sin interfaces P20/P21).

**Pertinencia/impacto.** El impacto sobre la oportunidad de mejora está **probado a nivel de modelo y de especificación**, no en operación: la detección oportuna de diferencia de existencias y de caja sólo ocurrirá cuando Spring Boot implemente las reglas críticas —RN01 (control de stock en transacción y concurrencia), RN04 y RN05 (unicidad de egreso por compra y de ingreso por venta)—. Hasta entonces, el valor real del avance es haber fijado exactamente qué hay que implementar y cómo se probará.

**Evidencia:** [04_funcionalidades.md](04_funcionalidades.md), [05_reglas_negocio.md](05_reglas_negocio.md), [00_auditoria.md](00_auditoria.md) §Interpretación de avance, README §13 y §16.

---

## Conclusión 3 · La trazabilidad con identificadores únicos hizo verificable la calidad del entregable en un proyecto académico

**Hallazgo.** Mantener la cadena **modelo → regla → funcionalidad → interfaz → entidad → HTML** con identificadores fijos (RN01–RN06, F01–F32, P01–P21, 11 entidades) permitió auditar el proyecto de forma mecánica y con resultados medibles: **0** funcionalidades sin interfaz, **0** reglas sin funcionalidad ni interfaz, **34** pares F×P en la matriz, **371** referencias locales HTML y **291** enlaces Markdown con **0** rotos, **0** identificadores inexistentes entre HTML y documentación, y **69** renderizaciones en tres viewports con **0** desbordamientos horizontales y **0** páginas con `<script>`.

**Pertinencia/impacto.** Para proyectos del mismo contexto, la trazabilidad no exige herramientas costosas ni tecnología prohibida: bastan identificadores disciplinados y una auditoría de texto sobre los propios archivos. Es el mecanismo que impulsa la desconexión clásica entre documento y prototipo (la maqueta promete lo que el documento no define, o viceversa) y prepara la etapa posterior, porque cada regla ya sabe en qué funcionalidad e interfaz deberá validarse.

**Evidencia:** [07_trazabilidad.md](07_trazabilidad.md) §Contadores, [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md), [00_auditoria.md](00_auditoria.md) §Controles y resultados.

---

> **[Revisión humana]**: las conclusiones redactan lo efectivamente desarrollado y medido. El equipo debe validar la redacción final y, si procede, añadir la valoración personal del equipo tras la exposición; no se agregó una cuarta conclusión porque el documento oficial fija el máximo en tres.
