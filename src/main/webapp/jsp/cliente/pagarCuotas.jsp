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
    <title>Pagar Cuotas - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagarCuotasEstilos.css">
</head>
<body>

<div class="navbar">
    <ul class="nav-left">
        <li><a href="menuCliente.jsp">Inicio</a></li>
        <li><a href="verCuentas.jsp">Mis cuentas</a></li>
        <li><a href="transferencias.jsp">Transferencias</a></li>
        <li><a href="solicitarPrestamo.jsp">Préstamos</a></li>
        <li><a href="pagarCuotas.jsp" class="active">Pago de cuotas</a></li>
        <li><a href="misDatos.jsp">Mis datos</a></li>
    </ul>
    <div class="user-info">
        Usuario: <strong><%= session.getAttribute("usuario") %></strong>
    </div>
</div>

<div class="main-container">
    <div class="welcome-card">
        <h1>Pagar cuotas</h1>
        <!-- Datos ficticios por ahora -->
        <p>Las siguientes cuotas están pendientes de pago:</p>

        <table border="1" style="width: 100%; margin-top: 20px; border-collapse: collapse;">
            <thead>
                <tr style="background-color: #3498db; color: white;">
                    <th>Préstamo N°</th>
                    <th>Cuota N°</th>
                    <th>Importe</th>
                    <th>Vencimiento</th>
                    <th>Estado</th>
                    <th>Acción</th>
                </tr>
            </thead>
            <tbody>
                <tr>
                    <td>1001</td>
                    <td>1</td>
                    <td>$5.000,00</td>
                    <td>2025-07-10</td>
                    <td>Pendiente</td>
                    <td><button>Pagar</button></td>
                </tr>
                <tr>
                    <td>1002</td>
                    <td>2</td>
                    <td>$7.500,00</td>
                    <td>2025-07-15</td>
                    <td>Pendiente</td>
                    <td><button>Pagar</button></td>
                </tr>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>