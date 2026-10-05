# 5.13 · Glosario

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.13 Glosario** — «Listado de términos técnicos o nuevos que requieren definición». Se usan sólo términos que realmente aparecen en este proyecto y en su documentación, ordenados por prioridad: dominio de la estación de servicio, modelo de datos, ingeniería web, y vocabulario específico de esta documentación. No se añadieron términos para aumentar la cantidad.

> **Formato — [PENDIENTE — decisión humana]:** la estructura oficial no exige plantilla de glosario; al compilar el informe final este listado debe volcarse en el formato general del documento (hoja A4, Arial 11, interlineado simple — sección 6 de la estructura oficial). Clasificación oficial en [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md), ítem (f). Hasta ese volcado, este punto se considera **Entregado, no cerrado**.

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
| Ingreso | Movimiento de caja que aumenta el saldo. Nace automáticamente de cada venta (RN04, concepto CE01 «Venta de combustible») y se consulta con F26 en P16. |
| Egreso | Movimiento de caja que disminuye el saldo. Nace automáticamente de cada compra (RN04, concepto CE02 «Compra de combustible») y se consulta con F27 en P16. |
| Saldo de caja | Resultado de la conciliación del día: S/ 3,430.00 en el corte (apertura S/ 4,410.00 + ingresos S/ 370.00 − egresos S/ 1,350.00). Es también la quinta serie temporal del dashboard. |
| Conciliación de caja | F28 (interfaz P15 y P30): cotejo de apertura, ingresos y egresos del día para explicar el saldo. |
| Rendición de caja | Informe con el que el operador rinde su jornada. Es el argumento de la variable social del diagnóstico: un registro único por turno la hace auditable. |
| Corte (de datos) | Fecha y hora de referencia de todos los datos ficticios: **10/09/2026, 12:00**. Ningún dato posterior existe en la maqueta. |

## B. Términos del modelo de datos

| Término | Definición en este proyecto |
|---|---|
| Entidad | Objeto del negocio con identidad propia y tabla asociada. Hay 12 en el modelo (`Categoria` … `MovimientoCaja`, más `Asistencia`). |
| Atributo | Propiedad de una entidad. El diccionario completo reúne 74 atributos (punto 5.10). |
| PK (clave primaria) | Atributo que identifica de forma única cada registro, por ejemplo `Venta.id_venta`. |
| FK (clave foránea) | Atributo que referencia a otra entidad, por ejemplo `DetalleVenta.id_venta`. Hay 15 en el modelo. |
| Cardinalidad | Número posible de participaciones en una relación: `1:N` (una categoría, muchos productos) y `1:0..1` (una venta, cero o un movimiento de caja). |
| Modelo entidad-relación (ER) | Representación conceptual de entidades, atributos y relaciones con sus claves. Entregable del punto 5.10, en Mermaid. |
| Diccionario de datos | Semántica de cada entidad y de cada uno de sus atributos, con tipo y dominio. Entregable del punto 5.10. |
| Historial | Conjunto de registros que referencian a un catálogo (por ejemplo, ventas que citan un producto). Obliga a desactivar en vez de eliminar: es RN02. |
| Transacción | Operación que se confirma completa o se revierte entera. Es el diseño previsto para F13 (compra) y F20 (venta) en la etapa Spring Boot. |
| Idempotencia | Propiedad de que repetir la misma operación no altera el resultado. La exige RN04 (un solo egreso por compra y un solo ingreso por venta). |
| Estado (del registro) | Situación de un registro en su ciclo de vida: `Activo / Inactivo` en catálogos; `Pendiente / Confirmada` en `Compra` y `Venta`; `Presente / Falta` en `Asistencia` (calculado a partir de las horas, RN05). |
| Asistencia | Entidad de la jornada laboral: 7 atributos (`id_asistencia` PK, `id_empleado` FK, `fecha`, `hora_entrada`, `hora_salida`, `estado`, `observacion`). Se relaciona con `Empleado` en `1:N` y alimenta las interfaces P28 y P29. |

## C. Términos de ingeniería web y de la documentación

| Término | Definición en este proyecto |
|---|---|
| Funcionalidad | Unidad funcional completa que permite cumplir un objetivo de negocio de principio a fin (definición oficial del punto 5.8). Hay 38: F01–F38. |
| Interfaz | Pantalla del sistema que representa funcionalidades. Hay 30: P01–P30 (31 archivos HTML en la raíz; `publicidad.html` es la segunda presentación de P01). |
| Mi asistencia (P28) | Interfaz del empleado autenticado: registra y consulta su propia marcación (F35, F36, F37; RN05). Ejemplo del corte: Ana Torres, 10/09/2026, 08:00–17:00, Presente. Archivo `mi-asistencia.html`. |
| Control de asistencia (P29) | Interfaz del administrador para consultar la asistencia de todo el personal (F38; RN05). Archivo `control-asistencia.html`. |
| Regla de negocio | Declaración formal, atómica, declarativa, estable y obligatoria que restringe el negocio (punto 5.9). Hay 5: RN01–RN05. |
| RN05 · Control de asistencia del personal | Sólo los empleados activos registran su propia jornada: cada registro pertenece a un solo empleado, «Mi asistencia» sólo muestra la marcación del usuario autenticado, no puede haber dos asistencias para la misma jornada, la hora de salida debe ser posterior a la de entrada y el estado (`Presente` o `Falta`) se calcula a partir de las horas marcadas (F35–F38 · P28, P29). |
| Caso de cumplimiento | Ejemplo concreto en que la regla se respeta, por ejemplo «vender 10 L con 1,990 L disponibles» en RN01. |
| Caso de violación | Ejemplo concreto en que la regla se quiebra, por ejemplo «retirar 2,000 L con 1,990 L disponibles» en RN01. |
| Matriz bidimensional | Tabla de dos dimensiones —funcionalidades × interfaces— exigida por el punto 5.7: 38 × 30 con 47 pares marcados. |
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
| Spring Boot | Framework Java de la etapa posterior (persistencia, validación de RN01–RN05, seguridad). Fuera del alcance de esta maqueta. |

## D. Vocabulario específico de esta documentación

| Término | Definición en este proyecto |
|---|---|
| DEFINIDA / MAQUETADA / IMPLEMENTADA | Estados de avance de cada requisito: documentado / representado en HTML / con lógica real. Hoy: 38 DEFINIDAS + MAQUETADAS, 0 IMPLEMENTADAS. |
| Módulo funcional | Agrupación de interfaces y funcionalidades. Los doce son Acceso, Portada y contacto, Dashboard, Categorías, Combustibles, Compras, Inventario, Ventas, Finanzas, Empleados, Usuarios y Asistencia. |
| Partes estáticas | Las dos páginas exigidas sin funcionalidad de negocio: Publicidad (P01) y Contacto (P04). |
| Par F×P | Cada relación marcada en la matriz entre una funcionalidad y una interfaz. Hay 47. |
| Proceso núcleo / proceso de soporte | Los dos procesos del BPMN: venta de combustible (`POOL-CORE`) y compra/abastecimiento (`POOL-SOPORTE`). |
| Datos ficticios | Nombres, importes y volúmenes inventados con fines académicos; nada corresponde a una estación real. |

---

**Total de términos: 58** (16 de dominio, 12 de modelo, 24 de ingeniería web y 6 específicos del proyecto). Todos aparecen en los HTML o en la documentación del repositorio.
