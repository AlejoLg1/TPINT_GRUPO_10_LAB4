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

<%
	List<Cuenta> lista = (List<Cuenta>)request.getAttribute("listaCuentas");
	String msg = (String)request.getAttribute("mensaje");
%>


<div class="main-container">
    <div class="welcome-card">
        <h1>Transferencia entre cuentas</h1>
			
		<%  // Mensaje de confirmacion de exito de solicitud
			boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
			
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
		

        <%-- FORMULARIO DE TRANSFERENCIA DE EJEMPLO --%>
        <form action="${pageContext.request.contextPath}/ServletTransferenciasUsuario" method="post">
            <label for="cuentaOrigen">Cuenta de origen:</label><br>
			<select name="cuentaOrigen" id="cuentaOrigen" required>
			    <%
			        if (lista != null && !lista.isEmpty()) 
			        {
			            for (Cuenta cuenta : lista) {
			                String nroCuenta = String.valueOf(cuenta.getNroCuenta());	// nro cuenta
			                String cbu = String.valueOf(cuenta.getCbu());			//cbu
			                String tipo = String.valueOf(cuenta.getIdTipoCuenta());		//tipo de cuenta
    			%>
		        			<option value="<%= nroCuenta %>"><%=cbu%> - <%=tipo%></option>
    			<%
			            }
			        } 
			        else{ %> <option disabled selected>No tiene cuentas disponibles</option> <% } %>
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