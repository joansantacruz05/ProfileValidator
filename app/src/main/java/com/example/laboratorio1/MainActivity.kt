package com.example.laboratorio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.laboratorio1.ui.navigation.EcoTripNavHost
import com.example.laboratorio1.ui.theme.EcoTripTheme

/**
 * Actividad Principal de EcoTrip 2026.
 *
 * Configura:
 * - Entorno de ejecución adaptado a Android 16 (API 36) y Java 21 / JBR 21.
 * - Modo Edge-to-Edge nativo para visualización completa de superficies y barras del sistema.
 * - Interfaz 100% declarativa en Jetpack Compose con Color Dinámico (Material You).
 *
 * El EcoTripViewModel no se crea aquí: lo crea el NavHost ligado a la entrada del formulario
 * en el Back Stack, con DataStore (Disco) y SavedStateHandle (Proceso) desde su Factory.
 */
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            // dynamicColor = true por defecto en Android 12+ (API 31+ / API 36)
            EcoTripTheme(dynamicColor = true) {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    EcoTripNavHost()
                }
            }
        }
    }
}
