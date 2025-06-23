package dao;
import java.util.List;

import dominio.Movimiento;

public interface MovimientosDao {
	
	public List<Movimiento> listarPorCuenta(int nroCuenta);
	

}
