package com.example.laboratorio1.navigation

import kotlinx.serialization.Serializable

/**
 * Contrato de rutas fuertemente tipadas (Type-Safety) para el grafo de navegación de EcoTrip 2026.
 * Diseñadas para su compilación e inyección declarativa en Jetpack Navigation Compose 2.8+.
 * Elimina completamente los riesgos de cadenas de texto erróneas (typos) y casteo inseguro de Bundles.
 */
sealed interface EcoTripDestination

/**
 * Ruta tipada para la Pantalla 1: Formulario ultra-resiliente y configuración de viaje.
 * Implementada como singleton serializable (`data object`) al no requerir argumentos de entrada.
 */
@Serializable
data object FormularioRoute : EcoTripDestination

/**
 * Ruta tipada para la Pantalla 2: Resumen dinámico y confirmación de la ruta ecológica.
 * Exige de forma obligatoria parámetros estrictos verificados por el compilador en tiempo de compilación.
 *
 * @property nombreViajero Nombre del usuario recuperado o ingresado.
 * @property tipoTransporte Transporte ecológico seleccionado ("Tren", "Bicicleta", "Vehículo Eléctrico").
 * @property soloBajaHuella Preferencia estricta por baja huella de carbono.
 * @property destino Ciudad o reserva natural de destino configurada en el formulario.
 * @property diasDuracion Duración estimada del trayecto expresada en días enteros válidos (> 0).
 * @property esViajeGrupal Modalidad de viaje que determina impacto compartido y logística colectiva.
 */
@Serializable
data class ResumenRoute(
    val nombreViajero: String,
    val tipoTransporte: String,
    val soloBajaHuella: Boolean,
    val destino: String,
    val diasDuracion: Int,
    val esViajeGrupal: Boolean
) : EcoTripDestination
