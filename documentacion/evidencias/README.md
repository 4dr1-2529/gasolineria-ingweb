# Evidencias · Revisión final (10/10/2026)

Archivos verificables de la regresión de cierre de Estación Nexo V2 (133 pruebas, PASS 133 / FAIL 0) y de las capturas del login antes/después. Informe completo: [23_informe_regresion_revision_final.md](../23_informe_regresion_revision_final.md).

## Contenido

| Archivo | Qué demuestra |
|---|---|
| `resultados_133_pruebas.csv` | Las 133 pruebas con su resultado (PASS) y el detalle de lo comprobado |
| `log_133_pruebas.txt` | Salida completa de la ronda final, con el bloque `REVISION FINAL` y el total 133/133 |
| `bateria_revision.ps1` | Guion de pruebas (PowerShell 5.1 + curl.exe) con el que se obtuvieron esos resultados |
| `verificacion_enlaces.txt` | Revisión de enlaces: 345 de Markdown, 516 de la maqueta V1 y 56 páginas V2 en vivo, 0 rotos |
| `login_antes.html` | Login previo a la revisión (estructura original) |
| `login_antes_error.html` | Aviso de error previo: usaba `class="notice notice-error"`, clase inexistente en el CSS |
| `login_antes_logout.html` | Aviso de cierre de sesión previo |
| `login_final.html` | Login rediseñado: tarjeta, cuenta demo sin contraseña visible, accesibilidad |
| `login_final_error.html` | Aviso de error corregido: `class="notice error login-aviso"` con `role="alert"` |
| `login_final_logout.html` | Aviso de cierre de sesión corregido: `class="notice ok login-aviso"` con `role="status"` |

## Cómo verificar

1. Arrancar la aplicación con la semilla: `mvn spring-boot:run` (puerto 8080).
2. Exportar la contraseña de la cuenta demo por variable de entorno: `$env:NEXO_DEMO_PASS = "<la contraseña indicada por el Administrador>"`. El guion no la incluye.
3. Ejecutar `bateria_revision.ps1` con PowerShell 5.1. Esperado: `PASS 133, FAIL 0`.
4. Ejecutar el verificador de enlaces (parte estática y recorrido en vivo con sesión de Administrador). Esperado: `ENLACES ROTOS: 0`.

## Higiene de los archivos

Los archivos se copiaron **antes** de subirlos al repositorio con las siguientes sustituciones, verificadas por auditoría automática (`AUDITORIA OK`):

- Contraseña de demostración → `[CONTRASENA-OCULTA]` (y por `$passDemo`/variable de entorno dentro del guion).
- Token CSRF de cada sesión → `[TOKEN-OCULTO]`.
- Rutas locales del equipo (`C:\Users\...`) → `%TEMP%`.
- No se incluyen cookies, identificadores de sesión ni datos de personas reales; las cuentas citadas son datos de prueba del semillero del curso.
