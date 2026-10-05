# 5.10 · Productos y entregables

Fuente: `recurso/Proyecto Estructura_v2 (1).pdf`, punto **5.10 Productos y entregables**. El documento oficial pide seis entregables y exige presentar los diagramas «en alguna herramienta basada en código, por ejemplo https://www.plantuml.com/». Este documento no vuelve a crear lo que ya existe: los enlaza en su fuente, sin duplicar documentación.

---

## 1. Inventario de los seis entregables oficiales

| # | Exigencia oficial | Dónde vive | Estado |
|---|---|---|---|
| 1 | Diagrama entidad-relación de la solución de base de datos | [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md) §Diagrama ER (Mermaid) | **Entregado** |
| 2 | Diccionario de datos de todo el modelo (semántica de cada entidad y sus atributos) | [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md) §cada entidad + §Condiciones de diseño futuro; resumen en la sección 3 de este documento | **Entregado** |
| 3 | Diagramas de secuencia del proceso core y del proceso de soporte | Sección 4 de este documento (Mermaid `sequenceDiagram`) | **Entregado** |
| 4 | Diagramas de casos de uso del proceso core y del proceso de soporte | Sección 5 de este documento (PlantUML) | **Entregado** |
| 5 | Patrones de desarrollo usados y su respectiva implementación (capturas de los patrones revisados en el curso) | Sección 6 de este documento | **Parcial** — §6.A registra lo realmente implementado en la maqueta con extractos, §6.B lo previsto para Spring Boot; las *capturas de los patrones revisados en clase* (§6.C) son **PENDIENTE · requiere evidencia** |
| 6 | Cronograma del proyecto | Sección 7 de este documento | **Parcial** — fases reales ordenadas; las fechas del ciclo son **PENDIENTE humano** |

Bibliografía a revisar exigida junto a este punto (Coronel/Morris/Rob; Cervantes Maceda, Velasco-Elizondo y Castro Careaga): registrada como pendiente en [14_bibliografia.md](14_bibliografia.md).

---

## 2. Diagrama entidad-relación (entregable 1)

Vive en [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md) y se dibuja con **Mermaid `erDiagram`** (herramienta basada en código). Contiene:

- Las **12 entidades** oficiales del modelo validado: `Categoria`, `Producto`, `Compra`, `DetalleCompra`, `Empleado`, `Usuario`, `Venta`, `DetalleVenta`, `MovimientoInventario`, `ConceptoMovimiento`, `MovimientoCaja`, `Asistencia`.
- Claves **PK** y **FK** declaradas en cada entidad y listadas en las tablas de atributos.
- Las **15 relaciones con cardinalidad** (`1:N`, `1:0..1`), incluida la relación `Categoria 1:N Producto` que sustenta el vínculo categoría → combustible exigido por la especificación y la relación `Empleado 1:N Asistencia`.
- Nada fuera del modelo oficial: el diagrama es el mismo que auditan [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md) y [07_trazabilidad.md](07_trazabilidad.md).

---

## 3. Diccionario de datos (entregable 2)

La semántica de cada entidad y de cada atributo está en las tablas de [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md) (una fila por atributo: clave, condición y descripción). Los tipos de dato están declarados en el bloque Mermaid del mismo documento y los dominios y restricciones en su sección *Condiciones de diseño futuro*. Resumen medido del diccionario completo:

