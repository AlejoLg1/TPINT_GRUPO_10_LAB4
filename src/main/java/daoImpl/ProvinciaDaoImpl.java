package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ProvinciaDao;
import dominio.Provincia;
import utils.Conexion;

public class ProvinciaDaoImpl implements ProvinciaDao {
	
	public List<Provincia> obtenerTodas() {
		   List<Provincia> provincias = new ArrayList<>();
	        String query = "SELECT id_provincia, nombre_provincia FROM provincia ORDER BY nombre_provincia";

	        try (Connection conn = Conexion.getConexion().getSQLConexion();
	             PreparedStatement ps = conn.prepareStatement(query);
	             ResultSet rs = ps.executeQuery()) {

	            while (rs.next()) {
	            	Provincia provincia = new Provincia();
	                provincia.setId(rs.getInt(1));
	                provincia.setNombre(rs.getString(2));
	                provincias.add(provincia);
	            }

	        } catch (SQLException e) {
	            e.printStackTrace();
	        }

	        return provincias;
	    }
	
	

}
