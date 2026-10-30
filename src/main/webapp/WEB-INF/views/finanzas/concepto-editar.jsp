<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Editar concepto · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P17</p>
                    <h1>Editar concepto</h1>
                    <p class="subtitle">Modificar el nombre, el tipo o el estado de un concepto económico.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/conceptos/list">Volver a la lista</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                Los campos con * son obligatorios: revise los cambios del concepto antes de guardar.
            </p>

            <section class="panel">
                <h2>Datos del concepto</h2>
                <!-- El código no se digita: viaja en el objeto vinculado -->
                <form:form action="${pageContext.request.contextPath}/finanzas/conceptos/editar" method="post" modelAttribute="concepto">
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
                            <form:label path="tipo">Tipo</form:label>
                            <form:select path="tipo" id="tipo" cssClass="form-select" required="required">
                                <form:option value="Ingreso" label="Ingreso"/>
                                <form:option value="Egreso" label="Egreso"/>
                            </form:select>
                        </div>
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
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/finanzas/conceptos/list">Cancelar</a>
                    </div>
                </form:form>
            </section>

            <p class="notice">
                RN02 · El concepto no se elimina: si tiene movimientos asociados se conserva
                con su historial y sólo puede cambiar de estado.
            </p>
        </main>
    </div>
</body>
</html>
