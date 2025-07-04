package negocio;

import java.util.List;
import java.util.Map;

import dominio.Prestamo;


public interface PrestamoNegocio {
	List<Prestamo> ListarPrestamos(String busqueda, String montoMinStr, String montoMaxStr, String estado, String fechaSolicitud);
	List<Prestamo> ListarPrestamosFiltrados(String busqueda, String montoMinStr, String montoMaxStr, String estado, String fechaSolicitud);
	Prestamo ObtenerPrestamo(String idPrestamoStr);	
	Map<String, Object> RealizarAccion(int idPrestamo, String accion);
}
