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
		String msg = "✅ Modificacion realizada exitosamente !";
		boolean status = false;
		
		 ClienteDao dao = new ClienteDaoImpl();

		AutenticacionNegocioImpl auth = new AutenticacionNegocioImpl();

		if (usuario == null || !auth.validarRolAdmin(usuario)) {
		    response.sendRedirect(request.getContextPath() + "/ServletMenuAdmin");
		    return;
		}
		
        int idCliente = Integer.parseInt(request.getParameter("idCliente"));
        int idDireccion = Integer.parseInt(request.getParameter("idDireccion"));
        int idUsuario = Integer.parseInt(request.getParameter("idUsuario"));

        // Datos del cliente
        String nombre = request.getParameter("nombre");
        String apellido = request.getParameter("apellido");
        String dni = request.getParameter("dni");
        String cuil = request.getParameter("cuil");
        String nacionalidad = request.getParameter("nacionalidad");
        LocalDate fechaNac = LocalDate.parse(request.getParameter("fechanac"));
        String correo = request.getParameter("email");
        String telefono = request.getParameter("telefono");
        String sexoCompleto = request.getParameter("sexo");
        
        //Datos de Usuario
        String username = request.getParameter("username");
	    String pass = request.getParameter("pass");
	    String passRepetida = request.getParameter("passRepetida");

        // Datos de dirección
	    String Calle = request.getParameter("direccion");
	    String numero = request.getParameter("numero");
	    String localidad = request.getParameter("localidad");
	    String provincia = request.getParameter("provincia");
		 

	    try {
        
	        String sexo;
	        switch (sexoCompleto) {
	            case "Masculino": sexo = "M"; break;
	            case "Femenino": sexo = "F"; break;
	            case "Otro": sexo = "X"; break;
	            default: sexo = "X"; break;
	        }

		    if (!pass.equals(passRepetida)) 
		    	throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden.");

		    if(!cuil.contains(dni))
		    	throw new AutenticacionException("El cuil no contiene el dni ingresado");
		    
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
			status = false;
			msg = "❌ " + ex1.getMessage();
		}catch(DniYaRegistradoException ex2) {
			status = false;
			msg = "❌ " + ex2.getMessage();
		}catch (ContrasenasNoCoincidenException e) {
			status = false;
			msg = "❌ " + e.getMessage();
		}catch (AutenticacionException e) {
			status = false;
			msg = "❌ " + e.getMessage();
		}catch (Exception e) {
			status = false;
			msg = "❌ Ocurrio un error durante la modificacion";
			e.printStackTrace();
		}
	    
        Cliente cliente= dao.obtenerPorIdCliente(id);

	    request.setAttribute("estado", status);
	    request.setAttribute("mensaje", msg);
	    
        if (cliente != null) {
            request.setAttribute("clienteMod", cliente);
            request.getRequestDispatcher("/jsp/admin/altaCliente.jsp").forward(request, response);
        } else {
            response.sendRedirect(request.getContextPath() + "/jsp/admin/clientes.jsp");
        }
	}
	
}
