package com.example.laboratorio1.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.laboratorio1.data.local.DataStoreManager
import com.example.laboratorio1.navigation.ResumenRoute
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
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

        // Pausa de escritura tras la última tecla antes de guardar el nombre en disco
        private const val ESPERA_GUARDADO_NOMBRE_MS = 500L

        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = checkNotNull(this[APPLICATION_KEY])
                EcoTripViewModel(
                    dataStoreManager = DataStoreManager(application),
                    savedStateHandle = createSavedStateHandle()
                )
            }
        }
    }

    // Persistencia de Proceso: Enlace con SavedStateHandle
    val destinoFlow: StateFlow<String> = savedStateHandle.getStateFlow(
        KEY_DESTINO,
        EcoTripUiState.DESTINO_INICIAL
    )

    val diasDuracionInputFlow: StateFlow<String> = savedStateHandle.getStateFlow(
        KEY_DIAS_DURACION,
        EcoTripUiState.DIAS_INICIAL.toString()
    )

    val esViajeGrupalFlow: StateFlow<Boolean> = savedStateHandle.getStateFlow(
        KEY_ES_VIAJE_GRUPAL,
        false
    )

    // Nombre que el usuario está tecleando; null mientras no haya editado (se usa el de disco)
    private val _nombreEnEdicion = MutableStateFlow<String?>(null)
    private var guardadoNombreJob: Job? = null

    // Estado efímero para mensajes de notificación en UI
    private val _mensajeSnackbar = MutableStateFlow<String?>(null)

    // El TextField lee el nombre de memoria, así el cursor no depende de la escritura en disco
    private val preferenciasFlow = combine(
        dataStoreManager.preferenciasViajeFlow,
        _nombreEnEdicion
    ) { prefs, nombreEnEdicion ->
        nombreEnEdicion?.let { prefs.copy(nombreViajero = it) } ?: prefs
    }

    /**
     * Estado consolidado y reactivo de la UI (Single Source of Truth).
     * Combina en tiempo real el flujo de DataStore (disco), SavedStateHandle (proceso)
     * y los mensajes efímeros de memoria.
     */
    val uiState: StateFlow<EcoTripUiState> = combine(
        preferenciasFlow,
        destinoFlow,
        diasDuracionInputFlow,
        esViajeGrupalFlow,
        _mensajeSnackbar
    ) { prefs, destino, diasInput, esGrupal, snackbarMsg ->

        // Desinfección numérica en tiempo real con toIntOrNull() y operador Elvis (?:)
        val diasParsed = diasInput.toIntOrNull()
        val errorDuracion = when {
            diasInput.isBlank() -> "Escribe cuántos días dura tu viaje."
            diasParsed == null -> "Usa solo números enteros, por ejemplo 4."
            diasParsed <= 0 -> "El viaje debe durar al menos 1 día."
            diasParsed > 60 -> "El viaje puede durar como máximo 60 días."
            else -> null
        }

        val errorNombre = if (prefs.nombreViajero.isBlank()) {
            "Escribe tu nombre."
        } else {
            null
        }

        val errorDestino = if (destino.isBlank()) {
            "Escribe a dónde vas."
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
            errorNombre = errorNombre,
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
     * Actualiza el nombre en memoria al instante y lo guarda en DataStore (Disco)
     * cuando el usuario deja de escribir, en lugar de escribir en disco con cada tecla.
     */
    fun onNombreChanged(nuevoNombre: String) {
        _nombreEnEdicion.value = nuevoNombre
        guardadoNombreJob?.cancel()
        guardadoNombreJob = viewModelScope.launch {
            delay(ESPERA_GUARDADO_NOMBRE_MS)
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
     * Guarda el texto de duración tal cual lo escribe el usuario en el SavedStateHandle (Proceso).
     * La validación con toIntOrNull() ocurre en uiState, que muestra el error correspondiente.
     */
    fun onDiasDuracionChanged(nuevoInput: String) {
        savedStateHandle[KEY_DIAS_DURACION] = nuevoInput
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
            _mensajeSnackbar.value = "Revisa los campos con error antes de continuar."
            return null
        }

        guardarNombrePendiente()

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

    // Al confirmar la ruta no se espera la pausa: el nombre se escribe en disco de inmediato
    private fun guardarNombrePendiente() {
        val nombre = _nombreEnEdicion.value ?: return
        guardadoNombreJob?.cancel()
        guardadoNombreJob = viewModelScope.launch {
            dataStoreManager.actualizarNombre(nombre)
        }
    }
}
