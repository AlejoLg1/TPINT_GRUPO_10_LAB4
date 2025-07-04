package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocio.ClienteNegocio;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.ClienteNegocioImpl;

import java.io.IOException;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Usuario;
import excepciones.ClienteNoVinculadoException;


@WebServlet("/ServletMisDatosCliente")
public class ServletMisDatosCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletMisDatosCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
        ClienteNegocio clienteNeg = new ClienteNegocioImpl();
        Cliente cliente = null;
        
        try {
		
        	cliente = clienteNeg.obtenerPorIdUsuario(usuario.getIdUsuario());
        	
		} catch (ClienteNoVinculadoException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
        

        
        request.setAttribute("infoCliente", cliente);

        if (cliente == null)
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
        else 
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
