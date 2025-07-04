package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import java.util.List;

import negocio.PrestamoNegocio;
import negocioImpl.PrestamoNegocioImpl;
import dominio.Prestamo;
import dominio.Usuario;


@WebServlet("/ServletListarPrestamos")
public class ServletListarPrestamos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletListarPrestamos() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        // Obtener filtros desde request
        String busqueda = request.getParameter("busqueda");        
        String montoMinStr = request.getParameter("montoMin");        
        String montoMaxStr = request.getParameter("montoMax");            
        String estado = request.getParameter("estadoPrestamo");
        String fechaSolicitud = request.getParameter("fechaSolicitud");
        
        //se pasa info al negocio para conseguir el listado
        PrestamoNegocio negocio = new PrestamoNegocioImpl();
        List<Prestamo> listaPrestamos = negocio.ListarPrestamos(busqueda, montoMinStr, montoMaxStr, estado, fechaSolicitud);

        request.setAttribute("prestamos", listaPrestamos);
        request.getRequestDispatcher("/jsp/admin/prestamos.jsp").forward(request, response);
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
