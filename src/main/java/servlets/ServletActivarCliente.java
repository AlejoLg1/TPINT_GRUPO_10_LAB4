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


@WebServlet("/ServletActivarCliente")
public class ServletActivarCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletActivarCliente() {
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
            boolean resultado = clienteNegocio.activarCliente(idCliente);
            if (!resultado) {
                throw new Exception("No se pudo activar el cliente.");
            }
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("mensaje", "❌ Error al activar cliente: " + e.getMessage());
            request.getRequestDispatcher("/jsp/admin/error.jsp").forward(request, response);
            return;
        }

        response.sendRedirect(request.getContextPath() + "/ServletListarCliente");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response); // opcional: permite activar por GET si querés
    }
}
