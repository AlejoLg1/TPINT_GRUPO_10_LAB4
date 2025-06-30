package dao;

import java.util.List;

import dominio.Usuario;

public interface UsuarioDao {
	
	public boolean Agregar(Usuario usuario) throws Exception;
	public boolean ExisteNombreUsuario(String nombreUsuario);
	public boolean Modificar(Usuario usuario, String accion) throws Exception;
	public boolean Eliminar(Usuario usuario);
	public boolean ValidarUsuario(Usuario usuario);
	public Usuario obtenerPorId(int id);
	public Usuario obtenerPorCredenciales(String username, String password);
	public Usuario obtenerPorIdCliente(int idCliente);
	public int ObtenerNuevoId();
	public List<Usuario> Listar();
	List<Usuario> listarConFiltros(String nombreUsuario, String rol, String estado);
	public int contarAdminsActivos();
}
