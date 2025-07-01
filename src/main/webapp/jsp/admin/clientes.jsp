<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cliente" %>
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
    <title>Clientes - Banco UTN</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <!-- TU CSS personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clientesEstilos.css">
</head>

<body>

<% request.setAttribute("activePage", "clientes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Listado de Clientes</h2>
                <a href="${pageContext.request.contextPath}/ServletAltaCliente" class="btn btn-primary">+ Crear Cliente</a>
            </div>

            <table class="table table-striped table-hover tabla-cliente" style="width:100%;">
                <thead class="table-light">
                    <tr>
                        <th>Nombre</th>
                        <th>Apellido</th>
                        <th>Dni</th>
                        <th>Cuil</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <%
                        List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("clientes"); 
                        if (listaClientes != null) {
                            for (Cliente c : listaClientes) {
                    %>
                    <tr>
                        <td><%= c.getNombre() %></td>
                        <td><%= c.getApellido() %></td>
                        <td><%= c.getDni() %></td>
                        <td><%= c.getCuil() %></td>
                        <td>
                            <span class="fw-bold <%= c.isEstado() ? "text-success" : "text-secondary" %>">
                                <%= c.isEstado() ? "Activo" : "Inactivo" %>
                            </span>
                        </td>
                        <td>
                            <% if (c.isEstado()) { %>
                                <button 
								  type="button"
								  class="btn btn-danger btn-sm me-1"
								  onclick="abrirConfirmacion({
								    action: '${pageContext.request.contextPath}/ServletBajaCliente',
								    mensaje: '¿Estás seguro que deseas desactivar al cliente <%= c.getNombre() %>?',
								    botonTexto: 'Desactivar',
								    botonClase: 'btn-danger',
								   inputs: [
    								{ name: 'id', value: '<%= c.getIdCliente() %>' },
									]
								    
								  })">
								  Desactivar
								</button>
                              <a href="<%= request.getContextPath() %>/ServletModificarCliente?id=<%= c.getIdCliente() %>" class="btn btn-warning btn-sm">Modificar</a>

                            <% } else { %>
                                <button 
								  type="button"
								  class="btn btn-success btn-sm"
								  onclick="abrirConfirmacion({
								    action: '${pageContext.request.contextPath}/ServletActivarCliente',
								    mensaje: '¿Deseas activar nuevamente al cliente <%= c.getNombre() %>?',
								    botonTexto: 'Activar',
								    botonClase: 'btn-success',
								   inputs: [
    								{ name: 'id', value: '<%= c.getIdCliente() %>' },
									]
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
          $('.tabla-cliente').DataTable({
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
