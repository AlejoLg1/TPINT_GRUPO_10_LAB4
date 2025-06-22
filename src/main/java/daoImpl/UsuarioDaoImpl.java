package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
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
	public boolean Modificar(Usuario usuario, String accion) {
	    Connection cn = Conexion.getConexion().getSQLConexion();
	    boolean modificado = false;

	    String query = "";
	    java.sql.PreparedStatement pst = null;

	    try {
	        switch (accion) {
	            case "estado":
	                query = "UPDATE Usuario SET estado = ? WHERE id_usuario = ?";
	                pst = cn.prepareStatement(query);
	                pst.setBoolean(1, usuario.isEstado());
	                pst.setInt(2, usuario.getIdUsuario());
	                break;

	            case "datos":
	                query = "UPDATE Usuario SET tipo = ?, clave = ?, is_admin = ? WHERE id_usuario = ?";
	                pst = cn.prepareStatement(query);
	                pst.setString(1, usuario.getTipo());
	                pst.setString(2, usuario.getClave());
	                pst.setBoolean(3, usuario.isAdmin());
	                pst.setInt(4, usuario.getIdUsuario());
	                break;

	            default:
	                return false; // acción inválida
	        }

	        int filas = pst.executeUpdate();
	        if (filas > 0) {
	            cn.commit();
	            modificado = true;
	        }

	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    return modificado;
	}


    @Override
    public boolean Eliminar(Usuario usuario) {
        Connection cn = Conexion.getConexion().getSQLConexion();
        boolean eliminado = false;

        String query = "UPDATE Usuario SET estado = false WHERE id_usuario = ?";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            pst.setInt(1, usuario.getIdUsuario());

            int filas = pst.executeUpdate();
            if (filas > 0) {
                cn.commit();
                eliminado = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return eliminado;
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
        Connection cn = Conexion.getConexion().getSQLConexion();
        List<Usuario> lista = new ArrayList<>();

        String query = "SELECT id_usuario, nombre_usuario, clave, tipo, is_admin, estado FROM Usuario";

        try {
            PreparedStatement pst = cn.prepareStatement(query);
            ResultSet rs = pst.executeQuery();

            while (rs.next()) {
                Usuario u = new Usuario();
                u.setIdUsuario(rs.getInt("id_usuario"));
                u.setNombreUsuario(rs.getString("nombre_usuario"));
                u.setClave(rs.getString("clave"));
                u.setTipo(rs.getString("tipo"));
                u.setIsAdmin(rs.getBoolean("is_admin"));
                u.setEstado(rs.getBoolean("estado"));

                lista.add(u);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return lista;
    }

}
