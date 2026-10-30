"""Verifica el DDL objetivo en un contenedor aislado; NO prueba los servicios Java.

Uso: python verificar_esquema_mysql.py nexo-auditoria-20261030 [--persistencia]
Solo admite contenedores de pruebas con prefijo nexo-auditoria- y BD nexo_auditoria.
"""
import json
from pathlib import Path
import subprocess
import sys

contenedor = sys.argv[1]
if not contenedor.startswith("nexo-auditoria-"):
    raise SystemExit("Se requiere un contenedor de pruebas exclusivo nexo-auditoria-*")
raiz = Path(__file__).resolve().parents[2]
resultados = []


def sql(texto, error=None):
    comando = ["docker", "exec", "-i", contenedor, "sh", "-c",
               'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" exec mysql -uroot --batch --skip-column-names nexo_auditoria']
    p = subprocess.run(comando, input="SET time_zone='-05:00';\n" + texto,
                       text=True, capture_output=True, encoding="utf-8", timeout=30)
    if error is None:
        if p.returncode != 0:
            raise AssertionError(p.stderr)
    else:
        assert p.returncode != 0 and f"ERROR {error}" in p.stderr, p.stderr
    return p.stdout.strip()


def comprobar(nombre, query, esperado):
    obtenido = sql(query)
    assert obtenido == esperado, f"{nombre}: {obtenido!r} != {esperado!r}"
    resultados.append({"prueba": nombre, "resultado": "PASS"})


def rechazo(nombre, query, codigo):
    sql(query, error=codigo)
    resultados.append({"prueba": nombre, "resultado": "PASS"})


if "--persistencia" in sys.argv:
    comprobar("Datos conservados tras reiniciar MySQL", "SELECT COUNT(*) FROM categoria WHERE nombre='Gasolinas';", "1")
    comprobar("Movimientos conservados tras reiniciar MySQL", "SELECT COUNT(*) FROM movimiento_caja;", "2")
    comprobar("Zona de sesión Perú", "SELECT @@session.time_zone;", "-05:00")
    salida = raiz / "documentacion/evidencias/resultados_mysql_reinicio.json"
