<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los importes se muestran igual que en la versión 1: 370.00 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Historial de ventas · Estación Nexo</title>
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
            <a href="${pageContext.request.contextPath}/ventas/list" aria-current="page">Ventas</a>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P09</p>
                    <h1>Historial de ventas</h1>
                    <p class="subtitle">Consultar las ventas confirmadas.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/ventas/crear">+ Registrar venta</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <section class="panel">
                <h2>Ventas confirmadas</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Ventas registradas: <c:out value="${fn:length(ventas)}"/> ·
                            Total: <fmt:formatNumber value="${totalLitros}" type="number" maxFractionDigits="2"/> L /
                            S/ <fmt:formatNumber value="${totalMonto}" type="number" minFractionDigits="2"/>
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Código</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Combustible</th>
                                <th scope="col">Litros</th>
                                <th scope="col">Total</th>
                                <th scope="col">Operador</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Detalle</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${ventas}" var="venta">
                                <c:set var="momentos" value="${fn:split(venta.fechaHora, 'T')}"/>
                                <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>
                                <tr>
                                    <td><c:out value="${venta.id}"/></td>
                                    <td>
                                        <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/>
                                        <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                                    </td>
                                    <td><c:out value="${combustiblesPorVenta[venta.id]}"/></td>
                                    <td><fmt:formatNumber value="${litrosPorVenta[venta.id]}" type="number" maxFractionDigits="2"/></td>
                                    <td>S/ <fmt:formatNumber value="${venta.total}" type="number" minFractionDigits="2"/></td>
                                    <td><c:out value="${operadores[venta.idUsuario]}"/></td>
                                    <td>
                                        <span class="badge">
                                            <c:out value="${venta.estado}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/ventas/detalle?id=${venta.id}">
                                            Ver <c:out value="${venta.id}"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty ventas}">
                                <tr>
                                    <td colspan="8">No hay ventas registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                RN04 · Cada venta confirmada representa un único ingreso económico por su importe
                total. El ingreso de caja de cada venta se consulta en el módulo Finanzas; el
                historial no muestra la columna Ingreso de la versión 1.
            </p>
        </main>
    </div>
</body>
</html>
