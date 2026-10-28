# 🌱 EcoTrip 2026: Sistema Resiliente de Planificación de Rutas e Identidad Visual Adaptativa

> **Proyecto Oficial de Evaluación Práctica — Unidad 2 (RDA-2)**  
> **Asignatura:** Programación Móvil  
> **Facultad de Hábitat, Infraestructura y Creatividad — Escuela de Ingeniería en Sistemas de Información**  
> **Pontificia Universidad Católica del Ecuador (PUCE)**  
> **Estado:** 100% Completado, Probado y Validado con Éxito.

---

## 🌟 1. ¿Qué es EcoTrip 2026? (Explicado con Manzanitas)

Imagina que quieres irte de vacaciones por el Ecuador (al Parque Nacional Cotopaxi, a la Laguna del Quilotoa o a los bosques nubosos de Mindo). Quieres un viaje ecológico, que cuide el planeta, calcule cuánto dióxido de carbono ($CO_2$) vas a ahorrar usando tren o bicicleta en vez de automóvil a gasolina, y que jamás te haga pasar corajes perdiendo lo que estabas escribiendo.

**EcoTrip 2026** es una aplicación móvil nativa construida al 100% con **Jetpack Compose** y **Kotlin 2.0**. Su súper poder no es solo verse increíble y moderna, sino que **es indestructible**:
- Aunque gires el teléfono de vertical a horizontal.
- Aunque te llamen por teléfono o abras otra aplicación pesada y Android cierre la app para ahorrar batería (*Process Death*).
- Aunque apagues el teléfono y lo vuelvas a encender días después.

¡Tus datos y tus preferencias siempre están a salvo!

---

## 🧠 2. La Resiliencia Multinivel: ¿Por qué la app nunca olvida nada?

Para entender cómo funciona la memoria de EcoTrip 2026, piensa en cómo guardas tú las cosas en la vida real:

```
┌────────────────────────────────────────────────────────────────────────┐
│                      RESILIENCIA MULTINIVEL DE DATOS                   │
├──────────────────┬──────────────────────┬──────────────────────────────┤
│ Nivel            │ Analogía Cotidiana   │ Componente Técnico Android   │
├──────────────────┼──────────────────────┼──────────────────────────────┤
│ 1. Memoria RAM   │ Memoria de Trabajo   │ ViewModel (StateFlow)        │
│ 2. Proceso       │ Libreta de Bolsillo  │ SavedStateHandle             │
│ 3. Disco Duro    │ Caja Fuerte de Metal │ Jetpack DataStore Preferences│
└──────────────────┴──────────────────────┴──────────────────────────────┘
```

### 🔹 Nivel 1: La Memoria de Trabajo (ViewModel)
- **¿Qué es?** Es la memoria rápida de la aplicación.
- **¿Para qué sirve?** Cuando rotas el teléfono de lado, Android normalmente destruye la pantalla y la vuelve a dibujar desde cero. El **ViewModel** sobrevive a esa rotación como si nada hubiera pasado. Mantiene los cálculos al día y el estado de la pantalla en tiempo real.

### 🔹 Nivel 2: La Libreta de Bolsillo (SavedStateHandle)
- **¿Qué es?** Es una libreta mágica donde la app anota rápido lo que estás haciendo por si ocurre una emergencia.
- **¿Para qué sirve?** Si estás llenando el formulario y de repente minimizas la app para contestar WhatsApp, y el sistema operativo Android necesita memoria RAM y "mata" a EcoTrip (*Process Death*), el **SavedStateHandle** guarda el destino seleccionado, los días de viaje y si viajas en grupo. Cuando vuelves a abrir la app, la libreta se lee automáticamente y todo sigue en su lugar.

### 🔹 Nivel 3: La Caja Fuerte de Metal (DataStore Preferences)
- **¿Qué es?** Es un archivo blindado grabado directamente en el disco del teléfono usando corrutinas y flujos reactivos (`Flow`).
- **¿Para qué sirve?** Guarda tus preferencias globales: tu nombre de viajero, tu medio de transporte ecológico favorito (Tren, Bicicleta o Caminata) y si prefieres rutas estrictas de baja huella. Aunque desinstales actualizaciones, reinicies el teléfono o lo apagues durante semanas, la caja fuerte nunca se borra.

---

## 🔄 3. Arquitectura UDF: "El Jefe y el Pintor"

La aplicación utiliza la arquitectura recomendada por Google: **MVI / MVVM con Flujo Unidireccional de Datos (UDF)**. 

