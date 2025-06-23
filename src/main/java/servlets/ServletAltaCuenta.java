package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import dao.ClienteDao;
import dao.CuentaDao;
import dao.TipoCuentaDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.TipoCuentaDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.TipoCuenta;

@WebServlet("/ServletAltaCuenta")
public class ServletAltaCuenta extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletAltaCuenta() {
        super();
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        cargarDatosFormulario(request);
        request.getRequestDispatcher("/jsp/admin/altaCuentas.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int idCliente = Integer.parseInt(request.getParameter("clienteId"));
            int idTipoCuenta = Integer.parseInt(request.getParameter("tipoCuenta"));
            String monto = request.getParameter("montoInicial");

            Cuenta cuenta = new Cuenta();
            cuenta.setSaldo(new java.math.BigDecimal(monto));
            cuenta.setFechaCreacion(java.time.LocalDateTime.now());

            CuentaDao cuentaDao = new CuentaDaoImpl();
            boolean creada = cuentaDao.agregar(cuenta, idCliente, idTipoCuenta);

            if (creada) {
                request.setAttribute("mensaje", "✅ Cuenta creada correctamente.");
            } else {
                request.setAttribute("mensaje", "❌ Hubo un error al crear la cuenta.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error inesperado al procesar los datos.");
        }

        cargarDatosFormulario(request);
        request.getRequestDispatcher("/jsp/admin/altaCuentas.jsp").forward(request, response);
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
