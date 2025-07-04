package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dominio.Usuario;
import negocio.UsuarioNegocio;
import negocioImpl.UsuarioNegocioImpl;

@WebServlet("/ServletActivarUsuario")
public class ServletActivarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletActivarUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    Usuario usuario = (Usuario) session.getAttribute("usuario");

	    AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

	    if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
	        response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
	        return;
	    }

	    try {
	        int id = Integer.parseInt(request.getParameter("id"));
	        UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
	        usuarioNegocio.activarUsuario(id);
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    response.sendRedirect(request.getContextPath() + "/ServletListarUsuario");
	}


}
