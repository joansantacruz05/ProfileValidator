package com.example.laboratorio1.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.DirectionsBike
import androidx.compose.material.icons.filled.Co2
import androidx.compose.material.icons.filled.DirectionsBike
import androidx.compose.material.icons.filled.DirectionsTransit
import androidx.compose.material.icons.filled.ElectricCar
import androidx.compose.material.icons.filled.EnergySavingsLeaf
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Park
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material.icons.filled.Verified
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.laboratorio1.data.model.PreferenciaViaje
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
@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ResumenScreen(
    route: ResumenRoute,
    onVolverAInicio: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    // Estimación ecológica reactiva mediante operadores idiomáticos
    val factorTransporte = when (route.tipoTransporte) {
        PreferenciaViaje.TRANSPORTE_BICICLETA -> 0.00
        PreferenciaViaje.TRANSPORTE_TREN -> 0.18
        else -> 0.25 // Vehículo Eléctrico
    }
    val multiplicadorGrupal = if (route.esViajeGrupal) 1.8 else 1.0
    val emisionAhorradaKg = String.format(
        java.util.Locale.US,
        "%.1f",
        (45.0 - (factorTransporte * 20.0)) * route.diasDuracion * multiplicadorGrupal
    )
    val arbolesEquivalentes = (route.diasDuracion * (if (route.soloBajaHuella) 3 else 1)).coerceAtLeast(1)
    val ecoPuntos = (route.diasDuracion * 150 * (if (route.esViajeGrupal) 2 else 1))

    // Formateo idiomático con plantillas de texto de Kotlin
    val descripcionModalidad = if (route.esViajeGrupal) {
        "Expedición Colectiva Sostenible (Grupo ≥ 4 personas)"
    } else {
        "Expedición Individual / Pareja de Bajo Impacto"
    }

    val certificacionHuella = if (route.soloBajaHuella) {
        "Certificación Nivel Oro: Rutas de Cero Emisiones Netas"
    } else {
        "Estándar Verde: Compensación Forestal Activa"
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
                            text = "Resumen de Ruta Confirmada",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onVolverAInicio) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Regresar al formulario",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                actions = {
                    IconButton(onClick = onVolverAInicio) {
                        Icon(
                            imageVector = Icons.Default.RestartAlt,
                            contentDescription = "Reiniciar y Salir limpiando el Back Stack",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface,
                    titleContentColor = MaterialTheme.colorScheme.onSurface
                )
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
                                text = "Ruta Validada por Compilador (Type-Safe)",
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
                                text = "${route.diasDuracion} días",
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
                            text = "Viajero titular: ${route.nombreViajero}",
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
                        text = "Configuración Detallada del Itinerario",
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
                        titulo = "Transporte Seleccionado",
                        valor = "Movilidad mediante ${route.tipoTransporte}"
                    )

                    // Fila Modalidad
                    FilaDetalle(
                        icono = Icons.Default.Groups,
                        titulo = "Tipo de Expedición",
                        valor = descripcionModalidad
                    )

                    // Fila Huella
                    FilaDetalle(
                        icono = Icons.Default.EnergySavingsLeaf,
                        titulo = "Certificación Ecológica",
                        valor = certificacionHuella
                    )

                    // Fila Destino y Tiempo
                    FilaDetalle(
                        icono = Icons.Default.LocationOn,
                        titulo = "Segmento de Destino",
                        valor = "Tramo planificado: ${route.destino} por un periodo estricto de ${route.diasDuracion} jornadas continuas."
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
                        text = "Impacto Ambiental Proyectado EcoTrip 2026",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        ColumnaImpacto(
                            icono = Icons.Default.Co2,
                            valor = "-$emisionAhorradaKg kg",
                            etiqueta = "CO2 Mitigado"
                        )
                        ColumnaImpacto(
                            icono = Icons.Default.Park,
                            valor = "+$arbolesEquivalentes",
                            etiqueta = "Árboles Salvos"
                        )
                        ColumnaImpacto(
                            icono = Icons.Default.Stars,
                            valor = "$ecoPuntos",
                            etiqueta = "EcoPuntos 2026"
                        )
                    }
                }
            }

            // =========================================================================
            // DEMOSTRACIÓN DE IDENTIDAD VISUAL ADAPTATIVA (MATERIAL YOU)
            // =========================================================================
            OutlinedCard(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Palette,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(22.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Identidad Visual Adaptativa (Material You)",
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = "Esta pantalla se acopla dinámicamente a la paleta cromática generada por el fondo de pantalla (Wallpaper) del sistema mediante MaterialTheme.colorScheme, sin valores hexadecimales estáticos.",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Text(
                        text = "Muestras de Roles Cromáticos Semánticos del Sistema:",
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        MuestraColor(
                            nombre = "Primary",
                            colorFondo = MaterialTheme.colorScheme.primary,
                            colorTexto = MaterialTheme.colorScheme.onPrimary
                        )
                        MuestraColor(
                            nombre = "PrimaryContainer",
                            colorFondo = MaterialTheme.colorScheme.primaryContainer,
                            colorTexto = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        MuestraColor(
                            nombre = "SecondaryContainer",
                            colorFondo = MaterialTheme.colorScheme.secondaryContainer,
                            colorTexto = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                        MuestraColor(
                            nombre = "TertiaryContainer",
                            colorFondo = MaterialTheme.colorScheme.tertiaryContainer,
                            colorTexto = MaterialTheme.colorScheme.onTertiaryContainer
                        )
                    }
                }
            }

            // =========================================================================
            // BOTÓN DE ACCIÓN: RETORNO HIGIÉNICO (popUpTo + launchSingleTop)
            // =========================================================================
            Button(
                onClick = onVolverAInicio,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary
                ),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.RestartAlt,
                    contentDescription = null
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Volver al Planificador (Limpiar Back Stack)",
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(16.dp))
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

@Composable
private fun MuestraColor(
    nombre: String,
    colorFondo: androidx.compose.ui.graphics.Color,
    colorTexto: androidx.compose.ui.graphics.Color
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(colorFondo)
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = nombre,
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Bold,
            color = colorTexto
        )
    }
}
