package negocio;

import dominio.Transferencia;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;

public interface TransferenciaNegocio {
    boolean registrarTransferencia(Transferencia transferencia) throws TransferenciaException, MovimientoException, Exception;
}