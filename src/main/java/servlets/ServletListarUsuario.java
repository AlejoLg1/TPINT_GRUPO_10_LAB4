package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletListarUsuario")
public class ServletListarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletListarUsuario() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		UsuarioDao dao = new UsuarioDaoImpl();
		List<Usuario> listaUsuarios = dao.Listar();

		request.setAttribute("usuarios", listaUsuarios);
		request.getRequestDispatcher("/jsp/admin/usuarios.jsp").forward(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doGet(request, response);
	}
}
