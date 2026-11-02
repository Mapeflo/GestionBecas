package sistemabecas;

import sistemabecas.modelo.Estudiante;
import sistemabecas.modelo.SolicitudBeca;
import sistemabecas.servicio.GestorBecas;

import java.util.List;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static final Scanner sc = new Scanner(System.in);
    private static final GestorBecas gestor = new GestorBecas();

    public static void main(String[] args) {
        cargarDatosPrueba(); // Cargamos datos de prueba al inicio
        int opcion;

        do {
            mostrarMenu();
            opcion = leerEntero("Seleccione una opción: ");

            switch (opcion) {
                case 1 -> registrarEstudiante();
                case 2 -> registrarSolicitud();
                case 3 -> consultarTodasSolicitudes();
                case 4 -> buscarSolicitudPorCodigo();
                case 5 -> buscarEstudiantePorDocumento();
                case 6 -> mostrarTiposBecaUnicos();
                case 7 -> mostrarTiposBecaOrdenados();
                case 8 -> verSiguientePrioritaria();
                case 9 -> procesarSiguientePrioritaria();
                case 10 -> actualizarEstado();
                case 11 -> actualizarPrioridad();
                case 12 -> eliminarSolicitud();
                case 13 -> gestor.mostrarEstadoColecciones();
                case 0 -> System.out.println("¡Gracias por usar el sistema! Hasta luego.");
                default -> System.out.println("Opción no válida. Intente de nuevo.");
            }

            if (opcion != 0) {
                System.out.println("\nPresione Enter para continuar...");
                sc.nextLine();
            }

        } while (opcion != 0);

        sc.close();
    }

    private static void mostrarMenu() {
        System.out.println("\n========== SISTEMA DE SOLICITUDES DE BECAS ==========");
        System.out.println("1.  Registrar estudiante");
        System.out.println("2.  Registrar solicitud de beca");
        System.out.println("3.  Consultar todas las solicitudes");
        System.out.println("4.  Buscar solicitud por código");
        System.out.println("5.  Buscar estudiante por documento");
        System.out.println("6.  Mostrar tipos de beca únicos (HashSet)");
        System.out.println("7.  Mostrar tipos de beca ordenados (TreeSet)");
        System.out.println("8.  Ver siguiente solicitud prioritaria (sin procesar)");
        System.out.println("9.  Procesar siguiente solicitud prioritaria");
        System.out.println("10. Actualizar estado de una solicitud");
        System.out.println("11. Actualizar prioridad de una solicitud");
        System.out.println("12. Eliminar solicitud");
        System.out.println("13. Mostrar estado de todas las colecciones");
        System.out.println("0.  Salir");
        System.out.println("====================================================");
    }

    // ========== MÉTODOS DEL MENÚ ==========

    private static void registrarEstudiante() {
        System.out.println("\n--- Registrar Estudiante ---");
        String documento = leerTexto("Documento: ");
        String nombre = leerTexto("Nombre: ");
        String apellido = leerTexto("Apellido: ");
        String programa = leerTexto("Programa/Carrera: ");
        double promedio = leerDouble("Promedio: ");

        Estudiante est = new Estudiante(documento, nombre, apellido, programa, promedio);
        if (gestor.registrarEstudiante(est)) {
            System.out.println("✓ Estudiante registrado correctamente.");
        } else {
            System.out.println("✗ Error: El documento ya existe o datos inválidos.");
        }
    }

    private static void registrarSolicitud() {
        System.out.println("\n--- Registrar Solicitud de Beca ---");
        String codigo = leerTexto("Código de la solicitud (ej: SOL-001): ");

        // Validar primero si el código ya existe
        if (gestor.buscarSolicitudPorCodigo(codigo) != null) {
            System.out.println("✗ Error: Ya existe una solicitud con el código " + codigo);
            return;
        }

        String documento = leerTexto("Documento del estudiante: ");

        Estudiante est = gestor.buscarEstudiantePorDocumento(documento);
        if (est == null) {
            System.out.println("El estudiante no existe. Vamos a registrarlo ahora.");
            String nombre = leerTexto("Nombre: ");
            String apellido = leerTexto("Apellido: ");
            String programa = leerTexto("Programa: ");
            double promedio = leerDouble("Promedio: ");
            est = new Estudiante(documento, nombre, apellido, programa, promedio);
        }

        String tipoBeca = leerTexto("Tipo de beca: ");
        double monto = leerDouble("Monto solicitado: ");
        int prioridad = leerEntero("Prioridad (1 = más alta, 5 = más baja): ");
        String estado = "PENDIENTE";

        SolicitudBeca sol = new SolicitudBeca(codigo, est, tipoBeca, monto, prioridad, estado);
        if (gestor.registrarSolicitud(sol)) {
            System.out.println("✓ Solicitud registrada correctamente.");
        } else {
            System.out.println("✗ Error: No se pudo registrar la solicitud.");
        }
    }

    private static void consultarTodasSolicitudes() {
        List<SolicitudBeca> lista = gestor.obtenerTodasLasSolicitudes();
        if (lista.isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
            return;
        }
        System.out.println("\n--- Todas las solicitudes ---");
        for (SolicitudBeca s : lista) {
            System.out.println(s);
        }
    }

    private static void buscarSolicitudPorCodigo() {
        String codigo = leerTexto("Ingrese el código de la solicitud: ");
        SolicitudBeca sol = gestor.buscarSolicitudPorCodigo(codigo);
        if (sol != null) {
            System.out.println("Solicitud encontrada:\n" + sol);
        } else {
            System.out.println("No se encontró ninguna solicitud con ese código.");
        }
    }

    private static void buscarEstudiantePorDocumento() {
        String doc = leerTexto("Ingrese el documento: ");
        Estudiante est = gestor.buscarEstudiantePorDocumento(doc);
        if (est != null) {
            System.out.println("Estudiante encontrado:\n" + est);
        } else {
            System.out.println("No se encontró el estudiante.");
        }
    }

    private static void mostrarTiposBecaUnicos() {
        Set<String> tipos = gestor.obtenerTiposBecaUnicos();
        System.out.println("\nTipos de beca únicos (HashSet): " + tipos);
    }

    private static void mostrarTiposBecaOrdenados() {
        Set<String> tipos = gestor.obtenerTiposBecaOrdenados();
        System.out.println("\nTipos de beca ordenados (TreeSet): " + tipos);
    }

    private static void verSiguientePrioritaria() {
        SolicitudBeca sol = gestor.verSiguientePrioritaria();
        if (sol != null) {
            System.out.println("Siguiente solicitud prioritaria (sin procesar):\n" + sol);
        } else {
            System.out.println("No hay solicitudes pendientes en la cola de prioridad.");
        }
    }

    private static void procesarSiguientePrioritaria() {
        SolicitudBeca sol = gestor.procesarSiguientePrioritaria();
        if (sol != null) {
            System.out.println("Se procesó la solicitud:\n" + sol);
        } else {
            System.out.println("No hay solicitudes pendientes para procesar.");
        }
    }

    private static void actualizarEstado() {
        String codigo = leerTexto("Código de la solicitud: ");
        String nuevoEstado = leerTexto("Nuevo estado (PENDIENTE, EN_REVISION, APROBADA, RECHAZADA, CANCELADA): ");
        if (gestor.actualizarEstado(codigo, nuevoEstado)) {
            System.out.println("✓ Estado actualizado correctamente.");
        } else {
            System.out.println("✗ No se encontró la solicitud.");
        }
    }

    private static void actualizarPrioridad() {
        String codigo = leerTexto("Código de la solicitud: ");
        int nuevaPrioridad = leerEntero("Nueva prioridad (1 = más alta): ");
        if (gestor.actualizarPrioridad(codigo, nuevaPrioridad)) {
            System.out.println("✓ Prioridad actualizada correctamente.");
        } else {
            System.out.println("✗ No se encontró la solicitud.");
        }
    }

    private static void eliminarSolicitud() {
        String codigo = leerTexto("Código de la solicitud a eliminar: ");
        if (gestor.eliminarSolicitud(codigo)) {
            System.out.println("✓ Solicitud eliminada correctamente.");
        } else {
            System.out.println("✗ No se encontró la solicitud.");
        }
    }

    // ========== UTILIDADES DE LECTURA ==========

    private static String leerTexto(String mensaje) {
        System.out.print(mensaje);
        return sc.nextLine().trim();
    }

    private static int leerEntero(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                int valor = Integer.parseInt(sc.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número entero.");
            }
        }
    }

    private static double leerDouble(String mensaje) {
        while (true) {
            try {
                System.out.print(mensaje);
                double valor = Double.parseDouble(sc.nextLine().trim());
                return valor;
            } catch (NumberFormatException e) {
                System.out.println("Error: debe ingresar un número válido.");
            }
        }
    }

    // ========== DATOS DE PRUEBA (≥ 15 solicitudes) ==========

    private static void cargarDatosPrueba() {
        // Estudiantes
        Estudiante e1 = new Estudiante("1001", "Ana", "García", "Ingeniería de Sistemas", 4.5);
        Estudiante e2 = new Estudiante("1002", "Carlos", "López", "Medicina", 4.2);
        Estudiante e3 = new Estudiante("1003", "María", "Rodríguez", "Derecho", 3.9);
        Estudiante e4 = new Estudiante("1004", "Juan", "Pérez", "Administración", 4.0);
        Estudiante e5 = new Estudiante("1005", "Laura", "Martínez", "Psicología", 4.7);
        Estudiante e6 = new Estudiante("1006", "Pedro", "Sánchez", "Ingeniería Civil", 3.8);
        Estudiante e7 = new Estudiante("1007", "Sofía", "Ramírez", "Enfermería", 4.3);
        Estudiante e8 = new Estudiante("1008", "Diego", "Torres", "Contaduría", 3.7);
        Estudiante e9 = new Estudiante("1009", "Valentina", "Flores", "Arquitectura", 4.1);
        Estudiante e10 = new Estudiante("1010", "Andrés", "Gómez", "Economía", 4.4);

        // Solicitudes (más de 15)
        gestor.registrarSolicitud(new SolicitudBeca("SOL-001", e1, "Meritocracia", 2500000, 2, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-002", e2, "Socioeconomica", 1800000, 1, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-003", e3, "Deportiva", 1500000, 3, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-004", e4, "Investigacion", 3000000, 1, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-005", e5, "Meritocracia", 2200000, 2, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-006", e6, "Socioeconomica", 1600000, 4, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-007", e7, "Excepcional", 2800000, 1, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-008", e8, "Deportiva", 1400000, 5, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-009", e9, "Meritocracia", 2000000, 3, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-010", e10, "Investigacion", 3200000, 2, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-011", e1, "Socioeconomica", 1700000, 4, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-012", e2, "Deportiva", 1300000, 3, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-013", e3, "Excepcional", 2900000, 1, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-014", e4, "Meritocracia", 2100000, 2, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-015", e5, "Investigacion", 3100000, 1, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-016", e6, "Socioeconomica", 1550000, 5, "PENDIENTE"));
        gestor.registrarSolicitud(new SolicitudBeca("SOL-017", e7, "Deportiva", 1450000, 4, "PENDIENTE"));

        System.out.println("✓ Datos de prueba cargados correctamente (17 solicitudes).");
    }
}