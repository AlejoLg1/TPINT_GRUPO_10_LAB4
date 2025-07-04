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
import negocio.CuentaNegocio;
import negocioImpl.CuentaNegocioImpl;


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


	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    HttpSession session = request.getSession(false);
	    Usuario usuario = (Usuario) session.getAttribute("usuario");

	    AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

	    if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
	        response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
	        return;
	    }

	    int idCuenta = Integer.parseInt(request.getParameter("id"));
	    CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();

	    try {
	        cuentaNegocio.activarCuenta(idCuenta);
	    } catch (Exception e) {
	        request.getSession().setAttribute("errorCuenta", "❌ " + e.getMessage());
	        e.printStackTrace();
	    }

	    response.sendRedirect(request.getContextPath() + "/ServletCuenta");
	}



}
