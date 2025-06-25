<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="dominio.Prestamo" %>
<%@ page import="dominio.Cliente" %>
<%@ page import="dominio.Cuenta" %>
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
    <title>Prestamos - Banco UTN</title>
    
    <!-- Bootstrap 5 CSS -->
    <link href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/css/bootstrap.min.css" rel="stylesheet">

    <!-- DataTables Bootstrap 5 CSS -->
    <link rel="stylesheet" href="https://cdn.datatables.net/1.13.6/css/dataTables.bootstrap5.min.css">
    
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/aprobacionPrestamosEstilos.css">
</head>

<body>
	<% request.setAttribute("activePage", "prestamos"); %>
	<%@ include file="navbarAdmin.jsp" %>

	<div class="main-container">
        <div class="welcome-card">
            <h1>Aprobación de Prestamos</h1>

            <!-- Filtros y búsqueda -->
            <form action="ServletAprobacionPrestamos" method="get" class="filtros-form">
                <label>Buscar por cliente o CBU:</label>
                <input type="text" name="busqueda" placeholder="Nombre o CBU">

                <label>Monto desde:</label>
                <input type="number" name="montoMin" step="0.01">

                <label>Monto hasta:</label>
                <input type="number" name="MontoMax" step="0.01">
                <br>
                
                <label>Estado: </label>
                <select name="estadoPrestamo">
                    <option value="">-- Todos --</option>
                    <option value="Caja de ahorro">Aprobado</option>
                    <option value="Cuenta corriente">No aprobado</option>                    
                </select>
                
                <label>Fecha de solicitud: </label>
                <input type="date" name="fechaSolicitud">

                <button type="submit" class="boton-filtrar">Filtrar</button>
            </form>

            <!-- Tabla de prestamos -->
            <form action="ServletAprobacionPrestamos" method="get"> 
	            <table class="tabla-prestamos">
	                <thead>
	                    <tr>
	                        <th>Cliente</th>
	                        <th>Cuenta Destino</th>
	                        <th>Tipo de cuenta</th>
	                        <th>Fecha de solicitud</th>
	                        <th>Monto</th>                    
	                        <th>Cantidad de cuotas</th>
	                        <th>Estado</th>
	                        <th>Acciones</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <%
	                        List<Prestamo> listaPrestamos = (List<Prestamo>) request.getAttribute("prestamos"); 
	                        if (listaPrestamos != null) {
	                            for (Prestamo p : listaPrestamos) {
                    	%>
	                    <tr>
	                        <td><%= p.get_cliente().getNombreCompleto() %></td>
	                        <td><%= p.get_cuenta().getNroCuenta()%></td>
	                        <td><%= p.get_cuenta().getTipoCuenta()%></td>
	                        <td><%= p.getFecha()%></td>
	                        <td><%= p.getImporte_solicitado()%></td>
	                        <td><%= p.getCantidad_cuotas()%></td>
	                        <td><%= p.getEstado() %></td>
	                        <td>
	                            <div class="acciones-botones">
	    							<button type="submit" class="boton-aprobar" onclick="return confirm('¿Seguro que desea aprobar este prestamo?')">Aprobar</button>
	    							<button type="submit" class="boton-rechazar" onclick="return confirm('¿Seguro que desea rechazar este prestamo?')">Rechazar</button>
	 							</div>
	                        </td>
	                    </tr>
	                    <%
                            	}
                        	}
                    	%>
	                </tbody>
	            </table>
            </form>
        </div>
    </div>

<!-- jQuery -->
  <script src="https://code.jquery.com/jquery-3.7.0.min.js"></script>

  <!-- DataTables JS -->
  <script src="https://cdn.datatables.net/1.13.6/js/jquery.dataTables.min.js"></script>
  <script src="https://cdn.datatables.net/1.13.6/js/dataTables.bootstrap5.min.js"></script>

  <!-- Configuración de idioma ES para DataTables -->
  <script src="${pageContext.request.contextPath}/js/datatables-es.js"></script>
  
<!-- Bootstrap Bundle JS (incluye Popper) -->
<script src="https://cdn.jsdelivr.net/npm/bootstrap@5.3.2/dist/js/bootstrap.bundle.min.js"></script>

  <script>
      $(document).ready(function () {
          $('.tabla-prestamos').DataTable({
              language: spanishLanguageSettings,
              pageLength: 5,
              lengthMenu: [5, 10, 25, 50],
              responsive: true
          });
      });
  </script>

	<%@ include file="../comunes/footer.jsp" %>
</body>
</html>