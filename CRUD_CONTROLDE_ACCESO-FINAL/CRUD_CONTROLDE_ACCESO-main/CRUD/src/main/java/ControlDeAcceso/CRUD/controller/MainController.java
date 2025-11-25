package ControlDeAcceso.CRUD.controller;

import ControlDeAcceso.CRUD.model.Asistencia;
import ControlDeAcceso.CRUD.repository.AsistenciaRepository;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.client.j2se.MatrixToImageWriter;

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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.embed.swing.SwingFXUtils;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;


import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

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
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ControlDeAcceso/CRUD/view/NuevoRegistro.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = new Stage();
            stage.setTitle("Registrar nueva asistencia");
            stage.setScene(scene);
            stage.showAndWait();


            cargarDatos();
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    "✅ Nueva asistencia registrada correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Registrar asistencia usando un DNI (se llama desde el diálogo y desde importar Excel)
     */
    private void registrarAsistenciaPorDni(String dni, Stage stage) {
        if (dni == null || dni.trim().isEmpty()) {
            mostrarToast(stage, "⚠️ Debes ingresar un DNI válido.");
            return;
        }

        dni = dni.trim();

        Asistencia existente = repo.buscarPorDni(dni); // debe devolver el último registro de ese DNI
        java.time.LocalDate hoy = java.time.LocalDate.now();
        java.time.LocalTime horaActual = java.time.LocalTime.now()
                .truncatedTo(java.time.temporal.ChronoUnit.MINUTES);


        if (existente != null) {

            //  Si el registro es de HOY
            if (hoy.toString().equals(existente.getFecha())) {


                if (existente.getHoraSalida() == null || existente.getHoraSalida().isEmpty()) {
                    repo.actualizarHoraSalida(dni, horaActual.toString());
                    cargarDatos();
                    mostrarToast(stage, "👋 " + existente.getNombre()
                            + " marcó su salida a las " + horaActual);
                    return;
                }


                repo.actualizarHoraSalida(dni, horaActual.toString());
                cargarDatos();
                mostrarToast(stage, "⏱ Se actualizó la hora de salida de "
                        + existente.getNombre() + " a las " + horaActual);
                return;
            }

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
            return;
        }

        //  No existe ningún usuario con ese DNI en la BD
        mostrarToast(stage, "❗ DNI no encontrado. Registre primero al usuario con 'Nuevo'.");
    }

    @FXML
    public void handleRegistrar(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Registrar asistencia");
        dialog.setHeaderText("Ingrese el DNI del usuario");
        dialog.setContentText("DNI:");

        dialog.showAndWait().ifPresent(dni -> registrarAsistenciaPorDni(dni, stage));
    }

    @FXML
    public void handleEliminar(ActionEvent event) {
        Asistencia seleccion = tablaAsistencias.getSelectionModel().getSelectedItem();
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        if (seleccion != null) {
            repo.eliminar(seleccion.getId());
            cargarDatos();
            mostrarToast(stage, "✅ Registro eliminado correctamente.");
        } else {
            mostrarToast(stage, " Selecciona una fila para eliminar.");
        }
    }

    @FXML
    public void handleCerrarSesion(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ControlDeAcceso/CRUD/view/LoginView.fxml"));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.setTitle("Iniciar sesión");
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // Notificación tipo "toast"
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

        javafx.scene.layout.Pane root = (javafx.scene.layout.Pane) stage.getScene().getRoot();

        StackPane overlay = new StackPane(label);
        overlay.setMouseTransparent(true);
        StackPane.setAlignment(label, javafx.geometry.Pos.BOTTOM_CENTER);
        StackPane.setMargin(label, new javafx.geometry.Insets(0, 0, 30, 0));

        root.getChildren().add(overlay);

        FadeTransition fadeIn = new FadeTransition(Duration.millis(250), label);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

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
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    " Ingrese un nombre o DNI para buscar.");
            return;
        }

        ObservableList<Asistencia> filtrados = FXCollections.observableArrayList();
        for (Asistencia a : repo.obtenerTodos()) {
            if (a.getNombre().toLowerCase().contains(filtro)
                    || a.getDni().toLowerCase().contains(filtro)) {
                filtrados.add(a);
            }
        }

        tablaAsistencias.setItems(filtrados);
        mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                filtrados.isEmpty()
                        ? " No se encontraron coincidencias."
                        : "✅ Resultados filtrados correctamente.");
    }

    @FXML
    public void handleMostrarTodo(ActionEvent event) {
        cargarDatos();
        txtBuscar.clear();
        mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                "🔄 Mostrando todos los registros.");
    }

    @FXML
    public void handleEditar(ActionEvent event) {
        Asistencia seleccion = tablaAsistencias.getSelectionModel().getSelectedItem();

        if (seleccion == null) {
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    " Selecciona una fila para editar.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(
                    getClass().getResource("/ControlDeAcceso/CRUD/view/NuevoRegistro.fxml"));
            Scene scene = new Scene(loader.load());
            NuevoRegistroController controller = loader.getController();
            controller.cargarDatosParaEditar(seleccion);

            Stage stage = new Stage();
            stage.setTitle("Editar asistencia");
            stage.setScene(scene);
            stage.showAndWait();

            cargarDatos();
            mostrarToast((Stage) ((Node) event.getSource()).getScene().getWindow(),
                    " Registro editado correctamente.");

        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML
    public void handleImportarExcel(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar archivo de asistencias (Excel)");
        fileChooser.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivos Excel (*.xlsx)", "*.xlsx")
        );

        File archivo = fileChooser.showOpenDialog(stage);
        if (archivo == null) {
            return; // usuario canceló
        }

        int contador = 0;

        try (FileInputStream fis = new FileInputStream(archivo);
             Workbook workbook = new XSSFWorkbook(fis)) {

            Sheet sheet = workbook.getSheetAt(0); // primera hoja del Excel

            // Estructura típica de Google Forms:
            // Columna 0 -> Timestamp
            // Columna 1 -> DNI
            // Columna 2 -> Nombre completo
            // Empezamos desde la fila 1 (saltamos encabezado)
            for (int i = 1; i <= sheet.getLastRowNum(); i++) {
                Row row = sheet.getRow(i);
                if (row == null) continue;

                Cell dniCell = row.getCell(1); // columna B = DNI
                if (dniCell == null) continue;

                String dni = null;

                if (dniCell.getCellType() == CellType.STRING) {
                    dni = dniCell.getStringCellValue();
                } else if (dniCell.getCellType() == CellType.NUMERIC) {
                    dni = String.valueOf((long) dniCell.getNumericCellValue());
                }

                if (dni == null || dni.trim().isEmpty()) continue;

                // Usamos tu lógica para registrar asistencia
                registrarAsistenciaPorDni(dni.trim(), stage);
                contador++;
            }

            mostrarToast(stage, "✅ Importación completada: " + contador + " asistencias agregadas.");

        } catch (Exception e) {
            e.printStackTrace();
            mostrarToast(stage, "❌ Error al leer el archivo de Excel.");
        }
    }



    @FXML
    public void onEscanearQR(ActionEvent event) {
        Stage mainStage = (Stage) ((Node) event.getSource()).getScene().getWindow();

        try {

            String contenidoQR = "https://docs.google.com/forms/d/e/1FAIpQLSdAhdvCvD80zXFis8PimJb5gM8CTXcQPhBZzT6343dp4gE-DQ/viewform?usp=dialog";

            int width = 300;
            int height = 300;

            BitMatrix matrix = new MultiFormatWriter()
                    .encode(contenidoQR, BarcodeFormat.QR_CODE, width, height);

            BufferedImage bufferedImage = MatrixToImageWriter.toBufferedImage(matrix);
            Image fxImage = SwingFXUtils.toFXImage(bufferedImage, null);

            ImageView imageView = new ImageView(fxImage);
            imageView.setFitWidth(width);
            imageView.setFitHeight(height);
            imageView.setPreserveRatio(true);

            StackPane root = new StackPane(imageView);
            Scene scene = new Scene(root, width + 40, height + 40);

            Stage ventanaQR = new Stage();
            ventanaQR.setTitle("Escanea este código QR con tu celular");
            ventanaQR.setScene(scene);
            ventanaQR.initOwner(mainStage);
            ventanaQR.show();

        } catch (Exception e) {
            e.printStackTrace();
            mostrarToast(mainStage, "❌ Error al generar el código QR.");
        }
    }

    private void mostrarAlerta(Alert.AlertType tipo, String titulo, String mensaje) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.show();
    }
}
