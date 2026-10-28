package com.example.laboratorio1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.laboratorio1.data.local.DataStoreManager
import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.navigation.ResumenRoute
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

/**
 * ViewModel Unificado de EcoTrip 2026.
 * Actúa como la única fuente de verdad (Single Source of Truth) para la capa de presentación,
 * orquestando el patrón de diseño de Flujo Unidireccional de Datos (UDF).
 *
 * Articula la persistencia multinivel de la aplicación:
 * - Persistencia en Disco: Jetpack DataStore para preferencias globales permanentes.
 * - Persistencia de Proceso: SavedStateHandle para mitigar la muerte súbita del proceso (Process Death).
 * - Persistencia en Memoria: StateFlow reactivo consumible por las vistas de Jetpack Compose.
 */
class EcoTripViewModel(
    private val dataStoreManager: DataStoreManager,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    companion object {
        private const val KEY_DESTINO = "saved_key_destino"
        private const val KEY_DIAS_DURACION = "saved_key_dias_duracion"
        private const val KEY_ES_VIAJE_GRUPAL = "saved_key_es_viaje_grupal"

        private const val DEFAULT_DESTINO = "Reserva Ecológica Mindo Nambillo"
        private const val DEFAULT_DIAS = "4"
    }

    // Persistencia de Proceso: Enlace con SavedStateHandle
    val destinoFlow: StateFlow<String> = savedStateHandle.getStateFlow(
        KEY_DESTINO,
        DEFAULT_DESTINO
    )

    val diasDuracionInputFlow: StateFlow<String> = savedStateHandle.getStateFlow(
        KEY_DIAS_DURACION,
        DEFAULT_DIAS
    )

    val esViajeGrupalFlow: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        KEY_ES_VIAJE_GRUPAL,
        false
    )

    // Estado efímero para mensajes de notificación en UI
    private val _mensajeSnackbar = MutableStateFlow<String?>(null)

    /**
     * Estado consolidado y reactivo de la UI (Single Source of Truth).
     * Combina en tiempo real el flujo de DataStore (disco), SavedStateHandle (proceso)
     * y los mensajes efímeros de memoria.
     */
    val uiState: StateFlow<EcoTripUiState> = combine(
        dataStoreManager.preferenciasViajeFlow,
        destinoFlow,
        diasDuracionInputFlow,
        esViajeGrupalFlow,
        _mensajeSnackbar
    ) { prefs, destino, diasInput, esGrupal, snackbarMsg ->

        // Desinfección numérica en tiempo real con toIntOrNull() y operador Elvis (?:)
        val diasParsed = diasInput.toIntOrNull()
        val errorDuracion = when {
            diasInput.isBlank() -> "La duración no puede estar vacía."
            diasParsed == null -> "Ingrese únicamente valores numéricos enteros."
            diasParsed <= 0 -> "La duración debe ser mayor a 0 días."
            diasParsed > 60 -> "El límite máximo para una expedición ecológica es de 60 días."
            else -> null
        }

        val errorDestino = if (destino.isBlank()) {
            "El destino ecológico es obligatorio."
        } else {
            null
        }

        EcoTripUiState(
            nombreViajero = prefs.nombreViajero,
            tipoTransporte = prefs.tipoTransporte,
            soloBajaHuella = prefs.soloBajaHuella,
            destino = destino,
            diasDuracionInput = diasInput,
            diasDuracionValido = diasParsed ?: 1,
            esViajeGrupal = esGrupal,
            errorDestino = errorDestino,
            errorDuracion = errorDuracion,
            mensajeSnackbar = snackbarMsg
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = EcoTripUiState()
    )

    // =========================================================================
    // EVENTOS UDF (Los eventos suben desde la UI hacia el ViewModel)
    // =========================================================================

    /**
     * Actualiza el nombre del viajero y lo persiste asíncronamente en DataStore (Disco).
     */
    fun onNombreChanged(nuevoNombre: String) {
        viewModelScope.launch {
            dataStoreManager.actualizarNombre(nuevoNombre)
        }
    }

    /**
     * Modifica el transporte ecológico y lo resguarda en DataStore (Disco).
     */
    fun onTransporteSelected(nuevoTransporte: String) {
        viewModelScope.launch {
            dataStoreManager.actualizarTipoTransporte(nuevoTransporte)
        }
    }

    /**
     * Alterna la preferencia estricta de baja huella de carbono en DataStore (Disco).
     */
    fun onSoloBajaHuellaChanged(soloBajaHuella: Boolean) {
        viewModelScope.launch {
            dataStoreManager.actualizarSoloBajaHuella(soloBajaHuella)
        }
    }

    /**
     * Actualiza el destino en el contenedor SavedStateHandle (Proceso).
     * Blindado ante Process Death por falta de memoria del SO.
     */
    fun onDestinoChanged(nuevoDestino: String) {
        savedStateHandle[KEY_DESTINO] = nuevoDestino
    }

    /**
     * Actualiza el texto de duración en el SavedStateHandle (Proceso).
     */
    fun onDiasDuracionChanged(nuevoInput: String) {
        // Filtrado preventivo para admitir sólo dígitos
        val filtrado = nuevoInput.filter { it.isDigit() }
        savedStateHandle[KEY_DIAS_DURACION] = filtrado
    }

    /**
     * Conmuta la bandera de viaje grupal en el SavedStateHandle (Proceso).
     */
    fun onViajeGrupalChanged(esGrupal: Boolean) {
        savedStateHandle[KEY_ES_VIAJE_GRUPAL] = esGrupal
    }

    /**
     * Valida de manera segura las entradas mediante el Mindset Kotlin Moderno (?., ?:, toIntOrNull).
     * Si las condiciones son óptimas, devuelve la ruta serializable fuertemente tipada (ResumenRoute).
     */
    fun validarYGenerarRuta(): ResumenRoute? {
        val currentState = uiState.value

        if (!currentState.esFormularioValido) {
            _mensajeSnackbar.value = "Por favor, complete todos los campos requeridos con datos válidos."
            return null
        }

        // Construcción segura libre de nulos e inyección de tipos directos
        return ResumenRoute(
            nombreViajero = currentState.nombreViajero.trim(),
            tipoTransporte = currentState.tipoTransporte,
            soloBajaHuella = currentState.soloBajaHuella,
            destino = currentState.destino.trim(),
            diasDuracion = currentState.diasDuracionValido,
            esViajeGrupal = currentState.esViajeGrupal
        )
    }

    /**
     * Consume el mensaje transitorio del Snackbar para evitar re-emisiones no deseadas.
     */
    fun limpiarMensajeSnackbar() {
        _mensajeSnackbar.value = null
    }

    /**
     * Factoría personalizada para inyectar dependencias (DataStoreManager y SavedStateHandle).
     */
    class Factory(
        private val dataStoreManager: DataStoreManager,
        private val savedStateHandle: SavedStateHandle
    ) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(EcoTripViewModel::class.java)) {
                return EcoTripViewModel(dataStoreManager, savedStateHandle) as T
            }
            throw IllegalArgumentException("Clase de ViewModel desconocida: ${modelClass.name}")
        }
    }
}
