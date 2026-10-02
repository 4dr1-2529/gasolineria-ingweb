# Sistema Web de Gestión para Estación de Servicio - G1

## 1. Descripción
**Estación Nexo** es una maqueta académica del curso Ingeniería Web, grupo G1. Presenta una estación de servicio con identidad visual propia en verde petróleo y lima, navegación consistente y datos ficticios relacionados.

## 2. Tema
Venta de combustibles organizados por categoría y producto, compra y abastecimiento al proveedor, control de inventario en litros, registro de ventas, ingresos y egresos en soles, tablero de control y gestión de empleados y usuarios.

## 3. Alcance
21 interfaces oficiales (P01–P21) en 22 HTML, 32 funcionalidades definidas y representadas, 6 reglas de negocio y 11 entidades. `publicidad.html` es otra presentación de P01. No hay backend ni persistencia. El informe cubre **5.1–5.15 con sus pendientes visibles** (nada oculto ni por resolver inventado): **5.1–5.9 documentados sin pendientes**; **5.10 documentado, parcial** — capturas reales de los patrones revisados en clase (*requiere evidencia*) y fechas reales del cronograma pendientes; **5.11 y 5.12 documentados** — pendiente la validación de la redacción por el equipo; **5.13 documentado** — pendiente volcarlo a la plantilla A4/Arial 11 al compilar el informe; **5.14 documentado, parcial** — las dos obras obligatorias (Coronel/Morris/Rob; Cervantes Maceda, Velasco-Elizondo y Castro Careaga) siguen **pendientes de consulta**; **5.15 documentado**. Los documentos son [05_reglas_negocio.md](documentacion/05_reglas_negocio.md) (5.9), [10_productos_y_entregables.md](documentacion/10_productos_y_entregables.md), [11_conclusiones.md](documentacion/11_conclusiones.md), [12_recomendaciones.md](documentacion/12_recomendaciones.md), [13_glosario.md](documentacion/13_glosario.md), [14_bibliografia.md](documentacion/14_bibliografia.md) (5.10–5.14) y [15_anexos.md](documentacion/15_anexos.md) (5.15), junto con el BPMN de los procesos núcleo y de soporte y la matriz 32 × 21. Los pendientes de decisión humana están clasificados y marcados en [08_puntos_1_al_5_8.md](documentacion/08_puntos_1_al_5_8.md) y se reflejan en la sección 10 de [10_productos_y_entregables.md](documentacion/10_productos_y_entregables.md).

## 4. Módulos
Inicio / Publicidad · Login · Dashboard · Categorías · Combustibles · Compras · Inventario · Ventas · Finanzas · Empleados · Usuarios · Contacto.

Los diez módulos funcionales son: Acceso, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados y Usuarios. Publicidad y Contacto son las dos partes estáticas exigidas por la especificación oficial.

## 5. Tecnologías
HTML5, CSS3 y Bootstrap CSS 5.3.3 mediante CDN. Sin Bootstrap JS, JavaScript, TypeScript, frameworks, Node.js, APIs ni base de datos. La hoja propia incluye estilos base para mantener la presentación si el CDN no está disponible; descargar Bootstrap desde CDN necesita conexión.

## 6. Modelo de negocio
Inicio → Login → Dashboard → seleccionar combustible → verificar existencias y estado → representar venta y detalle → salida física de inventario → ingreso económico único → historial → conciliación de caja.
El abastecimiento sigue el camino inverso: Compras → confirmación de la compra → entradas por línea → **un único** egreso por el importe total.
Inventario se expresa en litros; finanzas en soles. Las operaciones se representan visualmente, sin ejecutarse. El detalle y la conciliación están en [Modelo de negocio](documentacion/01_modelo_negocio.md) y los flujos en [BPMN](documentacion/09_bpmn.md).

Datos de corte al 10/09/2026, 12:00: compra `C001` de 300 L / S/ 1,350.00; 3 ventas / 80 L / S/ 370.00; existencias 6,920 L; ingresos de hoy S/ 415.00; egresos de hoy S/ 1,400.00; apertura de caja S/ 4,410.00; saldo S/ 3,425.00. Formularios nuevos son escenarios separados y no alteran el corte.

## 7. Modelo ER
Categoria, Producto, Compra, DetalleCompra, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento y MovimientoCaja. [Atributos, claves, cardinalidades y Mermaid ER](documentacion/02_modelo_entidad_relacion.md).

