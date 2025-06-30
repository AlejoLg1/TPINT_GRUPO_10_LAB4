package negocioImpl;

import java.sql.Connection;
import java.util.List;
import dao.CuotaDao;
import dao.MovimientosDao;
import dao.CuentaDao;
import daoImpl.CuotaDaoImpl;
import daoImpl.MovimientoDaoImpl;
import daoImpl.PrestamoDaoImpl;
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
        if (cuotas == null || cuotas.isEmpty()) {
            throw new Exception("No se recibió ninguna cuota para procesar.");
        }

        Cuota cuota = cuotas.get(0); // Tomamos la única cuota recibida

        Connection conn = null;
        try {
            conn = utils.Conexion.getConexion().getSQLConexion();
            conn.setAutoCommit(false);

            Cuenta cuenta = cuentaDao.obtenerCuentaPorId(nroCuenta);
            BigDecimal saldoActual = cuenta.getSaldo();

            if (cuota.getMonto() == null || saldoActual.compareTo(cuota.getMonto()) < 0) {
                if (conn != null) conn.rollback();
                return false;
            }

            // Descontar saldo de la cuenta
            boolean saldoActualizado = cuentaDao.actualizarSaldo(
                nroCuenta,
                cuota.getMonto(),
                false, // es resta
                conn
            );
            if (!saldoActualizado) {
                throw new Exception("No se pudo actualizar el saldo para la cuota ID: " + cuota.getIdCuota());
            }

            // Registrar movimiento
            Movimiento mov = new Movimiento();
            mov.setNroCuenta(nroCuenta);
            mov.setDetalle("Pago cuota préstamo N°" + cuota.getIdPrestamo() + " - Cuota " + cuota.getNumeroCuota());
            mov.setImporte(cuota.getMonto().negate());
            mov.setFecha(LocalDateTime.now());
            mov.setIdTipoMovimiento(3); // Pago Cuota Prestamo

            movimientoDao.insertarMovimiento(mov, conn);

            // Marcar cuota como pagada
            boolean pagada = cuotaDao.pagarCuota(cuota, nroCuenta, conn);
            if (!pagada) {
                throw new Exception("No se pudo marcar la cuota como pagada.");
            }

            // Actualizar cuotas_pagadas en Prestamo
            PrestamoDaoImpl prestamoDao = new PrestamoDaoImpl();
            boolean cuotasActualizadas = prestamoDao.actualizarCuotasPagadas(cuota.getIdPrestamo(), conn);
            if (!cuotasActualizadas) {
                throw new Exception("No se pudo actualizar la cantidad de cuotas pagadas en el préstamo ID: " + cuota.getIdPrestamo());
            }

            conn.commit();
            return true;

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
