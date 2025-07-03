<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
<title>Inicio - Banco UTN</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menuAdminEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "inicio"); %>
<%@ include file="navbarAdmin.jsp" %>
	
	<div class="main-container">
	    <div class="welcome-card">
	        <h1>Bienvenido al Banco UTN</h1>
	        <p class="user-greeting">Hola, <strong><%= session.getAttribute("usuario") %></strong> <span class="waving-hand">👋</span></p>
	    </div>
	
	    <div class="actions-section">
	        <h2>¿Qué deseas hacer hoy?</h2>
			<div class="action-buttons">
			    <a href="${pageContext.request.contextPath}/ServletListarCliente" class="action-button">Ver clientes</a>
				<a href="${pageContext.request.contextPath}/ServletListarUsuario" class="action-button">Gestionar usuarios</a>
			    <a href="${pageContext.request.contextPath}/ServletCuenta" class="action-button">Ver cuentas</a>
			    <a href="${pageContext.request.contextPath}/ServletListarPrestamos" class="action-button">Gestionar prestamos</a>
			    <a href="${pageContext.request.contextPath}/ServletReporte" class="action-button">Reportes</a>
			</div>

	    </div>
	</div>
	
	<%@ include file="../comunes/footer.jsp" %>

</body>
</html>