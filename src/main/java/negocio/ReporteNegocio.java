package negocio;

import java.util.Date;
import java.util.List;
import dominio.Reporte;

public interface ReporteNegocio {
    List<Reporte> generarReporte(String tipo, Date desde, Date hasta) throws Exception;
}