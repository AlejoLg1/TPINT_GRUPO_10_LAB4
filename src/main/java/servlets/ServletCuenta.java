package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


import java.io.IOException;

import dominio.Usuario;
import negocio.CuentaNegocio;
import negocioImpl.CuentaNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;

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

        CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
        java.util.List<Object[]> listaCuentas = cuentaNegocio.filtrarCuentas(busqueda, tipoCuenta, saldoMinStr, saldoMaxStr);

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
