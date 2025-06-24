package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Usuario;

/**
 * Servlet implementation class ServletMisDatosCliente
 */
@WebServlet("/ServletMisDatosCliente")
public class ServletMisDatosCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletMisDatosCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        Cliente cliente = clienteDao.obtenerPorIdUsuario(usuario.getIdUsuario());

        if (cliente == null) {
            
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        request.setAttribute("infoCliente", cliente);
        request.getRequestDispatcher("/jsp/cliente/misDatos.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
