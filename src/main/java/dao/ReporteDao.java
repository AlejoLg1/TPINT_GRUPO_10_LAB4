package dao;
 
 
import java.util.Date;
 
public interface ReporteDao {
    Object generarReporte(String tipoReporte, Date inicio, Date fin) throws Exception;
}
