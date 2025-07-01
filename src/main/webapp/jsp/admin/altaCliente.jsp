<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="dominio.Cliente" %>
<%@ page import="dominio.Localidad" %>
<%@ page import="dominio.Provincia" %>
<%@ page import="java.util.*" %>
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

        <%  // Mensaje de confirmación de estado
            boolean estado = (request.getAttribute("estado") != null ? (boolean)request.getAttribute("estado") : false);
            String msg = (String)request.getAttribute("mensaje");

            if(request.getAttribute("estado") != null) {
                if(estado) {
        %>
                    <p style="color: green; font-weight: bold; margin-bottom: 15px;"><%= msg %></p>
        <%
                } else {
        %>
                    <p style="color: red; font-weight: bold; margin-bottom: 15px;"><%= msg %></p>
        <%
                }
            }
        %>

        <form action="${pageContext.request.contextPath}/<%= esModificacion ? "ServletModificarCliente" : "ServletAltaCliente" %>" method="post">
            <% if (esModificacion) { %>
                <input type="hidden" name="idCliente" value="<%= clienteMod.getIdCliente() %>">
                <input type="hidden" name="idDireccion" value="<%= clienteMod.getDireccion() != null ? clienteMod.getDireccion().getId() : 0 %>">
                <input type="hidden" name="idUsuario" value="<%= clienteMod.getUsuario() != null ? clienteMod.getUsuario().getIdUsuario() : 0 %>">
                <input type="hidden" name="idProvincia" value="<%= clienteMod.getProvincia() != null ? clienteMod.getProvincia().getId() : 0 %>">
                <input type="hidden" name="idLocalidad" value="<%= clienteMod.getLocalidad() != null ? clienteMod.getLocalidad().getId() : 0 %>">
            <% } %>

<div class="form-item">
    <label for="provincia">Provincia:</label>
    <select name="idProvincia" id="provincia" required onchange="onProvinciaChange(this)">
        <option value="">--Seleccione provincia--</option>
        <% 
            List<Provincia> provincias = (List<Provincia>) request.getAttribute("provincias");
            String idProvinciaSel = request.getParameter("idProvincia");

            if (provincias != null) {
                for (Provincia p : provincias) {
                    boolean selected = idProvinciaSel != null && idProvinciaSel.equals(String.valueOf(p.getId()));
        %>
                    <option value="<%= p.getId() %>" <%= selected ? "selected" : "" %>><%= p.getNombre() %></option>
        <%
                }
            }
        %>
    </select>
