# Auditoría de entrega · G1

## Alcance y fecha
Revisión final realizada el 10/09/2026 sobre la maqueta completa, tras la corrección integral de modelo de negocio, reglas, funcionalidades, interfaces, entidades, matriz, trazabilidad, documentación y HTML/CSS. El 01/10/2026 se revisó y actualizó este documento con el cierre de los puntos 5.9–5.15 (nuevos documentos `10`–`15` y sus cruces, más las capturas de `documentacion/anexos/`). Se trabajó sobre los requisitos entregados por el equipo: no se incorporó una aplicación anterior ni se copiaron plantillas externas. Los datos comerciales y del personal son ficticios.

Los resultados que siguen son mediciones de esta revisión: se indican los totales observados en cada control, no estimaciones.

## Inventario final
47 archivos de la entrega: 22 HTML en la raíz, 1 CSS (`css/estilos.css`), 17 Markdown (`README.md` + los 16 de `documentacion/`), 1 HTML auxiliar (`documentacion/bpmn.html`), 5 capturas PNG (`documentacion/anexos/`) y `.gitignore`.

Los 16 documentos de `documentacion/` son `00_auditoria.md`, `01_modelo_negocio.md`, `02_modelo_entidad_relacion.md`, `03_interfaces.md`, `04_funcionalidades.md`, `05_reglas_negocio.md`, `06_matriz_funcionalidades_interfaces.md`, `07_trazabilidad.md`, `08_puntos_1_al_5_8.md`, `09_bpmn.md`, `10_productos_y_entregables.md`, `11_conclusiones.md`, `12_recomendaciones.md`, `13_glosario.md`, `14_bibliografia.md` y `15_anexos.md`, además de la carpeta `anexos/` con las 5 capturas del anexo A-5.

La maqueta usa sólo `css/` y `documentacion/` además de los HTML de la raíz. No hay carpetas `js/`, `backend/`, `frontend/`, `node_modules/` ni archivos de dependencias. Los scripts temporales de construcción y comprobación viven fuera del repositorio y no forman parte de la entrega.

En el repositorio también están `recurso/` (11 PDF de la asignatura) y `.vscode/` (`launch.json`, `settings.json`), ajenos a la maqueta: se conservaron sin modificar. `recurso/` es el material oficial del curso citado en `14_bibliografia.md`. No se cuentan entre los 47 archivos ni en los controles de texto.

## Controles y resultados

| Control | Resultado | Evidencia / alcance |
|---|---|---|
| HTML de la raíz | 22/22 analizados · **0 errores** de estructura | Análisis de etiquetas, cierres e IDs con `HTMLParser`; no equivale a certificación W3C |
| `documentacion/bpmn.html` | 1/1 analizado · **0 errores** | Mismo analizador; enlaces con ruta relativa `../` |
| IDs duplicados | **0** | Recuento por documento de los 23 HTML |
| Interfaces oficiales | **21/21** | P01–P21 en comentarios de cada archivo y en la documentación; `publicidad.html` reutiliza P01 |
| Funcionalidades oficiales | **32/32** | F01–F32 documentadas, representadas y presentes en la matriz |
| Reglas de negocio | **6/6** | RN01–RN06 con nombre, enunciado formal, justificación, impacto, validación técnica sugerida y casos de cumplimiento y violación (6 puntos exigidos en 5.9) |
| Entidades | **11/11** | Atributos, PK, FK, relaciones, cardinalidades y diagrama ER en Mermaid; el módulo retirado no aparece en ningún HTML |
| Módulo retirado (asistencia) | **0 en los 23 HTML** | El recuento global es 2 y ambos están en este documento (la decisión y este renglón); ninguno en el ER, en las reglas ni en la matriz |
| Referencias locales HTML | **371 revisadas · 0 rotas** | `href`, `src` y `action`; navegación, CSS local y anclas; destinos existentes |
| Enlaces Markdown locales | **291 revisados · 0 rotos** | Destinos y anclas de `README.md` y de los 16 documentos de `documentacion/` |
| JavaScript | **0** | 0 archivos `.js`/`.ts`, 0 etiquetas `<script>`, 0 manejadores de evento inline ni `javascript:`, 0 usos de `localStorage` o `fetch(` |
| Bootstrap CSS | 22/22 HTML | CDN 5.3.3 + `css/estilos.css`; sin Bootstrap JS |
| Semántica | 22/22 HTML | `lang="es"`, `charset`, `viewport`, `<title>`, un `h1`, `main`, `header`, `nav` y `footer` por archivo |
| Formularios | 103 controles / 103 etiquetas | `input`, `select` y `textarea` asociados a `label`; 0 archivos con controles sin etiqueta |
| Tablas | **19/19** | Contenedor `table-responsive`, `caption`, `thead` y `th` en todas |
| Dashboard | 5 KPI + 5 series temporales | 5 indicadores de estado puntual y 5 `figure`; las 5 unidades incluyen el rango `04–10 sep. 2026`; 5 títulos de serie |
| Matriz | 34 pares funcionalidad × interfaz | **0 funcionalidades sin interfaz**; 19/21 interfaces con ≥1 funcionalidad; P01 y P04 son páginas públicas sin funciones (excepción documentada) |
| Reglas ↔ negocio | 6/6 con funcionalidad e interfaz · **0 reglas huérfanas** | 22 funcionalidades con regla; 10 sin regla (F01–F06, F23, F24, F29, F30), comportamiento previsto y anotado en `04_funcionalidades.md` |
| Coherencia de IDs | F 32/32, P 21/21, RN 6/6 | Conjuntos idénticos entre HTML y documentos · **0 discrepancias** |
| Responsive | **69 renderizados** | 23 páginas × 3 viewports: 1440×1100, 820×1180 y 390×1200, en Chrome headless |
| Desbordamiento horizontal | **0/69** | Comparación de `scrollWidth` frente a `clientWidth` del documento; las tablas pueden desplazarse dentro de su contenedor |
| Páginas con `<script>` tras el render | **0/69** | Comprobado en el DOM ya cargado, no sólo en el texto fuente |
| Inspección visual | Realizada | Capturas de `dashboard.html`, `compras.html` y de `documentacion/bpmn.html` en 7 posiciones de scroll (ambas figuras, pies de figura y tablas) |
| Acceso | Destinos comprobados | Índice → login; login → dashboard; cerrar sesión → login |
| Datos | Conciliados | 6,700 + 300 − 80 = 6,920 L y 1,990 + 980 + 3,950 = 6,920 L · 10×5.00 + 20×6.00 + 50×4.00 = S/ 370.00 (80 L) · 370.00 + 45.00 = S/ 415.00 · 1,350.00 + 50.00 = S/ 1,400.00 · 4,410.00 + 415.00 − 1,400.00 = S/ 3,425.00 |
| Documento académico | Alcance declarado | Puntos 1–4 y 5.1–5.8 completos, con BPMN en `09_bpmn.md`/`bpmn.html`; 5.9 en `05_reglas_negocio.md`; 5.10–5.15 entregados en `10`–`15` (5.15 en `15_anexos.md`) con los pendientes clasificados en `08_puntos_1_al_5_8.md` |

