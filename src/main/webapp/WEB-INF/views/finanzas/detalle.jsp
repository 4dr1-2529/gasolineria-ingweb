<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
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
    <title>Detalle de movimiento de caja · Estación Nexo</title>
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
                <a href="${pageContext.request.contextPath}/finanzas/list" aria-current="page">Finanzas</a>
                <a href="${pageContext.request.contextPath}/finanzas/conceptos/list">Conceptos económicos</a>
                <a href="${pageContext.request.contextPath}/finanzas/conceptos/crear">Nuevo concepto</a>
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
            <c:set var="momentos" value="${fn:split(movimiento.fechaHora, 'T')}"/>
            <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>

            <div class="page-heading">
                <div>
                    <p class="eyebrow">OPERACIÓN DIARIA / P30</p>
                    <h1>Detalle de movimiento de caja</h1>
                    <p class="subtitle">Consultar el detalle de cada movimiento de caja del día.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/list">← Resumen financiero</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/ingresos">Ingresos</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/egresos">Egresos</a>
                </div>
            </div>

            <p class="notice">
                Corte de referencia: 10 de septiembre de 2026, 12:00. Los
                <c:out value="${cantidad}"/> movimientos de caja nacen de una compra o de una
                venta; no hay movimientos capturados a mano.
            </p>

            <section class="panel receipt" id="${movimiento.id}">
                <h2>Movimiento <c:out value="${movimiento.id}"/></h2>
                <c:choose>
                    <c:when test="${movimiento.tipo == 'Ingreso'}">
                        <span class="badge">Ingreso</span>
                    </c:when>
                    <c:otherwise>
                        <span class="badge muted">Egreso</span>
                    </c:otherwise>
                </c:choose>
                <dl class="receipt-meta">
                    <div>
                        <dt>Fecha</dt>
                        <dd>
                            <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/> ·
                            <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                        </dd>
                    </div>
                    <div>
                        <dt>Concepto</dt>
                        <dd><c:out value="${concepto.nombre}"/></dd>
                    </div>
                    <div>
                        <dt>Origen</dt>
                        <dd><c:out value="${movimiento.idVenta}"/><c:out value="${movimiento.idCompra}"/></dd>
                    </div>
                    <div>
                        <dt>Responsable</dt>
                        <dd><c:out value="${responsable}"/></dd>
                    </div>
                    <div>
                        <dt>Trazabilidad</dt>
                        <dd>
                            <c:if test="${not empty movimiento.idVenta}">
                                <a href="${pageContext.request.contextPath}/ventas/detalle?id=${movimiento.idVenta}">Ver <c:out value="${movimiento.idVenta}"/></a>
                            </c:if>
                            <c:if test="${not empty movimiento.idCompra}">
                                <a href="${pageContext.request.contextPath}/compras/detalle?id=${movimiento.idCompra}">Ver <c:out value="${movimiento.idCompra}"/></a>
                            </c:if>
                        </dd>
                    </div>
                </dl>
                <div class="total">
                    <span>Monto</span>
                    <strong>S/ <fmt:formatNumber value="${movimiento.monto}" type="number" minFractionDigits="2"/></strong>
                </div>
            </section>

            <section class="panel receipt">
                <h2>Conciliación de caja</h2>
                <dl class="receipt-meta">
                    <div>
                        <dt>Apertura</dt>
                        <dd>S/ <fmt:formatNumber value="${apertura}" type="number" minFractionDigits="2"/></dd>
                    </div>
                    <div>
                        <dt>Ingresos</dt>
                        <dd>S/ <fmt:formatNumber value="${ingresos}" type="number" minFractionDigits="2"/></dd>
                    </div>
                    <div>
                        <dt>Egresos</dt>
                        <dd>S/ <fmt:formatNumber value="${egresos}" type="number" minFractionDigits="2"/></dd>
                    </div>
                    <div>
                        <dt>Saldo</dt>
                        <dd>S/ <fmt:formatNumber value="${saldo}" type="number" minFractionDigits="2"/></dd>
                    </div>
                </dl>
                <p class="muted-text">
                    <fmt:formatNumber value="${apertura}" type="number" minFractionDigits="2"/> +
                    <fmt:formatNumber value="${ingresos}" type="number" minFractionDigits="2"/> −
                    <fmt:formatNumber value="${egresos}" type="number" minFractionDigits="2"/> =
                    <fmt:formatNumber value="${saldo}" type="number" minFractionDigits="2"/>.
                    El egreso corresponde a la compra <c:out value="${origenesEgresos}"/> (RN04) y los
                    ingresos a las ventas <c:out value="${origenesIngresos}"/> (RN04).
                </p>
            </section>

            <p class="notice">
                RN04 · Cada movimiento de caja tiene un único origen: una compra con un solo
                egreso y cada venta con un solo ingreso. No hay movimientos capturados a mano.
            </p>
        </main>
    </div>
</body>
</html>
