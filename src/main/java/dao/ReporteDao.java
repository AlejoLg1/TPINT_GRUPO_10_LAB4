package dao;
 
 
import java.util.Date;
import java.util.List;
import dominio.Reporte;
 
public interface ReporteDao {
    List<Reporte> generarReporte(String tipoReporte, Date inicio, Date fin) throws Exception;
}