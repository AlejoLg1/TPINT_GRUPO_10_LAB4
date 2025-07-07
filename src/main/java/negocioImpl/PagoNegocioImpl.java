package negocioImpl;

import java.util.ArrayList;

import java.util.List;
import daoImpl.CuentaDaoImpl;
import daoImpl.CuotaDaoImpl;
import dominio.Cuenta;
import dominio.Cuota;
import negocio.PagoNegocio;



public class PagoNegocioImpl implements PagoNegocio {

    private CuotaDaoImpl cuotaDao = new CuotaDaoImpl();
    private CuentaDaoImpl cuentaDao = new CuentaDaoImpl();

    @Override
    public List<Cuota> obtenerCuotasPorIds(String[] ids) throws Exception {
        List<Cuota> cuotas = new ArrayList<>();
        for (String idStr : ids) {
            try {
                int id = Integer.parseInt(idStr);
                Cuota cuota = cuotaDao.obtenerCuotaPorId(id);
                if (cuota != null) {
                    cuotas.add(cuota);
                }
            } catch (NumberFormatException e) {
                // ignorar valores no válidos
            }
        }
        return cuotas;
    }

    @Override
    public List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception {
        return cuentaDao.listarPorCliente(idCliente);
    }
    
    
    @Override
    public List<Cuota> obtenerCuotasPendientesConFiltros(int idCliente, Integer idPrestamo, String fechaVencimiento, String estado) throws Exception {
        return cuotaDao.obtenerCuotasPendientesConFiltros(idCliente, idPrestamo, fechaVencimiento, estado);
    }

    @Override
    public boolean procesarPagoCuotaIndividual(int idCuota, int nroCuenta) throws Exception {
        Cuota cuota = cuotaDao.obtenerCuotaPorId(idCuota);
        if (cuota == null || cuota.getMonto() == null) {
            throw new Exception("La cuota no existe o está incompleta.");
        }
        
        if ("PAGADO".equalsIgnoreCase(cuota.getEstado())) {
            throw new Exception("La cuota ya está pagada.");
        }
        
        List<Cuota> cuotas = new ArrayList<>();
        cuotas.add(cuota);

        return cuotaDao.procesarPagoCuotas(cuotas, nroCuenta);
    }

    @Override
    public Cuota obtenerCuotaPorId(int idCuota) throws Exception {
        return cuotaDao.obtenerCuotaPorId(idCuota);
    }


}

