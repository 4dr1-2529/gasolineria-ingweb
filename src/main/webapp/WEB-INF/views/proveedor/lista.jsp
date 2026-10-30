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
<h1>Proveedores · P31</h1><c:if test="${not empty mensaje}"><p class="notice" role="status"><c:out value="${mensaje}"/></p></c:if><a class="btn btn-primary" href="${pageContext.request.contextPath}/proveedores/crear">Registrar proveedor</a><form method="get" class="form-grid"><label>RUC<input class="form-control" name="ruc" value="<c:out value='${param.ruc}'/>"/></label><label>Razón social<input class="form-control" name="nombre" value="<c:out value='${param.nombre}'/>"/></label><label>Estado<select name="estado"><option value="">Todos</option><option>Activo</option><option>Inactivo</option></select></label><button class="btn" type="submit">Filtrar</button></form><table class="table"><caption>Proveedores registrados en MySQL</caption><thead><tr><th>RUC</th><th>Razón social</th><th>Teléfono</th><th>Estado</th><th>Acciones</th></tr></thead><tbody><c:forEach var="p" items="${proveedores}"><tr><td><c:out value="${p.ruc}"/></td><td><c:out value="${p.razonSocial}"/></td><td><c:out value="${p.telefono}"/></td><td><c:out value="${p.estado}"/></td><td><a href="${pageContext.request.contextPath}/proveedores/editar?id=${p.id}">Editar</a><form:form method="post" action="${pageContext.request.contextPath}/proveedores/estado"><input type="hidden" name="id" value="${p.id}"/><input type="hidden" name="estado" value="${p.estado == 'Activo' ? 'Inactivo' : 'Activo'}"/><button type="submit">${p.estado == 'Activo' ? 'Desactivar' : 'Activar'}</button></form:form></td></tr></c:forEach></tbody></table>
</main>
    </div>
</body>
</html>
