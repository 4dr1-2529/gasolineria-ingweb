# Sistema Web de Gestión para Estación de Servicio - G1

## 1. Descripción
**Estación Nexo** es una maqueta académica del curso Ingeniería Web, grupo G1. Presenta una estación de servicio con identidad visual propia en verde petróleo y lima, navegación consistente y datos ficticios relacionados.

## 2. Tema
Sistema de venta de gasolina, manejo de inventario, transacciones de ingreso y egreso y marcación de asistencia del personal.

## 3. Alcance
20 interfaces oficiales en 21 HTML, 30 funcionalidades definidas y representadas, 6 reglas de negocio y 10 entidades. Publicidad es otra presentación de P01. No hay backend ni persistencia.

## 4. Módulos
Inicio / Publicidad · Login · Dashboard · Categorías · Combustibles · Ventas · Inventario · Finanzas · Empleados · Usuarios · Asistencia · Contacto.

## 5. Tecnologías
HTML5, CSS3 y Bootstrap CSS 5.3.3 mediante CDN. Sin Bootstrap JS, JavaScript, TypeScript, frameworks, Node.js, APIs ni base de datos. La hoja propia incluye estilos base para mantener la presentación si el CDN no está disponible; descargar Bootstrap desde CDN necesita conexión.

## 6. Modelo de negocio
Inicio → Login → Dashboard → seleccionar combustible → consultar stock → representar venta y detalle → salida física → ingreso económico → historial.
Inventario se expresa en litros; finanzas en soles. Las operaciones se representan visualmente, sin ejecutarse. El detalle y la conciliación están en [Modelo de negocio](documentacion/01_modelo_negocio.md).

Datos de corte al 10/09/2026, 12:00: 3 ventas / 80 L / S/ 370.00; existencias 6,920 L; ingresos S/ 370.00; egresos S/ 50.00; saldo S/ 320.00; 2 personas con asistencia abierta. Formularios nuevos son escenarios separados y no alteran el corte.

## 7. Modelo ER
Categoria, Producto, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, MovimientoCaja y Asistencia. [Atributos, claves, cardinalidades y Mermaid ER](documentacion/02_modelo_entidad_relacion.md).

