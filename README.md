# Sistema Web de Gestión para Estación de Servicio - G1

## 1. Descripción
**Estación Nexo** es un sistema web para la gestión de una estación de servicio. Gestiona categorías, combustibles, compras, ventas, inventario, movimientos de caja, empleados, usuarios y asistencia, con identidad visual propia en verde petróleo y lima, navegación consistente y datos ficticios relacionados. El repositorio contiene la maqueta V1 (31 HTML + CSS en la raíz) y la versión V2 en Spring Boot, integrada en `main` desde el commit `9084263` (la rama `feature/version-2-spring` apunta al mismo commit), que opera los nueve módulos internos con datos en memoria.

## 2. Tema
Venta de combustibles organizados por categoría y producto, compra y abastecimiento al proveedor, control de inventario en litros, registro de ventas, ingresos y egresos de caja en soles, tablero de control, gestión de empleados y usuarios, y control de asistencia del personal.

## 3. Alcance
30 interfaces oficiales (P01–P30) en 31 HTML de la raíz, 38 funcionalidades definidas y representadas, 5 reglas de negocio (RN01–RN05) y 12 entidades. `publicidad.html` es otra presentación de P01. La V1 no tiene backend ni persistencia; la V2 trabaja con datos en memoria y no consume base de datos. El informe cubre **5.1–5.15 con sus pendientes visibles** (nada oculto ni por resolver inventado): **5.1–5.9 documentados sin pendientes**; **5.10 documentado, parcial** — capturas reales de los patrones revisados en clase (*requiere evidencia*) y fechas reales del cronograma pendientes; **5.11 y 5.12 documentados** — pendiente la validación de la redacción por el equipo; **5.13 documentado** — pendiente volcarlo a la plantilla A4/Arial 11 al compilar el informe; **5.14 documentado, parcial** — las dos obras obligatorias (Coronel/Morris/Rob; Cervantes Maceda, Velasco-Elizondo y Castro Careaga) siguen **pendientes de consulta**; **5.15 documentado**. Los documentos son [05_reglas_negocio.md](documentacion/05_reglas_negocio.md) (5.9), [10_productos_y_entregables.md](documentacion/10_productos_y_entregables.md), [11_conclusiones.md](documentacion/11_conclusiones.md), [12_recomendaciones.md](documentacion/12_recomendaciones.md), [13_glosario.md](documentacion/13_glosario.md), [14_bibliografia.md](documentacion/14_bibliografia.md) (5.10–5.14) y [15_anexos.md](documentacion/15_anexos.md) (5.15), junto con el BPMN de los procesos núcleo y de soporte y la matriz 38 × 30. Los pendientes de decisión humana están clasificados y marcados en [08_puntos_1_al_5_8.md](documentacion/08_puntos_1_al_5_8.md) y se reflejan en la sección 10 de [10_productos_y_entregables.md](documentacion/10_productos_y_entregables.md).

## 4. Módulos
Los doce módulos son: Acceso, Portada y contacto, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados, Usuarios y Asistencia. Portada y contacto son las dos partes públicas exigidas por la especificación oficial.

## 5. Tecnologías
**V1:** HTML5, CSS3 y Bootstrap CSS 5.3.3 mediante CDN. Sin Bootstrap JS, JavaScript, TypeScript, frameworks, Node.js, APIs ni base de datos. La hoja propia incluye estilos base para mantener la presentación si el CDN no está disponible; descargar Bootstrap desde CDN necesita conexión.
**V2 (actual):** Spring Boot 3.5.0 con Java 17, patrón MVC (Controller → Service → ServiceImpl), clases Java y vistas JSP/JSTL sobre datos en memoria (`List<T>`) y autenticación con Spring Security (contraseñas con BCrypt y roles). Sin base de datos, sin capa de Repositorio y sin interfaces REST; el acceso a persistencia queda para una etapa posterior.

