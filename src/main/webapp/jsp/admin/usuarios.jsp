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
</head>

<body>

<% request.setAttribute("activePage", "usuarios"); %>
<%@ include file="navbarAdmin.jsp" %>

<%
    String errorCuenta = (String) request.getAttribute("errorUsuario");
    if (errorCuenta != null) {
%>
    <div class="alert alert-danger alert-dismissible fade show m-4" role="alert">
        <%= errorCuenta %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<%
    }
%>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Listado de Usuarios</h2>
                <a href="${pageContext.request.contextPath}/jsp/admin/altaUsuario.jsp" class="btn btn-primary">+ Crear Usuario</a>
            </div>

            <!-- 🔎 Filtros -->
            <form action="${pageContext.request.contextPath}/ServletListarUsuario" method="get" class="row g-3 mb-4">
                <div class="col-md-4">
                    <input type="text" name="usuario" class="form-control" placeholder="Buscar por nombre de usuario">
                </div>
                
                <div class="col-md-3">
                    <select name="rol" class="form-select">
                        <option value="">-- Rol --</option>
                        <option value="Admin">Admin</option>
                        <option value="Usuario">Usuario</option>
                    </select>
                </div>

                <div class="col-md-3">
                    <select name="estado" class="form-select">
                        <option value="">-- Estado --</option>
                        <option value="Activo">Activo</option>
                        <option value="Inactivo">Inactivo</option>
                    </select>
                </div>

                <div class="w-100"></div> <!-- Salto de línea -->

                <div class="col-md-2">
                    <button type="submit" class="btn btn-outline-primary w-100">Filtrar</button>
                </div>
                <div class="col-md-2">
                    <a href="${pageContext.request.contextPath}/ServletListarUsuario" class="btn btn-outline-secondary w-100">Limpiar</a>
                </div>
            </form>

            <!-- 🔽 Tabla -->
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
                                <button 
                                  type="button"
                                  class="btn btn-danger btn-sm me-1"
                                  onclick="abrirConfirmacion({
                                    action: '${pageContext.request.contextPath}/ServletBajaUsuario',
                                    mensaje: '¿Estás seguro que deseas desactivar al usuario <%= u.getNombreUsuario() %>?',
                                    botonTexto: 'Desactivar',
                                    botonClase: 'btn-danger',
                                    inputs: [{ name: 'id', value: '<%= u.getIdUsuario() %>' }]
                                  })">
                                  Desactivar
                                </button>
                                <a href="${pageContext.request.contextPath}/ServletModificarUsuario?id=<%= u.getIdUsuario() %>" class="btn btn-warning btn-sm">Modificar</a>
                            <% } else { %>
                                <button 
                                  type="button"
                                  class="btn btn-success btn-sm"
                                  onclick="abrirConfirmacion({
                                    action: '${pageContext.request.contextPath}/ServletActivarUsuario',
                                    mensaje: '¿Deseas activar nuevamente al usuario <%= u.getNombreUsuario() %>?',
                                    botonTexto: 'Activar',
                                    botonClase: 'btn-success',
                                    inputs: [{ name: 'id', value: '<%= u.getIdUsuario() %>' }]
                                  })">
                                  Activar
                                </button>
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


<!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

  <!-- DataTables JS -->
  <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
  <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

  <!-- Configuración de idioma ES para DataTables -->
  <script src="${pageContext.request.contextPath}/js/datatables-es.js"></script>
  
<!-- Bootstrap Bundle JS (incluye Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

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
   
<%@ include file="../comunes/modalConfirmacion.jsp" %>
<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
