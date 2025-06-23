package daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.CuentaDao;
import dominio.Cuenta;
import utils.Conexion;

public class CuentaDaoImpl implements CuentaDao {

    private static final String LISTAR_CUENTAS_POR_CLIENTE = "{CALL ListarCuentasPorCliente(?)}";
    private static final String INSERTAR_CUENTA = "{CALL InsertarCuenta(?, ?, ?, ?)}";
    private static final String CONTAR_CUENTAS_ACTIVAS = "SELECT COUNT(*) FROM Cuenta WHERE id_cliente = ? AND estado = TRUE";
    private static final String LISTAR_CUENTAS = """
		    SELECT c.nro_cuenta, c.cbu, tc.descripcion AS tipo_cuenta,
		           cl.nombre, cl.apellido, c.saldo, c.fecha_creacion, c.estado
		    FROM Cuenta c
		    JOIN Cliente cl ON c.id_cliente = cl.id_cliente
		    JOIN Tipo_cuenta tc ON c.id_tipo_cuenta = tc.id_tipo_cuenta
		""";
    private static final String UPDATE_ESTADO_CUENTAS = "UPDATE Cuenta SET estado = ? WHERE nro_cuenta = ?";
	private static final String OBTENER_ID_CLIENTE = "SELECT id_cliente FROM Cuenta WHERE nro_cuenta = ?";
	private static final String OBTENER_CUENTA = "SELECT nro_cuenta, cbu, id_cliente, id_tipo_cuenta, saldo, fecha_creacion, estado FROM Cuenta WHERE nro_cuenta = ?";

    
    @Override
    public List<Cuenta> listarPorCliente(int idCliente) {
        List<Cuenta> cuentas = new ArrayList<>();
        Connection conexion = null;
        CallableStatement stmt = null;
        ResultSet rs = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            stmt = conexion.prepareCall(LISTAR_CUENTAS_POR_CLIENTE);
            stmt.setInt(1, idCliente);
            rs = stmt.executeQuery();

            while (rs.next()) {
                Cuenta cuenta = new Cuenta();
                cuenta.setNroCuenta(rs.getInt("nro_cuenta"));
                cuenta.setCbu(rs.getString("cbu"));
                cuenta.setTipoCuenta(rs.getString("tipo_cuenta"));
                cuenta.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
                cuenta.setSaldo(rs.getBigDecimal("saldo"));
                cuentas.add(cuenta);
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

        return cuentas;
    }
    

    @Override
    public boolean agregar(Cuenta cuenta, int idCliente, int idTipoCuenta) {
        boolean exito = false;
        Connection conexion = null;
        CallableStatement stmt = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            stmt = conexion.prepareCall(INSERTAR_CUENTA);

            stmt.setInt(1, idCliente);
            stmt.setInt(2, idTipoCuenta);
            stmt.setBigDecimal(3, cuenta.getSaldo());
            stmt.registerOutParameter(4, java.sql.Types.INTEGER);

            stmt.execute();
            int nroCuenta = stmt.getInt(4);
            cuenta.setNroCuenta(nroCuenta);

            conexion.commit();
            exito = true;

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (stmt != null) stmt.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return exito;
    }

    
	@Override
	public int contarCuentasActivasPorCliente(int idCliente) {
	    int cantidad = 0;
	    Connection conexion = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        stmt = conexion.prepareStatement(CONTAR_CUENTAS_ACTIVAS);
	        stmt.setInt(1, idCliente);
	        rs = stmt.executeQuery();

	        if (rs.next()) {
	            cantidad = rs.getInt(1);
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

	    return cantidad;
	}
	

	@Override
	public List<Object[]> listarConDatos() {
	    List<Object[]> lista = new ArrayList<>();
	    try (Connection conn = Conexion.getConexion().getSQLConexion();
	         PreparedStatement stmt = conn.prepareStatement(LISTAR_CUENTAS);
	         ResultSet rs = stmt.executeQuery()) {

	        while (rs.next()) {
	            Object[] fila = new Object[8];
	            fila[0] = rs.getInt("nro_cuenta");
	            fila[1] = rs.getString("cbu");
	            fila[2] = rs.getString("tipo_cuenta");
	            fila[3] = rs.getString("nombre") + " " + rs.getString("apellido");
	            fila[4] = rs.getBigDecimal("saldo");
	            fila[5] = rs.getTimestamp("fecha_creacion").toLocalDateTime();
	            fila[6] = rs.getBoolean("estado");
	            fila[7] = rs.getInt("nro_cuenta");
	            lista.add(fila);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	    return lista;
	}

	@Override
	public boolean cambiarEstado(int idCuenta, boolean nuevoEstado) {
	    String sql = UPDATE_ESTADO_CUENTAS;
	    try (Connection conn = Conexion.getConexion().getSQLConexion();
    	     PreparedStatement stmt = conn.prepareStatement(sql)) {

    	    stmt.setBoolean(1, nuevoEstado);
    	    stmt.setInt(2, idCuenta);
    	    int rows = stmt.executeUpdate();

    	    if (rows > 0) {
    	        conn.commit();
    	        return true;
    	    } else {
    	        conn.rollback();
    	        return false;
    	    }
    	} catch (Exception e) {
    	    e.printStackTrace();
    	    return false;
    	}
	}
	

	@Override
	public int obtenerIdClientePorCuenta(int nroCuenta) {
	    int idCliente = -1;
	    try (Connection conn = Conexion.getConexion().getSQLConexion();
	         PreparedStatement stmt = conn.prepareStatement(OBTENER_ID_CLIENTE)) {

	        stmt.setInt(1, nroCuenta);
	        try (ResultSet rs = stmt.executeQuery()) {
	            if (rs.next()) {
	                idCliente = rs.getInt("id_cliente");
	            }
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	    return idCliente;
	}
	
	@Override
	public Cuenta obtenerCuentaPorId(int idCuenta) {
	    Cuenta cuenta = null;

	    try (Connection conn = Conexion.getConexion().getSQLConexion();
	         PreparedStatement stmt = conn.prepareStatement(OBTENER_CUENTA)) {

	        stmt.setInt(1, idCuenta);
	        ResultSet rs = stmt.executeQuery();

	        if (rs.next()) {
	            cuenta = new Cuenta();
	            cuenta.setNroCuenta(rs.getInt("nro_cuenta"));
	            cuenta.setCbu(rs.getString("cbu"));
	            cuenta.setIdCliente(rs.getInt("id_cliente"));
	            cuenta.setIdTipoCuenta(rs.getInt("id_tipo_cuenta"));
	            cuenta.setSaldo(rs.getBigDecimal("saldo"));
	            cuenta.setFechaCreacion(rs.getTimestamp("fecha_creacion").toLocalDateTime());
	            cuenta.setEstado(rs.getBoolean("estado"));
	        }

	        rs.close();

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return cuenta;
	}
	
	@Override
	public boolean actualizar(Cuenta cuenta) {
	    boolean actualizado = false;
	    Connection conexion = null;
	    PreparedStatement stmt = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "UPDATE cuenta SET id_cliente = ?, id_tipo_cuenta = ?, saldo = ? WHERE nro_cuenta = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, cuenta.getIdCliente());
	        stmt.setInt(2, cuenta.getIdTipoCuenta());
	        stmt.setBigDecimal(3, cuenta.getSaldo());
	        stmt.setInt(4, cuenta.getNroCuenta());

	        int filas = stmt.executeUpdate();
	        if (filas > 0) {
	            conexion.commit();  // ✅ Confirmamos los cambios
	            actualizado = true;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return actualizado;
	}


}