## 8. Interfaces
| ID | Interfaz | Archivo | Funcionalidades | Reglas | Entidades | Estado |
|---|---|---|---|---|---|---|
| P01 | Inicio / Publicidad | [index.html](index.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P02 | Login | [login.html](login.html) | F01 | No aplica | Usuario | DEFINIDA + MAQUETADA |
| P03 | Dashboard | [dashboard.html](dashboard.html) | F02, F03 | No aplica | Venta Producto MovimientoCaja Asistencia | DEFINIDA + MAQUETADA |
| P04 | Contacto | [contacto.html](contacto.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P05 | Categorías | [categorias.html](categorias.html) | F04, F05, F06, F07 | RN03 | Categoria | DEFINIDA + MAQUETADA |
| P06 | Combustibles | [combustibles.html](combustibles.html) | F09, F11 | RN02, RN03 | Producto Categoria | DEFINIDA + MAQUETADA |
| P07 | Formulario combustible | [combustible-form.html](combustible-form.html) | F08, F10 | RN02, RN03 | Producto Categoria | DEFINIDA + MAQUETADA |
| P08 | Registrar venta | [ventas.html](ventas.html) | F12, F16 | RN01, RN02, RN04 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |
| P09 | Historial de ventas | [ventas-historial.html](ventas-historial.html) | F17 | No aplica | Venta DetalleVenta Producto Usuario | DEFINIDA + MAQUETADA |
| P10 | Detalle de venta | [venta-detalle.html](venta-detalle.html) | F18 | RN04 | Venta DetalleVenta Producto Usuario MovimientoCaja | DEFINIDA + MAQUETADA |
| P11 | Existencias | [inventario.html](inventario.html) | F12 | RN01 | Producto | DEFINIDA + MAQUETADA |
| P12 | Entrada combustible | [inventario-entrada.html](inventario-entrada.html) | F13 | No aplica | Producto Usuario MovimientoInventario | DEFINIDA + MAQUETADA |
| P13 | Salida combustible | [inventario-salida.html](inventario-salida.html) | F12, F14 | RN01 | Producto Usuario MovimientoInventario | DEFINIDA + MAQUETADA |
| P14 | Movimientos inventario | [inventario-movimientos.html](inventario-movimientos.html) | F15 | No aplica | MovimientoInventario Producto Usuario | DEFINIDA + MAQUETADA |
| P15 | Resumen financiero | [finanzas.html](finanzas.html) | F24 | RN04 | MovimientoCaja ConceptoMovimiento Venta | DEFINIDA + MAQUETADA |
| P16 | Movimiento económico | [movimiento-economico.html](movimiento-economico.html) | F22, F23 | RN04 | MovimientoCaja ConceptoMovimiento Usuario Venta | DEFINIDA + MAQUETADA |
| P17 | Conceptos económicos | [conceptos.html](conceptos.html) | F19, F20, F21 | RN03 | ConceptoMovimiento | DEFINIDA + MAQUETADA |
| P18 | Empleados | [empleados.html](empleados.html) | F25, F26, F27 | RN03 | Empleado | DEFINIDA + MAQUETADA |
| P19 | Usuarios | [usuarios.html](usuarios.html) | F28 | RN03 | Usuario Empleado | DEFINIDA + MAQUETADA |
| P20 | Asistencia | [asistencia.html](asistencia.html) | F29, F30 | RN05, RN06 | Asistencia Empleado | DEFINIDA + MAQUETADA |

[Fichas completas de P01–P20](documentacion/03_interfaces.md).

## 9. Funcionalidades
| ID | Funcionalidad | Módulo | Interfaz | Estado |
|---|---|---|---|---|
| F01 | Iniciar sesión | Acceso | P02 | DEFINIDA + MAQUETADA |
| F02 | Cerrar sesión | Acceso | P03 | DEFINIDA + MAQUETADA |
| F03 | Consultar dashboard | Dashboard | P03 | DEFINIDA + MAQUETADA |
| F04 | Registrar categoría | Categorías | P05 | DEFINIDA + MAQUETADA |
| F05 | Consultar categorías | Categorías | P05 | DEFINIDA + MAQUETADA |
| F06 | Editar categoría | Categorías | P05 | DEFINIDA + MAQUETADA |
| F07 | Activar/desactivar categoría | Categorías | P05 | DEFINIDA + MAQUETADA |
| F08 | Registrar combustible | Combustibles | P07 | DEFINIDA + MAQUETADA |
| F09 | Consultar combustibles | Combustibles | P06 | DEFINIDA + MAQUETADA |
| F10 | Editar combustible | Combustibles | P07 | DEFINIDA + MAQUETADA |
| F11 | Activar/desactivar combustible | Combustibles | P06 | DEFINIDA + MAQUETADA |
| F12 | Consultar existencias | Inventario | P08, P11, P13 | DEFINIDA + MAQUETADA |
| F13 | Registrar entrada de combustible | Inventario | P12 | DEFINIDA + MAQUETADA |
| F14 | Registrar salida de combustible | Inventario | P13 | DEFINIDA + MAQUETADA |
| F15 | Consultar movimientos de inventario | Inventario | P14 | DEFINIDA + MAQUETADA |
| F16 | Registrar venta | Ventas | P08 | DEFINIDA + MAQUETADA |
| F17 | Consultar ventas | Ventas | P09 | DEFINIDA + MAQUETADA |
| F18 | Consultar detalle de venta | Ventas | P10 | DEFINIDA + MAQUETADA |
| F19 | Registrar concepto económico | Finanzas | P17 | DEFINIDA + MAQUETADA |
| F20 | Consultar conceptos económicos | Finanzas | P17 | DEFINIDA + MAQUETADA |
| F21 | Editar/activar/desactivar concepto económico | Finanzas | P17 | DEFINIDA + MAQUETADA |
| F22 | Registrar ingreso económico | Finanzas | P16 | DEFINIDA + MAQUETADA |
| F23 | Registrar egreso económico | Finanzas | P16 | DEFINIDA + MAQUETADA |
| F24 | Consultar movimientos económicos | Finanzas | P15 | DEFINIDA + MAQUETADA |
| F25 | Registrar empleado | Empleados | P18 | DEFINIDA + MAQUETADA |
| F26 | Consultar empleados | Empleados | P18 | DEFINIDA + MAQUETADA |
| F27 | Editar/activar/desactivar empleado | Empleados | P18 | DEFINIDA + MAQUETADA |
| F28 | Gestionar usuarios | Usuarios | P19 | DEFINIDA + MAQUETADA |
| F29 | Registrar entrada de asistencia | Asistencia | P20 | DEFINIDA + MAQUETADA |
| F30 | Registrar salida y consultar historial de asistencia | Asistencia | P20 | DEFINIDA + MAQUETADA |

[Fichas completas de F01–F30](documentacion/04_funcionalidades.md).

## 10. Reglas de negocio
- RN01: **Existencia suficiente**. No se puede vender ni retirar más combustible que el stock disponible.
- RN02: **Combustible activo**. Solo combustibles activos pueden utilizarse en nuevas ventas.
- RN03: **Conservación del historial**. Los registros con historial asociado no deben eliminarse físicamente; deben cambiar de estado.
- RN04: **Venta e ingreso económico**. Cada venta confirmada debe generar un único ingreso económico asociado.
- RN05: **Una asistencia abierta**. Un empleado no puede marcar una nueva entrada si todavía tiene una asistencia sin salida.
- RN06: **Orden temporal**. La hora de salida debe ser posterior a la hora de entrada.

[Reglas completas y casos de validación](documentacion/05_reglas_negocio.md). No se validan en esta etapa.

## 11. Estructura de archivos
```text
ing-web/
├── README.md
├── .gitignore
├── index.html
├── login.html
├── dashboard.html
├── publicidad.html
├── contacto.html
├── categorias.html
├── combustibles.html
├── combustible-form.html
├── ventas.html
├── ventas-historial.html
├── venta-detalle.html
├── inventario.html
├── inventario-entrada.html
├── inventario-salida.html
├── inventario-movimientos.html
├── finanzas.html
├── movimiento-economico.html
├── conceptos.html
├── empleados.html
├── usuarios.html
├── asistencia.html
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
    └── 08_puntos_1_al_5_8.md
```

## 12. Navegación
Abrir [index.html](index.html) → **Ingresar al sistema** → [login.html](login.html) → **Iniciar sesión** → [dashboard.html](dashboard.html). Los campos de acceso no se envían como parámetros y no existe autenticación. **Cerrar sesión** vuelve a login. Inicio vuelve a la página pública.
La navegación interna reúne los módulos sin dropdowns. Ventas enlaza su historial y detalles; inventario ofrece entrada, salida y movimientos; finanzas ofrece movimiento y conceptos. Los formularios de edición se alcanzan por anclas. No hay controles de filtrado que prometan funciones inexistentes.

## 13. Estado del avance
- **DEFINIDA:** 30/30 funcionalidades, 20/20 interfaces y 6/6 reglas documentadas.
- **MAQUETADA:** 20/20 interfaces (100 %) y representación visual de las 30 capacidades. Los botones de mutación son visuales.
- **IMPLEMENTADA:** 0/30 funcionalidades con lógica de negocio real. La navegación estática funciona, pero no autentica, guarda, calcula ni valida reglas.

[Auditoría y alcance de comprobaciones](documentacion/00_auditoria.md) · [Matriz](documentacion/06_matriz_funcionalidades_interfaces.md) · [Trazabilidad](documentacion/07_trazabilidad.md).

## 14. Cómo ejecutar
Abrir index.html en un navegador mediante doble clic. También se puede abrir la carpeta con VS Code y usar Live Server sobre index.html. Live Server está configurado en el puerto **2222** mediante `.vscode/settings.json`: acceder a `http://localhost:2222/index.html`. La configuración de depuración de Chrome usa el mismo puerto; primero iniciar Live Server. Si ya estaba iniciado, detenerlo y volverlo a iniciar para aplicar el cambio. No se requiere instalar paquetes, compilar, iniciar Node.js ni configurar una base de datos. Los enlaces y CSS propios usan rutas relativas.

## 15. Limitaciones
Los datos son fijos. No hay autenticación, autorización, cálculos dinámicos, envío de contacto, persistencia, búsqueda ni aplicación automática de reglas. Cambiar un selector no carga otro registro ni recalcula importes. Los gráficos son HTML/CSS con valores escritos. Jornadas de asistencia dentro de un mismo día. Bootstrap vía CDN requiere conexión; la CSS propia ofrece una presentación de respaldo.

## 16. Próxima etapa con Spring Boot
Crear entidades JPA, repositorios, servicios transaccionales, controladores y vistas integradas. Incorporar base de datos y migraciones, Bean Validation, Spring Security, hash de contraseñas, roles, protección CSRF y sesiones. Implementar RN01–RN06, control de concurrencia en stock y asistencia, importes decimales exactos, unicidad de ingreso por venta e idempotencia. Integrar formularios con respuestas de éxito/error y consultas reales. Probar las reglas con casos válidos, inválidos y operaciones concurrentes. Decidir el tratamiento de jornadas nocturnas y el vínculo estructurado entre venta y movimiento físico antes de ampliar el modelo.

## 17. Guía breve para exposición
1. Presentar problema, objetivo y alcance estático del grupo G1.
2. Recorrer Inicio → Login → Dashboard y explicar sus cinco KPI y cinco gráficos.
3. Mostrar V001: 10 L × S/ 5.00 = S/ 50.00; encontrar MI004 y MC001. Distinguir litros y soles.
4. Mostrar la alerta de Premium y un formulario de abastecimiento sin confundirlo con un ingreso de dinero.
5. Explicar empleados frente a usuarios, dos asistencias abiertas y una cerrada.
6. Presentar las diez entidades, las cardinalidades y las seis reglas con un ejemplo válido e inválido.
7. Enseñar la matriz F/P y la trazabilidad; cerrar explicando lo que implementará Spring Boot.

Documento académico: [Puntos 1 al 5.8](documentacion/08_puntos_1_al_5_8.md). Pendiente completar integrantes y evidencias con fuentes; no se inventaron nombres del grupo ni estadísticas.
