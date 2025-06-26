package negocioImpl;


import java.util.List;
import dao.CuentaDao;
import daoImpl.CuentaDaoImpl;
import dominio.Cuenta;
import negocio.CuentaNegocio;

public class CuentaNegocioImpl implements CuentaNegocio {

    private CuentaDao cuentaDao = new CuentaDaoImpl();

    @Override
    public List<Cuenta> obtenerCuentasPorCliente(int idCliente) throws Exception {
        return cuentaDao.listarPorCliente(idCliente);
    }
    
    
}
