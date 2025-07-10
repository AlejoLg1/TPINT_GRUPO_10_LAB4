package negocio;

import java.util.Date;

public interface ReporteNegocio {
	Object generarReporte(String tipo, Date desde, Date hasta) throws Exception;
}