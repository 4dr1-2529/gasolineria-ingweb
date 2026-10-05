<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear combustible · Estación Nexo</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/estilos.css">
</head>
<body>
    <header class="sidebar">
        <a class="brand" href="${pageContext.request.contextPath}/categorias/list">
            <span class="brand-mark">N</span>
            <span>NEXO<small>ESTACIÓN NEXO</small></span>
        </a>
        <nav>
            <a href="${pageContext.request.contextPath}/categorias/list">Categorías</a>
            <a href="${pageContext.request.contextPath}/categorias/crear">Nueva categoría</a>
            <a href="${pageContext.request.contextPath}/combustibles/list">Combustibles</a>
            <a href="${pageContext.request.contextPath}/combustibles/crear" aria-current="page">Nuevo combustible</a>
            <a href="${pageContext.request.contextPath}/empleados/list">Empleados</a>
            <a href="${pageContext.request.contextPath}/empleados/crear">Nuevo empleado</a>
            <a href="${pageContext.request.contextPath}/usuarios/list">Usuarios</a>
            <a href="${pageContext.request.contextPath}/usuarios/crear">Nuevo usuario</a>
            <a href="${pageContext.request.contextPath}/asistencia/mi">Asistencia</a>
            <a href="${pageContext.request.contextPath}/compras/list">Compras</a>
            <a href="${pageContext.request.contextPath}/inventario/list">Inventario</a>
            <a href="${pageContext.request.contextPath}/ventas/list">Ventas</a>
            <a href="${pageContext.request.contextPath}/finanzas/list">Finanzas</a>
        </nav>
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
                    <h1>Crear combustible</h1>
                    <p class="subtitle">Representar el registro y la edición de combustible.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">← Catálogo</a>
                </div>
            </div>

            <p class="notice">
                El combustible se guarda en memoria mientras la aplicación está en ejecución.
                No hay base de datos.
            </p>

            <section class="panel">
                <h2>Registrar combustible</h2>
                <!-- El formulario está vinculado al objeto "producto" -->
                <form:form action="${pageContext.request.contextPath}/combustibles/crear" method="post" modelAttribute="producto">
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="nombre">Nombre</form:label>
                            <form:input path="nombre" id="nombre" cssClass="form-control" type="text" required="required" minlength="3" maxlength="40" />
                        </div>
                        <div class="field">
                            <form:label path="idCategoria">Categoría</form:label>
                            <form:select path="idCategoria" id="idCategoria" cssClass="form-select" required="required">
                                <form:options items="${categorias}" itemValue="id" itemLabel="nombre"/>
                            </form:select>
                        </div>
                        <div class="field">
                            <form:label path="unidadMedida">Unidad</form:label>
                            <form:select path="unidadMedida" id="unidadMedida" cssClass="form-select" required="required">
                                <form:option value="Litro" label="Litro"/>
                            </form:select>
                        </div>
                        <div class="field">
                            <form:label path="precioActual">Precio actual (S/ por L)</form:label>
                            <form:input path="precioActual" id="precioActual" cssClass="form-control" type="number" min="0.01" step="0.01" required="required" />
                        </div>
                        <div class="field">
                            <form:label path="stock">Stock de referencia (L)</form:label>
                            <form:input path="stock" id="stock" cssClass="form-control" type="number" min="0.01" step="0.01" required="required" />
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
                        <button type="submit" class="btn btn-primary">Registrar combustible</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/combustibles/list">Cancelar</a>
                    </div>
                    <p class="form-help">
                        El código PRnn se asigna automáticamente al registrar.
                    </p>
                </form:form>
            </section>
        </main>
    </div>
</body>
</html>
