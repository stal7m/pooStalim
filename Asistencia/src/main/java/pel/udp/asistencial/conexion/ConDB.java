package pel.udp.asistencial.conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConDB {
    private static Connection conexion = null;

    public static Connection getConexion() {
        if (conexion == null) {
            try {
                String url = "jdbc:sqlite:D:/ISTAI/miAsistencial/asistenciadb.db?foreign_keys=on";
                conexion = DriverManager.getConnection(url);
                System.out.println("¡Conexión exitosa!");
            } catch (SQLException e) {
                throw new RuntimeException("Error al conectar: " + e.getMessage(), e);
            }
        }
        return conexion;
    }

    public static void closeConexion() {
        try {
            if (conexion != null && !conexion.isClosed()) {
                conexion.close();
                System.out.println("Conexión cerrada.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error al cerrar conexión: " + e.getMessage(), e);
        }
    }
}