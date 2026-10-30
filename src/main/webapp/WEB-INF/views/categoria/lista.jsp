<%@ taglib uri="http://www.springframework.org/tags/form" prefix="form" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions" prefix="fn" %>
<%@ page contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="es">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <title>Categorías · Estación Nexo</title>
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
                    <h1>Categorías</h1>
                    <p class="subtitle">Organizar las familias de combustibles.</p>
                </div>
                <div class="actions">
                    <a class="btn btn-primary" href="${pageContext.request.contextPath}/categorias/crear">+ Categoría</a>
                </div>
            </div>

            <c:if test="${not empty mensaje}">
                <p class="notice ${mensajeTipo}">
                    <c:out value="${mensaje}"/>
                </p>
            </c:if>

            <section class="panel">
                <h2>Familias de combustible</h2>
                <div class="table-responsive">
                    <table class="table">
                        <caption>
                            <c:out value="${fn:length(categorias)}"/> categorías registradas
                        </caption>
                        <thead>
                            <tr>
                                <th scope="col">Id</th>
                                <th scope="col">Nombre</th>
                                <th scope="col">Descripción</th>
                                <th scope="col">Estado</th>
                                <th scope="col">Acciones</th>
                            </tr>
                        </thead>
                        <tbody>
                            <c:forEach items="${categorias}" var="categoria">
                                <tr>
                                    <td><c:out value="${categoria.id}"/></td>
                                    <td><c:out value="${categoria.nombre}"/></td>
                                    <td><c:out value="${categoria.descripcion}"/></td>
                                    <td><span class="badge${categoria.estado == 'Inactivo' ? ' muted' : ''}"><c:out value="${categoria.estado}"/></span></td>
                                    <td>
                                        <a class="btn btn-secondary" href="${pageContext.request.contextPath}/categorias/editar?id=${categoria.id}">Editar</a>
                                        <!-- RN02: se cambia el estado, nunca se elimina el registro -->
                                        <c:choose>
                                            <c:when test="${categoria.estado == 'Activo'}">
                                                <form:form action="${pageContext.request.contextPath}/categorias/estado" method="post" cssClass="inline-accion">
                                                    <input type="hidden" name="id" value="${categoria.id}"/>
                                                    <input type="hidden" name="estado" value="Inactivo"/>
                                                    <button type="submit" class="btn btn-secondary">Desactivar</button>
                                                </form:form>
                                            </c:when>
                                            <c:otherwise>
                                                <form:form action="${pageContext.request.contextPath}/categorias/estado" method="post" cssClass="inline-accion">
                                                    <input type="hidden" name="id" value="${categoria.id}"/>
                                                    <input type="hidden" name="estado" value="Activo"/>
                                                    <button type="submit" class="btn btn-primary">Activar</button>
                                                </form:form>
                                            </c:otherwise>
                                        </c:choose>
                                    </td>
                                </tr>
                            </c:forEach>
                            <c:if test="${empty categorias}">
                                <tr>
                                    <td colspan="5">No hay categorías registradas.</td>
                                </tr>
                            </c:if>
                        </tbody>
                    </table>
                </div>
            </section>
        </main>
    </div>
</body>
</html>
