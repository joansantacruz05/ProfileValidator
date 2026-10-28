package com.example.laboratorio1

import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.navigation.FormularioRoute
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.viewmodel.EcoTripUiState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Suite de Pruebas Unitarias para EcoTrip 2026.
 * Valida:
 * 1. Inmutabilidad y valores por defecto del modelo PreferenciaViaje.
 * 2. Type-Safety y serialización JSON de las rutas de navegación declarativa.
 * 3. Desinfección numérica en tiempo real mediante toIntOrNull() y operador Elvis (?:).
 * 4. Lógica de blindaje y validación reactiva del estado EcoTripUiState.
 */
class EcoTripValidationTest {

    @Test
    fun preferenciaViaje_inmutabilidadYTransportesValidos() {
        val pref = PreferenciaViaje(
            nombreViajero = "Sebastián Santacruz",
            tipoTransporte = PreferenciaViaje.TRANSPORTE_BICICLETA,
            soloBajaHuella = true
        )

        assertEquals("Sebastián Santacruz", pref.nombreViajero)
        assertEquals("Bicicleta", pref.tipoTransporte)
        assertTrue(pref.soloBajaHuella)
        assertTrue(PreferenciaViaje.LISTA_TRANSPORTES.contains(pref.tipoTransporte))
    }

    @Test
    fun rutasTipadas_serializacionYTypeSafetyExitosa() {
        val rutaOriginal = ResumenRoute(
            nombreViajero = "Sebastián Santacruz",
            tipoTransporte = PreferenciaViaje.TRANSPORTE_TREN,
            soloBajaHuella = true,
            destino = "Reserva Ecológica Mindo Nambillo",
            diasDuracion = 4,
            esViajeGrupal = true
        )

        // Verificamos serialización estricta de Kotlin Serialization
        val jsonString = Json.encodeToString(rutaOriginal)
        val rutaDeserializada = Json.decodeFromString<ResumenRoute>(jsonString)

        assertEquals(rutaOriginal, rutaDeserializada)
        assertEquals("Reserva Ecológica Mindo Nambillo", rutaDeserializada.destino)
        assertEquals(4, rutaDeserializada.diasDuracion)
        assertTrue(rutaDeserializada.esViajeGrupal)

        // Verificamos singleton de FormularioRoute
        val formJson = Json.encodeToString(FormularioRoute)
        val formDeserializado = Json.decodeFromString<FormularioRoute>(formJson)
        assertEquals(FormularioRoute, formDeserializado)
    }

    @Test
    fun desinfeccionNumerica_manejoRobustoDeEntradasAnomalas() {
        val entradaValida = "5"
        val entradaInvalidaTexto = "cinco_dias"
        val entradaVacia = ""
        val entradaNula: String? = null

        // Uso idiomático de toIntOrNull() con Elvis
        assertEquals(5, entradaValida.toIntOrNull() ?: 1)
        assertEquals(1, entradaInvalidaTexto.toIntOrNull() ?: 1)
        assertEquals(1, entradaVacia.toIntOrNull() ?: 1)
        assertEquals(1, entradaNula?.toIntOrNull() ?: 1)
    }

    @Test
    fun uiState_validacionEstrictaDeFormulario() {
        // Escenario A: Formulario completo y válido
        val estadoValido = EcoTripUiState(
            nombreViajero = "Sebastián Santacruz",
            destino = "Parque Nacional Cotopaxi",
            diasDuracionInput = "3",
            diasDuracionValido = 3,
            errorDestino = null,
            errorDuracion = null
        )
        assertTrue(estadoValido.esFormularioValido)

        // Escenario B: Destino vacío
        val estadoSinDestino = estadoValido.copy(
            destino = "",
            errorDestino = "El destino ecológico es obligatorio."
        )
        assertFalse(estadoSinDestino.esFormularioValido)

        // Escenario C: Duración no válida (0 días)
        val estadoDuracionCero = estadoValido.copy(
            diasDuracionValido = 0,
            errorDuracion = "La duración debe ser mayor a 0 días."
        )
        assertFalse(estadoDuracionCero.esFormularioValido)
    }
}
