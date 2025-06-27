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
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Usuario;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;


@WebServlet("/ServletBajaCliente")
public class ServletBajaCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletBajaCliente() {
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
		
		int idCliente = Integer.parseInt(request.getParameter("id"));
		
		ClienteDao dao = new ClienteDaoImpl();
		UsuarioDao daoUsuario = new UsuarioDaoImpl();
		
		Cliente c = new Cliente();
		Usuario u = daoUsuario.obtenerPorIdCliente(idCliente);
		
		c.setIdCliente(idCliente);
		c.setEstado(false);
		System.out.println("ID USUARIO: " + u.getIdUsuario());

		dao.Eliminar(c, u.getIdUsuario());
		
		
		
		response.sendRedirect(request.getContextPath() + "/ServletListarCliente");
	}

}
