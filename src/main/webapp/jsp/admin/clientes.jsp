<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page session="true" %>
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
    <title>Clientes - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clientesEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "clientes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Listado de Clientes</h1>

        <div class="acciones-superiores">
            <a href="altaCliente.jsp" class="crear-cliente-button">+ Crear Cliente</a>
        </div>

        <table class="tabla-clientes">
            <thead>
                <tr>
                    <th>Nombre</th>
                    <th>Apellido</th>
                    <th>DNI</th>
                    <th>CUIL</th>
                    <th>Estado</th>
                    <th>Acciones</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>Juan</td>
                    <td>Pérez</td>
                    <td>30555222</td>
                    <td>20-30555222-5</td>
                    <td>Activo</td>
                    <td>
                        <a href="modificarCliente.jsp?id=1" class="boton-modificar">Modificar</a>
                        <a href="bajaCliente.jsp?id=1" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
                <tr>
                    <td>María</td>
                    <td>Gómez</td>
                    <td>28333444</td>
                    <td>27-28333444-2</td>
                    <td>Inactivo</td>
                    <td>
                        <a href="modificarCliente.jsp?id=2" class="boton-modificar">Modificar</a>
                        <a href="bajaCliente.jsp?id=2" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
                <tr>
                    <td>Luis</td>
                    <td>Martínez</td>
                    <td>31222444</td>
                    <td>20-31222444-3</td>
                    <td>Activo</td>
                    <td>
                        <a href="modificarCliente.jsp?id=3" class="boton-modificar">Modificar</a>
                        <a href="bajaCliente.jsp?id=3" class="boton-eliminar">Eliminar</a>
                    </td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>
