package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.*;
import negocioImpl.AutenticacionNegocioImpl;
import daoImpl.MovimientoDaoImpl;
import dominio.Movimiento;
import dominio.Usuario;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletMovimientos")
public class ServletMovimientos extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String nroCuentaParam = request.getParameter("nroCuenta");
        if (nroCuentaParam == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Número de cuenta no especificado.");
            return;
        }

        int nroCuenta;
        try {
            nroCuenta = Integer.parseInt(nroCuentaParam);
        } catch (NumberFormatException e) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST, "Número de cuenta inválido.");
            return;
        }

        MovimientoDaoImpl movimientoDao = new MovimientoDaoImpl();
        List<Movimiento> movimientos = movimientoDao.listarPorCuenta(nroCuenta);

        request.setAttribute("movimientos", movimientos);
        request.setAttribute("nroCuenta", nroCuenta);
        request.getRequestDispatcher("/jsp/cliente/movimientos.jsp").forward(request, response);
    }
}

