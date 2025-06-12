package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ServletTransferenciasUsuario
 */
@WebServlet("/ServletTransferenciasUsuario")
public class ServletTransferenciasUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletTransferenciasUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// Cargar cuentas del usuario desde la db
		
		response.sendRedirect(request.getContextPath() + "/jsp/cliente/transferencias.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("btnTransferir") != null)
		{
			boolean status = false;
			
			try {
			    String idCuenta = request.getParameter("cuentaOrigen");
			    String cbu = request.getParameter("cbuDestino");
			    String monto = request.getParameter("monto");

			    // Validación de nulos o vacíos
			    if (idCuenta == null || monto == null || cbu == null ||
			        idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cbu.trim().isEmpty()) {
			        throw new IllegalArgumentException();
			    }

			    // Registrar solicitud en la db
			    
			    status = true;

			} catch (Exception e) {
			    status = false;
			    e.printStackTrace(); // Útil para depurar
			}

			
			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/cliente/transferencias.jsp");
			rd.forward(request, response);
		}
		
		doGet(request, response);
	}

}
