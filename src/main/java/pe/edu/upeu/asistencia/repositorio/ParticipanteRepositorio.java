package pe.edu.upeu.asistencia.repositorio;

import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import pe.edu.upeu.asistencia.enums.CARRERA;
import pe.edu.upeu.asistencia.enums.TIPO_PARTICIPANTE;
import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticipanteRepositorio {
   public List<Participante> listaParticipantes =new ArrayList<>();

   public List<Participante> findAll(){
      listaParticipantes.add(
              new Participante(
                      new SimpleStringProperty("43631917"),
                      new SimpleStringProperty("Juan"),
                      new SimpleStringProperty("Apaza"),
                      new SimpleBooleanProperty(true), CARRERA.Sistemas,
                      TIPO_PARTICIPANTE.Asistente
              )
      );
      listaParticipantes.add(
              new Participante(
                      new SimpleStringProperty("43631918"),
                      new SimpleStringProperty("Pedro"),
                      new SimpleStringProperty("Gutierrez"),
                      new SimpleBooleanProperty(true), CARRERA.Sistemas,
                      TIPO_PARTICIPANTE.Asistente
              )
      );
      return listaParticipantes;
   }

}
