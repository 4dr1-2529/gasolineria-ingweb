# Proyecto académico · Ingeniería Web · G1

## 1. Fundamentación
Una estación de servicio necesita relacionar combustible disponible, ventas realizadas, movimientos de dinero y presencia del personal. Esta propuesta organiza esos procesos en una maqueta web que permite discutir la estructura de información y los recorridos antes de construir el backend. Se parte de los requisitos académicos proporcionados; no se atribuyen resultados a una investigación empírica.

## 2. Objetivo
Crear una maqueta web académica profesional, completa, ordenada, responsive, fácil de explicar en exposición y preparada para una futura implementación con Spring Boot.

## 3. Integrantes
[PENDIENTE: completar integrantes del G1]

## 4. Especificación y alcance
Sistema de venta de gasolina, manejo de inventario, transacciones de ingreso y egreso y marcación de asistencia del personal. Incluye 12 módulos, 20 interfaces, 30 funcionalidades definidas, 6 reglas y 10 entidades. La tecnología actual es HTML5, CSS3 y Bootstrap CSS. Se excluyen JavaScript, backend, base de datos, APIs, autenticación real y persistencia. Inicio / Publicidad utiliza dos archivos para la misma interfaz P01.

## 5.1 Resumen
Estación Nexo presenta una experiencia navegable desde el acceso de demostración hasta ventas, existencias, caja y asistencia. Los formularios y acciones muestran los datos que serán procesados en la siguiente etapa. La documentación conecta el negocio con las entidades, funcionalidades, reglas e interfaces.

## 5.2 Introducción
La Ingeniería Web permite estructurar interfaces y modelos antes de implementar servicios. Esta propuesta separa los litros físicos del dinero y distingue la identidad laboral de la cuenta de acceso. Con esas decisiones, la maqueta facilita revisar los procesos con un alcance controlado.

## 5.3 Diagnóstico
Como hipótesis de trabajo, una operación con información dispersa puede dificultar la conciliación entre ventas, stock y caja y el seguimiento de jornadas. Esta hipótesis no constituye un hallazgo medido sobre una estación real. Se requiere incorporar evidencia antes de formular conclusiones empíricas.

[PENDIENTE: incorporar evidencia estadística con fuente]

[PENDIENTE: incorporar evidencia legal/noticiosa]

[PENDIENTE: colocar cita al pie]

## 5.4 Objetivos
Objetivo general: representar los procesos de gestión de una estación de servicio en una maqueta web coherente y trazable.

Objetivos específicos:
- Organizar 20 interfaces accesibles mediante enlaces relativos.
- Definir 30 capacidades sin contar controles de formulario como funciones.
- Relacionar 10 entidades con claves y cardinalidades explícitas.
- Documentar 6 reglas con casos válidos e inválidos para su implementación posterior.
- Separar inventario físico, caja económica y asistencia del personal.
- Mantener coherencia de datos y adaptación CSS a escritorio, tablet y móvil.

## 5.5 Justificación
La maqueta permite revisar estructura y navegación antes de invertir en lógica de servidor. Su valor académico reside en la trazabilidad: cada pantalla se vincula con un proceso, una entidad y las reglas pertinentes. El uso de HTML/CSS reduce dependencias y permite ejecutar los archivos directamente. No se presentan beneficios cuantificados sin evidencia.

## 5.6 Definición y alcance
Roles: Administrador, Operador / Vendedor y Empleado. Módulos: Inicio / Publicidad, Login, Dashboard, Categorías, Combustibles, Ventas, Inventario, Finanzas, Empleados, Usuarios, Asistencia y Contacto. Las acciones de negocio son DEFINIDAS + MAQUETADAS, no IMPLEMENTADAS. El acceso navega sin autenticar y los botones visuales no guardan datos. Spring Boot implementará las transacciones y validaciones posteriormente.

