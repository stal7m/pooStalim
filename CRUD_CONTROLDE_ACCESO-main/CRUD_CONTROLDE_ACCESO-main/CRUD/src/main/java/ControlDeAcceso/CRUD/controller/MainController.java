package ControlDeAcceso.CRUD.controller;

import javafx.scene.control.TextField;
import ControlDeAcceso.CRUD.model.Asistencia;
import ControlDeAcceso.CRUD.repository.AsistenciaRepository;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.io.IOException;

public class MainController {

    @FXML private Label lblUsuarioConectado;
    @FXML private TextField txtBuscar;
    @FXML private TableView<Asistencia> tablaAsistencias;
    @FXML private TableColumn<Asistencia, Integer> colId;
    @FXML private TableColumn<Asistencia, String> colNombre;
    @FXML private TableColumn<Asistencia, String> colDni;
    @FXML private TableColumn<Asistencia, String> colFecha;
    @FXML private TableColumn<Asistencia, String> colHoraIngreso;
    @FXML private TableColumn<Asistencia, String> colHoraSalida;
    @FXML private TableColumn<Asistencia, String> colProfesion;
    @FXML private TableColumn<Asistencia, String> colTipo;

    private final ObservableList<Asistencia> listaAsistencias = FXCollections.observableArrayList();
    private final AsistenciaRepository repo = new AsistenciaRepository();

    @FXML
    public void initialize() {
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNombre.setCellValueFactory(new PropertyValueFactory<>("nombre"));
        colDni.setCellValueFactory(new PropertyValueFactory<>("dni"));
        colFecha.setCellValueFactory(new PropertyValueFactory<>("fecha"));
        colHoraIngreso.setCellValueFactory(new PropertyValueFactory<>("horaIngreso"));
        colHoraSalida.setCellValueFactory(new PropertyValueFactory<>("horaSalida"));
        colProfesion.setCellValueFactory(new PropertyValueFactory<>("profesion"));
        colTipo.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        lblUsuarioConectado.setText("👤 Conectado como: Administrador");


        cargarDatos();
    }

    private void cargarDatos() {
        listaAsistencias.setAll(repo.obtenerTodos());
        tablaAsistencias.setItems(listaAsistencias);
    }

    @FXML
    public void handleNuevo(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ControlDeAcceso/CRUD/view/NuevoRegistro.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Registrar nueva asistencia");
            stage.setScene(scene);
            stage.showAndWait();

            // Recargar tabla y mostrar notificación
            cargarDatos();
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    "✅ Nueva asistencia registrada correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleRegistrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Registrar asistencia");
        dialog.setHeaderText("Ingrese el DNI del usuario");
        dialog.setContentText("DNI:");

        dialog.showAndWait().ifPresent(dni -> {
            if (dni.trim().isEmpty()) {
                mostrarToast(stage, "⚠️ Debes ingresar un DNI válido.");
                return;
            }

            // Buscar si el DNI existe
            Asistencia existente = repo.buscarPorDni(dni.trim());

            if (existente != null) {
                java.time.LocalDate hoy = java.time.LocalDate.now();
                java.time.LocalTime horaActual = java.time.LocalTime.now()
                        .truncatedTo(java.time.temporal.ChronoUnit.MINUTES);

                // Si ya tiene asistencia registrada hoy → actualiza hora de salida
                if (existente.getFecha().equals(hoy.toString()) &&
                        (existente.getHoraSalida() == null || existente.getHoraSalida().isEmpty())) {

                    repo.actualizarHoraSalida(dni.trim(), horaActual.toString());
                    cargarDatos();
                    mostrarToast(stage, "👋 " + existente.getNombre() + " marcó su salida a las " + horaActual);
                    return;
                }

                // Si no tiene asistencia hoy → registrar nueva
                Asistencia nueva = new Asistencia(
                        0,
                        existente.getNombre(),
                        existente.getDni(),
                        hoy.toString(),
                        horaActual.toString(),
                        "",
                        existente.getProfesion(),
                        existente.getTipo()
                );

                repo.insertar(nueva);
                cargarDatos();
                mostrarToast(stage, "✅ Asistencia registrada para " + existente.getNombre());

            } else {
                mostrarToast(stage, "⚠️ DNI no encontrado. Registre primero al usuario con 'Nuevo'.");
            }
        });
    }




    @FXML
    public void handleEliminar(ActionEvent event) {
        Asistencia seleccion = tablaAsistencias.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (seleccion != null) {
            repo.eliminar(seleccion.getId());
            cargarDatos();
            mostrarToast(stage, "🗑️ Registro eliminado correctamente.");
        } else {
            mostrarToast(stage, "⚠️ Selecciona una fila para eliminar.");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ControlDeAcceso/CRUD/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Iniciar sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 🔹 Método mejorado para mostrar notificaciones flotantes (toasts)
    private void mostrarToast(Stage stage, String mensaje) {
        Label label = new Label(mensaje);
        label.setStyle(
                "-fx-background-color: rgba(40,40,40,0.9);" +
                        "-fx-text-fill: white;" +
                        "-fx-padding: 10px 20px;" +
                        "-fx-background-radius: 10;" +
                        "-fx-font-size: 14px;"
        );
        label.setOpacity(0);

        // Obtener el nodo raíz de la escena (cualquiera sea el layout)
        javafx.scene.layout.Pane root = (javafx.scene.layout.Pane) stage.getScene().getRoot();

        // Usar un StackPane temporal para el toast
        StackPane overlay = new StackPane(label);
        overlay.setMouseTransparent(true); // no bloquea botones ni clics
        StackPane.setAlignment(label, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setMargin(label, new javafx.geometry.Insets(0, 0, 30, 0));

        root.getChildren().add(overlay);

        // Animación de entrada
        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Animación de salida
        FadeTransition fadeOut = new FadeTransition(Duration.millis(350), label);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setDelay(Duration.seconds(2.8));
        fadeOut.setOnFinished(e -> root.getChildren().remove(overlay));

        fadeIn.play();
        fadeOut.play();
    }

    @FXML
    public void handleBuscar(ActionEvent event) {
        String filtro = txtBuscar.getText().trim().toLowerCase();

        if (filtro.isEmpty()) {
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(), "⚠️ Ingrese un nombre o DNI para buscar.");
            return;
        }

        ObservableList<Asistencia> filtrados = FXCollections.observableArrayList();
        for (Asistencia a : repo.obtenerTodos()) {
            if (a.getNombre().toLowerCase().contains(filtro) || a.getDni().toLowerCase().contains(filtro)) {
                filtrados.add(a);
            }
        }

        tablaAsistencias.setItems(filtrados);
        mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                filtrados.isEmpty() ? "❌ No se encontraron coincidencias." : "✅ Resultados filtrados correctamente.");
    }

    @FXML
    public void handleMostrarTodo(ActionEvent event) {
        cargarDatos();
        txtBuscar.clear();
        mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(), "🔄 Mostrando todos los registros.");
    }
    @FXML
    public void handleEditar(ActionEvent event) {
        Asistencia seleccion = tablaAsistencias.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    "⚠️ Selecciona una fila para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/ControlDeAcceso/CRUD/view/NuevoRegistro.fxml"));
            Scene scene = new Scene(loader.load());
            NuevoRegistroController controller = loader.getController();
            controller.cargarDatosParaEditar(seleccion); // 👈 le pasamos el registro seleccionado

            Stage stage = new Stage();
            stage.setTitle("Editar asistencia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarDatos(); // Recargar tabla
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    "✏️ Registro editado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



}
