package com.example.laboratorio1.domain

import com.example.laboratorio1.data.model.PreferenciaViaje
import kotlin.math.roundToInt

/**
 * Estimación del impacto ambiental de una ruta confirmada.
 * Lógica pura de Kotlin (sin Android ni Compose) para poder verificarla con pruebas unitarias.
 */
data class ImpactoAmbiental(
    val co2MitigadoKg: Double,
    val arbolesEquivalentes: Int,
    val ecoPuntos: Int
) {
    companion object {
        private const val CO2_BASE_DIARIO_KG = 45.0
        private const val KM_DIARIOS = 20.0
        private const val MULTIPLICADOR_GRUPAL = 1.8

        // kg de CO2 por km de cada transporte
        private val FACTOR_EMISION = mapOf(
            PreferenciaViaje.TRANSPORTE_BICICLETA to 0.00,
            PreferenciaViaje.TRANSPORTE_TREN to 0.18,
            PreferenciaViaje.TRANSPORTE_ELECTRICO to 0.25
        )
        private const val FACTOR_EMISION_POR_DEFECTO = 0.25

        fun calcular(
            tipoTransporte: String,
            diasDuracion: Int,
            esViajeGrupal: Boolean,
            soloBajaHuella: Boolean
        ): ImpactoAmbiental {
            val factor = FACTOR_EMISION[tipoTransporte] ?: FACTOR_EMISION_POR_DEFECTO
            val multiplicador = if (esViajeGrupal) MULTIPLICADOR_GRUPAL else 1.0
            val co2 = (CO2_BASE_DIARIO_KG - factor * KM_DIARIOS) * diasDuracion * multiplicador

            return ImpactoAmbiental(
                co2MitigadoKg = (co2 * 10).roundToInt() / 10.0,
                arbolesEquivalentes = (diasDuracion * (if (soloBajaHuella) 3 else 1)).coerceAtLeast(1),
                ecoPuntos = diasDuracion * 150 * (if (esViajeGrupal) 2 else 1)
            )
        }
    }
}
