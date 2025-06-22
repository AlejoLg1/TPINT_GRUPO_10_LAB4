package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

import java.io.IOException;

@WebServlet("/ServletAltaUsuario")
public class ServletAltaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

    public ServletAltaUsuario() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		response.sendRedirect(request.getContextPath() + "/jsp/admin/altaUsuario.jsp");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		if (request.getParameter("btnAltaUsuario") != null) {
			boolean status = false;

			try {
				String tipoUser = request.getParameter("tipoUser").isEmpty() ? null : request.getParameter("tipoUser");
				String username = request.getParameter("username").isEmpty() ? null : request.getParameter("username");
				String pass = request.getParameter("pass").isEmpty() ? null : request.getParameter("pass");
				String passRepetida = request.getParameter("passRepetida").isEmpty() ? null : request.getParameter("passRepetida");

				// Validación de campos obligatorios
				if (tipoUser == null || username == null || pass == null || passRepetida == null) {
					throw new IllegalArgumentException("Campos obligatorios vacíos.");
				}

				// Validar que las contraseñas coincidan
				if (!pass.equals(passRepetida)) {
					throw new IllegalArgumentException("Las contraseñas no coinciden.");
				}

				// Crear el objeto Usuario
				Usuario nuevoUsuario = new Usuario();
				nuevoUsuario.setNombreUsuario(username);
				nuevoUsuario.setClave(pass);
				nuevoUsuario.setTipo("BANCARIO"); // "BANCARIO" o "CLIENTE"
				nuevoUsuario.setIsAdmin(tipoUser.equals("Admin"));
				nuevoUsuario.setEstado(true);   // por defecto activo

				// Insertar en la base de datos
				UsuarioDao dao = new UsuarioDaoImpl();
				status = dao.Agregar(nuevoUsuario);

			} catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
			rd.forward(request, response);
			return;
		}

		doGet(request, response);
	}
}
