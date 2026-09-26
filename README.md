# Examen RDA-1: SafePass 2026

Este es el repositorio para el examen de Programación Móvil.

## Progreso del equipo

**Miembro 1 (Completado en el último commit):**
- Se estandarizó el entorno (`gradle.properties`) para blindar el path de Java.
- Se limpió el `MainActivity.kt` del laboratorio anterior.
- Se implementó el **Punto 1 (Modelo de Datos)** creando el `data class Asistente` con propiedades inmutables para el nombre, edad (anulable) y tipo de entrada.
- Se configuró la función base `SafePassApp()` con `enableEdgeToEdge()`.

## Tareas Pendientes para el siguiente miembro:
1. **Gestión de Estados**: Crear la `sealed class RegistroState` (Idle, Success, Error).
2. **Interfaz de Usuario (Parte 1)**: Agregar los campos de texto (`OutlinedTextField`) para pedir el nombre, edad y tipo de entrada.
3. Asegurarse de hacer el respectivo commit (ej. `feat: ui state e inputs`).
