package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocio.ClienteNegocio;
import negocio.CuentaNegocio;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.ClienteNegocioImpl;
import negocioImpl.CuentaNegocioImpl;
import dominio.Cuenta;
import dominio.Cliente;
import dominio.Usuario;
import java.io.IOException;
import java.util.List;

@WebServlet("/ServletVerCuentas")
public class ServletVerCuentas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        
        ClienteNegocio clienteNeg = new ClienteNegocioImpl();
        CuentaNegocio cuentaNeg = new CuentaNegocioImpl();
        List<Cuenta> cuentas = null;
        Cliente cliente = null;
        
        try {
			cliente = clienteNeg.obtenerPorIdUsuario(usuario.getIdUsuario());
			cuentas = cuentaNeg.obtenerCuentasPorCliente(cliente.getIdCliente());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
        
        
        request.setAttribute("cuentas", cuentas);
        
        if (cliente == null)
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
        else
        	request.getRequestDispatcher("/jsp/cliente/verCuentas.jsp").forward(request, response);
    }
}