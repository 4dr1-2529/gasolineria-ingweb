> Informe histórico de la primera revisión. El estado posterior se documenta
> en [27_persistencia_real_y_validacion.md](27_persistencia_real_y_validacion.md).

# Resultado parcial de la revisión del 30 de octubre de 2026

## Estado de aceptación

**El proyecto no está terminado conforme al Word actualizado.** Se ejecutaron
auditoría, correcciones funcionales y de seguridad, preparación del esquema SQL
y pruebas reales. La aplicación Java continúa almacenando sus datos en memoria.
No existe todavía persistencia a través de las pantallas, Entity/Repository/JPA,
Flyway integrado ni transacciones Spring sobre MySQL.

Referencias: Word corregido localizado en Descargas y rúbrica oficial del
repositorio. No se modificaron esos documentos ni se eliminaron evidencias.
La rama sigue siendo main; no se hizo commit ni push.

## Arquitectura y carpetas

Se mantiene Spring Boot MVC → Controller → Service → ServiceImpl → modelos
en memoria, con Spring Security y JSP/JSTL. Los once módulos funcionales
existentes conservan sus paquetes. `shared` agrega configuración de tiempo,
normalización, validación numérica y protección de formularios repetidos.
La navegación se centraliza en `WEB-INF/views/shared/menu.jspf`.

El árbol completo, los archivos Java/JSP/CSS y los mappings se encuentran en
[25_inventario_tecnico_y_rutas.md](25_inventario_tecnico_y_rutas.md). No se
añadieron rutas ficticias para funcionalidades pendientes. Login y logout
siguen siendo POST procesados por Spring Security.

## Clases creadas y modificadas

Se crearon TiempoConfig, TextoNormalizado, Importes, ResponsableSesion,
CuentaVigenteFilter y FormularioUnico. Se modificaron los controladores de los
catálogos y operaciones, SecurityConfig, NexoUserDetailsService, HomeController,
los servicios de categorías, productos, usuarios, compras, ventas, inventario,
finanzas y asistencia. Se ajustaron las JSP operativas y administrativas.

El listado exacto de archivos está en
[archivos_revision_20261030.txt](evidencias/archivos_revision_20261030.txt).
Los cuatro conjuntos de pruebas nuevos son ReglasNegocioTest,
FormularioUnicoTest, SeguridadHttpTest y JspHttpTest.

## Correcciones ejecutadas

- Administrador, Operador y Vendedor se separan en el catálogo de roles actual
  y sus autoridades. Operador registra ajustes; Vendedor registra ventas;
  Administrador mantiene catálogos, compras, personal y finanzas, consulta
  ventas y puede registrarlas bajo la autorización administrativa del informe.
  La tabla Rol persistida sigue pendiente.
- El login comprueba estado de Usuario y Empleado. Las sesiones se revalidan
  para bloquear cuentas/empleados desactivados y roles modificados. Un rol
  desconocido no recibe los permisos de Operador.
- Se mantienen BCrypt, CSRF, expiración de sesión y logout POST. Los campos
  de contraseña no renderizan hashes ni conservan contraseñas rechazadas en
  atributos flash. Los clientes/proveedores externos no reciben acceso.
- Compra, venta y ajuste usan el responsable de la sesión. Se retiraron sus
  selectores de usuario. El navegador no decide el responsable almacenado.
- La categoría y el producto deben estar activos para nuevas operaciones.
  Los selectores y los servicios aplican la misma comprobación. El cambio de
  un producto hacia una categoría inactiva se rechaza; conservar su categoría
  histórica no elimina relaciones.
- Los nombres de catálogo se normalizan con Unicode NFKC, espacios Unicode
  y Locale.ROOT. El combustible es único por categoría, conforme al Word.
- Se conservan tres decimales en litros y dos en dinero. Los servicios rechazan
  precisión excesiva, cantidades no positivas e importes fuera de rango en
  vez de redondear silenciosamente las cantidades a dos decimales. La edición
  comercial del combustible conserva el stock: los ajustes pasan por Inventario.
- Las fechas operativas se generan con America/Lima explícita. La asistencia
  utiliza un Clock de servidor y toma fecha/hora de una misma lectura.
