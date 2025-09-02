
package pe.edu.upeu.asistencia.repositorio;


import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;

import pe.edu.upeu.asistencia.enuns.CARRERA;
import pe.edu.upeu.asistencia.enuns.TIPO_PARTICIPANTE;
import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.ArrayList;
import java.util.List;

public abstract class ParticipanteRepositorio {
   public List<Participante> listaParticipantes =new ArrayList<>();

    public List<Participante> findAll(){
        listaParticipantes.add(
                new Participante(
                        new SimpleStringProperty("74463509"),
                        new SimpleStringProperty("Stalim"),
                        new SimpleStringProperty("Cayte"),
                        new SimpleBooleanProperty(true), CARRERA.Sistemas ,
                        TIPO_PARTICIPANTE.Asistente

                )

);
        listaParticipantes.add(
                new Participante(
                        new SimpleStringProperty("74463509"),
                        new SimpleStringProperty("Stalim"),
                        new SimpleStringProperty("Cutisaca"),
                        new SimpleBooleanProperty(true), CARRERA.Sistemas,
                        TIPO_PARTICIPANTE.Asistente
                )
        );
        return listaParticipantes;
    }
}
