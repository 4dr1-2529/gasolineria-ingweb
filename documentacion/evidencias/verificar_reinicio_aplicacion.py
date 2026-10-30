"""Prueba HTTP real del WAR y reinicio de su proceso, sin reconstruir ni limpiar la BD.

Solo admite el contenedor de integración creado para esta revisión. Las altas se
realizan por formularios con login y CSRF; SQL se usa únicamente para verificar.
La configuración de credenciales se lee de target/nexo-test-config.json (ignorado).
"""
import hashlib
import http.cookiejar
import json
import os
from pathlib import Path
import re
import secrets
import subprocess
import time
import urllib.parse
import urllib.request
from datetime import datetime, timezone, timedelta
from decimal import Decimal

RAIZ = Path(__file__).resolve().parents[2]
CONTENEDOR = os.environ.get("NEXO_MYSQL_TEST_CONTAINER", "nexo-integracion-20261030")
BASE = "http://127.0.0.1:18080"
WAR = RAIZ / "target/estacion-nexo.war"
config_local = RAIZ / "target/nexo-test-config.json"
CONFIG = json.loads(config_local.read_text(encoding="utf-8-sig")) if config_local.is_file() else {}
password_db = os.environ.get("NEXO_DB_PASSWORD", CONFIG.get("password"))
PASS_DEMO = os.environ.get("NEXO_DEMO_PASSWORD", CONFIG.get("demo"))
if not password_db or not PASS_DEMO:
    raise SystemExit("Configura NEXO_DB_PASSWORD y NEXO_DEMO_PASSWORD para la base local de integración.")
ENV = dict(os.environ, NEXO_DB_URL="jdbc:mysql://127.0.0.1:13306/estacion_nexo?connectionTimeZone=America/Lima&forceConnectionTimeZoneToSession=true&allowPublicKeyRetrieval=true&useSSL=false", NEXO_DB_USER="nexo", NEXO_DB_PASSWORD=password_db, NEXO_DEMO_PASSWORD=PASS_DEMO)
proceso = None
archivo_log = None
comprobaciones = []


def sql(consulta):
    resultado = subprocess.run(["docker", "exec", "-i", CONTENEDOR, "sh", "-c", 'MYSQL_PWD="$MYSQL_ROOT_PASSWORD" exec mysql -uroot --batch --skip-column-names estacion_nexo'], input="SET time_zone='-05:00';\n" + consulta, text=True, capture_output=True, encoding="utf-8", timeout=30)
    if resultado.returncode:
        raise AssertionError(resultado.stderr)
    return resultado.stdout.strip()


def comprobar(nombre, condicion):
    if not condicion:
        raise AssertionError(nombre)
    comprobaciones.append({"prueba": nombre, "resultado": "PASS"})


def iniciar(numero):
    global proceso, archivo_log
    if not WAR.is_file():
        raise AssertionError("No existe el WAR; ejecutar Maven package antes de esta prueba.")
    archivo_log = (RAIZ / f"target/reinicio-app-{numero}.log").open("w", encoding="utf-8")
    opciones = {"creationflags": subprocess.CREATE_NO_WINDOW} if os.name == "nt" else {}
    proceso = subprocess.Popen(["java", "-Xms64m", "-Xmx512m", "-jar", str(WAR), "--server.port=18080", "--spring.profiles.active=demo"], cwd=RAIZ, env=ENV, stdout=archivo_log, stderr=subprocess.STDOUT, **opciones)
    limite = time.monotonic() + 150
    while time.monotonic() < limite:
        if proceso.poll() is not None:
            raise AssertionError(f"El WAR terminó al iniciar (código {proceso.returncode}); revisar target/reinicio-app-{numero}.log")
        try:
            with urllib.request.urlopen(BASE + "/login", timeout=2) as respuesta:
                if respuesta.status == 200:
                    print(f"Aplicación {numero} disponible.", flush=True)
                    return
        except (OSError, urllib.error.URLError):
            time.sleep(1)
    raise AssertionError("La aplicación no inició en el plazo de la prueba")


def detener():
    global proceso, archivo_log
    if proceso and proceso.poll() is None:
        proceso.terminate()
        try:
            proceso.wait(timeout=30)
        except subprocess.TimeoutExpired:
            proceso.kill()
            proceso.wait(timeout=10)
    if archivo_log:
        archivo_log.close()
    proceso = None


def cliente():
    return urllib.request.build_opener(urllib.request.HTTPCookieProcessor(http.cookiejar.CookieJar()))


def pagina(navegador, ruta):
    with navegador.open(BASE + ruta, timeout=40) as respuesta:
        return respuesta.read().decode("utf-8")


def campo(html, nombre):
    m = re.search(r'name="' + re.escape(nombre) + r'"[^>]*value="([^"]+)"', html)
    if not m:
        raise AssertionError(f"Falta el campo {nombre} en el formulario")
    return m[1]


