# REVISION FINAL - Regresion integral de Estacion Nexo (V2)
# Base: bateria de la ETAPA 10 (126 pruebas), actualizada tras la revision:
# textos de interfaz sin tecnicismos, fechas del servidor (America/Lima)
# y pantalla de login accesible. Nuevas pruebas: D02/D08, I02/I05/I08 y V06
# (fecha del servidor), D12/I12/V13 (formularios) y M01-M04 (textos).
# Compatible con PowerShell 5.1: texto ASCII puro; mensajes de servidor sin acentos.
$ErrorActionPreference = "Stop"
$base = "http://localhost:8080"
$dir = "$env:TEMP\opencode"
$hoy = Get-Date -Format "dd/MM/yyyy"
$passDemo = $env:NEXO_DEMO_PASS  # contrasena de la cuenta demo: se pasa por entorno, no en el repositorio
$global:res = @()

function Registrar($id, $ok, $detalle) {
    $global:res += [pscustomobject]@{ Prueba = $id; Resultado = $(if ($ok) { "PASS" } else { "FAIL" }); Detalle = $detalle }
    Write-Host ("[{0}] {1} - {2}" -f $(if ($ok) { "PASS" } else { "FAIL" }), $id, $detalle)
}

function EsperarApp() {
    for ($i = 0; $i -lt 60; $i++) {
        $out = curl.exe -s -o NUL -w "%{http_code}" --max-time 5 "$base/login"
        if ($out -eq "200") { return $true }
        Start-Sleep -Seconds 2
    }
    return $false
}

# Abre sesion y devuelve el token CSRF de esa sesion (se toma de /asistencia/mi)
function Login($usuario, $clave, $jar) {
    if (Test-Path $jar) { Remove-Item $jar }
    curl.exe -s -c $jar -o NUL --max-time 10 "$base/login" | Out-Null
    $html = curl.exe -s -b $jar -c $jar --max-time 10 "$base/login"
    $tok = [regex]::Match($html, 'name="_csrf" value="([^"]+)"').Groups[1].Value
    curl.exe -s -b $jar -c $jar -o NUL --max-redirs 0 --max-time 10 -d "username=$usuario&password=$clave&_csrf=$tok" "$base/login" | Out-Null
    $html2 = curl.exe -s -b $jar -c $jar --max-time 10 "$base/asistencia/mi"
    $tok2 = [regex]::Match($html2, 'name="_csrf" value="([^"]+)"').Groups[1].Value
    return $tok2
}

function Cuerpo($jar, $ruta, $archivo) {
    curl.exe -s -b $jar -o "$dir\$archivo" --max-time 10 "$base$ruta" | Out-Null
    if (Test-Path "$dir\$archivo") { return [System.IO.File]::ReadAllText("$dir\$archivo", [System.Text.Encoding]::UTF8) }
    return ""
}

# Cuenta filas del tbody de la tabla que sigue a la ancla indicada
function FilasTabla($html, $ancla) {
    $i = $html.IndexOf($ancla)
    if ($i -lt 0) { return -1 }
    $j = $html.IndexOf("</table>", $i)
    if ($j -lt 0) { return -1 }
    $tabla = $html.Substring($i, $j - $i)
    $k = $tabla.IndexOf("<tbody>")
    if ($k -lt 0) { return -1 }
    $cuerpo = $tabla.Substring($k)
    return [regex]::Matches($cuerpo, "(?s)<tr>.*?</tr>").Count
}

# Stock en litros de un producto segun la tabla de existencias de inventario
function StockDe($html, $nombre) {
    $m = [regex]::Match($html, "(?s)" + [regex]::Escape($nombre) + "</td>.*?<td>([\d,\.]+) L</td>")
    if (-not $m.Success) { return -1 }
    return [double]($m.Groups[1].Value -replace ',', '')
}

# POST con token; devuelve el codigo HTTP
function PostTok($jar, $ruta, $datos, $tok) {
    $args = @("-s", "-b", $jar, "-o", "NUL", "-w", "%{http_code}", "--max-redirs", "0", "--max-time", "10")
    foreach ($d in $datos) { $args += @("--data-urlencode", $d) }
    $args += @("--data-urlencode", "_csrf=$tok")
    $args += "$base$ruta"
    return (curl.exe @args)
}

if (-not (EsperarApp)) { Write-Host "LA APP NO ARRANCO"; exit 1 }

# Sesiones: ediaz (Administrador), atorres y lrojas (Operadores)
$jarA = "$dir\jar_e10a.txt"
$jarO = "$dir\jar_e10o.txt"
$jarL = "$dir\jar_e10l.txt"
$tokA = Login "ediaz" $passDemo $jarA
$tokO = Login "atorres" $passDemo $jarO
$tokL = Login "lrojas" $passDemo $jarL

# ================= SEMILLA VERIFICADA (S01-S07) =================
$cat0 = Cuerpo $jarA "/categorias/list" "s1.html"
$pro0 = Cuerpo $jarA "/combustibles/list" "s2.html"
$emp0 = Cuerpo $jarA "/empleados/list" "s3.html"
$con0 = Cuerpo $jarA "/finanzas/conceptos/list" "s4.html"
$inv0 = Cuerpo $jarA "/inventario/list" "s5.html"
$fin0 = Cuerpo $jarA "/finanzas/list" "s6.html"
$ctl0 = Cuerpo $jarA "/asistencia/control" "s7.html"

$ok = (FilasTabla $cat0 "registrad") -eq 2
Registrar "S01-semilla-categorias" $ok "2 categorias en memoria (Gasolinas, Disel)"
$ok = (FilasTabla $pro0 "registrad") -eq 3
Registrar "S02-semilla-combustibles" $ok "3 combustibles (PR01 1990 L, PR02 980 L, PR03 3950 L)"
$ok = (FilasTabla $emp0 "registrad") -eq 3
Registrar "S03-semilla-empleados" $ok "3 empleados (DNI 00000001/2/3)"
$ok = (FilasTabla $con0 "registrad") -eq 2
Registrar "S04-semilla-conceptos" $ok "2 conceptos (CE01 Ingreso, CE02 Egreso)"
$ok = ((FilasTabla $inv0 "Libro de movimientos") -eq 6) -and ($inv0 -match "entradas 300 L") -and ($inv0 -match "salidas 80 L") -and ((StockDe $inv0 "Gasolina Regular") -eq 1990)
Registrar "S05-semilla-inventario" $ok "libro 6 filas, entradas 300 L, salidas 80 L, PR01=1990 L"
$ok = ($fin0 -match "4,410.00") -and ($fin0 -match "3,430.00") -and ((FilasTabla $fin0 "recientes") -eq 4)
Registrar "S06-semilla-finanzas" $ok "apertura 4,410.00, saldo 3,430.00, 4 movimientos de caja"
$ok = (FilasTabla $ctl0 "registrad") -eq 5
Registrar "S07-semilla-asistencia" $ok "5 marcaciones historicas; ninguna el dia de hoy"

# ================= ACCESO ANONIMO Y LOGIN (A01-A08) =================
$a = curl.exe -s -o NUL -w "%{http_code}" --max-time 10 "$base/login"
Registrar "A01-anonimo-login" ($a -eq "200") "GET /login publico -> $a"

$a = curl.exe -s -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 "$base/ventas/list"
Registrar "A02-anonimo-protegida-redirect" ($a -match "302" -and $a -match "/login") "GET /ventas/list anonimo -> $a"

$a = curl.exe -s -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 "$base/finanzas/list"
Registrar "A03-anonimo-finanzas" ($a -match "302" -and $a -match "/login") "GET /finanzas/list anonimo -> $a"

