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
        <h1>Movimientos de cuenta: <%= request.getAttribute("nroCuenta") %></h1>

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
                    java.util.List movimientos = (java.util.List) request.getAttribute("movimientos");
                    if (movimientos != null) {
                        for (Object obj : movimientos) {
                            dominio.Movimiento mov = (dominio.Movimiento) obj;
                            java.time.format.DateTimeFormatter formatter = java.time.format.DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                            String fechaFormateada = mov.getFecha().format(formatter);
                            String claseImporte = mov.getImporte().compareTo(java.math.BigDecimal.ZERO) >= 0 ? "positivo" : "negativo";
                %>
                <tr>
                    <td><%= fechaFormateada %></td>
                    <td><%= mov.getDetalle() %></td>
                    <td class="<%= claseImporte %>">$<%= mov.getImporte() %></td>
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

<script>
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
