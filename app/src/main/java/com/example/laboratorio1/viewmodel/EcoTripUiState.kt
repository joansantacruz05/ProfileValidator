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
    val destino: String = "",
    val diasDuracionInput: String = "3",
    val diasDuracionValido: Int = 3,
    val esViajeGrupal: Boolean = false,

    // Validaciones reactivas y control de errores
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
                errorDestino == null &&
                errorDuracion == null
}
