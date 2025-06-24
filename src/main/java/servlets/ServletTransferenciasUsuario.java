package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import dominio.Cuenta;
import dominio.Usuario;
import excepciones.CuentaExistenteExcenption;

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
			    String cbuDestino = request.getParameter("cbuDestino") == null ? "" : request.getParameter("cbuDestino");
			    String monto = request.getParameter("monto") == null ? "" : request.getParameter("monto");

			    // Validación de vacíos
			    if (idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cbuDestino.trim().isEmpty()) {
			        throw new IllegalArgumentException();
			    }
		    	
			    //obtener datos de la cuenta origen
			    Cuenta cuentaOrigen = ObtenerCuenta(Integer.parseInt(idCuenta.trim()));
			    
			    //validar el cbu de destino
			    //validar monto disponible
			    
			    //registrar transferencia
			    
			    status = true;
			} catch (CuentaExistenteExcenption e) {
				// enviar mensaje al jsp
			
			} catch (Exception e) {
			    status = false;
			    e.printStackTrace(); 
			}

			
			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/cliente/transferencias.jsp");
			rd.forward(request, response);
		}
		
		doGet(request, response);	// para recargar la lista de cuentas
	}
	
	private Cuenta ObtenerCuenta(int idCuenta) throws CuentaExistenteExcenption
	{
		dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();
		Cuenta cuenta = null;
		
		try {
			cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
			
			if(cuenta == null)
				throw new CuentaExistenteExcenption("No se encontro la cuenta: " + idCuenta);
			
		} catch (Exception e) {
			throw new CuentaExistenteExcenption("Error al obtener datos de la cuenta origen");
		}
		
		return cuenta;
	}

}
