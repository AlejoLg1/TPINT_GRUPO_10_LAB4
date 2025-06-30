package dao;

import java.sql.Connection;
import java.util.List;
import dominio.Movimiento;
import java.math.BigDecimal;

public interface MovimientosDao {
    List<Movimiento> listarPorCuenta(int nroCuenta);
    public int insertarMovimiento(Movimiento mov, Connection conn) throws Exception; 
    public  BigDecimal obtenerSaldoPorCuenta(int nroCuenta) throws Exception;
}
