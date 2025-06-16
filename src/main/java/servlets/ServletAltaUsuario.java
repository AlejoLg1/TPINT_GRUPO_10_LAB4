package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Servlet implementation class ServletAltaUsuario
 */
@WebServlet("/ServletAltaUsuario")
public class ServletAltaUsuario extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAltaUsuario() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath() + "/jsp/admin/altaUsuario.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		if(request.getParameter("btnAltaUsuario") != null)
		{
			boolean status = false;
			
			try {
			    String nombre = request.getParameter("nombre").isEmpty() ? null : request.getParameter("nombre");
			    String apellido = request.getParameter("apellido").isEmpty() ? null : request.getParameter("apellido");
			    String dni = request.getParameter("dni").isEmpty() ? null : request.getParameter("dni");
			    String fechaNac = request.getParameter("fechanac").isEmpty() ? null : request.getParameter("fechanac");
			    String email = request.getParameter("email").isEmpty() ? null : request.getParameter("email");
			    String tipoUser = request.getParameter("tipoUser").isEmpty() ? null : request.getParameter("tipoUser");
			    String username = request.getParameter("username").isEmpty() ? null : request.getParameter("username");
			    String pass = request.getParameter("pass").isEmpty() ? null : request.getParameter("pass");

			    // Validación de nulos
			    if (nombre == null || apellido == null || dni == null || fechaNac == null ||
			        email == null || tipoUser == null || username == null || pass == null) {
			        throw new IllegalArgumentException();
			    }
			    // Registrar solicitud en la db
			    
			    status = true;

			} catch (Exception e) {
			    status = false;
			    e.printStackTrace(); 
			}

			
			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaUsuario.jsp");
			rd.forward(request, response);
		}
		
		doGet(request, response);
	}

}