$a = curl.exe -s -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "nombre=Hacker&estado=Activo" "$base/categorias/crear"
$cat1 = Cuerpo $jarA "/categorias/list" "a4.html"
$ok = ($a -match "302" -and $a -match "/login") -and ((FilasTabla $cat1 "registrad") -eq 2)
Registrar "A04-anonimo-post-sin-efecto" $ok "POST anonimo redirige a login y las categorias siguen en 2"

# Sesion anonima con su propio token de login para probar credenciales
$jarD = "$dir\jar_e10d.txt"
if (Test-Path $jarD) { Remove-Item $jarD }
curl.exe -s -c $jarD -o NUL --max-time 10 "$base/login" | Out-Null
$hl = curl.exe -s -b $jarD -c $jarD --max-time 10 "$base/login"
$tl = [regex]::Match($hl, 'name="_csrf" value="([^"]+)"').Groups[1].Value

# El POST a /login sin token tambien es rechazado por CSRF (redirige sin motivo)
$a = curl.exe -s -b $jarD -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "username=ediaz&password=MalaClave123" "$base/login"
Registrar "A05-login-csrf" (($a -match "302") -and ($a -notmatch "error=")) "POST a /login sin token -> $a (CSRF protege el propio login)"

$a = curl.exe -s -b $jarD -c $jarD -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "username=ediaz&password=MalaClave123&_csrf=$tl" "$base/login"
Registrar "A06-login-credenciales-invalidas" ($a -match "302" -and $a -match "error=credenciales") "clave incorrecta con token valido -> $a"

$a = curl.exe -s -b $jarD -c $jarD -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "username=ediaz&password=$passDemo&_csrf=$tl" "$base/login"
$trasIngreso = curl.exe -s -L -b $jarD -o "$dir\a7.html" --max-time 10 "$base/"
$trasIngreso = [System.IO.File]::ReadAllText("$dir\a7.html", [System.Text.Encoding]::UTF8)
$ok = ($a -match "302") -and ($trasIngreso -match "Familias de combustible")
Registrar "A07-login-admin" $ok "ediaz ingresa -> $a y / abre la lista de categorias (inicio del Admin)"

# atorres en su propia sesion: el / debe abrir el historial de ventas
$jarT = "$dir\jar_e10t.txt"
if (Test-Path $jarT) { Remove-Item $jarT }
curl.exe -s -c $jarT -o NUL --max-time 10 "$base/login" | Out-Null
$ht = curl.exe -s -b $jarT -c $jarT --max-time 10 "$base/login"
$tt = [regex]::Match($ht, 'name="_csrf" value="([^"]+)"').Groups[1].Value
$a = curl.exe -s -b $jarT -c $jarT -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "username=atorres&password=$passDemo&_csrf=$tt" "$base/login"
curl.exe -s -L -b $jarT -o "$dir\a8.html" --max-time 10 "$base/" | Out-Null
$trasIngreso2 = [System.IO.File]::ReadAllText("$dir\a8.html", [System.Text.Encoding]::UTF8)
$ok = ($a -match "302") -and ($trasIngreso2 -match "Historial de ventas")
Registrar "A08-login-operador" $ok "atorres ingresa -> $a y / abre el historial de ventas (inicio del staff)"

# La pagina de login sigue abriendo con sesion abierta (el cierre es /logout)
$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 "$base/login"
$okLogin = Cuerpo $jarA "/categorias/list" "a9b.html"
Registrar "A09-login-con-sesion" (($a -eq "200") -and ($okLogin -match "Familias de combustible")) "GET /login con sesion -> 200 (pagina utilizable); la sesion activa se confirma en /categorias/list"

# ================= CATEGORIAS (C01-C12) =================
$a = PostTok $jarA "/categorias/crear" @("nombre=Gasolinas", "estado=Activo") $tokA
$cat2 = Cuerpo $jarA "/categorias/crear" "c2.html"
$ok = ($a -eq "302") -and ($cat2 -match "Ya existe una categor")
Registrar "C01-cat-duplicado" $ok "nombre repetido rechazado en el servidor"

$a = PostTok $jarA "/categorias/crear" @("nombre=AB", "estado=Activo") $tokA
$cat3 = Cuerpo $jarA "/categorias/crear" "c3.html"
$ok = ($cat3 -match "debe tener entre 3 y 40 caracteres")
Registrar "C02-cat-nombre-corto" $ok "2 caracteres rechazados (minimo 3)"

$a = PostTok $jarA "/categorias/crear" @("nombre=XTAPA10", "descripcion=$('x' * 250)", "estado=Activo") $tokA
$cat4 = Cuerpo $jarA "/categorias/crear" "c4.html"
$ok = ($cat4 -match "no puede superar los 200 caracteres")
Registrar "C03-cat-descripcion-larga" $ok "250 caracteres rechazados (maximo 200)"

# conservacion: tras el rechazo, lo digitado se conserva en el formulario
$ok = ($cat2 -match "Gasolinas")
Registrar "C04-cat-conserva-digitado" $ok "tras el rechazo el formulario conserva el nombre digitado"

$a = PostTok $jarA "/categorias/crear" @("nombre=Combustibles ETAPA10", "descripcion=Categoria creada por la bateria de regresion", "estado=Activo") $tokA
$cat5 = Cuerpo $jarA "/categorias/list" "c5.html"
$ok = ($a -eq "302") -and ($cat5 -match "Combustibles ETAPA10") -and ((FilasTabla $cat5 "registrad") -eq 3)
Registrar "C05-cat-crear-valido" $ok "alta correcta: 3 categorias en memoria"

$a = PostTok $jarA "/categorias/editar" @("id=3", "nombre=Combustibles ETAPA10", "descripcion=Descripcion editada en ETAPA10", "estado=Activo") $tokA
$cat6 = Cuerpo $jarA "/categorias/list" "c6.html"
$ok = ($a -eq "302") -and ($cat6 -match "se edit") -and ($cat6 -match "Descripcion editada en ETAPA10")
Registrar "C06-cat-editar-valido" $ok "edicion reflejada en la lista"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 "$base/categorias/editar?id=999"
$cat7 = Cuerpo $jarA "/categorias/list" "c7.html"
$ok = ($a -match "302") -and ($cat7 -match "No se encontr")
Registrar "C07-cat-editar-inexistente" $ok "id 999 redirige con aviso y sin romper"

$a = PostTok $jarA "/categorias/estado" @("id=3", "estado=Inactivo") $tokA
$cat8 = Cuerpo $jarA "/categorias/list" "c8.html"
$ok = ($cat8 -match "Combustibles ETAPA10") -and ((FilasTabla $cat8 "registrad") -eq 3)
Registrar "C08-cat-estado-rn02" $ok "cambio de estado: registro conservado (RN02), 3 filas"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "id=3&_csrf=$tokA" "$base/categorias/eliminar"
$cat9 = Cuerpo $jarA "/categorias/list" "c9.html"
$ok = ($a -eq "404") -and ((FilasTabla $cat9 "registrad") -eq 3)
Registrar "C09-cat-eliminar-404-rn02" $ok "POST /categorias/eliminar -> 404 y las 3 categorias siguen (RN02)"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "nombre=SinToken&estado=Activo" "$base/categorias/crear"
Registrar "C10-cat-csrf-403" ($a -eq "403") "POST sin token CSRF -> $a"

$a = PostTok $jarO "/categorias/crear" @("nombre=Operador No Puede", "estado=Activo") $tokO
$catO = Cuerpo $jarO "/categorias/list" "c11.html"
$ok = ($catO -match "no te corresponde") -and ($a -eq "403")
Registrar "C11-cat-operador-403" $ok "POST del Operador -> 403 con aviso de permisos y la lista tambien lo niega"

$catFin = Cuerpo $jarA "/categorias/list" "c12.html"
$ok = ($catFin -notmatch "Operador No Puede")
Registrar "C12-cat-operador-sin-efecto" $ok "el intento del Operador no creo categorias"

