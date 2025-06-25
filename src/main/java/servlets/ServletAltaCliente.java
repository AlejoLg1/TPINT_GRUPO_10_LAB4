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
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;

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
			String nombre = "";
			String apellido = "";
			String dni = "";
			String cuil = "";
			String nacionalidad = "";
			java.time.LocalDate fechaNac = null;
			String direccionCalle = "";
			String numero = "";
			String localidad = "";
			String provincia = "";
			String correo = "";
			String telefono = "";
			String sexoCompleto = "";
			String username = "";
			String pass = "";
			String passRepetida = "";
			
			try {
				
				 nombre = request.getParameter("nombre");
			    apellido = request.getParameter("apellido");
			    dni = request.getParameter("dni");
			    cuil = request.getParameter("cuil");
			    nacionalidad = request.getParameter("nacionalidad");
			    fechaNac = LocalDate.parse(request.getParameter("fechanac"));
			    direccionCalle = request.getParameter("direccion");
			    numero =request.getParameter("numero");
			    localidad = request.getParameter("localidad");
			    provincia = request.getParameter("provincia");
			    correo = request.getParameter("email");
			    telefono = request.getParameter("telefono");
			    sexoCompleto = request.getParameter("sexo");

			    username = request.getParameter("username");
			    pass = request.getParameter("pass");
			    passRepetida = request.getParameter("passRepetida");

			    // 2. Validar que las contraseñas coincidan
			    if (!pass.equals(passRepetida)) {
			    	
			    	request.setAttribute("nombre", nombre);
			    	request.setAttribute("apellido", apellido);
			    	request.setAttribute("dni", dni);
			    	request.setAttribute("cuil", cuil);
			    	request.setAttribute("nacionalidad", nacionalidad);
			    	request.setAttribute("fechanac", request.getParameter("fechanac"));
			    	request.setAttribute("direccion", direccionCalle);
			    	request.setAttribute("numero", numero);
			    	request.setAttribute("localidad", localidad);
			    	request.setAttribute("provincia", provincia);
			    	request.setAttribute("email", correo);
			    	request.setAttribute("telefono", telefono);
			    	request.setAttribute("sexo", sexoCompleto);
			    	request.setAttribute("username", username);

			        request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
			        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
			        return;
			    }

			    // Convertir sexo
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

			}catch(NombreUsuarioExistenteException ex1) {
				request.setAttribute("nombre", nombre);
		    	request.setAttribute("apellido", apellido);
		    	request.setAttribute("dni", dni);
		    	request.setAttribute("cuil", cuil);
		    	request.setAttribute("nacionalidad", nacionalidad);
		    	request.setAttribute("fechanac", request.getParameter("fechanac"));
		    	request.setAttribute("direccion", direccionCalle);
		    	request.setAttribute("numero", numero);
		    	request.setAttribute("localidad", localidad);
		    	request.setAttribute("provincia", provincia);
		    	request.setAttribute("email", correo);
		    	request.setAttribute("telefono", telefono);
		    	request.setAttribute("sexo", sexoCompleto);
		    	request.setAttribute("username", username);
		    	request.setAttribute("pass", pass);
		    	request.setAttribute("passRepetida", passRepetida);

			    request.setAttribute("mensajeError", ex1.getMessage());
				request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
				
			}catch(DniYaRegistradoException ex2) {
				request.setAttribute("nombre", nombre);
		    	request.setAttribute("apellido", apellido);
		    	request.setAttribute("dni", dni);
		    	request.setAttribute("cuil", cuil);
		    	request.setAttribute("nacionalidad", nacionalidad);
		    	request.setAttribute("fechanac", request.getParameter("fechanac"));
		    	request.setAttribute("direccion", direccionCalle);
		    	request.setAttribute("numero", numero);
		    	request.setAttribute("localidad", localidad);
		    	request.setAttribute("provincia", provincia);
		    	request.setAttribute("email", correo);
		    	request.setAttribute("telefono", telefono);
		    	request.setAttribute("sexo", sexoCompleto);
		    	request.setAttribute("username", username);
		    	request.setAttribute("pass", pass);
		    	request.setAttribute("passRepetida", passRepetida);
		    	
				request.setAttribute("mensajeError", ex2.getMessage());
				request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
				
			}catch (Exception e) {
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