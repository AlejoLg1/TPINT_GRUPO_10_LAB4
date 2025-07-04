package negocio;

import java.util.List;

import dominio.Movimiento;

public interface MovimientoNegocio {
	
	List<Movimiento> ObtenerMovimientosPorNroCuenta(int nroCuenta) throws Exception;
}
