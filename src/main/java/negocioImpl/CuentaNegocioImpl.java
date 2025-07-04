package negocioImpl;


import java.math.BigDecimal;
import java.util.List;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dominio.Cuenta;
import negocio.CuentaNegocio;

public class CuentaNegocioImpl implements CuentaNegocio {

    private CuentaDao cuentaDao = new CuentaDaoImpl();

    @Override
    public List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception {
        return cuentaDao.listarPorCliente(idCliente);
    }
    
    @Override
    public List<Object[]> filtrarCuentas(String busqueda, String tipoCuenta, String saldoMinStr, String saldoMaxStr) {
        BigDecimal saldoMin = null;
        BigDecimal saldoMax = null;

        try {
            if (saldoMinStr != null && !saldoMinStr.isEmpty()) {
                saldoMin = new BigDecimal(saldoMinStr);
            }
            if (saldoMaxStr != null && !saldoMaxStr.isEmpty()) {
                saldoMax = new BigDecimal(saldoMaxStr);
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

        return cuentaDao.filtrarCuentas(busqueda, tipoCuenta, saldoMin, saldoMax);
    }
    
    @Override
    public void bajaCuenta(int idCuenta) throws Exception {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("ID de cuenta inválido.");
        }

        cuentaDao.cambiarEstado(idCuenta, false);
    }
}
