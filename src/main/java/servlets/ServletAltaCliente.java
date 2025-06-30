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
import excepciones.AutenticacionException;
import excepciones.ContrasenasNoCoincidenException;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;


@WebServlet("/ServletAltaCliente")
public class ServletAltaCliente extends HttpServlet {
	private static final long serialVersionUID = 1L;
       

    public ServletAltaCliente() {
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
		
		response.sendRedirect(request.getContextPath() + "/jsp/admin/altaCliente.jsp");
	}


	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		Usuario usuario = (Usuario) session.getAttribute("usuario");
		
		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
		boolean status = false;
		String msg = "✅ Cliente registrado correctamente !";
		
		String nombre = request.getParameter("nombre") == null? "" : request.getParameter("nombre");
	    String apellido = request.getParameter("apellido") == null? "" : request.getParameter("apellido");
	    String dni = request.getParameter("dni") == null? "" : request.getParameter("dni");
	    String cuil = request.getParameter("cuil") == null? "" : request.getParameter("cuil");
	    String nacionalidad = request.getParameter("nacionalidad") == null? "" : request.getParameter("nacionalidad");
	    LocalDate fechaNac = request.getParameter("fechanac") == null? LocalDate.now() : LocalDate.parse(request.getParameter("fechanac"));
	    String direccionCalle = request.getParameter("direccion") == null? "" : request.getParameter("direccion");
	    String numero =request.getParameter("numero") == null? "" : request.getParameter("numero");
	    String localidad = request.getParameter("localidad") == null? "" : request.getParameter("localidad");
	    String provincia = request.getParameter("provincia") == null? "" : request.getParameter("provincia");
	    String correo = request.getParameter("email") == null? "" : request.getParameter("email");
	    String telefono = request.getParameter("telefono") == null? "" : request.getParameter("telefono");
	    String sexoCompleto = request.getParameter("sexo") == null? "" : request.getParameter("sexo");

	    String username = request.getParameter("username") == null? "" : request.getParameter("username");
	    String pass = request.getParameter("pass") == null? "" : request.getParameter("pass");
	    String passRepetida = request.getParameter("passRepetida") == null? "" : request.getParameter("passRepetida");
		
		
		if(request.getParameter("btnAltaUsuario") != null)
		{
			try {

			    String sexo;
			    switch (sexoCompleto) {
			        case "Masculino": sexo = "M"; break;
			        case "Femenino": sexo = "F"; break;
			        case "Otro": sexo = "X"; break;
			        default: sexo = "X"; break;
		        }
			    
			    
			    // 2. Validar que las contraseñas coincidan
			    
			    if(!pass.equals(passRepetida))
			    	throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden.");
			    
			    // 3. validacion dni / cuil
			    if(!cuil.contains(dni))
			    	throw new AutenticacionException("El cuil no contiene el dni ingresado");
			    
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
				status = false;
				msg = "❌ " + ex1.getMessage();
				reenviarDatos(request, response);
				
			}catch(DniYaRegistradoException ex2) {
				status = false;
				msg = "❌ " + ex2.getMessage();
				reenviarDatos(request, response);
			
			}catch (ContrasenasNoCoincidenException e) {
				status = false;
				msg = "❌ " + e.getMessage();
				reenviarDatos(request, response);
			}catch (AutenticacionException e) {
				status = false;
				reenviarDatos(request, response);
				msg = "❌ " + e.getMessage();
			}catch (Exception e) {
				status = false;
				msg = "❌ Ocurrio un error durante el registro";
				e.printStackTrace();
			}

			request.setAttribute("estado", status);
			request.setAttribute("mensaje", msg);
			
			RequestDispatcher rd = request.getRequestDispatcher("/jsp/admin/altaCliente.jsp");
			rd.forward(request, response);
	}
	
  }
	
	private void reenviarDatos(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    request.setAttribute("nombre", request.getParameter("nombre"));
	    request.setAttribute("apellido", request.getParameter("apellido"));
	    request.setAttribute("dni", request.getParameter("dni"));
	    request.setAttribute("cuil", request.getParameter("cuil"));
	    request.setAttribute("nacionalidad", request.getParameter("nacionalidad"));
	    request.setAttribute("fechanac", request.getParameter("fechanac"));
	    request.setAttribute("direccion", request.getParameter("direccion"));
	    request.setAttribute("numero", request.getParameter("numero"));
	    request.setAttribute("localidad", request.getParameter("localidad"));
	    request.setAttribute("provincia", request.getParameter("provincia"));
	    request.setAttribute("email", request.getParameter("correo"));
	    request.setAttribute("telefono", request.getParameter("telefono"));
	    request.setAttribute("sexo", request.getParameter("sexo"));
	    request.setAttribute("username", request.getParameter("username"));
	    request.setAttribute("pass", request.getParameter("pass"));
	    request.setAttribute("passRepetida", request.getParameter("passRepetida"));
	}

	
}