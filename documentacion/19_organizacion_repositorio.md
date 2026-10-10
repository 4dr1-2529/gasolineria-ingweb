# ETAPA 9 — Organización del repositorio

Objetivo: ordenar **conceptualmente** los archivos y dejar una estructura clara sin mover código activo. Única operación destructiva autorizada: eliminar los dos PDF duplicados de `recurso/`, verificando antes que sean idénticos. No se borra ningún HTML V1, JSP, Java, diagrama, matriz, Maven Wrapper ni documentación original.

## 1. Mapa conceptual anterior (verificado el 09/10/2026; actualizado abajo)

```
raíz del proyecto
├── 31 archivos HTML (P01–P30) ....... maqueta V1; son la interfaz de la entrega
│                                       (cada uno declara Interfaz, Funcionalidades,
│                                       Entidades y Reglas en su cabecera)
├── css/estilos.css .................. hoja de estilo única de la maqueta V1
├── README.md ......................... descripción, alcance V1/V2 y mapa de secciones
├── pom.xml + mvnw + mvnw.cmd ......... Maven Wrapper (no se modifica)
├── src/main/java/com/example/nexo/ ... V2 Spring Boot
│   ├── config/ ....... SecurityConfig (matriz de roles y rutas)
│   ├── controller/ .... 11 controllers (Auth, Home, Categoria, Producto, Compra,
│   │                     Venta, Inventario, Finanzas, Empleado, Usuario, Asistencia)
│   ├── model/ ......... 12 entidades
│   └── service/ ........ 9 interfaces + 9 ServiceImpl + NexoUserDetailsService
├── src/main/webapp/
│   ├── css/estilos.css  estilo de la V2
│   └── WEB-INF/views/ .. 34 JSP en 10 subcarpetas por módulo (asistencia, categoria,
│                          compra, empleado, error, finanzas, inventario, producto,
│                          usuario, venta)
├── documentacion/ .................... 21 archivos: 00–19 en Markdown + bpmn.html
│   └── anexos/ ........ 5 PNG (capturas)
├── recurso/ ......................... 9 PDF del curso (tras la limpieza)
└── .vscode/ ......................... launch.json, settings.json (config del equipo)
```

Los scripts temporales de construcción y comprobación viven fuera del repositorio (decisión registrada en [00_auditoria.md](00_auditoria.md)) y no forman parte de la entrega.

## 2. Cambios ejecutados en la etapa anterior

### 2.1 Eliminación de los dos duplicados autorizados

Verificación previa por MD5 (y tamaño) antes de borrar:

| Archivo eliminado | Duplicaba a | MD5 (12 primeros bytes) | Tamaño | Decisión |
|---|---|---|---|---|
| `recurso/e6920ae9-009e-4ab0-891b-72dad466e347(1).pdf` | `guia3(1).pdf` | `1AB60FFBA19E` (idénticos) | 867,449 bytes | Se conserva `guia3(1).pdf` (nombre canónico citado en [14_bibliografia.md](14_bibliografia.md) §B) |
| `recurso/Proyecto%20Estructura_v2%20%281%29 (1).pdf` | `Proyecto%20Estructura_v2%20%281%29.pdf` | `ABD23F94F297` (idénticos) | 323,687 bytes | Se conserva el archivo sin « (1)» final (es la fuente citada por `10`–`15_*.md`) |

Ambos archivos estaban versionados en git (recuperables con `git checkout`), y ninguno de los dos era citado por la documentación: las citas apuntan al archivo que se conserva en cada par. Después del borrado se comprobó que los 9 PDF restantes tienen 9 MD5 distintos: **no queda ningún duplicado en `recurso/`**.

### 2.2 Actualización de los conteos afectados (para que la documentación siga diciendo la verdad)

| Archivo | Cambio |
|---|---|
| [00_auditoria.md](00_auditoria.md) | «`recurso/` (11 PDF de la asignatura)» → «(9 PDF…; la ETAPA 9 eliminó 2 copias byte-idénticas)» |
| [14_bibliografia.md](14_bibliografia.md) §B | Encabezado «10 documentos únicos» → «9 documentos únicos (tras la limpieza de la ETAPA 9)»; se retiró la fila de `e6920ae9-…(1).pdf` (era byte-idéntica a `guia3(1).pdf`, ya listado) y la nota final ahora documenta los dos pares eliminados |
| [15_anexos.md](15_anexos.md) | «`recurso/` (11 PDF del curso…)» → «(9 PDF…; 2 copias duplicadas eliminadas en la ETAPA 9)» |

No se modificó ningún otro contenido de la documentación original.

## 3. Evaluación anterior al movimiento (09/10/2026; sustituida por la sección 6)

| Elemento | Razón para no moverlo |
|---|---|
| Los 31 HTML de la raíz y `css/estilos.css` | En el análisis previo se recomendó no moverlos por ser la interfaz V1 y estar enlazados entre sí. En la revisión del 10/10/2026 se trasladaron juntos a `diseno-original/`; se comprobaron sus enlaces relativos y se actualizaron las referencias documentales. |
| `src/` (Java y JSP) | Paquete, rutas y nombres de vista están cableados (`@Controller` → JSP); cualquier movimiento exige refactor y recompilación |
| `documentacion/` | Los 21 archivos se enlazan entre sí y desde el README |
| `recurso/` | Los PDF se citan por nombre exacto en `14_bibliografia.md` §B y en las cabeceras de `10`–`15_*.md`; moverlos a subcarpetas rompería esas referencias |
| Maven Wrapper (`mvnw`, `mvnw.cmd`) y `pom.xml` | Exigidos por la rúbrica y el proceso del curso; intactos |

