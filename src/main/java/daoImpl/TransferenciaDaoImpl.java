package daoImpl;

import java.sql.*;
import java.util.*;
import java.math.BigDecimal;
import dominio.Transferencia;
import dao.TransferenciaDao;
import utils.Conexion;

public class TransferenciaDaoImpl implements TransferenciaDao {

	@Override
	public boolean insertarTransferencia(Connection conn, Transferencia t) throws Exception {
	    String query = "INSERT INTO Transferencia (id_movimiento_salida, id_movimiento_entrada, importe, fecha) VALUES (?, ?, ?, ?)";
	    PreparedStatement ps = conn.prepareStatement(query);
	    ps.setInt(1, t.getIdMovimientoSalida());
	    ps.setInt(2, t.getIdMovimientoEntrada());
	    ps.setBigDecimal(3, BigDecimal.valueOf(t.getImporte()));
	    ps.setDate(4, java.sql.Date.valueOf(t.getFecha()));

	    int rows = ps.executeUpdate();
	    ps.close();
	    return rows > 0;
	}

    @Override
    public List<Transferencia> obtenerTransferenciasPorCuenta(int nroCuenta) throws Exception {
        List<Transferencia> lista = new ArrayList<>();
        Connection con = Conexion.getConexion().getSQLConexion();

        String query = "SELECT t.id_transferencia, t.id_movimiento_salida, t.id_movimiento_entrada, t.importe, t.fecha, " +
                       "m1.nro_cuenta AS cuenta_origen, m2.nro_cuenta AS cuenta_destino " +
                       "FROM Transferencia t " +
                       "JOIN Movimiento m1 ON t.id_movimiento_salida = m1.id_movimiento " +
                       "JOIN Movimiento m2 ON t.id_movimiento_entrada = m2.id_movimiento " +
                       "WHERE m1.nro_cuenta = ? OR m2.nro_cuenta = ?";

        PreparedStatement ps = con.prepareStatement(query);
        ps.setInt(1, nroCuenta);
        ps.setInt(2, nroCuenta);

        ResultSet rs = ps.executeQuery();

        while (rs.next()) {
            Transferencia t = new Transferencia();
            t.setId(rs.getInt("id_transferencia"));
            t.setIdMovimientoSalida(rs.getInt("id_movimiento_salida"));
            t.setIdMovimientoEntrada(rs.getInt("id_movimiento_entrada"));
            t.setImporte(rs.getDouble("importe"));
            t.setFecha(rs.getDate("fecha").toLocalDate());
            t.setNroCuentaOrigen(rs.getInt("cuenta_origen"));
            t.setNroCuentaDestino(rs.getInt("cuenta_destino"));

            lista.add(t);
        }

        rs.close();
        ps.close();
        con.close();

        return lista;
    }
}
