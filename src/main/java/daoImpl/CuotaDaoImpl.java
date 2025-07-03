package daoImpl;

import java.sql.*;
import java.sql.Date;
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
    public boolean pagarCuota(Cuota cuota, int nroCuenta, Connection conn) throws Exception {
        String update = "UPDATE Cuota SET estado = 'PAGADO', fecha_pago = ? WHERE id_cuota = ?";
        try (PreparedStatement ps = conn.prepareStatement(update)) {
            java.sql.Date hoy = java.sql.Date.valueOf(java.time.LocalDate.now());

            ps.setDate(1, hoy);
            ps.setInt(2, cuota.getIdCuota());

            int result = ps.executeUpdate();
            return result > 0;

        } catch (Exception e) {
            e.printStackTrace();
            throw new Exception("Error al pagar la cuota ID: " + cuota.getIdCuota(), e);
        }
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
    
    public boolean crearCuota(Cuota cuota, Connection conn) {
        boolean creada = false;
        String sql = "INSERT INTO cuota (id_prestamo, nro_cuenta, numero_cuota, monto, fecha_pago, fecha_vencimiento, estado) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement pst = conn.prepareStatement(sql)) {
            pst.setInt(1, cuota.getIdPrestamo());
            pst.setInt(2, cuota.getNroCuenta());
            pst.setInt(3, cuota.getNumeroCuota());
            pst.setBigDecimal(4, cuota.getMonto());

            if (cuota.getFechaPago() != null) {
                pst.setDate(5, java.sql.Date.valueOf(cuota.getFechaPago()));
            } else {
                pst.setNull(5, java.sql.Types.DATE);
            }
            
            pst.setDate(6, java.sql.Date.valueOf(cuota.getFechaVencimiento()));
            pst.setString(7, cuota.getEstado());

            int filas = pst.executeUpdate();
            if (filas > 0) {
                creada = true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return creada;
    }

    
    @Override
    public List<Cuota> obtenerCuotasPendientesConFiltros(int idCliente, Integer idPrestamo, String fechaVencimiento, String estado) throws Exception {
        List<Cuota> lista = new ArrayList<>();
        Connection con = Conexion.getConexion().getSQLConexion();

        String query = "SELECT c.id_cuota, c.id_prestamo, c.nro_cuenta, c.numero_cuota, c.monto, c.fecha_pago, c.fecha_vencimiento, c.estado " +
                       "FROM Cuota c " +
                       "JOIN Prestamo p ON c.id_prestamo = p.id_prestamo " +
                       "WHERE p.id_cliente = ?";

        List<Object> params = new ArrayList<>();
        params.add(idCliente);

        if (idPrestamo != null) {
            query += " AND c.id_prestamo = ?";
            params.add(idPrestamo);
        }

        if (fechaVencimiento != null && !fechaVencimiento.isEmpty()) {
            query += " AND DATE(c.fecha_vencimiento) = ?";
            params.add(java.sql.Date.valueOf(fechaVencimiento));
        }

        if (estado != null && !estado.isEmpty()) {
            query += " AND c.estado = ?";
            params.add(estado);
        }

        PreparedStatement ps = con.prepareStatement(query);

        for (int i = 0; i < params.size(); i++) {
            ps.setObject(i + 1, params.get(i));
        }

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Cuota cuota = new Cuota();
            cuota.setIdCuota(rs.getInt("id_cuota"));
            cuota.setIdPrestamo(rs.getInt("id_prestamo"));
            cuota.setNroCuenta(rs.getInt("nro_cuenta"));
            cuota.setNumeroCuota(rs.getInt("numero_cuota"));
            cuota.setMonto(rs.getBigDecimal("monto"));

            // ✅ Validación null para fecha_pago
            Date fechaPagoSql = rs.getDate("fecha_pago");
            if (fechaPagoSql != null) {
                cuota.setFechaPago(fechaPagoSql.toLocalDate());
            } else {
                cuota.setFechaPago(null);
            }
            
            cuota.setFechaVencimiento(rs.getDate("fecha_vencimiento").toLocalDate());
            cuota.setEstado(rs.getString("estado"));
            lista.add(cuota);
        }


        rs.close();
        ps.close();
        con.close();

        return lista;
    }


}
