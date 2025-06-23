<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
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
    <title>Administrar Cuentas</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cuentaEstilos.css">
</head>
<body>
   <% request.setAttribute("activePage", "cuentas"); %>
  <%@ include file="navbarAdmin.jsp" %>

    <!-- Contenido principal -->
    <div class="main-container">
        <div class="welcome-card">
            <h1>Administrar Cuentas</h1>

            <!-- Acciones superiores -->
            <div class="acciones-superiores">
                <a href="<%= request.getContextPath() %>/ServletAltaCuenta" class="crear-cliente-button">Crear nueva Cuenta</a>
            </div>
            

            <!-- Filtros y búsqueda -->
            <form action="ServletCuenta" method="get" class="filtros-form">
                <label>Buscar por cliente o CBU:</label>
                <input type="text" name="busqueda" placeholder="Nombre o CBU">

                <label>Tipo de cuenta:</label>
                <select name="tipoCuenta">
                    <option value="">-- Todos --</option>
                    <option value="Caja de ahorro">Caja de ahorro</option>
                    <option value="Cuenta corriente">Cuenta corriente</option>
                </select>

                <label>Saldo desde:</label>
                <input type="number" name="saldoMin" step="0.01">

                <label>Saldo hasta:</label>
                <input type="number" name="saldoMax" step="0.01">

                <button type="submit" class="boton-filtrar">Filtrar</button>
            </form>

            <!-- Tabla de cuentas -->
            <table class="tabla-cuentas">
                <thead>
                    <tr>
                        <th>Nº Cuenta</th>
                        <th>CBU</th>
                        <th>Tipo</th>
                        <th>Cliente</th>
                        <th>Saldo</th>
                        <th>Fecha Creación</th>
                        <th>Estado</th>
                        <th>Acciones</th>
                    </tr>
                </thead>
                <tbody>
                    <!-- Ejemplo de fila -->
                    <tr>
                        <td>123456</td>
                        <td>0123456789123456789012</td>
                        <td>Caja de ahorro</td>
                        <td>Juan Pérez</td>
                        <td>$10,000.00</td>
                        <td>2025-06-12</td>
                        <td>Activa</td>
                        <td>
                            <a href="modificarCuenta.jsp?id=123" class="boton-modificar">Modificar</a>
                            <a href="ServletCuenta?accion=eliminar&id=123" class="boton-eliminar" onclick="return confirm('¿Seguro que desea eliminar esta cuenta?')">Eliminar</a>
                        </td>
                    </tr>
                </tbody>
            </table>
        </div>
    </div>

    <%@ include file="../comunes/footer.jsp" %>
</body>
</html>