package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import negocioImpl.AutenticacionNegocioImpl;
import negocio.ReporteNegocio;
import negocioImpl.ReporteNegocioImpl;
import dominio.Reporte;
import dominio.Usuario;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

@WebServlet("/ServletReporte")
public class ServletReporte extends HttpServlet {

    private static final long serialVersionUID = 1L;

    private final ReporteNegocio reporteNegocio = new ReporteNegocioImpl();
    private final AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!usuarioValido(request)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        if (!usuarioValido(request)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String tipoReporte = request.getParameter("tipoReporte");
        String inicioPeriodoStr = request.getParameter("inicioPeriodo");
        String finPeriodoStr = request.getParameter("finPeriodo");

        try {
            Date inicio = parseFecha(inicioPeriodoStr);
            Date fin = parseFecha(finPeriodoStr);

            Object resultado = reporteNegocio.generarReporte(tipoReporte, inicio, fin);

            if (resultado instanceof Map) {
                @SuppressWarnings("unchecked")
                Map<String, List<Reporte>> mapResultado = (Map<String, List<Reporte>>) resultado;
                request.setAttribute("reportesResumen", mapResultado.get("resumenDeuda"));
                request.setAttribute("morosos", mapResultado.get("clientesMorosos"));
            } else if (resultado instanceof List) {
                @SuppressWarnings("unchecked")
                List<Reporte> lista = (List<Reporte>) resultado;
                request.setAttribute("reportes", lista);
            }

            request.setAttribute("titulo", tipoReporte);

        } catch (IllegalArgumentException e) {
            request.setAttribute("error", e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al generar el reporte.");
        }

        request.getRequestDispatcher("/jsp/admin/reportes.jsp").forward(request, response);
    }

    private boolean usuarioValido(HttpServletRequest request) {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");
        return usuario != null && (auth.validarRolAdmin(usuario) || auth.validarRolBancario(usuario));
    }

    private Date parseFecha(String fechaStr) {
        if (fechaStr == null || fechaStr.trim().isEmpty()) return null;
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(fechaStr);
        } catch (Exception e) {
            throw new IllegalArgumentException("Formato de fecha inválido.");
        }
    }
}
