# Auditoría de entrega · G1

## Alcance y fecha
Revisión final de la maqueta completa, iniciada el 10/09/2026 y actualizada el 02/10/2026 después de la corrección integral de modelo de negocio, reglas, funcionalidades, interfaces, entidades, matriz, trazabilidad, documentación y HTML/CSS. Esa corrección llevó el proyecto a **30 interfaces (P01–P30), 38 funcionalidades (F01–F38), 5 reglas (RN01–RN05) y 12 entidades**, con el módulo de **asistencia agregado** y con las finanzas rediseñadas para que los movimientos de caja nazcan de compras y ventas. Se trabajó sobre los requisitos entregados por el equipo: no se incorporó una aplicación anterior ni se copiaron plantillas externas. Los datos comerciales y del personal son ficticios.

Los resultados que siguen son mediciones de esta revisión: se indican los totales observados en cada control, no estimaciones.

## Inventario final
56 archivos de la entrega: 31 HTML en la raíz, 1 CSS (`css/estilos.css`), 17 Markdown (`README.md` + los 16 de `documentacion/`), 1 HTML auxiliar (`documentacion/bpmn.html`), 5 capturas PNG (`documentacion/anexos/`) y `.gitignore`.

Los 16 documentos de `documentacion/` son `00_auditoria.md`, `01_modelo_negocio.md`, `02_modelo_entidad_relacion.md`, `03_interfaces.md`, `04_funcionalidades.md`, `05_reglas_negocio.md`, `06_matriz_funcionalidades_interfaces.md`, `07_trazabilidad.md`, `08_puntos_1_al_5_8.md`, `09_bpmn.md`, `10_productos_y_entregables.md`, `11_conclusiones.md`, `12_recomendaciones.md`, `13_glosario.md`, `14_bibliografia.md` y `15_anexos.md`, además de la carpeta `anexos/` con las 5 capturas del anexo A-5.

Los 31 HTML de la raíz son las 30 interfaces más `publicidad.html`, que es la segunda presentación de P01. Las 9 interfaces nuevas de esta corrección son `categoria-form.html` (P22), `compra-form.html` (P23), `venta-form.html` (P24), `empleado-form.html` (P25), `usuario-form.html` (P26), `concepto-form.html` (P27), `mi-asistencia.html` (P28), `control-asistencia.html` (P29) y `movimiento-detalle.html` (P30).

La maqueta usa sólo `css/` y `documentacion/` además de los HTML de la raíz. No hay carpetas `js/`, `backend/`, `frontend/`, `node_modules/` ni archivos de dependencias. Los scripts temporales de construcción y comprobación viven fuera del repositorio y no forman parte de la entrega.

En el repositorio también están `recurso/` (11 PDF de la asignatura) y `.vscode/` (`launch.json`, `settings.json`), ajenos a la maqueta: se conservaron sin modificar. `recurso/` es el material oficial del curso citado en `14_bibliografia.md`. No se cuentan entre los 56 archivos ni en los controles de texto.

## Controles y resultados

