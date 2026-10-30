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
                    <input type="hidden" name="formularioToken" value="${formularioToken}">
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
                            <input class="form-control" id="cantidad" name="cantidad" type="number" value="${not empty detalle.cantidad ? detalle.cantidad : '10'}" required min="0.001" step="0.001">
                        </div>
                        <p class="form-help">Responsable: <c:out value="${pageContext.request.remoteUser}"/> (sesión actual).</p>
                    </div>
<section aria-label="Líneas adicionales"><div id="lineas-adicionales"></div><button type="button" class="btn btn-secondary" id="agregar-linea">Agregar combustible</button><template id="plantilla-linea"><fieldset class="form-grid"><legend>Combustible adicional</legend><label>Combustible<select class="form-select" name="lineas[__indice__].idProducto" required><option value="">Seleccione</option><c:forEach var="p" items="${productos}"><option value="${p.id}"><c:out value="${p.nombre}"/></option></c:forEach></select></label><label>Litros<input class="form-control" type="number" min="0.001" step="0.001" required name="lineas[__indice__].cantidad"/></label><button type="button" class="btn" data-quitar-linea>Quitar línea</button></fieldset></template></section>                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Confirmar venta</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/ventas/list">Cancelar</a>
                    </div>
                    <p class="form-help">
                        La venta se registra con estado Confirmada y con la fecha y hora del
                        servidor: el precio unitario se toma del producto seleccionado, subtotal y
                        total se calculan en el servidor y los litros se descuentan del stock. La
                        salida de inventario queda en Inventario y el ingreso de caja en Finanzas.
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
                                    <td><fmt:formatNumber value="${producto.stock}" type="number" maxFractionDigits="3"/> L</td>
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
<script src="${pageContext.request.contextPath}/js/lineas.js" defer></script>
</body>
</html>
