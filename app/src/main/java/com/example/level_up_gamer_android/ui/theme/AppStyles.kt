package com.example.level_up_gamer_android.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * AppStyles - El "Style.css" de LevelUP Gamer.
 * MATCH 1:1 con LevelUP_MODELdISENG
 */
object AppStyles {

    // --- FONDOS Y GRADIENTES ---
    object Backgrounds {
        val MainGradient = Brush.verticalGradient(
            colors = listOf(
                Color(0xFF0A0A1A), // DarkBackground
                Color(0xFF0A0A1A).copy(alpha = 0.8f) 
            )
        )
        val SurfaceAlpha = 0.85f
    }

    // --- TARJETAS (Cards) ---
    object Cards {
        // La forma asimétrica es la clave del diseño del Mod
        val Shape = RoundedCornerShape(
            topStart = 16.dp, 
            topEnd = 16.dp, 
            bottomStart = 0.dp, 
            bottomEnd = 0.dp
        )
        val Elevation = 4.dp // Elevación sutil pero marcada por el borde
        val BorderWidth = 1.dp
        val BorderColor = Color(0xFF3FA4FF) // BlueMedium (Primary)
        val BackgroundColor = Color(0xFF14142B) // CardBackground
    }

    // --- BOTONES (Buttons) ---
    object Buttons {
        val NeonGradient = Brush.linearGradient(
            colors = listOf(
                Color(0xFF7A00FF), // violeta neon
                Color(0xFF00E5FF)  // cyan neon
            )
        )
        val Shape = RoundedCornerShape(12.dp)
        val Height = 50.dp
        val Elevation = 8.dp
        val PressedElevation = 12.dp // Faltaba esta propiedad
        val GlowAlpha = 0.25f
    }

    // --- ENTRADAS DE TEXTO (Inputs) ---
    object Inputs {
        val Shape = RoundedCornerShape(8.dp)
        val UnfocusedAlpha = 0.7f
    }
}
