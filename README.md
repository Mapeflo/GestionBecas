# Sistema de Gestión de Solicitudes de Becas

## Actividad
Estructuras de Datos - Unidad III  
**Diseño de un sistema de gestión utilizando Java Collections Framework**

## Descripción del problema
Aplicación de consola que permite gestionar estudiantes y solicitudes de becas.  
El sistema controla la unicidad de códigos y tipos de beca, permite búsquedas rápidas por identificadores, mantiene información ordenada y procesa las solicitudes según un criterio de prioridad.

## Versión de Java
Java 24 (Oracle OpenJDK 24)

## Cómo compilar y ejecutar

### Desde IntelliJ IDEA
1. Abrir el proyecto
2. Ejecutar la clase `sistemabecas.Main`

### Desde terminal
```bash
javac -d out src/sistemabecas/modelo/*.java src/sistemabecas/servicio/*.java src/sistemabecas/Main.java
java -cp out sistemabecas.Main