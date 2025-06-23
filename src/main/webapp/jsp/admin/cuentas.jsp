<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <meta charset="UTF-8">
    <title>Administrar Cuentas</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">

    <!-- CSS personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/cuentaEstilos.css">
</head>

<body>
<% request.setAttribute("activePage", "cuentas"); %>
<%@ include file="navbarAdmin.jsp" %>
<%
    String errorCuenta = (String) request.getAttribute("errorCuenta");
    if (errorCuenta != null) {
%>
    <div class="alert alert-danger alert-dismissible fade show m-4" role="alert">
        <%= errorCuenta %>
        <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
    </div>
<%
    }
%>

<div class="container mt-5">
    <div class="card shadow-sm">
        <div class="card-body">
            <div class="d-flex justify-content-between align-items-center mb-4">
                <h2 class="mb-0">Listado de Cuentas</h2>
                <a href="<%= request.getContextPath() %>/ServletAltaCuenta" class="btn btn-primary">+ Crear Cuenta</a>
            </div>

            <!-- Filtros -->
            <form action="ServletCuenta" method="get" class="row g-3 mb-4">
                <div class="col-md-4">
                    <input type="text" name="busqueda" class="form-control" placeholder="Buscar por cliente o CBU">
                </div>
                <div class="col-md-2">
                    <select name="tipoCuenta" class="form-select">
                        <option value="">-- Tipo de Cuenta --</option>
                        <option value="Caja de ahorro">Caja de ahorro</option>
                        <option value="Cuenta corriente">Cuenta corriente</option>
                    </select>
                </div>
                <div class="col-md-2">
                    <input type="number" name="saldoMin" class="form-control" placeholder="Saldo desde" step="0.01">
                </div>
                <div class="col-md-2">
                    <input type="number" name="saldoMax" class="form-control" placeholder="Saldo hasta" step="0.01">
                </div>
                <div class="col-md-2">
                    <button type="submit" class="btn btn-outline-primary w-100">Filtrar</button>
                </div>
            </form>

            <!-- Tabla de cuentas -->
            <table class="table table-striped table-hover tabla-cuentas" style="width:100%;">
                <thead class="table-light">
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
                        <td>
                            <span class="fw-bold <%= estado ? "text-success" : "text-secondary" %>">
                                <%= estado ? "Activa" : "Inactiva" %>
                            </span>
                        </td>
                        <td>
                            <% if (estado) { %>
                                <button 
                                    type="button"
                                    class="btn btn-danger btn-sm me-1"
                                    onclick="abrirConfirmacion({
                                        action: '${pageContext.request.contextPath}/ServletBajaCuenta',
                                        mensaje: '¿Estás seguro que deseas desactivar esta cuenta?',
                                        botonTexto: 'Desactivar',
                                        botonClase: 'btn-danger',
                                        inputs: [{ name: 'id', value: '<%= nroCuenta %>' }]
                                    })">
                                    Desactivar
                                </button>
                                <a href="<%= request.getContextPath() %>/ServletAltaCuenta?id=<%= nroCuenta %>" class="btn btn-warning btn-sm">Modificar</a>
                            <% } else { %>
                                <button 
                                    type="button"
                                    class="btn btn-success btn-sm"
                                    onclick="abrirConfirmacion({
                                        action: '${pageContext.request.contextPath}/ServletActivarCuenta',
                                        mensaje: '¿Deseas activar nuevamente esta cuenta?',
                                        botonTexto: 'Activar',
                                        botonClase: 'btn-success',
                                        inputs: [{ name: 'id', value: '<%= nroCuenta %>' }]
                                    })">
                                    Activar
                                </button>
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
</div>

<!-- jQuery -->
<script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

<!-- DataTables -->
<script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
<script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

<!-- Configuración idioma español -->
<script src="${pageContext.request.contextPath}/js/datatables-es.js"></script>

<!-- Bootstrap Bundle -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

<script>
    $(document).ready(function () {
        $('.tabla-cuentas').DataTable({
            language: spanishLanguageSettings,
            pageLength: 5,
            lengthMenu: [5, 10, 25, 50],
            responsive: true
        });
    });
</script>

<%@ include file="../comunes/modalConfirmacion.jsp" %>
<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
