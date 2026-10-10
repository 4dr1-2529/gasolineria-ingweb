# 23 · Informe de regresión — REVISIÓN FINAL (10/10/2026)

Regresión completa de Estación Nexo V2 después de la revisión final autorizada (login accesible, mensajes de interfaz, fechas del servidor y organización). Amplía la ETAPA 10, cuyo informe se conserva como histórico en [22_informe_regresion_etapa10.md](22_informe_regresion_etapa10.md).

## 1. Resumen ejecutivo

| Indicador | Resultado |
|---|---|
| Pruebas en la ronda final | **133 — PASS 133 / FAIL 0** |
| Errores encontrados en la aplicación | **0** (las correcciones fueron visuales/de textos y de formularios, ya aplicadas antes de la ronda) |
| Nuevas frente a la ETAPA 10 | 7 (D12, I12, V12, M01–M04); 6 ampliadas con fecha del servidor (D02, D08, I02, I05, I08, V06) |
| Funcionalidades con prueba HTTP real | 35/38 + F02 verificada en vivo; F03/F33/F34 fuera del alcance V2 |
| RN01–RN05 | las cinco con caso válido e inválido y estado de datos verificado |
| Enlaces | 346 de Markdown, 516 de la maqueta V1 y 56 páginas V2 recorridas en vivo: **0 rotos** |
| Recuentos de la rúbrica | **38 funcionalidades, 30 interfaces, 5 reglas, 47 asociaciones F×P — sin alterar** |
| Estado Git | sin commit, sin push; cambios entregados para aprobación |

## 2. Metodología

1. La aplicación se reinició (`mvn spring-boot:run`) antes de la ronda para recuperar la semilla exacta (2 categorías, 3 combustibles PR01 1990 L / PR02 980 L / PR03 3950 L, 3 empleados, 3 usuarios, 6 movimientos de inventario, 4 movimientos de caja, 5 marcaciones sin ninguna de hoy, 2 conceptos).
2. Batería de regresión (PowerShell 5.1 + curl.exe) construida sobre la de la ETAPA 10: crea sesiones reales (`ediaz` Administrador; `atorres` y `lrojas` Operadores; cuenta creada por la propia batería), extrae el token CSRF de cada sesión y verifica códigos HTTP, textos servidos y **estado posterior de los datos** (filas de tabla, litros de stock, importes de caja).
3. Las fechas se verifican contra el día real (`Get-Date` en America/Lima) y contra la ausencia de la fecha manipulada enviada en el POST.
4. Evidencias verificables (sin contraseñas, sesiones, cookies ni tokens) en [`evidencias/`](evidencias/).

## 3. Resultados por área (133 pruebas)

| Área | Pruebas | Variación frente a ETAPA 10 |
|---|---|---|
| Semilla verificada (S01–S07) | 7 | sin cambio |
| Acceso, anonimato, login, roles, CSRF (A01–A09) | 9 | sin cambio |
| Categorías CRUD + RN02 + permisos (C01–C12) | 12 | sin cambio |
| Combustibles CRUD + selectores (P01–P11) | 11 | sin cambio |
| Empleados CRUD + RN02 (E01–E09) | 9 | sin cambio |
| Usuarios CRUD + rol fabricado (U01–U15) | 15 | sin cambio |
| Conceptos económicos + caja (K01–K09) | 9 | sin cambio |
| Asistencia + unicidad RN05 (AS01–AS10) | 10 | sin cambio |
| Compras (D01–D12) | 12 | +1 (D12 fecha informativa); D02 y D08 ahora prueban fecha del servidor |
| Ventas (V01–V12) | 12 | +1 (V12 sin campo de fecha); V06 verifica fecha de hoy |
| Inventario (I01–I12) | 12 | +1 (I12 fecha informativa); I02/I05 ignoran fechas manipuladas; I08 valida litros |
| Finanzas + conciliación (F01–F06) | 6 | sin cambio |
| Cierre RN01–RN05 (R01–R05) | 5 | sin cambio |
| Textos de interfaz (M01–M04) | 4 | **nuevo**: 18 páginas sin «en memoria», «en ejecución», «base de datos» ni «almacenamiento temporal» |
| **Total** | **133** | **PASS 133 / FAIL 0** |

## 4. Fechas del servidor (America/Lima)

