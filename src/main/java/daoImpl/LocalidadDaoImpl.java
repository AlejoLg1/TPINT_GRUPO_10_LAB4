package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.LocalidadDao;
import dominio.Localidad;
import utils.Conexion;

public class LocalidadDaoImpl implements LocalidadDao {

	@Override
	public List<Localidad> obtenerPorProvincia(int idProvincia) {
	    List<Localidad> localidades = new ArrayList<>();

	    String query = "SELECT id_localidad, nombre_localidad FROM Localidad WHERE id_provincia = ?";
	    
	    try (
	        Connection conn = Conexion.getConexion().getSQLConexion();
	        PreparedStatement ps = conn.prepareStatement(query);
	    ) {
	        ps.setInt(1, idProvincia);
	        ResultSet rs = ps.executeQuery();

	        while (rs.next()) {
	            Localidad loc = new Localidad();
	            loc.setId(rs.getInt("id_localidad"));
	            loc.setNombre(rs.getString("nombre_localidad"));
	            localidades.add(loc);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace(); // o loguear
	    }

	    return localidades;
	}

}
