<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
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
					<%
					    List<Object[]> listaCuentas = (List<Object[]>) request.getAttribute("listaCuentas");
					    if (listaCuentas != null) {
					        for (Object[] fila : listaCuentas) {
					            int nroCuenta = (int) fila[0];
					            String cbu = (String) fila[1];
					            String tipo = (String) fila[2];
					            String cliente = (String) fila[3];
					            java.math.BigDecimal saldo = (java.math.BigDecimal) fila[4];
					            java.time.LocalDateTime fecha = (java.time.LocalDateTime) fila[5];
					            boolean estado = (boolean) fila[6];
					%>
					    <tr>
					        <td><%= nroCuenta %></td>
					        <td><%= cbu %></td>
					        <td><%= tipo %></td>
					        <td><%= cliente %></td>
					        <td>$<%= saldo %></td>
					        <td><%= fecha.toLocalDate() %></td>
					        <td><%= estado ? "Activa" : "Inactiva" %></td>
					        <td>
					            <a href="modificarCuenta.jsp?id=<%= nroCuenta %>" class="boton-modificar">Modificar</a>
					            <% if (estado) { %>
					                <a href="ServletCuenta?accion=desactivar&id=<%= nroCuenta %>" class="boton-eliminar" onclick="return confirm('¿Seguro que desea desactivar esta cuenta?')">Desactivar</a>
					            <% } else { %>
					                <a href="ServletCuenta?accion=activar&id=<%= nroCuenta %>" class="boton-activar">Activar</a>
					            <% } %>
					        </td>
					    </tr>
					<%
					        }
					    }
					%>
				</tbody>
            </table>
        </div>
    </div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>