| Formulario | Campo de fecha | Método que registra la fecha |
|---|---|---|
| Compra (F13) `/compras/crear` | `fecha` **quitado** del formulario; «Fecha de registro» informativa (solo lectura) | `CompraServiceImpl.crearCompra` — `LocalDateTime.of(LocalDate.now(), LocalTime.now())` |
| Entrada de inventario (F16) | `fechaHora` **quitado**; «Fecha y hora de registro» informativa | `InventarioController.crearEntrada` — `LocalDateTime.now()` |
| Salida de inventario (F17) | `fechaHora` **quitado**; informativa | `InventarioController.crearSalida` — `LocalDateTime.now()` |
| Venta (F20) `/ventas/crear` | nunca tuvo campo de fecha (V12 lo verifica) | `VentaServiceImpl.crearVenta` — `LocalDateTime.now()` |
| Caja (F22) | derivada de la operación (RN04) | `MovimientoInventario`/`MovimientoCajaServiceImpl` hereda `compra.getFechaHora()` / `venta.getFechaHora()` |
| Asistencia (F35–F38) | nunca tuvo campo de fecha | `AsistenciaServiceImpl.registrarEntrada/registrarSalida` — `LocalDate.now()` y `LocalTime.now()` |

Zona horaria: `NexoApplication.main` fija `TimeZone.setDefault(TimeZone.getTimeZone("America/Lima"))` antes de arrancar; no existen `Instant`, `ZoneOffset`, `Calendar`, `SimpleDateFormat` ni `new Date()` en el código. Las visualizaciones construyen `dd/MM/yyyy` a partir del ISO del servidor, sin conversión de zona. Pruebas: D08 (POST con `fecha=1999-05-05`), I02 (`1999-01-01T00:00`), I05 (`2099-12-31T23:59`) y V06/D12/I12 confirman la fecha de hoy en listados y formularios.

## 5. Cambios verificados en esta revisión

- **Login accesible** (`login.jsp`): cabecera de marca, tarjeta de formulario, avisos con clases existentes en el CSS (`notice error` / `notice ok`), `role="alert"` y `role="status"`, `aria-describedby` en los campos, cuentas de prueba enmarcadas y **sin contraseña visible** («La contraseña de cada cuenta la indica el Administrador»).
- **Mensajes de gestión**: ~22 JSP sin tecnicismos de implementación; conservan la descripción funcional y todos los mensajes de error reales (M01–M04 los comprueban en vivo).
- **Formularios de fecha**: compra e inventario ya no reciben ni envían campos de fecha (D12/I12 verifican `name="fecha"`/`name="fechaHora"` ausentes y la marca informativa del día).
- **Organización**: `.vscode` fuera de Git sin borrarlo del equipo; evidencias en `documentacion/evidencias/`; `22_…` conservado como histórico con nota al inicio.

## 6. Verificación de enlaces (sin rotos)

| Alcance | Revisados | Rotos |
|---|---|---|
| Enlaces locales de `documentacion/*.md` y `README.md` | 346 | 0 |
| Enlaces `href`/`src` de los 31 HTML V1 | 516 | 0 |
| Recorrido en vivo V2 con sesión Administrador | 56 páginas (rutas, `?id=`, CSS) | 0 (0 × 404/500) |

Los dos únicos códigos no-200 del recorrido son **403 correctos por rol** en `/ventas/crear` e `/inventario/salida/crear` (diseño documentado: crear = Operador/Vendedor; el Administrador recibe la página «Esta sección no te corresponde»).

## 7. Recuentos de la rúbrica (re-verificados sin alterar)

| Elemento | Cantidad | Dónde |
|---|---|---|
| Funcionalidades F01–F38 | **38** | [04_funcionalidades.md](04_funcionalidades.md) |
| Interfaces P01–P30 | **30** | [03_interfaces.md](03_interfaces.md) |
| Reglas RN01–RN05 | **5** | [05_reglas_negocio.md](05_reglas_negocio.md) |
| Asociaciones F×P (marcas `X`) | **47** | [06_matriz_funcionalidades_interfaces.md](06_matriz_funcionalidades_interfaces.md) |

## 8. Estado de la entrega

- Rama `main`, HEAD `043b13c` (= `origin/main`): cambios de las fases 2–5 y de esta revisión **sin commit ni push**, a la espera de aprobación.
- Interfaz HTML V1, catálogo 38 F / 30 P / 5 RN / 47 asociaciones y documentación de rúbrica: **sin eliminar ni mover**.
- Contraseñas y secretos: ninguna contraseña visible en el login público; no se encontraron claves reales, tokens ni credenciales en el repositorio. El único valor de demostración (la contraseña del semillero) está en la constante `CONTRASENA_DEMO` del código y en la documentación interna del semillero, fuera de las páginas públicas y de las evidencias.
