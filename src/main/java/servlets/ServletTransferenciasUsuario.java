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
        dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();

        java.util.List<Object[]> listaCuentas = cuentaDao.listarConDatos();

        request.setAttribute("listaCuentas", listaCuentas);
        request.getRequestDispatcher("/jsp/cliente/transferencias.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("btnTransferir") != null)	// obtener datos de transferencia
		{
			boolean status = false;
			
			try {
			    String idCuenta = request.getParameter("cuentaOrigen") == null ? "" : request.getParameter("cuentaOrigen");
			    String cbu = request.getParameter("cbuDestino") == null ? "" : request.getParameter("cbuDestino");
			    String monto = request.getParameter("monto") == null ? "" : request.getParameter("monto");

			    // Validación de vacíos
			    if (idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cbu.trim().isEmpty()) {
			        throw new IllegalArgumentException();
			    }



			    
			    status = true;

			} catch (Exception e) {
			    status = false;
			    e.printStackTrace(); // Útil para depurar
			}

			
			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/cliente/transferencias.jsp");
			rd.forward(request, response);
		}
		
		doGet(request, response);	// para recargar la lista de cuentas
	}

}
