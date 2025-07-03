package negocio;

import dominio.Cliente;
import excepciones.ClienteNoVinculadoException;

public interface ClienteNegocio {
	Cliente obtenerPorIdUsuario(int idUsuario) throws ClienteNoVinculadoException;

}