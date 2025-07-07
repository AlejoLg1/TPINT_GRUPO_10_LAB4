package servlets;

import java.io.IOException;

import java.util.List;
import java.util.ArrayList;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpSession;

import dominio.Cuota;

import dominio.Usuario;
import dominio.Cuenta;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.PagoNegocioImpl;
import negocio.PagoNegocio;

@WebServlet("/ServletPagoCuotas")
public class ServletPagoCuotas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
    private final PagoNegocio pagoNegocio = new PagoNegocioImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
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

        // Filtros opcionales
        String idPrestamoStr = request.getParameter("idPrestamo");
        String fechaVencimiento = request.getParameter("fechaVencimiento");
        String estado = request.getParameter("estado");

        Integer idPrestamo = null;
        if (idPrestamoStr != null && !idPrestamoStr.isEmpty()) {
            idPrestamo = Integer.parseInt(idPrestamoStr);
        }

        try {
            List<Cuota> cuotas = pagoNegocio.obtenerCuotasPendientesConFiltros(idCliente, idPrestamo, fechaVencimiento, estado);
            request.setAttribute("cuotasPendientes", cuotas);
            request.getRequestDispatcher("jsp/cliente/pagarCuotas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener cuotas");
        }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Object idClienteObj = session.getAttribute("idCliente");

        if (idClienteObj == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
            return;
        }

        int idCliente = (int) idClienteObj;

        String idCuotaStr = request.getParameter("cuotas");
        String nroCuentaStr = request.getParameter("nroCuenta");

        if (idCuotaStr == null || nroCuentaStr == null) {
            request.setAttribute("mensaje", "⚠️ No seleccionaste ninguna cuota o cuenta.");
            request.getRequestDispatcher("jsp/cliente/pagarCuotas.jsp").forward(request, response);
            return;
        }

        try {
            int idCuota = Integer.parseInt(idCuotaStr);
            int nroCuenta = Integer.parseInt(nroCuentaStr);

            boolean exito = pagoNegocio.procesarPagoCuotaIndividual(idCuota, nroCuenta);

            List<Cuota> cuotas = new ArrayList<>();
            Cuota cuota = pagoNegocio.obtenerCuotaPorId(idCuota);
            if (cuota != null) {
                cuotas.add(cuota);
            }

            String mensaje = exito
                    ? "✅ La cuota fue pagada correctamente."
                    : "❌ No se pudo realizar el pago. Verificá tu saldo.";

            List<Cuenta> cuentas = pagoNegocio.obtenerCuentasPorCliente(idCliente);

            request.setAttribute("mensaje", mensaje);
            request.setAttribute("cuotasSeleccionadas", cuotas);
            request.setAttribute("cuentasDisponibles", cuentas);
            request.getRequestDispatcher("jsp/cliente/confirmarPago.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error inesperado al procesar el pago.");
            request.getRequestDispatcher("jsp/cliente/confirmarPago.jsp").forward(request, response);
        }
    }
}