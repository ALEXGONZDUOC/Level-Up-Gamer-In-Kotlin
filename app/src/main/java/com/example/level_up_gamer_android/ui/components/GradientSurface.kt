package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import com.example.level_up_gamer_android.ui.theme.DarkBackground

@Composable
fun GradientSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF02010A), // Negro azulado profundo
                        Color(0xFF050515), // Azul espacio muy oscuro
                        Color(0xFF0A0A25)  // Azul noche profundo
                    )
                )
            )
    ) {
        content()
    }
}
