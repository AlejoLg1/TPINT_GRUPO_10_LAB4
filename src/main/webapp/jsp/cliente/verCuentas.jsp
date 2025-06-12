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
    <title>Mis Cuentas - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/verCuentasEstilos.css">
</head>
<body>

<!--  incluye navbar y envia un request con la pagina actual-->
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
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>12345678</td>
                    <td>2850590940090412345671</td>
                    <td>Caja de Ahorro</td>            <%-- Estos datos de las cuentas son de ejemplo, después van a venir de la base de datos --%>
                    <td>2024-05-10</td>
                    <td>$15.000,00</td>
                </tr>
                <tr>
                    <td>87654321</td>
                    <td>2850590940090476543210</td>
                    <td>Cuenta Corriente</td>                <%-- Estos datos de las cuentas son de ejemplo, después van a venir de la base de datos --%>
                    <td>2024-03-22</td>
                    <td>$8.500,00</td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>