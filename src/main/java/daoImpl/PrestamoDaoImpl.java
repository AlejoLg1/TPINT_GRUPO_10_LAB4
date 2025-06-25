package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.PrestamoDao;
import dominio.Cliente;
import dominio.Prestamo;
import dominio.Cuenta;
import utils.Conexion;

public class PrestamoDaoImpl implements PrestamoDao {

    @Override
    public boolean registrarPrestamo(Prestamo prestamo) {
        boolean estado = false;
        Connection con = Conexion.getConexion().getSQLConexion();
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO Prestamo (id_cliente, nro_cuenta, importe_solicitado, cantidad_cuotas, monto_cuota) VALUES (?, ?, ?, ?, ?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, prestamo.get_cliente().getIdCliente());
            stmt.setInt(2, prestamo.get_cuenta().getNroCuenta());
            stmt.setDouble(3, prestamo.getImporte_solicitado());
            stmt.setInt(4, prestamo.getCantidad_cuotas());
            stmt.setDouble(5, prestamo.getMonto_cuota());

            int rows = stmt.executeUpdate();
            estado = rows > 0;
            
            if (estado) {
                con.commit();
            } else {
                con.rollback();
            }

        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return estado;
    }

	@Override
	public List<Prestamo> Listar(){
		List<Prestamo> prestamos = new ArrayList<>();
	    Connection conexion = null;
	    PreparedStatement stmt = null;
	    ResultSet rs = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "SELECT id_prestamo, id_cliente, nro_cuenta, fecha, "
	        		+ "importe_solicitado, cantidad_cuotas, monto_cuota, estado FROM prestamo WHERE autorizacion != 1 and estado = 'Pendiente'";
	        stmt = conexion.prepareStatement(query);
	        rs = stmt.executeQuery();

	        while (rs.next()) {
	            Prestamo prestamo = new Prestamo();
	            
	            int nro_cta = rs.getInt("nro_cuenta");
	            CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
	            Cuenta cuenta = cuentaDao.obtenerCuentaPorId(nro_cta);
	            
	            int id_cliente = rs.getInt("id_cliente");	            
	            ClienteDaoImpl clienteDao = new ClienteDaoImpl();
	            Cliente cliente = clienteDao.obtenerPorIdCliente(id_cliente);
	            
	            prestamo.setId_prestamo(rs.getInt("id_prestamo"));
	            prestamo.set_cliente(cliente);
	            prestamo.set_cuenta(cuenta);
	            prestamo.setFecha(rs.getTimestamp("fecha"));
	            prestamo.setImporte_solicitado(rs.getInt("importe_solicitado"));
	            prestamo.setCantidad_cuotas(rs.getInt("cantidad_cuotas"));
	            prestamo.setMonto_cuota(rs.getDouble("monto_cuota"));
	            prestamo.setAutorizacion(false);
	            prestamo.setEstado(rs.getString("estado"));
	            prestamos.add(prestamo);
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

	    return prestamos;
	}

	@Override
	public boolean aprobarPrestamo(int idPrestamo) {
		boolean actualizado = false;
	    Connection conexion = null;
	    PreparedStatement stmt = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "UPDATE prestamo SET autorizacion = 1, estado = 'Aprobado' WHERE id_prestamo = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idPrestamo);
	        int filas = stmt.executeUpdate();
	        if (filas > 0) {
	            conexion.commit();  
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

	@Override
	public boolean rechazarPrestamo(int idPrestamo) {
		boolean actualizado = false;
	    Connection conexion = null;
	    PreparedStatement stmt = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "UPDATE prestamo SET estado = 'Rechazado' WHERE id_prestamo = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idPrestamo);
	        int filas = stmt.executeUpdate();
	        if (filas > 0) {
	            conexion.commit();  
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