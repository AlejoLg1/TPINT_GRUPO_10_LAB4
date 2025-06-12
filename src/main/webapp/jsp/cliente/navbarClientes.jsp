<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- Navbar para usuarios de tipo cliente, incluir archivo en cada jsp -->
<!-- Ademas incluir archivo css navbarClientesEstilos en cada archivo css -->

    
<div class="navbar">
    <ul class="nav-left">
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/menuCliente.jsp" class="<%= "inicio".equals(request.getAttribute("activePage")) ? "active" : "" %>">Inicio</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/verCuentas.jsp" class="<%= "cuentas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis cuentas</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/transferencias.jsp" class="<%= "transferencias".equals(request.getAttribute("activePage")) ? "active" : "" %>">Transferencias</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/solicitarPrestamo.jsp" class="<%= "prestamos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Préstamos</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/pagarCuotas.jsp" class="<%= "cuotas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Pago de cuotas</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/cliente/misDatos.jsp" class="<%= "datos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis datos</a></li>
    </ul>
    
    <div class="logout">
	    <a href="<%= request.getContextPath() %>/jsp/comunes/login.jsp" class="logout-link">Salir</a>
	</div>
	    
</div>
