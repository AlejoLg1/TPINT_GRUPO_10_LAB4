package daoImpl;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import dominio.Movimiento;
import utils.Conexion;

public class MovimientoDaoImpl {
    private static final String LISTAR_POR_CUENTA = 
        "SELECT id_movimiento, nro_cuenta, id_tipo_movimiento, fecha, detalle, importe FROM Movimiento WHERE nro_cuenta = ? ORDER BY fecha DESC";

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
}