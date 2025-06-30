package daoImpl;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import dao.PrestamoDao;
import dominio.Cliente;
import dominio.Prestamo;
import dominio.Cuenta;
import dominio.Cuota;
import dominio.Movimiento;
import utils.Conexion;
import dominio.TipoCuenta;

public class PrestamoDaoImpl implements PrestamoDao {

    @Override
    public boolean registrarPrestamo(Prestamo prestamo) {
        boolean estado = false;
        Connection con = Conexion.getConexion().getSQLConexion();
        PreparedStatement stmt = null;

        try {
            String sql = "INSERT INTO Prestamo (id_cliente, nro_cuenta, importe_solicitado, cantidad_cuotas, monto_cuota, estado) VALUES (?, ?, ?, ?, ?,?)";
            stmt = con.prepareStatement(sql);
            stmt.setInt(1, prestamo.get_cliente().getIdCliente());
            stmt.setInt(2, prestamo.get_cuenta().getNroCuenta());
            stmt.setDouble(3, prestamo.getImporte_solicitado());
            stmt.setInt(4, prestamo.getCantidad_cuotas());
            stmt.setDouble(5, prestamo.getMonto_cuota());
            stmt.setString(6, "Pendiente");

            int rows = stmt.executeUpdate();
            estado = rows > 0;
            
            if (estado) {
                con.commit();
            } else {
                con.rollback();
            }

        } catch (Exception e) {
            try { con.rollback(); } catch (Exception ex) { ex.printStackTrace(); }
            e.printStackTrace();
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception e) {}
            try { if (con != null) con.close(); } catch (Exception e) {}
        }

