# ETAPA 9 — Organización del repositorio

Objetivo: ordenar **conceptualmente** los archivos y dejar una estructura clara sin mover código activo. Única operación destructiva autorizada: eliminar los dos PDF duplicados de `recurso/`, verificando antes que sean idénticos. No se borra ningún HTML V1, JSP, Java, diagrama, matriz, Maven Wrapper ni documentación original.

## 1. Mapa conceptual vigente (verificado el 09/10/2026)

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

## 2. Cambios ejecutados en esta etapa

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

## 3. Qué NO se movió y por qué

| Elemento | Razón para no moverlo |
|---|---|
| Los 31 HTML de la raíz y `css/estilos.css` | Son la interfaz entregable de la V1; están enlazados entre sí (804 enlaces, 0 rotos) y citados por toda la documentación; moverlos rompería la navegación y las citas |
| `src/` (Java y JSP) | Paquete, rutas y nombres de vista están cableados (`@Controller` → JSP); cualquier movimiento exige refactor y recompilación |
| `documentacion/` | Los 21 archivos se enlazan entre sí y desde el README |
| `recurso/` | Los PDF se citan por nombre exacto en `14_bibliografia.md` §B y en las cabeceras de `10`–`15_*.md`; moverlos a subcarpetas rompería esas referencias |
| Maven Wrapper (`mvnw`, `mvnw.cmd`) y `pom.xml` | Exigidos por la rúbrica y el proceso del curso; intactos |

## 4. Propuesta futura (no ejecutada; requiere decisión del equipo)

1. **Subcarpetas dentro de `recurso/`** (por ejemplo `rúbricas/`, `guías/`, `plantilla/`) + actualización de las citas en `14_bibliografia.md` y las cabeceras de `10`–`15_*.md`. Riesgo bajo, pero toca documentación oficial.
2. **`.vscode/` en `.gitignore`** si el equipo prefiere no versionar la configuración local (hoy está versionado por decisión previa).
3. **Mover las capturas nuevas** (cuando existan) a `documentacion/anexos/` con nombre `captura-<interfaz>.png`, siguiendo el patrón actual.

Nada de esto se ejecuta sin autorización explícita.

## 5. Verificación final

- `recurso/`: **9 archivos PDF, 0 duplicados** (detección completa por MD5).
- Ningún HTML V1, JSP, Java, diagrama, matriz, Maven Wrapper ni documento original eliminados o movidos.
- Estado git: los borrados y ediciones quedan como cambios **pendientes en el working tree**; **sin commits** (instrucción vigente de la auditoría).
