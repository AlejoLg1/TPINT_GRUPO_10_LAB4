<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Usuario" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Usuarios - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuariosEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "usuarios"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Listado de Usuarios</h1>

        <div class="acciones-superiores">
            <a href="${pageContext.request.contextPath}/jsp/admin/altaUsuario.jsp" class="crear-usuario-button">+ Crear Usuario</a>
        </div>

        <table class="tabla-usuario">
            <thead>
                <tr>
                    <th>Usuario</th>
                    <th>Rol</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <%
                    List<dominio.Usuario> listaUsuarios = (List<dominio.Usuario>) request.getAttribute("usuarios");
                    if (listaUsuarios != null) {
                        for (dominio.Usuario u : listaUsuarios) {
                %>
                <tr>
                    <td><%= u.getNombreUsuario() %></td>
                    <td><%= u.isAdmin() ? "Admin" : "Usuario" %></td>
                    <td>
					    <span class="<%= u.isEstado() ? "estado-activo" : "estado-inactivo" %>">
					        <%= u.isEstado() ? "Activo" : "Inactivo" %>
					    </span>
					</td>

                    <td>
                        <% if (u.isEstado()) { %>
                            <form action="${pageContext.request.contextPath}/ServletBajaUsuario" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <button type="submit" class="boton-accion boton-eliminar">Desactivar</button>
                            </form>
                            <a href="modificarUsuario.jsp?id=<%= u.getIdUsuario() %>" class="boton-accion boton-modificar">Modificar</a>
                        <% } else { %>
                            <form action="${pageContext.request.contextPath}/ServletActivarUsuario" method="post" style="display:inline;">
                                <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                <button type="submit" class="boton-accion boton-activar">Activar</button>
                            </form>
                        <% } %>
                    </td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>