| Entidad | Atributos | PK | FK | Interfaces asociadas |
|---|---:|---|---|---|
| Categoria | 4 | `id_categoria` | — | P05, P22 |
| Producto | 7 | `id_producto` | `id_categoria` | P06, P07 |
| Compra | 6 | `id_compra` | `id_usuario` | P20, P23 |
| DetalleCompra | 6 | `id_detalle_compra` | `id_compra`, `id_producto` | P20, P21, P23 |
| Empleado | 7 | `id_empleado` | — | P18, P25, P28, P29 |
| Usuario | 6 | `id_usuario` | `id_empleado` | P02, P19, P26 |
| Venta | 5 | `id_venta` | `id_usuario` | P08, P24 |
| DetalleVenta | 6 | `id_detalle` | `id_venta`, `id_producto` | P08, P09, P10, P24 |
| MovimientoInventario | 7 | `id_movimiento_inventario` | `id_producto`, `id_usuario` | P12, P13, P14 |
| ConceptoMovimiento | 4 | `id_concepto` | — | P17, P27 |
| MovimientoCaja | 9 | `id_movimiento_caja` | `id_concepto`, `id_usuario`, `id_venta`, `id_compra` | P15, P16, P30 |
| **Asistencia** | **7** | **`id_asistencia`** | **`id_empleado`** | **P28, P29** |
| **Total** | **74** | **12 PK** | **15 FK** | **12/12 entidades con interfaz** |

La entidad `Asistencia` aporta 7 atributos: `id_asistencia` (PK), `id_empleado` (FK), `fecha`, `hora_entrada`, `hora_salida`, `estado` y `observacion` (opcional); con ella el diccionario llega a **74 atributos, 12 PK, 15 FK y 15 relaciones**, siendo la número 15 `Empleado 1:N Asistencia`. Las interfaces de cada entidad ya incluyen los formularios nuevos (P22 categoría, P23 compra, P24 venta, P25 empleado, P26 usuario, P27 concepto) y las páginas nuevas de asistencia (P28, P29) y de caja (P30).

---

## 4. Diagramas de secuencia (entregable 3)

Herramienta: **Mermaid `sequenceDiagram`** (código, renderizado por GitHub y VS Code). Son *diagramas de comportamiento* como pide la bibliografía oficial (Cervantes Maceda, «Diagrama de comportamiento — Diagrama de secuencia»). Especifican el flujo previsto para la etapa Spring Boot: **en esta maqueta no se ejecutan**; los participantes, mensajes y alternativas usan sólo IDs existentes. Las entidades que aparecen en las columnas *Entidad* pertenecen al diccionario de 12 entidades y 74 atributos de la §3. Las interfaces nuevas entran donde la misma funcionalidad se maqueta además en un formulario propio (P23 con F13, P24 con F20) o en una página de detalle (P30 con F28); la portada y el contacto (F33 y F34 en P01 y P04) y la asistencia (F35–F38 en P28 y P29) quedan fuera de estos dos procesos BPMN.

### 4.1 Proceso núcleo · Venta de combustible

Equivalente a `POOL-CORE` de [09_bpmn.md](09_bpmn.md): `S1 → A1 → G1 → A2 → A3 → G2 → A4 → A5 → A8 → E1` con los fin de error `E2` y `E3`.

```mermaid
sequenceDiagram
    autonumber
    participant OP as Operador de turno
    participant P08 as P08 ventas.html
    participant F20 as F20 Registrar venta
    participant PRO as Producto · existencias
    participant VTA as Venta y DetalleVenta
    participant INV as MovimientoInventario
    participant CAJ as MovimientoCaja
    participant PUB as P09 y P10 publicación
    participant FIN as P15 finanzas
    OP->>P08: Solicita combustible con litros
    P08->>F20: Registrar venta con su detalle
    F20->>PRO: Consultar existencia y estado (F18)
    alt RN01 existencia suficiente, RN02 producto activo y RN03 valores válidos
        F20->>VTA: Crear cabecera y detalle de la venta
        F20->>PRO: Descontar stock en litros
        F20->>INV: Registrar salida por venta · MI004, MI005, MI006
        F20->>CAJ: Crear el ingreso único · MC003, MC004, MC005
        F20-->>P08: Venta confirmada
        F20->>PUB: Publicar historial y detalle · A5 · F21, F22
        F20->>FIN: Conciliar caja del día · A8 · F28
    else RN01 o RN02 violada
        F20-->>P08: Venta rechazada · fin de error E2
    end
    Note over CAJ: Si el ingreso ya existe para la venta, fin de error E3 (RN04)
    Note over P08, PUB: F20 también se maqueta en P24 (venta-form.html) y F28 se consulta en P15 y en P30 (movimiento-detalle.html)
```

