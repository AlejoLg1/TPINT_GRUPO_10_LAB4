<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8" %>
<%@ page import="java.util.List" %>
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
    <title>Solicitar Préstamo - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/solicitudPrestamoEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "prestamos"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Solicitar préstamo</h1>

		<%  // Mensaje de confirmacion de exito de solicitud
			boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
			String msg = (String)request.getAttribute("mensaje");	
		
			if(request.getAttribute("estado") != null)
			{
				if(estado == true)
				{%>
					  <p style="color: green; font-weight: bold; margin-bottom: 15px;"><%= msg %></p>
				<%}
				else{
					%> <p style="color: red; font-weight: bold; margin-bottom: 15px;"><%= msg %></p><%
				}
			}
		%>

        <form action="${pageContext.request.contextPath}/ServletSolicitarPrestamo" method="post">
            <label for="cuentasCliente">Seleccione una cuenta:</label><br>
            <select name="cuentasCliente" id="cuentasCliente" required>
                <% 
                    List<Cuenta> cuentasCliente = (List<Cuenta>) request.getAttribute("cuentasCliente");
                    if (cuentasCliente != null && !cuentasCliente.isEmpty()) {
                    	int i=1;
                        for (Cuenta cuenta : cuentasCliente) {
                %>
                    <option value="<%= cuenta.getNroCuenta() %>">
                        <%= i %>- <%= cuenta.getTipoCuenta() %> - CBU: <%= cuenta.getCbu() %>
                         
                    </option>
                <% 
                		i++; 
                        }
                    } else {
                %>
                    <option value="invalid" selected>No tiene cuentas disponibles</option>
                <% } %>
            </select><br><br>

            <label for="monto">Monto a solicitar:</label><br>
            <input type="number" name="monto" id="monto" step="1" min="1" required><br><br>

            <label for="cuotas">Seleccione la cantidad de cuotas:</label><br>
            <select name="cuotas" id="cuotas" required>
                <option value="12">12 cuotas</option>
                <option value="24">24 cuotas</option>
                <option value="36">36 cuotas</option>
                <option value="48">48 cuotas</option>
                <option value="60">60 cuotas</option>
                <option value="72">72 cuotas</option>
            </select><br><br>

            <input type="submit" name="btnSolicitar" value="Solicitar Prestamo" class="action-button">
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>