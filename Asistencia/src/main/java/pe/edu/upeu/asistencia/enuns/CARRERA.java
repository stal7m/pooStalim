package pe.edu.upeu.asistencia.enuns;

public enum CARRERA {
    Sistemas(FACULTAD.FIA),
    civil(FACULTAD.FIA),

    Administracion(FACULTAD.FCE),

    Nutriente(FACULTAD.FCE),

    Educacion(FACULTAD.FACIEHD),

    General(FACULTAD.GENERAL);

    private FACULTAD facultad;

    CARRERA(FACULTAD facultad) {
        this.facultad = facultad;
    }

    public FACULTAD getFacultad() {return facultad;}


}
