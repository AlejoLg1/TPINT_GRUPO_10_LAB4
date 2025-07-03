package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;


import dominio.Usuario;
import negocio.UsuarioNegocio;
import negocioImpl.UsuarioNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;

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

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
		
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    Usuario usuario = (Usuario) session.getAttribute("usuario");

	    AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

	    if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
	        response.sendRedirect(request.getContextPath() + "/ServletLogin");
	        return;
	    }

	    int id = Integer.parseInt(request.getParameter("id"));
	    UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();

	    try {
	        usuarioNegocio.bajaUsuario(id, usuario.getIdUsuario());
	        response.sendRedirect(request.getContextPath() + "/ServletListarUsuario");
	    } catch (Exception e) {
	        request.setAttribute("errorUsuario", e.getMessage());
	        request.getRequestDispatcher("/ServletListarUsuario").forward(request, response);
	    }
	}



}
