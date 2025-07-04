package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;

import dao.ClienteDao;
import dao.TipoCuentaDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.TipoCuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;
import dominio.Usuario;
import negocio.CuentaNegocio;
import negocioImpl.CuentaNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;


@WebServlet("/ServletAltaCuenta")
public class ServletAltaCuenta extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletAltaCuenta() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String idParam = request.getParameter("id");

        if (idParam != null) {
            try {
                int idCuenta = Integer.parseInt(idParam);
                CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
                Cuenta cuenta = cuentaNegocio.obtenerCuentaPorId(idCuenta);

                if (cuenta != null) {
                    request.setAttribute("cuentaMod", cuenta);
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }

        cargarDatosFormulario(request);
        request.getRequestDispatcher("/jsp/admin/altaCuentas.jsp").forward(request, response);
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        try {
            String idCuentaParam = request.getParameter("cuentaId");
            int idCliente = Integer.parseInt(request.getParameter("clienteId"));
            int idTipoCuenta = Integer.parseInt(request.getParameter("tipoCuenta"));
            BigDecimal monto = new BigDecimal(request.getParameter("montoInicial"));

            CuentaNegocio cuentaNegocio = new CuentaNegocioImpl();
            String mensaje;

            if (idCuentaParam != null && !idCuentaParam.isEmpty()) {
                int idCuenta = Integer.parseInt(idCuentaParam);
                mensaje = cuentaNegocio.modificarCuenta(idCuenta, idCliente, idTipoCuenta, monto);
                Cuenta cuenta = cuentaNegocio.obtenerCuentaPorId(idCuenta);
                request.setAttribute("cuentaMod", cuenta);
            } else {
                mensaje = cuentaNegocio.crearCuenta(idCliente, idTipoCuenta, monto);
            }

            request.setAttribute("mensaje", mensaje);
            cargarDatosFormulario(request);
            request.getRequestDispatcher("/jsp/admin/altaCuentas.jsp").forward(request, response);

        } catch (Exception e) {
            e.printStackTrace();
            request.getSession().setAttribute("mensajeCuenta", "❌ Error inesperado al procesar los datos.");
            response.sendRedirect("ServletCuenta");
        }
    }


    private void cargarDatosFormulario(HttpServletRequest request) {
        ClienteDao clienteDao = new ClienteDaoImpl();
        TipoCuentaDao tipoCuentaDao = new TipoCuentaDaoImpl();

        List<Cliente> listaClientes = clienteDao.Listar();
        List<TipoCuenta> listaTiposCuenta = tipoCuentaDao.listar();

        request.setAttribute("listaClientes", listaClientes);
        request.setAttribute("listaTiposCuenta", listaTiposCuenta);
    }
}