## 6. Modelo de negocio
Inicio → Login → Dashboard → seleccionar combustible → verificar existencias y estado → representar venta y detalle → salida física de inventario → ingreso económico único → historial → conciliación de caja.
El abastecimiento sigue el camino inverso: Compras → confirmación de la compra → entradas por línea → **un único** egreso por el importe total.
El personal marca y consulta su asistencia en «Mi asistencia» (F35–F37) y el administrador la revisa en «Control de asistencia» (F38).
Inventario se explica en litros; finanzas en soles. En la V1 las operaciones se representan visualmente, sin ejecutarse; en la V2 se ejecutan sobre los datos en memoria. El detalle está en [Modelo de negocio](documentacion/01_modelo_negocio.md) y los flujos en [BPMN](documentacion/09_bpmn.md).

Datos de corte al 10/09/2026, 12:00: compra `C001` de 300 L / S/ 1,350.00; 3 ventas / 80 L / S/ 370.00; existencias 6,920 L; ingresos de hoy S/ 370.00; egresos de hoy S/ 1,350.00; apertura de caja S/ 4,410.00; saldo S/ 3,430.00. Asistencia: Ana Torres y Luis Rojas 08:00–17:00 y Elena Díaz 08:15–17:00, los tres *Presente*. No hay registro manual de ingresos ni de egresos: los ingresos nacen de las ventas (MC003–MC005) y el egreso de la compra (MC001). En la V2 los formularios crean registros en memoria mientras la aplicación corre; en la V1 los formularios son escenarios separados y no alteran el corte.

## 7. Modelo ER
Categoria, Producto, Compra, DetalleCompra, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, MovimientoCaja y Asistencia (relación Empleado 1:N Asistencia). [Atributos, claves, cardinalidades y Mermaid ER](documentacion/02_modelo_entidad_relacion.md).

## 8. Interfaces
| ID | Interfaz | Archivo | Funcionalidades | Reglas | Entidades | Versión V2 |
|---|---|---|---|---|---|---|
| P01 | Inicio / Publicidad | [index.html](index.html), [publicidad.html](publicidad.html) | F33 | No aplica | No aplica | Sólo V1 |
| P02 | Login | [login.html](login.html) | F01 | No aplica | Usuario | Sólo V1 |
| P03 | Dashboard | [dashboard.html](dashboard.html) | F02, F03 | No aplica | Venta DetalleVenta Producto MovimientoCaja | Sólo V1 |
| P04 | Contacto | [contacto.html](contacto.html) | F34 | No aplica | No aplica | Sólo V1 |
| P05 | Categorías | [categorias.html](categorias.html) | F04, F05, F06, F07, F08 | RN02 | Categoria Producto | /categorias/list |
| P06 | Combustibles | [combustibles.html](combustibles.html) | F10, F12 | RN02 | Producto Categoria | /combustibles/list |
| P07 | Formulario combustible | [combustible-form.html](combustible-form.html) | F09, F11 | RN02 | Producto Categoria | /combustibles/crear |
| P08 | Registrar venta | [ventas.html](ventas.html) | F18, F20 | RN01, RN02, RN03, RN04 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | /ventas/crear |
| P09 | Historial de ventas | [ventas-historial.html](ventas-historial.html) | F21 | RN03, RN04 | Venta DetalleVenta Producto Usuario MovimientoCaja | /ventas/list |
| P10 | Detalle de venta | [venta-detalle.html](venta-detalle.html) | F22 | RN03, RN04 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | /ventas/detalle |
| P11 | Existencias | [inventario.html](inventario.html) | F18 | RN01 | Producto | /inventario/list |
| P12 | Entrada combustible | [inventario-entrada.html](inventario-entrada.html) | F16 | RN01, RN03 | MovimientoInventario Producto Usuario Compra | /inventario/entrada/crear |
| P13 | Salida combustible | [inventario-salida.html](inventario-salida.html) | F17, F18 | RN01 | MovimientoInventario Producto Usuario | /inventario/salida/crear |
| P14 | Movimientos inventario | [inventario-movimientos.html](inventario-movimientos.html) | F19 | RN01, RN03 | MovimientoInventario Producto Usuario Compra | /inventario/entradas y /inventario/salidas |
| P15 | Resumen financiero | [finanzas.html](finanzas.html) | F28 | RN04 | MovimientoCaja ConceptoMovimiento Venta Compra | /finanzas/list |
| P16 | Ingresos y egresos de caja | [movimiento-economico.html](movimiento-economico.html) | F26, F27 | RN04 | MovimientoCaja ConceptoMovimiento | /finanzas/ingresos y /finanzas/egresos |
| P17 | Conceptos económicos | [conceptos.html](conceptos.html) | F24 | No aplica | ConceptoMovimiento | Sólo V1 |
| P18 | Empleados | [empleados.html](empleados.html) | F30 | No aplica | Empleado | /empleados/list |
| P19 | Usuarios | [usuarios.html](usuarios.html) | F32 | RN02 | Usuario Empleado | /usuarios/list |
| P20 | Compras | [compras.html](compras.html) | F13, F14 | RN03, RN04 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | /compras/list |
| P21 | Detalle de compra | [compra-detalle.html](compra-detalle.html) | F15 | RN03, RN04 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | /compras/detalle |
| P22 | Formulario categoría | [categoria-form.html](categoria-form.html) | F04, F07, F08 | RN02 | Categoria | /categorias/crear |
| P23 | Formulario compra | [compra-form.html](compra-form.html) | F13 | RN03, RN04 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | /compras/crear |
| P24 | Formulario venta | [venta-form.html](venta-form.html) | F20 | RN01, RN02, RN03, RN04 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | /ventas/crear |
| P25 | Formulario empleado | [empleado-form.html](empleado-form.html) | F29, F31 | RN02 | Empleado | /empleados/crear y /empleados/editar |
| P26 | Formulario usuario | [usuario-form.html](usuario-form.html) | F32 | RN02 | Usuario Empleado | /usuarios/crear y /usuarios/editar |
| P27 | Formulario concepto | [concepto-form.html](concepto-form.html) | F23, F25 | RN02 | ConceptoMovimiento | Sólo V1 |
| P28 | Mi asistencia | [mi-asistencia.html](mi-asistencia.html) | F35, F36, F37 | RN05 | Asistencia Empleado | /asistencia/mi |
| P29 | Control de asistencia | [control-asistencia.html](control-asistencia.html) | F38 | RN05 | Asistencia Empleado | /asistencia/control |
| P30 | Detalle de movimiento de caja | [movimiento-detalle.html](movimiento-detalle.html) | F28 | RN04 | MovimientoCaja ConceptoMovimiento Venta Compra | /finanzas/detalle |

