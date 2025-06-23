package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;


@WebServlet("/ServletCuenta")
public class ServletCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletCuenta() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();

        java.util.List<Object[]> listaCuentas = cuentaDao.listarConDatos();
        
        String errorCuenta = (String) request.getSession().getAttribute("errorCuenta");
        if (errorCuenta != null) {
            request.setAttribute("errorCuenta", errorCuenta);
            request.getSession().removeAttribute("errorCuenta");
        }

        request.setAttribute("listaCuentas", listaCuentas);
        request.getRequestDispatcher("/jsp/admin/cuentas.jsp").forward(request, response);
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
