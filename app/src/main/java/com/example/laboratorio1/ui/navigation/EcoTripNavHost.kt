package com.example.laboratorio1.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
 * 3. Higiene impecable de la pila de transiciones (Back Stack) mediante `popUpTo` con `launchSingleTop`
 *    para destruir instancias redundantes al regresar a la pantalla de inicio.
 */
@Composable
fun EcoTripNavHost(
    viewModel: EcoTripViewModel,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    NavHost(
        navController = navController,
        startDestination = FormularioRoute,
        modifier = modifier
    ) {
        // Pantalla 1: Formulario de Planificación Resiliente
        composable<FormularioRoute> {
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

            ResumenScreen(
                route = route,
                onVolverAInicio = {
                    // Higiene de la pila: destruye el historial previo y evita duplicados con launchSingleTop
                    navController.navigate(FormularioRoute) {
                        popUpTo<FormularioRoute> {
                            inclusive = false
                        }
                        launchSingleTop = true
                    }
                }
            )
        }
    }
}
