package negocioImpl;

import negocio.ClienteNegocio;

import java.util.List;

import dao.UsuarioDao;
import daoImpl.UsuarioDaoImpl;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import dominio.Direccion;
import dominio.Usuario;
import excepciones.AutenticacionException;
import excepciones.ClienteNoVinculadoException;
import excepciones.ContrasenasNoCoincidenException;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;

public class ClienteNegocioImpl implements ClienteNegocio {
	ClienteDao clienteDao = new ClienteDaoImpl();
	
	@Override
	public Cliente obtenerPorIdUsuario(int idUsuario) throws ClienteNoVinculadoException {
	    Cliente cliente = clienteDao.obtenerPorIdUsuario(idUsuario);
	    if (cliente == null) {
	        throw new ClienteNoVinculadoException("Este usuario no está vinculado a un cliente.");
	    }
	    return cliente;
	}

	@Override
	public List<Cliente> obtenerTodosLosClientes() {
	    ClienteDao clienteDao = new ClienteDaoImpl();
	    return clienteDao.Listar();
	}

	@Override
	public boolean registrarCliente(Cliente cliente, Usuario usuario, Direccion direccion, String passRepetida)
			throws ContrasenasNoCoincidenException, DniYaRegistradoException, NombreUsuarioExistenteException,
			AutenticacionException, Exception {
		
		// Validación de contraseña
        if (!usuario.getClave().equals(passRepetida)) {
            throw new ContrasenasNoCoincidenException("Las contraseñas no coinciden.");
        }

        // Validación DNI en CUIL
        if (!cliente.getCuil().contains(cliente.getDni())) {
            throw new AutenticacionException("El CUIL no contiene el DNI ingresado.");
        }

        // Validaciones adicionales
        if (usuario.getNombreUsuario().contains(" ")) {
            throw new AutenticacionException("El nombre de usuario no puede contener espacios.");
        }

        if (usuario.getClave().contains(" ")) {
            throw new ContrasenasNoCoincidenException("La contraseña no puede contener espacios.");
        }

        if (cliente.getLocalidad() == null || cliente.getLocalidad().getId() == 0) {
            throw new Exception("Debe seleccionar una localidad válida.");
        }

        return clienteDao.Agregar(cliente, usuario, direccion);
	}

	@Override
	public boolean modificarCliente(Cliente cliente, Usuario usuario, Direccion direccion) throws Exception {
		// Validaciones
        if (usuario.getNombreUsuario().contains(" ")) {
            throw new NombreUsuarioExistenteException("El nombre de usuario no puede contener espacios.");
        }

        if (!cliente.getCuil().contains(cliente.getDni())) {
            throw new DniYaRegistradoException("El cuil no contiene el dni ingresado");
        }

        if (usuario.getClave().contains(" ")) {
            throw new Exception("La contraseña no puede contener espacios.");
        }

        return clienteDao.Modificar(cliente, usuario, direccion);
	}

	@Override
	public Cliente obtenerClientePorId(int idCliente) {
		return clienteDao.obtenerPorIdCliente(idCliente);
	}

	@Override
	public boolean darDeBajaCliente(int idCliente) throws Exception {
	    ClienteDao clienteDao = new ClienteDaoImpl();
	    UsuarioDao usuarioDao = new UsuarioDaoImpl();

	    // Buscar usuario asociado al cliente
	    Usuario usuario = usuarioDao.obtenerPorIdCliente(idCliente);
	    if (usuario == null) {
	        throw new Exception("No se encontró el usuario asociado al cliente.");
	    }

	    Cliente cliente = new Cliente();
	    cliente.setIdCliente(idCliente);
	    cliente.setEstado(false); // dar de baja

	    return clienteDao.Eliminar(cliente, usuario.getIdUsuario());
	}

	@Override
	public boolean activarCliente(int idCliente) throws Exception {
	    ClienteDao clienteDao = new ClienteDaoImpl();
	    UsuarioDao usuarioDao = new UsuarioDaoImpl();

	    Usuario usuario = usuarioDao.obtenerPorIdCliente(idCliente);
	    if (usuario == null) {
	        throw new Exception("No se encontró el usuario asociado al cliente.");
	    }

	    Cliente cliente = new Cliente();
	    cliente.setIdCliente(idCliente);
	    cliente.setEstado(true);

	    return clienteDao.Activar(cliente, usuario.getIdUsuario());
	}
	
	@Override
    public List<Cliente> listarClientesFiltrados(String nombre, String apellido, String dni, String estado) throws Exception {
        ClienteDao dao = new ClienteDaoImpl();
        return dao.Listar2(nombre, apellido, dni, estado);
    }
	
}
