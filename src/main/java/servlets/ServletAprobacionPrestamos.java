package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import daoImpl.PrestamoDaoImpl;
import dao.PrestamoDao;
import dominio.Prestamo;

/**
 * Servlet implementation class ServletAprobacionPrestamos
 */
@WebServlet("/ServletAprobacionPrestamos")
public class ServletAprobacionPrestamos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAprobacionPrestamos() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        
    }

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String idPrestamoStr = request.getParameter("idPrestamo");
        String accion = request.getParameter("accion"); 
        
        PrestamoDao dao = new PrestamoDaoImpl();

        if (idPrestamoStr != null && !idPrestamoStr.isEmpty() && accion != null && !accion.isEmpty()) {
            try {
                int idPrestamo = Integer.parseInt(idPrestamoStr);

                boolean operacionExitosa = false;
                String mensaje = "";

                if ("aprobar".equals(accion)) {
                    operacionExitosa = dao.aprobarPrestamo(idPrestamo);
                    if (operacionExitosa) {
                        mensaje = "Préstamo " + idPrestamo + " aprobado exitosamente.";
                    } else {
                        mensaje = "Error al aprobar el préstamo " + idPrestamo + ".";
                    }
                } else if ("rechazar".equals(accion)) {
                    operacionExitosa = dao.rechazarPrestamo(idPrestamo);
                    if (operacionExitosa) {
                        mensaje = "Préstamo " + idPrestamo + " rechazado exitosamente.";
                    } else {
                        mensaje = "Error al rechazar el préstamo " + idPrestamo + ".";
                    }
                } else {
                    mensaje = "Acción no reconocida.";
                }

                
                String status = operacionExitosa ? "success" : "error"; 
                response.sendRedirect(request.getContextPath() + "/ServletListarPrestamos?status=" + status + "&message=" + java.net.URLEncoder.encode(mensaje, "UTF-8"));


            } catch (NumberFormatException e) {
                System.err.println("ID de préstamo inválido: " + idPrestamoStr);
                response.sendRedirect(request.getContextPath() + "/ServletListarPrestamos?status=error&message=" + java.net.URLEncoder.encode("ID de préstamo inválido.", "UTF-8"));
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect(request.getContextPath() + "/ServletListarPrestamos?status=error&message=" + java.net.URLEncoder.encode("Error interno al procesar la solicitud.", "UTF-8"));
            }
        } else {
            response.sendRedirect(request.getContextPath() + "/ServletListarPrestamos?status=error&message=" + java.net.URLEncoder.encode("Parámetros incompletos para la acción.", "UTF-8"));
        }
    }

}