- RN05 rechaza empleado inactivo tanto en entrada como en salida, duplicados
  de jornada y entradas concurrentes. Una jornada anterior abierta impide
  abrir otra. Su corrección administrativa está pendiente de política y flujo;
  no se inventa una salida retroactiva.
- Formularios de compra, venta y ajustes reciben un token de operación vinculado
  a la sesión y al tipo de formulario, además de CSRF. El token consumido impide
  repetir los efectos por reenvío de ese formulario en la misma sesión. Esto
  **no garantiza idempotencia distribuida ni sustituye unicidad transaccional SQL**.
- BindingResult trata errores de conversión y redirige con un mensaje visible.
  La navegación compartida muestra las rutas permitidas. Vendedor consulta
  existencias sin ver el libro de movimientos de inventario ni acciones de ajuste.

## Esquema MySQL preparado y probado

[V1__modelo_objetivo.sql](persistencia/V1__modelo_objetivo.sql) contiene las
15 tablas y 109 columnas del diccionario, con 15 PK y 22 FK:
Categoria, Producto, Proveedor, Compra, DetalleCompra, Empleado, Rol, Usuario,
Venta, DetalleVenta, MovimientoInventario, ConceptoMovimiento, CajaDiaria,
MovimientoCaja y Asistencia.

Se mantienen claves internas BIGINT, códigos comerciales separados, unicidad,
referencias restrictivas, DECIMAL(12,3), DECIMAL(12,2), DATE, TIME y DATETIME.
Se priorizó el diccionario concreto del Word frente al ejemplo genérico INT
del texto de solicitud. La migración de los identificadores Java antiguos a
Long aún no se realizó y es parte del trabajo pendiente.

Las relaciones incluyen categoría/productos; proveedor/compras; empleado/cuenta;
rol/cuentas; responsables de operaciones; detalles de compra/venta; origen de
movimientos físicos y económicos; caja diaria/movimientos y empleado/asistencias.
No se incorporó una entidad Cliente ni atributos para completar cantidades.

Se ejecutó el DDL en MySQL 8.4 dentro de un contenedor exclusivo sin puertos
publicados. Se probó integridad, restricciones, rollback SQL y conservación
de registros después de reiniciar MySQL. Las instrucciones reproducibles y
límites están en [persistencia/README.md](persistencia/README.md).

**Estas pruebas son del esquema aislado. No prueban una compra o venta Java
persistida, ni validan el reinicio de la aplicación con datos de usuario.**
La validación de caja abierta y el guardado coordinado de todos los efectos
siguen siendo responsabilidad pendiente de servicios transaccionales.

## Estado de RN01–RN05

- RN01: validación de stock y cantidades mejorada; CHECK SQL probado. Falta
  conectar la base y controlar concurrencia de compras/ventas/ajustes con
  bloqueo relacional. No se declara cumplimiento integral.
- RN02: baja lógica conservada; bloqueo de categoría/empleado inactivo
  corregido; FK restrictivas probadas en esquema. Falta migración completa
  de los catálogos y proveedores a repositorios.
- RN03: reenvío del mismo formulario bloqueado dentro de la sesión; unicidad
  del origen y rollback SQL probados aisladamente. Faltan varias líneas,
  @Transactional y rollback del flujo Java completo.
- RN04: esquema de caja y movimientos únicos preparado y probado; la aplicación
  aún reconstruye caja desde listas y una apertura constante. Apertura/cierre,
  caja abierta y conciliación transaccional permanecen pendientes.
- RN05: identidad desde sesión, hora Lima, estados y duplicados probados en
  Java; UNIQUE de jornada y orden horario probados en SQL. Falta persistir
  las marcaciones y definir el tratamiento administrativo de jornadas antiguas.

## Pruebas ejecutadas y resultados

- Compilación inicial: `mvnw.cmd test -q`, código 0, sin pruebas existentes.
- Regresión nueva: `mvnw.cmd -o test -q`, **35 pruebas, cero fallos, cero errores,
  cero omitidas**: 16 de reglas, 4 de reenvíos, 12 de seguridad/HTTP con MockMvc
  y 3 de HTTP real con Tomcat/Jasper. Las pruebas JSP recorren 32 páginas de
  Administrador, siete de Operador, existencias y venta de Vendedor y login.
  MockMvc por sí solo no acredita compilación JSP; por eso se ejecutó Tomcat real.
