package daoImpl;

import java.sql.Connection;
import java.util.List;

import dao.UsuarioDao;
import dominio.Usuario;
import utils.Conexion;

public class UsuarioDaoImpl implements UsuarioDao {

	@Override
	public boolean Agregar(Usuario usuario) {
	    Connection cn = Conexion.getConexion().getSQLConexion();
	    boolean agregado = false;

	    String query = "INSERT INTO Usuario (nombre_usuario, clave, tipo, is_admin, estado) VALUES (?, ?, ?, ?, ?)";

	    try {
	        java.sql.PreparedStatement pst = cn.prepareStatement(query);
	        pst.setString(1, usuario.getNombreUsuario());
	        pst.setString(2, usuario.getClave());
	        pst.setString(3, usuario.getTipo()); // "BANCARIO" o "CLIENTE"
	        pst.setBoolean(4, usuario.isAdmin());
	        pst.setBoolean(5, usuario.isEstado());

	        int filas = pst.executeUpdate();

	        if (filas > 0) {
	            cn.commit();
	            agregado = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return agregado;
	}



    @Override
    public boolean Modificar(Usuario usuario) {
        // TODO: implementar lógica para modificar un usuario existente
        return false;
    }

    @Override
    public boolean Eliminar(Usuario usuario) {
        // TODO: implementar lógica para eliminar (o desactivar) un usuario
        return false;
    }

    @Override
    public boolean ValidarUsuario(Usuario usuario) {
        // TODO: implementar lógica para validar credenciales del usuario
        return false;
    }

    @Override
    public int ObtenerNuevoId() {
        // TODO: implementar lógica para obtener el próximo ID disponible
        return 0;
    }

    @Override
    public List<Usuario> Listar() {
        // TODO: implementar lógica para listar todos los usuarios
        return null;
    }
}
