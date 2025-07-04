package negocio;

import java.util.List;

import dominio.Cuenta;

public interface CuentaNegocio {
    List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception;
    
    List<Object[]> filtrarCuentas(String busqueda, String dniCliente, String tipoCuenta, String saldoMinStr, String saldoMaxStr);
    
    void bajaCuenta(int idCuenta) throws Exception;
    
    void activarCuenta(int idCuenta) throws Exception;
    
    Cuenta obtenerCuentaPorId(int idCuenta);

    String crearCuenta(int idCliente, int idTipoCuenta, java.math.BigDecimal monto);

    String modificarCuenta(int idCuenta, int idCliente, int idTipoCuenta, java.math.BigDecimal monto);
}