## 8. Interfaces
| ID | Interfaz | Archivo | Funcionalidades | Reglas | Entidades | Estado |
|---|---|---|---|---|---|---|
| P01 | Inicio / Publicidad | [index.html](index.html), [publicidad.html](publicidad.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P02 | Login | [login.html](login.html) | F01 | No aplica | Usuario | DEFINIDA + MAQUETADA |
| P03 | Dashboard | [dashboard.html](dashboard.html) | F02, F03 | No aplica | Venta DetalleVenta Producto MovimientoCaja | DEFINIDA + MAQUETADA |
| P04 | Contacto | [contacto.html](contacto.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P05 | Categorías | [categorias.html](categorias.html) | F04, F05, F06, F07, F08 | RN03 | Categoria | DEFINIDA + MAQUETADA |
| P06 | Combustibles | [combustibles.html](combustibles.html) | F10, F12 | RN02, RN03 | Producto Categoria | DEFINIDA + MAQUETADA |
| P07 | Formulario combustible | [combustible-form.html](combustible-form.html) | F09, F11 | RN02, RN03, RN06 | Producto Categoria | DEFINIDA + MAQUETADA |
| P08 | Registrar venta | [ventas.html](ventas.html) | F18, F20 | RN01, RN02, RN05, RN06 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |
| P09 | Historial de ventas | [ventas-historial.html](ventas-historial.html) | F21 | RN05 | Venta DetalleVenta Producto Usuario MovimientoCaja | DEFINIDA + MAQUETADA |
| P10 | Detalle de venta | [venta-detalle.html](venta-detalle.html) | F22 | RN05 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |
| P11 | Existencias | [inventario.html](inventario.html) | F18 | RN01 | Producto | DEFINIDA + MAQUETADA |
| P12 | Entrada combustible | [inventario-entrada.html](inventario-entrada.html) | F16 | RN04, RN06 | MovimientoInventario Producto Usuario Compra | DEFINIDA + MAQUETADA |
| P13 | Salida combustible | [inventario-salida.html](inventario-salida.html) | F17, F18 | RN01, RN06 | MovimientoInventario Producto Usuario | DEFINIDA + MAQUETADA |
| P14 | Movimientos inventario | [inventario-movimientos.html](inventario-movimientos.html) | F19 | RN01, RN04 | MovimientoInventario Producto Usuario Compra | DEFINIDA + MAQUETADA |
| P15 | Resumen financiero | [finanzas.html](finanzas.html) | F28 | RN04, RN05 | MovimientoCaja ConceptoMovimiento Venta Compra | DEFINIDA + MAQUETADA |
| P16 | Movimiento económico | [movimiento-economico.html](movimiento-economico.html) | F26, F27 | RN06 | MovimientoCaja ConceptoMovimiento | DEFINIDA + MAQUETADA |
| P17 | Conceptos económicos | [conceptos.html](conceptos.html) | F23, F24, F25 | RN03 | ConceptoMovimiento | DEFINIDA + MAQUETADA |
| P18 | Empleados | [empleados.html](empleados.html) | F29, F30, F31 | RN03 | Empleado | DEFINIDA + MAQUETADA |
| P19 | Usuarios | [usuarios.html](usuarios.html) | F32 | RN03 | Usuario Empleado | DEFINIDA + MAQUETADA |
| P20 | Compras | [compras.html](compras.html) | F13, F14 | RN04, RN06 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |
| P21 | Detalle de compra | [compra-detalle.html](compra-detalle.html) | F15 | RN04 | Compra DetalleCompra Producto MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |

[Fichas completas de P01–P21](documentacion/03_interfaces.md) · [Matriz 32 × 21](documentacion/06_matriz_funcionalidades_interfaces.md).

## 9. Funcionalidades
| ID | Funcionalidad | Módulo | Interfaz | Reglas | Estado |
|---|---|---|---|---|---|
| F01 | Iniciar sesión | Acceso | P02 | No aplica | DEFINIDA + MAQUETADA |
| F02 | Cerrar sesión | Acceso | P03 | No aplica | DEFINIDA + MAQUETADA |
| F03 | Consultar dashboard | Dashboard | P03 | No aplica | DEFINIDA + MAQUETADA |
| F04 | Registrar categoría | Categorías | P05 | No aplica | DEFINIDA + MAQUETADA |
| F05 | Consultar categorías | Categorías | P05 | No aplica | DEFINIDA + MAQUETADA |
| F06 | Consultar combustibles por categoría | Categorías | P05 | No aplica | DEFINIDA + MAQUETADA |
| F07 | Editar categoría | Categorías | P05 | RN03 | DEFINIDA + MAQUETADA |
| F08 | Activar/desactivar categoría | Categorías | P05 | RN03 | DEFINIDA + MAQUETADA |
| F09 | Registrar combustible | Combustibles | P07 | RN02, RN06 | DEFINIDA + MAQUETADA |
| F10 | Consultar combustibles | Combustibles | P06 | RN02 | DEFINIDA + MAQUETADA |
| F11 | Editar combustible | Combustibles | P07 | RN02, RN03, RN06 | DEFINIDA + MAQUETADA |
| F12 | Activar/desactivar combustible | Combustibles | P06 | RN02, RN03 | DEFINIDA + MAQUETADA |
| F13 | Registrar compra de combustible | Compras | P20 | RN04, RN06 | DEFINIDA + MAQUETADA |
| F14 | Consultar compras | Compras | P20 | RN04 | DEFINIDA + MAQUETADA |
| F15 | Consultar detalle de compra | Compras | P21 | RN04 | DEFINIDA + MAQUETADA |
| F16 | Registrar entrada de combustible | Inventario | P12 | RN04, RN06 | DEFINIDA + MAQUETADA |
| F17 | Registrar salida de combustible | Inventario | P13 | RN01, RN06 | DEFINIDA + MAQUETADA |
| F18 | Consultar existencias | Inventario | P08, P11, P13 | RN01 | DEFINIDA + MAQUETADA |
| F19 | Consultar movimientos de inventario | Inventario | P14 | RN01, RN04 | DEFINIDA + MAQUETADA |
| F20 | Registrar venta | Ventas | P08 | RN01, RN02, RN05, RN06 | DEFINIDA + MAQUETADA |
| F21 | Consultar ventas | Ventas | P09 | RN05 | DEFINIDA + MAQUETADA |
| F22 | Consultar detalle de venta | Ventas | P10 | RN05 | DEFINIDA + MAQUETADA |
| F23 | Registrar concepto económico | Finanzas | P17 | No aplica | DEFINIDA + MAQUETADA |
| F24 | Consultar conceptos económicos | Finanzas | P17 | No aplica | DEFINIDA + MAQUETADA |
| F25 | Editar/activar/desactivar concepto económico | Finanzas | P17 | RN03 | DEFINIDA + MAQUETADA |
| F26 | Registrar ingreso económico | Finanzas | P16 | RN06 | DEFINIDA + MAQUETADA |
| F27 | Registrar egreso económico | Finanzas | P16 | RN06 | DEFINIDA + MAQUETADA |
| F28 | Consultar movimientos y saldo de caja | Finanzas | P15 | RN04, RN05 | DEFINIDA + MAQUETADA |
| F29 | Registrar empleado | Empleados | P18 | No aplica | DEFINIDA + MAQUETADA |
| F30 | Consultar empleados | Empleados | P18 | No aplica | DEFINIDA + MAQUETADA |
| F31 | Editar/activar/desactivar empleado | Empleados | P18 | RN03 | DEFINIDA + MAQUETADA |
| F32 | Gestionar usuarios | Usuarios | P19 | RN03 | DEFINIDA + MAQUETADA |

[Fichas completas de F01–F32](documentacion/04_funcionalidades.md). Ninguna funcionalidad carece de interfaz; P01 y P04 son páginas públicas sin funcionalidades, excepción documentada.

## 10. Reglas de negocio
- RN01: **Existencia suficiente**. No se puede vender ni retirar más combustible que el stock disponible.
- RN02: **Combustible activo**. Solo combustibles activos pueden utilizarse en nuevas ventas.
- RN03: **Conservación del historial**. Los registros con historial asociado no deben eliminarse físicamente; deben cambiar de estado.
- RN04: **Compra y abastecimiento**. Cada compra confirmada genera exactamente una entrada de inventario por línea recibida y un único egreso económico por su importe total.
- RN05: **Venta e ingreso económico**. Cada venta confirmada debe generar un único ingreso económico asociado.
- RN06: **Valores válidos**. Las cantidades en litros y los precios en soles deben ser mayores que cero y coherentes con el catálogo.

[Reglas completas con condición, caso de cumplimiento y caso de violación](documentacion/05_reglas_negocio.md). No se validan en esta etapa.

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
├── compras.html
├── compra-detalle.html
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
Abrir [index.html](index.html) → **Ingresar al sistema** → [login.html](login.html) → **Iniciar sesión** → [dashboard.html](dashboard.html). Los campos de acceso no se envían como parámetros y no existe autenticación. **Cerrar sesión** vuelve a login. Inicio vuelve a la página pública.
La navegación interna reúne los módulos en este orden, sin dropdowns: Inicio, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados, Usuarios, Contacto, Cerrar sesión. Ventas enlaza su historial y detalles; inventario ofrece entrada, salida y movimientos; compras enlaza a su detalle. Los formularios de edición se alcanzan por anclas. No hay controles de filtrado que prometan funciones inexistentes.

## 13. Estado del avance
- **DEFINIDA:** 32/32 funcionalidades, 21/21 interfaces, 6/6 reglas y 11/11 entidades documentadas.
- **MAQUETADA:** 21/21 interfaces (100 %) y representación visual de las 32 capacidades. Los botones de mutación son visuales.
- **IMPLEMENTADA:** 0/32 funcionalidades con lógica de negocio real. La navegación estática funciona, pero no autentica, guarda, calcula ni valida reglas.

[Auditoría y alcance de comprobaciones](documentacion/00_auditoria.md) · [Matriz](documentacion/06_matriz_funcionalidades_interfaces.md) · [Trazabilidad](documentacion/07_trazabilidad.md) · [BPMN](documentacion/09_bpmn.md) · [Productos y entregables 5.10](documentacion/10_productos_y_entregables.md) · [Conclusiones 5.11](documentacion/11_conclusiones.md) · [Recomendaciones 5.12](documentacion/12_recomendaciones.md) · [Glosario 5.13](documentacion/13_glosario.md) · [Bibliografía 5.14](documentacion/14_bibliografia.md) · [Anexos 5.15](documentacion/15_anexos.md).

## 14. Cómo ejecutar
Abrir index.html en un navegador mediante doble clic. También se puede abrir la carpeta con VS Code y usar Live Server sobre index.html. Live Server está configurado en el puerto **2222** mediante `.vscode/settings.json`: acceder a `http://localhost:2222/index.html`. La configuración de depuración de Chrome usa el mismo puerto; primero iniciar Live Server. Si ya estaba iniciado, detenerlo y volverlo a iniciar para aplicar el cambio. No se requiere instalar paquetes, compilar, iniciar Node.js ni configurar una base de datos. Los enlaces y CSS propios usan rutas relativas.

## 15. Limitaciones
Los datos son fijos. No hay autenticación, autorización, cálculos dinámicos, envío de contacto, persistencia, búsqueda ni aplicación automática de reglas. Cambiar un selector no carga otro registro ni recalcula importes. Los gráficos son HTML/CSS con valores escritos y el eje X de los cinco gráficos es temporal (04–10 sep. 2026), no categórico. Las acciones sobre categorías, productos, compras, empleados y usuarios son visuales. Bootstrap vía CDN requiere conexión; la CSS propia ofrece una presentación de respaldo.

## 16. Próxima etapa con Spring Boot
Crear entidades JPA, repositorios, servicios transaccionales, controladores y vistas integradas. Incorporar base de datos y migraciones, Bean Validation, Spring Security, hash de contraseñas, roles, protección CSRF y sesiones. Implementar RN01–RN06, control de concurrencia en stock, importes decimales exactos, unicidad de ingreso por venta y de egreso por compra, e idempotencia. Integrar formularios con respuestas de éxito/error y consultas reales. Probar las reglas con casos válidos, inválidos y operaciones concurrentes. Decidir el vínculo estructurado entre venta y movimiento físico antes de ampliar el modelo.

## 17. Guía breve para exposición
1. Presentar problema, objetivo y alcance estático del grupo G1.
2. Recorrer Inicio → Login → Dashboard y explicar sus cinco indicadores de estado puntual y sus cinco series temporales.
3. Mostrar V001: 10 L × S/ 5.00 = S/ 50.00; encontrar MI004 y MC003. Distinguir litros y soles.
4. Mostrar la compra C001 (300 L / S/ 1,350.00): tres entradas MI001–MI003 y **un único** egreso MC001; explicar RN04.
5. Explicar empleados frente a usuarios y el rol de la cuenta.
6. Presentar las once entidades, las cardinalidades y las seis reglas con un ejemplo de cumplimiento y uno de violación.
7. Enseñar la matriz F/P, la trazabilidad y el BPMN; cerrar explicando lo que implementará Spring Boot.

Documento académico: [Puntos 1 al 5.8](documentacion/08_puntos_1_al_5_8.md) · [BPMN](documentacion/09_bpmn.md) · [Puntos 5.9 a 5.14](documentacion/10_productos_y_entregables.md) · [Anexos 5.15](documentacion/15_anexos.md). Pendientes de decisión humana: integrantes, anexo oficial del proceso, capturas de patrones y fechas del cronograma (5.10), obras bibliográficas obligatorias (5.14) y validación del equipo sobre conclusiones, recomendaciones y glosario; no se inventaron nombres del grupo ni estadísticas.
