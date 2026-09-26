package com.example.laboratorio1

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

// 1. Modelado de Datos (SafePass 2026)
// TODO: El siguiente miembro del equipo debe agregar la gestión de estados (RegistroState)
data class Asistente(
    val nombre: String,
    val edad: Int?,
    val tipoEntrada: String
)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        enableEdgeToEdge()
        
        setContent {
            MaterialTheme {
                SafePassApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SafePassApp() {
    // TODO: El siguiente miembro del equipo debe implementar la interfaz de usuario con Scaffold y Column
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("SafePass 2026 - En Desarrollo", style = MaterialTheme.typography.headlineMedium)
            Text("Estructura base lista. Faltan estados y UI.")
        }
    }
}