<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="dominio.Cliente" %>
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
    <title>Mis Datos - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/misDatosEstilos.css">
</head>
<body>

<!--  incluye navbar y envia un request con la pagina actual-->
<% request.setAttribute("activePage", "datos"); %>
<%@ include file="navbarClientes.jsp" %>


<%
	Cliente cliente = (Cliente)request.getAttribute("infoCliente");
	if(cliente == null)
		response.sendRedirect(request.getContextPath() + "/jsp/cliente/menuCliente.jsp");
%>


	<div class="main-container">
	    <div class="welcome-card">
	        <h1>Mis datos</h1>
	
	        <div class="form-group">
	            <div class="form-item">
	                <label for="nombre">Nombre:</label>
	                <input type="text" id="nombre" value="<%=cliente.getNombre()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="apellido">Apellido:</label>
	                <input type="text" id="apellido" value="<%=cliente.getApellido()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="dni">DNI:</label>
	                <input type="text" id="dni" value="<%=cliente.getDni()%>" readonly>
	            </div>
	        </div>
	
	        <div class="form-group">
	            <div class="form-item">
	                <label for="cuil">Cuil:</label>
	                <input type="text" id="cuil" value="<%=cliente.getCuil()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="nacionalidad">Nacionalidad:</label>
	                <input type="text" id="nacionalidad" value="<%=cliente.getNacionalidad()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="fechanac">Fecha de Nacimiento:</label>
	                <input type="text" id="fechanac" value="<%=cliente.getFechaNacimiento()%>" readonly>
	            </div>
	        </div>
	
	        <div class="form-group">
	            <div class="form-item">
	                <label for="direccion">Dirección:</label>
	                <input type="text" id="direccion" value="<%=cliente.getDireccion().getDirCompleta()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="localidad">Localidad:</label>
	                <input type="text" id="localidad" value="<%=cliente.getLocalidad().getId() %>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="provincia">Provincia:</label>
	                <input type="text" id="provincia" value="<%=cliente.getProvincia().getId() %>" readonly>
	            </div>
	        </div>
	
	        <div class="form-group">
	            <div class="form-item">
	                <label for="email">Email:</label>
	                <input type="text" id="email" value="<%=cliente.getCorreo()%>" readonly>
	            </div>
	
	            <div class="form-item">
	                <label for="telefono">Teléfono:</label>
	                <input type="text" id="telefono" value="<%=cliente.getTelefono()%>" readonly>
	            </div>
	            
	            <div class="form-item">
	                <label for="sexo">Sexo:</label>
	                <input type="text" id="sexo" value="<%=cliente.getSexoCompleto()%>" readonly>
	            </div>
	        </div>
	    </div>
	</div>
	
	
	
	<%@ include file="../comunes/footer.jsp" %>
</body>
</html>