| Control | Resultado | Evidencia / alcance |
|---|---|---|
| HTML de la raíz | 31/31 analizados · **0 errores** de estructura | Análisis de etiquetas, cierres e IDs con `HTMLParser`; no equivale a certificación W3C |
| `documentacion/bpmn.html` | 1/1 analizado · **0 errores** | Mismo analizador; enlaces con ruta relativa `../` |
| IDs duplicados | **0** | Recuento por documento de los 32 HTML |
| Interfaces oficiales | **30/30** | P01–P30 en los comentarios de cada archivo y en la documentación; `publicidad.html` reutiliza P01 |
| Funcionalidades oficiales | **38/38** | F01–F38 documentadas, representadas y presentes en la matriz |
| Reglas de negocio | **5/5** | RN01–RN05 con código, nombre, enunciado, condición, justificación, ámbito, funcionalidades, interfaces, entidades, caso de cumplimiento, caso de violación y validación en el sistema |
| Entidades | **12/12** · 74 atributos · 12 PK · 15 FK · 15 relaciones | Atributos, PK, FK, relaciones, cardinalidades y diagrama ER en Mermaid |
| Asistencia | **AGREGADA** | Entidad Asistencia (7 atributos) con relación Empleado 1:N; regla RN05; funcionalidades F35–F38; interfaces P28 y P29; enlace «Asistencia» en la barra lateral de las 27 páginas internas |
| Referencias locales HTML | **599 revisadas · 0 rotas** | `href`, `src` y `action`; navegación, CSS local y anclas; destinos e IDs existentes |
| Enlaces Markdown locales | **359 revisados · 0 rotos** | Destinos y anclas de `README.md` y de los 16 documentos de `documentacion/` (incluidos los acentos de los encabezados) |
| JavaScript | **0** | 0 archivos `.js`/`.ts`, 0 etiquetas `<script>`, 0 manejadores de evento inline ni `javascript:` |
| Bootstrap CSS | 32/32 HTML | CDN 5.3.3 + `css/estilos.css`; sin Bootstrap JS |
| Semántica | 32/32 HTML | `lang="es"`, `charset`, `viewport`, `<title>`, un `h1`, `main`, `header`, `nav` y `footer` por archivo |
| Formularios | 28 formularios · 121 controles · 121 etiquetas | `required` en todos los controles de dato, `min`/`max`/`step` en numéricos, `pattern` o longitudes en textos, tipos `date`, `time`, `datetime-local`, `email` y `password`; botones `type="submit"`; 0 incidencias |
| Tablas | **25/25** | Contenedor `table-responsive`, `caption`, `thead` y `th` en todas |
| Dashboard | KPIs y barras conciliados | Ventas S/ 370.00, compras S/ 1,350.00 y saldo S/ 3,430.00; serie diaria 10×5.00 + 20×6.00 + 50×4.00 = S/ 370.00 |
| Matriz | 38 filas × 30 columnas · **47 X** | **0 funcionalidades sin interfaz**; **30/30 interfaces con al menos una funcionalidad**; P01 y P04 son páginas públicas que sólo aportan F33 y F34 |
| Reglas ↔ negocio | 5/5 con funcionalidad e interfaz · **0 reglas huérfanas** | 24 funcionalidades con regla; 14 sin regla (F01–F06, F09, F10, F23, F24, F29, F30, F33, F34), anotado en `04_funcionalidades.md` |
| Coherencia de IDs | F 38/38, P 30/30, RN 5/5 | Conjuntos idénticos entre HTML y documentos · **0 discrepancias** |
| Responsive | **96 renderizados** | 32 páginas × 3 viewports: 1440×1100, 820×1180 y 390×1200 |
| Desbordamiento horizontal | **0/96** | Comparación de `scrollWidth` frente a `clientWidth` del documento en el DOM ya cargado |
| Páginas con `<script>` tras el render | **0/96** | Comprobado sobre el DOM renderizado, no sólo en el texto fuente |
| Datos | Conciliados | 6,700 + 300 − 80 = 6,920 L y 1,990 + 980 + 3,950 = 6,920 L · 10×5.00 + 20×6.00 + 50×4.00 = S/ 370.00 (80 L) · 100×4.50 + 100×5.40 + 100×3.60 = S/ 1,350.00 (300 L) · 4,410.00 + 370.00 − 1,350.00 = S/ 3,430.00 |
| Documento académico | Alcance declarado | Puntos 1–4 y 5.1–5.8 completos, con BPMN en `09_bpmn.md`/`bpmn.html`; 5.9 en `05_reglas_negocio.md`; 5.10–5.15 entregados en `10`–`15` (5.15 en `15_anexos.md`) con los pendientes clasificados en `08_puntos_1_al_5_8.md` |

## Método de revisión del navegador
La herramienta de navegador del agente no estaba conectada durante la medición. Se usó Chrome headless local y su protocolo de depuración desde una utilidad temporal de Python con el módulo `websockets` instalado en el equipo, sin librerías añadidas al proyecto. La utilidad fija el viewport con `Emulation.setDeviceMetricsOverride`, navega, espera `readyState`, lee `scrollWidth`/`clientWidth` y las etiquetas `<script>` del DOM ya cargado. El perfil de Chrome se recrea en cada ejecución para descartar caché. La comprobación no añadió JavaScript ni dependencias al proyecto.

