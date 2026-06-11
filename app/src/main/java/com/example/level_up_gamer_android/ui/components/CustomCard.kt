package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun CustomCard(
    modifier: Modifier = Modifier,
    content: @Composable ColumnScope.() -> Unit
) {
    Card(
        modifier = modifier,
        // 1. Aplicamos la forma asimétrica del diseño del Mod (16.dp arriba, 0.dp abajo)
        shape = AppStyles.Cards.Shape,

        // 2. Borde sólido en sintonía con el color de acento oficial (Violeta Neón)
        border = BorderStroke(
            width = AppStyles.Cards.BorderWidth, // 1.dp oficial
            color = AppStyles.Cards.BorderColor  // Color(0xFF7A00FF)
        ),

        // 3. Elevación sutil pero marcada por el contorno neón
        elevation = CardDefaults.cardElevation(
            defaultElevation = AppStyles.Cards.Elevation // 4.dp oficial
        ),

        // 4. Fondo oscuro con matiz lila aplicando el alpha configurado en tus estilos
        colors = CardDefaults.cardColors(
            containerColor = AppStyles.Cards.BackgroundColor.copy(
                alpha = AppStyles.Backgrounds.SurfaceAlpha // 0.85f oficial
            )
        ),
        content = content
    )
}