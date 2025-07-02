package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;

import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Usuario;
import dominio.Transferencia;
import negocio.TransferenciaNegocio;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.TransferenciaNegocioImpl;
import excepciones.CuentaExistenteExcenption;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;


@WebServlet("/ServletTransferenciasUsuario")
public class ServletTransferenciasUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletTransferenciasUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
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

        // Recuperar mensajes desde sesion (si existen)
        String mensaje = (String) session.getAttribute("mensaje");
        Boolean estado = (Boolean) session.getAttribute("estado");

        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("estado", estado);

            // Eliminar los atributos de sesiÃ³n para que no se repitan
            session.removeAttribute("mensaje");
            session.removeAttribute("estado");
        }
        
        
        request.setAttribute("listaCuentas", cuentas);
        request.getRequestDispatcher("/jsp/cliente/transferencias.jsp").forward(request, response);
        
	}

	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession sessionSS = request.getSession(false);
		Usuario usuario = (Usuario) sessionSS.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
        HttpSession session = request.getSession();
        String msg = "✅ Operacion realizada con exito";
        boolean status = false;

        if (request.getParameter("btnTransferir") != null) {
            TransferenciaNegocio transfNegImp = new TransferenciaNegocioImpl();
            Transferencia transf = new Transferencia();

            try {
                String nroCuentaOrigen = request.getParameter("cuentaOrigen") == null ? "" : request.getParameter("cuentaOrigen");
                String cbuDestino = request.getParameter("cbuDestino") == null ? "" : request.getParameter("cbuDestino");
                String monto = request.getParameter("monto") == null ? "" : request.getParameter("monto");

                if (nroCuentaOrigen.trim().isEmpty() || monto.trim().isEmpty() || cbuDestino.trim().isEmpty())
                    throw new IllegalArgumentException();
                
                if(nroCuentaOrigen.compareTo("invalid") == 0)
                	throw new TransferenciaException("Seleccione una cuenta de origen valida");

                double importe = Double.parseDouble(monto);
                int nroCuentaDestino = ObtenerNroCuentaDestino(cbuDestino);

                transf.setNroCuentaOrigen(Integer.parseInt(nroCuentaOrigen));
                transf.setNroCuentaDestino(nroCuentaDestino);
                transf.setImporte(importe);

                transfNegImp.registrarTransferencia(transf);
                status = true;

            } catch(TransferenciaException e) {
            	status = false;
            	msg = "❌ " + e.getMessage();
            } catch(MovimientoException e) {
            	status = false;
            	msg = "❌ " + e.getMessage();
            } catch (CuentaExistenteExcenption e) {
            	msg = "❌ " + e.getMessage();
            } catch (Exception e) {
                status = false;
                e.printStackTrace();
                msg = "❌ Ocurrio un error al realizar la transferencia.";
            }
        }

        // Guardar mensaje y estado en sesiÃ³n
        session.setAttribute("mensaje", msg);
        session.setAttribute("estado", status);

        // Redirigir a doGet (POST â†’ REDIRECT â†’ GET)
        response.sendRedirect(request.getContextPath() + "/ServletTransferenciasUsuario");
    }
	

	private int ObtenerNroCuentaDestino(String cbu) throws CuentaExistenteExcenption
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
		
		return cuenta.getNroCuenta();
	}

}
