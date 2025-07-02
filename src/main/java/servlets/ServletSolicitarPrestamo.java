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
import excepciones.CuentaExistenteExcenption;
import excepciones.PrestamoException;
import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

@WebServlet("/ServletSolicitarPrestamo")
public class ServletSolicitarPrestamo extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletSolicitarPrestamo() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || !auth.validarRolCliente(usuario)) {
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

        String mensaje = (String) session.getAttribute("mensaje");
        Boolean estado = (Boolean) session.getAttribute("estado");
        
        if (mensaje != null) {
            request.setAttribute("mensaje", mensaje);
            request.setAttribute("estado", estado);

            // Eliminar los atributos de sesiÃ³n para que no se repitan
            session.removeAttribute("mensaje");
            session.removeAttribute("estado");
        }
        
        // Poner cuentas en el request
        request.setAttribute("cuentasCliente", cuentasCliente);

        // Ir al JSP
        RequestDispatcher dispatcher = request.getRequestDispatcher("/jsp/cliente/solicitarPrestamo.jsp");
        dispatcher.forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
    	HttpSession sessionSS = request.getSession(false);
		Usuario usuarioSS = (Usuario) sessionSS.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuarioSS == null || !auth.validarRolCliente(usuarioSS)) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }
        
        boolean status = false;
        String msg = "✅ Solicitud realizado con exito";

        try {
            String idCuenta = request.getParameter("cuentasCliente");
            String monto = request.getParameter("monto");
            String cuotas = request.getParameter("cuotas");

            if (idCuenta == null || monto == null || cuotas == null ||
                    idCuenta.trim().isEmpty() || monto.trim().isEmpty() || cuotas.trim().isEmpty()) 
                throw new IllegalArgumentException();
            
            if(idCuenta.compareTo("invalid") == 0)
            	throw new PrestamoException("Seleccione una cuenta destino valida");

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
            
            if(cuenta == null)
            	throw new CuentaExistenteExcenption("No se pudieron obtner datos de la cuenta seleccionada");
            
            Prestamo prestamo = new Prestamo();
            prestamo.set_cliente(cliente);
            prestamo.set_cuenta(cuenta);
            prestamo.setImporte_solicitado(importeSolicitado);
            prestamo.setCantidad_cuotas(cantidadCuotas);
            prestamo.setMonto_cuota(montoCuota);

            PrestamoDaoImpl prestamoDao = new PrestamoDaoImpl();
            status = prestamoDao.registrarPrestamo(prestamo);
            
            if(!status)
            	throw new PrestamoException("Ocurrio un error al registrar la solicitud");
            
        } catch (PrestamoException e) {
            status = false;
            msg = "❌ " + e.getMessage();
        } catch (CuentaExistenteExcenption e) {
            status = false;
            msg = "❌ " + e.getMessage();
        } catch (Exception e) {
            status = false;
            msg = "❌ Ocurrio un error durante la solicitud";
            e.printStackTrace();
        }
        
        request.setAttribute("mensaje", msg);
        request.setAttribute("estado", status);

        // Recargar cuentas y volver a JSP
        doGet(request, response);
    }
}
