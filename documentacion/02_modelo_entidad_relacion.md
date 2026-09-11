# Modelo entidad relación

Diez entidades oficiales. Modelo conceptual preparado para Spring Boot; no existe esquema de base de datos implementado. PK identifica el registro; FK referencia una entidad.

## Categoria

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_categoria | PK | Identificador de Categoria |
| nombre | Atributo | Nombre |
| descripcion | Atributo | Descripcion |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Categoria → Producto (1:N).

## Producto

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_producto | PK | Identificador de Producto |
| id_categoria | FK | Referencia a Categoria |
| nombre | Atributo | Nombre |
| unidad_medida | Atributo | Litro en esta maqueta. |
| precio_actual | Atributo | Precio actual |
| stock | Atributo | Saldo físico en litros, coherente con movimientos. |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Categoria → Producto (1:N); Producto → DetalleVenta (1:N); Producto → MovimientoInventario (1:N).

## Empleado

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_empleado | PK | Identificador de Empleado |
| dni | Atributo | Identificador ficticio en maqueta; cadena de 8 caracteres. |
| nombres | Atributo | Nombres |
| apellidos | Atributo | Apellidos |
| cargo | Atributo | Cargo |
| telefono | Atributo | Telefono |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Empleado → Usuario (1:0..1); Empleado → Asistencia (1:N).

## Usuario

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_usuario | PK | Identificador de Usuario |
| id_empleado | FK | Referencia a Empleado |
| username | Atributo | Username |
| password | Atributo | Hash de contraseña en la futura implementación; nunca texto plano. |
| rol | Atributo | Rol |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Empleado → Usuario (1:0..1); Usuario → Venta (1:N); Usuario → MovimientoInventario (1:N); Usuario → MovimientoCaja (1:N).

## Venta

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_venta | PK | Identificador de Venta |
| id_usuario | FK | Referencia a Usuario |
| fecha_hora | Atributo | Fecha hora |
| total | Atributo | Suma de subtotales de la venta. |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Usuario → Venta (1:N); Venta → DetalleVenta (1:N); Venta → MovimientoCaja (1:0..1).

## DetalleVenta

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_detalle | PK | Identificador de DetalleVenta |
| id_venta | FK | Referencia a Venta |
| id_producto | FK | Referencia a Producto |
| cantidad | Atributo | Cantidad positiva en litros. |
| precio_unitario | Atributo | Precio histórico aplicado al detalle, independiente del precio actual. |
| subtotal | Atributo | cantidad × precio_unitario. |

Relaciones: Venta → DetalleVenta (1:N); Producto → DetalleVenta (1:N).

## MovimientoInventario

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_movimiento_inventario | PK | Identificador de MovimientoInventario |
| id_producto | FK | Referencia a Producto |
| id_usuario | FK | Referencia a Usuario |
| tipo_movimiento | Atributo | Entrada o Salida de combustible. |
| cantidad | Atributo | Cantidad positiva en litros. |
| fecha_hora | Atributo | Fecha hora |
| motivo | Atributo | Motivo |

Relaciones: Producto → MovimientoInventario (1:N); Usuario → MovimientoInventario (1:N).

## ConceptoMovimiento

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_concepto | PK | Identificador de ConceptoMovimiento |
| nombre | Atributo | Nombre |
| tipo | Atributo | Ingreso o Egreso. |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: ConceptoMovimiento → MovimientoCaja (1:N).

## MovimientoCaja

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_movimiento_caja | PK | Identificador de MovimientoCaja |
| id_concepto | FK | Referencia a ConceptoMovimiento |
| id_usuario | FK | Referencia a Usuario |
| id_venta | FK opcional | Referencia a Venta |
| tipo | Atributo | Ingreso o Egreso. |
| monto | Atributo | Importe positivo en soles. |
| descripcion | Atributo | Descripcion |
| fecha_hora | Atributo | Fecha hora |

Relaciones: ConceptoMovimiento → MovimientoCaja (1:N); Venta → MovimientoCaja (1:0..1); Usuario → MovimientoCaja (1:N).

## Asistencia

| Atributo | Clave / condición | Descripción |
|---|---|---|
| id_asistencia | PK | Identificador de Asistencia |
| id_empleado | FK | Referencia a Empleado |
| fecha | Atributo | Fecha |
| hora_entrada | Atributo | Hora entrada |
| hora_salida | Atributo | Nula mientras la asistencia permanece abierta. |
| estado | Atributo | Situación del registro según su ciclo de vida. |

Relaciones: Empleado → Asistencia (1:N).

## Relaciones y cardinalidades

