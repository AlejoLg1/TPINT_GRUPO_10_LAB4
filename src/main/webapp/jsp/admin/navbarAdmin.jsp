<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
    
<%
    String rol = (String) session.getAttribute("rol");
%>

<div class="mi-navbar">
    <ul class="nav-left">
        <li><a href="<%= request.getContextPath() %>/ServletMenuAdmin" class="<%= "inicio".equals(request.getAttribute("activePage")) ? "active" : "" %>">Inicio</a></li>

        <% if ("admin".equals(rol)) { %>
            <li><a href="<%= request.getContextPath() %>/ServletListarCliente" class="<%= "clientes".equals(request.getAttribute("activePage")) ? "active" : "" %>">Clientes</a></li>
            <li><a href="<%= request.getContextPath() %>/ServletListarUsuario" class="<%= "usuarios".equals(request.getAttribute("activePage")) ? "active" : "" %>">Usuarios</a></li>
        <% } %>

        <li><a href="<%= request.getContextPath() %>/ServletCuenta" class="<%= "cuentas".equals(request.getAttribute("activePage")) ? "active" : "" %>">Cuentas</a></li>
        <li><a href="<%= request.getContextPath() %>/ServletListarPrestamos" class="<%= "prestamos".equals(request.getAttribute("activePage")) ? "active" : "" %>">Préstamos</a></li>
        <li><a href="<%= request.getContextPath() %>/jsp/admin/reportes.jsp" class="<%= "reportes".equals(request.getAttribute("activePage")) ? "active" : "" %>">Reportes</a></li>
    </ul>
    
    <div class="logout">
        <span class="user-info">Usuario: <%= session.getAttribute("usuario") %></span>
        <span class="separator"> | </span>
        <a href="<%= request.getContextPath() %>/jsp/comunes/login.jsp" class="logout-link">Salir</a>
    </div>
</div>

