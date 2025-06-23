package dao;

import java.util.List;

import dominio.Direccion;
import dominio.Usuario;
import dominio.Cliente;

public interface ClienteDao {
	public boolean Agregar(Cliente cliente, Usuario usuario,  Direccion direccion);
	public boolean Modificar(Cliente cliente);
	public boolean Eliminar(Cliente cliente);
	public int ObtenerNuevoId();
	public List<Cliente> Listar();
}
