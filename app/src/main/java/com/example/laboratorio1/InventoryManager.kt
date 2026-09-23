package com.example.laboratorio1

import java.util.Locale

fun main() {
    // ==========================================
    // PASO 1: Definición del Modelo de Producto
    // ==========================================
    val productId: Int = 101 // ID inmutable (no cambia)
    val productName: String = "Smartphone X1" // Nombre obligatorio (no nulo)
    var productDescription: String? = null // ? permite que sea nulo
    var stockQuantity: Int = 25 // Cantidad mutable (cambia con ventas)
    val unitPrice: Double = 599.99 // Precio base inmutable

    println("--- Datos Iniciales Cargados ---")

    // ==========================================
    // PASO 2: Lógica de Precios con IVA
    // ==========================================
    val TAX_RATE = 0.15 // IVA del 15%
    val totalPriceWithTax = unitPrice * (1 + TAX_RATE)

    // ==========================================
    // PASO 3: Manejo Seguro de Nulos (Operador Elvis)
    // ==========================================
    val descriptionToShow = productDescription ?: "Sin descripción disponible"
    println("Producto: $productName")
    println("Descripción: $descriptionToShow")

    // ==========================================
    // PASO 5: Actualización de Inventario y Reporte
    // ==========================================
    val itemsSold = 5
    stockQuantity -= itemsSold // Actualización de variable mutable

    val report = """
    --- REPORTE DE INVENTARIO ---
    ID: $productId | Nombre: $productName
    Precio Final: $${String.format(Locale.US, "%.2f", totalPriceWithTax)}
    Stock Actual: $stockQuantity
    Estado: ${if (stockQuantity > 10) "Suficiente" else "Crítico"}
    -----------------------------
    """.trimIndent()
    println(report)

    // ==========================================
    // DESAFÍO EXTRA 1: Análisis Crítico de Nulos
    // ==========================================
    // Si intentamos hacer: productName = null
    // El compilador de Kotlin arrojará un error en tiempo de compilación:
    // "Null can not be a value of a non-null type String"
    // Explicación: Previene errores NullPointerException en ejecución y asegura que los
    // campos obligatorios (como el nombre del producto) nunca ingresen vacíos a la BD.

    // ==========================================
    // DESAFÍO EXTRA 2: Validador de Precios Dinámico
    // ==========================================
    println("\n=== DESAFÍO EXTRA 2: Validador de Precios Dinámico ===")
    
    // Prueba A: Entrada válida de usuario
    val inputUsuarioValido: String? = "650.50"
    validarYCalcularPrecio("Entrada Válida", inputUsuarioValido, TAX_RATE)

    // Prueba B: Entrada inválida de usuario
    val inputUsuarioInvalido: String? = "No quiero pagar"
    validarYCalcularPrecio("Entrada Inválida", inputUsuarioInvalido, TAX_RATE)
}

/**
 * Función auxiliar para el Desafío Extra 2
 * Convierte de manera segura un String? a Double utilizando toDoubleOrNull() y Elvis Operator.
 */
fun validarYCalcularPrecio(escenario: String, inputUsuario: String?, taxRate: Double) {
    // Conversión segura con toDoubleOrNull() y respaldo con operador Elvis (?: 0.0)
    val precioBase = inputUsuario?.toDoubleOrNull() ?: 0.0
    val precioFinal = precioBase * (1 + taxRate)

    println("[$escenario] Input: \"$inputUsuario\" -> Precio Base: $$precioBase | Precio con IVA: $${String.format(Locale.US, "%.2f", precioFinal)}")
}
