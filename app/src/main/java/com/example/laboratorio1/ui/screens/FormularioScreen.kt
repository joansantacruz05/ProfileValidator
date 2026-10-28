package com.example.laboratorio1.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.viewmodel.EcoTripUiState
import kotlinx.coroutines.launch

/**
 * Pantalla 1: Formulario ultra-resiliente y de configuración global de EcoTrip 2026.
 *
 * Implementa un andamiaje estructural complejo mediante Scaffold, TopAppBar y ExtendedFloatingActionButton.
 * Su diseño se basa 100% en Material Design 3 con roles cromáticos semánticos adaptativos.
 *
 * Sometida a resiliencia multinivel:
 * - Persistencia en Disco (DataStore): Nombre y switch de huella de carbono.
 * - Persistencia de Proceso (SavedStateHandle): Destino y duración frente a rotación y muerte de proceso.
 * - Persistencia en Memoria (ViewModel): Validación y elevación de estado (State Hoisting).
 */
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun FormularioScreen(
    uiState: EcoTripUiState,
    onNombreChange: (String) -> Unit,
    onTransporteSelect: (String) -> Unit,
    onSoloBajaHuellaChange: (Boolean) -> Unit,
    onDestinoChange: (String) -> Unit,
    onDiasDuracionChange: (String) -> Unit,
    onViajeGrupalChange: (Boolean) -> Unit,
    onValidarYNavegar: () -> ResumenRoute?,
    onRutaGenerada: (ResumenRoute) -> Unit,
    onConsumirSnackbar: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()
    val snackbarHostState = remember { SnackbarHostState() }
    val coroutineScope = rememberCoroutineScope()

    // Gestión reactiva del Snackbar
    LaunchedEffect(uiState.mensajeSnackbar) {
        uiState.mensajeSnackbar?.let { mensaje ->
            snackbarHostState.showSnackbar(mensaje)
            onConsumirSnackbar()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "EcoTrip 2026",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "Planificación Resiliente de Rutas",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.EnergySavingsLeaf,
                        contentDescription = "Logo EcoTrip",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier
                            .padding(horizontal = 12.dp)
                            .size(32.dp)
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = {
                    val ruta = onValidarYNavegar()
                    if (ruta != null) {
                        onRutaGenerada(ruta)
                    } else {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Complete todos los campos obligatorios correctamente.")
                        }
                    }
                },
                icon = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Confirmar Ruta"
                    )
                },
                text = {
                    Text(
                        text = "Confirmar Ruta",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(scrollState)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // =========================================================================
            // TARJETA DE BIENVENIDA Y CONTEXTO
            // =========================================================================
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.EnergySavingsLeaf,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(40.dp)
                    )
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = "Turismo Ecológico Sostenible 2026",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Configure su itinerario protegido con resiliencia multinivel (DataStore + SavedStateHandle).",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 1: PERSISTENCIA EN DISCO (DATASTORE)
            // =========================================================================
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Preferencias Globales (Persistencia en Disco - DataStore)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Estos parámetros perduran permanentemente aunque la aplicación se cierre por completo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Campo Nombre del Viajero
                    OutlinedTextField(
                        value = uiState.nombreViajero,
                        onValueChange = onNombreChange,
                        label = { Text("Nombre del Viajero") },
                        placeholder = { Text("Ej. Sebastián Santacruz") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selector de Transporte Ecológico
                    Text(
                        text = "Medio de transporte ecológico predilecto:",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        PreferenciaViaje.LISTA_TRANSPORTES.forEach { transporte ->
                            val seleccionado = uiState.tipoTransporte == transporte
                            val icono = when (transporte) {
                                PreferenciaViaje.TRANSPORTE_TREN -> Icons.Default.DirectionsTransit
                                PreferenciaViaje.TRANSPORTE_BICICLETA -> Icons.AutoMirrored.Filled.DirectionsBike
                                else -> Icons.Default.ElectricCar
                            }

                            FilterChip(
                                selected = seleccionado,
                                onClick = { onTransporteSelect(transporte) },
                                label = { Text(transporte) },
                                leadingIcon = {
                                    Icon(
                                        imageVector = if (seleccionado) Icons.Default.Check else icono,
                                        contentDescription = transporte,
                                        modifier = Modifier.size(18.dp)
                                    )
                                },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer,
                                    selectedLeadingIconColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }

                    HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp))

                    // Switch Huella de Carbono
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Solo rutas de baja huella de carbono",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Prioriza trayectos con emisiones netas mínimas de CO2.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.soloBajaHuella,
                            onCheckedChange = onSoloBajaHuellaChange,
                            thumbContent = if (uiState.soloBajaHuella) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                }
                            } else null
                        )
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 2: PERSISTENCIA DE PROCESO (SAVEDSTATEHANDLE)
            // =========================================================================
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.tertiary
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Configuración de Ruta (SavedStateHandle - Proceso)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    Text(
                        text = "Datos temporales blindados contra rotación de pantalla y muerte de proceso (Process Death).",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Destino
                    OutlinedTextField(
                        value = uiState.destino,
                        onValueChange = onDestinoChange,
                        label = { Text("Destino Ecológico") },
                        placeholder = { Text("Ej. Reserva Ecológica Mindo Nambillo") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        isError = uiState.errorDestino != null,
                        supportingText = {
                            uiState.errorDestino?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Días de Duración (Desinfección en tiempo real con toIntOrNull)
                    OutlinedTextField(
                        value = uiState.diasDuracionInput,
                        onValueChange = onDiasDuracionChange,
                        label = { Text("Duración Estimada (días)") },
                        placeholder = { Text("Ej. 4") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.CalendarToday,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary
                            )
                        },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = uiState.errorDuracion != null,
                        supportingText = {
                            if (uiState.errorDuracion != null) {
                                Text(
                                    text = uiState.errorDuracion,
                                    color = MaterialTheme.colorScheme.error
                                )
                            } else {
                                Text(
                                    text = "Valor validado: ${uiState.diasDuracionValido} día(s)",
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Viaje Grupal Switch
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = "Expedición Grupal Colectiva",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Activar si viaja un grupo de 4 o más aventureros.",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = uiState.esViajeGrupal,
                            onCheckedChange = onViajeGrupalChange,
                            thumbContent = if (uiState.esViajeGrupal) {
                                {
                                    Icon(
                                        imageVector = Icons.Default.Groups,
                                        contentDescription = null,
                                        modifier = Modifier.size(SwitchDefaults.IconSize)
                                    )
                                }
                            } else null
                        )
                    }
                }
            }

            // =========================================================================
            // SECCIÓN 3: MONITOR DE RESILIENCIA MULTINIVEL (AUDITORÍA VISUAL)
            // =========================================================================
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Auditoría de Resiliencia Multinivel Activa",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "• Nivel Disco: DataStore sincroniza nombre y huella de carbono asíncronamente.\n" +
                               "• Nivel Proceso: SavedStateHandle garantiza supervivencia ante 'Don't keep activities'.\n" +
                               "• Nivel Memoria: EcoTripViewModel gestiona UDF y State Hoisting reactivo.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Espaciador inferior para evitar que el Extended FAB oculte información al final del scroll
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
