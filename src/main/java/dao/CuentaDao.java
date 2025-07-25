package dao;

import java.util.List;


import dominio.Cuenta;
import java.math.BigDecimal;

public interface CuentaDao {
	 List<Cuenta> listarPorCliente(int idCliente);
	 List<Object[]> listarConDatos();
	 boolean agregar(Cuenta cuenta, int idCliente, int idTipoCuenta);
	 int contarCuentasActivasPorCliente(int idCliente);
	 boolean cambiarEstado(int idCuenta, boolean nuevoEstado);
	 int obtenerIdClientePorCuenta(int nroCuenta);
	 Cuenta obtenerCuentaPorId(int idCuenta);
	 Cuenta obtenerCuentaPorCBU(String cbu);
	 public boolean actualizar(Cuenta cuenta);
	 List<Object[]> filtrarCuentas(String busqueda, String dniCliente, String tipoCuenta, BigDecimal saldoMin, BigDecimal saldoMax);
	 boolean actualizarSaldo(int nroCuenta, BigDecimal monto, boolean esSuma, java.sql.Connection conn);
	 

}
