package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import negocioImpl.AutenticacionNegocioImpl;
import dominio.Usuario;
import negocio.UsuarioNegocio;
import negocioImpl.UsuarioNegocioImpl;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletListarUsuario")
public class ServletListarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	public ServletListarUsuario() {
		super();
	}

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    Usuario usuario = (Usuario) session.getAttribute("usuario");

	    AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

	    if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
	        response.sendRedirect(request.getContextPath() + "/ServletLogin");
	        return;
	    }

	    String nombreUsuario = request.getParameter("usuario");
	    String rol = request.getParameter("rol");
	    String estado = request.getParameter("estado");

	    UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
	    List<Usuario> listaUsuarios = usuarioNegocio.listarConFiltros(nombreUsuario, rol, estado);

	    request.setAttribute("usuarios", listaUsuarios);
	    request.getRequestDispatcher("/jsp/admin/usuarios.jsp").forward(request, response);
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
		doGet(request, response);
	}
}