[Fichas completas de P01–P30](documentacion/03_interfaces.md) · [Matriz 38 × 30](documentacion/06_matriz_funcionalidades_interfaces.md).

## 9. Funcionalidades
| ID | Funcionalidad | Módulo | Interfaz | Reglas | Estado |
|---|---|---|---|---|---|
| F01 | Iniciar sesión | Acceso | P02 | No aplica | Sólo V1 |
| F02 | Cerrar sesión | Acceso | P03 | No aplica | Sólo V1 |
| F03 | Consultar dashboard | Dashboard | P03 | No aplica | Sólo V1 |
| F04 | Registrar categoría | Categorías | P05, P22 | No aplica | V1 + V2 |
| F05 | Consultar categorías | Categorías | P05 | No aplica | V1 + V2 |
| F06 | Consultar combustibles por categoría | Categorías | P05 | No aplica | Sólo V1 |
| F07 | Editar categoría | Categorías | P05, P22 | RN02 | Sólo V1 |
| F08 | Activar/desactivar categoría | Categorías | P05, P22 | RN02 | Sólo V1 |
| F09 | Registrar combustible | Combustibles | P07 | No aplica | V1 + V2 |
| F10 | Consultar combustibles | Combustibles | P06 | No aplica | V1 + V2 |
| F11 | Editar combustible | Combustibles | P07 | RN02 | Sólo V1 |
| F12 | Activar/desactivar combustible | Combustibles | P06 | RN02 | Sólo V1 |
| F13 | Registrar compra de combustible | Compras | P20, P23 | RN03, RN04 | V1 + V2 |
| F14 | Consultar compras | Compras | P20 | RN03, RN04 | V1 + V2 |
| F15 | Consultar detalle de compra | Compras | P21 | RN03, RN04 | V1 + V2 |
| F16 | Registrar entrada de combustible | Inventario | P12 | RN01, RN03 | V1 + V2 |
| F17 | Registrar salida de combustible | Inventario | P13 | RN01 | V1 + V2 |
| F18 | Consultar existencias | Inventario | P08, P11, P13 | RN01 | V1 + V2 |
| F19 | Consultar movimientos de inventario | Inventario | P14 | RN01, RN03 | V1 + V2 |
| F20 | Registrar venta | Ventas | P08, P24 | RN01, RN02, RN03, RN04 | V1 + V2 |
| F21 | Consultar ventas | Ventas | P09 | RN03, RN04 | V1 + V2 |
| F22 | Consultar detalle de venta | Ventas | P10 | RN03, RN04 | V1 + V2 |
| F23 | Registrar concepto económico | Finanzas | P27 | No aplica | Sólo V1 |
| F24 | Consultar conceptos económicos | Finanzas | P17 | No aplica | Sólo V1 |
| F25 | Editar/activar/desactivar concepto económico | Finanzas | P27 | RN02 | Sólo V1 |
| F26 | Consultar ingresos de caja | Finanzas | P16 | RN04 | V1 + V2 |
| F27 | Consultar egresos de caja | Finanzas | P16 | RN04 | V1 + V2 |
| F28 | Consultar movimientos y saldo de caja | Finanzas | P15, P30 | RN04 | V1 + V2 |
| F29 | Registrar empleado | Empleados | P25 | No aplica | V1 + V2 |
| F30 | Consultar empleados | Empleados | P18 | No aplica | V1 + V2 |
| F31 | Editar/activar/desactivar empleado | Empleados | P25 | RN02 | V1 + V2 |
| F32 | Gestionar usuarios | Usuarios | P19, P26 | RN02 | V1 + V2 |
| F33 | Consultar la portada pública | Portada y contacto | P01 | No aplica | Sólo V1 |
| F34 | Enviar mensaje de contacto | Portada y contacto | P04 | No aplica | Sólo V1 |
| F35 | Registrar asistencia | Asistencia | P28 | RN05 | V1 + V2 |
| F36 | Consultar mi asistencia | Asistencia | P28 | RN05 | V1 + V2 |
| F37 | Consultar mi resumen de asistencia | Asistencia | P28 | RN05 | V1 + V2 |
| F38 | Consultar asistencia del personal | Asistencia | P29 | RN05 | V1 + V2 |

