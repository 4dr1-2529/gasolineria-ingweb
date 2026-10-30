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
    <title>Resumen financiero · Estación Nexo</title>
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

        <main id="contenido"><h1>Dashboard · P03</h1><p>Datos registrados en MySQL. Fecha actual: <c:out value="${hoy}"/> · America/Lima.</p><div class="kpi-grid"><section class="kpi"><h2>Existencias totales</h2><strong><fmt:formatNumber value="${stock}" maxFractionDigits="3"/> L</strong><p>Combustibles y categorías activos · Inventario.</p></section><c:if test="${verVentas}"><section class="kpi"><h2>Ventas del día</h2><strong>S/ <fmt:formatNumber value="${actual.ventas}" minFractionDigits="2" maxFractionDigits="2"/></strong></section></c:if><c:if test="${admin}"><section class="kpi"><h2>Ingresos del día</h2><strong>S/ <fmt:formatNumber value="${actual.ingresos}" minFractionDigits="2" maxFractionDigits="2"/></strong></section><section class="kpi"><h2>Egresos del día</h2><strong>S/ <fmt:formatNumber value="${actual.egresos}" minFractionDigits="2" maxFractionDigits="2"/></strong></section><section class="kpi"><h2>Saldo de caja</h2><strong>S/ <fmt:formatNumber value="${actual.saldo}" minFractionDigits="2" maxFractionDigits="2"/></strong></section></c:if></div><c:forEach var="serie" items="${series}"><section class="panel"><h2><c:out value="${serie.nombre}"/> (<c:out value="${serie.unidad}"/>)</h2><p><c:out value="${serie.objetivo}"/>. Periodo: <c:out value="${dias[0].fecha}"/> a <c:out value="${dias[fn:length(dias)-1].fecha}"/>. Frecuencia diaria.</p><svg viewBox="0 0 800 190" role="img" aria-label="${serie.nombre}" style="width:100%;max-height:240px"><path d="M60 20V140H740" fill="none" stroke="currentColor"/><polyline points="${serie.polilinea}" fill="none" stroke="currentColor" stroke-width="2"/><c:forEach var="punto" items="${serie.puntos}"><circle cx="${punto.x}" cy="${punto.y}" r="3" fill="currentColor"/></c:forEach><text x="2" y="18"><c:out value="${serie.maximo}"/></text><text x="2" y="140"><c:out value="${serie.minimo}"/></text><text x="60" y="165"><c:out value="${dias[0].fecha}"/></text><text x="640" y="165"><c:out value="${dias[fn:length(dias)-1].fecha}"/></text><text x="330" y="185">Fecha de operación (día)</text></svg><p>Fórmula: <c:out value="${serie.formula}"/>. Fuente / módulo: <c:out value="${serie.fuente}"/>.</p></section></c:forEach><c:if test="${verVentas}"><section class="panel"><h2>Datos de las series</h2><table class="table"><thead><tr><th>Fecha</th><th>Ventas S/</th><th>Litros L</th><c:if test="${admin}"><th>Ingresos S/</th><th>Egresos S/</th><th>Saldo S/</th></c:if></tr></thead><tbody><c:forEach var="dia" items="${dias}"><tr><td><c:out value="${dia.fecha}"/></td><td><c:out value="${dia.ventas}"/></td><td><c:out value="${dia.litros}"/></td><c:if test="${admin}"><td><c:out value="${dia.ingresos}"/></td><td><c:out value="${dia.egresos}"/></td><td><c:out value="${dia.saldo}"/></td></c:if></tr></c:forEach></tbody></table><c:if test="${empty dias}"><p>Aún no hay operaciones registradas para representar.</p></c:if></section></c:if></main>
    </div>
</body>
</html>