# ================= COMBUSTIBLES (P01-P11) =================
$a = PostTok $jarA "/combustibles/crear" @("nombre=Gasolina Regular", "idCategoria=1", "unidadMedida=Litro", "precioActual=5.00", "stock=0", "estado=Activo") $tokA
$pr2 = Cuerpo $jarA "/combustibles/crear" "p2.html"
$ok = ($pr2 -match "Ya existe un combustible")
Registrar "P01-prod-duplicado" $ok "nombre repetido rechazado"

$a = PostTok $jarA "/combustibles/crear" @("nombre=Prueba Sin Cat", "idCategoria=99", "unidadMedida=Litro", "precioActual=5.00", "stock=0", "estado=Activo") $tokA
$pr3 = Cuerpo $jarA "/combustibles/crear" "p3.html"
$ok = ($pr3 -match "seleccionada no existe")
Registrar "P02-prod-categoria-inexistente" $ok "categoria 99 rechazada"

$a = PostTok $jarA "/combustibles/crear" @("nombre=Prueba Precio", "idCategoria=1", "unidadMedida=Litro", "precioActual=0", "stock=0", "estado=Activo") $tokA
$pr4 = Cuerpo $jarA "/combustibles/crear" "p4.html"
$ok = ($pr4 -match "mayor a cero")
Registrar "P03-prod-precio-cero" $ok "precio 0 rechazado"

$a = PostTok $jarA "/combustibles/crear" @("nombre=Prueba Unidad", "idCategoria=1", "unidadMedida=Galon", "precioActual=5.00", "stock=0", "estado=Activo") $tokA
$pr5 = Cuerpo $jarA "/combustibles/crear" "p5.html"
$ok = ($pr5 -match "debe ser Litro")
Registrar "P04-prod-unidad-invalida" $ok "unidad distinta de Litro rechazada"

$a = PostTok $jarA "/combustibles/crear" @("nombre=Nafta ETAPA10", "idCategoria=1", "unidadMedida=Litro", "precioActual=7.25", "stock=100", "estado=Activo") $tokA
$pr6 = Cuerpo $jarA "/combustibles/list" "p6.html"
$ok = ($a -eq "302") -and ($pr6 -match "Nafta ETAPA10") -and ((FilasTabla $pr6 "registrad") -eq 4)
Registrar "P05-prod-crear-valido" $ok "alta correcta: 4 combustibles"

$a = PostTok $jarA "/combustibles/editar" @("id=PR04", "nombre=Nafta ETAPA10", "idCategoria=1", "unidadMedida=Litro", "precioActual=8.00", "stock=100", "estado=Activo") $tokA
$pr7 = Cuerpo $jarA "/combustibles/list" "p7.html"
$ok = ($a -eq "302") -and ($pr7 -match "se edit") -and ($pr7 -match "8.00")
Registrar "P06-prod-editar-valido" $ok "precio actualizado a 8.00 en la lista"

$a = PostTok $jarA "/combustibles/estado" @("id=PR04", "estado=Inactivo") $tokA
$pr8 = Cuerpo $jarA "/combustibles/list" "p8.html"
$venCrea = Cuerpo $jarO "/ventas/crear" "p8b.html"
$ok = ($pr8 -match "Nafta ETAPA10") -and ((FilasTabla $pr8 "registrad") -eq 4) -and ($venCrea -notmatch "Nafta ETAPA10")
Registrar "P07-prod-estado-rn02" $ok "inactivo conservado en catalogo (RN02) y NO ofrecido para nuevas ventas"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "id=PR04&_csrf=$tokA" "$base/combustibles/eliminar"
$pr9 = Cuerpo $jarA "/combustibles/list" "p9.html"
$ok = ($a -eq "404") -and ((FilasTabla $pr9 "registrad") -eq 4)
Registrar "P08-prod-eliminar-404-rn02" $ok "POST /combustibles/eliminar -> 404, catalogo intacto (RN02)"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "nombre=CSRF&precioActual=1" "$base/combustibles/crear"
Registrar "P09-prod-csrf-403" ($a -eq "403") "POST sin token -> $a"

$a = curl.exe -s -b $jarO -o "$dir\p10.html" -w "%{http_code}" --max-redirs 0 --max-time 10 --data-urlencode "nombre=Operador Combustible" --data-urlencode "idCategoria=1" --data-urlencode "unidadMedida=Litro" --data-urlencode "precioActual=5.00" --data-urlencode "stock=0" --data-urlencode "estado=Activo" --data-urlencode "_csrf=$tokO" "$base/combustibles/crear"
$prO = [System.IO.File]::ReadAllText("$dir\p10.html", [System.Text.Encoding]::UTF8)
$ok = ($a -eq "403") -and ($prO -match "no te corresponde")
Registrar "P10-prod-operador-403" $ok "POST del Operador -> 403 con aviso (la lista de combustibles si es visible para todos los autenticados)"

$prFin = Cuerpo $jarA "/combustibles/list" "p11.html"
$ok = ($prFin -notmatch "Operador Combustible")
Registrar "P11-prod-operador-sin-efecto" $ok "el intento del Operador no creo productos"

