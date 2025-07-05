package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dominio.Usuario;
import dominio.Cliente;
import dominio.Cuenta;

import negocio.ClienteNegocio;
import negocio.CuentaNegocio;
import negocio.TransferenciaNegocio;

import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.ClienteNegocioImpl;
import negocioImpl.CuentaNegocioImpl;
import negocioImpl.TransferenciaNegocioImpl;

import excepciones.ClienteNoVinculadoException;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;

import java.io.IOException;
import java.util.List;

@WebServlet("/ServletTransferenciasUsuario")
public class ServletTransferenciasUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private final ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
	private final CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
	private final TransferenciaNegocio transferenciaNegocio = new TransferenciaNegocioImpl();

	@Override
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
		if (usuario == null || !auth.validarRolCliente(usuario)) {
			response.sendRedirect(request.getContextPath() + "/ServletLogin");
			return;
		}

		try {
			Cliente cliente = clienteNegocio.obtenerPorIdUsuario(usuario.getIdUsuario());
			List<Cuenta> cuentas = cuentaNegocio.obtenerCuentasPorCliente(cliente.getIdCliente());

			// Recuperar mensaje desde sesión
			String mensaje = (String) session.getAttribute("mensaje");
			Boolean estado = (Boolean) session.getAttribute("estado");

			if (mensaje != null) {
				request.setAttribute("mensaje", mensaje);
				request.setAttribute("estado", estado);
				session.removeAttribute("mensaje");
				session.removeAttribute("estado");
			}

			request.setAttribute("listaCuentas", cuentas);
			request.getRequestDispatcher("/jsp/cliente/transferencias.jsp").forward(request, response);

		} catch (ClienteNoVinculadoException e) {
			response.sendRedirect(request.getContextPath() + "/ServletLogin");
		} catch (Exception e) {
			e.printStackTrace();
			request.setAttribute("mensaje", "❌ Error al cargar las cuentas del cliente.");
			request.setAttribute("estado", false);
			request.getRequestDispatcher("/jsp/cliente/transferencias.jsp").forward(request, response);
		}
	}

	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
		if (usuario == null || !auth.validarRolCliente(usuario)) {
			response.sendRedirect(request.getContextPath() + "/ServletLogin");
			return;
		}

		String msg = "✅ Operación realizada con éxito";
		boolean status = false;

		if (request.getParameter("btnTransferir") != null) {
			try {
				String nroCuentaOrigen = request.getParameter("cuentaOrigen");
				String cbuDestino = request.getParameter("cbuDestino");
				String monto = request.getParameter("monto");

				status = transferenciaNegocio.transferirDesdeFormulario(nroCuentaOrigen, cbuDestino, monto);
			} catch (TransferenciaException | MovimientoException e) {
				msg = "❌ " + e.getMessage();
			} catch (Exception e) {
				e.printStackTrace();
				msg = "❌ Ocurrió un error al realizar la transferencia.";
			}
		}

		// Guardar mensaje en sesión para mostrar en el GET
		session.setAttribute("mensaje", msg);
		session.setAttribute("estado", status);
		response.sendRedirect(request.getContextPath() + "/ServletTransferenciasUsuario");
	}
}