| Mensaje | Funcionalidad | Interfaz | Regla | Entidad | Elemento BPMN |
|---|---|---|---|---|---|
| Registrar datos de la venta | F20 | P08, P24 | — | Venta, DetalleVenta | `A1` |
| Consultar existencia y estado | F18 | P08 | RN01, RN02 | Producto | `G1` |
| Confirmar venta y descontar existencias | F20 | P08, P24 | RN01, RN03 | Producto | `A2` |
| Registrar salida · MI004–MI006 | F20 (origen) · F19 (consulta) | P08, P14, P24 | RN01 | MovimientoInventario | `A3` |
| Crear ingreso único · MC003–MC005 | F20 (origen) · F28 (consulta) | P08, P15, P24, P30 | RN04 | MovimientoCaja | `A4`, `G2` |
| Publicar historial y detalle | F21, F22 | P09, P10 | — | Venta, DetalleVenta | `A5` |
| Conciliar caja del día | F28 | P15, P30 | RN04 | MovimientoCaja, ConceptoMovimiento | `A8` |
| Venta rechazada | — | P08 | RN01, RN02 | — | `E2` |
| Ingreso duplicado | — | P08 | RN04 | — | `E3` |

La columna *Funcionalidad* distingue el origen del efecto: las salidas (`A3`) y el ingreso (`A4`) los origina la venta F20 —maquetada en P08 y en el formulario P24— y se **consultan** como F19 (P14) y F28 (P15 y P30), exactamente como los enlaza [09_bpmn.md](09_bpmn.md). Los códigos de evento de error corresponden a los casos de violación de RN01/RN02 (`E2`) y RN04 (`E3`) y se muestran en la interfaz que originó la transacción.

### 4.2 Proceso de soporte · Compra y abastecimiento

Equivalente a `POOL-SOPORTE` y `POOL-PROV` de [09_bpmn.md](09_bpmn.md): `S3 → A6 → G3 → A7 → SP-INV → G4 → A9 → A10 → E6` con los fin de error `E4` y `E5` y el flujo de mensaje `M1`.

```mermaid
sequenceDiagram
    autonumber
    participant PRV as Proveedor
    participant P20 as P20 compras.html
    participant F13 as F13 Registrar compra
    participant DTC as DetalleCompra
    participant INV as MovimientoInventario
    participant PRO as Producto · existencias
    participant CAJ as MovimientoCaja
    participant P21 as P21 compra-detalle
    PRV-->>P20: M1 · factura de la compra
    P20->>F13: Registrar la compra con sus líneas
    F13->>DTC: Crear un detalle por combustible recibido
    alt RN03 litros y precios válidos
        F13->>INV: Crear una entrada por línea · MI001, MI002, MI003
        INV->>PRO: Sumar existencias · subproceso SP-INV
        alt RN04 aún no existe egreso de esta compra
            F13->>CAJ: Crear el egreso único · MC001
        else ya existe el egreso de esta compra
            F13-->>P20: Fin de error E5
        end
        F13-->>P20: Compra confirmada
        P20->>P21: Publicar detalle de la compra · A10 · F15
    else RN03 violada
        F13-->>P20: Fin de error E4
    end
    Note over PRV, P21: F13 también se maqueta en P23 (compra-form.html) y el egreso se consulta como F28 en P15 y en P30 (movimiento-detalle.html)
```

