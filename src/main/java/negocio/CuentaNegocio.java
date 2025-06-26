package negocio;


import java.util.List;
import dominio.Cuenta;

public interface CuentaNegocio {
    List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception;
}