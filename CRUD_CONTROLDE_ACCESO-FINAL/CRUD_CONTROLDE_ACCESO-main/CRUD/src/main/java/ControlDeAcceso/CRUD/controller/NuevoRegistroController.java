package ControlDeAcceso.CRUD.controller;

import ControlDeAcceso.CRUD.model.Asistencia;
import ControlDeAcceso.CRUD.repository.AsistenciaRepository;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

public class NuevoRegistroController {

    @FXML private TextField txtNombre;
    @FXML private TextField txtDni;
    @FXML private TextField txtProfesion;
    @FXML private ComboBox<String> cbTipo;
    @FXML private TextField txtHoraIngreso;
    @FXML private TextField txtHoraSalida;

    private final AsistenciaRepository repo = new AsistenciaRepository();
    private boolean modoEdicion = false; // 🔹 Saber si estamos editando o creando
    private int idEdicion = -1;

    @FXML
    public void initialize() {
        // 🔹 Cargar opciones del ComboBox
        cbTipo.setItems(FXCollections.observableArrayList("Docente", "Estudiante", "Externo"));
    }

    @FXML
    public void handleGuardar(ActionEvent event) {
        String nombre = txtNombre.getText().trim();
        String dni = txtDni.getText().trim();
        String profesion = txtProfesion.getText().trim();
        String tipo = cbTipo.getValue();
        String horaIngreso = txtHoraIngreso.getText().trim();
        String horaSalida = txtHoraSalida.getText().trim();

        // 🔸 Validar campos
        if (nombre.isEmpty() || dni.isEmpty() || tipo == null || horaIngreso.isEmpty() || horaSalida.isEmpty()) {
            mostrarAlerta(Alert.AlertType.WARNING, "Campos incompletos", "⚠️ Todos los campos son obligatorios.");
            return;
        }

        // 🔹 Crear objeto Asistencia
        Asistencia registro = new Asistencia(idEdicion, nombre, dni,
                java.time.LocalDate.now().toString(), horaIngreso, horaSalida, profesion, tipo);

        if (modoEdicion) {
            repo.actualizar(registro);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Registro actualizado", " Los cambios se guardaron correctamente.");
        } else {
            Asistencia existente = repo.buscarPorDni(dni);
            if (existente != null) {
                mostrarAlerta(Alert.AlertType.WARNING, "Registro duplicado", "Ya existe una asistencia registrada para este DNI.");
                return;
            }
            repo.insertar(registro);
            mostrarAlerta(Alert.AlertType.INFORMATION, "Asistencia registrada", "Nueva asistencia registrada correctamente.");
        }

        cerrarVentana();
    }

    @FXML
    public void handleCancelar(ActionEvent event) {
        cerrarVentana();
    }

    // 🔹 Método para cargar datos cuando se edita un registro existente
    public void cargarDatosParaEditar(Asistencia asistencia) {
        modoEdicion = true;
        idEdicion = asistencia.getId();

        txtNombre.setText(asistencia.getNombre());
        txtDni.setText(asistencia.getDni());
        txtProfesion.setText(asistencia.getProfesion());
        cbTipo.setValue(asistencia.getTipo());
        txtHoraIngreso.setText(asistencia.getHoraIngreso());
        txtHoraSalida.setText(asistencia.getHoraSalida());
    }

    // 🔹 Métodos de utilidad
    private void cerrarVentana() {
        Stage stage = (Stage) txtNombre.getScene().getWindow();
        stage.close();
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}
