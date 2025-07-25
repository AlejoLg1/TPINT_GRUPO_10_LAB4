package servlets;

import java.io.IOException;

import java.util.List;

import dominio.Cuenta;
import dominio.Cuota;
import dominio.Usuario;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.PagoNegocioImpl;

@WebServlet("/ServletConfirmarPagoCuotas")
public class ServletConfirmarPagoCuotas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
    private final PagoNegocioImpl pagoNegocio = new PagoNegocioImpl();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        Object idClienteObj = session.getAttribute("idCliente");
        if (idClienteObj == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
            return;
        }

        int idCliente = (int) idClienteObj;

        try {
            String[] cuotasSeleccionadas = request.getParameterValues("cuotas");
            if (cuotasSeleccionadas == null || cuotasSeleccionadas.length == 0) {
                response.sendRedirect("ServletPagoCuotas");
                return;
            }

            List<Cuota> cuotas = pagoNegocio.obtenerCuotasPorIds(cuotasSeleccionadas);
            List<Cuenta> cuentas = pagoNegocio.obtenerCuentasPorCliente(idCliente);

            request.setAttribute("cuotasSeleccionadas", cuotas);
            request.setAttribute("cuentasDisponibles", cuentas);
            request.getRequestDispatcher("jsp/cliente/confirmarPago.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Ocurrió un error al preparar el pago.");
        }
    }
}