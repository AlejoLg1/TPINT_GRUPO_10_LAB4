<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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

<!--  incluye navbar y envia un request con la pagina actual-->
<% request.setAttribute("activePage", "prestamos"); %>
<%@ include file="navbarClientes.jsp" %>


	<div class="main-container">
	    <div class="welcome-card">
	        <h1>Solicitar préstamo</h1>
	
	        <%-- FORMULARIO DE TRANSFERENCIA DE EJEMPLO --%>
	        <form action="ServletSolicitudPrestamo" method="post">
	            <label for="cuentasCliente">Seleccione una cuenta:</label><br>
	            <select name="cuentasCliente" id="cuentasCliente" required>
	                <option value="12345678">12345678 - Caja de Ahorro</option>
	                <option value="87654321">87654321 - Cuenta Corriente</option>     
	            </select><br><br>
	            
	            <label for="monto">Monto a solicitar:</label><br>
	            <input type="number" name="monto" id="monto" step="1" min="1" required><br><br>
	            
	            <label for="cantidadCuotas">Seleccione la cantidad de cuotas:</label><br>
	            <select name="cantidadCuotas" id="cantidadCuotas" required>
	                <option value="12">12 cuotas</option>
	                <option value="24">24 cuotas</option>   
	                <option value="36">36 cuotas</option>   
	                <option value="48">48 cuotas</option>   
	                <option value="60">60 cuotas</option>   
	                <option value="72">72 cuotas</option>     
	            </select><br><br>
	
	            <input type="submit" value="Solicitar Prestamo" class="action-button">
	        </form>
	    </div>
	</div>

	<%@ include file="../comunes/footer.jsp" %>

</body>
</html>