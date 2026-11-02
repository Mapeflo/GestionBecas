package sistemabecas.modelo;

import java.util.Objects;

public class Estudiante {

    private String documento;   // Clave única (cédula, TI, etc.)
    private String nombre;
    private String apellido;
    private String programa;    // Carrera o programa académico
    private double promedio;

    // Constructor
    public Estudiante(String documento, String nombre, String apellido, String programa, double promedio) {
        this.documento = documento;
        this.nombre = nombre;
        this.apellido = apellido;
        this.programa = programa;
        this.promedio = promedio;
    }

    // Getters
    public String getDocumento() {
        return documento;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellido() {
        return apellido;
    }

    public String getPrograma() {
        return programa;
    }

    public double getPromedio() {
        return promedio;
    }

    // Setters (solo los que se pueden modificar)
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellido(String apellido) {
        this.apellido = apellido;
    }

    public void setPrograma(String programa) {
        this.programa = programa;
    }

    public void setPromedio(double promedio) {
        this.promedio = promedio;
    }

    // equals y hashCode basados SOLO en el documento (clave única)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Estudiante that = (Estudiante) o;
        return Objects.equals(documento, that.documento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(documento);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "documento='" + documento + '\'' +
                ", nombre='" + nombre + " " + apellido + '\'' +
                ", programa='" + programa + '\'' +
                ", promedio=" + promedio +
                '}';
    }
}