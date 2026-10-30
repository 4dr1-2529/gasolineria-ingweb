"""Escaneo reproducible de archivos Git publicables. No imprime valores secretos.

Comprueba formatos de claves/tokens y los secretos conocidos del entorno.
Los nombres de variables, plantillas JSP y marcadores redactados no son secretos.
"""
import json
import os
from pathlib import Path
import re
import subprocess

raiz=Path(__file__).resolve().parents[2]
os.chdir(raiz)
archivos=subprocess.check_output(["git","ls-files","--cached","--others","--exclude-standard","-z"]).decode("utf-8").split("\0")
patrones={
    "token GitHub":r"\b(?:gh[pousr]_[A-Za-z0-9]{20,}|github_pat_[A-Za-z0-9_]{20,})",
    "clave API":r"\bsk-(?:proj-)?[A-Za-z0-9_-]{24,}",
    "clave AWS":r"\bAKIA[A-Z0-9]{16}\b",
    "clave privada":r"-----BEGIN (?:RSA |EC |OPENSSH )?PRIVATE KEY-----",
    "credenciales en URL":r"https?://[^\s/:]+:[^\s@/]+@",
    "sesión literal":r"JSESSIONID=[A-Za-z0-9._-]{20,}",
}
secretos=[os.environ.get(n) for n in ("NEXO_DB_PASSWORD","NEXO_TEST_DB_PASSWORD","NEXO_MYSQL_ROOT_PASSWORD","NEXO_DEMO_PASSWORD")]
secretos=[s for s in secretos if s and len(s)>=8]
hallazgos=[];revisados=0
for nombre in sorted(set(archivos)):
    p=Path(nombre)
    if not nombre or not p.is_file():continue
    try:contenido=p.read_text(encoding="utf-8")
    except (UnicodeError,OSError):continue
    revisados+=1
    motivos=[etiqueta for etiqueta,regex in patrones.items() if re.search(regex,contenido)]
    if any(s in contenido for s in secretos):motivos.append("valor secreto del entorno")
    if motivos:hallazgos.append({"archivo":nombre,"tipos":motivos})
resultado={"alcance":"Archivos seguidos por Git y nuevos no ignorados; valores conocidos del entorno y patrones de alta confianza","archivos_texto_revisados":revisados,"hallazgos":hallazgos,"target_seguido":bool(subprocess.check_output(["git","ls-files","target"]).strip())}
salida=raiz/"documentacion/evidencias/resultados_secretos_publicacion.json"
salida.write_text(json.dumps(resultado,ensure_ascii=False,indent=2),encoding="utf-8")
print(json.dumps({"archivos_texto_revisados":revisados,"hallazgos":len(hallazgos),"target_seguido":resultado["target_seguido"]}))
if hallazgos or resultado["target_seguido"]:raise SystemExit(1)
