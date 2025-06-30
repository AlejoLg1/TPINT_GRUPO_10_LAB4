<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
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

        <table class="table table-striped table-hover tabla-cuotas" style="width:100%; margin-top: 20px;">
		    <thead class="table-light">
		        <tr>
		            <th>Préstamo N°</th>
		            <th>Cuota N°</th>
		            <th>Importe</th>
		            <th>Vencimiento</th>
		            <th>Estado</th>
		            <th>Acción</th>
		        </tr>
		    </thead>
		    <tbody>
		        <%-- debug cantidad de cuotas --%>
		        <% out.println("Total cuotas recibidas: " + (cuotasPendientes != null ? cuotasPendientes.size() : "null")); %>
		        <%
		            if (cuotasPendientes != null && !cuotasPendientes.isEmpty()) {
		                for (Cuota cuota : cuotasPendientes) {
		        %>
		            <tr>
		                <td><%= cuota.getIdPrestamo() %></td>
		                <td><%= cuota.getNumeroCuota() %></td>
		                <td>$<%= cuota.getMonto() %></td>
		                <td><%= cuota.getFechaPago() != null ? cuota.getFechaPago() : "Sin fecha" %></td>
		                <td><%= cuota.getEstado() %></td>
		                <td>
		                    <form action="ServletConfirmarPagoCuotas" method="post">
		                        <input type="hidden" name="cuotas" value="<%= cuota.getIdCuota() %>" />
		                        <button type="submit" class="btn btn-primary btn-sm">Pagar</button>
		                    </form>
		                </td>
		            </tr>
		        <%
		                }
		            } else {
		        %>
		            <tr>
		                <td colspan="6">No hay cuotas pendientes para mostrar.</td>
		            </tr>
		        <%
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