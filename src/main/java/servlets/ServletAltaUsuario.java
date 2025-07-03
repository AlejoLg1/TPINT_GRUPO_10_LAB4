package servlets;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;

import java.io.IOException;

import dominio.Usuario;
import negocio.UsuarioNegocio;
import negocioImpl.UsuarioNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;

@WebServlet("/ServletAltaUsuario")
public class ServletAltaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletAltaUsuario() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
	        throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    Usuario usuario = (Usuario) session.getAttribute("usuario");

	    AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

	    if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
	        response.sendRedirect(request.getContextPath() + "/ServletLogin");
	        return;
	    }

	    RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
	    dispatcher.forward(request, response);
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

	    if (request.getParameter("btnUsuario") != null) {
	        boolean status = false;
	        String msg = "✅ Usuario registrado correctamente ";

	        String tipoUser = request.getParameter("tipoUser");
	        String username = request.getParameter("username");
	        String pass = request.getParameter("pass");
	        String passRepetida = request.getParameter("passRepetida");

	        UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();

	        try {
	            status = usuarioNegocio.altaUsuario(tipoUser, username, pass, passRepetida, usuario);
	        } catch (Exception e) {
	            status = false;
	            msg = "❌ " + e.getMessage();
	            e.printStackTrace();
	        }

	        request.setAttribute("estado", status);
	        request.setAttribute("mensaje", msg);
	        RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
	        rd.forward(request, response);
	        return;
	    }

	    doGet(request, response);
	}

}
