package pe.edu.upeu.asistencia.servicio;

import org.springframework.stereotype.Service;
import pe.edu.upeu.asistencia.modelo.Participante;
import pe.edu.upeu.asistencia.repositorio.ParticipanteRepositorio;

import java.util.List;

@Service
public class ParticipanteServicioImp extends ParticipanteRepositorio implements ParticipanteServicioI {

    @Override
    public void save(Participante participante) {
        listaParticipantes.add(participante);
    }

    @Override
    public List<Participante> findAll() {
        if(listaParticipantes.isEmpty()){
            return super.findAll();
        }
        return listaParticipantes;
    }

    @Override
    public void update(Participante participante, int index) {
        listaParticipantes.set(index, participante);
    }

    @Override
    public void delete(int index) {
        listaParticipantes.remove(index);
    }

    @Override
    public Participante findById(int index) {
        return listaParticipantes.get(index);
    }
}
