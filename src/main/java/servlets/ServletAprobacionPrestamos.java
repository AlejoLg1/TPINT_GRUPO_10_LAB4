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

import daoImpl.PrestamoDaoImpl;
import dao.PrestamoDao;
import dominio.Prestamo;
import dominio.Usuario;


@WebServlet("/ServletAprobacionPrestamos")
public class ServletAprobacionPrestamos extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletAprobacionPrestamos() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
    	
        PrestamoDao dao = new PrestamoDaoImpl(); 

        String busqueda = request.getParameter("busqueda"); 
        String montoMinStr = request.getParameter("montoMin");
        String montoMaxStr = request.getParameter("montoMax"); // 
        String estadoPrestamo = request.getParameter("estadoPrestamo"); 
        String fechaSolicitud = request.getParameter("fechaSolicitud"); 

        List<Prestamo> listaPrestamos = null;

        try {
            Double montoMin = null;
            if (montoMinStr != null && !montoMinStr.isEmpty()) {
                montoMin = Double.parseDouble(montoMinStr);
            }
            
            Double montoMax = null;
            if (montoMaxStr != null && !montoMaxStr.isEmpty()) {
                montoMax = Double.parseDouble(montoMaxStr);
            }

            
            listaPrestamos = dao.obtenerPrestamosFiltrados(busqueda, montoMin, montoMax, estadoPrestamo, fechaSolicitud);

            request.setAttribute("prestamos", listaPrestamos);

            request.setAttribute("busqueda", busqueda);
            request.setAttribute("montoMin", montoMinStr);
            request.setAttribute("montoMax", montoMaxStr);
            request.setAttribute("estadoPrestamo", estadoPrestamo);
            request.setAttribute("fechaSolicitud", fechaSolicitud);
            request.getRequestDispatcher("/jsp/admin/prestamos.jsp").forward(request, response);

        } catch (NumberFormatException e) {
            System.err.println("Error de formato de número en montos: " + e.getMessage());
            request.setAttribute("errorMessage", "Error en el formato de los montos ingresados.");
            request.getRequestDispatcher("/jsp/admin/prestamos.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("errorMessage", "Error al cargar los préstamos: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/prestamos.jsp").forward(request, response);
        }
    
    }


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
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
