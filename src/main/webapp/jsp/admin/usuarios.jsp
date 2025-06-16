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
    <title>Usuarios - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/usuariosEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "usuarios"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Listado de Usuarios</h1>

        <div class="acciones-superiores">
            <a href="${pageContext.request.contextPath}/jsp/admin/altaUsuario.jsp" class="crear-usuario-button">+ Crear Usuario</a>
        </div>

        <table class="tabla-usuario">
            <thead>
                <tr>
                    <th>Username</th>
		            <th>Email</th>
		            <th>Rol</th>
		            <th>Estado</th>
		            <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>admin01</td>
		            <td>admin@banco.com</td>
		            <td>Admin</td>
		            <td>Activo</td>
                    <td>
                        <a href="modificarUsuario.jsp?id=1" class="boton-modificar">Modificar</a>
                        <a href="bajaUsuario.jsp?id=1" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
                <tr>
                    <td>pedro123</td>
		            <td>pedro@gmail.com</td>
		            <td>Cliente</td>
		            <td>Activo</td>
                    <td>
                        <a href="modificarUsuario.jsp?id=2" class="boton-modificar">Modificar</a>
                        <a href="bajaUsuario.jsp?id=2" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
                <tr>
                    <td>admin02</td>
		            <td>admin2@banco.com</td>
		            <td>Admin</td>
		            <td>Activo</td>
                    <td>
                        <a href="modificarUsuario.jsp?id=3" class="boton-modificar">Modificar</a>
                        <a href="bajaUsuario.jsp?id=3" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>
