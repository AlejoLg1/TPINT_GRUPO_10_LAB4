package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Usuario;


@WebServlet("/ServletMisDatosCliente")
public class ServletMisDatosCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletMisDatosCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessionSS = request.getSession(false);
		Usuario usuarioSS = (Usuario) sessionSS.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuarioSS == null || !auth.validarRolCliente(usuarioSS)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
		doGet(request, response);
	}

}
