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
    <title>Compras · Estación Nexo</title>
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
                <a href="${pageContext.request.contextPath}/compras/list" aria-current="page">Compras</a>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P20</p>
                    <h1>Compras</h1>
                    <p class="subtitle">Registrar y consultar el abastecimiento de combustible.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/compras/crear">+ Nueva compra</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/detalle?id=C001">Ver C001</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <div class="kpi-grid">
                <section class="kpi">
                    <p>Compras registradas</p>
                    <strong>S/ <fmt:formatNumber value="${totalMonto}" type="number" minFractionDigits="2"/></strong>
                    <small>
                        <c:out value="${fn:length(compras)}"/>
                        <c:out value="${fn:length(compras) == 1 ? 'compra confirmada' : 'compras confirmadas'}"/> ·
                        <c:out value="${fn:length(detalles)}"/>
                        <c:out value="${fn:length(detalles) == 1 ? 'línea' : 'líneas'}"/>
                    </small>
                </section>
                <section class="kpi">
                    <p>Litros adquiridos</p>
                    <strong><fmt:formatNumber value="${totalLitros}" type="number" maxFractionDigits="2"/> L</strong>
                    <small><c:out value="${productosComprados}"/></small>
                </section>
                <section class="kpi">
                    <p>Efecto en inventario</p>
                    <strong>+<fmt:formatNumber value="${totalLitros}" type="number" maxFractionDigits="2"/> L</strong>
                    <small>Se suma al stock de los productos</small>
                </section>
            </div>

            <section class="panel">
                <h2>Compras registradas</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Compras registradas: <c:out value="${fn:length(compras)}"/> ·
                            Total: S/ <fmt:formatNumber value="${totalMonto}" type="number" minFractionDigits="2"/> ·
                            <fmt:formatNumber value="${totalLitros}" type="number" maxFractionDigits="2"/> L
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Código</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Proveedor</th>
                                <th scope="col">Combustibles</th>
                                <th scope="col">Litros</th>
                                <th scope="col">Total</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Detalle</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${compras}" var="compra">
                                <c:set var="momentos" value="${fn:split(compra.fechaHora, 'T')}"/>
                                <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>
                                <tr>
                                    <td><c:out value="${compra.id}"/></td>
                                    <td>
                                        <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/>
                                        <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                                    </td>
                                    <td><c:out value="${compra.proveedor}"/></td>
                                    <td><c:out value="${combustiblesPorCompra[compra.id]}"/></td>
                                    <td><fmt:formatNumber value="${litrosPorCompra[compra.id]}" type="number" maxFractionDigits="2"/> L</td>
                                    <td>S/ <fmt:formatNumber value="${compra.total}" type="number" minFractionDigits="2"/></td>
                                    <td>
                                        <span class="badge">
                                            <c:out value="${compra.estado}"/>
                                        </span>
                                    </td>
                                    <td>
                                        <a href="${pageContext.request.contextPath}/compras/detalle?id=${compra.id}">
                                            Ver <c:out value="${compra.id}"/>
                                        </a>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty compras}">
                                <tr>
                                    <td colspan="8">No hay compras registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                RN03 y RN04 · Cada compra confirmada suma los litros recibidos al stock, representa un
                único egreso económico por su importe total y genera su entrada de inventario;
                el egreso de caja se consulta en el módulo Finanzas.
            </p>
        </main>
    </div>
</body>
</html>
