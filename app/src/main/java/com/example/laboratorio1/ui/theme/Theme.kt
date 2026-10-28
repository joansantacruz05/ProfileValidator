package com.example.laboratorio1.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val EcoDarkColorScheme = darkColorScheme(
    primary = EcoGreenPrimaryDark,
    onPrimary = EcoGreenOnPrimaryDark,
    primaryContainer = EcoGreenPrimaryContainerDark,
    onPrimaryContainer = EcoGreenOnPrimaryContainerDark,
    secondary = EcoTealSecondaryDark,
    onSecondary = EcoTealOnSecondaryDark,
    secondaryContainer = EcoTealSecondaryContainerDark,
    onSecondaryContainer = EcoTealOnSecondaryContainerDark,
    tertiary = EcoBrownTertiaryDark,
    onTertiary = EcoBrownOnTertiaryDark,
    tertiaryContainer = EcoBrownTertiaryContainerDark,
    onTertiaryContainer = EcoBrownOnTertiaryContainerDark,
    surface = EcoSurfaceDark,
    onSurface = EcoOnSurfaceDark,
    surfaceVariant = EcoSurfaceVariantDark,
    onSurfaceVariant = EcoOnSurfaceVariantDark
)

private val EcoLightColorScheme = lightColorScheme(
    primary = EcoGreenPrimaryLight,
    onPrimary = EcoGreenOnPrimaryLight,
    primaryContainer = EcoGreenPrimaryContainerLight,
    onPrimaryContainer = EcoGreenOnPrimaryContainerLight,
    secondary = EcoTealSecondaryLight,
    onSecondary = EcoTealOnSecondaryLight,
    secondaryContainer = EcoTealSecondaryContainerLight,
    onSecondaryContainer = EcoTealOnSecondaryContainerLight,
    tertiary = EcoBrownTertiaryLight,
    onTertiary = EcoBrownOnTertiaryLight,
    tertiaryContainer = EcoBrownTertiaryContainerLight,
    onTertiaryContainer = EcoBrownOnTertiaryContainerLight,
    surface = EcoSurfaceLight,
    onSurface = EcoOnSurfaceLight,
    surfaceVariant = EcoSurfaceVariantLight,
    onSurfaceVariant = EcoOnSurfaceVariantLight
)

/**
 * Tema principal de EcoTrip 2026 adaptado a Material Design 3 y Color Dinámico (Material You).
 *
 * En dispositivos con Android 12+ (API 31 en adelante, incluyendo el entorno de evaluación API 36),
 * extrae automáticamente los roles cromáticos del fondo de pantalla (Wallpaper) del sistema mediante
 * `dynamicLightColorScheme` y `dynamicDarkColorScheme`.
 *
 * Cuando dynamicColor es false o en sistemas anteriores, aplica el sistema de diseño ecológico armónico.
 */
@Composable
fun EcoTripTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> EcoDarkColorScheme
        else -> EcoLightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = EcoTripTypography,
        content = content
    )
}

/**
 * Alias de compatibilidad hacia atrás
 */
@Composable
fun Laboratorio1Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    EcoTripTheme(darkTheme = darkTheme, dynamicColor = dynamicColor, content = content)
}