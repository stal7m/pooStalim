package ControlDeAcceso.CRUD.controller;

import ControlDeAcceso.CRUD.model.Usuario;
import ControlDeAcceso.CRUD.service.UsuarioService;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class LoginController {

    @FXML
    private TextField dniField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private Label errorLabel;

    private final UsuarioService usuarioService = new UsuarioService();

    @FXML
    public void handleLogin(ActionEvent event) {
        String dni = dniField.getText();
        String password = passwordField.getText();

        Usuario usuario = usuarioService.autenticarUsuario(dni, password);

        if (usuario != null) {
            errorLabel.setText("✅ Bienvenido, " + usuario.getNombre() + "!");
            System.out.println("Login correcto → " + usuario.getNombre());

            // 🔹 Cargar la ventana principal (MainView)
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/ControlDeAcceso/CRUD/view/MainView.fxml"));
                Scene scene = new Scene(loader.load());
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(scene);
                stage.setTitle("Registro de Asistencias");
                stage.show();
            } catch (IOException e) {
                e.printStackTrace();
                errorLabel.setText(" Error al cargar la vista principal.");
            }

        } else {
            errorLabel.setText(" Credenciales incorrectas");
        }
    }
}
