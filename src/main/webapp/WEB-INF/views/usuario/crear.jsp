<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Crear usuario · Estación Nexo</title>
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
            <a href="${pageContext.request.contextPath}/combustibles/crear">Nuevo combustible</a>
            <a href="${pageContext.request.contextPath}/empleados/list">Empleados</a>
            <a href="${pageContext.request.contextPath}/empleados/crear">Nuevo empleado</a>
            <a href="${pageContext.request.contextPath}/usuarios/list">Usuarios</a>
            <a href="${pageContext.request.contextPath}/usuarios/crear" aria-current="page">Nuevo usuario</a>
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
                    <p class="eyebrow">OPERACIÓN DIARIA / P26</p>
                    <h1>Crear usuario</h1>
                    <p class="subtitle">Representar el registro y la edición de una cuenta de acceso.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-secondary" href="${pageContext.request.contextPath}/usuarios/list">← Cuentas</a>
                </div>
            </div>

            <p class="notice">
                El usuario se guarda en memoria mientras la aplicación está en ejecución.
                No hay base de datos.
            </p>

            <section class="panel">
                <h2>Registrar usuario</h2>
                <!-- El formulario está vinculado al objeto "usuario" -->
                <form:form action="${pageContext.request.contextPath}/usuarios/crear" method="post" modelAttribute="usuario">
                    <div class="form-grid">
                        <div class="field">
                            <form:label path="idEmpleado">Empleado *</form:label>
                            <form:select path="idEmpleado" id="idEmpleado" cssClass="form-select" required="required">
                                <form:option value="" label="Seleccionar empleado sin cuenta"/>
                                <form:options items="${nombresEmpleados}"/>
                            </form:select>
                        </div>
                        <div class="field">
                            <form:label path="username">Username *</form:label>
                            <form:input path="username" id="username" cssClass="form-control" type="text" required="required" pattern="[a-z0-9._-]{4,20}" maxlength="20" title="De 4 a 20 caracteres en minúsculas, sin espacios" />
                        </div>
                        <div class="field">
                            <form:label path="password">Contraseña de demostración *</form:label>
                            <form:input path="password" id="password" cssClass="form-control" type="password" required="required" minlength="8" maxlength="40" />
                        </div>
                        <div class="field">
                            <form:label path="rol">Rol *</form:label>
                            <form:select path="rol" id="rol" cssClass="form-select" required="required">
                                <form:option value="Administrador" label="Administrador"/>
                                <form:option value="Operador / Vendedor" label="Operador / Vendedor"/>
                                <form:option value="Empleado" label="Empleado"/>
                            </form:select>
                        </div>
                        <div class="field">
                            <form:label path="estado">Estado *</form:label>
                            <form:select path="estado" id="estado" cssClass="form-select" required="required">
                                <form:option value="Activo" label="Activo"/>
                                <form:option value="Inactivo" label="Inactivo"/>
                            </form:select>
                        </div>
                    </div>
                    <div class="actions">
                        <button type="submit" class="btn btn-primary">Registrar usuario</button>
                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/usuarios/list">Cancelar</a>
                    </div>
                    <p class="form-help">
                        La contraseña no se guarda en memoria; el identificador se asigna automáticamente.
                    </p>
                </form:form>
            </section>
        </main>
    </div>
</body>
</html>
