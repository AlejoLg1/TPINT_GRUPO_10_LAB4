<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<!-- Navbar para usuarios de tipo cliente, incluir archivo en cada jsp -->
<!-- Ademas incluir archivo css navbarClientesEstilos en cada archivo css -->

    
<div class="navbar">
    <ul class="nav-left">
 <li><a href="${pageContext.request.contextPath}/jsp/cliente/menuCliente.jsp">Inicio</a></li>
    <li><a href="${pageContext.request.contextPath}/jsp/cliente/verCuentas.jsp">Mis cuentas</a></li>
    <li><a href="${pageContext.request.contextPath}/jsp/cliente/transferencias.jsp">Transferencias</a></li>
    <li><a href="${pageContext.request.contextPath}/jsp/cliente/solicitarPrestamo.jsp">Préstamos</a></li>
    <li><a href="${pageContext.request.contextPath}/jsp/cliente/pagarCuotas.jsp">Pago de cuotas</a></li>
    <li><a href="${pageContext.request.contextPath}/jsp/cliente/misDatos.jsp">Mis datos</a></li>
    </ul>
    
    <div class="logout">
	    <a href="<%= request.getContextPath() %>/jsp/comunes/login.jsp" class="logout-link">Salir</a>
	</div>
	    
</div>