| Mensaje | Funcionalidad | Interfaz | Regla | Entidad | Elemento BPMN |
|---|---|---|---|---|---|
| Factura del proveedor | F13 | P20 | — | Compra | `M1` |
| Registrar compra con líneas | F13 | P20, P23 | RN03 | Compra, DetalleCompra | `A6` |
| Crear entradas por línea · MI001–MI003 | F16 | P12 | RN03 | MovimientoInventario | `A7` (call activity) |
| Sumar existencias | F19 | P14 | RN01, RN03 | Producto | `SP-INV` |
| Crear egreso único · MC001 | F13 (origen) · F28 (consulta) | P20, P15, P23, P30 | RN04 | MovimientoCaja | `A9`, `G4` |
| Publicar detalle de la compra | F15 | P21 | RN03, RN04 | Compra, DetalleCompra | `A10` |
| Fin de error por valores no válidos | — | P20 | RN03 | — | `E4` |
| Fin de error por egreso duplicado | — | P20 | RN04 | — | `E5` |

La columna *Funcionalidad* vuelve a separar origen y consulta: el egreso lo origina la compra F13 —maquetada en P20 y en el formulario P23— y se consulta como F28 en P15 y en P30, como enlaza [09_bpmn.md](09_bpmn.md). `E4` y `E5` son los casos de violación de RN03 y RN04.

---

## 5. Diagramas de casos de uso (entregable 4)

Herramienta: **PlantUML** (la citada literalmente por el documento oficial). Los bloques `@startuml … @enduml` se pegan en <https://www.plantuml.com/plantuml/uml> para obtener el diagrama; también los resuelven las extensiones PlantUML de VS Code. Los casos de uso **son funcionalidades existentes** (se escribe su código `Fnn`); no se crean casos nuevos.

### 5.1 Proceso núcleo · Venta de combustible

```plantuml
@startuml
left to right direction
title Casos de uso · Proceso núcleo (venta de combustible)
actor "Operador / Vendedor" as OP
actor "Administrador" as ADM
rectangle "Estacion Nexo · Proceso nucleo" {
  usecase "Iniciar sesion (F01)" as UC01
  usecase "Registrar venta (F20)" as UC20
  usecase "Consultar existencias (F18)" as UC18
  usecase "Consultar movimientos de inventario (F19)" as UC19
  usecase "Consultar ventas (F21)" as UC21
  usecase "Consultar detalle de venta (F22)" as UC22
  usecase "Conciliar movimientos y saldo de caja (F28)" as UC28
}
OP --> UC01
OP --> UC20
OP --> UC18
OP --> UC21
OP --> UC22
ADM --> UC19
ADM --> UC28
UC20 ..> UC18 : <<include>>
note right of UC20
  RN01, RN02, RN03 y RN04.
  Camino BPMN: A1 - G1 - A2 - A3 - G2 - A4 - A5 - A8.
end note
note right of UC18
  F18 se representa en P08, P11 y P13.
end note
note left of UC19
  F19 participa tambien en el proceso de soporte (P14).
end note
note left of UC20
  F20 se registra en P08 y en el formulario P24 (venta-form.html).
end note
note right of UC28
  F28 se consulta en P15 y en P30 (movimiento-detalle.html).
end note
@enduml
```

### 5.2 Proceso de soporte · Compra y abastecimiento

```plantuml
@startuml
left to right direction
title Casos de uso · Proceso de soporte (compra y abastecimiento)
actor "Administrador" as ADM
actor "Proveedor" as PRV
rectangle "Estacion Nexo · Proceso de soporte" {
  usecase "Registrar compra de combustible (F13)" as UC13
  usecase "Consultar compras (F14)" as UC14
  usecase "Consultar detalle de compra (F15)" as UC15
  usecase "Registrar entrada de combustible (F16)" as UC16
  usecase "Consultar movimientos de inventario (F19)" as UC19
  usecase "Conciliar movimientos y saldo de caja (F28)" as UC28
}
ADM --> UC13
ADM --> UC14
ADM --> UC15
ADM --> UC16
ADM --> UC19
ADM --> UC28
PRV --> UC13
UC13 ..> UC16 : <<include>>
note right of UC13
  RN03 y RN04.
  Camino BPMN: A6 - G3 - A7 - SP-INV - G4 - A9 - A10.
  El proveedor aporta el mensaje M1 (factura).
  F13 se registra en P20 y en el formulario P23 (compra-form.html).
end note
note right of UC28
  F28 se consulta en P15 y en P30 (movimiento-detalle.html).
end note
@enduml
```

