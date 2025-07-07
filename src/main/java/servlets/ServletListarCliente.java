package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.ClienteNegocioImpl;

import java.io.IOException;
import java.util.List;

import dominio.Cliente;
import dominio.Usuario;

@WebServlet("/ServletListarCliente")
public class ServletListarCliente extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletListarCliente() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuario = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuario == null || (!auth.validarRolAdmin(usuario) && !auth.validarRolBancario(usuario))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String estado = request.getParameter("estado");

        ClienteNegocioImpl clienteNegocio = new ClienteNegocioImpl();
        try {
            List<Cliente> listaCliente = clienteNegocio.listarClientesFiltrados(nombre, apellido, dni, estado);
            request.setAttribute("clientes", listaCliente);
        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("error", "Error al listar clientes: " + e.getMessage());
        }

        request.getRequestDispatcher("/jsp/admin/clientes.jsp").forward(request, response);
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }
}
