package ControlDeAcceso.CRUD.model;


public class Usuario {
    private int id;
    private String dni;
    private String nombre;
    private String password;
    private String profesion;
    private String tipo;

    // 🔹 Constructor vacío
    public Usuario() {}

    // 🔹 Constructor completo (6 parámetros)
    public Usuario(int id, String dni, String nombre, String password, String profesion, String tipo) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.password = password;
        this.profesion = profesion;
        this.tipo = tipo;
    }

    // 🔹 Constructor con 5 parámetros (sin tipo)
    public Usuario(int id, String dni, String nombre, String password, String profesion) {
        this.id = id;
        this.dni = dni;
        this.nombre = nombre;
        this.password = password;
        this.profesion = profesion;
        this.tipo = "Empleado"; // Valor por defecto, puedes cambiarlo si quieres
    }

    // 🔹 Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getProfesion() {
        return profesion;
    }

    public void setProfesion(String profesion) {
        this.profesion = profesion;
    }

    public String getTipo() {
        return tipo;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    @Override
    public String toString() {
        return "Usuario{" +
                "id=" + id +
                ", dni='" + dni + '\'' +
                ", nombre='" + nombre + '\'' +
                ", profesion='" + profesion + '\'' +
                ", tipo='" + tipo + '\'' +
                '}';
    }
}
