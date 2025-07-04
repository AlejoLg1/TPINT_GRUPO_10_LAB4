package negocioImpl;


import java.util.List;

import dao.MovimientosDao;
import daoImpl.MovimientoDaoImpl;
import dominio.Movimiento;
import negocio.MovimientoNegocio;

public class MovimientoNegocioImpl implements MovimientoNegocio {
	
	private MovimientosDao movimientoDao = new MovimientoDaoImpl();
	
	@Override
	public List<Movimiento> ObtenerMovimientosPorNroCuenta(int nroCuenta) throws Exception{
		return movimientoDao.listarPorCuenta(nroCuenta);
	}
	
}
