package servlets;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
 
import dao.ReporteDao;
import daoImpl.ReporteDaoImpl;
import dominio.Reporte;
import dominio.Usuario;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
 
@WebServlet("/ServletReporte")
public class ServletReporte extends HttpServlet {
 
    private ReporteDao reporteDAO = new ReporteDaoImpl();
 
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
        request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
    }
 
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
        String tipoReporte = request.getParameter("tipoReporte");
        String inicioPeriodoStr = request.getParameter("inicioPeriodo");
        String finPeriodoStr = request.getParameter("finPeriodo");
 
        Date inicioPeriodo = null;
        Date finPeriodo = null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
 
        try {
            if (inicioPeriodoStr != null && !inicioPeriodoStr.isEmpty()) {
                inicioPeriodo = sdf.parse(inicioPeriodoStr);
            }
            if (finPeriodoStr != null && !finPeriodoStr.isEmpty()) {
                finPeriodo = sdf.parse(finPeriodoStr);
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Formato de fecha inválido.");
            request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
            return;
        }
 
        if (tipoReporte == null || tipoReporte.trim().isEmpty()) {
            request.setAttribute("error", "Debe seleccionar un tipo de reporte.");
            request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
            return;
        }
 
        try {
            List<Reporte> listaReportes = reporteDAO.generarReporte(tipoReporte, inicioPeriodo, finPeriodo);
 
            request.setAttribute("reportes", listaReportes);
            request.setAttribute("titulo", tipoReporte);
 
            // Forward a la misma JSP donde está el formulario y los resultados
            request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
 
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al generar el reporte.");
            request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
        }
    }
}