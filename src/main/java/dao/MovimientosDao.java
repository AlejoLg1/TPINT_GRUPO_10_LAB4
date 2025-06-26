package dao;

import java.sql.Connection;
import java.util.List;
import dominio.Movimiento;
import java.math.BigDecimal;

public interface MovimientosDao {
    List<Movimiento> listarPorCuenta(int nroCuenta);
    public int insertarMovimientoDesdeNegocio(Connection conn, Movimiento mov) throws Exception; 
    public int insertarMovimiento(Movimiento movimiento, Connection conn) throws Exception;
    public  BigDecimal obtenerSaldoPorCuenta(int nroCuenta) throws Exception;
    public int insertarMovimiento(int nroCuenta,int idTipoMovimiento, BigDecimal importe, String detalle);
    
    
    
}