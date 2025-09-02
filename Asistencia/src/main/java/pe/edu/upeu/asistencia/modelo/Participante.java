package pe.edu.upeu.asistencia.modelo;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.StringProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import pe.edu.upeu.asistencia.enuns.CARRERA;
import pe.edu.upeu.asistencia.enuns.TIPO_PARTICIPANTE;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class Participante {
    private StringProperty dni;
    private StringProperty nombre;
    private StringProperty apellidos;
    private BooleanProperty estado;
    private CARRERA carrera;

    private TIPO_PARTICIPANTE tipoParticipante;








}