**Correspondencia de los dos diagramas:**

| Diagrama | Casos de uso (funcionalidades) | Interfaces | Actores | Reglas | Entidades |
|---|---|---|---|---|---|
| Núcleo | F01, F18, F19, F20, F21, F22, F28 | P02, P08, P09, P10, P11, P13, P14, P15, P24, P30 | Operador / Vendedor, Administrador | RN01, RN02, RN03, RN04 | Venta, DetalleVenta, Producto, MovimientoInventario, MovimientoCaja, ConceptoMovimiento |
| Soporte | F13, F14, F15, F16, F19, F28 | P12, P14, P15, P20, P21, P23, P30 | Administrador, Proveedor | RN01, RN03, RN04 | Compra, DetalleCompra, Producto, MovimientoInventario, MovimientoCaja, ConceptoMovimiento |

Los casos de uso fuera de estos dos procesos quedan así: catálogo (F04–F12, con formulario en P07 y P22), finanzas (F23–F27, con formulario en P27 y consultas en P16 y P17), personal (F29–F32, con formularios en P25 y P26), portada y contacto (F33 y F34 en P01 y P04), asistencia (F35–F38 en P28 y P29) y tablero (F03 en P03). Todos pertenecen a los módulos de soporte administrativo y están completos en [04_funcionalidades.md](04_funcionalidades.md) y en la matriz de [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md): el punto 5.10 pide los dos procesos, no el inventario total de 38 funcionalidades.

---

## 6. Patrones de desarrollo usados (entregable 5)

Para no presentar como «implementado» algo que sólo está previsto, los patrones se separan en tres grupos.

### A. Implementado realmente en la maqueta estática

| Patrón / convención | Qué resuelve | Dónde se usa en Estación Nexo | Evidencia |
|---|---|---|---|
| **Design tokens (variables CSS)** | Un mismo lenguaje de color compartido por todos los componentes | La paleta `:root` de `css/estilos.css` gobierna botones, tarjetas y gráficos | Extracto 1 de esta sección |
| **Navegación consistente** | Un mismo menú, con el mismo orden de módulos, en todas las páginas internas | Las **27 páginas con barra lateral** repiten el mismo bloque `<nav>` con el mismo orden de módulos, de 13 enlaces que cubren los 12 módulos oficiales (sólo cambia `aria-current="page"`); las 4 páginas públicas (index, publicidad, contacto, login) usan una variante reducida de 4 enlaces. **No es un include/partial de servidor ni de plantilla:** en HTML estático el bloque está duplicado en cada archivo y se mantiene idéntico por convención | Extracto 2 de esta sección |
| **Convención de bloque de comentario por página** | Trazabilidad legible de qué cubre cada archivo | Cada uno de los 31 HTML arranca en su línea 3 con `Interfaz: Pnn …`, `Funcionalidades:`, `Entidades:`, `Reglas:` y `Estado:` | Propio de los 31 archivos HTML |

### B. Arquitectura prevista para la etapa Spring Boot (no implementada en esta etapa)

| Patrón | Qué resuelve | Dónde está definido | Estado |
|---|---|---|---|
| **Modelo-Vista-Controlador (MVC)** | Separar datos, presentación y lógica | Modelo: las 12 entidades de [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md); Vista: 31 HTML + `css/estilos.css`; Controlador: los F01–F38 que ejecutará Spring (README sección 16) | **Previsto para Spring Boot.** La maqueta separa ya modelo y vista, pero **no existe controlador en ejecución: 0 de 38 funcionalidades implementadas** (estados DEFINIDA + MAQUETADA) |
| **Repository / Service (capas)** | Separar acceso a datos y lógica transaccional | README sección 16 «Próxima etapa con Spring Boot» (Spring Data JPA, servicios transaccionales); aplicará RN01, RN02, RN03 y RN04 en una sola transacción sobre compra (F13) y venta (F20) | **Definido, no implementado** |

