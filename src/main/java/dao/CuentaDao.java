package dao;

import java.util.List;
import dominio.Cuenta;

public interface CuentaDao {
	 List<Cuenta> listarPorCliente(int idCliente);
	 List<Object[]> listarConDatos();
	 boolean agregar(Cuenta cuenta, int idCliente, int idTipoCuenta);
	 int contarCuentasActivasPorCliente(int idCliente);
	 boolean cambiarEstado(int idCuenta, boolean nuevoEstado);
	 int obtenerIdClientePorCuenta(int nroCuenta);

}
