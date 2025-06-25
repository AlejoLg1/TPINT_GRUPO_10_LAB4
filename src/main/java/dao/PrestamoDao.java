package dao;

import java.util.List;

import dominio.Cliente;
import dominio.Prestamo;

public interface PrestamoDao {
    boolean registrarPrestamo(Prestamo prestamo);
	public List<Prestamo> Listar();
}