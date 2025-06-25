package servlets;

import java.io.IOException;
import java.util.List;

import daoImpl.ClienteDaoImpl;
import daoImpl.CuentaDaoImpl;
import daoImpl.PrestamoDaoImpl;
import dominio.Cliente;
import dominio.Cuenta;
import dominio.Prestamo;
import dominio.Usuario;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

@WebServlet("/ServletSolicitarPrestamo")
public class ServletSolicitarPrestamo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletSolicitarPrestamo() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Obtener usuario de sesión
        HttpSession session = request.getSession();
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        if (usuario == null) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        // Obtener cliente
        ClienteDaoImpl clienteDao = new ClienteDaoImpl();
        Cliente cliente = clienteDao.obtenerPorIdUsuario(usuario.getIdUsuario());

        if (cliente == null) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        // Obtener cuentas del cliente
        CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
        List<Cuenta> cuentasCliente = cuentaDao.listarPorCliente(cliente.getIdCliente());

        // Poner cuentas en el request
        request.setAttribute("cuentasCliente", cuentasCliente);

        // Ir al JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/cliente/solicitarPrestamo.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        boolean status = false;

        try {
            String idCuenta = request.getParameter("cuentasCliente");
            String monto = request.getParameter("monto");
            String cuotas = request.getParameter("cuotas");

            if (idCuenta == null || monto == null || cuotas == null ||
                    idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cuotas.trim().isEmpty()) {
                throw new IllegalArgumentException();
            }

            int nroCuenta = Integer.parseInt(idCuenta);
            double importeSolicitado = Double.parseDouble(monto);
            int cantidadCuotas = Integer.parseInt(cuotas);
            double montoCuota = importeSolicitado / cantidadCuotas;

            // Obtener cliente de sesión
            HttpSession session = request.getSession();
            Usuario usuario = (Usuario) session.getAttribute("usuario");
            ClienteDaoImpl clienteDao = new ClienteDaoImpl();
            Cliente cliente = clienteDao.obtenerPorIdUsuario(usuario.getIdUsuario());
            
            //obtener cuenta seleccionada
            CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
            Cuenta cuenta = cuentaDao.obtenerCuentaPorId(nroCuenta);
            
            
            Prestamo prestamo = new Prestamo();
            prestamo.set_cliente(cliente);
            prestamo.set_cuenta(cuenta);
            prestamo.setImporte_solicitado(importeSolicitado);
            prestamo.setCantidad_cuotas(cantidadCuotas);
            prestamo.setMonto_cuota(montoCuota);

            PrestamoDaoImpl prestamoDao = new PrestamoDaoImpl();
            status = prestamoDao.registrarPrestamo(prestamo);

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        request.setAttribute("estado", status);

        // Recargar cuentas y volver a JSP
        doGet(request, response);
    }
}