[Fichas completas de F01–F38](documentacion/04_funcionalidades.md). Ninguna funcionalidad carece de interfaz; catorce de ellas (F01–F06, F09, F10, F23, F24, F29, F30, F33 y F34) no están ligadas a ninguna regla.

## 10. Reglas de negocio
- RN01: **Control de existencias**. El stock de un combustible nunca puede ser negativo; las salidas y las ventas sólo se realizan si la cantidad no supera el stock disponible, y las entradas válidas incrementan las existencias.
- RN02: **Conservación de registros**. Los registros relacionados con operaciones del sistema no se eliminan físicamente; cuando dejan de estar disponibles se cambian a Inactivo manteniendo su historial y sus relaciones.
- RN03: **Integridad de operaciones**. Toda compra contiene sus detalles y genera una entrada de inventario; toda venta contiene sus detalles y genera una salida de inventario; cada efecto se produce una sola vez.
- RN04: **Trazabilidad económica**. Toda compra genera un único egreso de caja y toda venta un único ingreso de caja, y cada movimiento queda relacionado con la operación que lo originó.
- RN05: **Control de asistencia del personal**. Sólo los empleados activos registran su propia jornada, sin asistencias abiertas dobles ni dos registros en la misma jornada, y con la salida posterior a la entrada.

[Reglas completas con enunciado, ámbito, casos y validación en el sistema](documentacion/05_reglas_negocio.md). Las cinco reglas se aplican en el servidor de la versión V2 y se representan en la maqueta V1; no existen reglas adicionales a RN01–RN05.

