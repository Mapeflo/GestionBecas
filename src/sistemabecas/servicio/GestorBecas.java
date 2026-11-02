package sistemabecas.servicio;

import sistemabecas.modelo.Estudiante;
import sistemabecas.modelo.SolicitudBeca;

import java.util.*;

public class GestorBecas {

    // ========== COLECCIONES ==========

    // 1. Colección principal de solicitudes (List)
    private List<SolicitudBeca> listaSolicitudes;

    // 2. Mapa para búsqueda rápida de solicitudes por código
    private Map<String, SolicitudBeca> solicitudesPorCodigo;

    // 3. Mapa para búsqueda rápida de estudiantes por documento
    private Map<String, Estudiante> estudiantesPorDocumento;

    // 4. Set para controlar tipos de beca únicos
    private Set<String> tiposBecaUnicos;

    // 5. TreeSet para mantener tipos de beca ordenados alfabéticamente
    private Set<String> tiposBecaOrdenados;

    // 6. PriorityQueue para procesar solicitudes por prioridad
    private PriorityQueue<SolicitudBeca> colaPrioridad;

    // Constructor
    public GestorBecas() {
        this.listaSolicitudes = new ArrayList<>();
        this.solicitudesPorCodigo = new HashMap<>();
        this.estudiantesPorDocumento = new HashMap<>();
        this.tiposBecaUnicos = new HashSet<>();
        this.tiposBecaOrdenados = new TreeSet<>();
        this.colaPrioridad = new PriorityQueue<>();
    }

    // ========== MÉTODOS DE REGISTRO ==========

    public boolean registrarEstudiante(Estudiante estudiante) {
        if (estudiante == null || estudiante.getDocumento() == null || estudiante.getDocumento().isBlank()) {
            return false;
        }
        if (estudiantesPorDocumento.containsKey(estudiante.getDocumento())) {
            return false; // Ya existe
        }
        estudiantesPorDocumento.put(estudiante.getDocumento(), estudiante);
        return true;
    }

    public boolean registrarSolicitud(SolicitudBeca solicitud) {
        if (solicitud == null || solicitud.getCodigo() == null || solicitud.getCodigo().isBlank()) {
            return false;
        }
        if (solicitudesPorCodigo.containsKey(solicitud.getCodigo())) {
            return false; // Código duplicado
        }
        if (solicitud.getEstudiante() == null) {
            return false;
        }

        // Registrar estudiante si no existe
        registrarEstudiante(solicitud.getEstudiante());

        // Agregar a todas las colecciones
        listaSolicitudes.add(solicitud);
        solicitudesPorCodigo.put(solicitud.getCodigo(), solicitud);
        tiposBecaUnicos.add(solicitud.getTipoBeca());
        tiposBecaOrdenados.add(solicitud.getTipoBeca());

        // Solo las pendientes van a la cola de prioridad
        if ("PENDIENTE".equalsIgnoreCase(solicitud.getEstado())) {
            colaPrioridad.offer(solicitud);
        }

        return true;
    }

    // ========== CONSULTAS ==========

    public SolicitudBeca buscarSolicitudPorCodigo(String codigo) {
        return solicitudesPorCodigo.get(codigo);
    }

    public Estudiante buscarEstudiantePorDocumento(String documento) {
        return estudiantesPorDocumento.get(documento);
    }

    public List<SolicitudBeca> obtenerTodasLasSolicitudes() {
        return new ArrayList<>(listaSolicitudes); // Copia defensiva
    }

    public Set<String> obtenerTiposBecaUnicos() {
        return new HashSet<>(tiposBecaUnicos);
    }

    public Set<String> obtenerTiposBecaOrdenados() {
        return new TreeSet<>(tiposBecaOrdenados);
    }

    // ========== PRIORIDAD ==========

    public SolicitudBeca verSiguientePrioritaria() {
        return colaPrioridad.peek(); // No la elimina
    }

    public SolicitudBeca procesarSiguientePrioritaria() {
        SolicitudBeca siguiente = colaPrioridad.poll(); // La elimina de la cola
        if (siguiente != null) {
            siguiente.setEstado("EN_REVISION");
        }
        return siguiente;
    }

    // ========== MODIFICAR ==========

    public boolean actualizarEstado(String codigo, String nuevoEstado) {
        SolicitudBeca sol = solicitudesPorCodigo.get(codigo);
        if (sol == null) return false;

        sol.setEstado(nuevoEstado);

        // Si ya no está pendiente, la quitamos de la cola de prioridad
        if (!"PENDIENTE".equalsIgnoreCase(nuevoEstado)) {
            colaPrioridad.remove(sol);
        }
        return true;
    }

    public boolean actualizarPrioridad(String codigo, int nuevaPrioridad) {
        SolicitudBeca sol = solicitudesPorCodigo.get(codigo);
        if (sol == null) return false;

        // Para PriorityQueue es más seguro eliminar y volver a insertar
        colaPrioridad.remove(sol);
        sol.setPrioridad(nuevaPrioridad);
        if ("PENDIENTE".equalsIgnoreCase(sol.getEstado())) {
            colaPrioridad.offer(sol);
        }
        return true;
    }

    // ========== ELIMINAR ==========

    public boolean eliminarSolicitud(String codigo) {
        SolicitudBeca sol = solicitudesPorCodigo.remove(codigo);
        if (sol == null) return false;

        listaSolicitudes.remove(sol);
        colaPrioridad.remove(sol);

        // Nota: no se elimino el tipo de beca del Set porque puede usarse en otras solicitudes
        return true;
    }

    // ========== INFORMACIÓN DE COLECCIONES (para depuración) ==========

    public void mostrarEstadoColecciones() {
        System.out.println("\n===== ESTADO DE LAS COLECCIONES =====");
        System.out.println("Total solicitudes (List): " + listaSolicitudes.size());
        System.out.println("Solicitudes por código (Map): " + solicitudesPorCodigo.size());
        System.out.println("Estudiantes registrados (Map): " + estudiantesPorDocumento.size());
        System.out.println("Tipos de beca únicos (HashSet): " + tiposBecaUnicos);
        System.out.println("Tipos de beca ordenados (TreeSet): " + tiposBecaOrdenados);
        System.out.println("Solicitudes en cola de prioridad: " + colaPrioridad.size());
        System.out.println("=====================================\n");
    }
}
