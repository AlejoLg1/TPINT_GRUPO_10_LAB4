package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dominio.Cuenta;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;

@WebServlet("/ServletActivarCuenta")
public class ServletActivarCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletActivarCuenta() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    int idCuenta = Integer.parseInt(request.getParameter("id"));
	    CuentaDao cuentaDao = new CuentaDaoImpl();

	    int idCliente = cuentaDao.obtenerIdClientePorCuenta(idCuenta);
	    int cuentasActivas = cuentaDao.contarCuentasActivasPorCliente(idCliente);

	    if (cuentasActivas >= 3) {
	        request.getSession().setAttribute("errorCuenta", "El cliente ya tiene 3 cuentas activas. No se puede activar otra.");
	        response.sendRedirect(request.getContextPath() + "/ServletCuenta");
	        return;
	    }

	    cuentaDao.cambiarEstado(idCuenta, true);
	    response.sendRedirect(request.getContextPath() + "/ServletCuenta");
	}


}
