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
                    <p class="eyebrow">OPERACIÓN DIARIA / P23</p>
                    <h1>Formulario compra</h1>
                    <p class="subtitle">Representar el registro de una compra de combustible.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/list">← Compras</a>
                    <c:if test="${demostracion}"><a class="btn btn-secondary" href="${pageContext.request.contextPath}/compras/detalle?id=C001">Ver C001</a></c:if>
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
                    <input type="hidden" name="formularioToken" value="${formularioToken}">
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="idProveedor">Proveedor activo *</form:label><form:select path="idProveedor" required="required" cssClass="form-select"><form:option value="" label="Seleccione proveedor"/><form:options items="${proveedores}" itemValue="id" itemLabel="razonSocial"/></form:select><c:if test="${empty proveedores}"><p class="notice">Registra o activa un proveedor antes de comprar.</p></c:if>
                        </div>
                        <div class="field">
                            <label for="fechaRegistro">Fecha de registro</label>
                            <%-- Sólo informativa: la fecha y la hora de la compra las fija el servidor --%>
                            <input class="form-control" id="fechaRegistro" type="text"
                                   value="${fechaRegistro}" readonly aria-describedby="ayuda-fecha">
                            <p class="form-help" id="ayuda-fecha">La registra el servidor (America/Lima); no se digita.</p>
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
                            <input class="form-control" id="cantidad" name="cantidad" type="number" value="${not empty detalle.cantidad ? detalle.cantidad : '100'}" required min="0.001" step="0.001">
                        </div>
                        <div class="field">
                            <label for="precioCompra">Precio de compra (S/ por litro) *</label>
                            <input class="form-control" id="precioCompra" name="precioCompra" type="number" value="${not empty detalle.precioCompra ? detalle.precioCompra : '4.50'}" required min="0.01" step="0.01">
                        </div>
                        <p class="form-help">Responsable: <c:out value="${pageContext.request.remoteUser}"/> (sesión actual).</p>
                    </div>
<section aria-label="Líneas adicionales"><div id="lineas-adicionales"></div><button type="button" class="btn btn-secondary" id="agregar-linea">Agregar combustible</button><template id="plantilla-linea"><fieldset class="form-grid"><legend>Combustible adicional</legend><label>Combustible<select class="form-select" name="lineas[__indice__].idProducto" required><option value="">Seleccione</option><c:forEach var="p" items="${productos}"><option value="${p.id}"><c:out value="${p.nombre}"/></option></c:forEach></select></label><label>Litros<input class="form-control" type="number" min="0.001" step="0.001" required name="lineas[__indice__].cantidad"/></label><label>Precio por litro<input class="form-control" type="number" min="0.01" step="0.01" required name="lineas[__indice__].precioCompra"/></label><button type="button" class="btn" data-quitar-linea>Quitar línea</button></fieldset></template></section>                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Registrar compra</button>
                    </div>
                    <p class="form-help">
                        Al registrar, la compra toma la fecha y hora del servidor, suma los litros
                        al stock del producto y crea su entrada de inventario; el egreso de caja
                        se consulta en el módulo Finanzas.
                    </p>
                </form:form>
            </section>

            <c:if test="${demostracion}"><section class="panel">
                <h2>Resumen del escenario</h2>
                <p>
                    Compra de referencia C001:
                    <strong>300 L</strong> ·
                    <strong>S/ 1,350.00</strong> · Petroandes S.A.
                </p>
                <p class="muted-text">
                    Proyección fija: 100 L a S/ 4.50 por litro = S/ 450.00. Al registrar, la compra
                    se registra con la fecha del servidor y suma sus litros al stock del producto
                    seleccionado.
                </p>
            </section></c:if>

            <p class="notice">
                Este formulario comparte F13 con P20: la lista de compras y el formulario dedicado
                son dos interfaces para la misma funcionalidad.
            </p>
        </main>
    </div>
<script src="${pageContext.request.contextPath}/js/lineas.js" defer></script>
</body>
</html>
