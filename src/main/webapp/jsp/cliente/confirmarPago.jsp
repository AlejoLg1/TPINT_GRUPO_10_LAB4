<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dominio.Cuota, dominio.Cuenta" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<Cuota> cuotas = (List<Cuota>) request.getAttribute("cuotasSeleccionadas");
    List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentasDisponibles");
    String mensaje = (String) request.getAttribute("mensaje");
%>
<!DOCTYPE html>
<html>
<head>
    <title>Confirmar Pago de Cuotas</title>
    <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/confirmarPagoEstilos.css">
</head>
<body>

<% if (mensaje != null) { %>
    <div id="toast" class="toast show"><%= mensaje %></div>
<% } %>

<h2>Confirmar Pago de Cuotas</h2>

<form action="ServletPagoCuotas" method="post">
    <table>
        <tr>
            <th>Préstamo N°</th>
            <th>Cuota N°</th>
            <th>Importe</th>
            <th>Estado</th>
        </tr>
        <%
            BigDecimal total = new BigDecimal(0);
            for (Cuota c : cuotas) {
                if (c.getMonto() != null) {
                    total = total.add(c.getMonto());
        %>
        <tr>
            <td><%= c.getIdPrestamo() %></td>
            <td><%= c.getNumeroCuota() %></td>
            <td>$<%= c.getMonto() %></td>
            <td><%= c.getEstado() %></td>
        </tr>
        <input type="hidden" name="cuotas" value="<%= c.getIdCuota() %>">
        <%      }
            } %>
    </table>

    <p><strong>Total a pagar:</strong> $<%= total %></p>

    <label for="cuentaSeleccionada">Seleccionar cuenta:</label>
    <select name="nroCuenta" required>
        <% for (Cuenta cuenta : cuentas) { %>
            <option value="<%= cuenta.getNroCuenta() %>">
                Cuenta N° <%= cuenta.getNroCuenta() %> - Saldo: $<%= cuenta.getSaldo() %>
            </option>
        <% } %>
    </select>

    <br><br>
    <button type="submit">Confirmar Pago</button>
    <a href="ServletPagoCuotas" class="volver-btn">Volver</a>
</form>

<script>
    // Desaparece el toast automáticamente después de 4 segundos
    window.onload = function () {
        const toast = document.getElementById('toast');
        if (toast) {
            setTimeout(() => toast.classList.remove('show'), 4000);
        }
    };
</script>

</body>
</html>