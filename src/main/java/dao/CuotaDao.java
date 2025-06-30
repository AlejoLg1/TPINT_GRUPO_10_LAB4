package dao;


import java.sql.Connection;
import java.util.List;
import dominio.Cuota;


public interface CuotaDao {

    List<Cuota> obtenerCuotasPendientes(int idCliente) throws Exception;
    boolean crearCuota(Cuota cuota, Connection conn);
    boolean pagarCuota(Cuota cuota, int nroCuenta, Connection conn) throws Exception;
    Cuota obtenerCuotaPorId(int idCuota);
	List<Cuota> obtenerCuotasPendientesConFiltros(int idCliente, Integer idPrestamo, String fechaVencimiento,
			String estado) throws Exception;
}