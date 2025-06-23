<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
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
    <title>Alta de Cuenta</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cuentaEstilos.css">
</head>
<body>
    <% request.setAttribute("activePage", "cuentas"); %>
    <%@ include file="navbarAdmin.jsp" %>

    <div class="main-container">
        <div class="welcome-card">
            <h1>Alta de Cuenta</h1>

            <form action="ServletAltaCuenta" method="post" class="form-alta-cuenta">
                <label>Cliente:</label>
                <select name="clienteId" required>
                    <option value="">-- Seleccionar Cliente --</option>
                    <!-- Aquí el Servlet debe cargar lista de clientes -->
                    <%
					    List<dominio.Cliente> listaClientes = (List<dominio.Cliente>) request.getAttribute("listaClientes");
					    if (listaClientes != null) {
					        for (dominio.Cliente c : listaClientes) {
					%>
					        <option value="<%= c.getIdCliente() %>"><%= c.getNombre() + " " + c.getApellido() %></option>
					<%
					        }
					    }
					%>

                </select>

                <label>Tipo de cuenta:</label>
                <select name="tipoCuenta" required>
                    <option value="">-- Seleccionar Tipo --</option>
                    <option value="Caja de ahorro">Caja de ahorro</option>
                    <option value="Cuenta corriente">Cuenta corriente</option>
                </select>

                <!-- Monto inicial fijo según consigna -->
                <label>Monto Inicial:</label>
                <input type="number" name="montoInicial" value="10000.00" readonly step="0.01">

                <button type="submit" class="boton-guardar">Crear Cuenta</button>
            </form>
        </div>
    </div>

    <%@ include file="../comunes/footer.jsp" %>
</body>
</html>