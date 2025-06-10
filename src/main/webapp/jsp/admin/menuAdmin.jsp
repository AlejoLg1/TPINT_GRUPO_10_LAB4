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
<title>Incio - Banco UTN</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menuAdminEstilos.css">
</head>
<body>


<div class="navbar">
    <ul>
        <li><a href="menuAdmin.jsp" class="active">Inicio</a></li>
        <li><a href="clientes.jsp">Clientes</a></li>
        <li><a href="usuarios.jsp">Usuarios</a></li>
        <li><a href="cuentas.jsp">Cuentas</a></li>
        <li><a href="prestamos.jsp">Préstamos</a></li>
        <li><a href="reportes.jsp">Reportes</a></li>
</div>

<div class="main-container">
    <div class="welcome-card">
        <h1>Bienvenido al Banco UTN</h1>
        <p class="user-greeting">Hola, <strong><%= session.getAttribute("usuario") %></strong> <span class="waving-hand">👋</span></p>
    </div>

    <div class="actions-section">
        <h2>¿Qué deseas hacer hoy?</h2>
        <div class="action-buttons">
            <a href="cuentas.jsp" class="action-button">
                Ver cuentas
            </a>
            <a href="transferencias.jsp" class="action-button">
                Hacer una transferencia
            </a>
            <a href="movimientos.jsp" class="action-button">
                Consultar movimientos
            </a>
        </div>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>