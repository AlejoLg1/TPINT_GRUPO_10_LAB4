package servlets;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.LocalDate;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.UsuarioDaoImpl;
import dominio.Cliente;
import dominio.Direccion;
import dominio.Usuario;

/**
 * Servlet implementation class ServletAltaCliente
 */
@WebServlet("/ServletAltaCliente")
public class ServletAltaCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletAltaCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		response.sendRedirect(request.getContextPath() + "/jsp/admin/altaCliente.jsp");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		if(request.getParameter("btnAltaUsuario") != null)
		{
			boolean status = false;
			
			try {
				
				String nombre = request.getParameter("nombre");
			    String apellido = request.getParameter("apellido");
			    String dni = request.getParameter("dni");
			    String cuil = request.getParameter("cuil");
			    String nacionalidad = request.getParameter("nacionalidad");
			    java.time.LocalDate fechaNac = LocalDate.parse(request.getParameter("fechanac"));
			    String direccionCalle = request.getParameter("direccion");
			    String numero =request.getParameter("numero");
			    String localidad = request.getParameter("localidad");
			    String provincia = request.getParameter("provincia");
			    String correo = request.getParameter("email");
			    String telefono = request.getParameter("telefono");
			    String sexoCompleto = request.getParameter("sexo");

			    String username = request.getParameter("username");
			    String pass = request.getParameter("pass");
			    String passRepetida = request.getParameter("passRepetida");

			    // 2. Validar que las contraseñas coincidan
			    if (!pass.equals(passRepetida)) {
			        request.setAttribute("error", "Las contraseñas no coinciden.");
			        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
			        return;
			    }

			    // 3. Convertir fecha al formato correcto (yyyy-MM-dd)
			    java.sql.Date fechaNacimiento = null;
			    try {
			        fechaNacimiento = java.sql.Date.valueOf(fechaNac); // viene como yyyy-MM-dd
			    } catch (Exception e) {
			        request.setAttribute("error", "Formato de fecha inválido.");
			        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
			        return;
			    }

			    // 4. Convertir sexo a M/F/X según lo que venga del select
			    String sexo;
			    switch (sexoCompleto) {
			        case "Masculino": sexo = "M"; break;
			        case "Femenino": sexo = "F"; break;
			        case "Otro": sexo = "X"; break;
			        default: sexo = "X"; break;
			    }

		
				// Insertar en la base de datos
			    
			    //Datos cliente
			    Cliente nuevoCliente = new Cliente();
			    nuevoCliente.setApellido(apellido);
			    nuevoCliente.setCorreo(correo);
			    nuevoCliente.setCuil(cuil);
			    nuevoCliente.setDni(dni);
			    nuevoCliente.setEstado(true);
			    nuevoCliente.setFechaNacimiento(fechaNac);
			    nuevoCliente.setNacionalidad(nacionalidad);
			    nuevoCliente.setNombre(nombre);
			    nuevoCliente.setSexo(sexo);
			    nuevoCliente.setTelefono(telefono);
			    
			    
			  //Datos Usuario
			    Usuario nuevoUsuario = new Usuario();
			    nuevoUsuario.setClave(pass);
			    nuevoUsuario.setEstado(true);
			    nuevoUsuario.setIsAdmin(false);
			    nuevoUsuario.setNombreUsuario(username);
			    nuevoUsuario.setTipo("Cliente");
			    
			    
			  //Datos Direccion
			    Direccion direccion = new Direccion();
			    direccion.setCalle(direccionCalle);
			    direccion.setLocalidad(localidad);
			    direccion.setNumero(numero);
			    direccion.setProvincia(provincia);
			    
			    
				ClienteDao dao = new ClienteDaoImpl();
				status = dao.Agregar(nuevoCliente, nuevoUsuario,direccion);

			} catch (Exception e) {
				status = false;
				e.printStackTrace();
			}

			request.setAttribute("estado", status);
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaCliente.jsp");
			rd.forward(request, response);
			return;
		
	}
	
  }
	
}