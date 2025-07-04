package negocioImpl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dominio.Prestamo;
import negocio.PrestamoNegocio;
import daoImpl.PrestamoDaoImpl;
import dao.PrestamoDao;

public class PrestamoNegocioImpl implements PrestamoNegocio {

	PrestamoDao dao = new PrestamoDaoImpl();

	@Override
	public List<Prestamo> ListarPrestamos(String busqueda, String montoMinStr, String montoMaxStr, String estado, String fechaSolicitud) {
		
		Double montoMin = null;
		
		try {            
            if (montoMinStr != null && !montoMinStr.isEmpty()) {
            	montoMin = Double.parseDouble(montoMinStr);
            }
        } catch (NumberFormatException e) {
           throw e;
        }
		
		Double montoMax = null;
        try {            
            if (montoMaxStr != null && !montoMaxStr.isEmpty()) {
                montoMax = Double.parseDouble(montoMaxStr);
            }
        } catch (NumberFormatException e) {
        	throw e;
        }
        
        List<Prestamo> listaPrestamos = dao.ListarConFiltros(busqueda, montoMin, montoMax, estado, fechaSolicitud);

		return listaPrestamos;
	}

	@Override
	public List<Prestamo> ListarPrestamosFiltrados(String busqueda, String montoMinStr, String montoMaxStr,	String estado, String fechaSolicitud) {
		
		Double montoMin = null;
		
		try {            
            if (montoMinStr != null && !montoMinStr.isEmpty()) {
            	montoMin = Double.parseDouble(montoMinStr);
            }
        } catch (NumberFormatException e) {
           throw e;
        }
		
		Double montoMax = null;
        try {            
            if (montoMaxStr != null && !montoMaxStr.isEmpty()) {
                montoMax = Double.parseDouble(montoMaxStr);
            }
        } catch (NumberFormatException e) {
        	throw e;
        }
		
		List<Prestamo> listaPrestamos = dao.obtenerPrestamosFiltrados(busqueda, montoMin, montoMax, estado, fechaSolicitud);
		return listaPrestamos;
	}

	@Override
	public Prestamo ObtenerPrestamo(String idPrestamoStr) {
		
		Prestamo prestamo = null;
		
		if (idPrestamoStr != null && !idPrestamoStr.isEmpty()) {
			int idPrestamo = Integer.parseInt(idPrestamoStr);
			prestamo = dao.obtenerPrestamoPorId(idPrestamo);
		}
		
		return prestamo;
	}

	@Override
	public Map<String, Object> RealizarAccion(int idPrestamo, String accion) {
		
		Map<String, Object> resultado = new HashMap<>();
		boolean exito = false;
		String mensajeResultado = "";
		
		if ("aprobar".equals(accion)) {
			exito = dao.aprobarPrestamo(idPrestamo);
            if (exito) {
                mensajeResultado = "Préstamo " + idPrestamo + " aprobado exitosamente.";
            } else {
                mensajeResultado = "Error al aprobar el préstamo " + idPrestamo + ".";
            }
        } else if ("rechazar".equals(accion)) {
        	exito = dao.rechazarPrestamo(idPrestamo);
            if (exito) {
                mensajeResultado = "Préstamo " + idPrestamo + " rechazado exitosamente.";
            } else {
                mensajeResultado = "Error al rechazar el préstamo " + idPrestamo + ".";
            }
        } else {
        	exito = false;
            mensajeResultado = "Acción '" + accion + "' no reconocida para el préstamo " + idPrestamo + ".";
        }

        resultado.put("exito", exito);
        resultado.put("mensaje", mensajeResultado);

        return resultado;
	}
	
	

}