Para explicarlo de forma sencilla:
1. **La Pantalla de Compose es el PINTOR:** El pintor no piensa, no calcula fórmulas y no toma decisiones. Solo tiene un pincel y dibuja lo que ve en una hoja de instrucciones llamada `EcoTripUiState`.
2. **El ViewModel es el JEFE DE OBRA:** Cuando el usuario toca la pantalla (un evento), el pintor no cambia nada por su cuenta; corre y le avisa al jefe: *"¡El usuario escribió 5 días!"*.
3. **El Jefe actualiza la hoja:** El jefe valida que no hayan letras extrañas, recalcula el impacto ambiental, genera un nuevo estado inmutable y le entrega la nueva hoja al pintor.
4. **El pintor se redibuja solito (*Recomposición*):** El pintor mira la nueva hoja y actualiza la pantalla al instante.

```
       Eventos del Usuario (Click, Escribir)
    ┌────────────────────────────────────────┐
    ▼                                        │
┌──────────────┐     Decide y Valida    ┌───────────────┐
│ Formulario / │ ─────────────────────> │    EcoTrip    │
│   Resumen    │ <───────────────────── │   ViewModel   │
└──────────────┘     Nuevo Estado       └───────────────┘
  (El Pintor)     (EcoTripUiState)         (El Jefe)
```

---

## 🚦 4. Navegación Fuertemente Tipada (Type-Safe Navigation)

En las aplicaciones antiguas de Android, para pasar de la pantalla de inicio a la de resumen se mandaban textos como `"resumen_screen/Cotopaxi/3"`. Si te equivocabas en una barra `/` o en una letra, la app se cerraba de golpe con una pantalla de error (*Crash*).

En **EcoTrip 2026** usamos **Navigation Compose 2.8+ con Kotlinx Serialization**:
- Las rutas son clases con tipos seguros:
  ```kotlin
  @Serializable
  data class ResumenRoute(
      val nombreViajero: String,
      val tipoTransporte: String,
      val soloBajaHuella: Boolean,
      val destino: String,
      val diasDuracion: Int,
      val esViajeGrupal: Boolean
  )
  ```
- **Higiene del Back Stack:** Cuando estás en la pantalla de Resumen y presionas *"Planificar Nueva Ruta"*, la aplicación no amontona pantallas en la memoria. Usa:
  ```kotlin
  navController.navigate(FormularioRoute) {
      popUpTo<FormularioRoute> { inclusive = false }
      launchSingleTop = true
  }
  ```
  Esto limpia el historial y te deja en el formulario limpio sin desperdiciar memoria.

---

## 🎨 5. Diseño Visual Adaptativo: Material Design 3 + Material You

La aplicación no usa colores aburridos ni genéricos:
- **Material You (Color Dinámico):** En teléfonos con Android 12 en adelante (API 31 a API 36), la aplicación extrae los colores del fondo de pantalla del usuario. Si el usuario tiene un fondo de flores verdes, la app se viste de tonos esmeralda y menta; si tiene un fondo azul marino, la app se adapta automáticamente a tonos oceánicos.
- **Componentes Nativos Elegantes:**
  - `Scaffold`: Estructura base con barra superior y botón flotante extendido (*ExtendedFloatingActionButton*).
  - `ElevatedCard`: Tarjetas con sombras suaves para organizar la información visualmente.
  - `FilterChip`: Botones modernos para elegir el transporte con iconos animados.
  - `Switch`: Interruptor táctil para activar o desactivar viajes grupales.
  - `SnackbarHost`: Mensajes emergentes elegantes que avisan si intentas continuar sin llenar los datos.

---

## 🧪 6. Calidad y Pruebas Automatizadas (100% Superadas)

El código cuenta con una batería completa de pruebas que aseguran que nada falle:

