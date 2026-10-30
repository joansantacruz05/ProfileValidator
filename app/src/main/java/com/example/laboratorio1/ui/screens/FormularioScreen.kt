package com.example.laboratorio1.ui.screens

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
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.navigation.ResumenRoute
import com.example.laboratorio1.viewmodel.EcoTripUiState

/**
 * Pantalla 1: Formulario ultra-resiliente y de configuración global de EcoTrip 2026.
 *
 * Implementa un andamiaje estructural complejo mediante Scaffold, TopAppBar y ExtendedFloatingActionButton.
 * Su diseño se basa 100% en Material Design 3 con roles cromáticos semánticos adaptativos.
 *
 * Sometida a resiliencia multinivel:
 * - Persistencia en Disco (DataStore): Nombre, transporte y switch de huella de carbono.
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

    // Único punto que muestra el Snackbar: el mensaje lo emite el ViewModel (UDF)
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
                            text = "Planifica tu viaje ecológico",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    Icon(
                        imageVector = Icons.Default.EnergySavingsLeaf,
                        contentDescription = null,
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
                    onValidarYNavegar()?.let(onRutaGenerada)
                },
                icon = {
                    // El FAB extendido de Material 3 oculta su texto a TalkBack: la etiqueta va en el ícono
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = "Ver resumen"
                    )
                },
                text = {
                    Text(
                        text = "Ver resumen",
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
                            text = "Viaja dejando menos huella",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "Completa estos datos y te mostramos cuánto CO2 te ahorras con tu viaje.",
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
                            text = "Tus preferencias",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Las guardamos para la próxima vez que abras la app.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    // Campo Nombre del Viajero
                    OutlinedTextField(
                        value = uiState.nombreViajero,
                        onValueChange = onNombreChange,
                        label = { Text("Tu nombre") },
                        placeholder = { Text("Nombre y apellido") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Person,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                        },
                        isError = uiState.errorNombre != null,
                        supportingText = {
                            uiState.errorNombre?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error
                                )
                            }
                        },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Words,
                            imeAction = ImeAction.Next
                        ),
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )

                    // Selector de Transporte Ecológico
                    Text(
                        text = "¿Cómo prefieres moverte?",
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
                                        contentDescription = null,
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
                                text = "Solo rutas que contaminen poco",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Te mostramos los trayectos que menos CO2 generan.",
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
                            text = "Tu viaje",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }

                    // Destino
                    OutlinedTextField(
                        value = uiState.destino,
                        onValueChange = onDestinoChange,
                        label = { Text("¿A dónde vas?") },
                        placeholder = { Text("Ej.: Mindo") },
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
                        label = { Text("¿Cuántos días?") },
                        placeholder = { Text("Ej.: 4") },
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
                            uiState.errorDuracion?.let { error ->
                                Text(
                                    text = error,
                                    color = MaterialTheme.colorScheme.error
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
                                text = "Viajo en grupo",
                                style = MaterialTheme.typography.bodyMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Text(
                                text = "Actívalo si van 4 personas o más.",
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

            // Espaciador inferior para evitar que el Extended FAB oculte información al final del scroll
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}
