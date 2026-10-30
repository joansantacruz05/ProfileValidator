package com.example.laboratorio1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.laboratorio1.data.model.PreferenciaViaje
import com.example.laboratorio1.domain.ImpactoAmbiental
import com.example.laboratorio1.navigation.ResumenRoute

/**
 * Pantalla 2: Resumen dinámico y confirmación tipada de la ruta ecológica (EcoTrip 2026).
 *
 * Demuestra:
 * 1. Consumo seguro de datos inyectados mediante Type-Safety con Kotlin Serialization.
 * 2. Formateo declarativo con plantillas de cadenas idiomáticas de Kotlin ("$variable").
 * 3. Adaptabilidad cromática nativa a Material You (Color Dinámico extraído del wallpaper).
 * 4. Limpieza higiénica del Back Stack mediante popUpTo y launchSingleTop.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ResumenScreen(
    route: ResumenRoute,
    onVolverAInicio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val impacto = remember(route) {
        ImpactoAmbiental.calcular(
            tipoTransporte = route.tipoTransporte,
            diasDuracion = route.diasDuracion,
            esViajeGrupal = route.esViajeGrupal,
            soloBajaHuella = route.soloBajaHuella
        )
    }

    // Formateo idiomático con plantillas de texto de Kotlin
    val descripcionModalidad = if (route.esViajeGrupal) {
        "En grupo (4 personas o más)"
    } else {
        "Solo o en pareja"
    }

    val descripcionRutas = if (route.soloBajaHuella) {
        "Solo las que contaminan poco"
    } else {
        "Todas las disponibles"
    }

    val textoDias = if (route.diasDuracion == 1) "1 día" else "${route.diasDuracion} días"

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
                            text = "Resumen de tu viaje",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolverAInicio) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Volver",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
            )
        },
        floatingActionButton = {
            // Retorno al formulario limpiando el Back Stack (popUpTo + launchSingleTop)
            ExtendedFloatingActionButton(
                onClick = onVolverAInicio,
                icon = {
                    // El FAB extendido de Material 3 oculta su texto a TalkBack: la etiqueta va en el ícono
                    Icon(
                        imageVector = Icons.Default.RestartAlt,
                        contentDescription = "Planear otro viaje"
                    )
                },
                text = {
                    Text(
                        text = "Planear otro viaje",
                        fontWeight = FontWeight.SemiBold
                    )
                },
                containerColor = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
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
            // HERO CARD: DESTINO Y VIAJERO (COLOR DINÁMICO PRIMARY CONTAINER)
            // =========================================================================
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                shape = RoundedCornerShape(20.dp)
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Verified,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tu viaje está listo",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }

                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        ) {
                            Text(
                                text = textoDias,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                            )
                        }
                    }

                    Text(
                        text = route.destino,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "A nombre de ${route.nombreViajero}",
                            style = MaterialTheme.typography.bodyMedium,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // =========================================================================
            // FICHA TÉCNICA DETALLADA (PLANTILLAS DE CADENA IDIOMÁTICAS DE KOTLIN)
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
                    Text(
                        text = "Detalles del viaje",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurface
                    )

                    HorizontalDivider()

                    // Fila Transporte
                    val transporteIcon = when (route.tipoTransporte) {
                        PreferenciaViaje.TRANSPORTE_BICICLETA -> Icons.AutoMirrored.Filled.DirectionsBike
                        PreferenciaViaje.TRANSPORTE_TREN -> Icons.Default.DirectionsTransit
                        else -> Icons.Default.ElectricCar
                    }
                    FilaDetalle(
                        icono = transporteIcon,
                        titulo = "Transporte",
                        valor = route.tipoTransporte
                    )

                    // Fila Modalidad
                    FilaDetalle(
                        icono = Icons.Default.Groups,
                        titulo = "Con quién viajas",
                        valor = descripcionModalidad
                    )

                    // Fila Huella
                    FilaDetalle(
                        icono = Icons.Default.EnergySavingsLeaf,
                        titulo = "Rutas",
                        valor = descripcionRutas
                    )
                }
            }

            // =========================================================================
            // IMPACTO AMBIENTAL Y ECO-PUNTOS
            // =========================================================================
            ElevatedCard(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.elevatedCardColors(
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                )
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "El impacto de tu viaje",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ColumnaImpacto(
                            icono = Icons.Default.Co2,
                            valor = "${impacto.co2MitigadoKg} kg",
                            etiqueta = "CO2 que evitas"
                        )
                        ColumnaImpacto(
                            icono = Icons.Default.Park,
                            valor = "${impacto.arbolesEquivalentes}",
                            etiqueta = "Árboles equivalentes"
                        )
                        ColumnaImpacto(
                            icono = Icons.Default.Stars,
                            valor = "${impacto.ecoPuntos}",
                            etiqueta = "EcoPuntos"
                        )
                    }
                }
            }

            // Espaciador inferior para evitar que el Extended FAB oculte información al final del scroll
            Spacer(modifier = Modifier.height(72.dp))
        }
    }
}

@Composable
private fun FilaDetalle(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    titulo: String,
    valor: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(20.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column {
            Text(
                text = titulo,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = valor,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Composable
private fun ColumnaImpacto(
    icono: androidx.compose.ui.graphics.vector.ImageVector,
    valor: String,
    etiqueta: String
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = icono,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(28.dp)
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = valor,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.ExtraBold,
            color = MaterialTheme.colorScheme.primary
        )
        Text(
            text = etiqueta,
            style = MaterialTheme.typography.labelSmall,
            color = MaterialTheme.colorScheme.onSecondaryContainer
        )
    }
}
