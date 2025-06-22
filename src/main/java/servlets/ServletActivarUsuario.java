package servlets;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;
import dominio.Usuario;

@WebServlet("/ServletActivarUsuario")
public class ServletActivarUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletActivarUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.getWriter().append("Served at: ").append(request.getContextPath());
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            int id = Integer.parseInt(request.getParameter("id"));

            UsuarioDao dao = new UsuarioDaoImpl();
            Usuario u = new Usuario();
            u.setIdUsuario(id);
            u.setEstado(true);

            dao.Modificar(u, "estado"); // Usa Modificar si ya existe. O hacé uno específico como dao.Activar()

        } catch (Exception e) {
            e.printStackTrace();
        }

        response.sendRedirect(request.getContextPath() + "/ServletListarUsuario");
    }

}