1. **Pruebas Unitarias ([EcoTripValidationTest.kt](file:///d:/SEBASTIAN/PROGRAMACION_MOVIL/app/src/test/java/com/example/laboratorio1/EcoTripValidationTest.kt)):**
   - Comprueba que si alguien escribe letras en el campo de días (ej. `"cinco"`), el operador Elvis `?:` y `.toIntOrNull()` lo conviertan limpiamente en un número seguro sin caerse.
   - Comprueba que la serialización de rutas no pierda ningún parámetro.
   - Comprueba que las fórmulas de huella ecológica ($CO_2$ y agua) calculen los valores matemáticos exactos.
2. **Pruebas Instrumentadas de UI ([EcoTripComposeTest.kt](file:///d:/SEBASTIAN/PROGRAMACION_MOVIL/app/src/androidTest/java/com/example/laboratorio1/EcoTripComposeTest.kt)):**
   - Levanta la pantalla en Compose y verifica que los textos, tarjetas e insignias ecológicas aparezcan en pantalla.

Para ejecutar todas las pruebas en la terminal:
```bash
./gradlew testDebugUnitTest
```
*(Resultado: **BUILD SUCCESSFUL** — Todas las pruebas pasan en verde).*

---

## 🗣️ 7. Guía Rápida para la Defensa Oral (Preguntas Típicas del Docente)

Si el docente te hace preguntas durante la evaluación oral, aquí tienes las respuestas directas, técnicas y fáciles de decir:

### ❓ Pregunta 1: *"¿Por qué separaron los datos en ViewModel, SavedStateHandle y DataStore?"*
> **Respuesta:**  
> *"Porque cada dato tiene un ciclo de vida diferente. El **ViewModel** retiene el estado rápido en memoria RAM mientras la pantalla gira. El **SavedStateHandle** protege el formulario en progreso si Android mata el proceso por falta de memoria RAM (*Process Death*). Y el **DataStore** guarda en el disco duro las preferencias globales del usuario para que perduren para siempre, incluso si el celular se apaga."*

### ❓ Pregunta 2: *"¿Qué es el State Hoisting y por qué lo usaron en FormularioScreen?"*
> **Respuesta:**  
> *"El State Hoisting (elevación de estado) consiste en quitarle el control del estado a los componentes visuales y pasárselo al ViewModel mediante eventos (`callbacks`). Así las funciones Composable se vuelven 'tontas', no guardan variables internas, no tienen efectos secundarios y son facilísimas de probar y reutilizar."*

### ❓ Pregunta 3: *"¿Cómo manejan los errores de entrada si el usuario escribe texto en un campo numérico?"*
> **Respuesta:**  
> *"Aplicamos sanitización reactiva con `.toIntOrNull()`. Si el usuario ingresa un valor inválido o vacío, usamos el operador Elvis `?:` para asignar un valor por defecto seguro (1 día) y activamos una bandera de validación en el `UiState` que muestra el error en rojo en el `OutlinedTextField` sin que la aplicación se caiga."*

### ❓ Pregunta 4: *"¿Cómo garantiza la aplicación que no se acumulen pantallas en el Back Stack al regresar?"*
> **Respuesta:**  
> *"Al presionar 'Planificar Nueva Ruta', configuramos el NavController con `popUpTo<FormularioRoute> { inclusive = false }` junto a `launchSingleTop = true`. Esto retira la pantalla de resumen del historial y reutiliza la instancia existente del formulario, evitando fugas de memoria y pantallas duplicadas."*

---

## 📂 8. Estructura Limpia del Proyecto

```text
app/src/main/java/com/example/laboratorio1/
├── MainActivity.kt               # Punto de entrada con AbstractSavedStateViewModelFactory
├── data/
│   ├── local/
│   │   └── DataStoreManager.kt   # Persistencia en Disco con Jetpack DataStore Preferences
│   └── model/
│       └── PreferenciaViaje.kt   # Data class inmutable con transportes y constantes
├── navigation/
│   └── EcoTripRoutes.kt          # Rutas seguras (@Serializable FormularioRoute y ResumenRoute)
├── ui/
│   ├── navigation/
│   │   └── EcoTripNavHost.kt     # Grafo de navegación declarativo con toRoute<ResumenRoute>()
│   ├── screens/
│   │   ├── FormularioScreen.kt   # Pantalla 1: Formulario Material 3 con State Hoisting
│   │   └── ResumenScreen.kt      # Pantalla 2: Resumen, Métricas de Impacto y Navegación
│   └── theme/
│       ├── Color.kt              # Paleta cromática ecológica
│       ├── Theme.kt              # Soporte completo para Material You Dynamic Colors
│       └── Type.kt               # Tipografía moderna
└── viewmodel/
    ├── EcoTripUiState.kt         # Data class inmutable del estado completo de la UI
    └── EcoTripViewModel.kt       # Orquestador con SavedStateHandle y Flow
```

---

## 🚀 9. Cómo Ejecutar el Proyecto en Android Studio

1. **Abrir el Proyecto:**
   - Abre Android Studio.
   - Selecciona **File -> Open...** y elige la carpeta `PROGRAMACION_MOVIL`.
2. **Sincronización:**
   - Espera a que Gradle termine la sincronización automática (*Gradle Sync*).
3. **Ejecutar la App:**
   - Selecciona un emulador con API 31 o superior (recomendado: Pixel 8 con API 36).
   - Haz clic en el botón verde **Run 'app'** (o presiona `Shift + F10`).
4. **Verificar Pruebas:**
   - En la pestaña lateral izquierda, haz clic derecho sobre `app/src/test` y selecciona **Run 'Tests in laboratorio1'**.

---

## 👥 Equipo de Desarrollo

- **Grupo:** Grupo de Trabajo N° 5  
- **Institución:** Pontificia Universidad Católica del Ecuador (PUCE)  
- **Docente:** Ing. Juan Francisco Chafla A.  
- **Repositorio Oficial:** [GitHub - ProfileValidator](https://github.com/joansantacruz05/ProfileValidator.git)
