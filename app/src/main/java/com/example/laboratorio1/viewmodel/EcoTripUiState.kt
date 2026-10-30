package com.example.laboratorio1.viewmodel

import com.example.laboratorio1.data.model.PreferenciaViaje

/**
 * Estado inmutable de la interfaz de usuario (UI State) para EcoTrip 2026.
 * Consolida la persistencia multinivel:
 * 1. Nivel Disco (DataStore): nombreViajero, tipoTransporte, soloBajaHuella
 * 2. Nivel Proceso (SavedStateHandle): destino, diasDuracionInput, esViajeGrupal
 * 3. Nivel Memoria (ViewModel): estados derivados, validaciones y feedback reactivo.
 */
data class EcoTripUiState(
    // Persistencia en Disco (DataStore)
    val nombreViajero: String = "",
    val tipoTransporte: String = PreferenciaViaje.TRANSPORTE_TREN,
    val soloBajaHuella: Boolean = true,

    // Persistencia de Proceso (SavedStateHandle)
    val destino: String = DESTINO_INICIAL,
    val diasDuracionInput: String = DIAS_INICIAL.toString(),
    val diasDuracionValido: Int = DIAS_INICIAL,
    val esViajeGrupal: Boolean = false,

    // Validaciones reactivas y control de errores
    val errorNombre: String? = null,
    val errorDestino: String? = null,
    val errorDuracion: String? = null,
    val mensajeSnackbar: String? = null
) {
    /**
     * Valida de manera segura si el formulario cuenta con los requisitos mínimos
     * para realizar una transición tipada sin anomalías.
     */
    val esFormularioValido: Boolean
        get() = nombreViajero.isNotBlank() &&
                destino.isNotBlank() &&
                diasDuracionValido > 0 &&
                errorNombre == null &&
                errorDestino == null &&
                errorDuracion == null

    companion object {
        // Única fuente de los valores iniciales: la usan tanto este estado como el SavedStateHandle
        const val DESTINO_INICIAL = "Reserva Ecológica Mindo Nambillo"
        const val DIAS_INICIAL = 4
    }
}
