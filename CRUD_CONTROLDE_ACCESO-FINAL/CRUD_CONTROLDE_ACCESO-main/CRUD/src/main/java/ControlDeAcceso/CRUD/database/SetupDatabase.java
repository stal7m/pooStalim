package ControlDeAcceso.CRUD.database;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

public class SetupDatabase {

    public static void initialize() {
        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement()) {

            // 🔹 Crear tabla de usuarios
            String sqlUsuarios = """
                CREATE TABLE IF NOT EXISTS usuarios (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    dni TEXT UNIQUE NOT NULL,
                    nombre TEXT NOT NULL,
                    password TEXT NOT NULL,
                    profesion TEXT,
                    tipo TEXT
                );
            """;
            stmt.execute(sqlUsuarios);

            // 🔹 Insertar usuario inicial (administrador)
            String sqlInsertUsuario = """
                INSERT OR IGNORE INTO usuarios (dni, nombre, password, profesion, tipo)
                VALUES ('12345678', 'Administrador', 'admin', 'Ingeniero de Sistemas', 'Docente');
            """;
            stmt.execute(sqlInsertUsuario);

            // 🔹 Crear tabla de asistencias (ajustado a camelCase)
            String sqlAsistencia = """
                CREATE TABLE IF NOT EXISTS asistencia (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    nombre TEXT,
                    dni TEXT,
                    fecha TEXT,
                    hora_ingreso TEXT,
                    hora_salida TEXT,
                    profesion TEXT,
                    tipo TEXT
                );
            """;
            stmt.execute(sqlAsistencia);

            System.out.println(" Tablas creadas y usuario inicial insertado correctamente.");

        } catch (SQLException e) {
            System.out.println(" Error al inicializar la base de datos: " + e.getMessage());
        }
    }
}
