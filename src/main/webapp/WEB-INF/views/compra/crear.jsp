<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Formulario compra · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P23</p>
                    <h1>Formulario compra</h1>
                    <p class="subtitle">Representar el registro de una compra de combustible.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/list">← Compras</a>
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/detalle?id=C001">Ver C001</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                RN03 y RN04 · Al confirmarse, la compra suma los litros recibidos al stock y
                representa un único egreso económico por su importe. Litros y precios deben ser
                numéricos mayores que cero; los campos marcados con * son obligatorios.
            </p>

            <section class="panel">
                <h2>Registrar compra de combustible · F13</h2>
                <%-- La compra se vincula al objeto "compra"; la línea de detalle
                     (combustible, cantidad y precio) llega como parámetros simples --%>
                <form:form action="${pageContext.request.contextPath}/compras/crear" method="post" modelAttribute="compra">
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="proveedor">Proveedor *</form:label>
                            <form:input path="proveedor" id="proveedor" cssClass="form-control" type="text"
                                required="required" minlength="3" maxlength="60"/>
                        </div>
                        <div class="field">
                            <label for="fecha">Fecha *</label>
                            <%-- La hora la toma el servidor al registrar la compra --%>
                            <input class="form-control" id="fecha" name="fecha" type="date" value="${fechaCompra}" required>
                        </div>
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
                            <input class="form-control" id="cantidad" name="cantidad" type="number" value="${not empty detalle.cantidad ? detalle.cantidad : '100'}" required min="0.01" step="0.01">
                        </div>
                        <div class="field">
                            <label for="precioCompra">Precio de compra (S/ por litro) *</label>
                            <input class="form-control" id="precioCompra" name="precioCompra" type="number" value="${not empty detalle.precioCompra ? detalle.precioCompra : '4.50'}" required min="0.01" step="0.01">
                        </div>
                        <div class="field">
                            <form:label path="idUsuario">Responsable *</form:label>
                            <form:select path="idUsuario" id="idUsuario" cssClass="form-select" required="required">
                                <form:option value="" label="Seleccionar responsable"/>
                                <form:options items="${responsables}"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Registrar compra</button>
                    </div>
                    <p class="form-help">
                        La compra se guarda en memoria, suma los litros al stock del producto y
                        registra su entrada de inventario; el egreso de caja se consulta en el
                        módulo Finanzas.
                    </p>
                </form:form>
            </section>

            <section class="panel">
                <h2>Resumen del escenario</h2>
                <p>
                    Compra de referencia C001:
                    <strong>300 L</strong> ·
                    <strong>S/ 1,350.00</strong> · Petroandes S.A.
                </p>
                <p class="muted-text">
                    Proyección fija: 100 L a S/ 4.50 por litro = S/ 450.00. Al registrar, la compra
                    se guarda en memoria y suma sus litros al stock del producto seleccionado.
                </p>
            </section>

            <p class="notice">
                Este formulario comparte F13 con P20: la lista de compras y el formulario dedicado
                son dos interfaces para la misma funcionalidad.
            </p>
        </main>
    </div>
</body>
</html>
