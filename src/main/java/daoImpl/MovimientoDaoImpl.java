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
    
    private static final String AGREGAR_MOV = 
    		"CALL sp_ejecutar_movimiento( ?, ?, ?, ?, ?, ?);";

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

        CallableStatement cs = null;
        int idGenerado = -1;

        try {
            cs = conn.prepareCall(AGREGAR_MOV);
            cs.setInt(1, movimiento.getNroCuenta());
            cs.setInt(2, movimiento.getIdTipoMovimiento());
            cs.setTimestamp(3, Timestamp.valueOf(movimiento.getFecha()));
            cs.setString(4, movimiento.getDetalle());
            cs.setBigDecimal(5, movimiento.getImporte());
            cs.registerOutParameter(6, java.sql.Types.INTEGER);

            cs.execute();

            idGenerado = cs.getInt(6);

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        } finally {
            if (cs != null) cs.close();
        }

        return idGenerado;
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
    
    @Override
    public int insertarMovimiento(int nroCuenta, int idTipoMovimiento, BigDecimal importe, String detalle) {
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet generatedKeys = null;
        int idMovimiento = -1; 

        try {
            conexion = Conexion.getConexion().getSQLConexion();

            
            stmt = conexion.prepareStatement(AGREGAR_MOV, Statement.RETURN_GENERATED_KEYS);
            stmt.setInt(1, nroCuenta);
            stmt.setInt(2, idTipoMovimiento);
            stmt.setString(3, detalle);
            stmt.setBigDecimal(4, importe);

            int filas = stmt.executeUpdate();

            if (filas > 0) {
                generatedKeys = stmt.getGeneratedKeys();
                if (generatedKeys.next()) {
                    idMovimiento = generatedKeys.getInt(1);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace(); // opcional: lanzar excepción personalizada
        } finally {
            try {
                if (generatedKeys != null) generatedKeys.close();
                if (stmt != null) stmt.close();
                if (conexion != null) conexion.close();
            } catch (SQLException ex) {
                ex.printStackTrace();
            }
        }

        return idMovimiento;
    }
    
    public int insertarMovimiento(Movimiento movimiento, Connection conn) throws Exception {
        String query = "INSERT INTO Movimiento (nro_cuenta, id_tipo_movimiento, fecha, detalle, importe) VALUES (?, ?, ?, ?, ?)";
        PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

        ps.setInt(1, movimiento.getNroCuenta());
        ps.setInt(2, movimiento.getIdTipoMovimiento());
        ps.setTimestamp(3, Timestamp.valueOf(movimiento.getFecha())); // Convierte LocalDateTime a Timestamp
        ps.setString(4, movimiento.getDetalle());
        ps.setBigDecimal(5, movimiento.getImporte());

        int rows = ps.executeUpdate();
        if (rows == 0) return -1;

        ResultSet rs = ps.getGeneratedKeys();
        if (rs.next()) return rs.getInt(1);

        return -1;
    }
    
    



}
