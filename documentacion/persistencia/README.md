> Antecedente de la primera fase. Las migraciones activas ya están integradas
> en `src/main/resources/db/migration/`. Véase [el estado actual](../27_persistencia_real_y_validacion.md).
> Se conserva el texto original como evidencia de la preparación del esquema.

# Modelo relacional preparado

`V1__modelo_objetivo.sql` reproduce el diccionario de la sección 5.10.2 del
Word corregido: 15 tablas, 109 columnas, 15 PK y 22 FK. Usa BIGINT para los
identificadores internos y conserva códigos comerciales independientes,
DECIMAL(12,3) para litros y DECIMAL(12,2) para dinero. No agrega Cliente.

Las FK restringen el borrado de registros relacionados. Se incluyen unicidad
de origen para movimientos físicos y económicos, una cuenta por empleado,
una asistencia por empleado/fecha y una caja por fecha. Las validaciones
de estado activo, autorización, caja abierta y sumas de detalles requieren
servicios transaccionales: no se consideran resueltas por este DDL.

El servidor MySQL no necesita cargar tablas de zonas horarias para este script:
se fija `-05:00` por sesión, correspondiente a America/Lima. Las marcas de tiempo
de la aplicación deben generarse en servidor y persistirse con esa misma zona.

Este archivo está en documentación deliberadamente: **todavía no hay Flyway ni
JPA conectados a los servicios Java**. No ejecutar sobre una base con datos reales
como si fuera una migración ya integrada. El esquema se validó en una base nueva
y aislada MySQL 8.4. Las tablas, índices y restricciones son SQL ejecutable, no
evidencia de persistencia funcional de las pantallas.

## Reproducir la verificación aislada

En PowerShell, crear un contenedor de pruebas sin puertos publicados:

```powershell
$env:MYSQL_ROOT_PASSWORD=[guid]::NewGuid().ToString('N')
docker run -d --name nexo-auditoria-local --network none -e MYSQL_ROOT_PASSWORD -e MYSQL_DATABASE=nexo_auditoria mysql:8.4
```

Cuando MySQL termine de iniciar, ejecutar con Python 3:

```powershell
python documentacion/evidencias/verificar_esquema_mysql.py nexo-auditoria-local
docker restart nexo-auditoria-local
# Esperar a que MySQL vuelva a estar disponible.
python documentacion/evidencias/verificar_esquema_mysql.py nexo-auditoria-local --persistencia
```

El verificador exige una base vacía, restringe el nombre del contenedor y usa
datos ficticios. No publica ni imprime la contraseña del contenedor. Los resultados
se guardan en `documentacion/evidencias/resultados_mysql_*.json`.

Para limpiar únicamente ese contenedor de prueba al terminar:

```powershell
docker rm -f -v nexo-auditoria-local
Remove-Item Env:MYSQL_ROOT_PASSWORD
```

Esto no crea usuarios de la aplicación. Los datos iniciales de producción y su
administrador deben incorporarse al completar la migración a Repository/JPA.