### C. Patrones revisados en el curso

> **[PENDIENTE — requiere evidencia]** el punto 5.10 pide además «capturas explicativas de los patrones revisados en el curso». El material de clase (diapositivas y prácticas de patrones) no está en el repositorio: **no se inventa** ni se sustituye por capturas ajenas, y ningún patrón se lista como «revisado en clase» sin ese material. Al recibirlo, esta sección debe añadir las capturas y, si el curso revisó otros patrones (por ejemplo del catálogo de Arquitectura de Software), incorporarlos con su implementación en Spring Boot.

**Extracto 1 — design tokens (`css/estilos.css`):**

```css
:root{
  --ink:#173c36;
  --muted:#657773;
  --paper:#f3f6f3;
  --line:#dce5dd;
  --lime:#d7f26c;
  --green:#226555;
  --white:#fff
}
```

**Extracto 2 — navegación consistente (`dashboard.html`, mismo bloque en las 27 páginas con barra lateral):**

```html
<nav aria-label="Navegación principal">
  <a href="index.html">Inicio</a>
  <a href="dashboard.html" aria-current="page">Dashboard</a>
  <a href="categorias.html">Categorías</a>
  <a href="combustibles.html">Combustibles</a>
  <!-- …enlaces restantes en el mismo orden: Compras, Inventario, Ventas,
       Finanzas, Empleados, Usuarios, Asistencia, Contacto, Cerrar sesión… -->
</nav>
```
---

## 7. Cronograma del proyecto (entregable 6)

Las fases siguen el orden real del trabajo realizado y del que queda. **Las fechas (semana de inicio, fecha de exposición y cierre del ciclo) no existen en el repositorio y no se inventan.**

| # | Fase | Entregable / evidencia | Estado | Fecha |
|---|---|---|---|---|
| 1 | Levantamiento de requisitos del material oficial | Rúbrica de Avance 1 y estructura del proyecto leídas y traducidas a controles | Ejecutado | Sin registrar en el repositorio |
| 2 | Modelo de negocio y modelo entidad-relación | [01_modelo_negocio.md](01_modelo_negocio.md), [02_modelo_entidad_relacion.md](02_modelo_entidad_relacion.md) | Ejecutado | Sin registrar en el repositorio |
| 3 | Definición de interfaces, funcionalidades y reglas | [03](03_interfaces.md), [04](04_funcionalidades.md), [05](05_reglas_negocio.md) | Ejecutado | Sin registrar en el repositorio |
| 4 | Matriz bidimensional y trazabilidad | [06](06_matriz_funcionalidades_interfaces.md), [07](07_trazabilidad.md) | Ejecutado | Sin registrar en el repositorio |
| 5 | Maquetación de las 30 interfaces (HTML5 + CSS3 + Bootstrap) | 31 archivos HTML navegables + `css/estilos.css` | Ejecutado | Sin registrar en el repositorio |
| 6 | BPMN núcleo y de soporte | [09_bpmn.md](09_bpmn.md), [bpmn.html](bpmn.html) | Ejecutado | Sin registrar en el repositorio |
| 7 | Documentación 5.1–5.8 y auditoría | [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md), [00_auditoria.md](00_auditoria.md) | Ejecutado | Sin registrar en el repositorio |
| 8 | Cierre de 5.9–5.15 | Este documento, los puntos 5.11–5.14 y [15_anexos.md](15_anexos.md) | Ejecutado | 01/10/2026 |
| 9 | Revisión del equipo y compilación del informe (A4, Arial 11) | Informe final con carátula, integrantes y glosario en plantilla | Pendiente | **PENDIENTE (equipo)** |
| 10 | Exposición Avance 1 | Presentación oral y respuesta de preguntas técnicas | Pendiente | **PENDIENTE (equipo)** |
| 11 | Etapa Spring Boot (fuera del Avance 1) | Implementación de F01–F38 y validación de RN01–RN05 | Pendiente | **PENDIENTE (equipo)** |

