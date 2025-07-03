package daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dominio.Provincia;
import dominio.Localidad;
import dao.ClienteDao;
import dominio.Cliente;
import utils.Conexion;
import dominio.Direccion;
import dominio.Usuario;
import excepciones.DniYaRegistradoException;
import excepciones.NombreUsuarioExistenteException;

public class ClienteDaoImpl implements ClienteDao {
	
	private static final String Agregar = 
		    "{CALL insertarCliente(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)}";

		@Override
		public boolean Agregar(Cliente cliente, Usuario usuario, Direccion direccion) throws NombreUsuarioExistenteException, DniYaRegistradoException, SQLException {
		    
		    boolean agregado = false;
		    
		    Connection conexion = Conexion.getConexion().getSQLConexion();
		    
		    try (
		        CallableStatement stmt = conexion.prepareCall(Agregar);
		    ) {
		        // Parámetros para Usuario
		        stmt.setString(1, usuario.getNombreUsuario());
		        stmt.setString(2, usuario.getClave());

		        // Parámetros para Direccion
		        stmt.setString(3, direccion.getCalle());
		        stmt.setString(4, direccion.getNumero());


		        // Parámetros para Cliente
		        stmt.setString(5, cliente.getDni());
		        stmt.setString(6, cliente.getCuil());
		        stmt.setString(7, cliente.getNombre());
		        stmt.setString(8, cliente.getApellido());
		        stmt.setString(9, cliente.getSexo());
		        stmt.setString(10, cliente.getNacionalidad());
		        stmt.setDate(11, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
		        stmt.setString(12, cliente.getCorreo());
		        stmt.setString(13, cliente.getTelefono());
		        stmt.setInt(14, cliente.getProvincia().getId());
		        stmt.setInt(15, cliente.getLocalidad().getId());

		        // Ejecutar procedure
		        stmt.execute();
		        conexion.commit();
		        agregado = true;
		        System.out.println("✅ Cliente agregado correctamente.");
		        
		    } catch (SQLException e) {
		        conexion.rollback();
		        System.err.println("❌ Error al insertar cliente: " + e.getMessage());

		        if (e.getMessage().contains("usuario ya existe")) {
		            throw new NombreUsuarioExistenteException("El nombre de usuario ya está en uso.");
		        } else if (e.getMessage().contains("DNI")) {
		            throw new DniYaRegistradoException("El DNI ya se encuentra registrado.");
		        } else if (e.getMessage().contains("CUIL")) {
		            throw new DniYaRegistradoException("El CUIL ya se encuentra registrado.");
		        }

		        throw e; // relanzamos si no fue manejada
		    }

		    return agregado;
		}
	
	@Override
	public boolean Modificar(Cliente cliente, Usuario usuario, Direccion direccion) throws Exception {
	    Connection cn = null;
	    boolean modificado = false;
	    
	    String queryCliente = "UPDATE cliente SET dni=?, cuil=?, nombre=?, apellido=?, sexo=?, id_direccion = ?, id_provincia = ?, id_localidad = ?, "
	            + "nacionalidad=?, fecha_nacimiento=?, correo=?, telefono=? WHERE id_cliente=?";
	    String queryUsuario = "UPDATE usuario SET nombre_usuario=?, clave=? WHERE id_usuario=?";
	    String queryDireccion = "UPDATE direccion set calle = ?, numero=? Where id_direccion=?";

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
	            pstCliente.setInt(6, cliente.getDireccion().getId());
	            pstCliente.setInt(7, cliente.getProvincia().getId());
	            pstCliente.setInt(8, cliente.getLocalidad().getId());
	            pstCliente.setString(9, cliente.getNacionalidad());
	            pstCliente.setDate(10, java.sql.Date.valueOf(cliente.getFechaNacimiento()));
	            pstCliente.setString(11, cliente.getCorreo());
	            pstCliente.setString(12, cliente.getTelefono());
	            pstCliente.setInt(13, cliente.getIdCliente());
	  
	            
	            int filasCliente = pstCliente.executeUpdate();
	            if (filasCliente == 0) {
	                throw new SQLException("No se actualizó el cliente");
	            }
	        }
	        
	        //Actualizar Direccion 
	        try (PreparedStatement pstDireccion = cn.prepareStatement(queryDireccion)) {
	            pstDireccion.setString(1, direccion.getCalle());
	            pstDireccion.setString(2, direccion.getNumero());
	            pstDireccion.setInt(3, direccion.getId());
	            
	            int filasUsuario = pstDireccion.executeUpdate();
	            if (filasUsuario == 0) {
	                throw new SQLException("No se actualizo la direccion");
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
	            throw e;
	        }
	        
	        e.printStackTrace();
	        throw e;
	    } finally {
	        try {
	            if (cn != null) cn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	            throw e;
	        }
	    }

	    return modificado;
	}
	


