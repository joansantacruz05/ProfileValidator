package com.example.laboratorio1

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.laboratorio1.ui.screens.FormularioScreen
import com.example.laboratorio1.ui.screens.ResumenScreen
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.ui.theme.EcoTripTheme
import com.example.laboratorio1.viewmodel.EcoTripUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Prueba de instrumentación en Jetpack Compose para EcoTrip 2026.
 */
@RunWith(AndroidJUnit4::class)
class EcoTripComposeTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun resumenScreen_muestraDestinoYDatosFormateados() {
        val testRoute = ResumenRoute(
            nombreViajero = "Viajero EcoTrip",
            tipoTransporte = "Tren",
            soloBajaHuella = true,
            destino = "Parque Nacional Cotopaxi",
            diasDuracion = 4,
            esViajeGrupal = false,
            ticketCode = "ECO-2026-XYZ"
        )

        composeTestRule.setContent {
            EcoTripTheme {
                ResumenScreen(
                    route = testRoute,
                    onVolverAInicio = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Parque Nacional Cotopaxi").assertExists()
        composeTestRule.onNodeWithText("A nombre de Viajero EcoTrip").assertExists()
        composeTestRule.onNodeWithText("Duración estimada: 4 días").assertExists()
        composeTestRule.onNodeWithText("Código de Reserva: ECO-2026-XYZ").assertExists()
        composeTestRule.onNodeWithContentDescription("Planear otro viaje").assertExists()
    }

    @Test
    fun formularioScreen_botonPlanificarReaccionaAlNombre() {
        var uiState by mutableStateOf(
            EcoTripUiState(nombreViajero = "Ana", destino = "Parque Nacional Cotopaxi")
        )

        composeTestRule.setContent {
            EcoTripTheme {
                FormularioScreen(
                    uiState = uiState,
                    onNombreChange = {},
                    onTransporteSelect = {},
                    onSoloBajaHuellaChange = {},
                    onDestinoChange = {},
                    onDiasDuracionChange = {},
                    onViajeGrupalChange = {},
                    onValidarYNavegar = { null },
                    onRutaGenerada = {},
                    onConsumirSnackbar = {}
                )
            }
        }

        composeTestRule.onNodeWithText("Planificar Ruta").assertIsNotEnabled()

        uiState = uiState.copy(nombreViajero = "Anas")

        composeTestRule.onNodeWithText("Planificar Ruta").assertIsEnabled()
    }
}
