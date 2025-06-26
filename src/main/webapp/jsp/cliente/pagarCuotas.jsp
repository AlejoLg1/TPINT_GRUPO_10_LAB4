<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Cuota" %>
<%@ page session="true" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    List<Cuota> cuotasPendientes = (List<Cuota>) request.getAttribute("cuotasPendientes");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Pagar Cuotas - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/pagarCuotasEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "pagoCuotas"); %>
<%@ include file="navbarClientes.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Pagar cuotas</h1>
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
            <% out.println("Total cuotas recibidas: " + (cuotasPendientes != null ? cuotasPendientes.size() : "null")); %>
                <%
                    if (cuotasPendientes != null && !cuotasPendientes.isEmpty()) {
                        for (Cuota cuota : cuotasPendientes) {
                %>
                    <tr>
                        <td><%= cuota.getIdPrestamo() %></td>
                        <td><%= cuota.getNumeroCuota() %></td>
                        <td>$<%= cuota.getMonto() %></td>
                        <td><%= cuota.getFechaPago() != null ? cuota.getFechaPago() : "Sin fecha" %></td>
                        <td><%= cuota.getEstado() %></td>
                        <td>
                            <form action="ServletConfirmarPagoCuotas" method="post">
                                <input type="hidden" name="cuotas" value="<%= cuota.getIdCuota() %>" />
                                <button type="submit">Pagar</button>
                            </form>
                        </td>
                    </tr>
                <%
                        }
                    } else {
                %>
                    <tr>
                        <td colspan="6">No hay cuotas pendientes para mostrar.</td>
                    </tr>
                <%
                    }
                %>
            </tbody>
        </table>
    </div>
</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>