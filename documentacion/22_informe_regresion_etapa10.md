# 22 · Informe de regresión integral — ETAPA 10 (09/10/2026)

> **Documento histórico.** Ronda de la ETAPA 10 con 126 pruebas (09/10/2026), conservado tal como se ejecutó. La revisión final del 10/10/2026 con 133 pruebas está en [23_informe_regresion_revision_final.md](23_informe_regresion_revision_final.md).

Regresión completa de Estación Nexo V2 contra la aplicación en marcha, con datos en memoria restablecidos a la semilla antes de la ronda final. Todas las pruebas son peticiones HTTP reales (curl) con sesiones autenticadas y token CSRF auténtico, no inspección de código.

## 1. Resumen ejecutivo

| Indicador | Resultado |
|---|---|
| Pruebas en la ronda final | **126 — PASS 126 / FAIL 0** |
| Errores encontrados en la aplicación | **0** (ninguna corrección de código fue necesaria) |
| Errores del guion de pruebas, corregidos | 11 (ver §4) |
| Funcionalidades con prueba HTTP real | 35/38 + F02 verificada en vivo; F03/F33/F34 fuera del alcance V2 |
| RN01–RN05 | las cinco con caso válido e inválido y estado de datos verificado |
| Estado Git | sin commit, sin push; working tree con las etapas 2–10 sin commitear |

## 2. Metodología

1. La aplicación se reinició (`mvn spring-boot:run`) **antes de cada ronda** para recuperar la semilla exacta: 2 categorías, 3 combustibles (PR01 1990 L, PR02 980 L, PR03 3950 L), 3 empleados, 3 usuarios, 6 movimientos de inventario (entradas 300 L, salidas 80 L), 4 movimientos de caja (apertura S/ 4,410.00, saldo S/ 3,430.00), 5 marcaciones de asistencia (ninguna de hoy) y 2 conceptos (CE01/CE02).
2. Batería `etapa10_regresion.ps1` (PowerShell 5.1 + curl.exe): crea sesiones reales (`ediaz` Administrador, `atorres`/`lrojas` Operadores, `mtest10` cuenta creada por la propia batería), extrae el token CSRF de cada sesión y verifica códigos HTTP, textos servidos y **estado posterior de los datos** (filas de tabla, litros de stock, importes de caja).
3. Cubre: semilla (S), acceso/login/roles anónimos (A), categorías (C), combustibles (P), empleados (E), usuarios (U), conceptos económicos (K), asistencia (AS), compras (D), ventas (V), inventario (I), finanzas (F) y cierre RN01–RN05 (R).

## 3. Resultados por área (126 pruebas)

| Área | Pruebas | Qué quedó comprobado |
|---|---|---|
| Semilla | 7 | los datos de partida exactos antes de tocar nada |
| Acceso y login | 9 | público sólo `/login`; anónimo 302→`/login` en GET y POST; CSRF también en el propio login; clave mala → `error=credenciales`; inicio por rol (Admin→categorías, staff→ventas); login con sesión sigue abriendo |
| Categorías (CRUD) | 12 | duplicado, nombre corto, descripción larga, alta/edición, id 999 con aviso, estado RN02, eliminar→404, CSRF 403, Operador 403 y sin efecto, conservación de lo digitado |
| Combustibles (CRUD) | 11 | duplicado, categoría inexistente, precio 0, unidad ≠ Litro, alta/edición, inactivo fuera del selector de ventas, eliminar→404, CSRF, Operador 403 |
| Empleados (CRUD) | 9 | DNI 7 dígitos, DNI duplicado, teléfono inválido, nombre corto, alta/edición, eliminar→404, Operador 403, CSRF |
| Usuarios (CRUD) | 15 | duplicado, clave corta, **rol fabricado rechazado**, empleado con cuenta, sin empleado, alta, clave en blanco conserva hash, desactivar, inactivo no loguea (`error=inactivo`), eliminar→404, CSRF, Operador 403 doble |
| Conceptos económicos | 9 | duplicado, tipo inválido, alta/edición sin corromper caja, estado RN02, eliminar→404, CSRF, Operador 403 |
| Asistencia | 10 | identidad real (id spoofeado ignorado), entrada repetida rechazada, salida sin entrada y salida repetida rechazadas, salida válida, control Admin con 6 marcaciones, Operador 403, CSRF, integridad final |
| Compras | 11 | fecha/proveedor/cantidad, producto inactivo, responsable inactivo, sin detalle, **alta válida con entrada por línea y egreso único (RN03/RN04)**, detalle inexistente, Operador 403, CSRF sin efectos |
| Ventas | 11 | sin cantidad sin efectos parciales, **sobre stock rechazada e intacta (RN01)**, producto inactivo, operador inactivo, **alta válida con salida única e ingreso único y descuento exacto (RN03/RN04)**, detalle inexistente, Admin 403 en `/ventas/crear`, Operador sí consulta, CSRF |
| Inventario | 11 | entrada/salida manuales válidas, producto inactivo, cantidad obligatoria, sobre stock, motivo corto, sin fecha, Operador sin entrada ni Admin sin salida, CSRF |
| Finanzas | 6 | **conciliación exacta** saldo S/ 3,000.00 = 4,410 + 420 − 1,830, filtros ingresos/egresos, detalle inexistente, Operador 403, inexistencia de ruta manual de caja (404) |
| Cierre RN01–RN05 | 5 | stock final PR01 2080 / PR02 1010 / PR03 3,950; cinco catálogos con todos los registros y sólo estados cambiados; libro 10 filas = semilla + 4 operaciones; caja 6 movimientos conciliados; una única jornada de hoy |

