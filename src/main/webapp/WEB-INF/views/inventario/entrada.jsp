<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%-- Los litros se muestran igual que en la versión 1: 1,990 --%>
<fmt:setLocale value="en_US"/>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Entrada combustible · Estación Nexo</title>
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
            <a href="${pageContext.request.contextPath}/inventario/list" aria-current="page">Inventario</a>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P12</p>
                    <h1>Entrada combustible</h1>
                    <p class="subtitle">Representar la entrada de combustible por compra o por ajuste.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/inventario/list">← Existencias</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                RN03 y RN04 · Una entrada originada en una compra recibe litros y genera también su egreso
                económico: así ocurrió con MI001–MI003 y MC001. Una entrada por ajuste, en cambio,
                no mueve dinero.
            </p>

            <section class="panel">
                <h2>Entrada de combustible</h2>
                <form:form modelAttribute="movimiento" action="${pageContext.request.contextPath}/inventario/entrada/crear" method="post">
                    <div class="form-grid">
                        <div class="form-field">
                            <label for="idProducto">Producto *</label>
                            <%-- RN02 · Sólo los combustibles activos admiten movimientos; la tabla inferior sí muestra todos --%>
                            <select class="form-select" id="idProducto" name="idProducto" required>
                                <c:forEach items="${productosActivos}" var="producto">
                                    <option value="${producto.id}" <c:if test="${movimiento.idProducto == producto.id}">selected</c:if>>
                                        <c:out value="${producto.nombre}"/>
                                    </option>
                                </c:forEach>
                            </select>
                        </div>
                        <div class="form-field">
                            <label for="cantidad">Cantidad en litros *</label>
                            <input class="form-control" id="cantidad" name="cantidad" type="number" value="${not empty movimiento.cantidad ? movimiento.cantidad : '500'}" required min="0.01" step="0.01">
                        </div>
                        <div class="form-field">
                            <label for="fechaHora">Fecha y hora de registro</label>
                            <%-- Sólo informativa: la fecha y la hora las fija el servidor --%>
                            <input class="form-control" id="fechaHora" type="text"
                                   value="${marcaTiempo}" readonly aria-describedby="ayuda-fecha-hora">
                            <p class="form-help" id="ayuda-fecha-hora">La registra el servidor (America/Lima); no se digita.</p>
                        </div>
                        <div class="form-field">
                            <label for="motivo">Motivo *</label>
                            <textarea class="form-control" id="motivo" name="motivo" rows="3" required minlength="5" maxlength="200"><c:choose><c:when test="${not empty movimiento.motivo}"><c:out value="${movimiento.motivo}"/></c:when><c:otherwise>Compra de combustible a proveedor</c:otherwise></c:choose></textarea>
                        </div>
                        <div class="form-field">
                            <label for="idUsuario">Responsable *</label>
                            <form:select path="idUsuario" id="idUsuario" cssClass="form-select" required="required">
                                <form:options items="${operadores}"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Registrar entrada</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/inventario/list">Cancelar</a>
                    </div>
                    <p class="form-help">
                        Al registrar, la entrada suma los litros al stock del producto
                        seleccionado, una sola vez (RN03), con la fecha y hora del servidor.
                    </p>
                </form:form>
            </section>

            <section class="panel">
                <h2>Existencias antes de registrar</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            Los litros sumados al confirmar aparecen en el libro de movimientos.
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Combustible</th>
                                <th scope="col">Existencias</th>
                                <th scope="col">Estado</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${productos}" var="producto">
                                <tr>
                                    <td><c:out value="${producto.nombre}"/></td>
                                    <td><fmt:formatNumber value="${producto.stock}" type="number" maxFractionDigits="2"/> L</td>
                                    <td><span class="badge"><c:out value="${producto.estado}"/></span></td>
                                </tr>
                            </c:forEach>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
