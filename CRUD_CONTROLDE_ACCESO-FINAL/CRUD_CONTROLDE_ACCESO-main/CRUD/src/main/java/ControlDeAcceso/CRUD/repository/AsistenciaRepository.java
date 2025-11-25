package ControlDeAcceso.CRUD.repository;

import ControlDeAcceso.CRUD.database.DatabaseConnection;
import ControlDeAcceso.CRUD.model.Asistencia;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class AsistenciaRepository {

    // 🔹 Obtener todas las asistencias
    public List<Asistencia> obtenerTodos() {
        List<Asistencia> lista = new ArrayList<>();
        String sql = "SELECT * FROM asistencia ORDER BY id DESC";

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Asistencia a = new Asistencia(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("fecha"),
                        rs.getString("hora_ingreso"),
                        rs.getString("hora_salida"),
                        rs.getString("profesion"),
                        rs.getString("tipo")
                );
                lista.add(a);
            }

        } catch (SQLException e) {
            System.out.println(" Error al obtener asistencias: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Buscar última asistencia por DNI
    public Asistencia buscarPorDni(String dni) {
        String sql = "SELECT * FROM asistencia WHERE dni = ? ORDER BY id DESC LIMIT 1";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                return new Asistencia(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("dni"),
                        rs.getString("fecha"),
                        rs.getString("hora_ingreso"),
                        rs.getString("hora_salida"),
                        rs.getString("profesion"),
                        rs.getString("tipo")
                );
            }

        } catch (SQLException e) {
            System.out.println(" Error al buscar por DNI: " + e.getMessage());
        }
        return null;
    }

    // 🔹 Insertar nueva asistencia
    public void insertar(Asistencia a) {
        String sql = """
                INSERT INTO asistencia (nombre, dni, fecha, hora_ingreso, hora_salida, profesion, tipo)
                VALUES (?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getNombre());
            stmt.setString(2, a.getDni());
            stmt.setString(3, a.getFecha());
            stmt.setString(4, a.getHoraIngreso());
            stmt.setString(5, a.getHoraSalida());
            stmt.setString(6, a.getProfesion());
            stmt.setString(7, a.getTipo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar asistencia: " + e.getMessage());
        }
    }

    // 🔹 Actualizar hora de salida SOLO del registro del día actual
    public void actualizarHoraSalida(String dni, String horaSalida) {
        String sql = "UPDATE asistencia SET hora_salida = ? WHERE dni = ? AND fecha = ? AND (hora_salida IS NULL OR hora_salida = '')";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, horaSalida);
            stmt.setString(2, dni);
            stmt.setString(3, java.time.LocalDate.now().toString());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(" Error al actualizar hora de salida: " + e.getMessage());
        }
    }


    // 🔹 Eliminar asistencia por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM asistencia WHERE id = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(" Error al eliminar asistencia: " + e.getMessage());
        }
    }

    public void actualizar(Asistencia a) {
        String sql = "UPDATE asistencia SET nombre=?, dni=?, fecha=?, hora_ingreso=?, hora_salida=?, profesion=?, tipo=? WHERE id=?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, a.getNombre());
            stmt.setString(2, a.getDni());
            stmt.setString(3, a.getFecha());
            stmt.setString(4, a.getHoraIngreso());
            stmt.setString(5, a.getHoraSalida());
            stmt.setString(6, a.getProfesion());
            stmt.setString(7, a.getTipo());
            stmt.setInt(8, a.getId());
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println(" Error al actualizar asistencia: " + e.getMessage());
        }
    }

}
