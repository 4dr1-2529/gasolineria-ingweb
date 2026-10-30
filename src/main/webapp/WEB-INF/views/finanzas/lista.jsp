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

        <main id="contenido">
            <div class="page-heading">
                <div>
                    <p class="eyebrow">OPERACIÓN DIARIA / P15</p>
                    <h1>Resumen financiero</h1>
                    <p class="subtitle">Consultar los movimientos de dinero.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/finanzas/ingresos">Ingresos</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/egresos">Egresos</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/conceptos/list">Conceptos</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

<section class="panel"><h2>Caja diaria</h2><c:choose><c:when test="${empty cajaActual}"><p>No hay caja abierta para hoy.</p><form:form method="post" action="${pageContext.request.contextPath}/finanzas/caja/abrir"><label for="saldo">Saldo de apertura (S/)</label><input class="form-control" id="saldo" name="saldo" type="number" min="0" step="0.01" required/><button class="btn btn-primary" type="submit">Abrir caja</button></form:form></c:when><c:otherwise><p>Fecha: <c:out value="${cajaActual.fecha}"/> · Estado: <c:out value="${cajaActual.estado}"/> · Apertura: S/ <c:out value="${cajaActual.saldoApertura}"/></p><c:if test="${cajaActual.estado == 'ABIERTA'}"><form:form method="post" action="${pageContext.request.contextPath}/finanzas/caja/cerrar"><button class="btn btn-primary" type="submit">Cerrar caja con saldo calculado</button></form:form></c:if><c:if test="${cajaActual.estado == 'CERRADA'}"><p>Saldo de cierre: S/ <c:out value="${cajaActual.saldoCierre}"/> · Cerrada: <c:out value="${cajaActual.fechaCierre}"/></p></c:if></c:otherwise></c:choose><details><summary>Historial de cajas</summary><table class="table"><thead><tr><th>Fecha</th><th>Apertura S/</th><th>Cierre S/</th><th>Estado</th></tr></thead><tbody><c:forEach var="caja" items="${cajas}"><tr><td><c:out value="${caja.fecha}"/></td><td><c:out value="${caja.saldoApertura}"/></td><td><c:out value="${caja.saldoCierre}"/></td><td><c:out value="${caja.estado}"/></td></tr></c:forEach></tbody></table></details></section>            <%-- KPI de la versión 1: ingresos, egresos y saldo de caja --%>
            <div class="kpi-grid">
                <section class="kpi">
                    <p>Ingresos de hoy</p>
                    <strong>S/ <fmt:formatNumber value="${ingresos}" type="number" minFractionDigits="2"/></strong>
                    <small>Ingresos por ventas (<c:out value="${origenesIngresos}"/>)</small>
                </section>
                <section class="kpi">
                    <p>Egresos de hoy</p>
                    <strong>S/ <fmt:formatNumber value="${egresos}" type="number" minFractionDigits="2"/></strong>
                    <small>Egresos por compras (<c:out value="${origenesEgresos}"/>)</small>
                </section>
                <section class="kpi">
                    <p>Saldo de caja</p>
                    <strong>S/ <fmt:formatNumber value="${saldo}" type="number" minFractionDigits="2"/></strong>
                    <small>Apertura de caja: S/ <fmt:formatNumber value="${apertura}" type="number" minFractionDigits="2"/></small>
                </section>
            </div>

            <section class="panel">
                <h2>Movimientos económicos recientes</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Movimientos económicos en soles · Apertura <fmt:formatNumber value="${apertura}" type="number" minFractionDigits="2"/> +
                            ingresos <fmt:formatNumber value="${ingresos}" type="number" minFractionDigits="2"/> −
                            egresos <fmt:formatNumber value="${egresos}" type="number" minFractionDigits="2"/> =
                            saldo <fmt:formatNumber value="${saldo}" type="number" minFractionDigits="2"/>
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Código</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Concepto</th>
                                <th scope="col">Tipo</th>
                                <th scope="col">Monto</th>
                                <th scope="col">Origen</th>
                                <th scope="col">Responsable</th>
                                <th scope="col">Detalle</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${movimientos}" var="movimiento">
                                <c:set var="momentos" value="${fn:split(movimiento.fechaHora, 'T')}"/>
                                <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>
                                <tr>
                                    <td><c:out value="${movimiento.id}"/></td>
                                    <td>
                                        <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/>
                                        <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                                    </td>
                                    <td><c:out value="${conceptosPorId[movimiento.idConcepto].nombre}"/></td>
                                    <td>
                                        <c:choose>
                                            <c:when test="${movimiento.tipo == 'Ingreso'}">
                                                <span class="badge">Ingreso</span>
                                            </c:when>
                                            <c:otherwise>
                                                <span class="badge muted">Egreso</span>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                    <td>S/ <fmt:formatNumber value="${movimiento.monto}" type="number" minFractionDigits="2"/></td>
                                    <td><c:out value="${movimiento.idVenta}"/><c:out value="${movimiento.idCompra}"/></td>
                                    <td><c:out value="${responsables[movimiento.idUsuario]}"/></td>
                                    <td><a href="${pageContext.request.contextPath}/finanzas/detalle?id=${movimiento.id}">Ver <c:out value="${movimiento.id}"/></a></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty movimientos}">
                                <tr>
                                    <td colspan="8">No hay movimientos de caja.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                Inventario expresa litros; finanzas expresa soles. Aquí sólo se consultan movimientos:
                los ingresos nacen de las ventas (<c:out value="${origenesIngresos}"/>) (RN04) y los
                egresos de las compras (<c:out value="${origenesEgresos}"/>) (RN04). Ningún monto se escribe a mano.
            </p>
        </main>
    </div>
</body>
</html>
