package pe.edu.upeu.asistencia.enums;

import lombok.Getter;

public enum CARRERA {
    Sistemas(FACULTAD.FIA),
    Civil(FACULTAD.FIA),

    Administrador(FACULTAD.FCE),

    Nutricion(FACULTAD.FCS),

    Educacion(FACULTAD.FACIHED),

    General(FACULTAD.GENERAL);

    private FACULTAD facultad;

    CARRERA(FACULTAD facultad) {
        this.facultad = facultad;
    }

    public FACULTAD getFacultad() {return facultad;}

}
