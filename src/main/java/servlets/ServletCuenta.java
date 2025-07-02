package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dominio.Usuario;


@WebServlet("/ServletCuenta")
public class ServletCuenta extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletCuenta() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
    	String busqueda = request.getParameter("busqueda");
    	String tipoCuenta = request.getParameter("tipoCuenta");
    	String saldoMinStr = request.getParameter("saldoMin");
    	String saldoMaxStr = request.getParameter("saldoMax");

    	java.math.BigDecimal saldoMin = null;
    	java.math.BigDecimal saldoMax = null;

    	try {
    	    if (saldoMinStr != null && !saldoMinStr.isEmpty()) {
    	        saldoMin = new java.math.BigDecimal(saldoMinStr);
    	    }
    	    if (saldoMaxStr != null && !saldoMaxStr.isEmpty()) {
    	        saldoMax = new java.math.BigDecimal(saldoMaxStr);
    	    }
    	} catch (NumberFormatException e) {
    	    e.printStackTrace();
    	}

    	
    	dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();
    	java.util.List<Object[]> listaCuentas = cuentaDao.filtrarCuentas(busqueda, tipoCuenta, saldoMin, saldoMax);

        
        String errorCuenta = (String) request.getSession().getAttribute("errorCuenta");
        if (errorCuenta != null) {
            request.setAttribute("errorCuenta", errorCuenta);
            request.getSession().removeAttribute("errorCuenta");
        }

        request.setAttribute("listaCuentas", listaCuentas);
        request.getRequestDispatcher("/jsp/admin/cuentas.jsp").forward(request, response);
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
