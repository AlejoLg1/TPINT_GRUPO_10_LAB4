package dao;

import java.util.List;


import dominio.Prestamo;

public interface PrestamoDao {
	List<Prestamo> ListarConFiltros(String busqueda, Double montoMin, Double montoMax, String estado, String fechaSolicitud);
	public List<Prestamo> obtenerPrestamosFiltrados(String busqueda, Double montoMin, Double montoMax, String estado, String fechaSolicitud);
	Prestamo obtenerPrestamoPorId(int idPrestamo);
    boolean registrarPrestamo(Prestamo prestamo);
	boolean aprobarPrestamo(int nroPrestamo);
	boolean rechazarPrestamo(int nroPrestamo);
}