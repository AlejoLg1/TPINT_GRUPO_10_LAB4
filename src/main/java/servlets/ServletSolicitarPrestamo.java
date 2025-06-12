package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ServletSolicitarPrestamo
 */
@WebServlet("/ServletSolicitarPrestamo")
public class ServletSolicitarPrestamo extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletSolicitarPrestamo() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		// Cargar cuentas del usuario desde la db
		
		response.sendRedirect(request.getContextPath() + "/jsp/cliente/solicitarPrestamo.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("btnSolicitar") != null)
		{
			boolean status = false;
			
			try {
			    String idCuenta = request.getParameter("cuentasCliente");
			    String monto = request.getParameter("monto");
			    String cuotas = request.getParameter("cuotas");

			    // Validación de nulos o vacíos
			    if (idCuenta == null || monto == null || cuotas == null ||
			        idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cuotas.trim().isEmpty()) {
			        throw new IllegalArgumentException();
			    }

			    // Registrar solicitud en la db
			    
			    status = true;

			} catch (Exception e) {
			    status = false;
			    e.printStackTrace(); // Útil para depurar
			}

			
			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/cliente/solicitarPrestamo.jsp");
			rd.forward(request, response);
		}
		
		doGet(request, response);
	}

}