## Método de revisión del navegador
La herramienta de navegador del agente no estaba conectada. Se usó Chrome headless local y su protocolo de depuración desde una utilidad temporal de Python con un cliente WebSocket escrito a medida, sin librerías externas. La utilidad fija el viewport, navega, espera `readyState`, lee las métricas de layout del DOM y toma la captura. El perfil de Chrome se recrea en cada ejecución para descartar caché. La comprobación no añadió JavaScript ni dependencias al proyecto.

## Interpretación de avance
- **DEFINIDA:** 21 interfaces, 32 funcionalidades, 6 reglas y 11 entidades.
- **MAQUETADA:** 21 de 21 interfaces = **100 %** del alcance visual. Las 32 capacidades tienen representación visual o navegación correspondiente.
- **IMPLEMENTADA:** **0 de 32 funcionalidades con lógica real de negocio.** La navegación entre archivos existe, pero nada se autentica, guarda, calcula ni aplica reglas.

## Decisiones y límites
- Se eliminó el módulo de asistencia: no pertenece al núcleo del negocio (categorías → combustibles → compras → inventario → ventas → ingresos/egresos → dashboard → usuarios). El quinto indicador y la quinta serie del dashboard son hoy *Saldo de caja por día*.
- Se añadieron `compras.html` (P20) y `compra-detalle.html` (P21) porque la compra/abastecimiento era un hueco real del flujo: sin ellas RN04 no tenía dónde representarse.
- P01 (Inicio/Publicidad) y P04 (Contacto) son contenido público y no añaden funcionalidades al catálogo oficial: aparecen vacías en la matriz por decisión explícita, no por olvido.
- Los formularios de alta, edición, venta y contacto tienen botones visuales sin persistencia; los selectores no cargan datos ni recalculan importes. Los formularios nuevos son escenarios separados y no alteran el corte de datos.
- Cada venta del ejemplo tiene detalle, salida de inventario e ingreso económico coherentes. La compra `C001` genera tres entradas (MI001–MI003) y **un único** egreso (MC001), según RN04.
- Un empleado puede tener cero o un usuario; las tres cuentas de referencia ya están asignadas.
- `MovimientoInventario` conserva el modelo de atributos solicitado; la referencia a la venta se expresa en el motivo y queda anotada la evaluación futura de un vínculo estructurado.
- Bootstrap es la única dependencia externa y sólo CSS. La hoja propia mantiene estilos de respaldo sin conexión; no se incluyó una copia local de Bootstrap.
- El BPMN se dibuja con HTML/CSS propio (filas, carriles y conectores) y se documenta con Mermaid en `09_bpmn.md`: sin librerías de diagramas ni JavaScript.
- No se realizaron pruebas con tecnologías asistivas ni validación W3C externa. Las comprobaciones de accesibilidad se limitan a estructura, etiquetas, encabezados, foco visible y enlace para saltar al contenido.

## Pendientes
Quedan clasificados y marcados en `08_puntos_1_al_5_8.md` como *pendiente humano* o *requiere evidencia*: integrantes y coordinador del G1; el anexo o registro oficial que respalda el proceso de gasolinera (el Anexo 1 de la estructura no incluye una estación de servicio); capturas de los patrones revisados en clase y fechas reales del cronograma (5.10); lectura y cita de las dos obras bibliográficas obligatorias (5.10/5.14); validación del equipo sobre conclusiones y recomendaciones (5.11/5.12); volcado del glosario a la plantilla del informe (5.13).

Para Spring Boot: persistencia, validación de RN01–RN06, seguridad y roles, transacciones de venta/stock/caja, concurrencia, formularios conectados, mensajes de resultado y pruebas de reglas. El detalle está en el README, sección 16.