else:
    assert sql("SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=DATABASE();") == "0", "La BD de pruebas debe estar vacía"
    sql((raiz / "documentacion/persistencia/V1__modelo_objetivo.sql").read_text(encoding="utf-8"))
    comprobar("15 tablas del diccionario", "SELECT COUNT(*) FROM information_schema.tables WHERE table_schema=DATABASE();", "15")
    comprobar("109 columnas del diccionario", "SELECT COUNT(*) FROM information_schema.columns WHERE table_schema=DATABASE();", "109")
    comprobar("22 relaciones FK", "SELECT COUNT(*) FROM information_schema.referential_constraints WHERE constraint_schema=DATABASE();", "22")
    comprobar("15 PK", "SELECT COUNT(*) FROM information_schema.table_constraints WHERE constraint_schema=DATABASE() AND constraint_type='PRIMARY KEY';", "15")
    # Datos ficticios exclusivos de la prueba. El hash no corresponde a una credencial real.
    sql("""
INSERT INTO categoria VALUES (1,'Gasolinas','Prueba aislada','Activo',NOW());
INSERT INTO producto VALUES (1,'PR01',1,'Gasolina Regular','L',5.00,10.125,'Activo');
INSERT INTO proveedor VALUES (1,'00000000001','Proveedor ficticio SQL',NULL,NULL,NULL,NULL,'Activo');
INSERT INTO empleado VALUES (1,'00000003','Elena','Díaz','Administradora','000 000 003',NULL,'Activo');
INSERT INTO rol VALUES (1,'ADMINISTRADOR','Administrador','Administración','Activo');
INSERT INTO usuario VALUES (1,1,1,'prueba_sql','DATO_FICTICIO_SIN_ACCESO','Activo',NOW(),NULL);
INSERT INTO compra VALUES (1,'C001',1,1,NOW(),4.00,'Confirmada',NULL);
INSERT INTO detalle_compra VALUES (1,1,1,1.000,4.00,4.00);
INSERT INTO venta VALUES (1,'V001',1,NOW(),5.00,'Confirmada');
INSERT INTO detalle_venta VALUES (1,1,1,1.000,5.00,5.00);
INSERT INTO movimiento_inventario VALUES (1,1,1,1,NULL,'ENTRADA',1.000,NOW(),'Compra C001');
INSERT INTO movimiento_inventario VALUES (2,1,1,NULL,1,'SALIDA',1.000,NOW(),'Venta V001');
INSERT INTO concepto_movimiento VALUES (1,'CE01','Venta de combustible','INGRESO','Activo');
INSERT INTO concepto_movimiento VALUES (2,'CE02','Compra de combustible','EGRESO','Activo');
INSERT INTO caja_diaria VALUES (1,CURRENT_DATE(),10.00,NULL,'ABIERTA',1,NULL,NOW(),NULL);
INSERT INTO movimiento_caja VALUES (1,'MC001',1,2,1,1,NULL,'EGRESO',4.00,'Compra C001',NOW());
INSERT INTO movimiento_caja VALUES (2,'MC002',1,1,1,NULL,1,'INGRESO',5.00,'Venta V001',NOW());
INSERT INTO asistencia VALUES (1,1,CURRENT_DATE(),'08:00','17:00','PRESENTE',NULL);
""")
    comprobar("DECIMAL de litros conserva milésimas", "SELECT stock_actual FROM producto WHERE id_producto=1;", "10.125")
    comprobar("Conciliación SQL apertura + ingresos - egresos", "SELECT saldo_apertura+(SELECT SUM(CASE WHEN tipo='INGRESO' THEN monto ELSE -monto END) FROM movimiento_caja WHERE id_caja=1) FROM caja_diaria WHERE id_caja=1;", "11.00")
    rechazo("Rechaza stock negativo", "UPDATE producto SET stock_actual=-1 WHERE id_producto=1;", 3819)
    rechazo("Rechaza FK de categoría inexistente", "UPDATE producto SET id_categoria=999 WHERE id_producto=1;", 1452)
    rechazo("Conserva producto con operaciones", "DELETE FROM producto WHERE id_producto=1;", 1451)
    rechazo("Una jornada por empleado y fecha", "INSERT INTO asistencia VALUES (2,1,CURRENT_DATE(),'09:00',NULL,'PRESENTE',NULL);", 1062)
    rechazo("Salida posterior a entrada", "UPDATE asistencia SET hora_salida='07:00' WHERE id_asistencia=1;", 3819)
    rechazo("Una caja por fecha", "INSERT INTO caja_diaria VALUES (2,CURRENT_DATE(),0,NULL,'ABIERTA',1,NULL,NOW(),NULL);", 1062)
    rechazo("Una entrada por detalle de compra", "INSERT INTO movimiento_inventario VALUES (3,1,1,1,NULL,'ENTRADA',1,NOW(),'Duplicado');", 1062)
    rechazo("Un ingreso por venta", "INSERT INTO movimiento_caja VALUES (3,'MC003',1,1,1,NULL,1,'INGRESO',5,'Duplicado',NOW());", 1062)
    rechazo("Movimiento de caja exige origen", "INSERT INTO movimiento_caja VALUES (3,'MC003',1,1,1,NULL,NULL,'INGRESO',5,'Sin origen',NOW());", 3819)
    rechazo("Rollback SQL de operación con FK inválida", "START TRANSACTION; INSERT INTO compra VALUES (2,'C002',1,1,NOW(),4,'Confirmada',NULL); INSERT INTO detalle_compra VALUES (2,2,999,1,4,4); COMMIT;", 1452)
    comprobar("No queda cabecera parcial tras rollback SQL", "SELECT COUNT(*) FROM compra WHERE id_compra=2;", "0")
    salida = raiz / "documentacion/evidencias/resultados_mysql_esquema.json"

salida.write_text(json.dumps({"alcance": "Esquema SQL aislado, no persistencia de la aplicación Java",
                             "pruebas": resultados}, ensure_ascii=False, indent=2), encoding="utf-8")
print(json.dumps({"pruebas": len(resultados), "fallos": 0, "alcance": "DDL MySQL aislado"}))
