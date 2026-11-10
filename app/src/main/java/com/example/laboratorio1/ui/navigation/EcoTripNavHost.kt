package com.example.laboratorio1.ui.navigation

import androidx.activity.compose.BackHandler
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.example.laboratorio1.navigation.FormularioRoute
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.ui.screens.FormularioScreen
import com.example.laboratorio1.ui.screens.ResumenScreen
import com.example.laboratorio1.viewmodel.EcoTripViewModel

/**
 * Orquestador principal del grafo de navegación de EcoTrip 2026.
 *
 * Implementa de forma estricta:
 * 1. Rutas declarativas y fuertemente tipadas (Type-Safety) basadas en clases `@Serializable`.
 * 2. Inyección directa de tipos validados en tiempo de compilación (`toRoute<ResumenRoute>()`).
 * 3. ViewModel ligado a la entrada del formulario en el Back Stack: al cancelar el itinerario,
 *    `popUpTo` inclusivo saca esa entrada de la pila y Android llama a `onCleared()`.
 */
@Composable
fun EcoTripNavHost(
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    NavHost(
        navController = navController,
        startDestination = FormularioRoute,
        modifier = modifier
    ) {
        // Pantalla 1: Formulario de Planificación Resiliente
        composable<FormularioRoute> {
            // Caso 4: el ViewModel vive mientras esta entrada siga en el Back Stack
            val viewModel: EcoTripViewModel = viewModel(factory = EcoTripViewModel.Factory)
            val uiState by viewModel.uiState.collectAsStateWithLifecycle()

            FormularioScreen(
                uiState = uiState,
                onNombreChange = viewModel::onNombreChanged,
                onTransporteSelect = viewModel::onTransporteSelected,
                onSoloBajaHuellaChange = viewModel::onSoloBajaHuellaChanged,
                onDestinoChange = viewModel::onDestinoChanged,
                onDiasDuracionChange = viewModel::onDiasDuracionChanged,
                onViajeGrupalChange = viewModel::onViajeGrupalChanged,
                onValidarYNavegar = viewModel::validarYGenerarRuta,
                onRutaGenerada = { rutaSegura ->
                    navController.navigate(rutaSegura)
                },
                onConsumirSnackbar = viewModel::limpiarMensajeSnackbar
            )
        }

        // Pantalla 2: Resumen Tipado de Ruta Confirmada
        composable<ResumenRoute> { backStackEntry ->
            // Desempaquetado tipado y seguro mediante Kotlin Serialization
            val route: ResumenRoute = backStackEntry.toRoute()

            // Cancela el itinerario: el formulario anterior sale de la pila y se crea uno limpio
            val volverAInicio = {
                navController.navigate(FormularioRoute) {
                    popUpTo<FormularioRoute> {
                        inclusive = true
                    }
                }
            }

            // El gesto de retroceso del sistema se comporta igual que la flecha de la TopAppBar
            BackHandler(onBack = volverAInicio)

            ResumenScreen(
                route = route,
                onVolverAInicio = volverAInicio
            )
        }
    }
}
