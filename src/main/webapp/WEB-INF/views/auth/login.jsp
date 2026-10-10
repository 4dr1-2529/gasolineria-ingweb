<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="description" content="Acceso al sistema de gestión de Estación Nexo">
    <title>Iniciar sesión · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body class="login-cuerpo">
    <main id="contenido" class="login-pagina">
        <header class="login-cabecera">
            <a class="brand login-brand" href="${pageContext.request.contextPath}/login">
                <span class="brand-mark" aria-hidden="true">N</span>
                <span>NEXO<small>ESTACIÓN NEXO</small></span>
            </a>
            <p class="eyebrow">F01 / P02 · ACCESO AL SISTEMA</p>
            <h1>Iniciar sesión</h1>
            <p class="subtitle">Ingresa con tu usuario y contraseña para operar el sistema.</p>
        </header>

        <!-- role="alert": el leedor de pantalla anuncia el error sin mover el foco -->
        <c:if test="${not empty error}">
            <p class="notice error login-aviso" role="alert">
                <c:out value="${error}"/>
            </p>
        </c:if>
        <c:if test="${not empty mensaje}">
            <p class="notice ok login-aviso" role="status">
                <c:out value="${mensaje}"/>
            </p>
        </c:if>

        <section class="panel login-tarjeta" aria-label="Formulario de inicio de sesión">
            <!-- El token CSRF se agrega solo con form:form -->
            <form:form action="${pageContext.request.contextPath}/login" method="post">
                <div class="form-grid login-campos">
                    <div class="field">
                        <label for="username">Usuario</label>
                        <input class="form-control" type="text" id="username" name="username"
                               required="required" minlength="4" maxlength="20"
                               autocomplete="username" aria-describedby="ayuda-usuario" />
                        <p class="form-help" id="ayuda-usuario">Nombre asignado por el Administrador.</p>
                    </div>
                    <div class="field">
                        <label for="password">Contraseña</label>
                        <input class="form-control" type="password" id="password" name="password"
                               required="required" minlength="8" maxlength="40"
                               autocomplete="current-password" aria-describedby="ayuda-password" />
                        <p class="form-help" id="ayuda-password">Entre 8 y 40 caracteres.</p>
                    </div>
                </div>
                <div class="actions login-acciones">
                    <button type="submit" class="btn btn-primary">Ingresar</button>
                </div>
            </form:form>

            <aside class="login-demo" aria-label="Cuentas de prueba">
                <p class="login-demo-titulo">Cuentas de acceso (datos de prueba, no personas reales)</p>
                <ul class="login-demo-lista">
                    <li><strong>ediaz</strong> · Administrador</li>
                    <li><strong>atorres</strong> · Operador / Vendedor</li>
                    <li><strong>lrojas</strong> · Operador / Vendedor</li>
                </ul>
                <p class="form-help">
                    La contraseña de cada cuenta la indica el Administrador.
                    Una cuenta inactiva no puede iniciar sesión.
                </p>
            </aside>
        </section>

        <p class="login-pie">Estación Nexo · Sistema de gestión de combustibles</p>
    </main>
</body>
</html>