- Se probaron login correcto/incorrecto, cuenta inexistente/inactiva, empleado
  inactivo, sesiones existentes, permisos, CSRF, logout, suplantación de responsable,
  asistencia manipulada, reenvío, formato numérico y milésimas de litro.
- Empaquetado: `mvnw.cmd -o package -DskipTests -q`, código 0. Se generó
  `target/estacion-nexo-0.0.1-SNAPSHOT.war`. Se omite la repetición de pruebas
  durante empaquetado porque ya se habían ejecutado sobre ese código.
- MySQL aislado: **17 comprobaciones del esquema y 3 después del reinicio,
  todas aprobadas**. Los resultados incluyen conteos, FK, stock negativo,
  duplicados, asistencia, caja, origen, conciliación y rollback SQL.
- `git diff --check` no detectó errores de espacios.

Evidencia: [Java](evidencias/resultados_revision_20261030.json),
[DDL MySQL](evidencias/resultados_mysql_esquema.json) y
[reinicio MySQL](evidencias/resultados_mysql_reinicio.json).
Los reportes Surefire completos quedan en `target/surefire-reports/`.
El aviso de instrumentación dinámica de Mockito con Java 21 no produjo fallos;
no se suprimieron controles de seguridad para ejecutar las pruebas.

Se encontró un error PKIX de Maven al descargar el proveedor de JUnit. Se
resolvió en la sesión utilizando el almacén de certificados confiables de
Windows; no se desactivó TLS ni se modificó el almacén global de certificados.
El primer intento de ejecutar el DDL reveló una CHECK de estado aplicada a
un detalle sin ese campo; se corrigió y se repitió sobre una base de pruebas
nueva. Los resultados finales corresponden al SQL corregido.

## Trabajo pendiente para aceptar el proyecto

1. Migrar los quince modelos y sus identificadores al diccionario objetivo,
   completar Entity y Repository y configurar JPA/Hibernate/MySQL/Flyway.
2. Sustituir las listas y semillas de constructores por persistencia y un
   procedimiento de inicialización controlado; no mezclar dos fuentes de datos.
3. Completar Proveedor y P31/P32; Rol persistido; CajaDiaria, apertura/cierre
   F48/F49 y los movimientos económicos guardados con origen único.
4. Compras y ventas de una o más líneas, responsable de sesión, locks de
   stock, idempotencia duradera y @Transactional sobre todos sus efectos.
5. Completar filtros solicitados de compras, personal, usuarios, asistencia
   y movimientos; último acceso de usuarios y validaciones de formularios.
6. Implementar dashboard y sus cinco series/KPI con fuentes persistidas y
   visibilidad por rol, sin series ficticias.
7. Integrar P01/P04 públicas sin inventar dirección, teléfono, correo u horario.
8. Completar autorización en servicios y pruebas de concurrencia, reinicio de
   la aplicación, rollback Java, persistencia y cobertura F01–F49.
9. Actualizar los diagramas operativos/arquitectónicos y matrices históricas
   una vez completada la migración. La auditoría actual registra las diferencias.
10. Resolver decisiones académicas del Anexo H: datos de carátula, cronograma,
    versión de JDK requerida por el docente y evidencias auténticas.

El acceso MySQL para la prueba aislada sí estuvo disponible; la integración
Java pendiente no se presenta como un bloqueo causado por ausencia de MySQL.

## Limpieza y ejecución

No se propone borrar archivos académicos: no hay una justificación verificada
para eliminar documentación, rúbrica, recursos docentes o maqueta. Se conserva
la documentación anterior en el README como historial claramente identificado.
El contenedor exclusivo de pruebas se limpia después de conservar la evidencia.

El [README actualizado](../README.md) contiene comandos para Visual Studio Code,
ejecución, pruebas, WAR y cuentas ficticias. No hay contraseñas reales incluidas
en esta revisión. Las semillas actuales son de demostración y se regeneran
al reiniciar; no constituyen un procedimiento final de carga MySQL.
