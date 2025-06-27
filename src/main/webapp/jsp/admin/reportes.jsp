<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.*, dominio.Reporte" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }
 
    String titulo = (String) request.getAttribute("titulo");
    List<Reporte> reportes = (List<Reporte>) request.getAttribute("reportes");
    String error = (String) request.getAttribute("error");
%>
 
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reportes - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reportesEstilos.css">
</head>
<body>
 
<% request.setAttribute("activePage", "reportes"); %>
<%@ include file="navbarAdmin.jsp" %>
 
<div class="main-container">
    <div class="welcome-card">
        <h1>Reportes</h1>
 
        <form action="${pageContext.request.contextPath}/ServletReporte" method="post">
            <div class="form-group">
                <div class="form-item">
                    <label for="tipoReporte">Seleccione Reporte:</label>
                    <select id="tipoReporte" name="tipoReporte">
                        <option value="">--Seleccione un valor--</option>
                        <option value="Alta de clientes">Alta de Clientes</option>
                        <option value="Baja de clientes">Baja de Clientes</option>
                        <option value="Estado de cuentas">Estado de cuentas</option>
                        <option value="Registro de deuda">Registro de deuda</option>
                    </select>
                </div>
 
                <div class="form-item">
                    <label for="inicioPeriodo">Desde:</label>
                    <input type="date" id="inicioPeriodo" name="inicioPeriodo">
                </div>
 
                <div class="form-item">
                    <label for="finPeriodo">Hasta:</label>
                    <input type="date" id="finPeriodo" name="finPeriodo">
                </div>
            </div>
 
            <div style="text-align: center; margin-top: 30px;">
                <input type="submit" name="btnGenerarReporte" value="Generar Reporte" class="action-button">    
            </div>            
        </form>
 
        <%-- Mensaje de error si hay --%>
        <% if (error != null) { %>
            <p style="color: red; text-align: center;"><%= error %></p>
        <% } %>
    </div>
 
    <% if (reportes != null) { %>
        <div class="reportes-generados">
            <h3><%= titulo != null ? titulo : "Resultados del Reporte" %></h3>
 
            <% if (!reportes.isEmpty()) { %>
                <table class="tabla-reportes">
                    <thead>
                        <tr>
                            <th>Nombre del Reporte</th>
                            <th>Total</th>
                            <th>Monto</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reporte r : reportes) { %>
                            <tr>
                                <td><%= r.getNombreReporte() %></td>
                                <td><%= r.getTotal() %></td>
                                <td>
                                    <%= r.getMonto() != null ? "$" + r.getMonto().setScale(2, java.math.RoundingMode.HALF_UP) : "-" %>
                                </td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p class="texto-placeholder">No se encontraron resultados para el reporte seleccionado.</p>
            <% } %>
        </div>
    <% } %>
</div>
 
<%@ include file="../comunes/footer.jsp" %>
</body>
</html>