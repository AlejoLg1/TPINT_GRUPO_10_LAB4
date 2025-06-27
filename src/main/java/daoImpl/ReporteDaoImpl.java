package daoImpl;
 
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import dominio.Reporte;  
import utils.Conexion;
 
public class ReporteDaoImpl implements dao.ReporteDao {
 
    // Agregué espacios antes de WHERE y AND para evitar que la query quede pegada
    private static final String SQL_ALTAS_CLIENTES = 
        "SELECT COUNT(*) AS total FROM Cliente "
        + "WHERE estado = TRUE "
        + "AND (? IS NULL OR fecha_alta >= ?) "
        + "AND (? IS NULL OR fecha_alta <= ?)";
 
    private static final String SQL_BAJAS_CLIENTES = 
        "SELECT COUNT(*) AS total FROM Cliente "
        + "WHERE estado = FALSE "
        + "AND fecha_baja IS NOT NULL "
        + "AND (? IS NULL OR fecha_baja >= ?) "
        + "AND (? IS NULL OR fecha_baja <= ?)";
 
    private static final String SQL_ESTADO_CUENTAS = 
        "SELECT estado, COUNT(*) AS total FROM Cuenta "
        + "WHERE (? IS NULL OR fecha_creacion >= ?) "
        + "AND (? IS NULL OR fecha_creacion <= ?) "
        + "GROUP BY estado";
 
    private static final String SQL_REGISTRO_DEUDA = 
        "SELECT estado, COUNT(*) AS total, IFNULL(SUM(monto),0) AS monto_total FROM Cuota "
        + "WHERE estado IN ('PENDIENTE', 'ATRASADO') "
        + "AND (? IS NULL OR fecha_pago >= ?) "
        + "AND (? IS NULL OR fecha_pago <= ?) "
        + "GROUP BY estado";
 
    @Override
    public List<Reporte> generarReporte(String tipoReporte, java.util.Date inicio, java.util.Date fin) throws Exception {
        List<Reporte> lista = new ArrayList<>();
 
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
 
        // Convertir a java.sql.Date o null
        Date sqlInicio = (inicio != null) ? new Date(inicio.getTime()) : null;
        Date sqlFin = (fin != null) ? new Date(fin.getTime()) : null;
 
        try {
            conn = Conexion.getConexion().getSQLConexion();
 
            switch (tipoReporte) {
                case "Alta de clientes":
                    ps = conn.prepareStatement(SQL_ALTAS_CLIENTES);
                    setFechas(ps, sqlInicio, sqlFin);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int total = rs.getInt("total");
                        lista.add(new Reporte("Altas de Clientes", total, null));
                    }
                    break;
 
                case "Baja de clientes":
                    ps = conn.prepareStatement(SQL_BAJAS_CLIENTES);
                    setFechas(ps, sqlInicio, sqlFin);
                    rs = ps.executeQuery();
                    if (rs.next()) {
                        int total = rs.getInt("total");
                        lista.add(new Reporte("Bajas de Clientes", total, null));
                    }
                    break;
 
                case "Estado de cuentas":
                    ps = conn.prepareStatement(SQL_ESTADO_CUENTAS);
                    setFechas(ps, sqlInicio, sqlFin);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        boolean estado = rs.getBoolean("estado");
                        int total = rs.getInt("total");
                        String nombre = estado ? "Cuentas Activas" : "Cuentas Inactivas";
                        lista.add(new Reporte(nombre, total, null));
                    }
                    break;
 
                case "Registro de deuda":
                    ps = conn.prepareStatement(SQL_REGISTRO_DEUDA);
                    setFechas(ps, sqlInicio, sqlFin);
                    rs = ps.executeQuery();
                    while (rs.next()) {
                        String estado = rs.getString("estado");
                        int total = rs.getInt("total");
                        java.math.BigDecimal monto = rs.getBigDecimal("monto_total");
                        lista.add(new Reporte("Cuotas " + estado, total, monto));
                    }
                    break;
 
                default:
                    throw new IllegalArgumentException("Tipo de reporte desconocido: " + tipoReporte);
            }
 
            conn.commit();
 
        } catch (SQLException e) {
            if (conn != null) conn.rollback();
            throw e;
        } finally {
            if (rs != null) try { rs.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            if (ps != null) try { ps.close(); } catch (SQLException ex) { ex.printStackTrace(); }
            if (conn != null) try { conn.close(); } catch (SQLException ex) { ex.printStackTrace(); }
        }
 
        return lista;
    }
 
    // Método para setear fechas en los PreparedStatement (4 params: inicio, inicio, fin, fin)
    private void setFechas(PreparedStatement ps, Date inicio, Date fin) throws SQLException {
        // Parámetros en orden: ( ? IS NULL OR campo >= ? ) AND ( ? IS NULL OR campo <= ? )
        ps.setDate(1, inicio);
        ps.setDate(2, inicio);
        ps.setDate(3, fin);
        ps.setDate(4, fin);
    }
}