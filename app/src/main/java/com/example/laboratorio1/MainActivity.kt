package com.example.laboratorio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.laboratorio1.ui.navigation.EcoTripNavHost
import com.example.laboratorio1.ui.theme.EcoTripTheme
import com.example.laboratorio1.viewmodel.EcoTripViewModel

/**
 * Actividad Principal de EcoTrip 2026.
 *
 * Configura:
 * - Entorno de ejecución adaptado a Android 16 (API 36) y Java 21 / JBR 21.
 * - Modo Edge-to-Edge nativo para visualización completa de superficies y barras del sistema.
 * - ViewModel creado con EcoTripViewModel.Factory, que le entrega:
 *   - Jetpack DataStore Preferences (Disco)
 *   - SavedStateHandle (Proceso / Process Death) mediante createSavedStateHandle().
 * - Interfaz 100% declarativa en Jetpack Compose con Color Dinámico (Material You).
 */
class MainActivity : ComponentActivity() {

    private val viewModel: EcoTripViewModel by viewModels { EcoTripViewModel.Factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // dynamicColor = true por defecto en Android 12+ (API 31+ / API 36)
            EcoTripTheme(dynamicColor = true) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EcoTripNavHost(viewModel = viewModel)
                }
            }
        }
    }
}
