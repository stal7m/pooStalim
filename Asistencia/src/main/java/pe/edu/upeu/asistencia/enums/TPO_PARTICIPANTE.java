package pe.edu.upeu.asistencia.enums;

public enum TPO_PARTICIPANTE {
    ORGANIZADOR("organizador"),
    ASISTENTE("asistente");

    private final String description;

    TPO_PARTICIPANTE(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}