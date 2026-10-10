# ETAPA 8 — Matriz de cumplimiento: Rúbrica de Avance 1 (Semana 8)

**Fuente:** `recurso/Rubrica%20Avance%201.pdf` (5 páginas, leída y transcrita en la ETAPA 8, 09/10/2026). Estructura oficial de la rúbrica: **Documentación 6 pts + Sistema maquetado 6 pts + Preguntas técnicas / exposición oral 8 pts = 20 pts**, con checklist del docente en las páginas 4–5.

Esta matriz contrasta **sólo** la rúbrica de Avance 1. La rúbrica del examen parcial está en [18_matriz_cumplimiento_examen_parcial.md](18_matriz_cumplimiento_examen_parcial.md) y **no se mezcla** con esta.

Columnas: requisito tal como lo pide la rúbrica → archivo de evidencia → estado real verificado en el repositorio → qué falta.

## 1. Documentación (6 pts) — puntos 5.1–5.8 y 5.10–5.14 (todo menos el 5.9)

| # | Requisito de la rúbrica | Archivo de evidencia | Estado real | Qué falta |
|---|---|---|---|---|
| 1.1 | Documentación de los puntos 5.1–5.8 y 5.10–5.14; el 5.9 queda excluido | [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) (secciones 5.1–5.8 y «Puntos 5.10 a 5.15 — Estado»), [10_productos_y_entregables.md](10_productos_y_entregables.md), [11_conclusiones.md](11_conclusiones.md), [12_recomendaciones.md](12_recomendaciones.md), [13_glosario.md](13_glosario.md), [14_bibliografia.md](14_bibliografia.md) | **Cumplido** — 5.1–5.8 documentados sin pendientes; 5.10–5.14 documentados con sus pendientes visibles y clasificados (nada oculto) | Pendientes declarados por el equipo: 5.10 (capturas de los patrones revisados en clase, requiere material de curso), 5.11–5.12 (validación de redacción), 5.13 (volcado a la plantilla A4/Arial 11), 5.14 (consulta de las dos obras obligatorias) |
| 1.2 | Diagnóstico con las 4 variables sustentadas | [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) §5.3 (Social, Económica, Tecnológica, Ecológica + síntesis) | **Cumplido** — las 4 variables presentes y sustentadas | — |
| 1.3 | 3 objetivos SMART | [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) §5.4 (OBJ 3.1, 3.2 y 3.3) | **Cumplido** — cada objetivo declara métrica, alcance y tiempo (cierre de la Semana 8) con criterio de autocumplimiento verificable | Detalle menor: el texto usa «medible, alcanzable, con tiempo» pero no la palabra literal «SMART» |
| 1.4 | BPMN con notación estándar estricta | [09_bpmn.md](09_bpmn.md), [bpmn.html](bpmn.html) (render del diagrama), `anexos/captura-bpmn.png` | **Cumplido** — procesos núcleo (venta) y de soporte (compra) con eventos, actividades, decisiones y actores | La validación estricta de notación corresponde al docente (revisión humana, no verificable en repo) |
| 1.5 | 5 reglas de negocio completas, con casos de prueba de cumplimiento y violación | [05_reglas_negocio.md](05_reglas_negocio.md) (RN01–RN05; cada una con «Caso de cumplimiento» y «Caso de violación») + [16_evidencias_etapas.md](16_evidencias_etapas.md) ETAPA 7 (pruebas H1–H13 ejercitan esos casos sobre el sistema real) | **Cumplido** — 5/5 reglas con ambos casos, además de verificación viva en la V2 | — |
| 1.6 | Matriz con 20+ funcionalidades mapeadas correctamente (bidimensional con interfaces) | [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md) (38 F × 30 interfaces), [04_funcionalidades.md](04_funcionalidades.md), [03_interfaces.md](03_interfaces.md) | **Cumplido** — 38 funcionalidades ≥ 20 exigidas; 804 enlaces cruzados con 0 rotos (ETAPA 1) | — |

## 2. Sistema maquetado (6 pts)

