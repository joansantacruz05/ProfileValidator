package com.example.laboratorio1

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.laboratorio1.ui.screens.ResumenScreen
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.ui.theme.EcoTripTheme
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
            esViajeGrupal = false
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
        composeTestRule.onNodeWithContentDescription("Planear otro viaje").assertExists()
    }
}
