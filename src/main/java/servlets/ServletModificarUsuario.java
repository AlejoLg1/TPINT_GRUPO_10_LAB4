package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import java.io.IOException;

import dominio.Usuario;
import negocio.UsuarioNegocio;
import negocioImpl.UsuarioNegocioImpl;
import negocioImpl.AutenticacionNegocioImpl;


@WebServlet("/ServletModificarUsuario")
public class ServletModificarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
 
    public ServletModificarUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuarioSS = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuarioSS == null || (!auth.validarRolAdmin(usuarioSS) && !auth.validarRolBancario(usuarioSS))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        int id = Integer.parseInt(request.getParameter("id"));

        UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
        Usuario usuario = usuarioNegocio.obtenerPorId(id);

        if (usuario != null) {
            request.setAttribute("usuarioMod", usuario);
            request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/usuarios.jsp");
        }
    }




    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        Usuario usuarioSS = (Usuario) session.getAttribute("usuario");

        AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

        if (usuarioSS == null || (!auth.validarRolAdmin(usuarioSS) && !auth.validarRolBancario(usuarioSS))) {
            response.sendRedirect(request.getContextPath() + "/ServletLogin");
            return;
        }

        UsuarioNegocio usuarioNegocio = new UsuarioNegocioImpl();
        String msg = "✅ Usuario modificado correctamente";
        boolean status = false;

        int idUsuario = Integer.parseInt(request.getParameter("id"));
        String tipoUser = request.getParameter("tipoUser");
        String pass = request.getParameter("pass");
        String passRepetida = request.getParameter("passRepetida");

        try {
            status = usuarioNegocio.modificarUsuario(idUsuario, tipoUser, pass, passRepetida);
        } catch (IllegalArgumentException e) {
            msg = "❌ " + e.getMessage();
            e.printStackTrace();
        } catch (Exception e) {
            msg = "❌ Ocurrio un error durante la modificación";
            e.printStackTrace();
        }

        Usuario usuario = usuarioNegocio.obtenerPorId(idUsuario);

        request.setAttribute("estado", status);
        request.setAttribute("mensaje", msg);

        if (usuario != null) {
            request.setAttribute("usuarioMod", usuario);
            request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/usuarios.jsp");
        }
    }



}
