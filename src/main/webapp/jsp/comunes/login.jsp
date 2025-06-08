<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <title>Login - Banco UTN</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/loginEstilos.css">
</head>
<body>
    <div class="login-container">
        <h2>Iniciar Sesión</h2>

        <form action="ServletLogin" method="post">
            <label for="usuario">Usuario:</label>
            <input type="text" id="usuario" name="usuario" required>

            <label for="clave">Contraseña:</label>
            <input type="password" id="clave" name="clave" required>

            <button type="submit">Ingresar</button>
        </form>

        <%-- Mostrar mensaje de error si lo hay --%>
        <div class="error-message">
            <%= request.getAttribute("errorLogin") != null ? request.getAttribute("errorLogin") : "" %>
        </div>
    </div>
</body>
</html>
