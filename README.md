# Estación Nexo — persistencia MySQL

Spring Boot MVC, JSP/JSTL, Spring Security, Spring Data JPA/Hibernate y MySQL.
Los módulos guardan sus datos mediante 15 entidades y 15 repositorios. Flyway
aplica las migraciones y Hibernate valida el esquema; no hay perfil de almacenamiento
en memoria. Compras, ventas, inventario, caja y asistencia utilizan persistencia relacional.
La comprobación del reinicio del WAR sigue bloqueada por la desaparición del
archivo al ejecutarlo en este equipo; no se considera aprobada. Los resultados y límites se detallan en
[la revisión de persistencia](documentacion/27_persistencia_real_y_validacion.md).

Se mantienen roles, BCrypt, sesiones, CSRF, responsable autenticado, categoría activa,
precisión de litros y protección de reenvíos. Cliente y Proveedor no tienen login.
Los códigos comerciales anteriores conservan sus rutas; las PK internas son Long.

## Ejecutar en Visual Studio Code

Requisitos: JDK 17 compatible (verificado con Temurin 21), Docker Desktop o un
MySQL disponible y la terminal PowerShell. Se incluye Maven Wrapper.

Para iniciar MySQL local con volumen persistente:

```powershell
$env:NEXO_MYSQL_ROOT_PASSWORD=[guid]::NewGuid().ToString('N')
$env:NEXO_DB_PASSWORD=Read-Host 'Contraseña del usuario MySQL nexo' -MaskInput
docker compose up -d --wait
```

La aplicación utiliza por defecto `localhost:3306/estacion_nexo`, usuario `nexo`.
Para otra instancia configurar NEXO_DB_URL, NEXO_DB_USER y NEXO_DB_PASSWORD.
La URL debe conservar America/Lima en la configuración de conexión.

Para una demostración con datos ficticios y cuentas `ediaz` (Administrador),
`atorres` (Operador) y `lrojas` (Vendedor):

```powershell
$env:NEXO_DEMO_PASSWORD=Read-Host 'Contraseña de las cuentas de demostración' -MaskInput
.\mvnw.cmd spring-boot:run '-Dspring-boot.run.arguments=--spring.profiles.active=demo'
```

