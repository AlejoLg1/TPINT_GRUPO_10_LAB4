package negocioImpl;

import java.util.List;

import dominio.TipoCuenta;
import negocio.TipoCuentaNegocio;
import dao.TipoCuentaDao;
import daoImpl.TipoCuentaDaoImpl;

public class TipoCuentaNegocioImpl implements TipoCuentaNegocio {
	
	@Override
	public List<TipoCuenta> obtenerTiposDeCuenta() {
	    TipoCuentaDao tipoCuentaDao = new TipoCuentaDaoImpl();
	    return tipoCuentaDao.listar();
	}
}
