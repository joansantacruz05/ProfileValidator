package com.example.laboratorio1.data.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.example.laboratorio1.data.model.PreferenciaViaje
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import java.io.IOException

// Extensión para instanciar el singleton delegado de DataStore a nivel de Context
private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "ecotrip_global_prefs")

/**
 * Gestor de Persistencia en Disco mediante Jetpack DataStore Preferences.
 * Almacena de manera asíncrona, transaccional y permanente las preferencias globales
 * del usuario para que perduren incluso tras el cierre forzado de la aplicación o reinicio del sistema.
 */
class DataStoreManager(private val context: Context) {

    companion object {
        private val KEY_NOMBRE_VIAJERO = stringPreferencesKey("pref_nombre_viajero")
        private val KEY_TIPO_TRANSPORTE = stringPreferencesKey("pref_tipo_transporte")
        private val KEY_SOLO_BAJA_HUELLA = booleanPreferencesKey("pref_solo_baja_huella")
    }

    /**
     * Flujo reactivo (Flow) que emite las preferencias globales almacenadas en disco.
     * Incorpora manejo seguro ante excepciones de I/O emitiendo un estado vacío seguro.
     */
    val preferenciasViajeFlow: Flow<PreferenciaViaje> = context.dataStore.data
        .catch { exception ->
            if (exception is IOException) {
                emit(emptyPreferences())
            } else {
                throw exception
            }
        }
        .map { preferences ->
            PreferenciaViaje(
                nombreViajero = preferences[KEY_NOMBRE_VIAJERO] ?: "",
                tipoTransporte = preferences[KEY_TIPO_TRANSPORTE] ?: PreferenciaViaje.TRANSPORTE_TREN,
                soloBajaHuella = preferences[KEY_SOLO_BAJA_HUELLA] ?: true
            )
        }

    /**
     * Actualiza únicamente el nombre del viajero en disco.
     */
    suspend fun actualizarNombre(nombre: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_NOMBRE_VIAJERO] = nombre
        }
    }

    /**
     * Actualiza el tipo de transporte ecológico predilecto en disco.
     */
    suspend fun actualizarTipoTransporte(transporte: String) {
        context.dataStore.edit { preferences ->
            preferences[KEY_TIPO_TRANSPORTE] = transporte
        }
    }

    /**
     * Actualiza el switch de rutas exclusivas de baja huella en disco.
     */
    suspend fun actualizarSoloBajaHuella(soloBajaHuella: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SOLO_BAJA_HUELLA] = soloBajaHuella
        }
    }
}
