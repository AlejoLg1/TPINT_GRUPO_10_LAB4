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

@WebServlet("/ServletBajaCuenta")
public class ServletBajaCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ServletBajaCuenta() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		System.out.println("Se llamó al ServletBajaCuenta");
		int idCuenta = Integer.parseInt(request.getParameter("id"));
		System.out.println("ID Recibido= "+ idCuenta);
        CuentaDao cuentaDao = new CuentaDaoImpl();
        boolean exito = cuentaDao.cambiarEstado(idCuenta, false); // false = inactiva
        System.out.println("Cambio de estado: " + exito);

        response.sendRedirect(request.getContextPath() + "/ServletCuenta");
    }

}