| # | Requisito de la rúbrica | Archivo de evidencia | Estado real | Qué falta |
|---|---|---|---|---|
| 2.1 | Maquetado completo e interactivo (HTML) que cubra 20+ funcionalidades, flujo intuitivo, coherente con BPMN y reglas | 31 HTML en la raíz (P01–P30; `publicidad.html` es la segunda presentación de P01), `css/estilos.css`, [07_trazabilidad.md](07_trazabilidad.md) | **Cumplido** — 31 pantallas navegables; cada archivo declara en su cabecera interfaz, funcionalidades, entidades y reglas | — |
| 2.2 | Presenta interfaz login | `login.html` (P29) | **Cumplido** | — |
| 2.3 | Uso de etiquetas semánticas | Verificación directa ETAPA 8: **31 de 31** HTML usan `<header>`, `<nav>`, `<main>`, `<section>`, `<footer>` o `<figure>` (100 %) | **Cumplido** | — |
| 2.4 | 5 métricas, todas series de tiempo con eje X | `dashboard.html` (P03): 5 bloques `<figure class="chart">` — importe vendido, litros vendidos, ingresos, egresos y saldo de caja — con eje X temporal 04–10 sep. 2026 («Serie ilustrativa») | **Cumplido** — 5/5 son series de tiempo, no categóricas | Las series son ilustrativas por decisión documentada (V1 sin backend); la V2 muestra cifras reales en `/finanzas/**` e `/inventario/**` |
| 2.5 | La interfaz considera la entidad Categoría en su diseño | `categorias.html` (P05), `categoria-form.html` (P06) | **Cumplido** | — |
| 2.6 | 2 páginas estáticas completas: publicidad y contacto | `publicidad.html` (P01b, 6,472 bytes) y `contacto.html` (P34, 5,164 bytes), ambas con contenido y navegación propios | **Cumplido** | El formulario de contacto no envía a ningún backend (V1 sin servidor, coherente con el alcance «sin APIs») |
| 2.7 | Redireccionamiento (ítem del checklist del docente) | Navegación por enlaces `<a href>` entre los 31 HTML; control de 804 enlaces → 0 rotos (ETAPA 1, [00_auditoria.md](00_auditoria.md)) | **Cumplido** | Redireccionamiento sin JavaScript, por diseño del alcance V1 («JavaScript no se usará», notas de clase) |
| 2.8 | Consumo de API (ítem del checklist del docente) | No existe | **No cumplido** | No hay API REST en la V1 ni en la V2 (declarado en [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) §4 y README §5). **No se inventa consumo**: la V2 es MVC con render en servidor |

## 3. Preguntas técnicas (8 pts) — comprensión de código y exposición oral

| # | Pregunta / área de la rúbrica | Base documental de la respuesta | Estado real | Qué falta |
|---|---|---|---|---|
| 3.1 | «Explique cómo funciona el redireccionamiento en su sistema Front-End» | Enlaces `<a href>` de la maqueta (menú lateral idéntico en 27 páginas) + rutas `@Controller`/`@GetMapping` de la V2; [00_auditoria.md](00_auditoria.md) | **Pendiente (oral)** — el material de respuesta existe en el repo | La respuesta oral del equipo; se ensaya con la explicación por módulo de la ETAPA 10 |
| 3.2 | «Estructura del proyecto front-end: carpetas y su propósito» | [00_auditoria.md](00_auditoria.md) (árbol: HTML en raíz, `css/`, `documentacion/`, `recurso/`; sin `js/`, `backend/`, `node_modules/`) | **Pendiente (oral)** — material listo | La respuesta oral del equipo |
| 3.3 | «Explique la lógica de implementación de [interfaz]» | Cada HTML declara `Interfaz: Pnn`, `Funcionalidades`, `Entidades`, `Reglas`; en la V2, JSP ↔ ruta ↔ Controller ↔ Service; detalle por módulo en la ETAPA 10 ([16_evidencias_etapas.md](16_evidencias_etapas.md)) | **Pendiente (oral)** — se entrega la explicación completa por módulo en la ETAPA 10 | Recorrer las 30 interfaces |
| 3.4 | «Indique UNA regla de negocio y desarrolle su implementación técnica» | RN01–RN05 en [05_reglas_negocio.md](05_reglas_negocio.md) + implementación real en `VentaServiceImpl`, `CompraServiceImpl`, `MovimientoInventarioServiceImpl`, `AsistenciaServiceImpl` con pruebas H1–H13 | **Pendiente (oral)** — material completo (regla → código → prueba) | La respuesta oral del equipo |
| — | «Código estructurado» (ítem del checklist) | V2 Spring Boot: `controller/` (11), `service/` (9 interfaces + 9 impls + `NexoUserDetailsService`), `model/` (12 entidades), 34 JSP | **Cumplido** | — |

## Resumen de la rúbrica Avance 1

| Sección | Puntos | Estado del contraste |
|---|---|---|
| 1. Documentación | 6 | 6/6 requisitos con evidencia en el repo; los pendientes de 5.10–5.14 están declarados, no ocultos |
| 2. Sistema maquetado | 6 | 7/8 requisitos cumplidos; 1 no cumplido honesto: «consumo de API» (no existe y no se inventa) |
| 3. Preguntas técnicas / oral | 8 | Material de respuesta completo en el repo; la puntuación depende de la exposición, que no es verificable en el repositorio |

*Verificado: ETAPA 8 (09/10/2026) contra `recurso/Rubrica%20Avance%201.pdf`. Se corrigió la ruta del PDF (el nombre real lleva `%20`).*