## 4. Errores encontrados y correcciones

### En la aplicación: 0

Ningún comportamiento del servidor contradijo una regla, un mensaje o un estado esperado. No se aplicó ninguna corrección de código en esta etapa (no hizo falta).

### En el guion de pruebas: 11, todos corregidos en el guion

| Fallo observado | Causa real | Corrección |
|---|---|---|
| A05/A06/A07 (ronda 1): el POST de login devolvía `/login` sin `error=` | el guion olvidaba enviar el token CSRF en `POST /login`; el servidor lo rechazaba correctamente | enviar el token de la sesión; además se añadió A05 como prueba explícita de CSRF en el login |
| A08 (ronda 1): `/login` con sesión esperaba 302 | el servidor devuelve **200** (la página de login es utilizable; el cierre es `/logout`) | aserción corregida a 200 + comprobación de sesión activa |
| A07/A08 (ronda 2): el seguimiento de `/` no mostraba la lista de destino | `Cuerpo` no seguía redirecciones (falta `-L` en curl) | seguimiento explícito con `-L` |
| C11/P10: el POST del Operador esperaba 200 con aviso | el servidor responde **403** con el cuerpo del aviso de permisos | aserción corregida a 403 + aviso |
| U09: la edición exitosa no mostraba «se edit» | el mensaje real de `UsuarioController` es «Usuario mtest10 actualizado correctamente.» | aserción ajustada al texto real |
| A17 (AS09): el POST sin token devolvía 405 | el guion hacía GET (faltaba `-X POST`) | método POST correcto → 403 |
| V06: la lista de ventas no mostraba «Venta V004» | `venta/lista.jsp` muestra el id «V004»; «Venta V004» sólo aparece en el libro de inventario | ancla corregida por módulo |
| I06: el aviso de stock no aparecía en la lista | el mensaje vuelve al **formulario** `salida/crear` (flash), no a la lista | doble comprobación: mensaje en el formulario y datos en la lista |
| R01: «Diesel» no coincidía | el nombre real es «Diésel» (con acento) | comprobación por literal «3,950» |

## 5. Verificaciones de integridad del catálogo

Comprobadas el 09/10/2026 sobre los archivos originales, sin alterarlos:

- **38 funcionalidades** (F01–F38) en [04_funcionalidades.md](04_funcionalidades.md).
- **30 interfaces** (P01–P30) en [03_interfaces.md](03_interfaces.md).
- **5 reglas** RN01–RN05 en [05_reglas_negocio.md](05_reglas_negocio.md).
- **47 asociaciones F×P** en la matriz de [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md).
- Código: 11 controllers, 9 interfaces de servicio + `NexoUserDetailsService`, 9 `ServiceImpl`, 34 JSP, 0 base de datos/JPA/Repository/DAO, 0 JavaScript, 0 API REST.

## 6. Actualizaciones documentales autorizadas en esta etapa

- `README.md`: §5 (V2 con Spring Security), §13 (35/38 implementadas; 3 fuera del alcance V2), §15 (limitaciones V2 con autenticación activa), §16 (etapa posterior sin «autenticación» pendiente).
- `10_productos_y_entregables.md`: 34 vistas JSP, 35/38 funcionalidades, RN01–RN05 en Service/ServiceImpl, fila 11 del cronograma con el resultado final.
- Nuevos documentos: [20_explicacion_tecnica_modulos.md](20_explicacion_tecnica_modulos.md) y [21_matriz_correspondencia.md](21_matriz_correspondencia.md).

## 7. Pendientes conocidos (decisiones humanas, no defectos)

1. F03 (tablero), F33 (portada) y F34 (contacto): fuera del alcance V2 por decisión documentada; su implementación V2 requeriría una decisión separada.
2. Persistencia (base de datos y capa de Repositorio): etapa posterior declarada.
3. Informe académico: mantener 35/38 como cifra real; la compilación del informe A4 y la exposición siguen a cargo del equipo.
4. Propuestas de organización de la ETAPA 9 (subcarpeta `recurso/`, `.vscode` en `.gitignore`): a decisión del usuario.

## 8. Estado de Git al cierre

- HEAD: `601493a`. **Sin commit, sin push, sin merge.**
- Working tree con cambios sin commitear de las ETAPAS 2–10: seguridad, validaciones, ajustes de mensajes, borrados autorizados de los 2 PDF duplicados y los documentos 17–22.
