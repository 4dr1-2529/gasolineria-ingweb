<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los importes se muestran igual que en la versión 1: 50.00 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Detalle de venta · Estación Nexo</title>
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
            <c:set var="momentos" value="${fn:split(venta.fechaHora, 'T')}"/>
            <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>

            <div class="page-heading">
                <div>
                    <p class="eyebrow">OPERACIÓN DIARIA / P10</p>
                    <h1>Detalle de venta</h1>
                    <p class="subtitle">Consultar cada detalle de la venta.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ventas/list">← Historial</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Ver combustibles</a>
                </div>
            </div>

            <section class="panel receipt" id="${venta.id}">
                <h2>Venta <c:out value="${venta.id}"/></h2>
                <span class="badge">
                    <c:out value="${venta.estado}"/>
                </span>
                <dl class="receipt-meta">
                    <div>
                        <dt>Fecha</dt>
                        <dd>
                            <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/> ·
                            <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                        </dd>
                    </div>
                    <div>
                        <dt>Operador</dt>
                        <dd><c:out value="${empty operador ? '—' : operador}"/></dd>
                    </div>
                </dl>
                <div class="table-responsive">
                    <table class="table">
                        <caption>Detalle de <c:out value="${venta.id}"/></caption>
                        <thead>
                            <tr>
                                <th scope="col">Combustible</th>
                                <th scope="col">Cantidad</th>
                                <th scope="col">Precio unitario</th>
                                <th scope="col">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${detalles}" var="detalle">
                                <tr>
                                    <td><c:out value="${productosPorId[detalle.idProducto].nombre}"/></td>
                                    <td><fmt:formatNumber value="${detalle.cantidad}" type="number" maxFractionDigits="2"/> L</td>
                                    <td>S/ <fmt:formatNumber value="${detalle.precioUnitario}" type="number" minFractionDigits="2"/></td>
                                    <td>S/ <fmt:formatNumber value="${detalle.subtotal}" type="number" minFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty detalles}">
                                <tr>
                                    <td colspan="4">Esta venta no tiene líneas de detalle.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="total">
                    <span>Total</span>
                    <strong>S/ <fmt:formatNumber value="${venta.total}" type="number" minFractionDigits="2"/></strong>
                </div>
                <p class="muted-text">
                    RN04 · Un único ingreso económico de
                    S/ <fmt:formatNumber value="${venta.total}" type="number" minFractionDigits="2"/>
                    asociado a esta venta, que se consulta en el módulo Finanzas.
                </p>
            </section>

            <p class="notice">
                Los litros de esta venta se descontaron del stock del producto en memoria y su
                salida de inventario quedó registrada en el módulo Inventario. El ingreso de caja
                se consulta en el módulo Finanzas.
            </p>
        </main>
    </div>
</body>
</html>
