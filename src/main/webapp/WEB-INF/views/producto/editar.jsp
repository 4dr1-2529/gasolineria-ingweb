<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar combustible · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P07</p>
                    <h1>Editar combustible</h1>
                    <p class="subtitle">Modificar un combustible del catálogo.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Volver a la lista</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                Los campos con * son obligatorios: revise los cambios del combustible antes de guardar.
            </p>

            <section class="panel">
                <h2>Datos del combustible</h2>
                <!-- El código no se digita: viaja en el objeto vinculado -->
                <form:form action="${pageContext.request.contextPath}/combustibles/editar" method="post" modelAttribute="producto">
                    <form:hidden path="id"/>
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="id">Código</form:label>
                            <form:input path="id" id="id" cssClass="form-control" readonly="true" />
                            <p class="form-help">El código se asigna al registrar y no cambia.</p>
                        </div>
                        <div class="field">
                            <form:label path="nombre">Nombre</form:label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" type="text" required="required" minlength="3" maxlength="40" />
                            <p class="form-help">Entre 3 y 40 caracteres, sin repetir.</p>
                        </div>
                        <div class="field">
                            <form:label path="idCategoria">Categoría</form:label>
                            <form:select path="idCategoria" id="idCategoria" cssClass="form-select" required="required">
                                <form:options items="${categorias}" itemValue="id" itemLabel="nombre"/>
                            </form:select>
                            <p class="form-help">Se muestran todas las familias para conservar la actual.</p>
                        </div>
                        <div class="field">
                            <form:label path="unidadMedida">Unidad</form:label>
                            <form:select path="unidadMedida" id="unidadMedida" cssClass="form-select" required="required">
                                <form:option value="L" label="Litro (L)"/>
                            </form:select>
                        </div>
                        <div class="field">
                            <form:label path="precioActual">Precio actual (S/ por L)</form:label>
                            <form:input path="precioActual" id="precioActual" cssClass="form-control" type="number" min="0.01" step="0.01" required="required" />
                        </div>
                        <p class="form-help">Las existencias se modifican mediante compras, ventas o ajustes de inventario.</p>
                        <div class="field">
                            <form:label path="estado">Estado</form:label>
                            <form:select path="estado" id="estado" cssClass="form-select" required="required">
                                <form:option value="Activo" label="Activo"/>
                                <form:option value="Inactivo" label="Inactivo"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Guardar cambios</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Cancelar</a>
                    </div>
                </form:form>
            </section>
        </main>
    </div>
</body>
</html>