Abrir [el login local](http://localhost:8080/login). La semilla se aplica una sola
vez a una base nueva y no reemplaza datos ni contraseñas después de reiniciar.
La contraseña elegida debe tener de 8 a 40 caracteres. No hay contraseña de demo
incluida en el repositorio. El administrador debe abrir la caja del día desde
Finanzas antes de registrar compras o ventas; el perfil demo crea una apertura
ficticia inicial de cero para su primera jornada.

Sin el perfil demo no se crean empleados, proveedores ni operaciones ficticias.
Para aprovisionar el primer administrador, definir NEXO_ADMIN_PASSWORD,
NEXO_ADMIN_DNI, NEXO_ADMIN_NOMBRES, NEXO_ADMIN_APELLIDOS y NEXO_ADMIN_TELEFONO
(formato `000 000 000`), y opcionalmente NEXO_ADMIN_USERNAME (predeterminado `admin`).
Ejecutar después `.\mvnw.cmd spring-boot:run`. El aprovisionamiento solo actúa
cuando no existen cuentas; no restablece contraseñas en reinicios posteriores.

## Compilar y probar

Las pruebas de integración requieren una base separada llamada `nexo_pruebas`.
La configuración de pruebas usa NEXO_TEST_DB_URL, NEXO_TEST_DB_USER y
NEXO_TEST_DB_PASSWORD; no reutiliza automáticamente la URL de la aplicación.
Su URL predeterminada apunta a `127.0.0.1:13306/nexo_pruebas`.

Si se utiliza el MySQL de Compose, crear la base de pruebas con el administrador
mediante `docker compose exec mysql mysql -uroot -p` y ejecutar:

```sql
CREATE DATABASE nexo_pruebas CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci;
GRANT ALL ON nexo_pruebas.* TO 'nexo'@'%';
```

Después:

```powershell
$env:NEXO_TEST_DB_URL='jdbc:mysql://localhost:3306/nexo_pruebas?connectionTimeZone=America/Lima&forceConnectionTimeZoneToSession=true'
$env:NEXO_TEST_DB_USER='nexo'
$env:NEXO_TEST_DB_PASSWORD=$env:NEXO_DB_PASSWORD
# NEXO_DEMO_PASSWORD debe corresponder a las cuentas existentes en la base de pruebas.
.\mvnw.cmd verify
java -jar target/estacion-nexo.war --spring.profiles.active=demo
```

`verify` compila, ejecuta pruebas y genera el WAR. Los reportes están en
`target/surefire-reports/`. El WAR usa el nombre estable `estacion-nexo.war`.
La conexión MySQL debe estar disponible; no se reemplaza por listas ni H2.
La prueba de reinicio completo y su evidencia están en
[verificar_reinicio_aplicacion.py](documentacion/evidencias/verificar_reinicio_aplicacion.py).
Ese verificador utiliza la instancia aislada de integración en el puerto 13306,
crea datos ficticios por HTTP y reinicia Java manteniendo la misma base.

Para el error de certificados PKIX del JDK de este equipo, usar los certificados
confiables de Windows, manteniendo la verificación TLS:

```powershell
$env:MAVEN_OPTS='-Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE'
```

## Archivos y publicación

Las migraciones activas están en `src/main/resources/db/migration/`: modelo,
catálogos de roles/conceptos, índices operativos y unicidad de conceptos.
`target/`, `.env` y `.env.*` están ignorados. Los generadores temporales
`target/migrar_modelos.py` y `target/migrar_catalogos.py` no son dependencias
para compilar o ejecutar y no se publican. Tampoco se publican WAR, clases,
copias locales, logs ni la configuración privada de las pruebas.

El usuario autorizó la publicación en main manteniendo pendiente la prueba de reinicio del WAR. El manifiesto y las evidencias
están en [la revisión final](documentacion/27_persistencia_real_y_validacion.md).

<details>
<summary>Historial documental previo a la integración de persistencia</summary>

# Estación Nexo — estado de la revisión del 30 de octubre de 2026

La aplicación funciona actualmente con **datos en memoria**. La migración integral
al modelo del Word (49 funcionalidades, 32 interfaces y 15 entidades) **no está
terminada**. No hay JPA, Repository ni conexión MySQL desde los servicios Java.

Esta revisión corrige seguridad, roles, disponibilidad de categorías, precisión de
litros, responsable autenticado, errores de formato y asistencia. Incluye pruebas
unitarias, MockMvc y HTTP real con compilación JSP. El esquema relacional objetivo
se prepara por separado; no constituye persistencia activa de la aplicación.

- [Auditoría y diferencias con el informe](documentacion/24_auditoria_modelo_actualizado.md).
- [Clases, carpetas y mappings actuales](documentacion/25_inventario_tecnico_y_rutas.md).
- [Resultados, alcance y pendientes](documentacion/26_resultado_revision_20261030.md).
- [DDL objetivo preparado](documentacion/persistencia/V1__modelo_objetivo.sql).

## Ejecutar desde Visual Studio Code

1. Abrir esta carpeta y usar un JDK compatible con Java 17 (pom.xml declara 17;
   esta revisión se verificó con Temurin 21.0.7, sin cambiar la versión objetivo).
2. En la terminal PowerShell ejecutar `.\mvnw.cmd test`.
3. Ejecutar `.\mvnw.cmd spring-boot:run` y abrir `http://localhost:8080/login`.
4. Para generar el WAR: `.\mvnw.cmd package`. El resultado queda en `target/`.

Las semillas ficticias se crean automáticamente al construir los servicios:
`ediaz` (Administrador), `atorres` (Operador), `lrojas` (Vendedor). La contraseña
pública de demostración se define en `UsuarioServiceImpl.CONTRASENA_DEMO` y no
corresponde a una cuenta real. Los hashes BCrypt se generan al iniciar. Esta
versión pierde los cambios al reiniciar y no debe presentarse como persistente.

Si Maven informa `PKIX path building failed` en este equipo Windows, puede usar
los certificados ya confiables del sistema, sin desactivar TLS:

```powershell
$env:MAVEN_OPTS='-Djavax.net.ssl.trustStoreType=Windows-ROOT -Djavax.net.ssl.trustStore=NONE'
.\mvnw.cmd test
```

Los permisos actuales separan Operador (ajustes de inventario) y Vendedor
(ventas). Administrador mantiene catálogos, compras, personal y finanzas. Una
cuenta o empleado inactivo pierde acceso, incluso con una sesión anterior.
Cliente y Proveedor no tienen login. La persistencia de Rol y Proveedor sigue
pendiente y no se sustituye por cuentas de acceso para esos actores externos.

<details>
<summary>Documentación histórica anterior a esta revisión — conserva evidencia del alcance previo</summary>

# Sistema Web de Gestión para Estación de Servicio — Estación Nexo

## 1. Descripción
**Estación Nexo** es un sistema web integral para la gestión operativa y económica de una estación de servicio (gasolinera). Administra categorías, combustibles, compras con abastecimiento físico, inventario en litros, ventas, ingresos y egresos de caja en soles, empleados, usuarios y control de asistencia del personal, con identidad visual propia en verde petróleo y lima, navegación estructurada y modelo de datos relacional consistente.

El repositorio presenta el sistema de forma integrada:
- **Capa de presentación y prototipado (V1):** 31 archivos HTML y su CSS original en `diseno-original/`, evidencia de las 30 interfaces oficiales (P01–P30) de la rúbrica.
- **Capa de aplicación y backend (V2 en Spring Boot):** Implementación en Spring Boot 3.5.0 con Java 17, arquitectura MVC (`Controller → Service → ServiceImpl → List<T> en memoria → Model → JSP`), seguridad con Spring Security (roles, BCrypt y protección CSRF) y registro automático de marcas de tiempo del servidor bajo la zona horaria `America/Lima`. Opera **35 de las 38 funcionalidades** del sistema, validadas con una batería de regresión automatizada de **133/133 pruebas HTTP PASS (0 fallos)**.

## 2. Tema
Operación diaria de venta de combustibles organizados por categoría y producto, compra y abastecimiento con proveedor, control de existencias en tanques (litros), registro de ventas, flujo monetario en caja (soles), tablero de control, gestión de personal (empleados y cuentas de usuario) y control de asistencia con jornada laboral.

## 3. Alcance
- **30 interfaces oficiales (P01–P30)** maquetadas en 31 HTML dentro de `diseno-original/` (`publicidad.html` es una variante de presentación de P01).
- **38 funcionalidades oficiales (F01–F38)** definidas en el catálogo de requisitos (mínimo de rúbrica: 20).
- **5 reglas de negocio (RN01–RN05)** aplicadas en servidor y representadas en interfaz: control de existencias, conservación de registros sin borrado físico, integridad de operaciones, trazabilidad económica y control de asistencia.
- **47 asociaciones Funcionalidad × Interfaz** verificadas en la matriz bidimensional.
- **12 entidades conceptuales**: Categoria, Producto, Compra, DetalleCompra, Empleado, Usuario, Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, MovimientoCaja y Asistencia.
- **Delimitación técnica:** El backend opera sobre colecciones en memoria (`List<T>`), sin base de datos relacional, sin JPA/Hibernate y sin JavaScript. Tres capacidades (F03 Dashboard de series de tiempo, F33 Portada pública index y F34 Mensaje de contacto) se mantienen acotadas a la capa de maquetación HTML (V1) y fuera del alcance de Spring Boot para este avance.

## 4. Módulos
Los doce módulos del sistema son:
1. **Acceso** (Login y Logout con Spring Security)
2. **Portada y publicidad** (Pública, P01)
3. **Contacto** (Pública, P04)
4. **Dashboard** (Tablero con métricas de serie de tiempo y KPIs, P03)
5. **Categorías** (Familias de combustibles con estado activo/inactivo)
6. **Combustibles** (Catálogo de productos, precios y existencias)
7. **Compras** (Abastecimiento, cálculo de importes y entrada de inventario)
8. **Inventario** (Existencias físicas en litros, entradas, salidas y libro de movimientos)
9. **Ventas** (Despacho a clientes, validación de stock y salida de inventario)
10. **Finanzas** (Ingresos, egresos, conceptos económicos y conciliación de caja)
11. **Empleados** (Ficha de personal, DNI, datos de contacto y estado)
12. **Usuarios y Asistencia** (Cuentas con roles y marcación de jornada laboral)

## 5. Tecnologías
- **Frontend / Maqueta:** HTML5 semántico, CSS3 adaptable y Bootstrap CSS 5.3.3 mediante CDN. Sin Bootstrap JS, sin JavaScript, sin TypeScript ni Node.js. Hoja de estilos propia [`diseno-original/css/estilos.css`](diseno-original/css/estilos.css) para soporte sin conexión.
- **Backend / Aplicación:** Spring Boot 3.5.0, Java 17, Apache Tomcat embebido con Jasper, Jakarta Servlet JSTL.
- **Seguridad:** Spring Security 6 con `BCryptPasswordEncoder`, sesiones autenticadas, segregación de roles (`ADMIN`, `OPERADOR`, `EMPLEADO`) y protección CSRF obligatoria en peticiones POST.
- **Zona Horaria:** Configuración estricta en servidor a `America/Lima` en `NexoApplication.main`.
- **Almacenamiento:** Colecciones en memoria (`List<T>`); sin persistencia en disco ni base de datos en esta fase.

## 6. Modelo de Negocio y Flujo Operativo
- **Venta de combustible:** Selección de combustible activo → validación de stock disponible (**RN01**) → registro de la venta con fecha y hora del servidor → deducción física de litros del tanque → generación atómica de la salida de inventario (**RN03**) → registro automático del ingreso de caja correspondiente (**RN04**).
- **Abastecimiento (Compra):** Registro de compra a proveedor → fecha/hora fijada por el servidor → incremento físico de litros en stock → generación atómica de la entrada de inventario (**RN03**) → registro automático del egreso de caja por concepto CE02 (**RN04**).
- **Asistencia:** El personal marca entrada y salida identificándose mediante su sesión de usuario (**RN05**); el servidor valida que el empleado esté activo, impide duplicidad de marcas en el mismo día y calcula el estado de la jornada a partir de la hora de marcación.
- **Conciliación de caja:** Finanzas es de solo consulta; la caja refleja con exactitud la ecuación:  
  `Saldo = Saldo de Apertura + Ingresos (Ventas) − Egresos (Compras)`.

Detalle en [Modelo de negocio](documentacion/01_modelo_negocio.md) y flujos de procesos en [BPMN](documentacion/09_bpmn.md).

## 7. Modelo Entidad-Relación
El modelo de datos comprende 12 entidades relacionadas que sustentan la coherencia de datos:
- `Categoria` (1:N) `Producto`
- `Producto` (1:N) `DetalleCompra`, `DetalleVenta`, `MovimientoInventario`
- `Compra` (1:N) `DetalleCompra` | `Compra` (1:1) `MovimientoCaja`
- `Venta` (1:N) `DetalleVenta` | `Venta` (1:1) `MovimientoCaja`
- `Empleado` (1:1) `Usuario` | `Empleado` (1:N) `Asistencia`
- `ConceptoMovimiento` (1:N) `MovimientoCaja`

Documentación detallada y diagrama Mermaid en [Modelo Entidad-Relación](documentacion/02_modelo_entidad_relacion.md).

## 8. Catálogo de Interfaces (P01–P30)
| ID | Interfaz | Archivo Maqueta (V1) | Funcionalidades | Reglas | Entidades | Ruta en Spring Boot (V2) |
|---|---|---|---|---|---|---|
| P01 | Inicio / Publicidad | [index.html](diseno-original/index.html), [publicidad.html](diseno-original/publicidad.html) | F33 | — | — | Maqueta V1 |
| P02 | Login | [login.html](diseno-original/login.html) | F01 | — | Usuario | `/login` |
| P03 | Dashboard | [dashboard.html](diseno-original/dashboard.html) | F02, F03 | — | Venta, Producto, MovimientoCaja | Maqueta V1 |
| P04 | Contacto | [contacto.html](diseno-original/contacto.html) | F34 | — | — | Maqueta V1 |
| P05 | Categorías | [categorias.html](diseno-original/categorias.html) | F04, F05, F06, F07, F08 | RN02 | Categoria, Producto | `/categorias/list` |
| P06 | Combustibles | [combustibles.html](diseno-original/combustibles.html) | F10, F12 | RN02 | Producto, Categoria | `/combustibles/list` |
| P07 | Formulario combustible | [combustible-form.html](diseno-original/combustible-form.html) | F09, F11 | RN02 | Producto, Categoria | `/combustibles/crear` |
| P08 | Registrar venta | [ventas.html](diseno-original/ventas.html) | F18, F20 | RN01–RN04 | Venta, DetalleVenta, Producto | `/ventas/crear` |
| P09 | Historial de ventas | [ventas-historial.html](diseno-original/ventas-historial.html) | F21 | RN03, RN04 | Venta, DetalleVenta, Usuario | `/ventas/list` |
| P10 | Detalle de venta | [venta-detalle.html](diseno-original/venta-detalle.html) | F22 | RN03, RN04 | Venta, DetalleVenta, MovimientoCaja | `/ventas/detalle` |
| P11 | Existencias | [inventario.html](diseno-original/inventario.html) | F18 | RN01 | Producto | `/inventario/list` |
| P12 | Entrada combustible | [inventario-entrada.html](diseno-original/inventario-entrada.html) | F16 | RN01, RN03 | MovimientoInventario, Producto | `/inventario/entrada/crear` |
| P13 | Salida combustible | [inventario-salida.html](diseno-original/inventario-salida.html) | F17, F18 | RN01 | MovimientoInventario, Producto | `/inventario/salida/crear` |
| P14 | Movimientos inventario | [inventario-movimientos.html](diseno-original/inventario-movimientos.html) | F19 | RN01, RN03 | MovimientoInventario, Producto | `/inventario/entradas` y `/salidas` |
| P15 | Resumen financiero | [finanzas.html](diseno-original/finanzas.html) | F28 | RN04 | MovimientoCaja, ConceptoMovimiento | `/finanzas/list` |
| P16 | Ingresos y egresos de caja | [movimiento-economico.html](diseno-original/movimiento-economico.html) | F26, F27 | RN04 | MovimientoCaja, ConceptoMovimiento | `/finanzas/ingresos` y `/egresos` |
| P17 | Conceptos económicos | [conceptos.html](diseno-original/conceptos.html) | F24 | — | ConceptoMovimiento | `/finanzas/conceptos/list` |
| P18 | Empleados | [empleados.html](diseno-original/empleados.html) | F30 | — | Empleado | `/empleados/list` |
| P19 | Usuarios | [usuarios.html](diseno-original/usuarios.html) | F32 | RN02 | Usuario, Empleado | `/usuarios/list` |
| P20 | Compras | [compras.html](diseno-original/compras.html) | F13, F14 | RN03, RN04 | Compra, DetalleCompra, Producto | `/compras/list` |
| P21 | Detalle de compra | [compra-detalle.html](diseno-original/compra-detalle.html) | F15 | RN03, RN04 | Compra, DetalleCompra, MovimientoCaja | `/compras/detalle` |
| P22 | Formulario categoría | [categoria-form.html](diseno-original/categoria-form.html) | F04, F07, F08 | RN02 | Categoria | `/categorias/crear` |
| P23 | Formulario compra | [compra-form.html](diseno-original/compra-form.html) | F13 | RN03, RN04 | Compra, DetalleCompra, Producto | `/compras/crear` |
| P24 | Formulario venta | [venta-form.html](diseno-original/venta-form.html) | F20 | RN01–RN04 | Venta, DetalleVenta, Producto | `/ventas/crear` |
| P25 | Formulario empleado | [empleado-form.html](diseno-original/empleado-form.html) | F29, F31 | RN02 | Empleado | `/empleados/crear` y `/editar` |
| P26 | Formulario usuario | [usuario-form.html](diseno-original/usuario-form.html) | F32 | RN02 | Usuario, Empleado | `/usuarios/crear` y `/editar` |
| P27 | Formulario concepto | [concepto-form.html](diseno-original/concepto-form.html) | F23, F25 | RN02 | ConceptoMovimiento | `/finanzas/conceptos/crear` y `/editar` |
| P28 | Mi asistencia | [mi-asistencia.html](diseno-original/mi-asistencia.html) | F35, F36, F37 | RN05 | Asistencia, Empleado | `/asistencia/mi` |
| P29 | Control de asistencia | [control-asistencia.html](diseno-original/control-asistencia.html) | F38 | RN05 | Asistencia, Empleado | `/asistencia/control` |
| P30 | Detalle movimiento caja | [movimiento-detalle.html](diseno-original/movimiento-detalle.html) | F28 | RN04 | MovimientoCaja, ConceptoMovimiento | `/finanzas/detalle` |

Detalle en [Fichas de interfaces P01–P30](documentacion/03_interfaces.md) y [Matriz de correspondencia](documentacion/06_matriz_funcionalidades_interfaces.md).

## 9. Catálogo de Funcionalidades (F01–F38)
| ID | Funcionalidad | Módulo | Interfaz | Reglas | Implementación |
|---|---|---|---|---|---|
| F01 | Iniciar sesión | Acceso | P02 | — | Spring Boot + Spring Security (`/login`) |
| F02 | Cerrar sesión | Acceso | P03 | — | Spring Boot + Spring Security (`/logout`) |
| F03 | Consultar dashboard | Dashboard | P03 | — | Maqueta V1 ([dashboard.html](diseno-original/dashboard.html)) |
| F04 | Registrar categoría | Categorías | P05, P22 | — | Spring Boot (`/categorias/crear`) |
| F05 | Consultar categorías | Categorías | P05 | — | Spring Boot (`/categorias/list`) |
| F06 | Consultar combustibles por categoría | Categorías | P05 | — | Spring Boot (`/categorias/list`) |
| F07 | Editar categoría | Categorías | P05, P22 | RN02 | Spring Boot (`/categorias/editar`) |
| F08 | Activar/desactivar categoría | Categorías | P05, P22 | RN02 | Spring Boot (`/categorias/editar`) |
| F09 | Registrar combustible | Combustibles | P07 | — | Spring Boot (`/combustibles/crear`) |
| F10 | Consultar combustibles | Combustibles | P06 | — | Spring Boot (`/combustibles/list`) |
| F11 | Editar combustible | Combustibles | P07 | RN02 | Spring Boot (`/combustibles/editar`) |
| F12 | Activar/desactivar combustible | Combustibles | P06 | RN02 | Spring Boot (`/combustibles/estado`) |
| F13 | Registrar compra de combustible | Compras | P20, P23 | RN03, RN04 | Spring Boot (`/compras/crear`) |
| F14 | Consultar compras | Compras | P20 | RN03, RN04 | Spring Boot (`/compras/list`) |
| F15 | Consultar detalle de compra | Compras | P21 | RN03, RN04 | Spring Boot (`/compras/detalle`) |
| F16 | Registrar entrada de combustible | Inventario | P12 | RN01, RN03 | Spring Boot (`/inventario/entrada/crear`) |
| F17 | Registrar salida de combustible | Inventario | P13 | RN01 | Spring Boot (`/inventario/salida/crear`) |
| F18 | Consultar existencias | Inventario | P08, P11, P13 | RN01 | Spring Boot (`/inventario/list`) |
| F19 | Consultar movimientos inventario | Inventario | P14 | RN01, RN03 | Spring Boot (`/inventario/entradas`, `/salidas`) |
| F20 | Registrar venta | Ventas | P08, P24 | RN01–RN04 | Spring Boot (`/ventas/crear`) |
| F21 | Consultar ventas | Ventas | P09 | RN03, RN04 | Spring Boot (`/ventas/list`) |
| F22 | Consultar detalle de venta | Ventas | P10 | RN03, RN04 | Spring Boot (`/ventas/detalle`) |
| F23 | Registrar concepto económico | Finanzas | P27 | — | Spring Boot (`/finanzas/conceptos/crear`) |
| F24 | Consultar conceptos económicos | Finanzas | P17 | — | Spring Boot (`/finanzas/conceptos/list`) |
| F25 | Editar/desactivar concepto | Finanzas | P27 | RN02 | Spring Boot (`/finanzas/conceptos/editar`) |
| F26 | Consultar ingresos de caja | Finanzas | P16 | RN04 | Spring Boot (`/finanzas/ingresos`) |
| F27 | Consultar egresos de caja | Finanzas | P16 | RN04 | Spring Boot (`/finanzas/egresos`) |
| F28 | Consultar movimientos y saldo | Finanzas | P15, P30 | RN04 | Spring Boot (`/finanzas/list`, `/detalle`) |
| F29 | Registrar empleado | Empleados | P25 | — | Spring Boot (`/empleados/crear`) |
| F30 | Consultar empleados | Empleados | P18 | — | Spring Boot (`/empleados/list`) |
| F31 | Editar/desactivar empleado | Empleados | P25 | RN02 | Spring Boot (`/empleados/editar`) |
| F32 | Gestionar cuentas de usuario | Usuarios | P19, P26 | RN02 | Spring Boot (`/usuarios/list`, `/crear`, `/editar`) |
| F33 | Consultar la portada pública | Portada | P01 | — | Maqueta V1 ([index.html](diseno-original/index.html)) |
| F34 | Enviar mensaje de contacto | Contacto | P04 | — | Maqueta V1 ([contacto.html](diseno-original/contacto.html)) |
| F35 | Registrar asistencia | Asistencia | P28 | RN05 | Spring Boot (`/asistencia/mi`) |
| F36 | Consultar mi asistencia | Asistencia | P28 | RN05 | Spring Boot (`/asistencia/mi`) |
| F37 | Consultar mi resumen mensual | Asistencia | P28 | RN05 | Spring Boot (`/asistencia/mi`) |
| F38 | Consultar control general | Asistencia | P29 | RN05 | Spring Boot (`/asistencia/control`) |

Detalle en [Fichas de funcionalidades F01–F38](documentacion/04_funcionalidades.md).

## 10. Reglas de Negocio (RN01–RN05)
- **RN01 · Control de existencias:** El stock de combustible nunca puede ser negativo. Las ventas y salidas manuales se rechazan si la cantidad solicitada supera el stock disponible.
- **RN02 · Conservación de registros:** Los registros con transacciones históricas no se eliminan físicamente; pasan a estado `Inactivo`, impidiendo su selección en operaciones nuevas y manteniendo la integridad referencial.
- **RN03 · Integridad de operaciones:** Toda compra y venta debe contener sus líneas de detalle y generar de forma atómica y única su correspondiente movimiento en el libro de inventario.
- **RN04 · Trazabilidad económica:** Toda compra genera un único egreso de caja y toda venta un único ingreso de caja, conservando el identificador de la operación de origen. No existen altas manuales de caja.
- **RN05 · Control de asistencia del personal:** Solo empleados activos pueden registrar asistencia; el empleado marca su propia jornada mediante su sesión de usuario (sin manipulación de identificadores), con un único registro diario y con la hora de salida estrictamente posterior a la de entrada.

Detalle en [Reglas de negocio](documentacion/05_reglas_negocio.md).

## 11. Estructura del Repositorio
```text
ing-web/
├── README.md
├── .gitignore
├── pom.xml
├── mvnw · mvnw.cmd · .mvn/
├── src/
│   ├── main/java/com/example/nexo/
│   │   ├── auth/                     (config, controller y service de acceso)
│   │   ├── categoria/                (controller, service, model)
│   │   ├── producto/                 (controller, service, model)
│   │   ├── compra/                   (controller, service, model)
│   │   ├── venta/                    (controller, service, model)
│   │   ├── inventario/               (controller, service, model)
│   │   ├── finanzas/                 (controller, service, model)
│   │   ├── empleado/                 (controller, service, model)
│   │   ├── usuario/                  (controller, service, model)
│   │   ├── asistencia/               (controller, service, model)
│   │   ├── shared/controller/        (rutas generales)
│   │   ├── NexoApplication.java      (Timezone America/Lima)
│   │   └── ServletInitializer.java
│   ├── main/resources/
│   │   └── application.properties
│   └── main/webapp/
│       ├── WEB-INF/views/            (34 JSP; login en auth/ y vistas por módulo)
│       └── css/estilos.css
├── diseno-original/                 (31 HTML V1 y css/estilos.css; maqueta navegable)
├── recurso/                          (9 PDF de consulta y rúbrica)
`-- documentacion/                    (documentos, evidencias y BPMN)
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
    ├── 16_evidencias_etapas.md
    ├── 17_matriz_cumplimiento_avance1.md
    ├── 18_matriz_cumplimiento_examen_parcial.md
    ├── 19_validacion_requisitos.md
    ├── 20_explicacion_tecnica_modulos.md
    ├── 21_matriz_correspondencia.md
    ├── 22_informe_regresion_etapa10.md              (histórico: 126 pruebas)
    ├── 23_informe_regresion_revision_final.md        (cierre: 133 pruebas)
    ├── bpmn.html
    ├── anexos/                                      (capturas PNG del sistema)
    └── evidencias/                                  (evidencias empíricas)
        ├── README.md
        ├── bateria_revision.ps1                     (suite de 133 pruebas)
        ├── resultados_133_pruebas.csv
        ├── log_133_pruebas.txt
        ├── verificacion_enlaces.txt
        └── login_*.html                             (evidencias de accesibilidad)
```

## 12. Navegación y Roles
- **V1 (Maqueta):** Inicio ([index.html](diseno-original/index.html)) → [login.html](diseno-original/login.html) → [dashboard.html](diseno-original/dashboard.html). Los menús superiores e internos permiten recorrer las 30 interfaces con datos de demostración fijos.
- **V2 (Aplicación Spring Boot):** 
  - Ingreso por `/login` con cuentas de prueba:
    - `ediaz` (Administrador): Acceso total a catálogos, personal, finanzas, compras, entradas de combustible y control general de asistencia.
    - `atorres` / `lrojas` (Operador / Vendedor): Registro de ventas (`/ventas/crear`), salidas de combustible (`/inventario/salida/crear`), historial de ventas y marcación propia de asistencia (`/asistencia/mi`).
  - La navegación lateral de las 27 vistas JSP enlaza los módulos permitidos según el rol autenticado. Intentar ingresar a una sección no autorizada redirige a `/sin-permisos` (HTTP 403 controlado).

## 13. Estado del Avance Académico
- **38/38 Funcionalidades definidas:** Documentadas con fichas completas, actores y entidades.
- **30/30 Interfaces maquetadas (100%):** Disponibles en `diseno-original/` y navegables.
- **35/38 Funcionalidades implementadas en Spring Boot:** Operando con lógica de negocio, validaciones en servidor y seguridad por roles.
- **3/38 Acotadas a la capa de maquetación:** F03 (Dashboard visual), F33 (Portada) y F34 (Contacto).
- **133/133 Pruebas de regresión PASS:** 0 fallos en peticiones HTTP reales sobre la aplicación en marcha (informe [23_informe_regresion_revision_final.md](documentacion/23_informe_regresion_revision_final.md)).
- **0 Enlaces rotos:** Verificación automatizada sobre la totalidad de Markdown y HTML.

## 14. Cómo Ejecutar

### Requisitos
- Java JDK 17 o superior (`java -version`).
- Apache Maven (o el wrapper incluido `./mvnw.cmd` / `./mvnw`).

### 1. Iniciar la aplicación
Desde la raíz del repositorio en la terminal:
```bash
./mvnw.cmd spring-boot:run
```
La aplicación inicia en `http://localhost:8080`.

### 2. Acceder al sistema
Abrir `http://localhost:8080/login` en el navegador:
- Administrador: usuario `ediaz`
- Operador: usuario `atorres` o `lrojas`
- Contraseña de prueba: indicada por el semillero / Administrador (`[valor configurado mediante NEXO_DEMO_PASSWORD]`).

### 3. Ejecutar la batería de pruebas de regresión (133 pruebas)
Con la aplicación levantada en el puerto 8080, abrir una consola PowerShell:
```powershell
$env:NEXO_DEMO_PASS = "[valor configurado mediante NEXO_DEMO_PASSWORD]"
& .\documentacion\evidencias\bateria_revision.ps1
```
El guion verifica automáticamente códigos HTTP, redirecciones, tokens CSRF, validaciones en servidor y consistencia física/monetaria de los datos.

### 4. Visualizar la maqueta estática (V1)
Abrir [index.html](diseno-original/index.html) directamente con doble clic en el navegador, o iniciar Live Server en VS Code (puerto 2222).

## 15. Limitaciones y Delimitaciones del Avance
- **Persistencia:** Los datos residen en memoria (`List<T>`); al reiniciar el servidor la información vuelve a su semilla inicial. La incorporación de persistencia relacional con base de datos (JPA/Hibernate) corresponde al Avance 2.
- **JavaScript:** El sistema no emplea JavaScript en ninguna capa; toda la interactividad responde a peticiones estándar HTTP GET y POST con redirecciones y recarga de páginas JSP.
- **Dashboard:** El tablero analítico (F03) cuenta con diseño y maquetación visual completa con series temporales en HTML/CSS, quedando delimitada su alimentación dinámica por API para la siguiente etapa.

---
Estación Nexo — Proyecto de Ingeniería Web — Avance 1.

</details>

</details>
