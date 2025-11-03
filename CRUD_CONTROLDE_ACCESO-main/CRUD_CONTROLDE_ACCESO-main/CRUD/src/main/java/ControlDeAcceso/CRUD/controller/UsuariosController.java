package ControlDeAcceso.CRUD.controller;

import ControlDeAcceso.CRUD.model.Usuario;
import ControlDeAcceso.CRUD.repository.UsuarioRepository;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;

public class UsuariosController {

    @FXML private TableView<Usuario> tablaUsuarios;
    @FXML private TableColumn<Usuario, Integer> colId;
    @FXML private TableColumn<Usuario, String> colDni;
    @FXML private TableColumn<Usuario, String> colNombre;
    @FXML private TableColumn<Usuario, String> colProfesion;
    @FXML private TableColumn<Usuario, String> colTipo;

    @FXML private TextField dniField;
    @FXML private TextField nombreField;
    @FXML private PasswordField passwordField;
    @FXML private TextField profesionField;
    @FXML private TextField tipoField;
    @FXML private Label mensajeLabel;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();
    private final ObservableList<Usuario> listaUsuarios = FXCollections.observableArrayList();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(c -> new javafx.beans.property.SimpleIntegerProperty(c.getValue().getId()).asObject());
        colDni.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getDni()));
        colNombre.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getNombre()));
        colProfesion.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getProfesion()));
        colTipo.setCellValueFactory(c -> new javafx.beans.property.SimpleStringProperty(c.getValue().getTipo()));

        cargarUsuarios();
    }

    private void cargarUsuarios() {
        listaUsuarios.setAll(usuarioRepository.obtenerTodos());
        tablaUsuarios.setItems(listaUsuarios);
    }

    @FXML
    private void agregarUsuario() {
        String dni = dniField.getText();
        String nombre = nombreField.getText();
        String password = passwordField.getText();
        String profesion = profesionField.getText();
        String tipo = tipoField.getText();

        if (dni.isEmpty() || nombre.isEmpty() || password.isEmpty()) {
            mensajeLabel.setText("⚠️ Todos los campos son obligatorios");
            return;
        }

        usuarioRepository.insertar(new Usuario(0, dni, nombre, password, profesion, tipo));
        mensajeLabel.setText("✅ Usuario agregado correctamente");
        limpiarCampos();
        cargarUsuarios();
    }

    @FXML
    private void editarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("⚠️ Selecciona un usuario para editar");
            return;
        }

        seleccionado.setDni(dniField.getText());
        seleccionado.setNombre(nombreField.getText());
        seleccionado.setPassword(passwordField.getText());
        seleccionado.setProfesion(profesionField.getText());
        seleccionado.setTipo(tipoField.getText());

        usuarioRepository.actualizar(seleccionado);
        mensajeLabel.setText("✅ Usuario actualizado correctamente");
        limpiarCampos();
        cargarUsuarios();
    }

    @FXML
    private void eliminarUsuario() {
        Usuario seleccionado = tablaUsuarios.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mensajeLabel.setText("⚠️ Selecciona un usuario para eliminar");
            return;
        }

        usuarioRepository.eliminar(seleccionado.getId());
        mensajeLabel.setText("🗑️ Usuario eliminado correctamente");
        cargarUsuarios();
    }

    private void limpiarCampos() {
        dniField.clear();
        nombreField.clear();
        passwordField.clear();
        profesionField.clear();
        tipoField.clear();
    }
}
