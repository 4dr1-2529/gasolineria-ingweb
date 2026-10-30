<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear categoría · Estación Nexo</title>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P05</p>
                    <h1>Crear categoría</h1>
                    <p class="subtitle">Registrar una nueva familia de combustibles.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/categorias/list">Volver a la lista</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <p class="notice">
                Los campos con * son obligatorios: revise los datos de la categoría antes de guardar.
            </p>

            <section class="panel">
                <h2>Datos de la categoría</h2>
                <!-- El formulario está vinculado al objeto "categoria" -->
                <form:form action="${pageContext.request.contextPath}/categorias/crear" method="post" modelAttribute="categoria">
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="nombre">Nombre</form:label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" required="required" minlength="3" maxlength="40" />
                            <p class="form-help">Ejemplos: Gasolinas, Diésel. Entre 3 y 40 caracteres, sin repetir.</p>
                        </div>
                        <div class="field">
                            <form:label path="descripcion">Descripción</form:label>
                            <form:textarea path="descripcion" id="descripcion" cssClass="form-control" rows="3" maxlength="200" />
                            <p class="form-help">Texto explicativo de la familia (opcional, hasta 200 caracteres).</p>
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
                        <button type="submit" class="btn btn-primary">Guardar</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/categorias/list">Cancelar</a>
                    </div>
                </form:form>
            </section>
        </main>
    </div>
</body>
</html>
