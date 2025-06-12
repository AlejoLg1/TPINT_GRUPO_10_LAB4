<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
<div class="navbar">
    <ul class="nav-left">
        <li><a href="menuCliente.jsp" class="<%= "inicio".equals(request.getAttribute("activePage")) ? "active" : "" %>">Inicio</a></li>
        <li><a href="verCuentas.jsp" class="<%= "cuentas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis cuentas</a></li>
        <li><a href="transferencias.jsp" class="<%= "transferencias".equals(request.getAttribute("activePage")) ? "active" : "" %>">Transferencias</a></li>
        <li><a href="solicitarPrestamo.jsp" class="<%= "prestamos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Préstamos</a></li>
        <li><a href="pagarCuotas.jsp" class="<%= "cuotas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Pago de cuotas</a></li>
        <li><a href="misDatos.jsp" class="<%= "datos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Mis datos</a></li>
    </ul>
    
    <div class="user-info">
        Usuario: <strong><%= session.getAttribute("usuario") %></strong>
    </div>
</div>