## 4. Propuesta futura (no ejecutada; requiere decisión del equipo)

1. **Subcarpetas dentro de `recurso/`** (por ejemplo `rúbricas/`, `guías/`, `plantilla/`) + actualización de las citas en `14_bibliografia.md` y las cabeceras de `10`–`15_*.md`. Riesgo bajo, pero toca documentación oficial.
2. **`.vscode/` en `.gitignore`** si el equipo prefiere no versionar la configuración local (hoy está versionado por decisión previa).
3. **Mover las capturas nuevas** (cuando existan) a `documentacion/anexos/` con nombre `captura-<interfaz>.png`, siguiendo el patrón actual.

Nada de esto se ejecuta sin autorización explícita.

## 5. Verificación de la etapa anterior (09/10/2026; antes del movimiento)

- `recurso/`: **9 archivos PDF, 0 duplicados** (detección completa por MD5).
- En ese corte no se habían movido HTML V1, JSP, Java, diagramas, matrices, Maven Wrapper ni documentos originales.
- El estado pendiente de Git descrito correspondía a la etapa anterior; el estado de la rama de reorganización se registra al integrar y validar los movimientos en la sección 6.


## 6. Revision de organizacion en la rama de trabajo (10/10/2026)

Se trasladaron los **31 HTML de maqueta** desde la raiz a `diseno-original/` y `css/estilos.css` a `diseno-original/css/estilos.css`. Son evidencia de las 30 interfaces P01-P30 (31 archivos porque `publicidad.html` presenta P01 por segunda vez) y se conservan intactos. El estilo se movio junto con las paginas: las rutas relativas dentro de la maqueta siguen iguales.

La aplicacion Spring Boot no depende de esos prototipos: Maven compila `src/main/java/`; Spring MVC resuelve sus JSP desde `src/main/webapp/WEB-INF/views/`; Spring Security expone `/css/**` para `src/main/webapp/css/estilos.css`; y `login.jsp` enlaza esa hoja por `${pageContext.request.contextPath}/css/estilos.css`. No hay controladores ni recursos Maven que publiquen los HTML de `diseno-original/`. La aplicacion funcional se ejecuta con `./mvnw.cmd spring-boot:run` y abre en `http://localhost:8080/login`; la maqueta se abre desde `diseno-original/index.html`.

Las referencias Markdown a las 31 interfaces se actualizaron y se verificaron despues del movimiento. La revision local detecto 516 enlaces de recursos/navegacion en los HTML originales y no hallo rutas rotas.

### Clasificacion de archivos, sin eliminacion

| Grupo | Inventario actual | Tratamiento propuesto |
|---|---|---|
| Entrega academica | `README.md`; `documentacion/00`-`23`; `documentacion/bpmn.html`; 5 capturas en `documentacion/anexos/`; las 31 maquetas en `diseno-original/`; 9 PDF en `recurso/` | Conservar para rubrica, informe, trazabilidad y fuentes. No hay duplicados binarios entre los archivos de `recurso/` y `documentacion/` revisados. |
| Evidencias de pruebas | `documentacion/evidencias/bateria_revision.ps1`, `resultados_133_pruebas.csv`, `log_133_pruebas.txt`, `verificacion_enlaces.txt` y seis capturas HTML del login | Conservar bajo `documentacion/evidencias/`; separadas del informe y respaldan sus resultados. |
| Material de consulta docente | Los 9 PDF de `recurso/` (incluyen la rubrica y documentos del curso) | Conservar; son fuentes citadas y no hay copias identicas en el inventario actual. |
| Historicos o parcialmente solapados | `16_evidencias_etapas.md`, `17_matriz_cumplimiento_avance1.md`, `18_matriz_cumplimiento_examen_parcial.md`, `22_informe_regresion_etapa10.md` | Revisar consolidacion con el equipo: registran etapas y cortes distintos; no son duplicados exactos y su eliminacion romperia la trazabilidad historica. |
| Candidatos a archivo, no a borrado | `22_informe_regresion_etapa10.md` y `23_informe_regresion_revision_final.md` | Mantener ambos hasta aprobar que corte exige la entrega; si se archiva el corte anterior, conservarlo en una carpeta historica y actualizar todos los enlaces. |

No se propone eliminar ningun archivo en esta revision. El documento Word mencionado para el informe no esta presente en el repositorio (no se encontro `.doc` ni `.docx`); el Markdown `08_puntos_1_al_5_8.md` contiene los puntos 5.1-5.8 y el estado de 5.10-5.14, con 5.9 separado en `05_reglas_negocio.md`.

## 7. Organización Java por módulos (10/10/2026)

Las 43 clases de dominio, capa de negocio, controladores y seguridad se movieron bajo sus módulos funcionales, conservando las capas `controller/`, `service/`, `model/` y `config/` donde corresponden. `NexoApplication.java` y `ServletInitializer.java` siguen en `com.example.nexo`. La pantalla funcional de acceso se trasladó a `src/main/webapp/WEB-INF/views/auth/login.jsp`; `AuthController` devuelve `auth/login`. Permanecen intactas las 34 JSP y las rutas HTTP. Se actualizó el árbol del README y la ubicación de `SecurityConfig` y la vista de login en la explicación técnica. Se conservaron las 31 maquetas, documentación, fuentes y evidencias académicas.
