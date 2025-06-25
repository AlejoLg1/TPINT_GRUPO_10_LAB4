package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

@WebServlet("/ServletBajaUsuario")
public class ServletBajaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletBajaUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
		int id = Integer.parseInt(request.getParameter("id"));

		UsuarioDao dao = new UsuarioDaoImpl();
		Usuario u = new Usuario();
		u.setIdUsuario(id);
		u.setEstado(false);

		dao.Eliminar(u);

		response.sendRedirect(request.getContextPath() + "/ServletListarUsuario");
	}

}
