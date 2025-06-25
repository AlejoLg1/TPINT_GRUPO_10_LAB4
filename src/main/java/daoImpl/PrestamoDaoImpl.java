package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;

import dao.PrestamoDao;
import dominio.Prestamo;
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
            stmt.setInt(1, prestamo.getId_cliente());
            stmt.setInt(2, prestamo.getNro_cuenta());
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
}