## Interpretación de avance
- **DEFINIDA:** 30 interfaces, 38 funcionalidades, 5 reglas y 12 entidades.
- **MAQUETADA (V1):** 30 de 30 interfaces = **100 %** del alcance visual, medición correspondiente a la maqueta V1. Las 38 capacidades tienen representación visual o navegación correspondiente; la matriz confirma 47 pares funcionalidad × interfaz.
- **IMPLEMENTADA (V2):** **25 de 38 funcionalidades operan con lógica real de negocio** en la versión V2 con Spring Boot — `@Controller` → `Service` → `ServiceImpl` sobre datos en memoria (`List<T>`), integrada en `main` (commit `9084263`) — aplicando RN01–RN05 en el servidor, sin base de datos ni persistencia. En la medición correspondiente a la maqueta V1 ninguna funcionalidad tenía lógica real de negocio (todo en estado DEFINIDA + MAQUETADA). Las 13 restantes (F01–F03, F06–F08, F11, F12, F23–F25, F33 y F34) siguen pendientes de implementación.

## Decisiones y límites
- El módulo de **asistencia quedó agregado** en esta corrección: entidad Asistencia (id_asistencia, id_empleado, fecha, hora_entrada, hora_salida, estado y observación opcional) con relación Empleado 1:N, regla RN05, funcionalidades F35–F38 e interfaces P28 (mi asistencia, sin selector de empleado: el usuario lo resolverá Spring Security) y P29 (control del personal).
- Las finanzas **no incluyen formularios para crear movimientos de caja sueltos**: F26 y F27 sólo consultan ingresos y egresos que nacen de ventas y compras; los conceptos se reducen a CE01 (Ingreso) y CE02 (Egreso) y los movimientos existentes son MC001 (egreso de la compra) y MC003–MC005 (ingresos de las ventas).
- Se añadieron `compras.html` (P20) y `compra-detalle.html` (P21) porque la compra/abastecimiento era un hueco real del flujo: sin ellas RN03 y RN04 no tenían dónde representarse.
- Los formularios de alta y edición que antes vivían dentro de categorías, empleados y usuarios se movieron a pantallas propias (P22, P25 y P26) con botones «Abrir formulario» desde la consulta; las pantallas de consulta conservan su tabla y su botón de crear.
- P01 (Inicio/Publicidad) y P04 (Contacto) son contenido público: aportan F33 y F34 y por eso aparecen sin reglas asociadas en la matriz, por decisión explícita y no por olvido.
- Los formularios tienen botones visuales sin persistencia; los selectores no cargan datos ni recalculan importes. Los formularios son escenarios separados y no alteran el corte de datos del 10/09/2026 12:00.
- Cada venta del ejemplo tiene detalle, salida de inventario e ingreso económico coherentes. La compra `C001` (300 L, S/ 1,350.00) genera tres entradas (MI001–MI003) y **un único** egreso (MC001), según RN03 y RN04.
- Un empleado puede tener cero o un usuario; las tres cuentas de referencia ya están asignadas.
- `MovimientoInventario` conserva el modelo de atributos solicitado; la referencia a la venta se expresa en el motivo y queda anotada la evaluación futura de un vínculo estructurado.
- Bootstrap es la única dependencia externa y sólo CSS. La hoja propia mantiene estilos de respaldo sin conexión; no se incluyó una copia local de Bootstrap.
- El BPMN se dibuja con HTML/CSS propio (filas, carriles y conectores) y se documenta con Mermaid en `09_bpmn.md`: sin librerías de diagramas ni JavaScript; el proceso incluye el carril de asistencia.
- No se realizaron pruebas con tecnologías asistivas ni validación W3C externa. Las comprobaciones de accesibilidad se limitan a estructura, etiquetas, encabezados, foco visible y enlace para saltar al contenido.

## Pendientes
Quedan clasificados y marcados en `08_puntos_1_al_5_8.md` como *pendiente humano* o *requiere evidencia*: integrantes y coordinador del G1; el anexo o registro oficial que respalda el proceso de gasolinera (el Anexo 1 de la estructura no incluye una estación de servicio); capturas de los patrones revisados en clase y fechas reales del cronograma (5.10); lectura y cita de las dos obras bibliográficas obligatorias (5.10/5.14); validación del equipo sobre conclusiones y recomendaciones (5.11/5.12); volcado del glosario a la plantilla del informe (5.13).

Para la etapa posterior de la V2 (ya integrada en `main` y operando 25 de 38 funcionalidades con RN01–RN05 aplicadas en el servidor): persistencia con base de datos, seguridad y roles (incluida la sesión que resuelve el empleado en P28), transacciones de venta/stock/caja sobre base de datos, concurrencia, las 13 funcionalidades restantes y pruebas automatizadas de reglas. El detalle está en el README, sección 16.
