package negocio;

import java.util.List;
import dominio.Cuota;
import dominio.Cuenta;

public interface PagoNegocio {
    List<Cuota> obtenerCuotasPorIds(String[] ids) throws Exception;
    List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception;
}