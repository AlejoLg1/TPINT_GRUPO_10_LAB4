<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <title>Solicitar Prestmao - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/misDatosEstilos.css">
</head>
<body>

<!--  incluye navbar y envia un request con la pagina actual-->
<% request.setAttribute("activePage", "datos"); %>
<%@ include file="navbarClientes.jsp" %>


<%
	// obtener datos de un request a futuro

	String Nombre = "Nombre Usuario";
	String Apellido = "Apellido Usuario";
	String Dni = "34567890";
	String Sexo = "Masculino";
	String Cuil = "12-34567890-1";
	String Nacionalidad = "Argentina";
	String FechaNac = "01/01/1999";
	String Direccion = "Direccion usuairo 1234";
	String Localidad = "localidad usaurio";
	String Provincia = "Buenos Aires";
	String Email = "Usuario@gmail.com";
	String Telefono = "1123456789";
%>


<div class="main-container">
    <div class="welcome-card">
        <h1>Mis datos</h1>

        <div class="form-group">
            <div class="form-item">
                <label for="nombre">Nombre:</label>
                <input type="text" id="nombre" value="<%=Nombre%>" readonly>
            </div>

            <div class="form-item">
                <label for="apellido">Apellido:</label>
                <input type="text" id="apellido" value="<%=Apellido%>" readonly>
            </div>

            <div class="form-item">
                <label for="dni">DNI:</label>
                <input type="text" id="dni" value="<%=Dni%>" readonly>
            </div>
        </div>

        <div class="form-group">
            <div class="form-item">
                <label for="cuil">Cuil:</label>
                <input type="text" id="cuil" value="<%=Cuil%>" readonly>
            </div>

            <div class="form-item">
                <label for="nacionalidad">Nacionalidad:</label>
                <input type="text" id="nacionalidad" value="<%=Nacionalidad%>" readonly>
            </div>

            <div class="form-item">
                <label for="fechanac">Fecha de Nacimiento:</label>
                <input type="text" id="fechanac" value="<%=FechaNac%>" readonly>
            </div>
        </div>

        <div class="form-group">
            <div class="form-item">
                <label for="direccion">Dirección:</label>
                <input type="text" id="direccion" value="<%=Direccion%>" readonly>
            </div>

            <div class="form-item">
                <label for="localidad">Localidad:</label>
                <input type="text" id="localidad" value="<%=Localidad%>" readonly>
            </div>

            <div class="form-item">
                <label for="provincia">Provincia:</label>
                <input type="text" id="provincia" value="<%=Provincia%>" readonly>
            </div>
        </div>

        <div class="form-group">
            <div class="form-item">
                <label for="email">Email:</label>
                <input type="text" id="email" value="<%=Email%>" readonly>
            </div>

            <div class="form-item">
                <label for="telefono">Teléfono:</label>
                <input type="text" id="telefono" value="<%=Telefono%>" readonly>
            </div>
            
            <div class="form-item">
                <label for="sexo">Sexo:</label>
                <input type="text" id="sexo" value="<%=Sexo%>" readonly>
            </div>
        </div>
    </div>
</div>



<%@ include file="../comunes/footer.jsp" %>

</body>
</html>