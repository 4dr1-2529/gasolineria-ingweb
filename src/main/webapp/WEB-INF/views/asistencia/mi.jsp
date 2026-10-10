<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Mi asistencia · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P28</p>
                    <h1>Mi asistencia</h1>
                    <p class="subtitle">Consultar y registrar la asistencia del empleado autenticado.</p>
                </div>
                <div class="actions">
                    <c:if test="${esAdmin}">
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/asistencia/control">Control de asistencia</a>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/empleados/list">Personal</a>
                    </c:if>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <section class="panel">
                <h2>Registrar asistencia del día · F35</h2>
                <c:set var="fechaHoyTexto" value="${fn:split(fechaHoy, '-')}"/>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Marcación de hoy · <c:out value="${fechaHoyTexto[2]}/${fechaHoyTexto[1]}/${fechaHoyTexto[0]}"/>
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Empleado</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Entrada</th>
                                <th scope="col">Salida</th>
                                <th scope="col">Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <tr>
                                <td><c:out value="${empleadoActual.nombres} ${empleadoActual.apellidos}"/></td>
                                <td><c:out value="${fechaHoyTexto[2]}/${fechaHoyTexto[1]}/${fechaHoyTexto[0]}"/></td>
                                <td><c:out value="${empty asistenciaHoy.horaEntrada ? '—' : fn:substring(asistenciaHoy.horaEntrada, 0, 5)}"/></td>
                                <td><c:out value="${empty asistenciaHoy.horaSalida ? '—' : fn:substring(asistenciaHoy.horaSalida, 0, 5)}"/></td>
                                <td>
                                    <c:choose>
                                        <c:when test="${empty asistenciaHoy}">
                                            <c:out value="—"/>
                                        </c:when>
                                        <c:otherwise>
                                            <span class="badge${asistenciaHoy.estado == 'Falta' ? ' muted' : ''}">
                                                <c:out value="${asistenciaHoy.estado}"/>
                                            </span>
                                        </c:otherwise>
                                    </c:choose>
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="actions">
                    <form:form action="${pageContext.request.contextPath}/asistencia/entrada" method="post">
                        <button type="submit" class="btn btn-primary">Registrar entrada</button>
                    </form:form>
                    <form:form action="${pageContext.request.contextPath}/asistencia/salida" method="post">
                        <button type="submit" class="btn btn-secondary">Registrar salida</button>
                    </form:form>
                </div>
                <p class="form-help">
                    La fecha y la hora se generan en el servidor a la hora de Perú (America/Lima)
                    al momento de registrar; los cambios quedan en memoria mientras la
                    aplicación está en ejecución.
                </p>
            </section>

            <section class="panel">
                <h2>Mis marcaciones · F36</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Asistencia de <c:out value="${empleadoActual.nombres} ${empleadoActual.apellidos}"/>
                            · <c:out value="${fn:length(asistencias)}"/> filas del periodo
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Fecha</th>
                                <th scope="col">Entrada</th>
                                <th scope="col">Salida</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Observación</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${asistencias}" var="asistencia">
                                <c:set var="fechaTexto" value="${fn:split(asistencia.fecha, '-')}"/>
                                <tr>
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
                                    <td colspan="5">No hay marcaciones registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <section class="panel">
                <h2>Resumen del periodo · F37</h2>
                <c:set var="presentes" value="0"/>
                <c:set var="faltas" value="0"/>
                <c:forEach items="${asistencias}" var="asistencia">
                    <c:if test="${asistencia.estado == 'Presente'}">
                        <c:set var="presentes" value="${presentes + 1}"/>
                    </c:if>
                    <c:if test="${asistencia.estado == 'Falta'}">
                        <c:set var="faltas" value="${faltas + 1}"/>
                    </c:if>
                </c:forEach>
                <div class="kpi-grid">
                    <section class="kpi">
                        <p>Días presentes</p>
                        <strong><c:out value="${presentes}"/></strong>
                        <small>con estado Presente</small>
                    </section>
                    <section class="kpi">
                        <p>Días con falta</p>
                        <strong><c:out value="${faltas}"/></strong>
                        <small>con estado Falta</small>
                    </section>
                    <section class="kpi">
                        <p>Última marcación</p>
                        <c:choose>
                            <c:when test="${not empty asistencias}">
                                <c:set var="ultima" value="${asistencias[0]}"/>
                                <c:set var="fechaUltima" value="${fn:split(ultima.fecha, '-')}"/>
                                <strong><c:out value="${fechaUltima[2]}/${fechaUltima[1]}/${fechaUltima[0]}"/></strong>
                                <small><c:out value="${empty ultima.horaEntrada ? '—' : fn:substring(ultima.horaEntrada, 0, 5)}"/><c:if test="${not empty ultima.horaSalida}"> a <c:out value="${fn:substring(ultima.horaSalida, 0, 5)}"/></c:if></small>
                            </c:when>
                            <c:otherwise>
                                <strong>—</strong>
                                <small>sin marcaciones</small>
                            </c:otherwise>
                        </c:choose>
                    </section>
                </div>
            </section>

            <p class="notice">
                RN05 · Sólo se consulta la asistencia del empleado actual; cada
                fecha aparece una sola vez; la salida es posterior a la entrada; y el estado se deriva
                de las horas registradas. Todas las reglas se aplican en el servidor.
            </p>
        </main>
    </div>
</body>
</html>