## 11. Estructura de archivos
```text
ing-web/
├── README.md
├── .gitignore
├── pom.xml
├── mvnw · mvnw.cmd · .mvn/
├── src/
│   ├── main/java/com/example/nexo/   (controladores, servicios y modelos — V2)
│   └── main/webapp/WEB-INF/views/    (27 vistas JSP — V2)
├── index.html
├── login.html
├── dashboard.html
├── publicidad.html
├── contacto.html
├── categorias.html
├── categoria-form.html
├── combustibles.html
├── combustible-form.html
├── compras.html
├── compra-detalle.html
├── compra-form.html
├── ventas.html
├── ventas-historial.html
├── venta-detalle.html
├── venta-form.html
├── inventario.html
├── inventario-entrada.html
├── inventario-salida.html
├── inventario-movimientos.html
├── finanzas.html
├── movimiento-economico.html
├── movimiento-detalle.html
├── conceptos.html
├── concepto-form.html
├── empleados.html
├── empleado-form.html
├── usuarios.html
├── usuario-form.html
├── mi-asistencia.html
├── control-asistencia.html
├── css/
│   └── estilos.css
└── documentacion/
    ├── 00_auditoria.md
    ├── 01_modelo_negocio.md
    ├── 02_modelo_entidad_relacion.md
    ├── 03_interfaces.md
    ├── 04_funcionalidades.md
    ├── 05_reglas_negocio.md
    ├── 06_matriz_funcionalidades_interfaces.md
    ├── 07_trazabilidad.md
    ├── 08_puntos_1_al_5_8.md
    ├── 09_bpmn.md
    ├── 10_productos_y_entregables.md
    ├── 11_conclusiones.md
    ├── 12_recomendaciones.md
    ├── 13_glosario.md
    ├── 14_bibliografia.md
    ├── 15_anexos.md
    ├── anexos/
    │   ├── captura-dashboard.png
    │   ├── captura-finanzas.png
    │   ├── captura-compras.png
    │   ├── captura-compra-detalle.png
    │   └── captura-bpmn.png
    └── bpmn.html
```

## 12. Navegación
**V1:** abrir [index.html](index.html) → **Ingresar al sistema** → [login.html](login.html) → **Iniciar sesión** → [dashboard.html](dashboard.html). Los campos de acceso no se envían como parámetros y no existe autenticación. **Cerrar sesión** vuelve a login. Inicio vuelve a la página pública.
La navegación interna reúne los módulos en este orden, sin dropdowns: Inicio, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados, Usuarios, Asistencia, Contacto, Cerrar sesión. Ventas enlaza su historial y detalles; inventario ofrece entrada, salida y movimientos; compras enlaza a su detalle; las pantallas de lista enlazan sus formularios dedicados (P22–P27) y el resumen financiero enlaza el detalle de cada movimiento (P30). Los formularios de edición se alcanzan por anclas. No hay controles de filtrado que prometan funciones inexistentes.
**V2:** cada módulo se abre por su ruta (`/categorias/list`, `/combustibles/list`, `/compras/list`, `/ventas/list`, `/inventario/list`, `/finanzas/list`, `/empleados/list`, `/usuarios/list`, `/asistencia/mi`, `/asistencia/control` y los formularios y detalles asociados); las 27 vistas JSP comparten un menú lateral que enlaza los nueve módulos internos.

## 13. Estado del avance
- **DEFINIDA:** 38/38 funcionalidades, 30/30 interfaces, 5/5 reglas y 12/12 entidades documentadas.
- **MAQUETADA (V1):** 30/30 interfaces (100 %) y representación visual de las 38 capacidades. Los botones de mutación son visuales.
- **IMPLEMENTADA (V2, datos en memoria):** 35/38 funcionalidades operan con lógica real en la V2 integrada en `main` (F01, F02, F04–F32 y F35–F38), verificadas por la regresión integral de la ETAPA 10 (126/126 pruebas HTTP contra la aplicación en marcha). El servidor aplica RN01–RN05 (rechazo por stock, cambio de estado sin borrados, efectos de compra/venta una sola vez, ingreso/egreso únicos con origen conciliado, y asistencia con identidad real y jornada única).
- **FUERA DEL ALCANCE V2 (sólo V1):** 3/38 — F03 (tablero), F33 (portada pública) y F34 (mensaje de contacto), decisión documentada en [04_funcionalidades.md](documentacion/04_funcionalidades.md); se conservan definidas y maquetadas en V1. El acceso, la edición de catálogo y la gestión de conceptos antes listados como pendientes ya operan en V2.

