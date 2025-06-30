package negocio;

import java.util.List;
import dominio.Cuota;

public interface CuotaNegocio {
    List<Cuota> listarCuotasPendientesPorCliente(int idCliente) throws Exception;

    boolean procesarPagoCuotas(List<Cuota> cuotas, int nroCuenta) throws Exception;

	List<Cuota> listarCuotasPendientesConFiltros(int idCliente, Integer idPrestamo, String fechaVencimiento,
			String estado) throws Exception;
}