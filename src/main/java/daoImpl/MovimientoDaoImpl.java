package daoImpl;

import java.sql.*;
import java.math.BigDecimal;
import java.util.*;
import dominio.Movimiento;
import dao.MovimientosDao;
import utils.Conexion;

public class MovimientoDaoImpl implements MovimientosDao {
    private static final String LISTAR_POR_CUENTA = 
        "SELECT id_movimiento, nro_cuenta, id_tipo_movimiento, fecha, detalle, importe FROM Movimiento WHERE nro_cuenta = ? ORDER BY fecha DESC";

    @Override
    public List<Movimiento> listarPorCuenta(int nroCuenta) {
        List<Movimiento> movimientos = new ArrayList<>();
        try (
            Connection con = Conexion.getConexion().getSQLConexion();
            PreparedStatement ps = con.prepareStatement(LISTAR_POR_CUENTA);
        ) {
            ps.setInt(1, nroCuenta);
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Movimiento m = new Movimiento();
                m.setIdMovimiento(rs.getInt("id_movimiento"));
                m.setNroCuenta(rs.getInt("nro_cuenta"));
                m.setIdTipoMovimiento(rs.getInt("id_tipo_movimiento"));
                m.setFecha(rs.getTimestamp("fecha").toLocalDateTime());
                m.setDetalle(rs.getString("detalle"));
                m.setImporte(rs.getBigDecimal("importe"));
                movimientos.add(m);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return movimientos;
    }
    
    @Override
    public int insertarMovimientoDesdeNegocio(Connection conn, Movimiento movimiento) throws Exception {
        String query = "INSERT INTO Movimiento (nroCuenta, idTipoMovimiento, fecha, detalle, importe) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
        ps.setInt(1, movimiento.getNroCuenta());
        ps.setInt(2, movimiento.getIdTipoMovimiento());
        ps.setTimestamp(3, Timestamp.valueOf(movimiento.getFecha())); // LocalDateTime → Timestamp
        ps.setString(4, movimiento.getDetalle());
        ps.setBigDecimal(5, movimiento.getImporte());

        int rows = ps.executeUpdate();
        if (rows == 0) return -1;

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);

        return -1;
    }
    
    @Override
    public BigDecimal obtenerSaldoPorCuenta(int nroCuenta) throws Exception {
        BigDecimal saldo = BigDecimal.ZERO;
        String query = "SELECT SUM(importe) AS saldo FROM Movimiento WHERE nro_cuenta = ?";

        try (
            Connection con = Conexion.getConexion().getSQLConexion();
            PreparedStatement ps = con.prepareStatement(query);
        ) {
            ps.setInt(1, nroCuenta);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                saldo = rs.getBigDecimal("saldo");
                if (rs.wasNull()) saldo = BigDecimal.ZERO;
            }
        }

        return saldo;
    }
}
