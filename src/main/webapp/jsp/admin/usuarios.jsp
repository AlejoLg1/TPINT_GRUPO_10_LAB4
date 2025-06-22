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

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <!-- TU CSS personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuariosEstilos.css">

    <!-- jQuery -->
    <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

    <!-- DataTables JS -->
    <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
    <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

    <!-- Configuración de idioma ES para DataTables -->
    <script src="${pageContext.request.contextPath}/js/datatables-es.js"></script>

    <script>
        $(document).ready(function () {
            $('.tabla-usuario').DataTable({
                language: spanishLanguageSettings,
                pageLength: 5,
                lengthMenu: [5, 10, 25, 50],
                responsive: true
            });
        });
    </script>
</head>

<body>

<% request.setAttribute("activePage", "usuarios"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Listado de Usuarios</h2>
                <a href="${pageContext.request.contextPath}/jsp/admin/altaUsuario.jsp" class="btn btn-primary">+ Crear Usuario</a>
            </div>

            <table class="table table-striped table-hover tabla-usuario" style="width:100%;">
                <thead class="table-light">
                    <tr>
                        <th>Usuario</th>
                        <th>Rol</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Usuario> listaUsuarios = (List<Usuario>) request.getAttribute("usuarios");
                        if (listaUsuarios != null) {
                            for (Usuario u : listaUsuarios) {
                    %>
                    <tr>
                        <td><%= u.getNombreUsuario() %></td>
                        <td><%= u.isAdmin() ? "Admin" : "Usuario" %></td>
                        <td>
                            <span class="fw-bold <%= u.isEstado() ? "text-success" : "text-secondary" %>">
                                <%= u.isEstado() ? "Activo" : "Inactivo" %>
                            </span>
                        </td>
                        <td>
                            <% if (u.isEstado()) { %>
                                <form action="${pageContext.request.contextPath}/ServletBajaUsuario" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                    <button type="submit" class="btn btn-danger btn-sm me-1">Desactivar</button>
                                </form>
                                <a href="${pageContext.request.contextPath}/ServletModificarUsuario?id=<%= u.getIdUsuario() %>" class="btn btn-warning btn-sm">Modificar</a>
                            <% } else { %>
                                <form action="${pageContext.request.contextPath}/ServletActivarUsuario" method="post" style="display:inline;">
                                    <input type="hidden" name="id" value="<%= u.getIdUsuario() %>">
                                    <button type="submit" class="btn btn-success btn-sm">Activar</button>
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
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
