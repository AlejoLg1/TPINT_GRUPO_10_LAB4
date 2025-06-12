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
    <title>Crear Cliente - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaClienteEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "clientes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Alta de Cliente</h1>

        <form action="ServletAltaCliente" method="post">
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
                    <label for="cuil">CUIL:</label>
                    <input type="text" id="cuil" name="cuil" required>
                </div>

                <div class="form-item">
                    <label for="nacionalidad">Nacionalidad:</label>
                    <input type="text" id="nacionalidad" name="nacionalidad">
                </div>

                <div class="form-item">
                    <label for="fechanac">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanac" name="fechanac">
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion">
                </div>

                <div class="form-item">
                    <label for="localidad">Localidad:</label>
                    <input type="text" id="localidad" name="localidad">
                </div>

                <div class="form-item">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia">
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email">
                </div>

                <div class="form-item">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono">
                </div>

                <div class="form-item">
                    <label for="sexo">Sexo:</label>
                    <select id="sexo" name="sexo">
                        <option value="Masculino">Masculino</option>
                        <option value="Femenino">Femenino</option>
                        <option value="Otro">Otro</option>
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
                    <label for="password">Contraseña:</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <div class="form-item">
                    <label for="estado">Estado:</label>
                    <select id="estado" name="estado">
                        <option value="Activo">Activo</option>
                        <option value="Inactivo">Inactivo</option>
                    </select>
                </div>
            </div>

            <div style="text-align: center; margin-top: 30px;">
			    <a href="${pageContext.request.contextPath}/jsp/admin/clientes.jsp" class="action-button">Crear Cliente</a>            
			    <a href="${pageContext.request.contextPath}/jsp/admin/clientes.jsp" class="action-button volver-button">Volver </a>            
			</div>
            
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
