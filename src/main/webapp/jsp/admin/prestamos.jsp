<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
    
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
                    <option value="Cuenta corriente">Rechazado</option>
					<option value="Cuenta corriente">Solicita Aprobacion</option>
                    
                </select>
                
                <label>Fecha de solicitud: </label>
                <input type="date" name="fechaSolicitud">

                <button type="submit" class="boton-filtrar">Filtrar</button>
            </form>

            <!-- Tabla de cuentas -->
            <form action="ServletAprobacionPrestamos" method="get"> 
	            <table class="tabla-prestamos">
	                <thead>
	                    <tr>
	                        <th>Cliente</th>
	                        <th>Cuenta Destino</th>
	                        <th>Tipo de cuenta</th>
	                        <th>Fecha de solicitud</th>
	                        <th>Tipo</th>
	                        <th>Monto</th>
	                        <th>Tasa de Interes</th>                        
	                        <th>Cantidad de cuotas</th>
	                        <th>Estado</th>
	                        <th>Acciones</th>
	                    </tr>
	                </thead>
	                <tbody>
	                    <!-- Ejemplo de fila -->
	                    <tr>
	                        <td>Juan Pérez</td>
	                        <td>123456</td>
	                        <td>Caja de ahorro</td>
	                        <td>2025-06-12</td>
	                        <td>Prestamo hipotecario</td>
	                        <td>$100,000,000.00</td>
	                        <td>10%</td>
	                        <td>48</td>
	                        <td>Solicita Aprobacion</td>
	                        <td>
	                            <div class="acciones-botones">
	    							<button type="submit" class="boton-aprobar" onclick="return confirm('¿Seguro que desea aprobar este prestamo?')">Aprobar</button>
	    							<button type="submit" class="boton-rechazar" onclick="return confirm('¿Seguro que desea rechazar este prestamo?')">Rechazar</button>
	 							</div>
	                        </td>
	                    </tr>
	                </tbody>
	            </table>
            </form>
        </div>
    </div>


	<%@ include file="../comunes/footer.jsp" %>
</body>
</html>