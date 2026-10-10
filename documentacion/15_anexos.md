# 5.15 · Anexos

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.15 Anexos** — «Material complementario que permite ampliar la comprensión del proyecto mismo.». El punto no fija cantidad, formato ni tipo de anexo. Por eso sólo se anexa material que **ya existe en el repositorio** y que efectivamente amplía la comprensión del proyecto; no se crea contenido nuevo para llenar anexos ni se inventa material que no se tiene.

**Criterio de anexo:** material complementario de consulta y verificación —diagramas, matriz, trazabilidad, evidencias de validación y capturas— distinto de la narrativa de los capítulos. Cada anexo se lista **por referencia** al archivo que ya forma parte de la entrega; su contenido no se duplica aquí.

---

## A. Anexos incluidos (material existente en el repositorio) — 5

| ID | Anexo | Archivo(s) | Complementa a | Estado |
|---|---|---|---|---|
| A-1 | Diagrama BPMN de los procesos núcleo y de soporte, con diccionario de notación | [09_bpmn.md](09_bpmn.md) y su versión navegable [bpmn.html](bpmn.html) | 5.6 · 5.10 | **Incluido** |
| A-2 | Matriz de cobertura Funcionalidad × Interfaz (38 × 30; 47 pares) | [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md) | 5.7 · 5.8 | **Incluido** |
| A-3 | Cadenas de trazabilidad modelo → regla → funcionalidad → interfaz → entidad → HTML | [07_trazabilidad.md](07_trazabilidad.md) | 5.6 · 5.8 | **Incluido** |
| A-4 | Evidencias de validación de la entrega (controles, resultados y método de la auditoría) | [00_auditoria.md](00_auditoria.md) | Todo el documento | **Incluido** |
| A-5 | Capturas de interfaces renderizadas (evidencia visual de la maqueta) | `anexos/` → 5 archivos PNG (ver §B) | 5.6 · 5.7 | **Incluido** |

## B. Detalle de A-5 · Capturas

Capturadas con Chrome headless (viewport 1440 × 1100) durante la inspección visual documentada en [00_auditoria.md](00_auditoria.md) §Método, el 01/10/2026 a las 17:04. Cada PNG fue verificado: firma correcta, dimensiones 1440 × 1100, 8 bits por canal.

| Archivo | Página mostrada | Tamaño |
|---|---|---:|
| [anexos/captura-dashboard.png](anexos/captura-dashboard.png) | `dashboard.html` — indicadores y series temporales | 78 KB |
| [anexos/captura-finanzas.png](anexos/captura-finanzas.png) | `finanzas.html` — flujo de caja y egresos | 84 KB |
| [anexos/captura-compras.png](anexos/captura-compras.png) | `compras.html` — registro de compras y proveedores | 73 KB |
| [anexos/captura-compra-detalle.png](anexos/captura-compra-detalle.png) | `compra-detalle.html` — detalle de partida de compra | 62 KB |
| [anexos/captura-bpmn.png](anexos/captura-bpmn.png) | `documentacion/bpmn.html` — BPMN de los procesos | 83 KB |

## C. Material que NO constituye anexo

| Material | Por qué no es anexo | Clasificación |
|---|---|---|
| Capítulos del informe (`01`–`05`, `08`, `10`–`14`) y `README.md` | Son el informe mismo, no material complementario | **no requerido** |
| `recurso/` (9 PDF del curso, incluido el Anexo 1; 2 copias duplicadas eliminadas en la ETAPA 9) | Material oficial de la asignatura; ya citado como bibliografía en [14_bibliografia.md](14_bibliografia.md) §B | **no requerido** |
| Scripts de validación (auditoría, render, diagramas) | Viven fuera del repositorio por decisión documentada en [00_auditoria.md](00_auditoria.md) §Decisiones; sus resultados están en el anexo A-4 | **no requerido** |
| `.vscode/`, `.gitignore` | Configuración técnica, no material del proyecto | **no requerido** |
| Diagramas, capturas o evidencias nuevas «para completar» anexos | No existen; fabricarlos sería inventar contenido. Ejemplo: las capturas de los patrones revisados en clase siguen sin material en el repositorio | **requiere evidencia** (pendiente (c) de [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md)) |

## D. Aclaración: «Anexos» del punto 5.15 ≠ anexos del documento oficial

El PDF usa la palabra «anexo» en dos sentidos distintos:

1. **Punto 5.15 (este documento):** material complementario propio del proyecto — sección A de aquí arriba. **Resuelto.**
2. **Anexo del documento oficial (lista de procesos):** el texto pide elegir «un proceso a desarrollar consignado en el Anexo 4», pero el PDF adjunta un **Anexo 1** con nueve sistemas (eventos, gimnasio, almacén, reclutamiento, trámite documentario, policlínico, CRM, call center y ventas por internet/delivery). **La gasolinera/estación de servicio no figura en esa lista** y la remisión a «Anexo 4» no coincide con el anexo entregado. → **Pendiente (b) · requiere evidencia** en [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md): confirmar con el docente el anexo o registro oficial que respalda este proceso. No se resuelve desde el repositorio ni se sustituye con material inventado.

---

**Autocontrol:** 5 anexos, todos verificables por archivo propio; 0 archivos de contenido creados para rellenar anexos (sólo este índice y las 5 capturas copiadas de la validación ya ejecutada); conteos oficiales intactos (38 F · 30 P · 5 RN · 12 entidades · diccionario 74 atributos · 47 pares en la matriz); JavaScript = 0; único anexo con pendiente humano es la aclaración de la §D (registro oficial del proceso).
