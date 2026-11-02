package sistemabecas.modelo;

import java.time.LocalDate;
import java.util.Objects;

public class SolicitudBeca implements Comparable<SolicitudBeca> {

    private String codigo;              // Clave única de la solicitud (ej: SOL-001)
    private Estudiante estudiante;      // Relación con Estudiante
    private String tipoBeca;            // Meritocracia, Socioeconomica, etc.
    private double montoSolicitado;
    private int prioridad;              // 1 = más alta, 5 = más baja (o al revés según prefieras)
    private String estado;              // PENDIENTE, EN_REVISION, APROBADA, RECHAZADA, CANCELADA
    private LocalDate fechaSolicitud;

    // Constructor
    public SolicitudBeca(String codigo, Estudiante estudiante, String tipoBeca,
                         double montoSolicitado, int prioridad, String estado) {
        this.codigo = codigo;
        this.estudiante = estudiante;
        this.tipoBeca = tipoBeca;
        this.montoSolicitado = montoSolicitado;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fechaSolicitud = LocalDate.now();
    }

    // Getters
    public String getCodigo() {
        return codigo;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public String getTipoBeca() {
        return tipoBeca;
    }

    public double getMontoSolicitado() {
        return montoSolicitado;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public LocalDate getFechaSolicitud() {
        return fechaSolicitud;
    }

    // Setters
    public void setTipoBeca(String tipoBeca) {
        this.tipoBeca = tipoBeca;
    }

    public void setMontoSolicitado(double montoSolicitado) {
        this.montoSolicitado = montoSolicitado;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    // equals y hashCode basados en el código (clave única)
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SolicitudBeca that = (SolicitudBeca) o;
        return Objects.equals(codigo, that.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    // Comparable: orden natural por prioridad (menor número = mayor prioridad)
    @Override
    public int compareTo(SolicitudBeca otra) {
        return Integer.compare(this.prioridad, otra.prioridad);
    }

    @Override
    public String toString() {
        return "SolicitudBeca{" +
                "codigo='" + codigo + '\'' +
                ", estudiante=" + estudiante.getNombre() + " " + estudiante.getApellido() +
                ", tipoBeca='" + tipoBeca + '\'' +
                ", monto=" + montoSolicitado +
                ", prioridad=" + prioridad +
                ", estado='" + estado + '\'' +
                ", fecha=" + fechaSolicitud +
                '}';
    }
}
