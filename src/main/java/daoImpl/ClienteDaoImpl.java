package daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

import dao.ClienteDao;
import dominio.Cliente;
import utils.Conexion;
import dominio.Direccion;
import dominio.Usuario;

public class ClienteDaoImpl implements ClienteDao {
	
	  private static final String Agregar = 
			  "{CALL InsertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

	@Override
	public boolean Agregar(Cliente cliente, Usuario usuario , Direccion direccion) {
	    Connection conexion = null;
	    CallableStatement stmt = null;
	    
	    int filas = 0;
	    boolean agregado = false;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion(); 
	        stmt = conexion.prepareCall(Agregar);

	        // Parámetros para Usuario
	        stmt.setString(1, usuario.getNombreUsuario());
	        stmt.setString(2,  usuario.getClave());

	        // Parámetros para Direccion
	        stmt.setString(3, direccion.getCalle());
	        stmt.setString(4, direccion.getNumero());
	        stmt.setString(5, direccion.getLocalidad());
	        stmt.setString(6, direccion.getProvincia());

	        // Parámetros para Cliente
	        stmt.setString(7, cliente.getDni());
	        stmt.setString(8, cliente.getCuil());
	        stmt.setString(9, cliente.getNombre());
	        stmt.setString(10, cliente.getApellido());
	        stmt.setString(11, cliente.getSexo());
	        stmt.setString(12, cliente.getNacionalidad());
	        stmt.setDate(13, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
	        stmt.setString(14, cliente.getCorreo());
	        stmt.setString(15, cliente.getTelefono());

	        // Ejecutar procedure
	        filas = stmt.executeUpdate();
	        
	        if (filas > 0) {
	        	agregado = true;
	        	conexion.commit();
	        	System.out.println("Cliente agregado correctamente.");
	        }


	    } catch (SQLException e) {
	        System.err.println("Error al insertar cliente: " + e.getMessage());
	    }
	    
	    return agregado;
	}


	@Override
	public boolean Modificar(Cliente cliente) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean Eliminar(Cliente cliente) {
		// TODO Auto-generated method stub
		return false;
	}



	@Override
	public int ObtenerNuevoId() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public List<Cliente> Listar() {
		// TODO Auto-generated method stub
		return null;
	}

}