        return estado;
    }
    
    @Override
    public List<Prestamo> ListarConFiltros(String busqueda, Double montoMin, Double montoMax, String estado, String fechaSolicitud) {
        List<Prestamo> prestamos = new ArrayList<>();
        Connection conexion = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;

        try {
            conexion = Conexion.getConexion().getSQLConexion();

            StringBuilder query = new StringBuilder("SELECT id_prestamo, id_cliente, nro_cuenta, fecha, importe_solicitado, cantidad_cuotas, monto_cuota, estado, cuotas_pagadas FROM prestamo WHERE (autorizacion = 1 OR estado = 'Pendiente')");

            List<Object> params = new ArrayList<>();

            if (busqueda != null && !busqueda.isEmpty()) {
                query.append(" AND nro_cuenta LIKE ?");
                params.add("%" + busqueda + "%");
            }

            if (montoMin != null) {
                query.append(" AND importe_solicitado >= ?");
                params.add(montoMin);
            }

            if (montoMax != null) {
                query.append(" AND importe_solicitado <= ?");
                params.add(montoMax);
            }

            if (estado != null && !estado.isEmpty()) {
                query.append(" AND estado = ?");
                params.add(estado);
            }

            if (fechaSolicitud != null && !fechaSolicitud.isEmpty()) {
                query.append(" AND DATE(fecha) = ?");
                params.add(fechaSolicitud);
            }

            stmt = conexion.prepareStatement(query.toString());

            // Setear parámetros
            for (int i = 0; i < params.size(); i++) {
                stmt.setObject(i + 1, params.get(i));
            }

            rs = stmt.executeQuery();

            while (rs.next()) {
                Prestamo prestamo = new Prestamo();

                int nro_cta = rs.getInt("nro_cuenta");
                CuentaDaoImpl cuentaDao = new CuentaDaoImpl();
                Cuenta cuenta = cuentaDao.obtenerCuentaPorId(nro_cta);

                int id_cliente = rs.getInt("id_cliente");
                ClienteDaoImpl clienteDao = new ClienteDaoImpl();
                Cliente cliente = clienteDao.obtenerPorIdCliente(id_cliente);

                prestamo.setId_prestamo(rs.getInt("id_prestamo"));
                prestamo.set_cliente(cliente);
                prestamo.set_cuenta(cuenta);
                prestamo.setFecha(rs.getTimestamp("fecha"));
                prestamo.setImporte_solicitado(rs.getDouble("importe_solicitado"));
                prestamo.setCantidad_cuotas(rs.getInt("cantidad_cuotas"));
                prestamo.setMonto_cuota(rs.getDouble("monto_cuota"));
                prestamo.setAutorizacion(true);
                prestamo.setEstado(rs.getString("estado"));
                prestamo.setCuotas_pagadas(rs.getInt("cuotas_pagadas"));

                prestamos.add(prestamo);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (conexion != null) conexion.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return prestamos;
    }



	@Override
	public boolean aprobarPrestamo(int idPrestamo) {
	    boolean actualizado = false;
	    Connection conexion = null;
	    PreparedStatement stmt = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        conexion.setAutoCommit(false); // Habilitar control de transacciones manual

	        // 1. Actualizar estado del préstamo
	        String query = "UPDATE prestamo SET autorizacion = 1, estado = 'Aprobado' WHERE id_prestamo = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idPrestamo);
	        int filas = stmt.executeUpdate();

	        if (filas > 0) {
	            // 2. Obtener datos del préstamo aprobado
	            Prestamo prestamo = obtenerPrestamoPorId(idPrestamo);
	            
	            // 3. Registrar movimiento de alta de préstamo
	            MovimientoDaoImpl movimientoDao = new MovimientoDaoImpl();

	            Movimiento mov = new Movimiento();
	            mov.setNroCuenta(prestamo.get_cuenta().getNroCuenta());
	            mov.setDetalle("Alta de préstamo N°" + prestamo.getId_prestamo());
	            mov.setImporte(BigDecimal.valueOf(prestamo.getImporte_solicitado()));
	            mov.setFecha(LocalDateTime.now());
	            mov.setIdTipoMovimiento(2); // Alta de Préstamo

	            int idMov = -1;
	            try {
	                idMov = movimientoDao.insertarMovimiento(mov, conexion);
	            } catch (Exception e) {
	                e.printStackTrace();
	                conexion.rollback();
	                return false;
	            }

	            if (idMov <= 0) {
	                conexion.rollback();
	                return false;
	            }
	            
	            // 4. Crear cuotas pedidas + intereses
	            CuotaDaoImpl cuotaDao = new CuotaDaoImpl();
	            LocalDate fechaHoy = LocalDate.now();

	            double porcentajeInteres = 0;

	            switch(prestamo.getCantidad_cuotas()) {
	                case 12:
	                    porcentajeInteres = 0.25;
	                    break;
	                case 24:
	                    porcentajeInteres = 0.40;
	                    break;
	                case 36:
	                    porcentajeInteres = 0.60;
	                    break;
	                case 48:
	                    porcentajeInteres = 0.80;
	                    break;
	                case 60:
	                    porcentajeInteres = 1.00;
	                    break;
	                case 72:
	                    porcentajeInteres = 1.20;
	                    break;
	            }
	            
	            double montoConInteres = prestamo.getImporte_solicitado() * (1 + porcentajeInteres);
	            double montoCuota = montoConInteres / prestamo.getCantidad_cuotas();
	            
	            for (int i = 1; i <= prestamo.getCantidad_cuotas(); i++) {
	                Cuota cuota = new Cuota();
	                cuota.setIdPrestamo(idPrestamo);
	                cuota.setNroCuenta(prestamo.get_cuenta().getNroCuenta());
	                cuota.setNumeroCuota(i);
	                cuota.setMonto(BigDecimal.valueOf(montoCuota));
	                cuota.setFechaVencimiento(fechaHoy.plusMonths(i));
	                cuota.setEstado("Pendiente");

	                boolean creada = cuotaDao.crearCuota(cuota, conexion);
	                if (!creada) {
	                    conexion.rollback();
	                    return false;
	                }
	            }


	            // 5. Commit general si todo sale bien
	            conexion.commit();
	            actualizado = true;
	        } else {
	            conexion.rollback();
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	        try {
	            if (conexion != null) conexion.rollback();
	        } catch (SQLException ex) {
	            ex.printStackTrace();
	        }
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return actualizado;
	}



	@Override
	public boolean rechazarPrestamo(int idPrestamo) {
		boolean actualizado = false;
	    Connection conexion = null;
	    PreparedStatement stmt = null;

	    try {
	        conexion = Conexion.getConexion().getSQLConexion();
	        String query = "UPDATE prestamo SET estado = 'Rechazado' WHERE id_prestamo = ?";
	        stmt = conexion.prepareStatement(query);
	        stmt.setInt(1, idPrestamo);
	        int filas = stmt.executeUpdate();
	        if (filas > 0) {
	            conexion.commit();  
	            actualizado = true;
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    } finally {
	        try {
	            if (stmt != null) stmt.close();
	            if (conexion != null) conexion.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    return actualizado;
	}

	@Override
	public List<Prestamo> obtenerPrestamosFiltrados(String busqueda, Double montoMin, Double montoMax, String estado, String fechaSolicitud) {
		
		List<Prestamo> listaPrestamos = new ArrayList<>();
        Connection cn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        // Construcción dinámica de la consulta SQL
        StringBuilder sql = new StringBuilder();
        sql.append("SELECT ");
        sql.append("p.id_prestamo, p.id_cliente, p.nro_cuenta, p.fecha, p.importe_solicitado, p.cantidad_cuotas, p.monto_cuota, p.autorizacion, p.estado, ");        
        sql.append("cl.id_cliente, cl.nombre AS cliente_nombre, cl.apellido AS cliente_apellido, cl.dni AS cliente_dni, "); 
        sql.append("cu.nro_cuenta, cu.cbu AS cuenta_cbu, cu.saldo AS cuenta_saldo, "); 
        sql.append("tc.id_tipo_cuenta, tc.descripcion AS tipo_cuenta_descripcion "); 
        sql.append("FROM Prestamo p ");
        sql.append("JOIN Cliente cl ON p.id_cliente = cl.id_cliente "); 
        sql.append("JOIN Cuenta cu ON p.nro_cuenta = cu.nro_cuenta "); 
        sql.append("JOIN Tipo_cuenta tc ON cu.id_tipo_cuenta = tc.id_tipo_cuenta ");
        sql.append("WHERE 1=1 "); // Condición base que siempre es verdadera para facilitar la adición de AND

        // Lista para almacenar los parámetros de PreparedStatement dinámicamente
        List<Object> parametros = new ArrayList<>();

        // Aplica filtro por búsqueda (nombre de cliente, apellido de cliente o CBU de cuenta)
        if (busqueda != null && !busqueda.trim().isEmpty()) {
            sql.append("AND (cl.nombre LIKE ? OR cl.apellido LIKE ? OR cu.cbu LIKE ?) ");
            parametros.add("%" + busqueda + "%");
            parametros.add("%" + busqueda + "%");
            parametros.add("%" + busqueda + "%");
        }
        // Aplica filtro por monto mínimo
        if (montoMin != null) {
            sql.append("AND p.importe_solicitado >= ? ");
            parametros.add(montoMin);
        }
        // Aplica filtro por monto máximo
        if (montoMax != null) {
            sql.append("AND p.importe_solicitado <= ? ");
            parametros.add(montoMax);
        }
        // Aplica filtro por estado del préstamo
        // El valor "-- Todos --" o vacío indica que no se aplica este filtro
        if (estado != null && !estado.trim().isEmpty() && !estado.equalsIgnoreCase("-- Todos --")) {
            sql.append("AND p.estado = ? ");
            parametros.add(estado);
        }
        // Aplica filtro por fecha de solicitud
        if (fechaSolicitud != null && !fechaSolicitud.trim().isEmpty()) {
            sql.append("AND DATE(p.fecha) = ? ");
            parametros.add(fechaSolicitud);
        }

        try {
        	cn = Conexion.getConexion().getSQLConexion();; // Obtiene tu conexión a la base de datos
            ps = cn.prepareStatement(sql.toString());

            // Asignar los parámetros al PreparedStatement en el orden correcto
            for (int i = 0; i < parametros.size(); i++) {             
                ps.setObject(i + 1, parametros.get(i)); 
            }

            rs = ps.executeQuery();

            while (rs.next()) {
                Prestamo p = new Prestamo();
                p.setId_prestamo(rs.getInt("id_prestamo"));
                p.setFecha(rs.getTimestamp("fecha"));
                p.setImporte_solicitado(rs.getDouble("importe_solicitado"));
                p.setCantidad_cuotas(rs.getInt("cantidad_cuotas"));
                p.setMonto_cuota(rs.getDouble("monto_cuota"));
                p.setAutorizacion(rs.getBoolean("autorizacion"));
                p.setEstado(rs.getString("estado"));

                // Mapear el objeto Cliente anidado
                Cliente cliente = new Cliente();
                cliente.setIdUsuario(rs.getInt("id_cliente")); 
                cliente.setNombre(rs.getString("cliente_nombre"));
                cliente.setApellido(rs.getString("cliente_apellido"));
                p.set_cliente(cliente); 

                // Mapear el objeto Cuenta anidado
                Cuenta cuenta = new Cuenta();
                cuenta.setNroCuenta(rs.getInt("nro_cuenta"));
                cuenta.setCbu(rs.getString("cuenta_cbu"));
                cuenta.setSaldo(rs.getBigDecimal("cuenta_saldo"));
                
                // Mapear el objeto Tipo_cuenta anidado dentro de Cuenta
                TipoCuenta tipoCuenta = new TipoCuenta();
                tipoCuenta.setIdTipoCuenta(rs.getInt("id_tipo_cuenta"));
                tipoCuenta.setDescripcion(rs.getString("tipo_cuenta_descripcion"));
                cuenta.setTipoCuenta(tipoCuenta.getDescripcion());

                p.set_cuenta(cuenta); 

                listaPrestamos.add(p);
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
        } finally {
            try {
                if (rs != null) rs.close();
                if (ps != null) ps.close();
                if (cn != null) cn.close();
            } catch (SQLException e) {
                e.printStackTrace(); 
            }
        }
        return listaPrestamos;
	}
	
	@Override
	public Prestamo obtenerPrestamoPorId(int idPrestamo) {
	    Prestamo prestamo = null;
	    String sql = "SELECT p.id_prestamo, p.importe_solicitado, p.cantidad_cuotas, p.monto_cuota, p.estado, " +
	                 "c.nro_cuenta, c.id_cliente " +
	                 "FROM prestamo p " +
	                 "JOIN cuenta c ON p.nro_cuenta = c.nro_cuenta " +
	                 "WHERE p.id_prestamo = ?";

	    try (Connection conn = Conexion.getConexion().getSQLConexion();
	         PreparedStatement pst = conn.prepareStatement(sql)) {

	        pst.setInt(1, idPrestamo);

	        ResultSet rs = pst.executeQuery();
	        if (rs.next()) {
	            prestamo = new Prestamo();
	            prestamo.setId_prestamo(rs.getInt("id_prestamo"));
	            prestamo.setImporte_solicitado(rs.getDouble("importe_solicitado"));
	            prestamo.setCantidad_cuotas(rs.getInt("cantidad_cuotas"));
	            prestamo.setMonto_cuota(rs.getDouble("monto_cuota"));
	            prestamo.setEstado(rs.getString("estado"));

	            // Obtener cuenta
	            Cuenta cuenta = new Cuenta();
	            cuenta.setNroCuenta(rs.getInt("nro_cuenta"));
	            prestamo.set_cuenta(cuenta);

	            // Si necesitas el cliente completo, usa ClienteDaoImpl aquí.
	            ClienteDaoImpl clienteDao = new ClienteDaoImpl();
	            Cliente cliente = clienteDao.obtenerPorIdCliente(rs.getInt("id_cliente"));
	            prestamo.set_cliente(cliente);
	        }

	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return prestamo;
	}

	public boolean actualizarCuotasPagadas(int idPrestamo, Connection conn) throws Exception {
	    String query = "UPDATE Prestamo SET cuotas_pagadas = cuotas_pagadas + 1 WHERE id_prestamo = ?";
	    try (PreparedStatement stmt = conn.prepareStatement(query)) {
	        stmt.setInt(1, idPrestamo);
	        int filas = stmt.executeUpdate();
	        return filas > 0;
	    } catch (Exception e) {
	        e.printStackTrace();
	        throw new Exception("Error al actualizar cuotas pagadas del préstamo ID: " + idPrestamo, e);
	    }
	}


}