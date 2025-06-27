package dao;

import java.util.List;

import dominio.Direccion;
import dominio.Usuario;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;
import dominio.Cliente;

public interface ClienteDao {
	public boolean Agregar(Cliente cliente, Usuario usuario,  Direccion direccion)throws NombreUsuarioExistenteException, DniYaRegistradoException;
	public boolean Modificar(Cliente cliente,Usuario usuario, Direccion direccion)throws NombreUsuarioExistenteException, DniYaRegistradoException;
	public boolean Eliminar(Cliente cliente, int idUsuario);
	public boolean Activar(Cliente cliente, int idUsuario);
	public int ObtenerNuevoId();
	public List<Cliente> Listar();
	public List<Cliente> Listar2();
	public Cliente obtenerPorIdUsuario(int idUsuario);
	public Cliente obtenerPorIdCliente(int idCliente);
	public int obtenerIdClientePorIdUsuario(int idUsuario);
}
