package daoImpl;

import java.sql.CallableStatement;
import java.sql.Connection;
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


}