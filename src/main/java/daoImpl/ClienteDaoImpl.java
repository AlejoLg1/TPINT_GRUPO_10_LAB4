package daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.ClienteDao;
import dominio.Cliente;
import utils.Conexion;
import dominio.Direccion;
import dominio.Usuario;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;

public class ClienteDaoImpl implements ClienteDao {
	
	  private static final String Agregar = 
			  "{CALL InsertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

	@Override
	public boolean Agregar(Cliente cliente, Usuario usuario , Direccion direccion) throws NombreUsuarioExistenteException, DniYaRegistradoException {
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
	        stmt.execute();
	        conexion.commit();
	        agregado = true;
	        System.out.println("Cliente agregado correctamente.");


	    } catch (SQLException e) {
	        System.err.println("Error al insertar cliente: " + e.getMessage());
	        
	        System.err.println("SQLState: " + e.getSQLState());
	        
	        // Si el mensaje contiene el texto de usuario existente
	        if (e.getMessage().contains("El nombre de usuario ya existe.")) {
	            throw new NombreUsuarioExistenteException("El nombre de usuario ya está en uso.");
	        }
	        
	        if (e.getMessage().contains("El DNI de usuario ya esta registrado.")) {
	        	throw new DniYaRegistradoException("El Dni ya se encuentra registrado");
	        }
	        
	        
	    }
	    