- **Categoria 1:N Producto**: Cada producto pertenece a una categoría; una categoría puede agrupar muchos productos.
- **Empleado 1:0..1 Usuario**: Un empleado puede no tener cuenta; cada usuario tiene un empleado, con id_empleado único.
- **Usuario 1:N Venta**: Un usuario registra muchas ventas; cada venta tiene un usuario responsable.
- **Venta 1:N DetalleVenta**: Una venta confirmada tiene al menos un detalle; cada detalle pertenece a una venta.
- **Producto 1:N DetalleVenta**: Un producto aparece en muchos detalles; cada detalle identifica un producto.
- **Producto 1:N MovimientoInventario**: Un producto tiene muchos movimientos; cada movimiento corresponde a un producto.
- **ConceptoMovimiento 1:N MovimientoCaja**: Un concepto clasifica muchos movimientos; cada movimiento tiene un concepto.
- **Venta 1:0..1 MovimientoCaja**: Una venta tiene cero o un movimiento asociado; al confirmarse exige uno de ingreso por RN04. Los movimientos manuales no tienen venta.
- **Empleado 1:N Asistencia**: Un empleado tiene múltiples asistencias; cada asistencia pertenece a un empleado.
- **Usuario 1:N MovimientoInventario**: Relación adicional derivada de id_usuario: identifica al responsable del movimiento físico.
- **Usuario 1:N MovimientoCaja**: Relación adicional derivada de id_usuario: identifica al responsable del movimiento económico.

## Condiciones de diseño futuro
- Unicidad de Usuario.id_empleado (un usuario por empleado), username y DNI. No son funcionalidades adicionales.
- MovimientoCaja.id_venta admite nulo para movimientos manuales y será único cuando tenga valor. En venta confirmada RN04 exige exactamente un ingreso por su total.
- Producto.precio_actual, DetalleVenta.precio_unitario, subtotal, Venta.total y monto usarán decimales exactos; cantidades y stock también admitirán fracciones. No usar punto flotante para dinero.
- Producto.stock y MovimientoInventario deben cambiar de forma atómica; RN01 requiere controlar concurrencia.
- Estados de catálogos: Activo / Inactivo. Venta: Pendiente / Confirmada en esta maqueta. Asistencia: Abierta / Cerrada, coherente con hora_salida.
- No se añade ninguna entidad. Las capacidades y umbral visual de stock no son atributos base persistentes.
- En relaciones 1:N se permite cero registros dependientes antes de operar; Venta confirmada exige uno o más detalles.

## Diagrama ER (solo documentación)
```mermaid
erDiagram
    Categoria ||--o{ Producto : clasifica
    Empleado ||--o| Usuario : tiene
    Usuario ||--o{ Venta : registra
    Venta ||--|{ DetalleVenta : contiene
    Producto ||--o{ DetalleVenta : integra
    Producto ||--o{ MovimientoInventario : recibe
    ConceptoMovimiento ||--o{ MovimientoCaja : clasifica
    Venta o|--o| MovimientoCaja : genera
    Empleado ||--o{ Asistencia : marca
    Usuario ||--o{ MovimientoInventario : responsable_fisico
    Usuario ||--o{ MovimientoCaja : responsable_caja
    Categoria {
        int id_categoria PK
        string nombre
        string descripcion
        string estado
    }
    Producto {
        int id_producto PK
        int id_categoria FK
        string nombre
        string unidad_medida
        decimal precio_actual
        decimal stock
        string estado
    }
    Empleado {
        int id_empleado PK
        string dni
        string nombres
        string apellidos
        string cargo
        string telefono
        string estado
    }
    Usuario {
        int id_usuario PK
        int id_empleado FK
        string username
        string password
        string rol
        string estado
    }
    Venta {
        int id_venta PK
        int id_usuario FK
        datetime fecha_hora
        decimal total
        string estado
    }
    DetalleVenta {
        int id_detalle PK
        int id_venta FK
        int id_producto FK
        decimal cantidad
        decimal precio_unitario
        decimal subtotal
    }
    MovimientoInventario {
        int id_movimiento_inventario PK
        int id_producto FK
        int id_usuario FK
        string tipo_movimiento
        decimal cantidad
        datetime fecha_hora
        string motivo
    }
    ConceptoMovimiento {
        int id_concepto PK
        string nombre
        string tipo
        string estado
    }
    MovimientoCaja {
        int id_movimiento_caja PK
        int id_concepto FK
        int id_usuario FK
        int id_venta FK
        string tipo
        decimal monto
        string descripcion
        datetime fecha_hora
    }
    Asistencia {
        int id_asistencia PK
        int id_empleado FK
        date fecha
        time hora_entrada
        time hora_salida
        string estado
    }
```
