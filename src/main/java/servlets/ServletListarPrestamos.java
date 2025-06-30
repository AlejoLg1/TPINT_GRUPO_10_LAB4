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

import dao.PrestamoDao;
import daoImpl.PrestamoDaoImpl;
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
        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
            return;
        }

        // Obtener filtros desde request
        String busqueda = request.getParameter("busqueda");

        Double montoMin = null;
        try {
            String montoMinStr = request.getParameter("montoMin");
            if (montoMinStr != null && !montoMinStr.isEmpty()) {
                montoMin = Double.parseDouble(montoMinStr);
            }
        } catch (NumberFormatException e) {
           throw e;
        }

        Double montoMax = null;
        try {
            String montoMaxStr = request.getParameter("montoMax");
            if (montoMaxStr != null && !montoMaxStr.isEmpty()) {
                montoMax = Double.parseDouble(montoMaxStr);
            }
        } catch (NumberFormatException e) {
        	throw e;
        }

        String estado = request.getParameter("estadoPrestamo");
        String fechaSolicitud = request.getParameter("fechaSolicitud");

        PrestamoDao dao = new PrestamoDaoImpl();
        List<Prestamo> listaPrestamos = dao.ListarConFiltros(busqueda, montoMin, montoMax, estado, fechaSolicitud);

        request.setAttribute("prestamos", listaPrestamos);
        request.getRequestDispatcher("/jsp/admin/prestamos.jsp").forward(request, response);
    }



	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
