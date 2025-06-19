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
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/navbarClientesEstilos.css">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/movimientosEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "movimientos"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Movimientos de cuenta</h1>

        <!-- Filtro -->
        <input type="text" id="filtroInput" placeholder="Filtrar movimientos...">

        <table class="tabla-movimientos">
            <thead>
                <tr>
                    <th>Fecha</th>
                    <th>Descripción</th>
                    <th>Importe</th>
                </tr>
            </thead>
            <tbody id="tablaBody">
                <%
                    String[][] movimientos = {
                        {"2025-06-01", "Transferencia recibida", "5000"},
                        {"2025-06-03", "Compra supermercado", "-3200"},
                        {"2025-06-05", "Pago préstamo", "-1500"},
                        {"2025-06-06", "Depósito", "4000"}
                    };

                    for (int i = 0; i < movimientos.length; i++) {
                        String fecha = movimientos[i][0];
                        String descripcion = movimientos[i][1];
                        double importe = Double.parseDouble(movimientos[i][2]);
                %>
                <tr>
                    <td><%= fecha %></td>
                    <td><%= descripcion %></td>
                    <td class="<%= (importe >= 0) ? "positivo" : "negativo" %>">
                        $<%= importe %>
                    </td>
                </tr>
                <% } %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

<script>
// Filtro de tabla
document.getElementById('filtroInput').addEventListener('keyup', function() {
    let filtro = this.value.toLowerCase();
    let filas = document.querySelectorAll('#tablaBody tr');

    filas.forEach(function(fila) {
        let texto = fila.textContent.toLowerCase();
        fila.style.display = texto.includes(filtro) ? '' : 'none';
    });
});
</script>

</body>
</html>