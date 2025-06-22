<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dominio.Usuario" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    Usuario usuarioMod = (Usuario) request.getAttribute("usuarioMod");
    boolean esModificacion = (usuarioMod != null);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= esModificacion ? "Modificar Usuario" : "Crear Usuario" %> - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaUsuarioEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "usuarios"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1><%= esModificacion ? "Modificar Usuario" : "Alta de Usuario" %></h1>

        <%
            boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
            if (estado) {
        %>
            <div class="panelSuccess">¡Operación realizada con éxito!</div>    
        <% } %>
		
		<%
		    String mensajeError = (String) request.getAttribute("mensajeError");
		    if (mensajeError != null) {
		%>
		    <div class="panelError"><%= mensajeError %></div>
		<%
		    }
		%>
		
		
        <form action="${pageContext.request.contextPath}/<%= esModificacion ? "ServletModificarUsuario" : "ServletAltaUsuario" %>" method="post">
            <% if (esModificacion) { %>
                <input type="hidden" name="id" value="<%= usuarioMod.getIdUsuario() %>">
            <% } %>

            <div class="form-group">                
                <div class="form-item">
                    <label for="tipoUser">Tipo de Usuario:</label>
                    <select name="tipoUser" id="tipoUser" required>
                        <option value="Admin" <%= esModificacion && usuarioMod.isAdmin() ? "selected" : "" %>>Administrador</option>
                        <option value="Usuario" <%= esModificacion && !usuarioMod.isAdmin() ? "selected" : "" %>>Usuario</option>     
                    </select>
                </div>
            </div>

            <hr style="margin: 30px 0;">

            <h2>Datos de Usuario</h2>

            <div class="form-group">
                <div class="form-item">
                    <label for="username">Usuario:</label>
                    <input type="text" id="username" name="username" value="<%= esModificacion ? usuarioMod.getNombreUsuario() : "" %>" <%= esModificacion ? "readonly=\"readonly\"" : "" %> required>
                </div>

                <div class="form-item">
                    <label for="pass">Escriba una contraseña:</label>
                    <input type="password" id="pass" name="pass" value="" <%= esModificacion ? "" : "required" %>>
                </div>

                <div class="form-item">
                    <label for="passRepetida">Repita la contraseña:</label>
                    <input type="password" id="passRepetida" name="passRepetida" value="" <%= esModificacion ? "" : "required" %>>
                </div>
            </div>

            <div style="text-align: center; margin-top: 30px;">
                <input type="submit" name="btnUsuario" value="<%= esModificacion ? "Guardar Cambios" : "Crear Usuario" %>" class="action-button">           
                <a href="${pageContext.request.contextPath}/ServletListarUsuario" class="action-button volver-button">Volver</a>            
            </div>
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
