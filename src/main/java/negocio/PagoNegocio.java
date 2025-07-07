package negocio;

import java.util.List;
import dominio.Cuota;
import dominio.Cuenta;

public interface PagoNegocio {
    List<Cuota> obtenerCuotasPorIds(String[] ids) throws Exception;
    List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception;
    
    List<Cuota> obtenerCuotasPendientesConFiltros(int idCliente, Integer idPrestamo, String fechaVencimiento, String estado) throws Exception;

    boolean procesarPagoCuotaIndividual(int idCuota, int nroCuenta) throws Exception;

    Cuota obtenerCuotaPorId(int idCuota) throws Exception;

   
}