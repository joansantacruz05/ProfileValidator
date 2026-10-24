package com.example.laboratorio1

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    SmartHome()
                }
            }
        }
    }
}

@Composable
fun SmartHome() {
    // Estado Hoisting: El padre posee la verdad
    var isLightOn by remember { mutableStateOf(false) }

    // Desafío: Variable de estado independiente para demostrar Skipping
    var refreshCount by remember { mutableStateOf(0) }

    // Trazabilidad: Log para observar recomposición del Padre
    // Al leer refreshCount en este ámbito, SmartHome se recompondrá cuando cambie
    Log.d("RECOMPOSE", "DIBUJANDO PADRE (SmartHome) - Contador: $refreshCount")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Centro de Control Domótico",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Enviamos el estado AL HIJO y una lambda para recibir cambios
        LightBulb(isOn = isLightOn)

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Interruptor Principal")
            Spacer(modifier = Modifier.width(16.dp))
            Switch(
                checked = isLightOn,
                onCheckedChange = { isLightOn = it }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))

        // Desafío: Botón "Refrescar UI" que altera una variable de estado independiente
        Button(
            onClick = {
                refreshCount++
            }
        ) {
            Text("Refrescar UI ($refreshCount)")
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "UI Refrescada: $refreshCount veces",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.Gray
        )
    }
}

@Composable
fun LightBulb(isOn: Boolean) {
    // Trazabilidad: Log para observar recomposición del Hijo
    Log.d("RECOMPOSE", "DIBUJANDO HIJO (LightBulb)")
    Icon(
        imageVector = Icons.Default.Lightbulb,
        contentDescription = "Bombilla",
        modifier = Modifier.size(120.dp),
        tint = if (isOn) Color.Yellow else Color.Gray
    )
}