## 5.7 Interfaces
| ID | Interfaz | Archivo | Funcionalidades | Reglas | Entidades | Estado |
|---|---|---|---|---|---|---|
| P01 | Inicio / Publicidad | [index.html](../index.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P02 | Login | [login.html](../login.html) | F01 | No aplica | Usuario | DEFINIDA + MAQUETADA |
| P03 | Dashboard | [dashboard.html](../dashboard.html) | F02, F03 | No aplica | Venta Producto MovimientoCaja Asistencia | DEFINIDA + MAQUETADA |
| P04 | Contacto | [contacto.html](../contacto.html) | No aplica | No aplica | No aplica | DEFINIDA + MAQUETADA |
| P05 | Categorías | [categorias.html](../categorias.html) | F04, F05, F06, F07 | RN03 | Categoria | DEFINIDA + MAQUETADA |
| P06 | Combustibles | [combustibles.html](../combustibles.html) | F09, F11 | RN02, RN03 | Producto Categoria | DEFINIDA + MAQUETADA |
| P07 | Formulario combustible | [combustible-form.html](../combustible-form.html) | F08, F10 | RN02, RN03 | Producto Categoria | DEFINIDA + MAQUETADA |
| P08 | Registrar venta | [ventas.html](../ventas.html) | F12, F16 | RN01, RN02, RN04 | Venta DetalleVenta Producto Usuario MovimientoInventario MovimientoCaja | DEFINIDA + MAQUETADA |
| P09 | Historial de ventas | [ventas-historial.html](../ventas-historial.html) | F17 | No aplica | Venta DetalleVenta Producto Usuario | DEFINIDA + MAQUETADA |
| P10 | Detalle de venta | [venta-detalle.html](../venta-detalle.html) | F18 | RN04 | Venta DetalleVenta Producto Usuario MovimientoCaja | DEFINIDA + MAQUETADA |
| P11 | Existencias | [inventario.html](../inventario.html) | F12 | RN01 | Producto | DEFINIDA + MAQUETADA |
| P12 | Entrada combustible | [inventario-entrada.html](../inventario-entrada.html) | F13 | No aplica | Producto Usuario MovimientoInventario | DEFINIDA + MAQUETADA |
| P13 | Salida combustible | [inventario-salida.html](../inventario-salida.html) | F12, F14 | RN01 | Producto Usuario MovimientoInventario | DEFINIDA + MAQUETADA |
| P14 | Movimientos inventario | [inventario-movimientos.html](../inventario-movimientos.html) | F15 | No aplica | MovimientoInventario Producto Usuario | DEFINIDA + MAQUETADA |
| P15 | Resumen financiero | [finanzas.html](../finanzas.html) | F24 | RN04 | MovimientoCaja ConceptoMovimiento Venta | DEFINIDA + MAQUETADA |
| P16 | Movimiento económico | [movimiento-economico.html](../movimiento-economico.html) | F22, F23 | RN04 | MovimientoCaja ConceptoMovimiento Usuario Venta | DEFINIDA + MAQUETADA |
| P17 | Conceptos económicos | [conceptos.html](../conceptos.html) | F19, F20, F21 | RN03 | ConceptoMovimiento | DEFINIDA + MAQUETADA |
| P18 | Empleados | [empleados.html](../empleados.html) | F25, F26, F27 | RN03 | Empleado | DEFINIDA + MAQUETADA |
| P19 | Usuarios | [usuarios.html](../usuarios.html) | F28 | RN03 | Usuario Empleado | DEFINIDA + MAQUETADA |
| P20 | Asistencia | [asistencia.html](../asistencia.html) | F29, F30 | RN05, RN06 | Asistencia Empleado | DEFINIDA + MAQUETADA |

La ficha completa de cada interfaz está en [03_interfaces.md](03_interfaces.md).

## 5.8 Funcionalidades
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

Los procesos y resultados se detallan en [04_funcionalidades.md](04_funcionalidades.md). La matriz está en [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md).
