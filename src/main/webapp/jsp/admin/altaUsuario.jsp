<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>Crear Usuario - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaUsuarioEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "clientes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Alta de Usuario</h1>

		<%
			boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
			
			if(estado == true)
			{%>
				<div class="panelSuccess">
				  ¡Operación realizada con éxito!
				</div>	
			<%}
		%>

        <form action="${pageContext.request.contextPath}/ServletAltaUsuario" method="post">
            <div class="form-group">                
                <div class="form-item">
                	<label for="tipoUser">Tipo de Usuario:</label>
                    <select name="tipoUser" id="tipoUser" required>
		                <option value="Admin">Administrador</option>
		                <option value="Usuario"> Usuario</option>     
		            </select>
                </div>
            </div>

            <hr style="margin: 30px 0;">

            <h2>Datos de Usuario</h2>

            <div class="form-group">
                <div class="form-item">
                    <label for="username">Usuario:</label>
                    <input type="text" id="username" name="username" required>
                </div>

                <div class="form-item">
                    <label for="pass">Esciba una contraseña:</label>
                    <input type="password" id="pass" name="pass" required>
                </div>

                <div class="form-item">
                    <label for="passRepetida">Repita la contraseña:</label>
                    <input type="password" id="passRepetida" name="passRepetida" required>
                </div>

            </div>

            <div style="text-align: center; margin-top: 30px;">
			    <input type="submit" name="btnAltaUsuario" value="Crear Usuario" class="action-button">           
			    <a href="${pageContext.request.contextPath}/jsp/admin/usuarios.jsp" class="action-button volver-button">Volver </a>            
			</div>
            
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