---

## 8. Herramienta basada en código

El documento oficial exige presentar los diagramas «en alguna herramienta basada en código, por ejemplo PlantUML». Este proyecto usa dos, ambas de código y sin interfaces gráficas de dibujo:

| Diagrama | Herramienta | Cómo se visualiza |
|---|---|---|
| Entidad-relación (02) | Mermaid `erDiagram` | Render automático en GitHub y VS Code |
| BPMN (09) | Mermaid `flowchart` con formas canónicas BPMN 2.0 | Render automático en GitHub y VS Code |
| Secuencia (este documento, §4) | Mermaid `sequenceDiagram` | Render automático en GitHub y VS Code |
| Casos de uso (este documento, §5) | PlantUML | Pegar el bloque en plantuml.com o usar la extensión PlantUML de VS Code |

Ninguna herramienta añade JavaScript al repositorio: son bloques de texto dentro de Markdown.

---

## 9. Autocontrol de trazabilidad

| Comprobación | Resultado |
|---|---|
| ¿Se usaron sólo IDs existentes? | Sí · todos los códigos F, P, RN y entidades citados en este documento existen en los HTML y en la matriz (verificado por el control 8 de [00_auditoria.md](00_auditoria.md)) |
| ¿Se crearon funcionalidades, interfaces, reglas o entidades nuevas? | No se inventó nada fuera de la especificación · conteos oficiales: 38 F (F01–F38), 30 P (P01–P30), 5 RN (RN01–RN05) y 12 entidades |
| ¿Los diagramas de secuencia cubren compra y venta como exige el BPMN? | Sí · §4.2 refleja `A6, G3, A7, SP-INV, G4, A9, A10` con `E4/E5`; §4.1 refleja `A1, G1, A2, A3, G2, A4, A5, A8` con `E2/E3` |
| ¿Los casos de uso corresponden a procesos reales? | Sí · núcleo = proceso de venta; soporte = proceso de abastecimiento (los dos de [09_bpmn.md](09_bpmn.md)) |
| ¿Algún entregable se inventó fuera de la especificación? | No · las seis filas de la sección 1 son textualmente las del punto 5.10 oficial |
| ¿Se presentó como implementado algo que sólo está previsto? | No · §6 separa lo implementado en la maqueta (A) de lo previsto para Spring Boot (B) y de los patrones de clase pendientes (C); 0 de 38 funcionalidades con lógica real |
| ¿Qué queda en pendiente humano? | Los 7 pendientes clasificados en [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) — ver la sección 10 de este documento |

---

## 10. Pendientes humanos (espejo de la clasificación oficial)

Estado idéntico al de [08_puntos_1_al_5_8.md](08_puntos_1_al_5_8.md) §*Clasificación de los pendientes*; ante cualquier cambio, manda ese documento. **Nada de esto está resuelto ni se inventó:**

| Pendiente | Clasificación | Afecta a |
|---|---|---|
| Integrantes y coordinador del G1 | pendiente humano | Carátula del informe (punto 3) |
| Capturas reales de los patrones revisados en clase | requiere evidencia | **5.10 §6.C** |
| Fechas reales del cronograma (inicio, exposición, cierre) | pendiente humano | **5.10 §7** |
| Lectura y citación de las 2 obras obligatorias (Coronel/Morris/Rob; Cervantes Maceda, Velasco-Elizondo y Castro Careaga) | pendiente humano | **5.10 §1 y 5.14 §D** |
| Validación del equipo sobre conclusiones y recomendaciones | pendiente humano | 5.11 y 5.12 |
| Volcado del glosario a la plantilla del informe (A4, Arial 11) | pendiente humano | 5.13 |
| Evidencia oficial del proceso de gasolinera (Anexo 1/4) | requiere evidencia | Punto 4 y 5.15 §D |
