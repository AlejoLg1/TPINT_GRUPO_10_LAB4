<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- Navbar para usuarios de tipo cliente, incluir archivo en cada jsp -->
<!-- Ademas incluir archivo css navbarClientesEstilos en cada archivo css -->

    
<div class="mi-navbar">
    <ul class="nav-left">
	 	<li><a href="${pageContext.request.contextPath}/ServletMenuCliente" class="<%= "inicio".equals(request.getAttribute("activePage")) ? "active" : "" %>">Inicio</a></li>

		<li><a href="${pageContext.request.contextPath}/ServletVerCuentas" class="<%= "cuentas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis cuentas</a></li>
		
		<li><a href="${pageContext.request.contextPath}/ServletTransferenciasUsuario" class="<%= "transferencias".equals(request.getAttribute("activePage")) ? "active" : "" %>">Transferencias</a></li>
		
		<li><a href="${pageContext.request.contextPath}/ServletSolicitarPrestamo" class="<%= "prestamos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Préstamos</a></li>
		
		<li><a href="${pageContext.request.contextPath}/ServletPagoCuotas" class="<%= "pagoCuotas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Pago de cuotas</a></li>
		
		<li><a href="${pageContext.request.contextPath}/ServletMisDatosCliente" class="<%= "datos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis datos</a></li>
    </ul>
    
	<div class="logout">
	    <span class="user-info">Usuario: <%= session.getAttribute("usuario") %></span>
	    <span class="separator">|</span>
	    <a href="<%= request.getContextPath() %>/jsp/comunes/login.jsp" class="logout-link">Salir</a>
	</div>


	    
</div>
	