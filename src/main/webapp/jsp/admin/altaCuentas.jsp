<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page session="true" %>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cliente" %>
<%@ page import="dominio.TipoCuenta" %>

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

            <%
                String mensaje = (String) request.getAttribute("mensaje");
                if (mensaje != null) {
            %>
                <p style="color: green; font-weight: bold; margin-bottom: 15px;"><%= mensaje %></p>
            <%
                }
            %>

            <form action="ServletAltaCuenta" method="post" class="form-alta-cuenta">
                <label>Cliente:</label>
                <select name="clienteId" required>
                    <option value="">-- Seleccionar Cliente --</option>
                    <%
                        List<Cliente> listaClientes = (List<Cliente>) request.getAttribute("listaClientes");
                        if (listaClientes != null) {
                            for (Cliente c : listaClientes) {
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
                    <%
                        List<TipoCuenta> listaTiposCuenta = (List<TipoCuenta>) request.getAttribute("listaTiposCuenta");
                        if (listaTiposCuenta != null) {
                            for (TipoCuenta tipo : listaTiposCuenta) {
                    %>
                        <option value="<%= tipo.getIdTipoCuenta() %>"><%= tipo.getDescripcion() %></option>
                    <%
                            }
                        }
                    %>
                </select>

                <label>Monto Inicial:</label>
                <input type="number" name="montoInicial" value="10000.00" readonly step="0.01">

                <button type="submit" class="boton-guardar">Crear Cuenta</button>
            </form>
        </div>
    </div>

    <%@ include file="../comunes/footer.jsp" %>
</body>
</html>
