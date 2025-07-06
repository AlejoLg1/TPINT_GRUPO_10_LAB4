package negocio;

import java.util.List;
import dominio.Localidad;

public interface LocalidadNegocio {

	 List<Localidad> obtenerPorProvincia(int idProvincia);
}
