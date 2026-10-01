# 5.13 · Glosario

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.13 Glosario** — «Listado de términos técnicos o nuevos que requieren definición». Se usan sólo términos que realmente aparecen en este proyecto y en su documentación, ordenados por prioridad: dominio de la estación de servicio, modelo de datos, ingeniería web, y vocabulario específico de esta documentación. No se añadieron términos para aumentar la cantidad.

> **Formato:** la estructura oficial no exige plantilla de glosario; al compilar el informe final este listado debe volcarse en el formato general del documento (hoja A4, Arial 11, interlineado simple — sección 6 de la estructura oficial).

---

## A. Términos del dominio · estación de servicio

| Término | Definición en este proyecto |
|---|---|
| Abastecimiento | Compra de combustible a un proveedor para reponer el tanque. Es el proceso de soporte del BPMN (`POOL-SOPORTE`, inicio `S3`). |
| Combustible | Producto vendible medido en litros. En la maqueta: Gasolina Regular (PR01), Gasolina Premium (PR02) y Diésel (PR03). |
| Existencias (stock) | Saldo físico en litros de un combustible, atributo `Producto.stock`. RN01 impide vender o retirar por encima de él. |
| Litro | Unidad de medida del inventario (`Producto.unidad_medida`). El inventario se contabiliza en litros; las finanzas, en soles. |
| Surtidor | Punto de despacho de combustible al cliente. Da inicio al proceso núcleo del BPMN (`S1`). |
| Tanque | Depósito físico que almacena el combustible; el inventario representa su estado en litros. |
| Proveedor | Persona o empresa que entrega la factura de la compra. En el BPMN es el participante externo `POOL-PROV` que envía el mensaje `M1`. No es una entidad del modelo: es el atributo de texto `Compra.proveedor` (por ejemplo, Petroandes S.A.). |
| Precio de referencia | Precio que la OSINERGMIN publica con periodicidad fija como referencia de la banda minorista. Fuente central de la variable económica del diagnóstico (5.3). |
| Margen | Diferencia entre el precio de compra y el precio de venta por litro. Es la utilidad que la estación necesita calcular y que una hoja de cálculo aislada no conecta con las existencias. |
| Apertura de caja | Saldo con el que inicia el día la caja (S/ 4,410.00 en el corte). Base de la conciliación `apertura + ingresos − egresos = saldo`. |
| Ingreso | Movimiento de caja que aumenta el saldo. Nace automáticamente de cada venta (RN05) o se registra manualmente con F26. |
| Egreso | Movimiento de caja que disminuye el saldo. Nace automáticamente de cada compra (RN04) o se registra manualmente con F27. |
| Saldo de caja | Resultado de la conciliación del día: S/ 3,425.00 en el corte. Es también la quinta serie temporal del dashboard. |
| Conciliación de caja | F28 (interfaz P15): cotejo de apertura, ingresos y egresos del día para explicar el saldo. |
| Rendición de caja | Informe con el que el operador rinde su jornada. Es el argumento de la variable social del diagnóstico: un registro único por turno la hace auditable. |
| Corte (de datos) | Fecha y hora de referencia de todos los datos ficticios: **10/09/2026, 12:00**. Ningún dato posterior existe en la maqueta. |

## B. Términos del modelo de datos

| Término | Definición en este proyecto |
|---|---|
| Entidad | Objeto del negocio con identidad propia y tabla asociada. Hay 11 en el modelo (`Categoria` … `MovimientoCaja`). |
| Atributo | Propiedad de una entidad. El diccionario completo reúne 67 atributos (punto 5.10). |
| PK (clave primaria) | Atributo que identifica de forma única cada registro, por ejemplo `Venta.id_venta`. |
| FK (clave foránea) | Atributo que referencia a otra entidad, por ejemplo `DetalleVenta.id_venta`. Hay 14 en el modelo. |
| Cardinalidad | Número posible de participaciones en una relación: `1:N` (una categoría, muchos productos) y `1:0..1` (una venta, cero o un movimiento de caja). |
| Modelo entidad-relación (ER) | Representación conceptual de entidades, atributos y relaciones con sus claves. Entregable del punto 5.10, en Mermaid. |
| Diccionario de datos | Semántica de cada entidad y de cada uno de sus atributos, con tipo y dominio. Entregable del punto 5.10. |
| Historial | Conjunto de registros que referencian a un catálogo (por ejemplo, ventas que citan un producto). Obliga a desactivar en vez de eliminar: es RN03. |
| Transacción | Operación que se confirma completa o se revierte entera. Es el diseño previsto para F13 (compra) y F20 (venta) en la etapa Spring Boot. |
| Idempotencia | Propiedad de que repetir la misma operación no altera el resultado. La exigen RN04 (un solo egreso por compra) y RN05 (un solo ingreso por venta). |
| Estado (del registro) | Situación de un registro en su ciclo de vida: `Activo / Inactivo` en catálogos; `Pendiente / Confirmada` en `Compra` y `Venta`. |

