package daoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import dao.TipoCuentaDao;
import dominio.TipoCuenta;
import utils.Conexion;

public class TipoCuentaDaoImpl implements TipoCuentaDao {

    private static final String LISTAR_TIPOS = "SELECT id_tipo_cuenta, descripcion FROM Tipo_cuenta";

    @Override
    public List<TipoCuenta> listar() {
        List<TipoCuenta> tipos = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();
            stmt = conexion.prepareStatement(LISTAR_TIPOS);
            rs = stmt.executeQuery();

            while (rs.next()) {
                TipoCuenta tipo = new TipoCuenta();
                tipo.setIdTipoCuenta(rs.getInt("id_tipo_cuenta"));
                tipo.setDescripcion(rs.getString("descripcion"));
                tipos.add(tipo);
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

        return tipos;
    }
}
