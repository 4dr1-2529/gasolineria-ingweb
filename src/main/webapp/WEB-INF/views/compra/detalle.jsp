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
    <title>Detalle de compra · Estación Nexo</title>
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
            <a href="${pageContext.request.contextPath}/compras/list" aria-current="page">Compras</a>
            <a href="${pageContext.request.contextPath}/inventario/list">Inventario</a>
            <a href="${pageContext.request.contextPath}/ventas/list">Ventas</a>
            <a href="${pageContext.request.contextPath}/finanzas/list">Finanzas</a>
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
            <c:set var="momentos" value="${fn:split(compra.fechaHora, 'T')}"/>
            <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>

            <div class="page-heading">
                <div>
                    <p class="eyebrow">OPERACIÓN DIARIA / P21</p>
                    <h1>Detalle de compra</h1>
                    <p class="subtitle">Consultar cada compra y sus efectos en inventario y caja.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/list">← Compras</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Ver combustibles</a>
                </div>
            </div>

            <section class="panel receipt" id="${compra.id}">
                <h2>Compra <c:out value="${compra.id}"/></h2>
                <span class="badge">
                    <c:out value="${compra.estado}"/>
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
                        <dt>Proveedor</dt>
                        <dd><c:out value="${compra.proveedor}"/></dd>
                    </div>
                    <div>
                        <dt>Responsable</dt>
                        <dd><c:out value="${empty responsable ? '—' : responsable}"/></dd>
                    </div>
                </dl>
                <div class="table-responsive">
                    <table class="table">
                        <caption>Detalle de <c:out value="${compra.id}"/></caption>
                        <thead>
                            <tr>
                                <th scope="col">Combustible</th>
                                <th scope="col">Cantidad</th>
                                <th scope="col">Precio de compra</th>
                                <th scope="col">Subtotal</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${detalles}" var="detalle">
                                <tr>
                                    <td><c:out value="${productosPorId[detalle.idProducto].nombre}"/></td>
                                    <td><fmt:formatNumber value="${detalle.cantidad}" type="number" maxFractionDigits="2"/> L</td>
                                    <td>S/ <fmt:formatNumber value="${detalle.precioCompra}" type="number" minFractionDigits="2"/></td>
                                    <td>S/ <fmt:formatNumber value="${detalle.subtotal}" type="number" minFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty detalles}">
                                <tr>
                                    <td colspan="4">Esta compra no tiene líneas de detalle.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
                <div class="total">
                    <span>Total</span>
                    <strong>S/ <fmt:formatNumber value="${compra.total}" type="number" minFractionDigits="2"/></strong>
                </div>
                <p class="muted-text">
                    RN04 · La compra produce un único egreso económico de
                    S/ <fmt:formatNumber value="${compra.total}" type="number" minFractionDigits="2"/>,
                    que se consulta en el módulo Finanzas.
                </p>
            </section>

            <p class="notice">
                Los litros de esta compra se sumaron al stock de los productos en memoria y sus
                entradas de inventario quedaron registradas; el egreso de caja se consulta en
                el módulo Finanzas.
            </p>
        </main>
    </div>
</body>
</html>
