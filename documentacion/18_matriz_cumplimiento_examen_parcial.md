# ETAPA 8 — Matriz de cumplimiento: Examen parcial

**Fuente:** descripción de la rúbrica del examen parcial entregada por el docente en las instrucciones de la ETAPA 8 (09/10/2026). En el repositorio **no hay** un PDF de esta rúbrica; se usa tal como fue descrita: evolución de la landing inicial a storyboard y MVP, retroalimentación recibida, MVP frontend con HTML + CSS + JavaScript, despliegue en la nube, arquitectura propuesta, técnicas de ingeniería web y conclusiones.

Esta matriz **no se mezcla** con la de Avance 1 ([17_matriz_cumplimiento_avance1.md](17_matriz_cumplimiento_avance1.md)).

Regla aplicada en cada fila: **no se inventa** storyboard, retroalimentación, JavaScript, URL de despliegue ni consumo de API. El estado es el verificado directamente en el repositorio.

| # | Requisito del examen parcial | Archivo de evidencia | Estado real | Qué falta |
|---|---|---|---|---|
| E1 | Evolución de la landing inicial a storyboard y MVP | Landing inicial: `index.html` + `publicidad.html` (P01). MVP: maqueta V1 (31 HTML + `css/estilos.css`) y MVP funcional V2 (`src/main/`: Spring Boot + 34 JSP, datos en memoria) | **Parcial** — la landing inicial existe y el MVP existe en sus dos formas (estático y funcional); el tramo intermedio «storyboard» **no está en el repositorio** (0 coincidencias de «storyboard» en HTML, MD, JSP, Java y TXT) | Un storyboard real del equipo; no se fabrica uno ni se deduce a partir de la maqueta |
| E2 | Retroalimentación recibida | No existe en el repo (0 coincidencias de «retroalimentación» o «feedback») | **No cumplido** | Documento o registro real de la retroalimentación recibida sobre la landing/storyboard; no se inventa |
| E3 | MVP frontend con HTML + CSS + JavaScript | HTML: 31 archivos en la raíz. CSS: `css/estilos.css` + Bootstrap 5.3.3 por CDN. JavaScript: **0** archivos `.js` y **0** etiquetas `<script>` en todo el repo | **Parcial** — HTML y CSS cumplidos; JavaScript no existe y su ausencia es una decisión documentada (notas de clase: «JavaScript no se usará»; README §5; [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) §4) | Si el docente exige JavaScript, deberá implementarse como cambio nuevo y declararse como tal; hoy no se oculta ni se maquilla |
| E4 | Despliegue en la nube | No hay URL ni proveedor en ningún archivo del repo (0 coincidencias de vercel, netlify, heroku, azurewebsites, railway, fly.io, onrender) | **No cumplido** | Desplegar la V2 y publicar la URL real; no se inventa ninguna URL |
| E5 | Arquitectura propuesta | [10_productos_y_entregables.md](10_productos_y_entregables.md) §B (MVC; Controller → Service → ServiceImpl; persistencia pendiente por diseño V2), README §5 (stack V2), `src/main/java/com/example/nexo/{config,controller,model,service}` y `config/SecurityConfig.java` | **Cumplido** — la arquitectura está documentada y coincide con la implementada: capas sobre `List<T>` en memoria, sin BD/JPA/Repository/DAO | — |
| E6 | Técnicas de ingeniería web | [10_productos_y_entregables.md](10_productos_y_entregables.md) §A–C: design tokens (variables CSS), navegación consistente, convención de comentario por página, MVC, Service/ServiceImpl, capas, y en la V2: BCrypt, flash messages, validación en servidor, protección CSRF | **Parcial** — los patrones propios del proyecto están documentados con evidencia en código; falta «capturas explicativas de los patrones revisados en el curso», que exige material de clase que no está en el repo (declarado PENDIENTE en `10_productos` §C; **no se inventa**) | Material de clase o capturas reales de los patrones revisados en clase |
| E7 | Conclusiones | [11_conclusiones.md](11_conclusiones.md) (complementada por [12_recomendaciones.md](12_recomendaciones.md)) | **Cumplido** | — |

## Resumen del examen parcial

| Estado | Cantidad | Ítems |
|---|---|---|
| Cumplido | 2 | E5 arquitectura propuesta, E7 conclusiones |
| Parcial | 3 | E1 (sin storyboard), E3 (sin JavaScript), E6 (capturas de patrones de clase) |
| No cumplido | 2 | E2 retroalimentación recibida, E4 despliegue en la nube |

**Ítems que dependen de material humano y por eso no se completan desde el repositorio:** storyboard (E1), retroalimentación (E2), JavaScript si el docente lo exige (E3), URL de despliegue (E4) y capturas de patrones de clase (E6). Ninguno de ellos se sustituye con contenido fabricado.

*Verificado: ETAPA 8 (09/10/2026), búsqueda global en el repo (HTML, MD, JSP, Java, TXT; excluyendo `.git/` y `target/`).*
