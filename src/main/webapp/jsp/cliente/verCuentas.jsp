<%@ page language="java" contentType="text/html; charset=UTF-8"%>
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
    <title>Mis Cuentas - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/verCuentasEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "cuentas"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Mis cuentas</h1>

        <table border="1" style="width: 100%; margin-top: 20px; border-collapse: collapse;">
            <thead>
                <tr style="background-color: #3498db; color: white;">
                    <th>N° de Cuenta</th>
                    <th>CBU</th>
                    <th>Tipo de Cuenta</th>
                    <th>Fecha de Alta</th>
                    <th>Saldo</th>
                    <th>Mis movimientos</th>
                </tr>
            </thead>
            <tbody>
                <%
                    java.util.List cuentas = (java.util.List) request.getAttribute("cuentas");
                    if (cuentas != null) {
                        for (Object obj : cuentas) {
                            dominio.Cuenta cuenta = (dominio.Cuenta) obj;
                %>
                <tr>
                    <td><%= cuenta.getNroCuenta() %></td>
                    <td class="cbu" onclick="copiarCBU(this)"><%= cuenta.getCbu() %></td>
                    <td><%= cuenta.getTipoCuenta() %></td>
                    <td><%= cuenta.getFechaCreacion() %></td>
                    <td>$<%= cuenta.getSaldo() %></td>
                    <td>
                       <button type="button" class="boton-ir-movimientos"
                        onclick="window.location.href='<%= request.getContextPath() %>/ServletMovimientos?nroCuenta=<%= cuenta.getNroCuenta() %>'">
                        Ir a Movimientos
                      </button>
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
<script src="${pageContext.request.contextPath}/js/verCuentas.js"></script>
</body>
</html>