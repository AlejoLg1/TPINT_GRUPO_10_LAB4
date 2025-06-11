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
<title>Inicio - Cliente Banco UTN</title>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/menuClienteEstilos.css">
</head>
<body>

<div class="navbar">
    <ul>
        <li><a href="menuCliente.jsp" class="active">Inicio</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/cliente/verCuentas.jsp">Ver cuentas</a></li>
        <li><a href="${pageContext.request.contextPath}/jsp/cliente/transferencias.jsp">Transferencias</a></li>
        <li><a href="#">Préstamos</a></li>
        <li><a href="#">Pago de cuotas</a></li>
        <li><a href="#">Mis datos</a></li>
    </ul>
</div>

<div class="main-container">
    <div class="welcome-card">
        <h1>Bienvenido al Banco UTN</h1>
        <p class="user-greeting">Hola, <strong><%= session.getAttribute("usuario") %></strong> <span class="waving-hand">👋</span></p>
    </div>

    <div class="actions-section">
        <h2>¿Qué deseas hacer hoy?</h2>
        <div class="action-buttons">
            <a href="${pageContext.request.contextPath}/jsp/cliente/verCuentas.jsp" class="action-button">Ver mis cuentas</a>
            <a href="${pageContext.request.contextPath}/jsp/cliente/transferencias.jsp" class="action-button">Hacer una transferencia</a>
            <a href="#" class="action-button">Consultar mis movimientos</a>
            <a href="#" class="action-button">Pagar mis préstamos</a>
        </div>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>