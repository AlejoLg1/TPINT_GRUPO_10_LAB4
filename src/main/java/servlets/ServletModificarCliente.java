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
import java.time.LocalDate;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Direccion;
import dominio.Usuario;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;


@WebServlet("/ServletModificarCliente")
public class ServletModificarCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletModificarCliente() {
        super();
        // TODO Auto-generated constructor stub
    }


	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
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


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		int id = Integer.parseInt(request.getParameter("idCliente"));
		
		 ClienteDao dao = new ClienteDaoImpl();
	     Cliente cliente= dao.obtenerPorIdCliente(id);

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
		boolean status = false;
		String nombre = "";
		String apellido = "";
		String dni = "";
		String cuil = "";
		String nacionalidad = "";
		java.time.LocalDate fechaNac = null;
		String Calle = "";
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
	        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
	        int idDireccion = Integer.parseInt(request.getParameter("idDireccion"));
	        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

	        // Datos del cliente
	        nombre = request.getParameter("nombre");
	        apellido = request.getParameter("apellido");
	        dni = request.getParameter("dni");
	        cuil = request.getParameter("cuil");
	        nacionalidad = request.getParameter("nacionalidad");
	        fechaNac = LocalDate.parse(request.getParameter("fechanac"));
	        correo = request.getParameter("email");
	        telefono = request.getParameter("telefono");
	        sexoCompleto = request.getParameter("sexo");
	        
	        //Datos de Usuario
	        username = request.getParameter("username");
		    pass = request.getParameter("pass");
		    passRepetida = request.getParameter("passRepetida");

	        // Datos de dirección
	        Calle = request.getParameter("direccion");
	        numero = request.getParameter("numero");
	        localidad = request.getParameter("localidad");
	        provincia = request.getParameter("provincia");

	       
	       

		    if (!pass.equals(passRepetida)) {
		    	
		    	request.setAttribute("nombre", nombre);
		    	request.setAttribute("apellido", apellido);
		    	request.setAttribute("dni", dni);
		    	request.setAttribute("cuil", cuil);
		    	request.setAttribute("nacionalidad", nacionalidad);
		    	request.setAttribute("fechanac", request.getParameter("fechanac"));
		    	request.setAttribute("direccion", Calle);
		    	request.setAttribute("numero", numero);
		    	request.setAttribute("localidad", localidad);
		    	request.setAttribute("provincia", provincia);
		    	request.setAttribute("email", correo);
		    	request.setAttribute("telefono", telefono);
		    	request.setAttribute("sexo", sexoCompleto);
		    	request.setAttribute("username", username);
		    	
		    	
		    	request.setAttribute("clienteMod", cliente);
		        request.setAttribute("mensajeError", "Las contraseñas no coinciden.");
		        request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
		        return;
		    }
		    
	        
	        String sexo;
	        switch (sexoCompleto) {
	            case "Masculino": sexo = "M"; break;
	            case "Femenino": sexo = "F"; break;
	            case "Otro": sexo = "X"; break;
	            default: sexo = "X"; break;
	        }

	  
		    
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
		    direccion.setCalle(Calle);
		    direccion.setLocalidad(localidad);
		    direccion.setNumero(numero);
		    direccion.setProvincia(provincia);
		    direccion.setId(idDireccion);
		    
	        // Guardar
	        status = dao.Modificar(Cliente, Usuario, direccion);

	    }catch(NombreUsuarioExistenteException ex1) {
	    	request.setAttribute("nombre", nombre);
	    	request.setAttribute("apellido", apellido);
	    	request.setAttribute("dni", dni);
	    	request.setAttribute("cuil", cuil);
	    	request.setAttribute("nacionalidad", nacionalidad);
	    	request.setAttribute("fechanac", request.getParameter("fechanac"));
	    	request.setAttribute("direccion", Calle);
	    	request.setAttribute("numero", numero);
	    	request.setAttribute("localidad", localidad);
	    	request.setAttribute("provincia", provincia);
	    	request.setAttribute("email", correo);
	    	request.setAttribute("telefono", telefono);
	    	request.setAttribute("sexo", sexoCompleto);
	    	request.setAttribute("username", username);
	    	request.setAttribute("pass", pass);
	    	request.setAttribute("passRepetida", passRepetida);

	    	request.setAttribute("clienteMod", cliente);
		    request.setAttribute("mensajeError", ex1.getMessage());
			request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
	    	
	    }catch(DniYaRegistradoException ex2) {
	    	request.setAttribute("nombre", nombre);
	    	request.setAttribute("apellido", apellido);
	    	request.setAttribute("dni", dni);
	    	request.setAttribute("cuil", cuil);
	    	request.setAttribute("nacionalidad", nacionalidad);
	    	request.setAttribute("fechanac", request.getParameter("fechanac"));
	    	request.setAttribute("direccion", Calle);
	    	request.setAttribute("numero", numero);
	    	request.setAttribute("localidad", localidad);
	    	request.setAttribute("provincia", provincia);
	    	request.setAttribute("email", correo);
	    	request.setAttribute("telefono", telefono);
	    	request.setAttribute("sexo", sexoCompleto);
	    	request.setAttribute("username", username);
	    	request.setAttribute("pass", pass);
	    	request.setAttribute("passRepetida", passRepetida);

	    	request.setAttribute("clienteMod", cliente);
		    request.setAttribute("mensajeError", ex2.getMessage());
			request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
	    	
	    
	    
	    }catch (Exception e) {
	        status = false;
	        e.printStackTrace();
	    }

	    request.setAttribute("estado", status);
	    RequestDispatcher rd = request.getRequestDispatcher("/ServletListarCliente");
	    rd.forward(request, response);
	}
}
