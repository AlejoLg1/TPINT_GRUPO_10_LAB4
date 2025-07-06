package negocioImpl;

import java.util.List;

import dominio.Provincia;
import negocio.ProvinciaNegocio;

import dao.ProvinciaDao;
import daoImpl.ProvinciaDaoImpl;

public class ProvinciaNegocioImpl implements ProvinciaNegocio {
	
	private ProvinciaDao dao = new ProvinciaDaoImpl();

	@Override
	public List<Provincia> obtenerTodas() {
		
		return dao.obtenerTodas();
	}

}
