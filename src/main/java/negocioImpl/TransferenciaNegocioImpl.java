package negocioImpl;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import dao.MovimientosDao;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dao.TransferenciaDao;
import daoImpl.MovimientoDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Cuenta;
import dominio.Movimiento;
import dominio.Transferencia;
import excepciones.CuentaExistenteExcenption;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;
import negocio.TransferenciaNegocio;
import utils.Conexion;

public class TransferenciaNegocioImpl implements TransferenciaNegocio {
	
	@Override
	public boolean transferirDesdeFormulario(String nroCuentaOrigen, String cbuDestino, String montoStr) throws Exception {
	    if (nroCuentaOrigen == null || montoStr == null || cbuDestino == null ||
	        nroCuentaOrigen.trim().isEmpty() || montoStr.trim().isEmpty() || cbuDestino.trim().isEmpty()) {
	        throw new IllegalArgumentException("Todos los campos son obligatorios.");
	    }

	    if (nroCuentaOrigen.equals("invalid")) {
	        throw new TransferenciaException("Seleccione una cuenta de origen válida.");
	    }

	    int nroOrigen = Integer.parseInt(nroCuentaOrigen);
	    int nroDestino = obtenerNroCuentaDestino(cbuDestino);
	    double monto = Double.parseDouble(montoStr);

	    Transferencia transferencia = new Transferencia();
	    transferencia.setNroCuentaOrigen(nroOrigen);
	    transferencia.setNroCuentaDestino(nroDestino);
	    transferencia.setImporte(monto);

	    return registrarTransferencia(transferencia);
	}

	private int obtenerNroCuentaDestino(String cbu) throws CuentaExistenteExcenption {
	    CuentaDao cuentaDao = new CuentaDaoImpl();
	    try {
	        Cuenta cuenta = cuentaDao.obtenerCuentaPorCBU(cbu);
	        if (cuenta == null || !cuenta.isEstado()) {
	            throw new CuentaExistenteExcenption("No se encontró la cuenta con CBU: " + cbu);
	        }
	        return cuenta.getNroCuenta();
	    } catch (Exception e) {
	        throw new CuentaExistenteExcenption("Error al obtener datos de la cuenta destino.");
	    }
	}


	@Override
	public boolean registrarTransferencia(Transferencia transferencia)throws MovimientoException, TransferenciaException, Exception {
	    boolean resultado = false;
	    Connection conn = null;

	    try {
	        conn = Conexion.getConexion().getSQLConexion();
	        MovimientosDao movimientoDao = new MovimientoDaoImpl();
	        TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();

	        //  VALIDACIONES
	        if (transferencia.getImporte() <= 0)
	            throw new TransferenciaException("El importe debe ser mayor a cero.");

	        if (transferencia.getNroCuentaOrigen() == transferencia.getNroCuentaDestino())
	            throw new TransferenciaException("La cuenta origen y destino no pueden ser la misma.");

	        BigDecimal saldoOrigen = movimientoDao.obtenerSaldoPorCuenta(transferencia.getNroCuentaOrigen());
	        if (saldoOrigen.compareTo(BigDecimal.valueOf(transferencia.getImporte())) < 0)
	            throw new TransferenciaException("Saldo insuficiente en la cuenta origen.");

	        //  Movimiento salida
	        Movimiento movSalida = new Movimiento();
	        movSalida.setNroCuenta(transferencia.getNroCuentaOrigen());
	        movSalida.setIdTipoMovimiento(4); // Transferencia
	        movSalida.setFecha(LocalDateTime.now());
	        movSalida.setDetalle("Transferencia salida");
	        movSalida.setImporte(BigDecimal.valueOf(transferencia.getImporte() * -1));

	        int idMovSalida = movimientoDao.insertarMovimiento(movSalida, conn);
	        if (idMovSalida == -1) throw new MovimientoException("Error al registrar movimiento de salida");

	        //  Movimiento entrada
	        Movimiento movEntrada = new Movimiento();
	        movEntrada.setNroCuenta(transferencia.getNroCuentaDestino());
	        movEntrada.setIdTipoMovimiento(4); // Transferencia
	        movEntrada.setFecha(LocalDateTime.now());
	        movEntrada.setDetalle("Transferencia entrada");
	        movEntrada.setImporte(BigDecimal.valueOf(transferencia.getImporte()));

	        int idMovEntrada = movimientoDao.insertarMovimiento(movEntrada, conn);
	        if (idMovEntrada == -1) throw new MovimientoException("Error al registrar movimiento de entrada");

	        //  Datos de transferencia
	        transferencia.setIdMovimientoSalida(idMovSalida);
	        transferencia.setIdMovimientoEntrada(idMovEntrada);
	        transferencia.setFecha(LocalDate.now());

	        resultado = transferenciaDao.insertarTransferencia(conn, transferencia);
	        if (!resultado) throw new TransferenciaException("Error al registrar transferencia");

	        conn.commit();
	    }catch(MovimientoException e) {
	    	throw e;
	    }catch(TransferenciaException e){
	    	throw e;
	    } catch (Exception e) {
	        e.printStackTrace();
	        try {
	            if (conn != null) conn.rollback();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	        throw e;
	    } finally {
	        try {
	            if (conn != null) conn.close();
	        } catch (Exception ex) {
	            ex.printStackTrace();
	        }
	    }

	    return resultado;
	}
	
	
}