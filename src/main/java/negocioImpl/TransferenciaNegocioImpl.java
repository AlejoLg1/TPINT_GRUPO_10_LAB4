package negocioImpl;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.math.BigDecimal;

import dao.MovimientosDao;
import dao.TransferenciaDao;
import daoImpl.MovimientoDaoImpl;
import daoImpl.TransferenciaDaoImpl;
import dominio.Movimiento;
import dominio.Transferencia;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;
import negocio.TransferenciaNegocio;
import utils.Conexion;

public class TransferenciaNegocioImpl implements TransferenciaNegocio {

	@Override
	public boolean registrarTransferencia(Transferencia transferencia)throws MovimientoException, TransferenciaException, Exception {
	    boolean resultado = false;
	    Connection conn = null;

	    try {
	        conn = Conexion.getConexion().getSQLConexion();
	        MovimientosDao movimientoDao = new MovimientoDaoImpl();
	        TransferenciaDao transferenciaDao = new TransferenciaDaoImpl();

	        //  VALIDACIONES BÃSICAS
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

	        int idMovSalida = movimientoDao.insertarMovimientoDesdeNegocio(conn, movSalida);
	        if (idMovSalida == -1) throw new MovimientoException("Error al registrar movimiento de salida");

	        //  Movimiento entrada
	        Movimiento movEntrada = new Movimiento();
	        movEntrada.setNroCuenta(transferencia.getNroCuentaDestino());
	        movEntrada.setIdTipoMovimiento(4); // Transferencia
	        movEntrada.setFecha(LocalDateTime.now());
	        movEntrada.setDetalle("Transferencia entrada");
	        movEntrada.setImporte(BigDecimal.valueOf(transferencia.getImporte()));

	        int idMovEntrada = movimientoDao.insertarMovimientoDesdeNegocio(conn, movEntrada);
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