	    return agregado;
	}
	
	@Override
	public boolean Modificar(Cliente cliente, Usuario usuario, Direccion direccion) {
	    Connection cn = null;
	    boolean modificado = false;
	    
	    String queryCliente = "UPDATE cliente SET dni=?, cuil=?, nombre=?, apellido=?, sexo=?, "
	            + "nacionalidad=?, fecha_nacimiento=?, correo=?, telefono=? WHERE id_cliente=?";
	    String queryDireccion = "UPDATE direccion SET calle=?, numero=?, localidad=?, provincia=? WHERE id_direccion=?";
	    String queryUsuario = "UPDATE usuario SET nombre_usuario=?, clave=? WHERE id_usuario=?";

	    try {
	        cn = Conexion.getConexion().getSQLConexion();
	        cn.setAutoCommit(false); 

	        // 1. Actualizar cliente
	        try (PreparedStatement pstCliente = cn.prepareStatement(queryCliente)) {
	            pstCliente.setString(1, cliente.getDni());
	            pstCliente.setString(2, cliente.getCuil());
	            pstCliente.setString(3, cliente.getNombre());
	            pstCliente.setString(4, cliente.getApellido());
	            pstCliente.setString(5, cliente.getSexo());
	            pstCliente.setString(6, cliente.getNacionalidad());
	            pstCliente.setDate(7, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
	            pstCliente.setString(8, cliente.getCorreo());
	            pstCliente.setString(9, cliente.getTelefono());
	            pstCliente.setInt(10, cliente.getIdCliente());
	            
	            int filasCliente = pstCliente.executeUpdate();
	            if (filasCliente == 0) {
	                throw new SQLException("No se actualizó el cliente");
	            }
	        }

	        // 2. Actualizar dirección
	        try (PreparedStatement pstDireccion = cn.prepareStatement(queryDireccion)) {
	            pstDireccion.setString(1, direccion.getCalle());
	            pstDireccion.setString(2, direccion.getNumero());
	            pstDireccion.setString(3, direccion.getLocalidad());
	            pstDireccion.setString(4, direccion.getProvincia());
	            pstDireccion.setInt(5, direccion.getId());  // Usar el ID de direccion directamente
	            
	            int filasDireccion = pstDireccion.executeUpdate();
	            if (filasDireccion == 0) {
	                throw new SQLException("No se actualizó la dirección");
	            }
	        }

	        // 3. Actualizar usuario
	        try (PreparedStatement pstUsuario = cn.prepareStatement(queryUsuario)) {
	            pstUsuario.setString(1, usuario.getNombreUsuario());
	            pstUsuario.setString(2, usuario.getClave());
	            pstUsuario.setInt(3, usuario.getIdUsuario());
	            
	            int filasUsuario = pstUsuario.executeUpdate();
	            if (filasUsuario == 0) {
	                throw new SQLException("No se actualizo el usuario");
	            }
	        }

	        cn.commit();
	        modificado = true;
	        
	    } catch (Exception e) {
	        try {
	            if (cn != null) cn.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	        e.printStackTrace();
	    } finally {
	        try {
	            if (cn != null) cn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return modificado;
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
	    List<Cliente> clientes = new ArrayList<>();
	    Connection conexion = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "SELECT id_cliente, nombre, apellido FROM Cliente WHERE estado = 1";
	        stmt = conexion.prepareStatement(query);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            Cliente cliente = new Cliente();
	            cliente.setIdCliente(rs.getInt("id_cliente"));
	            cliente.setNombre(rs.getString("nombre"));
	            cliente.setApellido(rs.getString("apellido"));
	            clientes.add(cliente);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return clientes;
	}

	
	@Override
	public Cliente obtenerPorIdUsuario(int idUsuario) {
	    Cliente cliente = null;
	    Connection conexion = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "SELECT * FROM DatosCliente WHERE id_usuario = ?;";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idUsuario);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            cliente = new Cliente();
	            
	            cliente.setIdCliente(rs.getInt("id_cliente"));
	            cliente.setIdUsuario(rs.getInt("id_usuario"));
	            cliente.setDni(rs.getString("dni"));
	            cliente.setCuil(rs.getString("cuil"));
	            cliente.setNombre(rs.getString("nombre"));
	            cliente.setApellido(rs.getString("apellido"));
	            cliente.setSexo(rs.getString("sexo"));
	            cliente.setNacionalidad(rs.getString("nacionalidad"));
	            cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
	            int id = rs.getInt("id_direccion");
	            String calle = rs.getString("calle");
	            String numero = rs.getString("numero");
	            String localidad = rs.getString("localidad");
	            String provincia = rs.getString("provincia");
	            cliente.setCorreo(rs.getString("correo"));
	            cliente.setTelefono(rs.getString("telefono"));
	            cliente.setEstado(rs.getBoolean("estado"));
	            cliente.setDireccion(new Direccion(id,calle,numero,localidad,provincia));
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (rs != null) rs.close();
	            if (stmt != null) stmt.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return cliente;
	}


	@Override
	public List<Cliente> Listar2() {
		  Connection cn = Conexion.getConexion().getSQLConexion();
	        List<Cliente> lista = new ArrayList<>();

	        String query = "SELECT id_cliente, nombre, apellido, dni, cuil, estado FROM cliente";

	        try {
	            PreparedStatement pst = cn.prepareStatement(query);
	            ResultSet rs = pst.executeQuery();

	            while (rs.next()) {
	                Cliente c = new Cliente();
	                c.setIdCliente(rs.getInt("id_cliente"));
	                c.setNombre(rs.getString("nombre"));
	                c.setApellido(rs.getString("apellido"));
	                c.setDni(rs.getString("dni"));
	                c.setCuil(rs.getString("cuil"));
	                c.setEstado(rs.getBoolean("estado"));

	                lista.add(c);
	            }

	        } catch (Exception e) {
	            e.printStackTrace();
	        }

	        return lista;
	}


	@Override
	public Cliente obtenerPorIdCliente(int idCliente) {
		    Cliente cliente = null;
		    Direccion direccion = null;
		    Usuario usuario = null;
		    Connection conexion = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;

		    try {
		        conexion = Conexion.getConexion().getSQLConexion();
		        String query = "select c.id_cliente, c.id_usuario, c.dni, c.cuil, c.nombre, c.apellido, \r\n"
		        		+ "    c.sexo, c.nacionalidad, c.fecha_nacimiento, c.correo, c.telefono, c.estado,\r\n"
		        		+ "    d.id_direccion, d.calle, d.numero, d.localidad, d.provincia, u.nombre_usuario, u.clave\r\n"
		        		+ "FROM cliente c\r\n"
		        		+ "inner JOIN direccion d ON c.id_direccion = d.id_direccion\r\n"
		        		+ "inner join usuario u on c.id_usuario = u.id_usuario\r\n"
		        		+ "WHERE c.id_cliente = ?";
		        stmt = conexion.prepareStatement(query);
		        stmt.setInt(1, idCliente);
		        rs = stmt.executeQuery();

		        if (rs.next()) {
		            cliente = new Cliente();
		            direccion = new Direccion();
		            usuario = new Usuario(); 
		            
		            cliente.setIdCliente(rs.getInt("id_cliente"));
		            cliente.setDni(rs.getString("dni"));
		            cliente.setCuil(rs.getString("cuil"));
		            cliente.setNombre(rs.getString("nombre"));
		            cliente.setApellido(rs.getString("apellido"));
		            cliente.setSexo(rs.getString("sexo"));
		            cliente.setNacionalidad(rs.getString("nacionalidad"));
		            cliente.setFechaNacimiento(rs.getDate("fecha_nacimiento").toLocalDate());
		            direccion.setId(rs.getInt("id_direccion"));
		            direccion.setCalle(rs.getString("calle"));
		            direccion.setNumero(rs.getString("numero"));
		            direccion.setLocalidad(rs.getString("localidad"));
		            direccion.setProvincia(rs.getString("provincia"));
		            cliente.setCorreo(rs.getString("correo"));
		            cliente.setTelefono(rs.getString("telefono"));
		            cliente.setEstado(rs.getBoolean("estado"));
		            cliente.setDireccion(direccion);
		            usuario.setClave(rs.getString("clave"));
		            usuario.setNombreUsuario(rs.getString("nombre_usuario"));
		            usuario.setIdUsuario(rs.getInt("id_usuario"));
		            cliente.setUsuario(usuario);
		        }

		    } catch (SQLException e) {
		        e.printStackTrace();
		    } finally {
		        try {
		            if (rs != null) rs.close();
		            if (stmt != null) stmt.close();
		            if (conexion != null) conexion.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
		    }

		    return cliente;
		}

}
