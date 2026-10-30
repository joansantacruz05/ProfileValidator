package com.example.laboratorio1

import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.domain.ImpactoAmbiental
import com.example.laboratorio1.navigation.FormularioRoute
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.viewmodel.EcoTripUiState
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * Suite de Pruebas Unitarias para EcoTrip 2026.
 * Valida:
 * 1. Inmutabilidad y valores por defecto del modelo PreferenciaViaje.
 * 2. Type-Safety y serialización JSON de las rutas de navegación declarativa.
 * 3. Desinfección numérica en tiempo real mediante toIntOrNull() y operador Elvis (?:).
 * 4. Lógica de blindaje y validación reactiva del estado EcoTripUiState.
 * 5. Fórmulas de impacto ambiental (CO2 mitigado, árboles y EcoPuntos).
 */
class EcoTripValidationTest {

    @Test
    fun preferenciaViaje_inmutabilidadYTransportesValidos() {
        val pref = PreferenciaViaje(
            nombreViajero = "Viajero EcoTrip",
            tipoTransporte = PreferenciaViaje.TRANSPORTE_BICICLETA,
            soloBajaHuella = true
        )

        assertEquals("Viajero EcoTrip", pref.nombreViajero)
        assertEquals("Bicicleta", pref.tipoTransporte)
        assertTrue(pref.soloBajaHuella)
        assertTrue(PreferenciaViaje.LISTA_TRANSPORTES.contains(pref.tipoTransporte))
    }

    @Test
    fun rutasTipadas_serializacionYTypeSafetyExitosa() {
        val rutaOriginal = ResumenRoute(
            nombreViajero = "Viajero EcoTrip",
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
            nombreViajero = "Viajero EcoTrip",
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
            errorDestino = "Escribe a dónde vas."
        )
        assertFalse(estadoSinDestino.esFormularioValido)

        // Escenario C: Duración no válida (0 días)
        val estadoDuracionCero = estadoValido.copy(
            diasDuracionValido = 0,
            errorDuracion = "El viaje debe durar al menos 1 día."
        )
        assertFalse(estadoDuracionCero.esFormularioValido)

        // Escenario D: Nombre del viajero vacío
        val estadoSinNombre = estadoValido.copy(
            nombreViajero = "",
            errorNombre = "Escribe tu nombre."
        )
        assertFalse(estadoSinNombre.esFormularioValido)
    }

    @Test
    fun uiState_valoresInicialesCoherentesConSavedStateHandle() {
        val inicial = EcoTripUiState()

        assertEquals(EcoTripUiState.DESTINO_INICIAL, inicial.destino)
        assertEquals(EcoTripUiState.DIAS_INICIAL.toString(), inicial.diasDuracionInput)
        assertEquals(EcoTripUiState.DIAS_INICIAL, inicial.diasDuracionValido)
    }

    @Test
    fun impactoAmbiental_trenIndividual() {
        val impacto = ImpactoAmbiental.calcular(
            tipoTransporte = PreferenciaViaje.TRANSPORTE_TREN,
            diasDuracion = 4,
            esViajeGrupal = false,
            soloBajaHuella = true
        )

        // (45 - 0.18 * 20) * 4 = 165.6 kg
        assertEquals(165.6, impacto.co2MitigadoKg, 0.001)
        assertEquals(12, impacto.arbolesEquivalentes)
        assertEquals(600, impacto.ecoPuntos)
    }

    @Test
    fun impactoAmbiental_bicicletaGrupalSinFiltroDeHuella() {
        val impacto = ImpactoAmbiental.calcular(
            tipoTransporte = PreferenciaViaje.TRANSPORTE_BICICLETA,
            diasDuracion = 2,
            esViajeGrupal = true,
            soloBajaHuella = false
        )

        // 45 * 2 * 1.8 = 162.0 kg
        assertEquals(162.0, impacto.co2MitigadoKg, 0.001)
        assertEquals(2, impacto.arbolesEquivalentes)
        assertEquals(600, impacto.ecoPuntos)
    }

    @Test
    fun impactoAmbiental_transporteDesconocidoUsaFactorPorDefecto() {
        val desconocido = ImpactoAmbiental.calcular("Patineta", 1, false, false)
        val electrico = ImpactoAmbiental.calcular(PreferenciaViaje.TRANSPORTE_ELECTRICO, 1, false, false)

        assertEquals(electrico, desconocido)
        assertEquals(40.0, desconocido.co2MitigadoKg, 0.001)
    }
}