def enviar(navegador, ruta, datos, formulario):
    payload = dict(datos, _csrf=campo(formulario, "_csrf"))
    with navegador.open(BASE + ruta, urllib.parse.urlencode(payload).encode(), timeout=40) as respuesta:
        return respuesta.read().decode("utf-8")


def login(username):
    c = cliente()
    html = enviar(c, "/login", {"username": username, "password": PASS_DEMO}, pagina(c, "/login"))
    comprobar(f"Login HTTP {username}", "Cerrar sesión" in html)
    return c


def codigo(token, prefijo):
    return prefijo + hashlib.sha256(token.encode()).hexdigest()[:11].upper()


def snapshot(compra, venta, ruc, motivo):
    return {
        "proveedor": sql(f"SELECT id_proveedor,ruc,razon_social,estado FROM proveedor WHERE ruc='{ruc}';"),
        "compra": sql(f"SELECT codigo_compra,total,id_usuario,fecha_hora FROM compra WHERE codigo_compra='{compra}';"),
        "detalle_compra": sql(f"SELECT p.codigo_producto,d.cantidad_litros,d.precio_compra,d.subtotal FROM detalle_compra d JOIN compra c ON c.id_compra=d.id_compra JOIN producto p ON p.id_producto=d.id_producto WHERE c.codigo_compra='{compra}' ORDER BY d.id_detalle_compra;"),
        "venta": sql(f"SELECT codigo_venta,total,id_usuario,fecha_hora FROM venta WHERE codigo_venta='{venta}';"),
        "detalle_venta": sql(f"SELECT p.codigo_producto,d.cantidad_litros,d.precio_unitario,d.subtotal FROM detalle_venta d JOIN venta v ON v.id_venta=d.id_venta JOIN producto p ON p.id_producto=d.id_producto WHERE v.codigo_venta='{venta}' ORDER BY d.id_detalle_venta;"),
        "stock": sql("SELECT codigo_producto,stock_actual FROM producto WHERE codigo_producto IN ('PR01','PR02','PR03') ORDER BY codigo_producto;"),
        "usuarios": sql("SELECT id_usuario,username,id_empleado,id_rol,estado,fecha_creacion FROM usuario ORDER BY id_usuario;"),
        "caja_diaria": sql("SELECT id_caja,fecha,saldo_apertura,saldo_cierre,estado,id_usuario_apertura,id_usuario_cierre,fecha_apertura,fecha_cierre FROM caja_diaria WHERE fecha=CURRENT_DATE();"),
        "movimientos_inventario": sql(f"SELECT m.id_movimiento_inventario,m.tipo,m.cantidad_litros,m.id_usuario,m.fecha_hora FROM movimiento_inventario m LEFT JOIN detalle_compra dc ON dc.id_detalle_compra=m.id_detalle_compra LEFT JOIN compra c ON c.id_compra=dc.id_compra LEFT JOIN detalle_venta dv ON dv.id_detalle_venta=m.id_detalle_venta LEFT JOIN venta v ON v.id_venta=dv.id_venta WHERE c.codigo_compra='{compra}' OR v.codigo_venta='{venta}' ORDER BY m.id_movimiento_inventario;"),
        "caja": sql(f"SELECT m.codigo_movimiento,m.tipo,m.monto,m.id_usuario,m.fecha_hora FROM movimiento_caja m LEFT JOIN compra c ON c.id_compra=m.id_compra LEFT JOIN venta v ON v.id_venta=m.id_venta WHERE c.codigo_compra='{compra}' OR v.codigo_venta='{venta}' ORDER BY m.id_movimiento_caja;"),
        "ajuste": sql(f"SELECT id_movimiento_inventario,cantidad_litros,id_usuario,fecha_hora FROM movimiento_inventario WHERE motivo='{motivo}';"),
        "asistencia": sql("SELECT id_empleado,fecha,hora_entrada,estado FROM asistencia WHERE id_empleado=2 AND fecha=CURRENT_DATE();"),
    }


