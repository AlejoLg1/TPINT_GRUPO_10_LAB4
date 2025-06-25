<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cuenta" %>

<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    List<Cuenta> lista = (List<Cuenta>) request.getAttribute("listaCuentas");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Transferencias - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transferenciasEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "transferencias"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Transferencia entre cuentas</h1>

        <% 
            boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
            if (estado) {
        %>
            <div class="panelSuccess">
                ¡Operación realizada con éxito!
            </div>
        <% } %>

        <form action="${pageContext.request.contextPath}/ServletTransferenciasUsuario" method="post">
            <label for="cuentaOrigen">Cuenta de origen:</label><br>
            <select name="cuentaOrigen" id="cuentaOrigen" required>
                <%
                    if (lista != null && !lista.isEmpty()) {
                        for (Cuenta cuenta : lista) {
                            String nroCuenta = String.valueOf(cuenta.getNroCuenta());
                            String cbu = cuenta.getCbu();
                            String tipo = cuenta.getTipoCuenta();
                %>
                    <option value="<%= nroCuenta %>"><%= cbu %> - <%= tipo %></option>
                <%
                        }
                    } else {
                %>
                    <option disabled selected>No tiene cuentas disponibles</option>
                <%
                    }
                %>
            </select>
            <br><br>

            <label for="cbuDestino">CBU destino:</label><br>
            <input type="text" name="cbuDestino" id="cbuDestino" required><br><br>

            <label for="monto">Monto a transferir:</label><br>
            <input type="number" name="monto" id="monto" step="0.01" min="1" required><br><br>

            <input type="submit" name="btnTransferir" value="Transferir" class="action-button">
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>
