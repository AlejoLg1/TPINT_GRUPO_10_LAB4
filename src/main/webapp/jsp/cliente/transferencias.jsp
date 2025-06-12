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
    <title>Transferencias - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/transferenciasEstilos.css">
</head>
<body>

<!--  incluye navbar y envia un request con la pagina actual-->
<% request.setAttribute("activePage", "transferencias"); %>
<%@ include file="navbarClientes.jsp" %>


<div class="main-container">
    <div class="welcome-card">
        <h1>Transferencia entre cuentas</h1>

        <%-- FORMULARIO DE TRANSFERENCIA DE EJEMPLO --%>
        <form action="TransferenciaServlet" method="post">
            <label for="cuentaOrigen">Cuenta de origen:</label><br>
            <select name="cuentaOrigen" id="cuentaOrigen" required>
                <option value="12345678">12345678 - Caja de Ahorro</option>
                <option value="87654321">87654321 - Cuenta Corriente</option>     <%-- Valores ficticios por ahora, más adelante se cargarán dinámicamente desde la base de datos --%>
            </select><br><br>

            <label for="cbuDestino">CBU destino:</label><br>
            <input type="text" name="cbuDestino" id="cbuDestino" required><br><br>

            <label for="monto">Monto a transferir:</label><br>
            <input type="number" name="monto" id="monto" step="0.01" min="1" required><br><br>

            <input type="submit" value="Transferir" class="action-button">
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>