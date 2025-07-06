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
}

