<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dominio.Cliente" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }
    
    Cliente clienteMod = (Cliente) request.getAttribute("clienteMod");
    boolean esModificacion = (clienteMod != null);
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title><%= esModificacion ? "Modificar Cliente" : "Crear Cliente" %> - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/altaClienteEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "clientes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
         <h1><%= esModificacion ? "Modificar Cliente" : "Alta de Cliente" %></h1>

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

         <form action="${pageContext.request.contextPath}/<%= esModificacion ? "ServletModificarCliente" : "ServletAltaCliente" %>" method="post">
         <% if (esModificacion) { %>
    	<input type="hidden" name="idCliente" value="<%= clienteMod.getIdCliente() %>">
    	<input type="hidden" name="idDireccion" value="<%= clienteMod.getDireccion() != null ? clienteMod.getDireccion().getId() : 0 %>">
    	<input type="hidden" name="idUsuario" value="<%= clienteMod.getUsuario() != null ? clienteMod.getUsuario().getIdUsuario() : 0 %>">
    	
        <% } %>
         
            <div class="form-group">
                <div class="form-item">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : (esModificacion ? clienteMod.getNombre() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                     title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido"  value="<%= request.getAttribute("apellido") != null ? request.getAttribute("apellido") : (esModificacion ? clienteMod.getApellido() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                   title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni"  value="<%=request.getAttribute("dni") != null ? request.getAttribute("dni") : (esModificacion ? clienteMod.getDni() : "") %>" <%= esModificacion ? "readonly=\"readonly\"" : "" %> required pattern="[0-9]+"
                    title="Solo se permiten números.">
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="cuil">CUIL:</label>
                    <input type="text" id="cuil" name="cuil"  value="<%= request.getAttribute("cuil") != null ? request.getAttribute("cuil") : (esModificacion ? clienteMod.getCuil() : "") %>" <%= esModificacion ? "readonly=\"readonly\"" : "" %> required pattern="[0-9]+"
                    title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="nacionalidad">Nacionalidad:</label>
                    <input type="text" id="nacionalidad" name="nacionalidad"  value="<%=request.getAttribute("nacionalidad") != null ? request.getAttribute("nacionalidad") :  (esModificacion ? clienteMod.getNacionalidad() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" 
                    title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="fechanac">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanac" name="fechanac"  value="<%=request.getAttribute("fechanac") != null ? request.getAttribute("fechanac") : (esModificacion ? clienteMod.getFechaNacimiento() : "") %>" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" value="<%= request.getAttribute("direccion") != null ? request.getAttribute("direccion") : (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getCalle() : "") %>" required>
                </div>
                
                 <div class="form-group">
                <div class="form-item">
                    <label for="Numero">Número:</label>
                    <input type="text" id="numero" name="numero"  value="<%=request.getAttribute("numero") != null ? request.getAttribute("numero") : (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getNumero() : "") %>" required pattern="[0-9]+"
       title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="localidad">Localidad:</label>
                    <input type="text" id="localidad" name="localidad" value="<%=request.getAttribute("localidad") != null ? request.getAttribute("localidad") :  (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getLocalidad() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="provincia">Provincia:</label>
                    <input type="text" id="provincia" name="provincia" value="<%=request.getAttribute("provincia") != null ? request.getAttribute("provincia") : (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getProvincia() : "") %>" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") :(esModificacion ? clienteMod.getCorreo() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono" value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") :(esModificacion ? clienteMod.getTelefono() : "") %>" required pattern="[0-9]+"
                     title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="sexo">Sexo:</label>
                    <select id="sexo" name="sexo" required>
                        <option value="Masculino" <%= "Masculino".equals(request.getAttribute("sexo")) || (request.getAttribute("sexo") == null && esModificacion && "M".equals(clienteMod.getSexo())) ? "selected" : "" %>>Masculino</option>
						<option value="Femenino" <%= "Femenino".equals(request.getAttribute("sexo")) || (request.getAttribute("sexo") == null && esModificacion && "F".equals(clienteMod.getSexo())) ? "selected" : "" %>>Femenino</option>
						<option value="Otro" <%= "Otro".equals(request.getAttribute("sexo")) || (request.getAttribute("sexo") == null && esModificacion && "X".equals(clienteMod.getSexo())) ? "selected" : "" %>>Otro</option>
                    </select>
                </div>
            </div>

            <hr style="margin: 30px 0;">

            <h2>Datos de Usuario</h2>

            <div class="form-group">
                <div class="form-item">
                    <label for="username">Usuario:</label>
                    <input type="text" id="username" name="username" value="<%=request.getAttribute("username") != null ? request.getAttribute("username") : (esModificacion ? clienteMod.getUsuario().getNombreUsuario() : "") %>" required <%= esModificacion ? "readonly=\"readonly\"" : "" %>>
                </div>

                <div class="form-item">
                    <label for="pass">Escriba una contraseña:</label>
                    <input type="password" id="pass" name="pass" value="<%=request.getAttribute("pass") != null ? request.getAttribute("pass") : (esModificacion ? clienteMod.getUsuario().getClave() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="passRepetida">Repita la contraseña:</label>
                    <input type="password" id="passRepetida" name="passRepetida" value="<%=request.getAttribute("passRepetida") != null ? request.getAttribute("passRepetida") : (esModificacion ? clienteMod.getUsuario().getClave() : "") %>" required>
                </div>
            </div>

            <div style="text-align: center; margin-top: 30px;">
			   <input type="submit" name="btnAltaUsuario" value="<%= esModificacion ? "Guardar Cambios" : "Crear Cliente" %>" class="action-button">               
			    <a href="${pageContext.request.contextPath}/ServletListarCliente" class="action-button volver-button">Volver </a>            
			</div>
            
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
