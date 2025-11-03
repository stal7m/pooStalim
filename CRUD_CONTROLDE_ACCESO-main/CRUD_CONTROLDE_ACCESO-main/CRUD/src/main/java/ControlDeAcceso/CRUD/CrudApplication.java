package ControlDeAcceso.CRUD;

import ControlDeAcceso.CRUD.database.SetupDatabase;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class CrudApplication extends Application {

    @Override
    public void start(Stage stage) throws Exception {
        SetupDatabase.initialize();
        // Crea las tablas y usuario admin si no existen

        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/ControlDeAcceso/CRUD/view/LoginView.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Sistema de Control de Asistencia");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