## C. Términos de ingeniería web y de la documentación

| Término | Definición en este proyecto |
|---|---|
| Funcionalidad | Unidad funcional completa que permite cumplir un objetivo de negocio de principio a fin (definición oficial del punto 5.8). Hay 32: F01–F32. |
| Interfaz | Pantalla del sistema que representa funcionalidades. Hay 21: P01–P21 (22 archivos HTML; `publicidad.html` es la segunda presentación de P01). |
| Regla de negocio | Declaración formal, atómica, declarativa, estable y obligatoria que restringe el negocio (punto 5.9). Hay 6: RN01–RN06. |
| Caso de cumplimiento | Ejemplo concreto en que la regla se respeta, por ejemplo «vender 10 L con 1,990 L disponibles» en RN01. |
| Caso de violación | Ejemplo concreto en que la regla se quiebra, por ejemplo «retirar 2,000 L con 1,990 L disponibles» en RN01. |
| Matriz bidimensional | Tabla de dos dimensiones —funcionalidades × interfaces— exigida por el punto 5.7: 32 × 21 con 34 pares marcados. |
| Trazabilidad | Cadena que enlaza modelo de negocio → regla → funcionalidad → interfaz → entidad → archivo HTML sin contradicciones. |
| Maqueta (prototipo) | Representación navegable hecha sólo con HTML5 y CSS3: sin lógica, sin persistencia y sin JavaScript. |
| BPMN | Notación de procesos usada aquí en estándar estricto 2.0. Vocabulario asociado del proyecto: pool (participante), carril (lane), tarea, evento de inicio/fin, gateway exclusivo, flujo de secuencia, flujo de mensajes, call activity y subproceso. |
| Caso de uso | Interacción entre un actor y el sistema que entrega un resultado observable. En el punto 5.10 los casos de uso son funcionalidades existentes (se identifican por su código `Fnn`). |
| Actor | Rol que participa en los casos de uso: `Operador / Vendedor`, `Administrador` y `Proveedor` (roles de `01_modelo_negocio.md`). |
| Diagrama de secuencia | Diagrama de comportamiento que muestra los mensajes entre participantes en orden cronológico. Entregable del proceso núcleo y de soporte en el punto 5.10. |
| Diagrama como código | Diagrama escrito como texto y renderizado por herramienta, sin dibujo manual. Aquí: Mermaid (ER, BPMN, secuencia) y PlantUML (casos de uso), como pide el punto 5.10. |
| KPI | Indicador de estado puntual. El dashboard tiene 5: ventas del día, existencias, ingresos, egresos y saldo de caja. |
| Serie de tiempo | Métrica con eje X temporal. El dashboard tiene 5, todas del 04 al 10 de septiembre de 2026. |
| CDN | Red de distribución de contenidos. Por ella se carga el CSS de Bootstrap 5.3.3 (`cdn.jsdelivr.net`); no se descargó copia local. |
| Bootstrap | Framework CSS usado únicamente para estilos. Su JavaScript no se incluye: el proyecto no contiene ningún `<script>`. |
| Spring Boot | Framework Java de la etapa posterior (persistencia, validación de RN01–RN06, seguridad). Fuera del alcance de esta maqueta. |

## D. Vocabulario específico de esta documentación

| Término | Definición en este proyecto |
|---|---|
| DEFINIDA / MAQUETADA / IMPLEMENTADA | Estados de avance de cada requisito: documentado / representado en HTML / con lógica real. Hoy: 32 DEFINIDAS + MAQUETADAS, 0 IMPLEMENTADAS. |
| Módulo funcional | Agrupación de interfaces y funcionalidades. Los diez son Acceso, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados y Usuarios. |
| Partes estáticas | Las dos páginas exigidas sin funcionalidad de negocio: Publicidad (P01) y Contacto (P04). |
| Par F×P | Cada relación marcada en la matriz entre una funcionalidad y una interfaz. Hay 34. |
| Proceso núcleo / proceso de soporte | Los dos procesos del BPMN: venta de combustible (`POOL-CORE`) y compra/abastecimiento (`POOL-SOPORTE`). |
| Datos ficticios | Nombres, importes y volúmenes inventados con fines académicos; nada corresponde a una estación real. |

---

**Total de términos: 51** (16 de dominio, 11 de modelo, 18 de ingeniería web y 6 específicos del proyecto). Todos aparecen en los HTML o en la documentación del repositorio.
