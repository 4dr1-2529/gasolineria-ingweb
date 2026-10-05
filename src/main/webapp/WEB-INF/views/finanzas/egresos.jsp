<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los importes se muestran igual que en la versión 1: 1,350.00 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Egresos de caja · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <header class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/categorias/list">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <nav>
            <a href="${pageContext.request.contextPath}/categorias/list">Categorías</a>
            <a href="${pageContext.request.contextPath}/categorias/crear">Nueva categoría</a>
            <a href="${pageContext.request.contextPath}/combustibles/list">Combustibles</a>
            <a href="${pageContext.request.contextPath}/combustibles/crear">Nuevo combustible</a>
            <a href="${pageContext.request.contextPath}/empleados/list">Empleados</a>
            <a href="${pageContext.request.contextPath}/empleados/crear">Nuevo empleado</a>
            <a href="${pageContext.request.contextPath}/usuarios/list">Usuarios</a>
            <a href="${pageContext.request.contextPath}/usuarios/crear">Nuevo usuario</a>
            <a href="${pageContext.request.contextPath}/asistencia/mi">Asistencia</a>
            <a href="${pageContext.request.contextPath}/compras/list">Compras</a>
            <a href="${pageContext.request.contextPath}/inventario/list">Inventario</a>
            <a href="${pageContext.request.contextPath}/ventas/list">Ventas</a>
            <a href="${pageContext.request.contextPath}/finanzas/list" aria-current="page">Finanzas</a>
        </nav>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P16</p>
                    <h1>Egresos de caja</h1>
                    <p class="subtitle">Consultar los egresos de la caja del día.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/list">← Resumen financiero</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/ingresos">Ingresos</a>
                </div>
            </div>

            <section class="panel">
                <h2>Egresos de caja (F27)</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            <c:choose>
                                <c:when test="${fn:length(movimientos) == 1}">
                                    1 egreso · Generado por la compra del día · Total S/
                                    <fmt:formatNumber value="${total}" type="number" minFractionDigits="2"/>
                                </c:when>
                                <c:otherwise>
                                    <c:out value="${fn:length(movimientos)}"/> egresos ·
                                    Generados por las compras del día · Total S/
                                    <fmt:formatNumber value="${total}" type="number" minFractionDigits="2"/>
                                </c:otherwise>
                            </c:choose>
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Código</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Concepto</th>
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
                                    <td>S/ <fmt:formatNumber value="${movimiento.monto}" type="number" minFractionDigits="2"/></td>
                                    <td><c:out value="${movimiento.idVenta}"/><c:out value="${movimiento.idCompra}"/></td>
                                    <td><c:out value="${responsables[movimiento.idUsuario]}"/></td>
                                    <td><a href="${pageContext.request.contextPath}/finanzas/detalle?id=${movimiento.id}">Ver <c:out value="${movimiento.id}"/></a></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty movimientos}">
                                <tr>
                                    <td colspan="7">No hay egresos de caja.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                RN04 · Aquí sólo se consultan movimientos: cada egreso nace de una compra
                confirmada. Ningún monto se escribe a mano. Apertura
                <fmt:formatNumber value="${apertura}" type="number" minFractionDigits="2"/> + ingresos
                <fmt:formatNumber value="${ingresos}" type="number" minFractionDigits="2"/> − egresos
                <fmt:formatNumber value="${egresos}" type="number" minFractionDigits="2"/> = saldo
                <fmt:formatNumber value="${saldo}" type="number" minFractionDigits="2"/>.
            </p>
        </main>
    </div>
</body>
</html>
