package negocioImpl;


import java.math.BigDecimal;
import java.time.LocalDateTime;
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
    public List<Object[]> filtrarCuentas(String busqueda, String dniCliente, String tipoCuenta, String saldoMinStr, String saldoMaxStr) {
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

        return cuentaDao.filtrarCuentas(busqueda, dniCliente, tipoCuenta, saldoMin, saldoMax);
    }
    
    @Override
    public void bajaCuenta(int idCuenta) throws Exception {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("ID de cuenta inválido.");
        }

        cuentaDao.cambiarEstado(idCuenta, false);
    }
    
    @Override
    public void activarCuenta(int idCuenta) throws Exception {
        if (idCuenta <= 0) {
            throw new IllegalArgumentException("ID de cuenta inválido.");
        }

        int idCliente = cuentaDao.obtenerIdClientePorCuenta(idCuenta);
        int cuentasActivas = cuentaDao.contarCuentasActivasPorCliente(idCliente);

        if (cuentasActivas >= 3) {
            throw new Exception("El cliente ya tiene 3 cuentas activas. No se puede activar otra.");
        }

        cuentaDao.cambiarEstado(idCuenta, true);
    }
    
    @Override
    public Cuenta obtenerCuentaPorId(int idCuenta) {
        return cuentaDao.obtenerCuentaPorId(idCuenta);
    }

    @Override
    public String crearCuenta(int idCliente, int idTipoCuenta, BigDecimal monto) {
        int cuentasActivas = cuentaDao.contarCuentasActivasPorCliente(idCliente);
        if (cuentasActivas >= 3) {
            return "❌ El cliente ya tiene 3 cuentas activas.";
        } else {
            Cuenta nueva = new Cuenta();
            nueva.setSaldo(monto);
            nueva.setFechaCreacion(LocalDateTime.now());

            boolean creada = cuentaDao.agregar(nueva, idCliente, idTipoCuenta);
            return creada ? "✅ Cuenta creada correctamente." : "❌ Error al crear la cuenta.";
        }
    }

    @Override
    public String modificarCuenta(int idCuenta, int idCliente, int idTipoCuenta, BigDecimal monto) {
        Cuenta cuenta = cuentaDao.obtenerCuentaPorId(idCuenta);
        if (cuenta != null) {
            cuenta.setIdCliente(idCliente);
            cuenta.setIdTipoCuenta(idTipoCuenta);
            cuenta.setSaldo(monto);

            boolean actualizado = cuentaDao.actualizar(cuenta);
            return actualizado ? "✅ Cuenta modificada correctamente." : "❌ Error al modificar la cuenta.";
        } else {
            return "❌ No se encontró la cuenta a modificar.";
        }
    }
}
