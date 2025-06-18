<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
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
    <title>Crear Cliente - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/reportesEstilos.css">
</head>
<body>

<% request.setAttribute("activePage", "reportes"); %>
<%@ include file="navbarAdmin.jsp" %>

<div class="main-container">
    <div class="welcome-card">
        <h1>Reportes</h1>

        <form action="${pageContext.request.contextPath}/ServletReportes" method="post">
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
    </div>
    <div class="reportes-generados">
	    <h3>Reportes Generados</h3>
	    <p class="texto-placeholder">Aquí aparecerán los reportes generados una vez que estén disponibles.</p>
	    <table class="tabla-reportes" style="display: none;">
	        <thead>
	            <tr>
	                <th>Nombre del Reporte</th>
	                <th>Descargar</th>
	            </tr>
	        </thead>
	        <tbody>
	            <!-- Acá se cargarán los reportes en el futuro -->
	        </tbody>
	    </table>
	</div>
</div>

<%@ include file="../comunes/footer.jsp" %>
</body>
</html>
