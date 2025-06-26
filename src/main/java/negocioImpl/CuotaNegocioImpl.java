package negocioImpl;

import java.sql.Connection;
import java.util.List;
import dao.CuotaDao;
import dao.MovimientosDao;
import dao.CuentaDao;
import daoImpl.CuotaDaoImpl;
import daoImpl.MovimientoDaoImpl;
import daoImpl.CuentaDaoImpl;
import dominio.Cuota;
import dominio.Cuenta;
import dominio.Movimiento;
import negocio.CuotaNegocio;
import java.time.LocalDateTime;
import java.math.BigDecimal;

public class CuotaNegocioImpl implements CuotaNegocio {

    private CuotaDao cuotaDao = new CuotaDaoImpl();
    private MovimientosDao movimientoDao = new MovimientoDaoImpl();
    private CuentaDao cuentaDao = new CuentaDaoImpl();

    @Override
    public List<Cuota> listarCuotasPendientesPorCliente(int idCliente) throws Exception {
        return cuotaDao.obtenerCuotasPendientes(idCliente);
    }

    @Override
    public boolean procesarPagoCuotas(List<Cuota> cuotas, int nroCuenta) throws Exception {
        Connection conn = null;
        try {
            conn = utils.Conexion.getConexion().getSQLConexion();
            conn.setAutoCommit(false);

            // Calcular total
            BigDecimal total = BigDecimal.ZERO;
            for (Cuota c : cuotas) {
                if (c.getMonto() != null) {
                    total = total.add(c.getMonto());
                    System.out.println("- Cuota ID: " + c.getIdCuota() + " | Monto: " + c.getMonto());
                } else {
                    System.out.println("⚠️ Cuota con ID " + c.getIdCuota() + " tiene monto nulo");
                }
            }

            // Obtener saldo actual
            Cuenta cuenta = cuentaDao.obtenerCuentaPorId(nroCuenta);
            BigDecimal saldo = cuenta.getSaldo();

            if (saldo.compareTo(total) < 0) {
                throw new Exception("Saldo insuficiente");
            }

            // Descontar saldo de la cuenta
            BigDecimal nuevoSaldo = saldo.subtract(total);
            boolean saldoActualizado = cuentaDao.actualizarSaldo(nroCuenta, nuevoSaldo);
            if (!saldoActualizado) {
                throw new Exception("No se pudo actualizar el saldo de la cuenta.");
            }

            // Registrar movimiento negativo
            Movimiento mov = new Movimiento();
            mov.setNroCuenta(nroCuenta);
            mov.setDetalle("Pago de cuotas de préstamo");
            mov.setImporte(total.negate());
            mov.setFecha(LocalDateTime.now());
            mov.setIdTipoMovimiento(2); // Asegurate que este ID exista en tipo_movimiento

            movimientoDao.insertarMovimiento(mov, conn);

            // Marcar cuotas como pagadas
            boolean pagadas = cuotaDao.pagarCuotas(cuotas, nroCuenta, conn);

            conn.commit();
            return pagadas;
        } catch (Exception ex) {
            if (conn != null) conn.rollback();
            throw ex;
        } finally {
            if (conn != null) conn.close();
        }
    }

    public Cuota obtenerCuotaPorId(int idCuota) throws Exception {
        return cuotaDao.obtenerCuotaPorId(idCuota);
    }
}
