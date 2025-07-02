package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.ClienteDaoImpl;
import dominio.Cuenta;
import dominio.Cliente;
import dominio.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletVerCuentas")
public class ServletVerCuentas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        
        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        Cliente cliente = clienteDao.obtenerPorIdUsuario(usuario.getIdUsuario());

        if (cliente == null) {
            
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        int idCliente = cliente.getIdCliente();

        CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
        List<Cuenta> cuentas = cuentaDao.listarPorCliente(idCliente);

        request.setAttribute("cuentas", cuentas);
        request.getRequestDispatcher("/jsp/cliente/verCuentas.jsp").forward(request, response);
    }
}