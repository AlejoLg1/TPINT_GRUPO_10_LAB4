package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dao.ClienteDao;
import dao.UsuarioDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.UsuarioDaoImpl;
import dominio.Cliente;
import dominio.Usuario;


@WebServlet("/ServletActivarCliente")
public class ServletActivarCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletActivarCliente() {
        super();
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

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
		
        try {
            int idCliente = Integer.parseInt(request.getParameter("id"));

            ClienteDao dao = new ClienteDaoImpl();
    		UsuarioDao daoUsuario = new UsuarioDaoImpl();
    		
    		Cliente c = new Cliente();
    		Usuario u = daoUsuario.obtenerPorIdCliente(idCliente);
    		
    		c.setIdCliente(idCliente);
    		c.setEstado(false);
    		
            dao.Activar(c, u.getIdUsuario());

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/ServletListarCliente");
    }

}
