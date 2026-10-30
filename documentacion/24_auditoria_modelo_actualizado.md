# Auditoría del modelo actualizado — 30 de octubre de 2026

## Fuentes y estado inicial

Repositorio real: `C:\Users\HP\Music\ing-web`. Rama `main`, último commit
`bb331d4`. `git status --short` y `git diff` no mostraron cambios al iniciar.
No se autoriza commit ni push.

Referencia funcional: `C:\Users\HP\Downloads\Informe_Avance_1_Estacion_Nexo_Grupo1_CORREGIDO.docx`.
Rúbrica: `recurso/Rubrica%20Avance%201.pdf`. Se leyeron el texto del Word y sus
tablas, incluida la matriz de permisos, RN01–RN05 y el diccionario de datos.
El alcance objetivo es 49 funcionalidades, 32 interfaces y 15 entidades.
La evidencia histórica 133/133 pertenece al catálogo anterior y no valida este alcance.

## Arquitectura encontrada antes de modificar el código

Spring Boot 3.5.0, Java objetivo 17, WAR, MVC, JSP/JSTL, Spring Security.
Módulos auth, shared, categoria, producto, compra, venta, inventario, finanzas,
empleado, usuario y asistencia. Se conservan sus paquetes y vistas.
Controller → Service → ServiceImpl → listas de modelos en memoria.
No hay Entity, Repository, datasource, Hibernate, Flyway ni transacciones de base de datos.
Compra y Venta dependen de Producto, Usuario e Inventario; Finanzas deriva
movimientos desde Compra/Venta. Usuario depende de Empleado y BCrypt.
Asistencia depende de Empleado; el controlador obtiene el empleado desde la sesión.
Home redirige al catálogo o ventas: P03 no está implementada en Spring MVC.

## Matriz de diferencias iniciales

| Requisito del Word | Archivo o módulo | Estado inicial | Corrección necesaria |
|---|---|---|---|
| F01: usuario y empleado activos | auth/NexoUserDetailsService | Pendiente | Comprobar ambos estados; no asignar privilegios a roles desconocidos |
| Tres roles separados | SecurityConfig, UsuarioServiceImpl, JSP | Pendiente | Administrador, Operador y Vendedor con permisos de la sección 5.6.3 |
| CSRF, BCrypt, cierre de sesión | SecurityConfig | Implementado, pendiente de nueva prueba | Mantener protección y verificar peticiones HTTP |
| F04–F08, duplicados normalizados | CategoriaServiceImpl | Parcial | Normalización Unicode y espacios; conservar baja lógica |
| F09–F12, nombre único por categoría | ProductoServiceImpl | Parcial | El código rechaza nombres iguales incluso en categorías distintas |
| Categoría inactiva bloquea operaciones nuevas | Producto/Compra/Venta/Inventario y selectores | Pendiente | Validar padre e hijo tanto en servicio como en listas de selección |
| Litros DECIMAL(12,3), dinero DECIMAL(12,2) | Servicios operativos | Pendiente | No redondear silenciosamente litros a dos decimales |
| Responsable autenticado | CompraController, VentaController, InventarioController | Pendiente | Sustituir identificadores enviados por el navegador por el usuario de sesión |
| RN01, concurrencia y stock no negativo | Venta/Inventario | Parcial | Validación secuencial presente; faltan bloqueo relacional y CHECK |
| RN02, conservación histórica | Servicios de catálogo/personal | Parcial | No se borran registros; falta garantizar FK y estado de categoría/empleado |
| RN03, atomicidad e idempotencia | Compra/Venta/Inventario | Pendiente | Persistencia transaccional, varias líneas y unicidad del origen |
| RN04, caja abierta y movimiento persistido | MovimientoCajaServiceImpl | Pendiente | Reemplazar apertura constante y reconstrucción de movimientos por CajaDiaria y movimientos persistidos |
| RN05, jornada propia | AsistenciaController/ServiceImpl | Parcial | Identidad desde sesión presente; faltan control de empleado inactivo en salida y jornada abierta anterior |
| F44–F47, Proveedor y P31/P32 | No existen | Pendiente | Entidad, repositorio, CRUD, filtros, JSP y FK desde Compra |
| F48–F49, caja diaria | No existe CajaDiaria | Pendiente | Apertura/cierre por fecha y movimientos vinculados |
| Persistencia de 15 entidades/22 FK | Modelos y pom.xml | Pendiente | Migrar según diccionario y probar MySQL y reinicio |
| F03, cinco series y KPI reales | HomeController | Pendiente | Dashboard con datos registrados y visibilidad según rol |
| F33/F34 públicas | Solo maqueta V1 | Pendiente | Integración MVC; contenido real de contacto no confirmado |
| Filtros de personal, cuentas y operaciones | Controladores/JSP | Parcial | Completar todos los filtros descritos en 5.6.7 |
| Validación visible y BindingResult | Controladores/JSP | Parcial | Servicios devuelven mensajes; falta tratar errores de conversión y formularios completos |
| Pruebas nuevas | src/test inexistente inicialmente | Pendiente | Crear regresiones, pruebas de seguridad, integración y persistencia |

## Contradicciones y decisiones

El texto de la solicitud menciona INT, pero el Word propone BIGINT/Long y
códigos comerciales separados. No se deben convertir los códigos PR01/C001/V001
en claves numéricas sin actualizar entidades, relaciones, formularios y pruebas.
El documento contiene decisiones pendientes del Grupo G1 en el Anexo H. La
solicitud autoriza implementar MySQL/JPA y los tres roles del modelo objetivo;
no autoriza inventar contactos reales, fechas académicas ni varias cajas por turno.
La política documentada actualmente es una caja por fecha.

## Limpieza

No se elimina documentación, maqueta, recursos docentes ni evidencias.
Los imports sin uso pueden limpiarse al modificar la clase correspondiente.
Las listas y semillas no son archivos prescindibles mientras constituyen la
implementación activa. Su sustitución requiere completar la migración funcional.
No hay justificación verificada para borrar archivos académicos.

## Verificación inicial

`mvnw.cmd test -q` terminó con código 0 antes de los cambios. No había archivos
en src/test: esto acredita compilación inicial, no cobertura funcional.
Docker respondió con servidor 29.9.2. No se encontró un servicio local MySQL
ni el cliente mysql mediante los comandos de inventario; esto no demuestra que
no exista una instancia accesible por red. Aún no se ha probado conexión MySQL.
