package pe.edu.upeu.asistencia.servicio;

import pe.edu.upeu.asistencia.modelo.Participante;

import java.util.List;

public interface ParticipanteServicioI {

    void save(Participante participante); //C

    List<Participante> findAll(); // R

    void update(Participante participante, int index); //U

    void delete(int index); //D

    Participante findById(int index); //Buscar

}