# ================= EMPLEADOS (E01-E09) =================
$a = PostTok $jarA "/empleados/crear" @("dni=1234567", "nombres=Nuevo", "apellidos=Empleado", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokA
$em2 = Cuerpo $jarA "/empleados/crear" "e2.html"
$ok = ($em2 -match "exactamente 8")
Registrar "E01-emp-dni-corto" $ok "DNI de 7 digitos rechazado"

$a = PostTok $jarA "/empleados/crear" @("dni=00000001", "nombres=Duplicado", "apellidos=Empleado", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokA
$em3 = Cuerpo $jarA "/empleados/crear" "e3.html"
$ok = ($em3 -match "Ya existe un empleado con el DNI")
Registrar "E02-emp-duplicado" $ok "DNI repetido rechazado"

$a = PostTok $jarA "/empleados/crear" @("dni=87654321", "nombres=Nuevo", "apellidos=Empleado", "cargo=Auxiliar", "telefono=1234", "estado=Activo") $tokA
$em4 = Cuerpo $jarA "/empleados/crear" "e4.html"
$ok = ($em4 -match "formato 000 000 000")
Registrar "E03-emp-telefono-invalido" $ok "telefono fuera de formato rechazado"

$a = PostTok $jarA "/empleados/crear" @("dni=87654321", "nombres=Prueba", "apellidos=Etapa Diez", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokA
$em5 = Cuerpo $jarA "/empleados/list" "e5.html"
$ok = ($a -eq "302") -and ($em5 -match "se registr") -and ((FilasTabla $em5 "registrad") -eq 4)
Registrar "E04-emp-crear-valido" $ok "alta correcta: 4 empleados (nuevo id 4, DNI 87654321)"

$a = PostTok $jarA "/empleados/editar" @("id=4", "dni=87654321", "nombres=AB", "apellidos=Etapa Diez", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokA
$em6 = Cuerpo $jarA "/empleados/editar?id=4" "e6.html"
$ok = ($em6 -match "entre 3 y 60 caracteres")
Registrar "E05-emp-nombre-corto" $ok "nombre de 2 letras rechazado en edicion"

$a = PostTok $jarA "/empleados/editar" @("id=4", "dni=87654321", "nombres=Prueba", "apellidos=Etapa Diez Dos", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokA
$em7 = Cuerpo $jarA "/empleados/list" "e7.html"
$ok = ($a -eq "302") -and ($em7 -match "se edit") -and ($em7 -match "Etapa Diez Dos")
Registrar "E06-emp-editar-valido" $ok "edicion reflejada en la lista"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "id=4&_csrf=$tokA" "$base/empleados/eliminar"
$em8 = Cuerpo $jarA "/empleados/list" "e8.html"
$ok = ($a -eq "404") -and ((FilasTabla $em8 "registrad") -eq 4)
Registrar "E07-emp-eliminar-404-rn02" $ok "POST /empleados/eliminar -> 404, 4 empleados conservados (RN02)"

$a = PostTok $jarO "/empleados/crear" @("dni=11112222", "nombres=Operador", "apellidos=Sin Permiso", "cargo=Auxiliar", "telefono=999 888 777", "estado=Activo") $tokO
$emO = Cuerpo $jarO "/empleados/list" "e9.html"
$ok = ($emO -match "no te corresponde")
Registrar "E08-emp-operador-403" $ok "Operador sin permiso sobre empleados"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "dni=99998888&nombres=CSRF&apellidos=Prueba&cargo=Auxiliar&telefono=999 888 777&estado=Activo" "$base/empleados/crear"
Registrar "E09-emp-csrf-403" ($a -eq "403") "POST sin token -> $a"

# ================= USUARIOS (U01-U15) =================
$us0 = Cuerpo $jarA "/usuarios/list" "u1.html"
$ok = (FilasTabla $us0 "registrad") -eq 3
Registrar "U01-usu-lista" $ok "3 cuentas de usuario en memoria"

$usO = Cuerpo $jarO "/usuarios/list" "u2.html"
$ok = ($usO -match "no te corresponde")
Registrar "U02-usu-operador-403" $ok "Operador sin permiso sobre usuarios"

$a = PostTok $jarA "/usuarios/crear" @("idEmpleado=1", "username=atorres", "password=$passDemo", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us3 = Cuerpo $jarA "/usuarios/crear" "u3.html"
$ok = ($us3 -match "El nombre de usuario") -and ($us3 -match "atorres")
Registrar "U03-usu-duplicado" $ok "usuario repetido rechazado y valor conservado"

$a = PostTok $jarA "/usuarios/crear" @("idEmpleado=1", "username=corto1", "password=12345", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us4 = Cuerpo $jarA "/usuarios/crear" "u4.html"
$ok = ($us4 -match "entre 8 y 40 caracteres")
Registrar "U04-usu-password-corta" $ok "password de 5 caracteres rechazada"

$a = PostTok $jarA "/usuarios/crear" @("idEmpleado=1", "username=rolmal1", "password=$passDemo", "rol=SuperAdmin", "estado=Activo") $tokA
$us5 = Cuerpo $jarA "/usuarios/crear" "u5.html"
$ok = ($us5 -match "El rol seleccionado no es")
Registrar "U05-usu-rol-fabricado" $ok "rol fuera del catalogo rechazado (impide elevar rol por formulario)"

$a = PostTok $jarA "/usuarios/crear" @("idEmpleado=1", "username=duenot1", "password=$passDemo", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us6 = Cuerpo $jarA "/usuarios/crear" "u6.html"
$ok = ($us6 -match "Ese empleado ya tiene una cuenta de usuario")
Registrar "U06-usu-empleado-con-cuenta" $ok "un empleado no puede tener dos cuentas"

$a = PostTok $jarA "/usuarios/crear" @("username=sinemple1", "password=$passDemo", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us7 = Cuerpo $jarA "/usuarios/crear" "u7.html"
$ok = ($us7 -match "Debe seleccionar el empleado")
Registrar "U07-usu-sin-empleado" $ok "cuenta sin empleado responsable rechazada"

$a = PostTok $jarA "/usuarios/crear" @("idEmpleado=4", "username=mtest10", "password=$passDemo", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us8 = Cuerpo $jarA "/usuarios/list" "u8.html"
$ok = ($a -eq "302") -and ($us8 -match "mtest10") -and ((FilasTabla $us8 "registrad") -eq 4)
Registrar "U08-usu-crear-valido" $ok "alta correcta: 4 cuentas (mtest10 sobre el empleado 4)"

$a = PostTok $jarA "/usuarios/editar" @("id=4", "idEmpleado=4", "username=mtest10", "password=", "rol=Operador / Vendedor", "estado=Activo") $tokA
$us9 = Cuerpo $jarA "/usuarios/list" "u9.html"
$jarN = "$dir\jar_e10n.txt"
$tokN = Login "mtest10" $passDemo $jarN
$us9b = Cuerpo $jarN "/ventas/list" "u9b.html"
$ok = ($a -eq "302") -and ($us9 -match "mtest10 actualizado correctamente") -and ($us9b -match "Historial de ventas")
Registrar "U09-usu-password-blanco-conserva-hash" $ok "editar sin password no rompe el acceso (hash BCrypt conservado)"

$a = PostTok $jarA "/usuarios/editar" @("id=4", "idEmpleado=4", "username=mtest10", "password=", "rol=Operador / Vendedor", "estado=Inactivo") $tokA
$us10 = Cuerpo $jarA "/usuarios/list" "u10.html"
$ok = ($a -eq "302") -and ($us10 -match "Inactivo")
Registrar "U10-usu-desactivar" $ok "cuenta mtest10 en estado Inactivo (F32, sin eliminar)"

$jarX = "$dir\jar_e10x.txt"
curl.exe -s -c $jarX -o NUL --max-time 10 "$base/login" | Out-Null
$hx = curl.exe -s -b $jarX -c $jarX --max-time 10 "$base/login"
$tx = [regex]::Match($hx, 'name="_csrf" value="([^"]+)"').Groups[1].Value
$a = curl.exe -s -b $jarX -o NUL -w "%{http_code}|%{redirect_url}" --max-redirs 0 --max-time 10 -d "username=mtest10&password=$passDemo&_csrf=$tx" "$base/login"
Registrar "U11-usu-inactivo-no-loguea" ($a -match "error=inactivo") "login de cuenta inactiva -> $a"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "id=4&_csrf=$tokA" "$base/usuarios/eliminar"
$us11 = Cuerpo $jarA "/usuarios/list" "u11.html"
$ok = ($a -eq "404") -and ((FilasTabla $us11 "registrad") -eq 4)
Registrar "U12-usu-eliminar-404-rn02" $ok "POST /usuarios/eliminar -> 404, 4 cuentas conservadas (RN02)"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "idEmpleado=4&username=csrf10&password=$passDemo&rol=Operador / Vendedor&estado=Activo" "$base/usuarios/crear"
Registrar "U13-usu-csrf-403" ($a -eq "403") "POST sin token -> $a"

$a = PostTok $jarO "/usuarios/crear" @("idEmpleado=1", "username=operadorno", "password=$passDemo", "rol=Operador / Vendedor", "estado=Activo") $tokO
$usO2 = Cuerpo $jarO "/usuarios/list" "u14.html"
$ok = ($usO2 -match "no te corresponde")
Registrar "U14-usu-operador-post-403" $ok "POST del Operador rechazado con aviso de permisos"

$usFin = Cuerpo $jarA "/usuarios/list" "u15.html"
$ok = ($usFin -notmatch "operadorno") -and ($usFin -notmatch "csrf10")
Registrar "U15-usu-doble-control-sin-efecto" $ok "ninguno de los POST denegados creo cuentas"

# ================= CONCEPTOS ECONOMICOS (K01-K09) =================
$a = PostTok $jarA "/finanzas/conceptos/crear" @("nombre=Venta de combustible", "tipo=Ingreso", "estado=Activo") $tokA
$kn2 = Cuerpo $jarA "/finanzas/conceptos/crear" "k2.html"
$ok = ($kn2 -match "Ya existe un concepto")
Registrar "K01-con-duplicado" $ok "nombre repetido rechazado"

$a = PostTok $jarA "/finanzas/conceptos/crear" @("nombre=Prueba Tipo", "tipo=Retiro", "estado=Activo") $tokA
$kn3 = Cuerpo $jarA "/finanzas/conceptos/crear" "k3.html"
$ok = ($kn3 -match "El tipo debe ser Ingreso o Egreso")
Registrar "K02-con-tipo-invalido" $ok "tipo fuera de Ingreso/Egreso rechazado"

$a = PostTok $jarA "/finanzas/conceptos/crear" @("nombre=Ajuste ETAPA10", "tipo=Ingreso", "estado=Activo") $tokA
$kn4 = Cuerpo $jarA "/finanzas/conceptos/list" "k4.html"
$ok = ($a -eq "302") -and ($kn4 -match "Ajuste ETAPA10") -and ((FilasTabla $kn4 "registrad") -eq 3)
Registrar "K03-con-crear-valido" $ok "alta correcta: 3 conceptos"

$a = PostTok $jarA "/finanzas/conceptos/editar" @("id=CE03", "nombre=Ajuste ETAPA10 Editado", "tipo=Ingreso", "estado=Activo") $tokA
$kn5 = Cuerpo $jarA "/finanzas/conceptos/list" "k5.html"
$finK = Cuerpo $jarA "/finanzas/list" "k5b.html"
$ok = ($a -eq "302") -and ($kn5 -match "se edit") -and ($kn5 -match "Ajuste ETAPA10 Editado") -and ((FilasTabla $finK "recientes") -eq 4)
Registrar "K04-con-editar-sin-tocar-caja" $ok "renombrar el concepto no corrompe los movimientos de caja (siguen 4)"

$a = PostTok $jarA "/finanzas/conceptos/estado" @("id=CE03", "estado=Inactivo") $tokA
$kn6 = Cuerpo $jarA "/finanzas/conceptos/list" "k6.html"
$ok = ($kn6 -match "Ajuste ETAPA10 Editado") -and ((FilasTabla $kn6 "registrad") -eq 3)
Registrar "K05-con-estado-rn02" $ok "cambio a Inactivo con registro conservado (RN02)"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "id=CE03&_csrf=$tokA" "$base/finanzas/conceptos/eliminar"
$kn7 = Cuerpo $jarA "/finanzas/conceptos/list" "k7.html"
$ok = ($a -eq "404") -and ((FilasTabla $kn7 "registrad") -eq 3)
Registrar "K06-con-eliminar-404-rn02" $ok "POST eliminar -> 404, 3 conceptos (RN02)"

$knO = Cuerpo $jarO "/finanzas/conceptos/list" "k8.html"
$ok = ($knO -match "no te corresponde")
Registrar "K07-con-operador-403" $ok "Operador sin permiso sobre finanzas"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "nombre=CSRFCon&tipo=Ingreso&estado=Activo" "$base/finanzas/conceptos/crear"
Registrar "K08-con-csrf-403" ($a -eq "403") "POST sin token -> $a"

$knFin = Cuerpo $jarA "/finanzas/conceptos/list" "k9.html"
$ok = ($knFin -notmatch "CSRFCon")
Registrar "K09-con-sin-efectos-colaterales" $ok "el catalogo termina con 3 conceptos (CE01, CE02, Ajuste ETAPA10 Editado)"

# ================= ASISTENCIA (A09-A18) =================
$mi1 = Cuerpo $jarO "/asistencia/mi" "a9.html"
$ok = ($mi1 -match "Registrar entrada")
Registrar "AS01-mi-pantalla" $ok "pantalla Mi asistencia operativa para atorres"

$a = PostTok $jarO "/asistencia/entrada" @("idEmpleado=2") $tokO
$mi2 = Cuerpo $jarO "/asistencia/mi" "a10.html"
$ok = ($a -eq "302") -and ($mi2 -match "Entrada registrada a las")
Registrar "AS02-entrada-ignora-id spoofeado" $ok "idEmpleado=2 del navegador se ignora; la entrada usa la identidad real (atorres, empleado 1)"

$a = PostTok $jarO "/asistencia/entrada" @() $tokO
$mi3 = Cuerpo $jarO "/asistencia/mi" "a11.html"
$ok = ($mi3 -match "Ya registraste la entrada de hoy")
Registrar "AS03-entrada-repetida" $ok "segunda entrada del dia rechazada (peticion repetida sin doble registro)"

$a = PostTok $jarL "/asistencia/salida" @() $tokL
$mi4 = Cuerpo $jarL "/asistencia/mi" "a12.html"
$ok = ($mi4 -match "No hay una asistencia abierta")
Registrar "AS04-salida-sin-entrada" $ok "salida sin entrada previa rechazada (RN05)"

$a = PostTok $jarO "/asistencia/salida" @() $tokO
$mi5 = Cuerpo $jarO "/asistencia/mi" "a13.html"
$ok = ($a -eq "302") -and ($mi5 -match "Salida registrada a las")
Registrar "AS05-salida-valida" $ok "salida de atorres registrada tras su entrada"

$a = PostTok $jarO "/asistencia/salida" @() $tokO
$mi6 = Cuerpo $jarO "/asistencia/mi" "a14.html"
$ok = ($mi6 -match "No hay una asistencia abierta")
Registrar "AS06-salida-repetida" $ok "jornada ya cerrada: no se registra una segunda salida"

$ctl1 = Cuerpo $jarA "/asistencia/control" "a15.html"
$ok = (FilasTabla $ctl1 "registrad") -eq 6
Registrar "AS07-control-hoy" $ok "control con 6 marcaciones (5 de semilla + 1 de atorres de hoy)"

$ctlO = Cuerpo $jarO "/asistencia/control" "a16.html"
$ok = ($ctlO -match "no te corresponde")
Registrar "AS08-control-operador-403" $ok "el Operador no abre el control (solo Admin)"

$a = curl.exe -s -b $jarO -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -X POST -d "dummy=1" "$base/asistencia/entrada"
Registrar "AS09-csrf-403" ($a -eq "403") "POST sin token valido -> $a"

$ctl2 = Cuerpo $jarA "/asistencia/control" "a18.html"
$ok = (FilasTabla $ctl2 "registrad") -eq 6
Registrar "AS10-integridad-final" $ok "los intentos repetidos dejaron exactamente 1 marcacion de hoy"

# ================= COMPRAS (D01-D11) =================
$co0 = Cuerpo $jarA "/compras/list" "d1.html"
$ok = (FilasTabla $co0 "Compras registradas") -eq 1
Registrar "D01-com-lista" $ok "1 compra en memoria (C001)"

# El formulario ya no expone campo de fecha: la registra el servidor
$a = PostTok $jarA "/compras/crear" @("proveedor=Proveedor X", "idProducto=PR01", "cantidad=0", "precioCompra=4.50", "idUsuario=3") $tokA
$co2 = Cuerpo $jarA "/compras/crear" "d2.html"
$ok = ($co2 -match "debe ser mayor que cero") -and ($co2 -notmatch "La fecha de la compra")
Registrar "D02-com-sin-fecha-del-formulario" $ok "sin campo fecha: la pone el servidor y se valida la cantidad"

$a = PostTok $jarA "/compras/crear" @("fecha=2026-10-09", "proveedor=AB", "idProducto=PR01", "cantidad=10", "precioCompra=4.50", "idUsuario=3") $tokA
$co3 = Cuerpo $jarA "/compras/crear" "d3.html"
$ok = ($co3 -match "entre 3 y 60 caracteres")
Registrar "D03-com-proveedor-corto" $ok "proveedor de 2 letras rechazado"

$a = PostTok $jarA "/compras/crear" @("fecha=2026-10-09", "proveedor=Proveedor X", "idProducto=PR04", "cantidad=10", "precioCompra=4.50", "idUsuario=3") $tokA
$co4 = Cuerpo $jarA "/compras/crear" "d4.html"
$ok = ($co4 -match "pueden comprarse")
Registrar "D04-com-producto-inactivo" $ok "combustible inactivo no puede comprarse (RN02 extendido)"

$a = PostTok $jarA "/compras/crear" @("fecha=2026-10-09", "proveedor=Proveedor X", "idProducto=PR01", "cantidad=10", "precioCompra=4.50", "idUsuario=4") $tokA
$co5 = Cuerpo $jarA "/compras/crear" "d5.html"
$ok = ($co5 -match "elija una cuenta activa")
Registrar "D05-com-responsable-inactivo" $ok "responsable inactivo rechazado"

$a = PostTok $jarA "/compras/crear" @("fecha=2026-10-09", "proveedor=Proveedor X", "idProducto=PR01", "cantidad=0", "precioCompra=4.50", "idUsuario=3") $tokA
$co6 = Cuerpo $jarA "/compras/crear" "d6.html"
$ok = ($co6 -match "debe ser mayor que cero")
Registrar "D06-com-cantidad-cero" $ok "cantidad 0 rechazada"

$a = PostTok $jarA "/compras/crear" @("fecha=2026-10-09", "proveedor=Proveedor Sin Detalle", "idUsuario=3") $tokA
$co7 = Cuerpo $jarA "/compras/crear" "d7.html"
$ok = ($co7 -match "El producto seleccionado no existe")
Registrar "D07-com-sin-detalle" $ok "compra sin lineas rechazada (RN03: sin detalle no hay entrada)"

# La fecha del formulario (1999) es manipulada: el servidor debe registrar la de hoy
$a = PostTok $jarA "/compras/crear" @("fecha=1999-05-05", "proveedor=Proveedor ETAPA10", "idProducto=PR01", "cantidad=100", "precioCompra=4.80", "idUsuario=3") $tokA
$co8 = Cuerpo $jarA "/compras/list" "d8.html"
$inv8 = Cuerpo $jarA "/inventario/list" "d8b.html"
$fin8 = Cuerpo $jarA "/finanzas/list" "d8c.html"
$ok = ($a -eq "302") -and ($co8 -match "Compra C002") -and ((FilasTabla $co8 "Compras registradas") -eq 2) -and ($co8 -match $hoy) -and ($co8 -notmatch "05/05/1999") -and ($inv8 -match "Compra C002") -and ($inv8 -match "entradas 400 L") -and ((FilasTabla $inv8 "Libro de movimientos") -eq 7) -and ($fin8 -match "C002") -and ($fin8 -match "1,830.00") -and ((FilasTabla $fin8 "recientes") -eq 5)
Registrar "D08-com-crear-valido-rn03-rn04" $ok "C002 con fecha del servidor: 2 compras, libro 7 filas (entradas 400 L), caja 5 filas (egresos 1,830.00 = 1350+480)"

$co9 = Cuerpo $jarA "/compras/detalle?id=Z999" "d9.html"
$co9b = Cuerpo $jarA "/compras/list" "d9b.html"
$ok = ($co9b -match "No se encontr")
Registrar "D09-com-detalle-inexistente" $ok "id inexistente redirige con aviso (sin 500)"

$a = PostTok $jarO "/compras/crear" @("fecha=2026-10-09", "proveedor=Operador Compra", "idProducto=PR01", "cantidad=10", "precioCompra=4.50", "idUsuario=1") $tokO
$coO = Cuerpo $jarO "/compras/list" "d10.html"
$ok = ($coO -match "no te corresponde")
Registrar "D10-com-operador-403" $ok "Operador sin permiso sobre compras"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "fecha=2026-10-09&proveedor=CSRF&idProducto=PR01&cantidad=10&precioCompra=4.50&idUsuario=3" "$base/compras/crear"
$coF = Cuerpo $jarA "/compras/list" "d11.html"
$ok = ($a -eq "403") -and ((FilasTabla $coF "Compras registradas") -eq 2)
Registrar "D11-com-csrf-403" $ok "POST sin token -> 403 y siguen exactamente 2 compras"

# El formulario de compra ya no expone campo de fecha editable
$coFrm = Cuerpo $jarA "/compras/crear" "d12.html"
$ok = ($coFrm -match "Fecha de registro") -and ($coFrm -match $hoy) -and ($coFrm -notmatch 'name="fecha"')
Registrar "D12-com-fecha-informativa" $ok "fecha de registro informativa del servidor y sin campo editable"

# ================= VENTAS (V01-V12) =================
$ve0 = Cuerpo $jarA "/ventas/list" "v1.html"
$ok = (FilasTabla $ve0 "Ventas registradas") -eq 3
Registrar "V01-ven-lista" $ok "3 ventas en memoria (V001-V003)"

$a = PostTok $jarO "/ventas/crear" @("idProducto=PR01", "idUsuario=1") $tokO
$ve2 = Cuerpo $jarO "/ventas/crear" "v2.html"
$ve2b = Cuerpo $jarA "/ventas/list" "v2b.html"
$inv2 = Cuerpo $jarA "/inventario/list" "v2c.html"
$fin2 = Cuerpo $jarA "/finanzas/list" "v2d.html"
$ok = ($ve2 -match "La cantidad de litros es obligatoria") -and ((FilasTabla $ve2b "Ventas registradas") -eq 3) -and ((FilasTabla $inv2 "Libro de movimientos") -eq 7) -and ((FilasTabla $fin2 "recientes") -eq 5)
Registrar "V02-ven-sin-cantidad-sin-efectos" $ok "rechazo total: ventas 3, libro 7, caja 5 (sin efectos parciales, RN03)"

$stockAntes = StockDe $inv2 "Gasolina Regular"
$a = PostTok $jarO "/ventas/crear" @("idProducto=PR01", "cantidad=99999", "idUsuario=1") $tokO
$ve3 = Cuerpo $jarO "/ventas/crear" "v3.html"
$inv3 = Cuerpo $jarA "/inventario/list" "v3b.html"
$stockDespues = StockDe $inv3 "Gasolina Regular"
$ok = ($ve3 -match "No hay stock suficiente") -and ($stockAntes -eq $stockDespues) -and ($stockDespues -eq 2090)
Registrar "V03-ven-sobre-stock-rn01" $ok "99999 L rechazados y stock intacto en $stockDespues L (RN01)"

$a = PostTok $jarO "/ventas/crear" @("idProducto=PR04", "cantidad=5", "idUsuario=1") $tokO
$ve4 = Cuerpo $jarO "/ventas/crear" "v4.html"
$ok = ($ve4 -match "pueden venderse")
Registrar "V04-ven-producto-inactivo" $ok "combustible inactivo no puede venderse (RN02 extendido)"

$a = PostTok $jarO "/ventas/crear" @("idProducto=PR01", "cantidad=5", "idUsuario=4") $tokO
$ve5 = Cuerpo $jarO "/ventas/crear" "v5.html"
$ok = ($ve5 -match "elija una cuenta activa")
Registrar "V05-ven-operador-inactivo" $ok "operador inactivo rechazado"

$a = PostTok $jarO "/ventas/crear" @("idProducto=PR01", "cantidad=10", "idUsuario=1") $tokO
$ve6 = Cuerpo $jarA "/ventas/list" "v6.html"
$inv6 = Cuerpo $jarA "/inventario/list" "v6b.html"
$fin6 = Cuerpo $jarA "/finanzas/list" "v6c.html"
$ok = ($a -eq "302") -and ($ve6 -match "V004") -and ($ve6 -match $hoy) -and ((FilasTabla $ve6 "Ventas registradas") -eq 4) -and ($inv6 -match "Venta V004") -and ((FilasTabla $inv6 "Libro de movimientos") -eq 8) -and ($inv6 -match "salidas 90 L") -and ($fin6 -match "V004") -and ($fin6 -match "420.00") -and ((FilasTabla $fin6 "recientes") -eq 6) -and ((StockDe $inv6 "Gasolina Regular") -eq 2080)
Registrar "V06-ven-crear-valido-rn01-rn03-rn04" $ok "V004: 4 ventas, libro 8 filas (1 salida por venta), caja 6 filas (ingresos 420.00), stock 2090->2080"

$ve8 = Cuerpo $jarA "/ventas/detalle?id=V999" "v8.html"
$ve8b = Cuerpo $jarA "/ventas/list" "v8b.html"
$ok = ($ve8b -match "No se encontr")
Registrar "V07-ven-detalle-inexistente" $ok "id inexistente redirige con aviso"

$a = PostTok $jarA "/ventas/crear" @("idProducto=PR01", "cantidad=10", "idUsuario=3") $tokA
$ve9 = Cuerpo $jarA "/ventas/crear" "v9.html"
$ok = ($ve9 -match "no te corresponde")
Registrar "V08-ven-admin-403" $ok "el Administrador no registra ventas (seccion de operacion)"

$venO = Cuerpo $jarO "/ventas/list" "v10.html"
$ok = ($venO -match "Historial de ventas") -and ((FilasTabla $venO "Ventas registradas") -eq 4)
Registrar "V09-ven-operador-lista" $ok "Operador si consulta el historial (autenticado)"

$a = curl.exe -s -b $jarO -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "idProducto=PR01&cantidad=10&idUsuario=1" "$base/ventas/crear"
$veF = Cuerpo $jarA "/ventas/list" "v11.html"
$ok = ($a -eq "403") -and ((FilasTabla $veF "Ventas registradas") -eq 4)
Registrar "V10-ven-csrf-403" $ok "POST sin token -> 403 y siguen 4 ventas"

$invV = Cuerpo $jarA "/inventario/list" "v12.html"
$ok = ((FilasTabla $invV "Libro de movimientos") -eq 8)
Registrar "V11-ven-salida-unica-rn03" $ok "la venta V004 aporto exactamente una salida al libro"

# El formulario de venta no expone campo de fecha: la registra el servidor
$veFrm = Cuerpo $jarO "/ventas/crear" "v13.html"
$ok = ($veFrm -notmatch 'name="fecha"')
Registrar "V12-ven-sin-campo-fecha" $ok "el formulario de venta no pide fecha (la toma el servidor)"

# ================= INVENTARIO (I01-I11) =================
$in0 = Cuerpo $jarA "/inventario/list" "i1.html"
$ok = ($in0 -match "Saldo inicial")
Registrar "I01-inv-pantalla" $ok "existencias con saldo inicial visibles"

# La fecha del formulario (1999) es manipulada: el servidor debe registrar la de hoy
$a = PostTok $jarA "/inventario/entrada/crear" @("idProducto=PR02", "cantidad=50", "fechaHora=1999-01-01T00:00", "motivo=Entrada ETAPA10 validada", "idUsuario=3") $tokA
$in2 = Cuerpo $jarA "/inventario/list" "i2.html"
$ok = ($a -eq "302") -and ($in2 -match "Entrada ETAPA10 validada") -and ((FilasTabla $in2 "Libro de movimientos") -eq 9) -and ($in2 -match "entradas 450 L") -and ($in2 -match $hoy) -and ($in2 -notmatch "01/01/1999") -and ((StockDe $in2 "Gasolina Premium") -eq 1030)
Registrar "I02-inv-entrada-valida" $ok "entrada manual: libro 9 filas, PR02 980->1030, fecha manipulada ignorada (servidor)"

$a = PostTok $jarA "/inventario/entrada/crear" @("idProducto=PR04", "cantidad=10", "fechaHora=2026-10-09T10:00", "motivo=Entrada sobre inactivo", "idUsuario=3") $tokA
$in3 = Cuerpo $jarA "/inventario/entrada/crear" "i3.html"
$ok = ($in3 -match "productos inactivos")
Registrar "I03-inv-entrada-producto-inactivo" $ok "entrada sobre producto inactivo rechazada (RN02)"

$a = PostTok $jarA "/inventario/entrada/crear" @("idProducto=PR02", "fechaHora=2026-10-09T10:00", "motivo=Entrada sin cantidad", "idUsuario=3") $tokA
$in4 = Cuerpo $jarA "/inventario/entrada/crear" "i4.html"
$ok = ($in4 -match "La cantidad de litros es obligatoria")
Registrar "I04-inv-entrada-sin-cantidad" $ok "cantidad vacia rechazada"

# La fecha del formulario (2099) es manipulada: el servidor debe registrar la de hoy
$a = PostTok $jarO "/inventario/salida/crear" @("idProducto=PR02", "cantidad=20", "fechaHora=2099-12-31T23:59", "motivo=Salida ETAPA10 validada", "idUsuario=1") $tokO
$in5 = Cuerpo $jarA "/inventario/list" "i5.html"
$ok = ($a -eq "302") -and ($in5 -match "Salida ETAPA10 validada") -and ((FilasTabla $in5 "Libro de movimientos") -eq 10) -and ($in5 -match "salidas 110 L") -and ($in5 -match $hoy) -and ($in5 -notmatch "31/12/2099") -and ((StockDe $in5 "Gasolina Premium") -eq 1010)
Registrar "I05-inv-salida-valida" $ok "salida manual: libro 10 filas, PR02 1030->1010, fecha manipulada ignorada (servidor)"

$a = PostTok $jarO "/inventario/salida/crear" @("idProducto=PR02", "cantidad=99999", "fechaHora=2026-10-09T11:00", "motivo=Salida mayor al stock", "idUsuario=1") $tokO
$in6f = Cuerpo $jarO "/inventario/salida/crear" "i6f.html"
$in6 = Cuerpo $jarA "/inventario/list" "i6.html"
$ok = ($in6f -match "No hay stock suficiente") -and ((StockDe $in6 "Gasolina Premium") -eq 1010) -and ((FilasTabla $in6 "Libro de movimientos") -eq 10)
Registrar "I06-inv-salida-sobre-stock-rn01" $ok "aviso en el formulario y rechazo sin efectos: stock 1010 y libro 10 (RN01)"

$a = PostTok $jarO "/inventario/salida/crear" @("idProducto=PR02", "cantidad=10", "fechaHora=2026-10-09T11:00", "motivo=abc", "idUsuario=1") $tokO
$in7 = Cuerpo $jarO "/inventario/salida/crear" "i7.html"
$ok = ($in7 -match "entre 5 y 200 caracteres")
Registrar "I07-inv-salida-motivo-corto" $ok "motivo de 3 letras rechazado"

# El formulario ya no expone campo de fecha: la registra el servidor
$a = PostTok $jarO "/inventario/salida/crear" @("idProducto=PR02", "cantidad=0", "motivo=Salida sin fecha en el formulario", "idUsuario=1") $tokO
$in8 = Cuerpo $jarO "/inventario/salida/crear" "i8.html"
$ok = ($in8 -match "debe ser mayor que cero") -and ($in8 -notmatch "son obligatorias")
Registrar "I08-inv-sin-fecha-en-formulario" $ok "sin campo fechaHora: la pone el servidor y se valida la cantidad"

$a = PostTok $jarO "/inventario/entrada/crear" @("idProducto=PR02", "cantidad=10", "fechaHora=2026-10-09T10:00", "motivo=Entrada del operador", "idUsuario=1") $tokO
$in9 = Cuerpo $jarO "/inventario/entrada/crear" "i9.html"
$ok = ($in9 -match "no te corresponde")
Registrar "I09-inv-entrada-operador-403" $ok "el Operador no registra entradas (seccion Admin)"

$a = PostTok $jarA "/inventario/salida/crear" @("idProducto=PR02", "cantidad=10", "fechaHora=2026-10-09T11:00", "motivo=Salida del admin", "idUsuario=3") $tokA
$inA = Cuerpo $jarA "/inventario/salida/crear" "i10.html"
$ok = ($inA -match "no te corresponde")
Registrar "I10-inv-salida-admin-403" $ok "el Administrador no registra salidas manuales (seccion de operacion)"

$a = curl.exe -s -b $jarO -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "idProducto=PR02&cantidad=10&fechaHora=2026-10-09T11:00&motivo=CSRF&idUsuario=1" "$base/inventario/salida/crear"
$inF = Cuerpo $jarA "/inventario/list" "i11.html"
$ok = ($a -eq "403") -and ((FilasTabla $inF "Libro de movimientos") -eq 10)
Registrar "I11-inv-csrf-403" $ok "POST sin token -> 403 y el libro sigue en 10 filas"

# El formulario de inventario ya no expone campo de fecha editable
$inFrm = Cuerpo $jarA "/inventario/entrada/crear" "i12.html"
$ok = ($inFrm -match "Fecha y hora de registro") -and ($inFrm -match $hoy) -and ($inFrm -notmatch 'name="fechaHora"')
Registrar "I12-inv-fecha-informativa" $ok "marca de tiempo informativa del servidor y sin campo editable"

# ================= FINANZAS (F01-F06) =================
$fi1 = Cuerpo $jarA "/finanzas/list" "f1.html"
$ok = ($fi1 -match "3,000.00") -and ($fi1 -match "4,410.00") -and ((FilasTabla $fi1 "recientes") -eq 6)
Registrar "F01-fin-conciliacion" $ok "saldo 3,000.00 = apertura 4,410.00 + ingresos 420.00 - egresos 1,830.00; 6 movimientos"

$fi2 = Cuerpo $jarA "/finanzas/ingresos" "f2.html"
$ok = ($fi2 -match "V004")
Registrar "F02-fin-ingresos" $ok "filtro de ingresos operativo (V001-V004)"

$fi3 = Cuerpo $jarA "/finanzas/egresos" "f3.html"
$ok = ($fi3 -match "C002")
Registrar "F03-fin-egresos" $ok "filtro de egresos operativo (C001, C002)"

$fi4 = Cuerpo $jarA "/finanzas/detalle?id=MC99" "f4.html"
$fi4b = Cuerpo $jarA "/finanzas/list" "f4b.html"
$ok = ($fi4b -match "no existe en caja")
Registrar "F04-fin-detalle-inexistente" $ok "movimiento inexistente redirige con aviso"

$fiO = Cuerpo $jarO "/finanzas/list" "f5.html"
$ok = ($fiO -match "no te corresponde")
Registrar "F05-fin-operador-403" $ok "Operador sin acceso a finanzas"

$a = curl.exe -s -b $jarA -o NUL -w "%{http_code}" --max-redirs 0 --max-time 10 -d "tipo=Ingreso&monto=999&concepto=CE01&_csrf=$tokA" "$base/finanzas/movimientos/crear"
$fi6 = Cuerpo $jarA "/finanzas/list" "f6.html"
$ok = ($a -eq "404") -and ((FilasTabla $fi6 "recientes") -eq 6)
Registrar "F06-fin-sin-ruta-manual" $ok "no existe ruta para crear/duplicar caja a mano: 404 y 6 movimientos (RN04)"

# ================= CIERRE RN01-RN05 (R01-R05) =================
$finF = Cuerpo $jarA "/inventario/list" "r1.html"
$cajF = Cuerpo $jarA "/finanzas/list" "r2.html"
$venF = Cuerpo $jarA "/ventas/list" "r3.html"
$comF = Cuerpo $jarA "/compras/list" "r4.html"

$ok = ((StockDe $finF "Gasolina Regular") -eq 2080) -and ((StockDe $finF "Gasolina Premium") -eq 1010) -and ($finF -match "3,950")
Registrar "R01-RN01-stock-conciliado" $ok "stock final PR01=2080 (1990+100-10), PR02=1010 (980+50-20), Diesel 3,950 sin tocar; nunca negativo"

$catF2 = Cuerpo $jarA "/categorias/list" "r5.html"
$ok = ((FilasTabla $catF2 "registrad") -eq 3) -and ((FilasTabla $usFin "registrad") -eq 4) -and ((FilasTabla $knFin "registrad") -eq 3) -and ((FilasTabla $prFin "registrad") -eq 4) -and ((FilasTabla $em7 "registrad") -eq 4)
Registrar "R02-RN02-cambio-de-estado" $ok "los cinco catalogos (categorias 3, usuarios 4, conceptos 3, combustibles 4, empleados 4) conservan todos los registros; solo cambian estados y eliminar devuelve 404"

$ok = ((FilasTabla $finF "Libro de movimientos") -eq 10) -and ($finF -match "entradas 450 L") -and ($finF -match "salidas 110 L")
Registrar "R03-RN03-integridad-movimientos" $ok "libro 10 filas = 6 semilla + 4 operaciones; cada venta/compra aporto exactamente su movimiento"

$ok = ((FilasTabla $cajF "recientes") -eq 6) -and ($cajF -match "3,000.00") -and ((FilasTabla $comF "Compras registradas") -eq 2) -and ((FilasTabla $venF "Ventas registradas") -eq 4)
Registrar "R04-RN04-caja-conciliada" $ok "6 movimientos de caja = 2 compras + 4 ventas; saldo exacto; sin duplicados ni rutas manuales"

$ctlF = Cuerpo $jarA "/asistencia/control" "r6.html"
$ok = (FilasTabla $ctlF "registrad") -eq 6
Registrar "R05-RN05-asistencia-integra" $ok "una unica jornada de hoy (entrada+salida de atorres); las repeticiones fueron rechazadas"

# ================= TEXTOS DE INTERFAZ (M01-M04) =================
$mp = ""
foreach ($ruta in @("/login","/categorias/crear","/categorias/list","/combustibles/list","/empleados/list","/usuarios/list","/ventas/crear","/ventas/list","/compras/crear","/compras/list","/inventario/entrada/crear","/inventario/salida/crear","/inventario/list","/finanzas/list","/finanzas/conceptos/list","/finanzas/concepto-crear","/asistencia/mi","/asistencia/control")) {
    $mp += Cuerpo $jarA $ruta "m01.txt"
}
Registrar "M01-textos-sin-memoria" ($mp -notmatch "en memoria") "ninguna pagina visible menciona almacen en memoria"
Registrar "M02-textos-sin-ejecucion" ($mp -notmatch "en ejecuci") "ninguna pagina visible menciona la ejecucion de la aplicacion"
Registrar "M03-textos-sin-base-datos" ($mp -notmatch "base de datos") "ninguna pagina visible menciona base de datos"
Registrar "M04-textos-sin-temporal" ($mp -notmatch "almacenamiento temporal") "ninguna pagina visible menciona almacenamiento temporal"

# ================= RESUMEN =================
Write-Host ""
Write-Host "================= REVISION FINAL ================="
$global:res | Group-Object Resultado | ForEach-Object { "{0,-6} {1,3}" -f $_.Name, $_.Count }
Write-Host ("TOTAL: {0} pruebas, PASS {1}, FAIL {2}" -f $global:res.Count, @($global:res | Where-Object { $_.Resultado -eq "PASS" }).Count, @($global:res | Where-Object { $_.Resultado -eq "FAIL" }).Count)
$global:res | Where-Object { $_.Resultado -eq "FAIL" } | ForEach-Object { "FALLO: $($_.Prueba) - $($_.Detalle)" }
$global:res | Export-Csv -Path "$dir\revision_resultados.csv" -NoTypeInformation -Encoding UTF8
Write-Host "Resultados exportados a $dir\revision_resultados.csv"
