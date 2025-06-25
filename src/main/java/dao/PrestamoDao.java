package dao;

import java.util.List;

import dominio.Cliente;
import dominio.Prestamo;

public interface PrestamoDao {
    boolean registrarPrestamo(Prestamo prestamo);
	public List<Prestamo> Listar();
	boolean aprobarPrestamo(int nroPrestamo);
	boolean rechazarPrestamo(int nroPrestamo);
}