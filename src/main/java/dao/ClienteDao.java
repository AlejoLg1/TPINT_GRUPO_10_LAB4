package dao;

import java.util.List;

import dominio.Direccion;
import dominio.Usuario;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;
import dominio.Cliente;

public interface ClienteDao {
	public boolean Agregar(Cliente cliente, Usuario usuario,  Direccion direccion)throws NombreUsuarioExistenteException, DniYaRegistradoException;
	public boolean Modificar(Cliente cliente);
	public boolean Eliminar(Cliente cliente);
	public int ObtenerNuevoId();
	public List<Cliente> Listar();
	public Cliente obtenerPorIdUsuario(int idUsuario);
}
