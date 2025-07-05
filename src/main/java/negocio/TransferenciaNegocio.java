package negocio;

import dominio.Transferencia;
import excepciones.MovimientoException;
import excepciones.TransferenciaException;

public interface TransferenciaNegocio {
    boolean registrarTransferencia(Transferencia transferencia) throws TransferenciaException, MovimientoException, Exception;

    // NUEVO método para que el servlet no tenga lógica de validación ni DAO
    boolean transferirDesdeFormulario(String nroCuentaOrigen, String cbuDestino, String monto) throws Exception;
}