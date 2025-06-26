package daoImpl;

import java.sql.*;
import java.util.*;
import dao.CuotaDao;
import dominio.Cuota;
import utils.Conexion;

public class CuotaDaoImpl implements CuotaDao {

    @Override
    public List<Cuota> obtenerCuotasPendientes(int idCliente) throws Exception {
        List<Cuota> lista = new ArrayList<>();
        Connection con = Conexion.getConexion().getSQLConexion();

        String query = "SELECT c.id_cuota, c.id_prestamo, c.nro_cuenta, c.numero_cuota, c.monto, c.fecha_pago, c.estado " +
                       "FROM Cuota c " +
                       "JOIN Prestamo p ON c.id_prestamo = p.id_prestamo " +
                       "WHERE p.id_cliente = ? AND c.estado = 'PENDIENTE'";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, idCliente);
        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Cuota cuota = new Cuota();
            cuota.setIdCuota(rs.getInt("id_cuota"));
            cuota.setIdPrestamo(rs.getInt("id_prestamo"));
            cuota.setNroCuenta(rs.getInt("nro_cuenta"));
            cuota.setNumeroCuota(rs.getInt("numero_cuota"));
            cuota.setMonto(rs.getBigDecimal("monto"));
            cuota.setFechaPago(rs.getDate("fecha_pago").toLocalDate());
            cuota.setEstado(rs.getString("estado"));
            lista.add(cuota);
        }

        rs.close();
        ps.close();
        con.close();

        return lista;
    }

    @Override
    public boolean pagarCuotas(List<Cuota> cuotas, int nroCuenta, Connection conn) throws Exception {
        String update = "UPDATE Cuota SET estado = 'PAGADO', fecha_pago = ? WHERE id_cuota = ?";
        PreparedStatement ps = conn.prepareStatement(update);
        java.sql.Date hoy = java.sql.Date.valueOf(java.time.LocalDate.now());

        for (Cuota c : cuotas) {
            ps.setDate(1, hoy);
            ps.setInt(2, c.getIdCuota());
            ps.addBatch();
        }

        int[] results = ps.executeBatch();
        ps.close();

        for (int result : results) {
            if (result == 0) return false;
        }

        return true;
    }
    
    public Cuota obtenerCuotaPorId(int idCuota) {
        Cuota cuota = null;
        String query = "SELECT id_cuota, id_prestamo, nro_cuenta, numero_cuota, monto, fecha_pago, estado FROM Cuota WHERE id_cuota = ?";

        try (Connection conn = Conexion.getConexion().getSQLConexion();
             PreparedStatement stmt = conn.prepareStatement(query)) {

            stmt.setInt(1, idCuota);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                cuota = new Cuota();
                cuota.setIdCuota(rs.getInt("id_cuota"));
                cuota.setIdPrestamo(rs.getInt("id_prestamo"));
                cuota.setNroCuenta(rs.getInt("nro_cuenta"));
                cuota.setNumeroCuota(rs.getInt("numero_cuota"));
                cuota.setMonto(rs.getBigDecimal("monto"));
                java.sql.Date fechaSql = rs.getDate("fecha_pago");
                if (fechaSql != null) {
                    cuota.setFechaPago(fechaSql.toLocalDate());
                }
                cuota.setEstado(rs.getString("estado"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cuota;
    }
}
