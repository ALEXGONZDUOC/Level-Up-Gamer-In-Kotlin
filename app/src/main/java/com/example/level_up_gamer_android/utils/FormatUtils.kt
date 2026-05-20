package com.example.level_up_gamer_android.utils

import java.util.Locale

/**
 * Sello LevelUP: Convierte precios al formato gamer (ej: 100.000)
 */
fun Double.format3(): String {
    // Formato: 1.000.000 (sin decimales, con puntos de miles)
    return String.format(Locale.GERMANY, "%,.0f", this)
}
