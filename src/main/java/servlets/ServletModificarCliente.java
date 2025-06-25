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
import dao.UsuarioDao;
import daoImpl.ClienteDaoImpl;
import daoImpl.UsuarioDaoImpl;
import dominio.Cliente;
import dominio.Direccion;
import dominio.Usuario;

/**
 * Servlet implementation class ServletModificarCliente
 */
@WebServlet("/ServletModificarCliente")
public class ServletModificarCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ServletModificarCliente() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 int id = Integer.parseInt(request.getParameter("id"));

	        ClienteDao dao = new ClienteDaoImpl();
	        Cliente cliente= dao.obtenerPorIdCliente(id);

	        if (cliente != null) {
	            request.setAttribute("clienteMod", cliente);
	            request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
	        } else {
	            response.sendRedirect(request.getContextPath() + "/jsp/admin/clientes.jsp");
	        }
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		boolean status = false;

	    try {
	        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
	        int idDireccion = Integer.parseInt(request.getParameter("idDireccion"));
	        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

	        // Datos del cliente
	        String nombre = request.getParameter("nombre");
	        String apellido = request.getParameter("apellido");
	        String dni = request.getParameter("dni");
	        String cuil = request.getParameter("cuil");
	        String nacionalidad = request.getParameter("nacionalidad");
	        String fechanacStr = request.getParameter("fechanac");
	        String correo = request.getParameter("email");
	        String telefono = request.getParameter("telefono");
	        String sexoCompleto = request.getParameter("sexo");
	        
	        //Datos de Usuario
	        String username = request.getParameter("username");
		    String pass = request.getParameter("pass");
		    String passRepetida = request.getParameter("passRepetida");

	        // Datos de dirección
	        String calle = request.getParameter("direccion");
	        String numero = request.getParameter("numero");
	        String localidad = request.getParameter("localidad");
	        String provincia = request.getParameter("provincia");

	        // Convertir fecha
	        LocalDate fechaNac = LocalDate.parse(fechanacStr);

	        // 2. Validar que las contraseñas coincidan
		    if (!pass.equals(passRepetida)) {
		    	
		    	request.setAttribute("nombre", nombre);
		    	request.setAttribute("apellido", apellido);
		    	request.setAttribute("dni", dni);
		    	request.setAttribute("cuil", cuil);
		    	request.setAttribute("nacionalidad", nacionalidad);
		    	request.setAttribute("fechanac", request.getParameter("fechanac"));
		    	request.setAttribute("direccion", calle);
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
		    Cliente Cliente = new Cliente();
		    Cliente.setApellido(apellido);
		    Cliente.setCorreo(correo);
		    Cliente.setCuil(cuil);
		    Cliente.setDni(dni);
		    Cliente.setEstado(true);
		    Cliente.setFechaNacimiento(fechaNac);
		    Cliente.setNacionalidad(nacionalidad);
		    Cliente.setNombre(nombre);
		    Cliente.setSexo(sexo);
		    Cliente.setTelefono(telefono);
		    Cliente.setIdCliente(idCliente);
		    
		    
		  //Datos Usuario
		    Usuario Usuario = new Usuario();
		    Usuario.setClave(pass);
		    Usuario.setEstado(true);
		    Usuario.setIsAdmin(false);
		    Usuario.setNombreUsuario(username);
		    Usuario.setTipo("Cliente");
		    Usuario.setIdUsuario(idUsuario);
		    
		    
		  //Datos Direccion
		    Direccion direccion = new Direccion();
		    direccion.setCalle(calle);
		    direccion.setLocalidad(localidad);
		    direccion.setNumero(numero);
		    direccion.setProvincia(provincia);
		    direccion.setId(idDireccion);
		    
	        // Guardar
	        ClienteDao dao = new ClienteDaoImpl();
	        status = dao.Modificar(Cliente, Usuario, direccion);

	    } catch (Exception e) {
	        status = false;
	        e.printStackTrace();
	    }

	    request.setAttribute("estado", status);
	    RequestDispatcher rd = request.getRequestDispatcher("/ServletListarCliente");
	    rd.forward(request, response);
	}
}
