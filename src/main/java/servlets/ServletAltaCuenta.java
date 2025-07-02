package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;
import java.util.List;
import java.math.BigDecimal;

import dao.ClienteDao;
import dao.CuentaDao;
import dao.TipoCuentaDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.TipoCuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;
import dominio.Usuario;

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
                CuentaDao cuentaDao = new CuentaDaoImpl();
                Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);

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
        
    	CuentaDao cuentaDao = new CuentaDaoImpl();

        try {
            String idCuentaParam = request.getParameter("cuentaId");
            int idCliente = Integer.parseInt(request.getParameter("clienteId"));
            int idTipoCuenta = Integer.parseInt(request.getParameter("tipoCuenta"));
            BigDecimal monto = new BigDecimal(request.getParameter("montoInicial"));

            if (idCuentaParam != null && !idCuentaParam.isEmpty()) {
                // MODO MODIFICACIÓN
                int idCuenta = Integer.parseInt(idCuentaParam);
                Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
                if (cuenta != null) {
                    cuenta.setIdCliente(idCliente);
                    cuenta.setIdTipoCuenta(idTipoCuenta);
                    cuenta.setSaldo(monto);

                    boolean actualizado = cuentaDao.actualizar(cuenta);
                    request.setAttribute("mensaje", actualizado ? "✅ Cuenta modificada correctamente." : "❌ Error al modificar la cuenta.");
                    request.setAttribute("cuentaMod", cuenta);
                } else {
                    request.setAttribute("mensaje", "❌ No se encontró la cuenta a modificar.");
                }
            } else {
                // MODO CREACIÓN
                int cuentasActivas = cuentaDao.contarCuentasActivasPorCliente(idCliente);
                if (cuentasActivas >= 3) {
                    request.setAttribute("mensaje", "❌ El cliente ya tiene 3 cuentas activas.");
                } else {
                    Cuenta nueva = new Cuenta();
                    nueva.setSaldo(monto);
                    nueva.setFechaCreacion(java.time.LocalDateTime.now());

                    boolean creada = cuentaDao.agregar(nueva, idCliente, idTipoCuenta);
                    request.setAttribute("mensaje", creada ? "✅ Cuenta creada correctamente." : "❌ Error al crear la cuenta.");
                }
            }

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
