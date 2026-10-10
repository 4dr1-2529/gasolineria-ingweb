<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Sin permisos · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <main id="contenido" class="login-pagina">
        <a class="brand" href="${pageContext.request.contextPath}/">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <p class="eyebrow">ACCESO · SIN PERMISOS</p>
        <h1>Esta sección no te corresponde</h1>
        <p class="subtitle">
            Tu rol no tiene permiso para abrir esta pantalla.
            Pide acceso al Administrador o vuelve a tu sección.
        </p>
        <div class="actions">
            <a class="btn btn-primary" href="${pageContext.request.contextPath}/">Volver al inicio</a>
        </div>
    </main>
</body>
</html>
