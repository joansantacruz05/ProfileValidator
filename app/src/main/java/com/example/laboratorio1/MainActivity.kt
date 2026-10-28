package com.example.laboratorio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.laboratorio1.data.local.DataStoreManager
import com.example.laboratorio1.ui.navigation.EcoTripNavHost
import com.example.laboratorio1.ui.theme.EcoTripTheme
import com.example.laboratorio1.viewmodel.EcoTripViewModel

/**
 * Actividad Principal de EcoTrip 2026.
 *
 * Configura:
 * - Entorno de ejecución adaptado a Android 16 (API 36) y Java 21 / JBR 21.
 * - Modo Edge-to-Edge nativo para visualización completa de superficies y barras del sistema.
 * - Inyección de dependencias para persistencia multinivel:
 *   - Jetpack DataStore Preferences (Disco)
 *   - SavedStateHandle (Proceso / Process Death) mediante AbstractSavedStateViewModelFactory.
 * - Interfaz 100% declarativa en Jetpack Compose con Color Dinámico (Material You).
 */
class MainActivity : ComponentActivity() {

    // ViewModel con enlace al SavedStateRegistry del ciclo de vida de la Actividad
    private val viewModel: EcoTripViewModel by viewModels {
        object : AbstractSavedStateViewModelFactory(this@MainActivity, null) {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                key: String,
                modelClass: Class<T>,
                handle: SavedStateHandle
            ): T {
                val dataStoreManager = DataStoreManager(applicationContext)
                return EcoTripViewModel(dataStoreManager, handle) as T
            }
        }
    }

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