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

		<%  // Mensaje de confirmacion de exito de solicitud
			boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
			
			if(estado == true)
			{%>
				<div class="panelSuccess">
				  ¡Operación realizada con éxito!
				</div>	
			<%}
		%>
		
		<%
		    String mensajeError = (String) request.getAttribute("mensajeError");
		    if (mensajeError != null) {
		%>
		    <div class="panelError"><%= mensajeError %></div>
		<%
		    }
		%>

        <form action="${pageContext.request.contextPath}/ServletAltaCliente" method="post">
            <div class="form-group">
                <div class="form-item">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="${nombre != null ? nombre : ''}" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                     title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" value="${apellido != null ? apellido : ''}" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                   title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni" value="${dni != null ? dni : ''}" required pattern="[0-9]+"
                    title="Solo se permiten números.">
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="cuil">CUIL:</label>
                    <input type="text" id="cuil" name="cuil" value="${cuil != null ? cuil : ''}" required pattern="[0-9]+"
                    title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="nacionalidad">Nacionalidad:</label>
                    <input type="text" id="nacionalidad" name="nacionalidad" value="${nacionalidad != null ? nacionalidad : ''}" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                    title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="fechanac">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanac" name="fechanac" value="${fechanac != null ? fechanac : ''}" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" value="${direccion != null ? direccion : ''}" required>
                </div>
                
                 <div class="form-group">
                <div class="form-item">
                    <label for="Numero">Número:</label>
                    <input type="text" id="numero" name="numero" value="${numero != null ? numero : ''}" required pattern="[0-9]+"
       title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="localidad">Localidad:</label>
                    <input type="text" id="localidad" name="localidad" value="${localidad != null ? localidad : ''}" required>
                </div>

                <div class="form-item">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia" value="${provincia != null ? provincia : ''}" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="${email != null ? email : ''}" required>
                </div>

                <div class="form-item">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono" value="${telefono != null ? telefono : ''}" required pattern="[0-9]+"
                     title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="sexo">Sexo:</label>
                    <select id="sexo" name="sexo" required>
                        <option ${sexo == 'Masculino' ? 'selected' : ''}>Masculino</option>
                        <option ${sexo == 'Femenino' ? 'selected' : ''}>Femenino</option>
                        <option ${sexo == 'Masculino' ? 'selected' : ''}>Otro</option>
                    </select>
                </div>
            </div>

            <hr style="margin: 30px 0;">

            <h2>Datos de Usuario</h2>

            <div class="form-group">
                <div class="form-item">
                    <label for="username">Usuario:</label>
                    <input type="text" id="username" name="username" value="${username != null ? username : ''}" required>
                </div>

                <div class="form-item">
                    <label for="pass">Escriba una contraseña:</label>
                    <input type="password" id="pass" name="pass" required>
                </div>

                <div class="form-item">
                    <label for="passRepetida">Repita la contraseña:</label>
                    <input type="password" id="passRepetida" name="passRepetida" required>
                </div>
            </div>

            <div style="text-align: center; margin-top: 30px;">
			    <input type="submit" name="btnAltaUsuario" value="Crear Usuario" class="action-button">                 
			    <a href="${pageContext.request.contextPath}/jsp/admin/clientes.jsp" class="action-button volver-button">Volver </a>            
			</div>
            
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