</div>



			<div class="form-group">
			  <div class="form-item">
			    <label for="localidad">Localidad:</label>
			    <select name="idLocalidad" id="localidad" required>
			      <option value="">--Seleccione localidad--</option>
			      <%
			      List<Localidad> localidades = (List<Localidad>) request.getAttribute("localidades");
			      if (localidades != null && !localidades.isEmpty()) {
			        for (Localidad loc : localidades) {
			      %>
			        <option value="<%= loc.getId() %>"><%= loc.getNombre() %></option>
			      <%
			        }
			      }
			      %>
			    </select>
			  </div>
			</div>

            <div class="form-group">
                <div class="form-item">
                    <label for="nombre">Nombre:</label>
                    <input type="text" id="nombre" name="nombre" value="<%= request.getAttribute("nombre") != null ? request.getAttribute("nombre") : (esModificacion ? clienteMod.getNombre() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="apellido">Apellido:</label>
                    <input type="text" id="apellido" name="apellido" value="<%= request.getAttribute("apellido") != null ? request.getAttribute("apellido") : (esModificacion ? clienteMod.getApellido() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="dni">DNI:</label>
                    <input type="text" id="dni" name="dni" value="<%= request.getAttribute("dni") != null ? request.getAttribute("dni") : (esModificacion ? clienteMod.getDni() : "") %>" <%= esModificacion ? "readonly" : "" %> required pattern="[0-9]+" title="Solo se permiten números.">
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="cuil">CUIL:</label>
                    <input type="text" id="cuil" name="cuil" value="<%= request.getAttribute("cuil") != null ? request.getAttribute("cuil") : (esModificacion ? clienteMod.getCuil() : "") %>" <%= esModificacion ? "readonly" : "" %> required pattern="[0-9]+" title="Solo se permiten números.">
                </div>

                <div class="form-item">
                    <label for="nacionalidad">Nacionalidad:</label>
                    <input type="text" id="nacionalidad" name="nacionalidad" value="<%= request.getAttribute("nacionalidad") != null ? request.getAttribute("nacionalidad") : (esModificacion ? clienteMod.getNacionalidad() : "") %>" required pattern="[A-Za-zÁÉÍÓÚáéíóúÑñ\s]+" title="Solo se permiten letras y espacios.">
                </div>

                <div class="form-item">
                    <label for="fechanac">Fecha de Nacimiento:</label>
                    <input type="date" id="fechanac" name="fechanac" value="<%= request.getAttribute("fechanac") != null ? request.getAttribute("fechanac") : (esModificacion ? clienteMod.getFechaNacimiento() : "") %>" required>
                </div>
            </div>

            <div class="form-group">
                <div class="form-item">
                    <label for="direccion">Dirección:</label>
                    <input type="text" id="direccion" name="direccion" value="<%= request.getAttribute("direccion") != null ? request.getAttribute("direccion") : (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getCalle() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="numero">Número:</label>
                    <input type="text" id="numero" name="numero" value="<%= request.getAttribute("numero") != null ? request.getAttribute("numero") : (esModificacion && clienteMod.getDireccion() != null ? clienteMod.getDireccion().getNumero() : "") %>" required pattern="[0-9]+" title="Solo se permiten números.">
                </div>
            </div>
            


            <div class="form-group">
                <div class="form-item">
                    <label for="email">Email:</label>
                    <input type="email" id="email" name="email" value="<%= request.getAttribute("email") != null ? request.getAttribute("email") : (esModificacion ? clienteMod.getCorreo() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="telefono">Teléfono:</label>
                    <input type="text" id="telefono" name="telefono" value="<%= request.getAttribute("telefono") != null ? request.getAttribute("telefono") : (esModificacion ? clienteMod.getTelefono() : "") %>" required pattern="[0-9]+" title="Solo se permiten números.">
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
                    <input type="text" id="username" name="username" value="<%= request.getAttribute("username") != null ? request.getAttribute("username") : (esModificacion ? clienteMod.getUsuario().getNombreUsuario() : "") %>" required <%= esModificacion ? "readonly" : "" %>>
                </div>

                <div class="form-item">
                    <label for="pass">Escriba una contraseña:</label>
                    <input type="password" id="pass" name="pass" value="<%= request.getAttribute("pass") != null ? request.getAttribute("pass") : (esModificacion ? clienteMod.getUsuario().getClave() : "") %>" required>
                </div>

                <div class="form-item">
                    <label for="passRepetida">Repita la contraseña:</label>
                    <input type="password" id="passRepetida" name="passRepetida" value="<%= request.getAttribute("passRepetida") != null ? request.getAttribute("passRepetida") : (esModificacion ? clienteMod.getUsuario().getClave() : "") %>" required>
                </div>
            </div>

            <div style="text-align: center; margin-top: 30px;">
                <input type="submit" name="btnAltaUsuario" value="<%= esModificacion ? "Guardar Cambios" : "Crear Cliente" %>" class="action-button">
                <a href="${pageContext.request.contextPath}/ServletListarCliente" class="action-button volver-button">Volver</a>
            </div>
        </form>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>

<script>
function onProvinciaChange(select) {
    const idProvincia = select.value;

    // Llamada al servlet que devuelve localidades
    fetch('${pageContext.request.contextPath}/ServletLocalidades?idProvincia=' + idProvincia)
        .then(response => response.json())
        .then(data => {
            const localidadSelect = document.getElementById('localidad');
            localidadSelect.innerHTML = '';

            // Agregar opción por defecto
            const defaultOption = document.createElement('option');
            defaultOption.value = "";
            defaultOption.text = "--Seleccione localidad--";
            localidadSelect.appendChild(defaultOption);

            // Localidades
            data.forEach(localidad => {
                const option = document.createElement('option');
                option.value = localidad.id;
                option.text = localidad.nombre;
                localidadSelect.appendChild(option);
            });
        })
        .catch(error => console.error('Error al cargar localidades:', error));
}
</script>

</html>
