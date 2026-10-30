<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los litros se muestran igual que en la versión 1: 10 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Salidas de inventario · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P14</p>
                    <h1>Salidas de inventario</h1>
                    <p class="subtitle">Consultar las salidas de combustible registradas.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/inventario/salida/crear">Registrar salida</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/inventario/list">Existencias</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/inventario/entradas">Entradas</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <section class="panel">
                <h2>Salidas registradas</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Salidas registradas: <c:out value="${fn:length(movimientos)}"/> · Total:
                            <fmt:formatNumber value="${totalLitros}" type="number" maxFractionDigits="3"/> L
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Código</th>
                                <th scope="col">Tipo</th>
                                <th scope="col">Producto</th>
                                <th scope="col">Cantidad</th>
                                <th scope="col">Fecha</th>
                                <th scope="col">Responsable</th>
                                <th scope="col">Motivo</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${movimientos}" var="movimiento">
                                <c:set var="momentos" value="${fn:split(movimiento.fechaHora, 'T')}"/>
                                <c:set var="partesFecha" value="${fn:split(momentos[0], '-')}"/>
                                <tr>
                                    <td><c:out value="${movimiento.id}"/></td>
                                    <td><span class="badge muted">Salida</span></td>
                                    <td><c:out value="${productosPorId[movimiento.idProducto].nombre}"/></td>
                                    <td><fmt:formatNumber value="${movimiento.cantidad}" type="number" maxFractionDigits="3"/> L</td>
                                    <td>
                                        <c:out value="${partesFecha[2]}/${partesFecha[1]}/${partesFecha[0]}"/>
                                        <c:out value="${fn:substring(momentos[1], 0, 5)}"/>
                                    </td>
                                    <td><c:out value="${responsables[movimiento.idUsuario]}"/></td>
                                    <td><c:out value="${movimiento.motivo}"/></td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty movimientos}">
                                <tr>
                                    <td colspan="7">No hay salidas de inventario.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>

            <p class="notice">
                RN01 · El retiro requiere disponibilidad suficiente. Una salida física no es
                necesariamente un gasto.
            </p>
        </main>
    </div>
</body>
</html>
