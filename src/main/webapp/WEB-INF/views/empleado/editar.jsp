<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar empleado · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <header class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/categorias/list">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <nav>
            <c:set var="esAdmin" value="false"/>
            <c:forEach var="rolMenu" items="${pageContext.request.userPrincipal.authorities}">
                <c:if test="${rolMenu.authority == 'ROLE_ADMIN'}">
                    <c:set var="esAdmin" value="true"/>
                </c:if>
            </c:forEach>
            <c:if test="${esAdmin}">
                <a href="${pageContext.request.contextPath}/categorias/list">Categorías</a>
                <a href="${pageContext.request.contextPath}/categorias/crear">Nueva categoría</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/combustibles/list">Combustibles</a>
            <c:if test="${esAdmin}">
                <a href="${pageContext.request.contextPath}/combustibles/crear">Nuevo combustible</a>
                <a href="${pageContext.request.contextPath}/empleados/list">Empleados</a>
                <a href="${pageContext.request.contextPath}/empleados/crear">Nuevo empleado</a>
                <a href="${pageContext.request.contextPath}/usuarios/list">Usuarios</a>
                <a href="${pageContext.request.contextPath}/usuarios/crear">Nuevo usuario</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/asistencia/mi">Asistencia</a>
            <c:if test="${esAdmin}">
                <a href="${pageContext.request.contextPath}/compras/list">Compras</a>
            </c:if>
            <a href="${pageContext.request.contextPath}/inventario/list">Inventario</a>
            <a href="${pageContext.request.contextPath}/ventas/list">Ventas</a>
            <c:if test="${esAdmin}">
                <a href="${pageContext.request.contextPath}/finanzas/list">Finanzas</a>
            </c:if>
        </nav>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P25</p>
                    <h1>Editar empleado</h1>
                    <p class="subtitle">Representar el registro y la edición de un empleado.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/empleados/list">← Personal</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                Los cambios se actualizan en memoria mientras la aplicación está en ejecución.
                No hay base de datos.
            </p>

            <section class="panel">
                <h2>Editar empleado</h2>
                <!-- Los campos llegan precargados desde el objeto "empleado" del Model -->
                <form:form action="${pageContext.request.contextPath}/empleados/editar" method="post" modelAttribute="empleado">
                    <form:hidden path="id" />
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="dni">DNI *</form:label>
                            <form:input path="dni" id="dni" cssClass="form-control" type="text" required="required" pattern="[0-9]{8}" maxlength="8" title="Exactamente 8 dígitos" />
                        </div>
                        <div class="field">
                            <form:label path="nombres">Nombres *</form:label>
                            <form:input path="nombres" id="nombres" cssClass="form-control" type="text" required="required" minlength="3" maxlength="60" />
                        </div>
                        <div class="field">
                            <form:label path="apellidos">Apellidos *</form:label>
                            <form:input path="apellidos" id="apellidos" cssClass="form-control" type="text" required="required" minlength="3" maxlength="60" />
                        </div>
                        <div class="field">
                            <form:label path="cargo">Cargo *</form:label>
                            <form:input path="cargo" id="cargo" cssClass="form-control" type="text" required="required" minlength="3" maxlength="40" />
                        </div>
                        <div class="field">
                            <form:label path="telefono">Teléfono *</form:label>
                            <form:input path="telefono" id="telefono" cssClass="form-control" type="tel" required="required" pattern="[0-9]{3} [0-9]{3} [0-9]{3}" title="Formato: 000 000 000" />
                        </div>
                        <div class="field">
                            <form:label path="estado">Estado *</form:label>
                            <form:select path="estado" id="estado" cssClass="form-select" required="required">
                                <form:option value="Activo" label="Activo"/>
                                <form:option value="Inactivo" label="Inactivo"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Guardar edición</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/empleados/list">Cancelar</a>
                    </div>
                </form:form>
            </section>
        </main>
    </div>
</body>
</html>
