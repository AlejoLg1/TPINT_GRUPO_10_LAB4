package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import negocioImpl.AutenticacionNegocioImpl;

import java.io.IOException;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

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
		Usuario usuarioSession = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuarioSession == null || !auth.validarRolAdmin(usuarioSession)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
        int id = Integer.parseInt(request.getParameter("id"));

        UsuarioDao dao = new UsuarioDaoImpl();
        Usuario usuario = dao.obtenerPorId(id);

        if (usuario != null) {
            request.setAttribute("usuarioMod", usuario);
            request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/usuarios.jsp");
        }
    }



    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        boolean status = false;
        
        HttpSession session = request.getSession(false);
		Usuario usuarioSession = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuarioSession == null || !auth.validarRolAdmin(usuarioSession)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
        try {
            String idParam = request.getParameter("id");
            String tipoUser = request.getParameter("tipoUser");
            String pass = request.getParameter("pass");
            String passRepetida = request.getParameter("passRepetida");

            if (idParam == null) throw new IllegalArgumentException("Falta el ID de usuario.");
            int idUsuario = Integer.parseInt(idParam);

            Usuario usuario = new Usuario();
            usuario.setIdUsuario(idUsuario);
            usuario.setTipo("BANCARIO");

            String modo = "";

            // Si viene cambio de tipo de usuario
            if (tipoUser != null) {
                usuario.setIsAdmin(tipoUser.equals("Admin"));
                modo = "tipo";
            }

            // Si viene cambio de contraseña
            if (pass != null && !pass.isEmpty()) {
                if (!pass.equals(passRepetida)) {
                    throw new IllegalArgumentException("Las contraseñas no coinciden.");
                }
                usuario.setClave(pass);
                modo = modo.equals("tipo") ? "ambos" : "clave";
            }

            if (modo.isEmpty()) throw new IllegalArgumentException("No se enviaron cambios válidos.");

            UsuarioDao dao = new UsuarioDaoImpl();
            status = dao.Modificar(usuario, modo);

        } catch (Exception e) {
            status = false;
            e.printStackTrace();
        }

        request.setAttribute("estado", status);
        RequestDispatcher rd = request.getRequestDispatcher("/ServletListarUsuario");
        rd.forward(request, response);
    }


}
