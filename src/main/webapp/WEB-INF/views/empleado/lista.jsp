<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Empleados · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P18</p>
                    <h1>Empleados</h1>
                    <p class="subtitle">Representar la administración del personal.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/empleados/crear">+ Empleado</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

<form method="get" class="form-grid"><label>DNI<input name="dni" value="<c:out value='${param.dni}'/>"/></label><label>Nombre o apellido<input name="nombre" value="<c:out value='${param.nombre}'/>"/></label><label>Cargo<input name="cargo" value="<c:out value='${param.cargo}'/>"/></label><label>Estado<select name="estado"><option value="">Todos</option><option>Activo</option><option>Inactivo</option></select></label><button class="btn" type="submit">Filtrar personal</button></form>            <section class="panel">
                <h2>Equipo de la estación</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            <c:out value="${fn:length(empleados)}"/> empleados registrados
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">DNI</th>
                                <th scope="col">Nombres</th>
                                <th scope="col">Apellidos</th>
                                <th scope="col">Cargo</th>
                                <th scope="col">Teléfono</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${empleados}" var="empleado">
                                <tr>
                                    <td><c:out value="${empleado.dni}"/></td>
                                    <td><c:out value="${empleado.nombres}"/></td>
                                    <td><c:out value="${empleado.apellidos}"/></td>
                                    <td><c:out value="${empleado.cargo}"/></td>
                                    <td><c:out value="${empleado.telefono}"/></td>
                                    <td>
                                        <span class="badge${empleado.estado == 'Inactivo' ? ' muted' : ''}">
                                            <c:out value="${empleado.estado}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/empleados/editar?id=${empleado.id}">Editar</a>
                                        <!-- RN02: se cambia el estado, nunca se elimina el registro -->
                                        <c:choose>
                                            <c:when test="${empleado.estado == 'Activo'}">
                                                <form:form action="${pageContext.request.contextPath}/empleados/estado" method="post" cssClass="inline-accion">
                                                    <input type="hidden" name="id" value="${empleado.id}"/>
                                                    <input type="hidden" name="estado" value="Inactivo"/>
                                                    <button type="submit" class="btn btn-secondary">Desactivar</button>
                                                </form:form>
                                            </c:when>
                                            <c:otherwise>
                                                <form:form action="${pageContext.request.contextPath}/empleados/estado" method="post" cssClass="inline-accion">
                                                    <input type="hidden" name="id" value="${empleado.id}"/>
                                                    <input type="hidden" name="estado" value="Activo"/>
                                                    <button type="submit" class="btn btn-primary">Activar</button>
                                                </form:form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty empleados}">
                                <tr>
                                    <td colspan="7">No hay empleados registrados.</td>
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
