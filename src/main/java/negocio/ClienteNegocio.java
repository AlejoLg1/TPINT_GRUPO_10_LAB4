package negocio;

import java.util.List;

import dominio.Cliente;
import dominio.Direccion;
import dominio.Usuario;
import excepciones.AutenticacionException;
import excepciones.ClienteNoVinculadoException;
import excepciones.ContrasenasNoCoincidenException;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;

public interface ClienteNegocio {
	Cliente obtenerPorIdUsuario(int idUsuario) throws ClienteNoVinculadoException;
	
	List<Cliente> obtenerTodosLosClientes();
	
	boolean registrarCliente(Cliente cliente, Usuario usuario, Direccion direccion, String passRepetida) 
	        throws ContrasenasNoCoincidenException, 
	               DniYaRegistradoException, 
	               NombreUsuarioExistenteException, 
	               AutenticacionException, 
	               Exception;
	 boolean modificarCliente(Cliente cliente, Usuario usuario, Direccion direccion) throws Exception;
	 
	 Cliente obtenerClientePorId(int idCliente);
	 
	 boolean darDeBajaCliente(int idCliente) throws Exception;

	 boolean activarCliente(int idCliente) throws Exception;
	 
	 List<Cliente> listarClientesFiltrados(String nombre, String apellido, String dni, String estado) throws Exception;
}