package dao;

import java.util.List;
import dominio.Cuenta;

public interface CuentaDao {
	 List<Cuenta> listarPorCliente(int idCliente);
}
