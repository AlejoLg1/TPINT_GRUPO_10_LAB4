package dao;

import java.util.List;

import dominio.Localidad;

public interface LocalidadDao {
	
	List<Localidad> obtenerPorProvincia(int idProvincia);

}
