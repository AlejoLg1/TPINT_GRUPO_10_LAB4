<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cliente" %>
<%@ page import="dominio.TipoCuenta" %>
<%@ page import="dominio.Cuenta" %>

<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    Cuenta cuentaMod = (Cuenta) request.getAttribute("cuentaMod");
    boolean esModificacion = cuentaMod != null;
%>

<!DOCTYPE html>
<html>
<head>
    <title><%= esModificacion ? "Modificar Cuenta" : "Alta de Cuenta" %></title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cuentaEstilos.css">
</head>
<body>
    <% request.setAttribute("activePage", "cuentas"); %>
    <%@ include file="navbarAdmin.jsp" %>

    <div class="main-container">
        <div class="welcome-card">
            <h1><%= esModificacion ? "Modificar Cuenta" : "Alta de Cuenta" %></h1>

            <%
                String mensaje = (String) request.getAttribute("mensaje");
                if (mensaje != null) {
            %>
                <p style="color: green; font-weight: bold; margin-bottom: 15px;"><%= mensaje %></p>
            <%
                }
            %>

            <form action="ServletAltaCuenta" method="post" class="form-alta-cuenta">
                <% if (esModificacion) { %>
                    <input type="hidden" name="cuentaId" value="<%= cuentaMod.getNroCuenta() %>">
                <% } %>

                <label>Cliente:</label>
				<% if (esModificacion) { %>
				    <input type="hidden" name="clienteId" value="<%= cuentaMod.getIdCliente() %>">
				<% } %>
				<select name="clienteId" required <%= esModificacion ? "disabled" : "" %>>

                    <option value="">-- Seleccionar Cliente --</option>
                    <%
                        List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
                        if (listaClientes != null) {
                            for (Cliente c : listaClientes) {
                                boolean seleccionado = esModificacion && cuentaMod.getIdCliente() == c.getIdCliente();
                    %>
                        <option value="<%= c.getIdCliente() %>" <%= seleccionado ? "selected" : "" %>><%= c.getNombre() + " " + c.getApellido() %></option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Tipo de cuenta:</label>
                <select name="tipoCuenta" required>
                    <option value="">-- Seleccionar Tipo --</option>
                    <%
                        List<TipoCuenta> listaTiposCuenta = (List<TipoCuenta>) request.getAttribute("listaTiposCuenta");
                        if (listaTiposCuenta != null) {
                            for (TipoCuenta tipo : listaTiposCuenta) {
                                boolean seleccionado = esModificacion && cuentaMod.getIdTipoCuenta() == tipo.getIdTipoCuenta();
                    %>
                        <option value="<%= tipo.getIdTipoCuenta() %>" <%= seleccionado ? "selected" : "" %>><%= tipo.getDescripcion() %></option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Monto Inicial:</label>
                <input type="number" name="montoInicial" 
                       value="<%= esModificacion ? cuentaMod.getSaldo() : "10000.00" %>" 
                       <%= esModificacion ? "" : "readonly" %> step="0.01">

				<button type="submit" class="boton-formulario boton-guardar">
				    <%= esModificacion ? "Guardar Cambios" : "Crear Cuenta" %>
				</button>
				

				<button type="button" onclick="location.href='ServletCuenta'" class="boton-formulario boton-volver">Volver</button>
            </form>
        </div>
    </div>

    <%@ include file="../comunes/footer.jsp" %>
</body>
</html>
