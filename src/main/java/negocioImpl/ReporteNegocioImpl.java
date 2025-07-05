package negocioImpl;

import java.util.Date;
import java.util.List;
import dao.ReporteDao;
import daoImpl.ReporteDaoImpl;
import dominio.Reporte;
import negocio.ReporteNegocio;

public class ReporteNegocioImpl implements ReporteNegocio {

    private ReporteDao reporteDao = new ReporteDaoImpl();

    @Override
    public List<Reporte> generarReporte(String tipo, Date desde, Date hasta) throws Exception {
        if (tipo == null || tipo.trim().isEmpty()) {
            throw new IllegalArgumentException("Debe seleccionar un tipo de reporte.");
        }

        return reporteDao.generarReporte(tipo, desde, hasta);
    }
}