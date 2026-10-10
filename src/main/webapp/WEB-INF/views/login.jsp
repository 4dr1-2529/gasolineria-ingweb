<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Iniciar sesión · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <main id="contenido" class="login-pagina">
        <a class="brand" href="${pageContext.request.contextPath}/login">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <p class="eyebrow">F01 / P02 · ACCESO</p>
        <h1>Iniciar sesión</h1>
        <p class="subtitle">Ingresa con tu usuario y contraseña para operar el sistema.</p>

        <c:if test="${not empty error}">
            <p class="notice notice-error">
                <c:out value="${error}"/>
            </p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="notice notice-ok">
                <c:out value="${mensaje}"/>
            </p>
        </c:if>

        <section class="panel">
            <!-- El token CSRF se agrega solo con form:form -->
            <form:form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-grid">
                    <div class="field">
                        <label for="username">Usuario</label>
                        <input class="form-control" type="text" id="username" name="username"
                               required="required" minlength="4" maxlength="20" autocomplete="username" />
                    </div>
                    <div class="field">
                        <label for="password">Contraseña</label>
                        <input class="form-control" type="password" id="password" name="password"
                               required="required" minlength="8" maxlength="40" autocomplete="current-password" />
                    </div>
                </div>
                <div class="actions">
                    <button type="submit" class="btn btn-primary">Entrar</button>
                </div>
            </form:form>
            <p class="form-help">
                Cuentas de demostración (datos de prueba, no personas reales):
                <strong>atorres</strong> y <strong>lrojas</strong> (Operador / Vendedor) ·
                <strong>ediaz</strong> (Administrador) · contraseña <strong>NexoDemo2026</strong>.
                El usuario inactivo no puede iniciar sesión.
            </p>
        </section>
    </main>
</body>
</html>
