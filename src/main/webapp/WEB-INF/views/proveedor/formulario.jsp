<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear categoría · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <header class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <%@ include file="/WEB-INF/views/shared/menu.jspf" %>
        <div class="sesion">
            <span class="sesion-usuario">Sesión: <c:out value="${pageContext.request.remoteUser}"/></span>
            <form:form action="${pageContext.request.contextPath}/logout" method="post" cssClass="sesion-salir">
                <button type="submit">Cerrar sesión</button>
            </form:form>
        </div>
        <div class="sidebar-bottom">
            INGENIERÍA WEB
            <span>GRUPO G1 · 2026</span>
        </div>
    </header>

    <div class="page">
        <div class="topbar">
            <span>Centro de operaciones / Estación Nexo</span>
            <span class="demo">Versión 2 · Spring Boot</span>
        </div>

        <main id="contenido">
<h1>Proveedor · P32</h1><c:if test="${not empty mensaje}"><p class="notice" role="status"><c:out value="${mensaje}"/></p></c:if><form:form method="post" action="${pageContext.request.contextPath}/proveedores/${empty proveedor.id ? 'crear' : 'editar'}" modelAttribute="proveedor"><form:hidden path="id"/><div class="form-grid"><div class="field"><form:label path="ruc">RUC</form:label><form:input path="ruc" maxlength="11" required="required" pattern="[0-9]{11}" cssClass="form-control"/></div><div class="field"><form:label path="razonSocial">Razón social</form:label><form:input path="razonSocial" maxlength="120" required="required" cssClass="form-control"/></div><div class="field"><form:label path="contacto">Contacto</form:label><form:input path="contacto" maxlength="80" cssClass="form-control"/></div><div class="field"><form:label path="telefono">Teléfono</form:label><form:input path="telefono" maxlength="15" cssClass="form-control"/></div><div class="field"><form:label path="correo">Correo</form:label><form:input path="correo" maxlength="120" cssClass="form-control"/></div><div class="field"><form:label path="direccion">Dirección</form:label><form:input path="direccion" maxlength="200" cssClass="form-control"/></div><div class="field"><form:label path="estado">Estado</form:label><form:select path="estado"><form:option value="Activo"/><form:option value="Inactivo"/></form:select></div></div><button class="btn btn-primary" type="submit">Guardar proveedor</button><a class="btn" href="${pageContext.request.contextPath}/proveedores/list">Cancelar</a></form:form>
</main>
    </div>
</body>
</html>
