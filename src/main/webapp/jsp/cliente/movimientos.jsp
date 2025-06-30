<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
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
    <title>Movimientos - Banco UTN</title>

    <!-- Bootstrap CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbarClientesEstilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movimientosEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "movimientos"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Movimientos de cuenta: <%= request.getAttribute("nroCuenta") %></h1>

      
        <table id="tablaMovimientos" class="table table-striped table-hover" style="width:100%">
            <thead class="table-light">
                <tr>
                    <th>Fecha</th>
                    <th>Descripción</th>
                    <th>Importe</th>
                </tr>
            </thead>
            <tbody>
                <%
                    java.util.List movimientos = (java.util.List) request.getAttribute("movimientos");
                    if (movimientos != null) {
                        for (Object obj : movimientos) {
                            dominio.Movimiento mov = (dominio.Movimiento) obj;
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String fechaFormateada = mov.getFecha().format(formatter);
                %>
                <tr>
                    <td><%= fechaFormateada %></td>
                    <td><%= mov.getDetalle() %></td>
                    <td class="<%= mov.getImporte().compareTo(java.math.BigDecimal.ZERO) >= 0 ? "text-success fw-bolder" : "text-danger fw-bolder" %>">$<%= mov.getImporte() %></td>
                </tr>
                <%
                        }
                    }
                %>
            </tbody>
        </table>

        <br>
        
        <div class="mb-3 text-center">
            <a href="${pageContext.request.contextPath}/ServletVerCuentas" class="btn btn-secondary">Volver</a>
        </div>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<!-- DataTables JS -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<!-- Bootstrap Bundle JS -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<!-- Inicialización DataTable -->
<script>
$(document).ready(function () {
    $('#tablaMovimientos').DataTable({
        language: {
            url: '//cdn.datatables.net/plug-ins/1.13.6/i18n/es-ES.json'
        },
        pageLength: 5,
        lengthMenu: [5, 10, 25, 50],
        order: [[0, 'desc']] // orden por fecha descendente
    });
});
</script>

</body>
</html>
