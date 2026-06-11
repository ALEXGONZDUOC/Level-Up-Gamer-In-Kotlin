package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun GradientSurface(content: @Composable () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            // Vinculamos el fondo directamente al gradiente oficial del Mod
            .background(brush = AppStyles.Backgrounds.MainGradient)
    ) {
        content()
    }
}