[Auditoría y alcance de comprobaciones](documentacion/00_auditoria.md) · [Matriz](documentacion/06_matriz_funcionalidades_interfaces.md) · [Trazabilidad](documentacion/07_trazabilidad.md) · [BPMN](documentacion/09_bpmn.md) · [Productos y entregables 5.10](documentacion/10_productos_y_entregables.md) · [Conclusiones 5.11](documentacion/11_conclusiones.md) · [Recomendaciones 5.12](documentacion/12_recomendaciones.md) · [Glosario 5.13](documentacion/13_glosario.md) · [Bibliografía 5.14](documentacion/14_bibliografia.md) · [Anexos 5.15](documentacion/15_anexos.md).

## 14. Cómo ejecutar
**V2:** requiere JDK 17 y Maven. Desde la raíz, `mvnw spring-boot:run` (o `mvn spring-boot:run`) y abrir una ruta de módulo, por ejemplo `http://localhost:8080/ventas/list` o `http://localhost:8080/inventario/list`. La raíz `/` no tiene vista propia. Los datos viven en memoria: reiniciar la aplicación restablece la semilla.
**V1:** abrir index.html en un navegador mediante doble clic. También se puede abrir la carpeta con VS Code y usar Live Server sobre index.html. Live Server está configurado en el puerto **2222** mediante `.vscode/settings.json`: acceder a `http://localhost:2222/index.html`. La configuración de depuración de Chrome usa el mismo puerto; primero iniciar Live Server. Si ya estaba iniciado, detenerlo y volverlo a iniciar para aplicar el cambio. Los enlaces y CSS propios usan rutas relativas.

## 15. Limitaciones
**V1:** los datos son fijos; no hay autenticación, autorización, cálculos dinámicos, envío de contacto, persistencia, búsqueda ni aplicación automática de reglas. Cambiar un selector no carga otro registro ni recalcula importes. Los gráficos son HTML/CSS con valores escritos y el eje X de los cinco gráficos es temporal (04–10 sep. 2026), no categórico. Las acciones sobre categorías, productos, compras, empleados y usuarios son visuales. Los formularios sí tienen validaciones nativas de HTML5 (obligatoriedad, rangos, formatos y longitudes), pero no envían nada. Bootstrap vía CDN requiere conexión; la CSS propia ofrece una presentación de respaldo.
**V2:** los datos son en memoria y se pierden al reiniciar; no hay persistencia ni base de datos. La autenticación y los roles sí están activos con Spring Security: las rutas se protegen por rol, las contraseñas se guardan con BCrypt y todo POST exige token CSRF. Fuera del alcance V2 quedan el tablero (F03), la portada pública (F33) y el contacto (F34), que existen sólo en V1. La asistencia usa la identidad del usuario autenticado, no un valor enviado por el formulario. Sin JavaScript: toda la interacción es envío de formularios y enlaces.

## 16. Versión V2 y etapa posterior
La V2 actual sigue el patrón Controller → Service → ServiceImpl → `List<T>` en memoria → modelo → JSP/JSTL, con anotaciones `@Controller`, `@RequestMapping`, `@GetMapping`, `@PostMapping`, `@RequestParam`, `@ModelAttribute`, `Model`, `@Service`, redirecciones `redirect:` y vistas JSTL; código simple y explicable, sin patrones avanzados. Sobre esa base ya operan el acceso y los nueve módulos de gestión (categorías, combustibles, compras, inventario, ventas, finanzas, empleados, usuarios y asistencia) con RN01–RN05 aplicadas en el servidor.
La etapa posterior incorporará el acceso a persistencia (base de datos y, si el patrón lo requiere, una capa de Repositorio) y, como decisión separada, las capacidades fuera del alcance V2 (F03, F33 y F34). La autenticación con roles ya está operativa desde la ETAPA 2 con Spring Security (`SecurityConfig`, `NexoUserDetailsService` y BCrypt). La V2 no usa JPA, Hibernate, JdbcTemplate, Repository, DAO ni APIs REST.

Documento académico: [Puntos 1 al 5.8](documentacion/08_puntos_1_al_5_8.md) · [BPMN](documentacion/09_bpmn.md) · [Puntos 5.9 a 5.14](documentacion/10_productos_y_entregables.md) · [Anexos 5.15](documentacion/15_anexos.md). Pendientes de decisión humana: integrantes, anexo oficial del proceso, capturas de patrones y fechas del cronograma (5.10), obras bibliográficas obligatorias (5.14) y validación del equipo sobre conclusiones, recomendaciones y glosario; no se inventaron nombres del grupo ni estadísticas.
