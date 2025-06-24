package dao;

import java.sql.Connection;
import java.util.List;
import dominio.Transferencia;

public interface TransferenciaDao {
    
	
    List<Transferencia> obtenerTransferenciasPorCuenta(int nroCuenta) throws Exception;

    public boolean insertarTransferencia(Connection conn, Transferencia t) throws Exception; 
}