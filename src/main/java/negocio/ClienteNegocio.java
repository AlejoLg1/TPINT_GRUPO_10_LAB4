package negocio;

import java.util.List;

import dominio.Cliente;
import excepciones.ClienteNoVinculadoException;

public interface ClienteNegocio {
	Cliente obtenerPorIdUsuario(int idUsuario) throws ClienteNoVinculadoException;
	
	List<Cliente> obtenerTodosLosClientes();

}