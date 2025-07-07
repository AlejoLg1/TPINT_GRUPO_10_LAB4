<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="java.util.*, dominio.Cuota, dominio.Cuenta" %>
<%@ page import="java.math.BigDecimal" %>
<%
    List<Cuota> cuotas = (List<Cuota>) request.getAttribute("cuotasSeleccionadas");
    List<Cuenta> cuentas = (List<Cuenta>) request.getAttribute("cuentasDisponibles");
    String mensaje = (String) request.getAttribute("mensaje");

    boolean cuotaPagada = cuotas != null && !cuotas.isEmpty() && "PAGADO".equalsIgnoreCase(cuotas.get(0).getEstado());
    boolean pagoExitoso = mensaje != null && mensaje.contains("✅ La cuota fue pagada correctamente");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Confirmar Pago de Cuotas</title>

    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- Tu CSS personalizado -->
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/confirmarPagoEstilos.css">
</head>
<body>

<div class="container mt-5">
    <% if (mensaje != null) { %>
        <div class="alert alert-info alert-dismissible fade show" role="alert">
            <%= mensaje %>
            <button type="button" class="btn-close" data-bs-dismiss="alert" aria-label="Close"></button>
        </div>
    <% } %>

    <div class="card shadow-sm">
        <div class="card-body">
            <h2 class="mb-4">Confirmar Pago de Cuota</h2>

            <% if (!cuotaPagada) { %>
            <!-- Mostrar formulario solo si la cuota no está pagada -->
            <form action="ServletPagoCuotas" method="post">
                <table class="table table-bordered">
                    <thead class="table-light">
                        <tr>
                            <th>Préstamo N°</th>
                            <th>Cuota N°</th>
                            <th>Importe</th>
                            <th>Estado</th>
                        </tr>
                    </thead>
                    <tbody>
                        <%
                            BigDecimal total = new BigDecimal(0);
                            if (cuotas != null) {
                                for (Cuota c : cuotas) {
                                    if (c.getMonto() != null) {
                                        total = total.add(c.getMonto());
                        %>
                        <tr>
                            <td><%= c.getIdPrestamo() %></td>
                            <td><%= c.getNumeroCuota() %></td>
                            <td>$<%= c.getMonto() %></td>
                            <td><%= c.getEstado() %></td>
                        </tr>
                        <input type="hidden" name="cuotas" value="<%= c.getIdCuota() %>">
                        <%
                                    }
                                }
                            }
                        %>
                    </tbody>
                </table>

                <p class="fw-bold">Total a pagar: $<%= total %></p>

                <div class="mb-3">
                    <label for="cuentaSeleccionada" class="form-label">Seleccionar cuenta</label>
                    <select name="nroCuenta" class="form-select" required <%= pagoExitoso ? "disabled" : "" %>>
                        <% if (cuentas != null && !cuentas.isEmpty()) { %>
                            <%
							    int i = 1; // inicializa el contador
							    for (Cuenta cuenta : cuentas) {
							%>
							    <option value="<%= cuenta.getNroCuenta() %>">
							        <%= i %> - <%= cuenta.getTipoCuenta() %> - CBU: <%= cuenta.getCbu() %>
							    </option>
							<%
							        i++; // incrementa el contador
							    }
							%>
                        <% } else { %>
                            <option disabled>No hay cuentas disponibles</option>
                        <% } %>
                    </select>
                </div>

                <div class="d-flex justify-content-between">
                    <% if (!pagoExitoso) { %>
                        <button type="submit" class="btn btn-success">Confirmar Pago</button>
                    <% } %>

                    <button type="button" class="btn btn-secondary" onclick="window.location.href='ServletPagoCuotas'">Volver</button>
                </div>
            </form>
            <% } else { %>
            <div class="text-center">
                
                <button type="button" class="btn btn-secondary" onclick="window.location.href='ServletPagoCuotas'">Volver al listado</button>
            </div>
            <% } %>

        </div>
    </div>
</div>

<!-- Bootstrap Bundle JS (incluye Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

</body>
</html>
