<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.util.ArrayList" %>
<%@ page import="dominio.Cuota" %>
<%@ page session="true" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    List<Cuota> cuotasPendientes = (List<Cuota>) request.getAttribute("cuotasPendientes");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pagar Cuotas - Banco UTN</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <!-- CSS personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagarCuotasEstilos.css">
</head>

<body>

<% request.setAttribute("activePage", "pagoCuotas"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Pagar cuotas</h1>
        <p>Las siguientes cuotas están pendientes de pago:</p>
		
		<form action="ServletPagoCuotas" method="get" class="row g-3 mb-4">
		    <div class="col-md-3">
		        <select name="idPrestamo" class="form-select">
		            <option value="">Nº de Préstamo</option>
		            <% 
		                // Generar dinámicamente las opciones de préstamos disponibles
		                if (cuotasPendientes != null) {
		                    List<Integer> prestamosAgregados = new ArrayList<>();
		                    for (Cuota c : cuotasPendientes) {
		                        if (!prestamosAgregados.contains(c.getIdPrestamo())) {
		                            prestamosAgregados.add(c.getIdPrestamo());
		            %>
		                <option value="<%= c.getIdPrestamo() %>"><%= c.getIdPrestamo() %></option>
		            <%
		                        }
		                    }
		                }
		            %>
		        </select>
		    </div>
		

		
		    <div class="col-md-3">
		        <select name="estado" class="form-select">
		            <option value="">-- Estado --</option>
		            <option value="PENDIENTE">Pendiente</option>
		            <option value="PAGADO">Pagado</option>
		        </select>
		    </div>
			
		    <div class="w-100"></div>
			
			<div class="col-md-3">
				<label>Fecha de vencimiento: </label>
		       <input type="date" name="fechaVencimiento" class="form-control" placeholder="Fecha de vencimiento">
		    </div>
		    
		    <div class="w-100"></div>
		
		    <div class="col-md-2">
		        <button type="submit" class="btn btn-outline-primary w-100">Filtrar</button>
		    </div>
		    <div class="col-md-2">
		        <a href="ServletPagoCuotas" class="btn btn-outline-secondary w-100">Limpiar</a>
		    </div>
		</form>
		
        <table class="table table-striped table-hover tabla-cuotas" style="width:100%; margin-top: 20px;">
		    <thead class="table-light">
		        <tr>
		            <th>Préstamo N°</th>
		            <th>Cuota N°</th>
		            <th>Importe</th>
		            <th>Vencimiento</th>
		            <th>Pago</th>
		            <th>Estado</th>
		            <th>Acción</th>
		        </tr>
		    </thead>
		    <tbody>
			    <% out.println("Total cuotas recibidas: " + (cuotasPendientes != null ? cuotasPendientes.size() : "null")); %>
			    <%
			        if (cuotasPendientes != null && !cuotasPendientes.isEmpty()) {
			            for (Cuota cuota : cuotasPendientes) {
			    %>
			        <tr>
			            <td><%= cuota.getIdPrestamo() %></td>
			            <td><%= cuota.getNumeroCuota() %></td>
			            <td>$<%= cuota.getMonto() %></td>
			            <td><%= cuota.getFechaVencimiento() %></td>
			            <td><%= cuota.getFechaPago() != null ? cuota.getFechaPago() : "Sin fecha" %></td>
			            <td><%= cuota.getEstado() %></td>
			            <td>
			                <% if ("PAGADO".equalsIgnoreCase(cuota.getEstado())) { %>
			                    -
			                <% } else { %>
			                    <form action="ServletConfirmarPagoCuotas" method="post">
			                        <input type="hidden" name="cuotas" value="<%= cuota.getIdCuota() %>" />
			                        <button type="submit" class="btn btn-primary btn-sm">Pagar</button>
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

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<!-- Configuración idioma español (si tienes datatables-es.js en tu proyecto) -->
<script src="${pageContext.request.contextPath}/js/datatables-es.js"></script>

<!-- Bootstrap Bundle JS (incluye Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    $(document).ready(function () {
        $('.tabla-cuotas').DataTable({
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