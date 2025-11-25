package ControlDeAcceso.CRUD.model;

public class Asistencia {
    private int id;
    private String nombre;
    private String dni;
    private String fecha;
    private String horaIngreso;
    private String horaSalida;
    private String profesion;
    private String tipo;

    public Asistencia(int id, String nombre, String dni, String fecha, String horaIngreso, String horaSalida, String profesion, String tipo) {
        this.id = id;
        this.nombre = nombre;
        this.dni = dni;
        this.fecha = fecha;
        this.horaIngreso = horaIngreso;
        this.horaSalida = horaSalida;
        this.profesion = profesion;
        this.tipo = tipo;
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDni() { return dni; }
    public String getFecha() { return fecha; }
    public String getHoraIngreso() { return horaIngreso; }
    public String getHoraSalida() { return horaSalida; }
    public String getProfesion() { return profesion; }
    public String getTipo() { return tipo; }

    public void setHoraSalida(String horaSalida) {
        this.horaSalida = horaSalida;
    }
}
