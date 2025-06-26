package dao;


import java.sql.Connection;
import java.util.List;
import dominio.Cuota;


public interface CuotaDao {

    List<Cuota> obtenerCuotasPendientes(int idCliente) throws Exception;
    
    boolean pagarCuotas(List<Cuota> cuotas, int nroCuenta, Connection conn) throws Exception;
    
    Cuota obtenerCuotaPorId(int idCuota);
    
   
    
    

}