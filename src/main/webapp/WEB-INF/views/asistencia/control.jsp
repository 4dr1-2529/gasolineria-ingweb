<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Control de asistencia · Estación Nexo</title>
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
            <a href="${pageContext.request.contextPath}/asistencia/mi" aria-current="page">Asistencia</a>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P29</p>
                    <h1>Control de asistencia</h1>
                    <p class="subtitle">Consultar la asistencia de todo el personal.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/asistencia/mi">← Mi asistencia</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/empleados/list">Personal</a>
                </div>
            </div>

            <section class="panel">
                <h2>Filtrar por empleado</h2>
                <form action="${pageContext.request.contextPath}/asistencia/control" method="get">
                    <div class="form-grid">
                        <div class="field">
                            <label for="idEmpleado">Empleado</label>
                            <select class="form-select" id="idEmpleado" name="idEmpleado">
                                <option value="">Todo el personal</option>
                                <c:forEach items="${empleadosPorId}" var="entrada">
                                    <option value="${entrada.value.id}"${param.idEmpleado == entrada.value.id ? ' selected' : ''}>
                                        <c:out value="${entrada.value.nombres} ${entrada.value.apellidos}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Consultar asistencia</button>
                    </div>
                    <p class="form-help">
                        La consulta muestra los registros de asistencia del personal; el filtro por empleado es opcional.
                    </p>
                </form>
            </section>

            <section class="panel">
                <h2>Asistencia del personal · F38</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            <c:out value="${fn:length(asistencias)}"/> asistencias registradas
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">DNI</th>
                                <th scope="col">Empleado</th>
                                <th scope="col">Cargo</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Entrada</th>
                                <th scope="col">Salida</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Observación</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${asistencias}" var="asistencia">
                                <c:set var="empleado" value="${empleadosPorId[asistencia.idEmpleado]}"/>
                                <c:set var="fechaTexto" value="${fn:split(asistencia.fecha, '-')}"/>
                                <tr>
                                    <td><c:out value="${empleado.dni}"/></td>
                                    <td><c:out value="${empleado.nombres} ${empleado.apellidos}"/></td>
                                    <td><c:out value="${empleado.cargo}"/></td>
                                    <td><c:out value="${fechaTexto[2]}/${fechaTexto[1]}/${fechaTexto[0]}"/></td>
                                    <td><c:out value="${empty asistencia.horaEntrada ? '—' : fn:substring(asistencia.horaEntrada, 0, 5)}"/></td>
                                    <td><c:out value="${empty asistencia.horaSalida ? '—' : fn:substring(asistencia.horaSalida, 0, 5)}"/></td>
                                    <td>
                                        <span class="badge${asistencia.estado == 'Falta' ? ' muted' : ''}">
                                            <c:out value="${asistencia.estado}"/>
                                        </span>
                                    </td>
                                    <td><c:out value="${empty asistencia.observacion ? '—' : asistencia.observacion}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty asistencias}">
                                <tr>
                                    <td colspan="8">No hay asistencias registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                RN05 · Cada empleado aparece una sola vez por fecha y su estado sale de las
                horas registradas. El control es una consulta de solo lectura: las marcaciones se
                realizan en Mi asistencia (P28).
            </p>
        </main>
    </div>
</body>
</html>
