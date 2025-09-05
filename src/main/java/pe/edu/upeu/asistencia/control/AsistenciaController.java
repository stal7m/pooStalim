package pe.edu.upeu.asistencia.control;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.servicio.ParticipanteServicioI;

@Controller
public class AsistenciaController {

    @Autowired
    ParticipanteServicioI participanteServicioI;

    @FXML private Label idMsg;
    @FXML TextField txtDato;

    //@FXML Button btnEnviar;

    @FXML
    void enviar(){
        System.out.println("Enviando asistencia");
        idMsg.setText(txtDato.getText());
    }

    @FXML
    void regEstudiante(){
        Participante participante = new Participante();
        participante.setNombre(new SimpleStringProperty(txtDato.getText()));
        participante.setEstado(new SimpleBooleanProperty(true));

        participanteServicioI.save(participante);
        listarEstudiantes();
    }

    void listarEstudiantes(){
        for (Participante e: participanteServicioI.findAll()){
            System.out.println(e.getNombre());
        }
    }


}
