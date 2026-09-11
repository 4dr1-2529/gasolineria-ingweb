# Auditoría de entrega · G1

## Alcance y fecha
Revisión realizada el 10/09/2026 sobre una carpeta inicialmente vacía. Se construyó una maqueta nueva con los requisitos proporcionados, sin incorporar una aplicación anterior ni consultar plantillas externas. Los datos comerciales y del personal son ficticios.

## Inventario final
33 archivos generados de entrega: 21 HTML, 1 CSS, 9 documentos Markdown en documentacion/, README.md y .gitignore. La maqueta utiliza solo las carpetas css/ y documentacion/. Los scripts temporales de construcción y comprobación no forman parte de la entrega. No se crearon carpetas js, backend, frontend, node_modules ni archivos de dependencias.

En el control final también aparecieron recurso/ (11 PDF) y .vscode/launch.json, ajenos a los archivos generados. Se conservaron sin modificar y no se usaron como fuentes. No se cuentan entre los 33 archivos de la maqueta ni se incluyen en el control UTF-8, que corresponde a archivos de texto de la entrega.

## Controles y resultados
| Control | Resultado | Evidencia / alcance |
|---|---|---|
| HTML revisados | 21/21 | Análisis de etiquetas, cierres, IDs y estructura mediante HTMLParser; no equivale a certificación W3C |
| Interfaces oficiales | 20/20 | P01–P20 en comentarios y documentación; publicidad.html reutiliza P01 |
| Funcionalidades oficiales | 30/30 | F01–F30 documentadas, representadas y presentes en la matriz |
| Reglas | 6/6 | RN01–RN06 con enunciado, justificación, impacto, validación futura y ambos casos |
| Entidades | 10/10 | Atributos base, PK, FK, relaciones, cardinalidades y Mermaid ER |
| Referencias locales HTML | 338/338 válidas | href, src y action; incluye navegación, CSS local y anclas; destinos existentes |
| Enlaces Markdown | Verificados | Destinos locales de README y documentos existentes |
| Bootstrap CSS | 21/21 HTML | CDN 5.3.3; contenido de la hoja confirmado cargado en Chrome |
| JavaScript | Cero | Sin script, manejadores inline, URLs javascript:, Bootstrap JS o archivos JS/TS |
| UTF-8 | Verificado | Decodificación estricta de todos los HTML, CSS y Markdown; meta charset en 21 HTML |
| Caracteres dañados | Ninguno detectado | Búsqueda de carácter de reemplazo, marcas típicas de mojibake y interrogaciones entre letras |
| Formularios | 101 controles etiquetados | input, select y textarea asociados a label por id/for; IDs únicos por documento |
| Tablas | 15/15 responsive | Contenedor table-responsive, caption y encabezados con scope |
| Semántica | Verificada | lang=es, viewport, título, un h1 y un main, header, nav y footer por archivo |
| Dashboard | 5 KPI + 5 gráficos | Gráficos de barras HTML/CSS con valores textuales, sin bibliotecas de gráficos |
| Responsive | 63 renderizados | 21 páginas × 3 viewports: 1440×1100, 820×1180, 390×1200 en Chrome headless |
| Desbordamiento de página | 0/63 | Comparación del ancho del contenido con el viewport mediante protocolo del navegador; tablas pueden desplazarse dentro de su contenedor |
| Inspección visual | Realizada | Dashboard de escritorio, portada de tablet, portada y formulario de venta en móvil |
| Acceso | Destinos comprobados | Inicio → login; formulario login → dashboard; cerrar sesión → login |
| Datos | Conciliados | 7,000 − 80 = 6,920 L; ventas S/ 370.00; caja 370 − 50 = S/ 320.00 |
| Matriz | 30 × 20 | X marca relación directa; P01/P04 no añaden funciones; cierre de sesión es transversal |
| Trazabilidad | Completa | Negocio → entidad → funcionalidad → regla → interfaz → HTML |
| Documento académico | Completo en estructura | Puntos 1–4 y 5.1–5.8, con marcadores de integrantes y evidencias pendientes |

## Método de revisión del navegador
La herramienta agent-browser no estaba instalada. Se usó Chrome headless local y su protocolo de depuración desde una utilidad temporal de Python. La primera captura móvil por tamaño de ventana fue recortada por el viewport del navegador; se repitió con emulación explícita de 390 px. El resultado final no presentó desbordamiento global en ninguna de las tres resoluciones. La comprobación no añadió JavaScript al proyecto ni dependencias de ejecución.

## Interpretación de avance
- DEFINIDA: 20 interfaces, 30 funcionalidades, 6 reglas y 10 entidades.
- MAQUETADA: 20 de 20 interfaces = **100 %** del alcance visual solicitado. Las 30 capacidades tienen una representación visual o navegación correspondiente.
- IMPLEMENTADA: **0 de 30 funcionalidades con lógica real de negocio**. La navegación entre archivos existe, pero no implica que se autentique, guarde, calcule o aplique reglas.

## Decisiones y límites
- Los formularios de altas, edición, venta, marcación y contacto tienen botones visuales sin persistencia. Los selectores no cargan datos ni recalculan importes.
- Cada venta confirmada del ejemplo tiene un detalle, una salida de inventario y un ingreso económico coherentes. Los formularios nuevos representan propuestas separadas, no nuevos registros en el corte.
- Un empleado puede tener cero o un usuario. Las tres cuentas de referencia ya están asignadas; un alta requerirá un empleado sin cuenta.
- MovimientoInventario conserva el modelo de atributos solicitado; la referencia a la venta se expresa en motivo. Se documenta la evaluación futura de un vínculo estructurado.
- Las nueve relaciones oficiales se documentan, junto con las dos relaciones a Usuario derivadas de las FK de MovimientoInventario y MovimientoCaja; esto no añade entidades.
- Bootstrap es la única dependencia externa, exclusivamente CSS. La hoja propia mantiene estilos de respaldo sin conexión; no se incluye una copia local de Bootstrap.
- No se realizaron pruebas de tecnologías asistivas ni validación W3C externa. Las comprobaciones de accesibilidad se limitan a estructura, etiquetas, encabezados, foco visible y enlace para saltar al contenido.

## Pendientes
Completar los integrantes del G1 y las evidencias estadísticas, legales/noticiosas y citas solicitadas. Para Spring Boot: persistencia, validaciones RN01–RN06, seguridad y roles, transacciones de venta/stock/caja, concurrencia, formularios conectados, mensajes de resultado y pruebas de reglas. El detalle está en README, sección 16.
