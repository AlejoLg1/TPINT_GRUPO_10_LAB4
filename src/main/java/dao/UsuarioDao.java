package dao;

import java.util.List;

import dominio.Usuario;

public interface UsuarioDao {
	
	public boolean Agregar(Usuario usuario);
	public boolean Modificar(Usuario usuario);
	public boolean Eliminar(Usuario usuario);
	public boolean ValidarUsuario(Usuario usuario);
	public int ObtenerNuevoId();
	public List<Usuario> Listar();
	
}
