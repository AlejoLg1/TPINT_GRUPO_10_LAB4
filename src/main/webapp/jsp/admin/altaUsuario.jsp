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

        <form action="ServletAltaUsuario" method="post">
            <div class="form-group">
                <div class="form-item">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" required>
                </div>

                <div class="form-item">
                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" required>
                </div>

                <div class="form-item">
                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="fechanac">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanac" name="fechanac" required>
                </div>
                
                <div class="form-item">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" required>
                </div>
                
                <div class="form-item">
                	<label for="tipoUsuario">Tipo de Usuario:</label>
                    <select name="tipoUsuario" id="tipoUsuario" required>
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
                    <label for="psw">Esciba una contraseña:</label>
                    <input type="password" id="psw" name="psw" required>
                </div>

                <div class="form-item">
                    <label for="pswRepetida">Repita la contraseña:</label>
                    <input type="password" id="pswRepetida" name="pswRepetida" required>
                </div>

            </div>

            <div style="text-align: center; margin-top: 30px;">
			    <a href="${pageContext.request.contextPath}/jsp/admin/usuarios.jsp" class="action-button">Crear Usuario</a>            
			    <a href="${pageContext.request.contextPath}/jsp/admin/usuarios.jsp" class="action-button volver-button">Volver </a>            
			</div>
            
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
