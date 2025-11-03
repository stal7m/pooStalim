package ControlDeAcceso.CRUD.repository;

import ControlDeAcceso.CRUD.model.Usuario;
import ControlDeAcceso.CRUD.database.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UsuarioRepository {

    // 🔹 Obtener todos los usuarios
    public List<Usuario> obtenerTodos() {
        List<Usuario> lista = new ArrayList<>();
        String sql = "SELECT * FROM usuarios"; // ✅ tabla correcta y sin filtros

        try (Connection conn = DatabaseConnection.getConnection();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Usuario usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("password"),
                        rs.getString("profesion"),
                        rs.getString("tipo")
                );
                lista.add(usuario);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener usuarios: " + e.getMessage());
        }

        return lista;
    }

    // 🔹 Insertar nuevo usuario
    public void insertar(Usuario usuario) {
        String sql = "INSERT INTO usuarios (dni, nombre, password, profesion, tipo) VALUES (?, ?, ?, ?, ?)"; // ✅ plural

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getProfesion());
            stmt.setString(5, usuario.getTipo());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al insertar usuario: " + e.getMessage());
        }
    }

    // 🔹 Actualizar usuario existente
    public void actualizar(Usuario usuario) {
        String sql = "UPDATE usuarios SET dni = ?, nombre = ?, password = ?, profesion = ?, tipo = ? WHERE id = ?"; // ✅ plural

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, usuario.getDni());
            stmt.setString(2, usuario.getNombre());
            stmt.setString(3, usuario.getPassword());
            stmt.setString(4, usuario.getProfesion());
            stmt.setString(5, usuario.getTipo());
            stmt.setInt(6, usuario.getId());

            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar usuario: " + e.getMessage());
        }
    }

    // 🔹 Eliminar usuario por ID
    public void eliminar(int id) {
        String sql = "DELETE FROM usuarios WHERE id = ?"; // ✅ plural

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);
            stmt.executeUpdate();

        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar usuario: " + e.getMessage());
        }
    }

    // 🔹 Buscar usuario por DNI y contraseña (para login)
    public Usuario buscarPorDniYPassword(String dni, String password) {
        Usuario usuario = null;
        String sql = "SELECT * FROM usuarios WHERE dni = ? AND password = ?"; // ✅ plural

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, dni);
            stmt.setString(2, password);

            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                usuario = new Usuario(
                        rs.getInt("id"),
                        rs.getString("dni"),
                        rs.getString("nombre"),
                        rs.getString("password"),
                        rs.getString("profesion"),
                        rs.getString("tipo")
                );
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al autenticar usuario: " + e.getMessage());
        }

        return usuario;
    }
}
