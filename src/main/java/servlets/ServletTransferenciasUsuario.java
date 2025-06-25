package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Usuario;
import excepciones.CuentaExistenteExcenption;
import excepciones.MontoInsuficienteException;

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
		HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        
        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        Cliente cliente = clienteDao.obtenerPorIdUsuario(usuario.getIdUsuario());

        if (cliente == null) {
            
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        int idCliente = cliente.getIdCliente();

        CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
        List<Cuenta> cuentas = cuentaDao.listarPorCliente(idCliente);

        request.setAttribute("listaCuentas", cuentas);
        request.getRequestDispatcher("/jsp/cliente/transferencias.jsp").forward(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if (request.getParameter("btnTransferir") != null) {
		    boolean status = false;

		    try {
		        String nroCuenta = request.getParameter("cuentaOrigen") == null ? "" : request.getParameter("cuentaOrigen");
		        String cbuDestino = request.getParameter("cbuDestino") == null ? "" : request.getParameter("cbuDestino");
		        String monto = request.getParameter("monto") == null ? "" : request.getParameter("monto");
		        BigDecimal montoDecimal = new BigDecimal(monto.trim());

		        if (nroCuenta.trim().isEmpty() || monto.trim().isEmpty() || cbuDestino.trim().isEmpty()) {
		            throw new IllegalArgumentException();
		        }

		        Cuenta cuentaOrigen = ObtenerCuentaOrigen(Integer.parseInt(nroCuenta.trim()), montoDecimal);
		        Cuenta cuentaDestino = ObtenerCuentaDestino(cbuDestino);

		        int idMovSalida = RegistrarMovimiento(cuentaOrigen, montoDecimal, 1);
		        int idMovEntrada = RegistrarMovimiento(cuentaDestino, montoDecimal, 2);

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
		    return; // ⬅️ Asegura que no siga al doGet
		}

		doGet(request, response);
	}
	
	private Cuenta ObtenerCuentaOrigen(int idCuenta, BigDecimal monto) throws CuentaExistenteExcenption, MontoInsuficienteException
	{
		dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();
		Cuenta cuenta = null;
		
		try {
			cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
			
			if(cuenta == null || cuenta.isEstado() == false)
				throw new CuentaExistenteExcenption("No se encontro la cuenta con id: " + idCuenta);
			
			if(cuenta.getSaldo().compareTo(monto) == -1) 
				throw new MontoInsuficienteException("Monto insuficiente en la cuenta");
			
	    } catch (CuentaExistenteExcenption | MontoInsuficienteException e) {
	        throw e; 
	    } catch (Exception e) {
	        throw new CuentaExistenteExcenption("Error al obtener datos de la cuenta origen");
	    }
		
		return cuenta;
	}

	private Cuenta ObtenerCuentaDestino(String cbu) throws CuentaExistenteExcenption
	{
		dao.CuentaDao cuentaDao = new daoImpl.CuentaDaoImpl();
		Cuenta cuenta = null;
		
		try {
			cuenta = cuentaDao.obtenerCuentaPorCBU(cbu);
			
			if(cuenta == null || cuenta.isEstado() == false)
				throw new CuentaExistenteExcenption("No se encontro la cuenta con CBU: " + cbu);
			
	    } catch (Exception e) {
	    	throw new CuentaExistenteExcenption("Error al obtener datos de la cuenta Destino");
	    }
		
		return cuenta;
	}

	private int RegistrarMovimiento(Cuenta cuenta, BigDecimal monto, int tipoMov)
	{
		
		// registrar mov en la db
		return 1;
	}
}
