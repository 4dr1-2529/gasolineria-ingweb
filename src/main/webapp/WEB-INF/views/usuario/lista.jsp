<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Usuarios · Estación Nexo</title>
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
            <div class="page-heading">
                <div>
                    <p class="eyebrow">OPERACIÓN DIARIA / P19</p>
                    <h1>Usuarios</h1>
                    <p class="subtitle">Representar las cuentas y sus roles conceptuales.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/usuarios/crear">+ Usuario</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

<form method="get" class="form-grid"><label>Username<input name="username" value="<c:out value='${param.username}'/>"/></label><label>Empleado<input name="empleado" value="<c:out value='${param.empleado}'/>"/></label><label>Rol<select name="rol"><option value="">Todos</option><option>Administrador</option><option>Operador</option><option>Vendedor</option></select></label><label>Estado<select name="estado"><option value="">Todos</option><option>Activo</option><option>Inactivo</option></select></label><button class="btn" type="submit">Filtrar usuarios</button></form>            <section class="panel">
                <h2>Cuentas de acceso</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            <c:out value="${fn:length(usuarios)}"/> usuarios registrados
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Empleado</th>
                                <th scope="col">Username</th>
                                <th scope="col">Rol</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Último acceso</th><th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${usuarios}" var="usuario">
                                <tr>
                                    <td><c:out value="${nombresEmpleados[usuario.idEmpleado]}"/></td>
                                    <td><c:out value="${usuario.username}"/></td>
                                    <td><c:out value="${usuario.rol}"/></td>
                                    <td>
                                        <span class="badge">
                                            <c:out value="${usuario.estado}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <c:out value="${usuario.ultimoAcceso}"/></td><td><a href="${pageContext.request.contextPath}/usuarios/editar?id=${usuario.id}">Editar</a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty usuarios}">
                                <tr>
                                    <td colspan="6">No hay usuarios registrados.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
