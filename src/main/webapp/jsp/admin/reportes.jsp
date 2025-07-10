<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ page import="java.util.*" %>
<%@ page import="dominio.Reporte" %>
<%
    Object usuario = session.getAttribute("usuario");
    if (usuario == null) {
        response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
        return;
    }

    String titulo = (String) request.getAttribute("titulo");
    List<Reporte> reportes = (List<Reporte>) request.getAttribute("reportes");
    List<Reporte> reportesResumen = (List<Reporte>) request.getAttribute("reportesResumen");
    List<Reporte> morosos = (List<Reporte>) request.getAttribute("morosos");
    String error = (String) request.getAttribute("error");
%>

<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Reportes - Banco UTN</title>
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reportesEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "reportes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1 class="mb-4">Reportes</h1>

        <form action="${pageContext.request.contextPath}/ServletReporte" method="post">
            <div class="d-flex flex-wrap gap-4 mb-4">
                <div class="form-item">
                    <label for="tipoReporte">Seleccione Reporte:</label>
                    <select id="tipoReporte" name="tipoReporte" class="form-select">
                        <option value="">--Seleccione un valor--</option>
                        <option value="Alta de clientes">Alta de Clientes</option>
                        <option value="Baja de clientes">Baja de Clientes</option>
                        <option value="Estado de cuentas">Estado de cuentas</option>
                        <option value="Registro de deuda">Registro de deuda</option>
                    </select>
                </div>

                <div class="form-item">
                    <label for="inicioPeriodo">Desde:</label>
                    <input type="date" id="inicioPeriodo" name="inicioPeriodo" class="form-control">
                </div>

                <div class="form-item">
                    <label for="finPeriodo">Hasta:</label>
                    <input type="date" id="finPeriodo" name="finPeriodo" class="form-control">
                </div>
            </div>

            <div class="text-center">
                <input type="submit" name="btnGenerarReporte" value="Generar Reporte" class="btn btn-primary px-4">
            </div>
        </form>

        <% if (error != null) { %>
            <p class="text-danger text-center mt-3"><%= error %></p>
        <% } %>
    </div>

    <%-- Mostrar tablas si hay resultados --%>
    <% if (reportes != null) { %>
        <div class="reportes-generados mt-5">
            <h3><%= titulo != null ? titulo : "Resultados del Reporte" %></h3>

            <% if (!reportes.isEmpty()) { %>
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-primary">
                        <tr>
                            <th>Reporte</th>
                            <th class="text-center">Total</th>
                            <th class="text-end">Monto</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reporte r : reportes) { %>
                            <tr>
                                <td><%= r.getNombreReporte() %></td>
                                <td class="text-center"><%= r.getTotal() %></td>
                                <td class="text-end"><%= r.getMonto() != null ? "$" + r.getMonto() : "-" %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p class="text-muted">No se encontraron resultados para el reporte seleccionado.</p>
            <% } %>
        </div>
    <% } %>

    <%-- Mostrar resumen de deuda si existe --%>
    <% if (reportesResumen != null) { %>
        <div class="reportes-generados mt-5">
            <h3>Resumen de Deuda</h3>

            <% if (!reportesResumen.isEmpty()) { %>
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-primary">
                        <tr>
                            <th>Estado</th>
                            <th class="text-center">Total</th>
                            <th class="text-end">Monto Total</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reporte r : reportesResumen) { %>
                            <tr>
                                <td><%= r.getNombreReporte() %></td>
                                <td class="text-center"><%= r.getTotal() %></td>
                                <td class="text-end"><%= r.getMonto() != null ? "$" + r.getMonto() : "-" %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p class="text-muted">No se encontraron resultados para el resumen de deuda.</p>
            <% } %>
        </div>
    <% } %>

    <%-- Mostrar clientes morosos si existen --%>
    <% if (morosos != null) { %>
        <div class="reportes-generados mt-5">
            <h3>Clientes Morosos</h3>

            <% if (!morosos.isEmpty()) { %>
                <table class="table table-bordered table-hover align-middle">
                    <thead class="table-danger">
                        <tr>
                            <th>Cliente</th>
                            <th class="text-center">Cantidad de Cuotas</th>
                            <th class="text-end">Monto Cuota</th>
                        </tr>
                    </thead>
                    <tbody>
                        <% for (Reporte r : morosos) { %>
                            <tr>
                                <td><%= r.getNombreReporte() %></td>
                                <td class="text-center"><%= r.getTotal() %></td>
                                <td class="text-end"><%= r.getMonto() != null ? "$" + r.getMonto() : "-" %></td>
                            </tr>
                        <% } %>
                    </tbody>
                </table>
            <% } else { %>
                <p class="text-muted">No se encontraron clientes morosos.</p>
            <% } %>
        </div>
    <% } %>

</div>

<%@ include file="../comunes/footer.jsp" %>

</body>
</html>
