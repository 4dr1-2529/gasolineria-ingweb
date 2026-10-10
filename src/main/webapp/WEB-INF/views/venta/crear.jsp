<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los importes se muestran igual que en la versión 1: 50.00 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Formulario venta · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P24</p>
                    <h1>Formulario venta</h1>
                    <p class="subtitle">Representar el registro de una venta de combustible.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ventas/list">← Ventas</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Consultar disponibilidad</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                RN01, RN02, RN03 y RN04 · Spring Boot comprueba existencias, producto activo,
                importes positivos y el ingreso único de la venta. Los campos marcados con * son
                obligatorios.
            </p>

            <section class="panel">
                <h2>Nueva venta · F20</h2>
                <%-- La venta se vincula al objeto "venta"; la línea de detalle
                     (combustible y cantidad) llega como parámetros simples.
                     El precio unitario lo toma el servidor del producto elegido --%>
                <form:form action="${pageContext.request.contextPath}/ventas/crear" method="post" modelAttribute="venta">
                    <div class="form-grid">
                        <div class="field">
                            <label for="idProducto">Combustible *</label>
                            <%-- RN02 · Sólo aparecen los productos con estado Activo --%>
                            <select class="form-select" id="idProducto" name="idProducto" required>
                                <c:forEach items="${productos}" var="producto">
                                    <option value="${producto.id}" <c:if test="${detalle.idProducto == producto.id}">selected</c:if>><c:out value="${producto.nombre}"/></option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="field">
                            <label for="cantidad">Cantidad (L) *</label>
                            <input class="form-control" id="cantidad" name="cantidad" type="number" value="${not empty detalle.cantidad ? detalle.cantidad : '10'}" required min="0.01" step="0.01">
                        </div>
                        <div class="field">
                            <form:label path="idUsuario">Operador *</form:label>
                            <form:select path="idUsuario" id="idUsuario" cssClass="form-select" required="required">
                                <form:options items="${operadores}"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Confirmar venta</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ventas/list">Cancelar</a>
                    </div>
                    <p class="form-help">
                        La venta se guarda en memoria con estado Confirmada: el precio unitario se toma
                        del producto seleccionado, subtotal y total se calculan en el servidor y los
                        litros se descuentan del stock. La salida de inventario queda registrada en
                        el módulo Inventario y el ingreso de caja se consulta en el módulo Finanzas.
                    </p>
                </form:form>
            </section>

            <section class="panel">
                <h2>Disponibilidad antes de confirmar</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Existencias antes de confirmar · No forma parte de los totales de venta
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Combustible</th>
                                <th scope="col">Disponible antes</th>
                                <th scope="col">Precio / L</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${productos}" var="producto">
                                <tr>
                                    <td><c:out value="${producto.nombre}"/></td>
                                    <td><fmt:formatNumber value="${producto.stock}" type="number" maxFractionDigits="2"/> L</td>
                                    <td>S/ <fmt:formatNumber value="${producto.precioActual}" type="number" minFractionDigits="2"/></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
                <div class="flow">
                    <span>01 · Venta</span>
                    →
                    <span>02 · Detalle</span>
                    →
                    <span>03 · Salida de 10 L</span>
                    →
                    <span>04 · Ingreso de S/ 50.00</span>
                </div>
            </section>

            <p class="notice">
                Este formulario comparte F20 con P08: la interfaz de captura de la versión 1 y este
                formulario dedicado representan la misma funcionalidad.
            </p>
        </main>
    </div>
</body>
</html>
