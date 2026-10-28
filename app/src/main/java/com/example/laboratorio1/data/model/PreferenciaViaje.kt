package com.example.laboratorio1.data.model

import kotlinx.serialization.Serializable

/**
 * Modelo de datos inmutable para representar las opciones fijas del usuario en EcoTrip 2026.
 * Cumple con el principio de inmutabilidad estricta (val) para evitar mutaciones colaterales
 * y garantizar la predictibilidad del flujo unidireccional de datos (UDF).
 *
 * @property nombreViajero Identificador nominal del viajero registrado.
 * @property tipoTransporte Modalidad de transporte ecológico elegida: Tren, Bicicleta o Vehículo Eléctrico.
 * @property soloBajaHuella Flag booleano que indica preferencia estricta por trayectos de mínima huella de carbono.
 */
@Serializable
data class PreferenciaViaje(
    val nombreViajero: String = "",
    val tipoTransporte: String = "Tren",
    val soloBajaHuella: Boolean = true
) {
    companion object {
        const val TRANSPORTE_TREN = "Tren"
        const val TRANSPORTE_BICICLETA = "Bicicleta"
        const val TRANSPORTE_ELECTRICO = "Vehículo Eléctrico"

        val LISTA_TRANSPORTES = listOf(
            TRANSPORTE_TREN,
            TRANSPORTE_BICICLETA,
            TRANSPORTE_ELECTRICO
        )
    }
}
