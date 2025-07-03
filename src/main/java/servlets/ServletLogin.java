package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import dominio.Usuario;
import dominio.Cliente;
import excepciones.AutenticacionException;
import negocioImpl.AutenticacionNegocioImpl;
import negocioImpl.ClienteNegocioImpl;
import negocio.ClienteNegocio;

import java.io.IOException;

@WebServlet("/ServletLogin")
public class ServletLogin extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public ServletLogin() {
        super();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String nombreUsuario = request.getParameter("usuario");
        String clave = request.getParameter("clave");

        AutenticacionNegocioImpl authNegocio = new AutenticacionNegocioImpl();
        ClienteNegocio clienteNegocio = new ClienteNegocioImpl();

        try {
            Usuario usuario = authNegocio.autenticar(nombreUsuario, clave);

            HttpSession session = request.getSession();
            session.setAttribute("usuario", usuario);

            if (usuario.getTipo().equalsIgnoreCase("bancario")) {
                if (usuario.isAdmin()) {
                    session.setAttribute("rol", "admin");
                } else {
                    session.setAttribute("rol", "bancario");
                }
                response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
            } else {
                Cliente cliente = clienteNegocio.obtenerPorIdUsuario(usuario.getIdUsuario());
                
                session.setAttribute("idCliente", cliente.getIdCliente());
                session.setAttribute("rol", "cliente");
                response.sendRedirect(request.getContextPath() + "/ServletMenuCliente");
            }

        } catch (AutenticacionException | excepciones.ClienteNoVinculadoException e) {
            request.setAttribute("errorLogin", e.getMessage());
            request.getRequestDispatcher("/jsp/comunes/login.jsp").forward(request, response);
        }
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/comunes/login.jsp").forward(request, response);
    }
}
