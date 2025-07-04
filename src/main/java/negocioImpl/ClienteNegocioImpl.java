package negocioImpl;

import negocio.ClienteNegocio;

import java.util.List;

import dao.ClienteDao;
import daoImpl.ClienteDaoImpl;
import dominio.Cliente;
import excepciones.ClienteNoVinculadoException;

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
}
