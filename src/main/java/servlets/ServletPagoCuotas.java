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
import dominio.Cuenta;
import negocioImpl.CuotaNegocioImpl;
import daoImpl.CuentaDaoImpl;

@WebServlet("/ServletPagoCuotas")
public class ServletPagoCuotas extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object idClienteObj = session.getAttribute("idCliente");

        if (idClienteObj == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
            return;
        }

        int idCliente = (int) idClienteObj;

        try {
            CuotaNegocioImpl cuotaNegocio = new CuotaNegocioImpl();
            List<Cuota> cuotasPendientes = cuotaNegocio.listarCuotasPendientesPorCliente(idCliente);
            request.setAttribute("cuotasPendientes", cuotasPendientes);
            request.getRequestDispatcher("jsp/cliente/pagarCuotas.jsp").forward(request, response);
        } catch (Exception e) {
            e.printStackTrace();
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Error al obtener cuotas");
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        Object idClienteObj = session.getAttribute("idCliente");

        if (idClienteObj == null) {
            response.sendRedirect(request.getContextPath() + "/jsp/comunes/login.jsp");
            return;
        }

        int idCliente = (int) idClienteObj;

        try {
            String[] cuotasSeleccionadas = request.getParameterValues("cuotas");
            String nroCuentaStr = request.getParameter("nroCuenta");

            if (cuotasSeleccionadas == null || cuotasSeleccionadas.length == 0 || nroCuentaStr == null) {
                request.setAttribute("mensaje", "⚠️ No seleccionaste ninguna cuota o cuenta.");
                request.getRequestDispatcher("jsp/cliente/pagarCuotas.jsp").forward(request, response);
                return;
            }

            int nroCuenta = Integer.parseInt(nroCuentaStr);
            CuotaNegocioImpl cuotaNegocio = new CuotaNegocioImpl();
            List<Cuota> cuotasAPagar = new ArrayList<>();

            for (String idCuotaStr : cuotasSeleccionadas) {
                int idCuota = Integer.parseInt(idCuotaStr);
                Cuota c = cuotaNegocio.obtenerCuotaPorId(idCuota);
                if (c != null && c.getMonto() != null) {
                    cuotasAPagar.add(c);
                }
            }

            boolean exito = cuotaNegocio.procesarPagoCuotas(cuotasAPagar, nroCuenta);

            if (exito) {
                request.setAttribute("mensaje", "✅ Las cuotas fueron pagadas correctamente.");
            } else {
                request.setAttribute("mensaje", "❌ No se pudo realizar el pago. Verificá tu saldo.");
            }

            // Volver a mostrar la pantalla de confirmación con datos
            CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
            List<Cuenta> cuentas = cuentaDao.listarPorCliente(idCliente);

            request.setAttribute("cuotasSeleccionadas", cuotasAPagar);
            request.setAttribute("cuentasDisponibles", cuentas);
            request.getRequestDispatcher("jsp/cliente/confirmarPago.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error inesperado al procesar el pago.");
            request.getRequestDispatcher("jsp/cliente/confirmarPago.jsp").forward(request, response);
        }
    }
}