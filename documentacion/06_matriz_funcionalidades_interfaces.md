# Matriz funcionalidades / interfaces

X indica representación directa de la capacidad en la interfaz. Hay **32 funcionalidades** y **21 interfaces**; la relación es de 1 funcionalidad → ≥1 interfaz y de 1 interfaz → muchas funcionalidades. P01 (Inicio/Publicidad) y P04 (Contacto) son contenido público estático, no añaden funciones al catálogo oficial y por eso aparecen vacías. F02 figura en P03 como origen de referencia y está accesible también en toda la navegación interna.

| Funcionalidad | P01 | P02 | P03 | P04 | P05 | P06 | P07 | P08 | P09 | P10 | P11 | P12 | P13 | P14 | P15 | P16 | P17 | P18 | P19 | P20 | P21 |
|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|---|
| F01 Iniciar sesión |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F02 Cerrar sesión |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F03 Consultar dashboard |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F04 Registrar categoría |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F05 Consultar categorías |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F06 Consultar combustibles por categoría |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F07 Editar categoría |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F08 Activar/desactivar categoría |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F09 Registrar combustible |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F10 Consultar combustibles |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F11 Editar combustible |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F12 Activar/desactivar combustible |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F13 Registrar compra de combustible |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |
| F14 Consultar compras |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |
| F15 Consultar detalle de compra |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |
| F16 Registrar entrada de combustible |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |
| F17 Registrar salida de combustible |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |
| F18 Consultar existencias |  |  |  |  |  |  |  | X |  |  | X |  | X |  |  |  |  |  |  |  |  |
| F19 Consultar movimientos de inventario |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |
| F20 Registrar venta |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |  |
| F21 Consultar ventas |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |  |
| F22 Consultar detalle de venta |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |  |  |  |  |  |
| F23 Registrar concepto económico |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |
| F24 Consultar conceptos económicos |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |
| F25 Editar/activar/desactivar concepto económico |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |
| F26 Registrar ingreso económico |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |
| F27 Registrar egreso económico |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |
| F28 Consultar movimientos y saldo de caja |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |  |  |  |
| F29 Registrar empleado |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |
| F30 Consultar empleados |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |
| F31 Editar/activar/desactivar empleado |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |  |
| F32 Gestionar usuarios |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  |  | X |  |  |

## Cobertura por interfaz

| Interfaz | Funcionalidades representadas | Cantidad |
|---|---|---:|
| P01 | Contenido público sin funciones (decisión documentada) | 0 |
| P02 | F01 | 1 |
| P03 | F02, F03 | 2 |
| P04 | Contenido público sin funciones (decisión documentada) | 0 |
| P05 | F04, F05, F06, F07, F08 | 5 |
| P06 | F10, F12 | 2 |
| P07 | F09, F11 | 2 |
| P08 | F18, F20 | 2 |
| P09 | F21 | 1 |
| P10 | F22 | 1 |
| P11 | F18 | 1 |
| P12 | F16 | 1 |
| P13 | F17, F18 | 2 |
| P14 | F19 | 1 |
| P15 | F28 | 1 |
| P16 | F26, F27 | 2 |
| P17 | F23, F24, F25 | 3 |
| P18 | F29, F30, F31 | 3 |
| P19 | F32 | 1 |
| P20 | F13, F14 | 2 |
| P21 | F15 | 1 |

**Total de relaciones X: 34. Funcionalidades sin interfaz: 0. Interfaces sin funcionalidad (excluyendo P01 y P04, públicas): 0.**

## Cobertura por funcionalidad

| Funcionalidad | Módulo | Interfaces | Reglas |
|---|---|---|---|
| F01 Iniciar sesión | Acceso | P02 | No aplica |
| F02 Cerrar sesión | Acceso | P03 | No aplica |
| F03 Consultar dashboard | Dashboard | P03 | No aplica |
| F04 Registrar categoría | Categorías | P05 | No aplica |
| F05 Consultar categorías | Categorías | P05 | No aplica |
| F06 Consultar combustibles por categoría | Categorías | P05 | No aplica |
| F07 Editar categoría | Categorías | P05 | RN03 |
| F08 Activar/desactivar categoría | Categorías | P05 | RN03 |
| F09 Registrar combustible | Combustibles | P07 | RN02, RN06 |
| F10 Consultar combustibles | Combustibles | P06 | RN02 |
| F11 Editar combustible | Combustibles | P07 | RN02, RN03, RN06 |
| F12 Activar/desactivar combustible | Combustibles | P06 | RN02, RN03 |
| F13 Registrar compra de combustible | Compras | P20 | RN04, RN06 |
| F14 Consultar compras | Compras | P20 | RN04 |
| F15 Consultar detalle de compra | Compras | P21 | RN04 |
| F16 Registrar entrada de combustible | Inventario | P12 | RN04, RN06 |
| F17 Registrar salida de combustible | Inventario | P13 | RN01, RN06 |
| F18 Consultar existencias | Inventario | P08, P11, P13 | RN01 |
| F19 Consultar movimientos de inventario | Inventario | P14 | RN01, RN04 |
| F20 Registrar venta | Ventas | P08 | RN01, RN02, RN05, RN06 |
| F21 Consultar ventas | Ventas | P09 | RN05 |
| F22 Consultar detalle de venta | Ventas | P10 | RN05 |
| F23 Registrar concepto económico | Finanzas | P17 | No aplica |
| F24 Consultar conceptos económicos | Finanzas | P17 | No aplica |
| F25 Editar/activar/desactivar concepto económico | Finanzas | P17 | RN03 |
| F26 Registrar ingreso económico | Finanzas | P16 | RN06 |
| F27 Registrar egreso económico | Finanzas | P16 | RN06 |
| F28 Consultar movimientos y saldo de caja | Finanzas | P15 | RN04, RN05 |
| F29 Registrar empleado | Empleados | P18 | No aplica |
| F30 Consultar empleados | Empleados | P18 | No aplica |
| F31 Editar/activar/desactivar empleado | Empleados | P18 | RN03 |
| F32 Gestionar usuarios | Usuarios | P19 | RN03 |

**Reglas sin funcionalidad: 0. Diez funcionalidades carecen de regla porque son altas de registro nuevo, consultas o gestión sin restricción de negocio: F01, F02, F03, F04, F05, F06, F23, F24, F29 y F30.**
