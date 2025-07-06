package servlets;

import java.io.IOException;
import java.util.List;

import negocio.PrestamoNegocio;
import negocioImpl.PrestamoNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;
import dominio.Cuenta;
import dominio.Usuario;
import excepciones.CuentaExistenteExcenption;
import excepciones.PrestamoException;


import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;


@WebServlet("/ServletSolicitarPrestamo")
public class ServletSolicitarPrestamo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private final PrestamoNegocio prestamoNegocio = new PrestamoNegocioImpl();
    private final AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        try {
            List<Cuenta> cuentasCliente = prestamoNegocio.obtenerCuentasCliente(usuario.getIdUsuario());
            request.setAttribute("cuentasCliente", cuentasCliente);
        } catch (Exception e) {
            request.setAttribute("mensaje", "❌ Error al obtener las cuentas del cliente.");
            request.setAttribute("estado", false);
        }

        // Pasar mensaje de sesión si existe
        String mensaje = (String) session.getAttribute("mensaje");
        Boolean estado = (Boolean) session.getAttribute("estado");
        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("estado", estado);
            session.removeAttribute("mensaje");
            session.removeAttribute("estado");
        }

        request.getRequestDispatcher("/jsp/cliente/solicitarPrestamo.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null || !auth.validarRolCliente(usuario)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String msg = "✅ Solicitud realizada con éxito";
        boolean status = true;

        try {
            int idCuenta = Integer.parseInt(request.getParameter("cuentasCliente"));
            double monto = Double.parseDouble(request.getParameter("monto"));
            int cuotas = Integer.parseInt(request.getParameter("cuotas"));

            boolean resultado = prestamoNegocio.solicitarPrestamo(usuario.getIdUsuario(), idCuenta, monto, cuotas);
            if (!resultado) {
                throw new PrestamoException("Error al registrar el préstamo.");
            }

        } catch (PrestamoException | CuentaExistenteExcenption e) {
            status = false;
            msg = "❌ " + e.getMessage();
        } catch (Exception e) {
            status = false;
            msg = "❌ Error inesperado al procesar la solicitud";
            e.printStackTrace();
        }

        session.setAttribute("mensaje", msg);
        session.setAttribute("estado", status);
        response.sendRedirect("ServletSolicitarPrestamo");
    }
}
