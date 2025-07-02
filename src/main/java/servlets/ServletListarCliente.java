package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import java.util.List;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Usuario;


@WebServlet("/ServletListarCliente")
public class ServletListarCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletListarCliente() {
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
		
		ClienteDao dao = new ClienteDaoImpl();
		List<Cliente> listaCliente = dao.Listar2();

		request.setAttribute("clientes", listaCliente);
		request.getRequestDispatcher("/jsp/admin/clientes.jsp").forward(request, response);
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
