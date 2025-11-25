package ControlDeAcceso.CRUD.database;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static final String URL = "jdbc:sqlite:database/control_acceso.db";
    private static Connection connection = null;

    public static Connection getConnection() {
        try {
            //  Asegurarse de que la carpeta "database" exista
            File dbFolder = new File("database");
            if (!dbFolder.exists()) {
                dbFolder.mkdirs();
                System.out.println("📁 Carpeta 'database' creada automáticamente.");
            }

            //  Conectar a SQLite
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL);
                System.out.println("Conexión a SQLite establecida correctamente.");
            }
        } catch (SQLException e) {
            System.out.println(" Error al conectar con la base de datos: " + e.getMessage());
        }
        return connection;
    }

    public static void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
                System.out.println(" Conexión SQLite cerrada correctamente.");
            }
        } catch (SQLException e) {
            System.out.println(" Error al cerrar la conexión: " + e.getMessage());
        }
    }
}
