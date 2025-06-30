<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Prestamo" %>
<%@ page import="dominio.Cliente" %>
<%@ page import="dominio.Cuenta" %>
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
    <title>Prestamos - Banco UTN</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aprobacionPrestamosEstilos.css">
</head>

<body>
	<% request.setAttribute("activePage", "prestamos"); %>
	<%@ include file="navbarAdmin.jsp" %>

	<div class="main-container">
        <div class="welcome-card">
            <h1>Aprobación de Prestamos</h1>

            <form action="ServletAprobacionPrestamos" method="get" class="filtros-form">
			    <input type="text" name="busqueda" placeholder="Buscar CBU">
			
			    <input type="number" name="montoMin" step="0.01" placeholder="Monto desde">
			
			    <input type="number" name="montoMax" step="0.01" placeholder="Monto hasta">
			    <br>
			
			    <select name="estadoPrestamo">
			        <option value="">-- Estado --</option>
			        <option value="Aprobado">Aprobado</option>
			        <option value="Pendiente">Pendiente</option>  
			        <option value="Rechazado">Rechazado</option>            
			    </select>
			    
                <br>
                <br>
                
                <div style="display: block; margin-top: 10px;">
				    <label>Fecha de solicitud: </label>
				    <input type="date" name="fechaSolicitud">
				</div>

                <br>
                
                <div class="col-md-2">
                    <button type="submit" class="btn btn-outline-primary w-100">Filtrar</button>
                </div>
                <div class="col-md-2">
				    <a href="ServletAprobacionPrestamos" class="btn btn-outline-secondary w-100">Limpiar</a>
				</div>
			</form>

            <!-- Tabla de prestamos -->
	            <table class="tabla-prestamos">
	                <thead>
					    <tr>
					        <th>Cliente</th>
					        <th>Cuenta Destino</th>
					        <th>Tipo de cuenta</th>
					        <th>Fecha de solicitud</th>
					        <th>Monto</th>                    
					        <th>Cantidad de cuotas</th>
					        <th>Cuotas Pagadas</th> <!-- Nueva columna -->
					        <th>Estado</th>
					        <th>Acciones</th>
					    </tr>
					</thead>
					<tbody>
					    <%
					        List<Prestamo> listaPrestamos = (List<Prestamo>) request.getAttribute("prestamos"); 
					        if (listaPrestamos != null) {
					            for (Prestamo p : listaPrestamos) {
					    %>
					    <tr>
					        <td><%= p.get_cliente().getNombreCompleto() %></td>
					        <td><%= p.get_cuenta().getNroCuenta()%></td>
					        <td><%= p.get_cuenta().getTipoCuenta()%></td>
					        <td><%= p.getFecha()%></td>
					        <td><%= p.getImporte_solicitado()%></td>
					        <td><%= p.getCantidad_cuotas()%></td>
					        <td><%= p.getCuotas_pagadas()%></td> <!-- Nueva celda -->
					        <td><%= p.getEstado() %></td>
					        <td>
					           <div class="acciones-botones">                                    
					                <% if (p.getEstado() != null && p.getEstado().equalsIgnoreCase("Pendiente")) { %>
					                    <button 
                                            type="button" 
                                            class="boton-aprobar" 
                                            onclick="abrirConfirmacion({
                                                action: '${pageContext.request.contextPath}/ServletAprobacionPrestamos',
                                                mensaje: '¿Seguro que desea aprobar este préstamo?',
                                                botonTexto: 'Aprobar',
                                                botonClase: 'btn-success',
                                                inputs: [
                                                    { name: 'idPrestamo', value: '<%= p.getId_prestamo() %>' },
                                                    { name: 'accion', value: 'aprobar' }
                                                ]
                                            })">Aprobar</button>
                                          <button 
                                            type="button" 
                                            class="boton-rechazar" 
                                            onclick="abrirConfirmacion({
                                                action: '${pageContext.request.contextPath}/ServletAprobacionPrestamos',
                                                mensaje: '¿Seguro que desea rechazar este préstamo?',
                                                botonTexto: 'Rechazar',
                                                botonClase: 'btn-danger', 
                                                inputs: [
                                                    { name: 'idPrestamo', value: '<%= p.getId_prestamo() %>' },
                                                    { name: 'accion', value: 'rechazar' }
                                                ]
                                            })">Rechazar</button>
					                <% } else { %>
					                    <span>-</span> 
					                <% } %>
					            </div>
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
    
 <!-- HTML del Modal de Mensajes de Estado (Éxito/Error) -->
    <div class="modal fade" id="statusModal" tabindex="-1" aria-labelledby="statusModalLabel" aria-hidden="true">
        <div class="modal-dialog modal-dialog-centered">
            <div class="modal-content">
                <div class="modal-header">
                    <h5 class="modal-title" id="statusModalLabel"></h5>
                    <button type="button" class="btn-close" data-bs-dismiss="modal" aria-label="Cerrar"></button>
                </div>
                <div class="modal-body" id="statusModalBody"></div>
                <div class="modal-footer">
                    <button type="button" class="btn btn-primary" data-bs-dismiss="modal">Aceptar</button>
                </div>
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

<style>
        /* Estilos para el modal de estado*/
        .modal-body.success {
            color: #155724;
            background-color: #d4edda;
            border-color: #c3e6cb;
            padding: 1rem;
            border-radius: .25rem;
        }
        .modal-body.error {
            color: #721c24;
            background-color: #f8d7da;
            border-color: #f5c6cb;
            padding: 1rem;
            border-radius: .25rem;
        }
        .modal-title.success-title {
            color: #28a745;
        }
        .modal-title.error-title {
            color: #dc3545;
        }
</style>
  <script>
      $(document).ready(function () {
          $('.tabla-prestamos').DataTable({
              language: spanishLanguageSettings,
              pageLength: 5,
              lengthMenu: [5, 10, 25, 50],
              responsive: true
          });
          
          function mostrarStatusModal(title, message, type) {
              const statusModal = new bootstrap.Modal(document.getElementById('statusModal'));
              const statusModalLabel = document.getElementById('statusModalLabel');
              const statusModalBody = document.getElementById('statusModalBody');

              statusModalLabel.textContent = title;
              statusModalBody.textContent = message;

              // Limpiar clases de estado anteriores en el body y título del modal
              statusModalLabel.classList.remove('success-title', 'error-title');
              statusModalBody.classList.remove('success', 'error');

              // Añadir clases según el tipo de mensaje
              if (type === 'success') {
                  statusModalLabel.classList.add('success-title');
                  statusModalBody.classList.add('success');
              } else if (type === 'error') {
                  statusModalLabel.classList.add('error-title');
                  statusModalBody.classList.add('error');
              }

              statusModal.show();
          }

          // Detectar parámetros de URL al cargar la página para mostrar el modal de estado
          const urlParams = new URLSearchParams(window.location.search);
          const status = urlParams.get('status');
          const message = urlParams.get('message');

          if (status && message) {
              let title = '';
              if (status === 'success') {
                  title = 'Operación Exitosa';
              } else if (status === 'error') {
                  title = 'Error en la Operación';
              }
              mostrarStatusModal(title, decodeURIComponent(message), status);

              //Limpiar los parámetros de la URL para que el modal no aparezca de nuevo al refrescar
              history.replaceState(null, '', window.location.pathname);
          }
      });
  </script>
	<%@ include file="../comunes/modalConfirmacion.jsp" %>
	<%@ include file="../comunes/footer.jsp" %>
</body>
</html>