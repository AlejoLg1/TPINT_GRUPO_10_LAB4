package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dominio.Usuario;

import negocio.ClienteNegocio;
import negocioImpl.ClienteNegocioImpl;


@WebServlet("/ServletBajaCliente")
public class ServletBajaCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletBajaCliente() {
        super();
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();
        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        int idCliente = Integer.parseInt(request.getParameter("id"));

        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();
        try {
            boolean resultado = clienteNegocio.darDeBajaCliente(idCliente);
            if (!resultado) {
                throw new Exception("No se pudo dar de baja al cliente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al dar de baja al cliente: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/error.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/ServletListarCliente");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // permite manejar baja también por GET si es necesario
    }
}