	@Override
	public boolean Eliminar(Cliente cliente, int idUsuario) {
		   Connection cn = Conexion.getConexion().getSQLConexion();
	        boolean eliminado = false;
	        PreparedStatement pstCliente = null;
	        PreparedStatement pstUsuario = null;

	        String queryCliente = "UPDATE cliente SET estado = false WHERE id_cliente = ?";
	        String queryUsuario = "UPDATE usuario SET estado = false WHERE id_usuario = ?";

	        try {
	            pstCliente = cn.prepareStatement(queryCliente);
	            pstUsuario = cn.prepareStatement(queryUsuario);
	            pstCliente.setInt(1, cliente.getIdCliente());
	            pstUsuario.setInt(1, idUsuario);
	            int filasCliente = pstCliente.executeUpdate();
	            int filasUsuario = pstUsuario.executeUpdate();
	            if (filasCliente > 0 && filasUsuario > 0) {
	                cn.commit();
	                eliminado = true;
	            }
	        } catch (Exception e) {
	        	try {
	        		if(cn != null) cn.rollback();
	  		
	        	}catch(SQLException ex)
	        	 {
	        		ex.printStackTrace();
	        	 }
	        	
	            e.printStackTrace();
	        } finally {
	        	try {
	                if (pstCliente != null) pstCliente.close();
	                if (pstUsuario != null) pstUsuario.close();
	                if (cn != null) cn.close();
	            } catch (SQLException e) {
	                e.printStackTrace();
	            }
	        }

	        return eliminado;
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
	            int idDireccion = rs.getInt("id_direccion");
	            int idProvincia = rs.getInt("id_provincia");
	            int idLocalidad = rs.getInt("id_localidad");
	            String calle = rs.getString("calle");
	            String numero = rs.getString("numero");
	            String localidad = rs.getString("nombre_localidad");
	            String provincia = rs.getString("nombre_provincia");
	            cliente.setCorreo(rs.getString("correo"));
	            cliente.setTelefono(rs.getString("telefono"));
	            cliente.setEstado(rs.getBoolean("estado"));
	            cliente.setDireccion(new Direccion(idDireccion,calle,numero));
	            cliente.setProvincia(new Provincia(idProvincia,provincia));
	            cliente.setLocalidad(new Localidad(idLocalidad, localidad));
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
	public List<Cliente> Listar2(String nombre, String apellido, String dni, String estado) {
	    Connection cn = Conexion.getConexion().getSQLConexion();
	    PreparedStatement pst = null;
	    List<Cliente> lista = new ArrayList<>();

	    StringBuilder query = new StringBuilder("SELECT id_cliente, nombre, apellido, dni, cuil, estado FROM cliente WHERE 1=1 ");

	    // Armado dinámico de filtros
	    if (nombre != null && !nombre.trim().isEmpty()) {
	        query.append("AND nombre LIKE ? ");
	    }
	    if (apellido != null && !apellido.trim().isEmpty()) {
	        query.append("AND apellido LIKE ? ");
	    }
	    if (dni != null && !dni.trim().isEmpty()) {
	        query.append("AND dni LIKE ? ");
	    }
	    if (estado != null && !estado.isEmpty()) {
	        if (estado.equals("Activo")) {
	            query.append("AND estado = 1 ");
	        } else if (estado.equals("Inactivo")) {
	            query.append("AND estado = 0 ");
	        }
	    }

	    try {
	        pst = cn.prepareStatement(query.toString());
	        int index = 1;

	        if (nombre != null && !nombre.trim().isEmpty()) {
	            pst.setString(index++, "%" + nombre + "%");
	        }
	        if (apellido != null && !apellido.trim().isEmpty()) {
	            pst.setString(index++, "%" + apellido + "%");
	        }
	        if (dni != null && !dni.trim().isEmpty()) {
	            pst.setString(index++, "%" + dni + "%");
	        }

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
	    } finally {
	        try {
	            if (pst != null) pst.close();
	            if (cn != null) cn.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return lista;
	}



	@Override
	public Cliente obtenerPorIdCliente(int idCliente) {
		    Cliente cliente = null;
		    Direccion direccion = null;
		    Usuario usuario = null;
		    Provincia provincia = null;
		    Localidad localidad = null;
		    Connection conexion = null;
		    PreparedStatement stmt = null;
		    ResultSet rs = null;

		    try {
		        conexion = Conexion.getConexion().getSQLConexion();
		        String query = "select c.id_cliente, c.id_usuario, c.dni, c.cuil, c.nombre, c.apellido, \r\n"
		        		+ "    c.sexo, c.nacionalidad, c.fecha_nacimiento, c.correo, c.telefono, c.estado,\r\n"
		        		+ "    d.id_direccion, d.calle, d.numero, l.id_localidad, l.nombre_localidad, p.id_provincia, p.nombre_provincia, u.nombre_usuario, u.clave\r\n"
		        		+ "FROM cliente c\r\n"
		        		+ "inner JOIN direccion d ON c.id_direccion = d.id_direccion\r\n"
		        		+ "inner join provincia p on c.id_provincia  = p.id_provincia\r\n"
		        		+ "inner join localidad l on c.id_localidad = l.id_localidad\r\n"
		        		+ "inner join usuario u on c.id_usuario = u.id_usuario\r\n"
		        		+ "WHERE c.id_cliente = ?";
		        stmt = conexion.prepareStatement(query);
		        stmt.setInt(1, idCliente);
		        rs = stmt.executeQuery();

		        if (rs.next()) {
		            cliente = new Cliente();
		            direccion = new Direccion();
		            usuario = new Usuario(); 
		            localidad = new Localidad();
		            provincia = new Provincia(); 
		            
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
		            provincia.setId(rs.getInt("id_provincia"));
		            provincia.setNombre(rs.getString("nombre_provincia"));
		            localidad.setId(rs.getInt("id_localidad"));
		            localidad.setNombre(rs.getString("nombre_localidad"));
		            cliente.setCorreo(rs.getString("correo"));
		            cliente.setTelefono(rs.getString("telefono"));
		            cliente.setEstado(rs.getBoolean("estado"));
		            cliente.setDireccion(direccion);
		            usuario.setClave(rs.getString("clave"));
		            usuario.setNombreUsuario(rs.getString("nombre_usuario"));
		            usuario.setIdUsuario(rs.getInt("id_usuario"));
		            cliente.setUsuario(usuario);
		            cliente.setProvincia(provincia);
		            cliente.setLocalidad(localidad);
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
	public int obtenerIdClientePorIdUsuario(int idUsuario) {
	    int idCliente = -1;
	    Connection conexion = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "SELECT id_cliente FROM Cliente WHERE id_usuario = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idUsuario);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            idCliente = rs.getInt("id_cliente");
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

	    return idCliente;
	}

	@Override
	public boolean Activar(Cliente cliente, int idUsuario) {
		  Connection cn = Conexion.getConexion().getSQLConexion();
		  PreparedStatement pstCliente = null;
          PreparedStatement pstUsuario = null;
	        boolean activado = false;

	        String queryCliente = "UPDATE cliente SET estado = true WHERE id_cliente = ?";
	        String queryUsuario = "UPDATE usuario SET estado = true WHERE id_usuario = ?";

	        try {
	            pstCliente = cn.prepareStatement(queryCliente);
	            pstUsuario = cn.prepareStatement(queryUsuario);
	            pstCliente.setInt(1, cliente.getIdCliente());
	            pstUsuario.setInt(1, idUsuario);

	            int filasCliente = pstCliente.executeUpdate();
	            int filasUsuario = pstUsuario.executeUpdate();
	            if (filasCliente > 0 && filasUsuario > 0) {
	                cn.commit();
	                activado = true;
	            }
	        } catch (Exception e) {
	            e.printStackTrace();
	        }finally 
	        {
	        	try {
		            if (pstCliente != null) pstCliente.close();
		            if (pstUsuario != null) pstUsuario.close();
		            if (cn != null) cn.close();
		        } catch (SQLException e) {
		            e.printStackTrace();
		        }
	        }

	        return activado;
	}

}