try:
    iniciar(1)
    admin = login("ediaz")
    sufijo = secrets.token_hex(4)
    ruc = "20" + f"{secrets.randbelow(1000000000):09d}"
    razon = "Proveedor reinicio " + sufijo
    enviar(admin, "/proveedores/crear", {"ruc": ruc, "razonSocial": razon, "estado": "Activo"}, pagina(admin, "/proveedores/crear"))
    proveedor_id = sql(f"SELECT id_proveedor FROM proveedor WHERE ruc='{ruc}';")
    comprobar("Alta de proveedor por JSP persistida", proveedor_id.isdigit())
    previos = {c: Decimal(s) for c, s in (fila.split("\t") for fila in sql("SELECT codigo_producto,stock_actual FROM producto WHERE codigo_producto IN ('PR01','PR02','PR03');").splitlines())}
    f = pagina(admin, "/compras/crear")
    token_c = campo(f, "formularioToken")
    compra = codigo(token_c, "C")
    datos = {"formularioToken": token_c, "idProveedor": proveedor_id, "idUsuario": "1", "idProducto": "PR01", "cantidad": "1.125", "precioCompra": "4.50", "lineas[0].idProducto": "PR02", "lineas[0].cantidad": "2", "lineas[0].precioCompra": "5.40", "lineas[1].idProducto": "PR03", "lineas[1].cantidad": "3", "lineas[1].precioCompra": "3.60"}
    enviar(admin, "/compras/crear", datos, f)
    comprobar("Compra de tres líneas confirmada por HTTP", sql(f"SELECT COUNT(*) FROM detalle_compra d JOIN compra c ON c.id_compra=d.id_compra WHERE c.codigo_compra='{compra}';") == "3")
    comprobar("Compra conserva responsable autenticado", sql(f"SELECT id_usuario FROM compra WHERE codigo_compra='{compra}';") == "3")
    enviar(admin, "/compras/crear", datos, f)
    comprobar("Reenvío HTTP no duplica la compra", sql(f"SELECT COUNT(*) FROM compra WHERE codigo_compra='{compra}';") == "1")
    vendedor = login("lrojas")
    f = pagina(vendedor, "/ventas/crear")
    token_v = campo(f, "formularioToken")
    venta = codigo(token_v, "V")
    enviar(vendedor, "/ventas/crear", {"formularioToken": token_v, "idUsuario": "3", "idProducto": "PR01", "cantidad": "0.125", "lineas[0].idProducto": "PR02", "lineas[0].cantidad": "0.250"}, f)
    comprobar("Venta de dos líneas y precio de servidor", sql(f"SELECT total FROM venta WHERE codigo_venta='{venta}';") == "2.13")
    operador = login("atorres")
    f = pagina(operador, "/inventario/entrada/crear")
    motivo = "Reconteo reinicio " + sufijo
    enviar(operador, "/inventario/entrada/crear", {"formularioToken": campo(f, "formularioToken"), "idUsuario": "3", "idProducto": "PR03", "cantidad": "0.375", "motivo": motivo}, f)
    f = pagina(vendedor, "/asistencia/mi")
    enviar(vendedor, "/asistencia/entrada", {"idEmpleado": "3", "fecha": "2000-01-01", "horaEntrada": "01:00"}, f)
    antes = snapshot(compra, venta, ruc, motivo)
    stock = {c: Decimal(s) for c, s in (fila.split("\t") for fila in antes["stock"].splitlines())}
    comprobar("Inventario de compra y venta mantiene milésimas", stock["PR01"] == previos["PR01"] + Decimal("1.000") and stock["PR02"] == previos["PR02"] + Decimal("1.750") and stock["PR03"] == previos["PR03"] + Decimal("3.375"))
    comprobar("Un ingreso y un egreso para las operaciones HTTP", len(antes["caja"].splitlines()) == 2)
    comprobar("Tres entradas y dos salidas vinculadas a sus detalles", len(antes["movimientos_inventario"].splitlines()) == 5)
    comprobar("Asistencia propia con fecha de servidor", bool(antes["asistencia"]) and antes["asistencia"].startswith("2\t"))
    comprobar("Ajuste manual persistido con operador autenticado", "\t0.375\t1\t" in antes["ajuste"])
    print("Altas HTTP confirmadas; reiniciando únicamente la aplicación Java.", flush=True)
    detener()
    iniciar(2)
    despues = snapshot(compra, venta, ruc, motivo)
    for nombre, valor in antes.items():
        comprobar(f"Persistencia tras reinicio de aplicación: {nombre}", valor == despues[nombre])
    admin = login("ediaz")
    comprobar("Compra recuperada por JSP después del reinicio", compra in pagina(admin, "/compras/detalle?id=" + compra))
    comprobar("Venta recuperada por JSP después del reinicio", venta in pagina(admin, "/ventas/detalle?id=" + venta))
    comprobar("Proveedor recuperado por filtro JSP después del reinicio", ruc in pagina(admin, "/proveedores/list?ruc=" + ruc))
    comprobar("Finanzas recupera origen persistido", compra in pagina(admin, "/finanzas/list"))
    evidencia = {"fecha": datetime.now(timezone(timedelta(hours=-5))).isoformat(), "alcance": "Formularios reales del WAR con MySQL; reinicio del proceso Java conservando la BD", "compra": compra, "venta": venta, "antes": antes, "despues": despues, "pruebas": comprobaciones}
    (RAIZ / "documentacion/evidencias/resultados_reinicio_aplicacion.json").write_text(json.dumps(evidencia, ensure_ascii=False, indent=2), encoding="utf-8")
    print(json.dumps({"pruebas": len(comprobaciones), "fallos": 0, "reinicio_aplicacion": True}), flush=True)
finally:
    detener()
