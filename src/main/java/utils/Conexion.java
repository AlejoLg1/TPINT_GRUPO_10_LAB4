package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Conexion {
    private static final String URL = "jdbc:mysql://localhost:3306/banco";
    private static final String USER = "root";
    private static final String PASSWORD = "11102003Tebi";
    private static Conexion instancia;
    
    private Conexion() {
        try {
        	// Para MySQL Connector 8.0+
			Class.forName("com.mysql.cj.jdbc.Driver");

			// Para MySQL Connector 5.1.x
			// Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static Conexion getConexion() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }

    public Connection getSQLConexion() {
        try {
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            conn.setAutoCommit(false);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
