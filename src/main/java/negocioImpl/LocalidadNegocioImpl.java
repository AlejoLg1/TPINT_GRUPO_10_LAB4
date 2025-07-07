package negocioImpl;

import java.util.List;

import dao.LocalidadDao;
import daoImpl.LocalidadDaoImpl;
import dominio.Localidad;
import negocio.LocalidadNegocio;

public class LocalidadNegocioImpl implements LocalidadNegocio {
	
	private LocalidadDao dao = new LocalidadDaoImpl();

	@Override
	public List<Localidad> obtenerPorProvincia(int idProvincia){
		
		if (idProvincia <= 0) {
            throw new IllegalArgumentException("El ID de provincia es inválido.");
        }
		
		return dao.obtenerPorProvincia(idProvincia);
	}

}
