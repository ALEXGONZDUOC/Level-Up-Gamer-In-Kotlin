package com.example.level_up_gamer_android.ui.components

import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun CustomFloatingActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    focusRequester: FocusRequester,
    content: @Composable () -> Unit
) {
    FloatingActionButton(
        onClick = onClick,
        modifier = modifier.focusRequester(focusRequester),
        // 1. Usamos la misma forma redondeada (12.dp) de tus botones del Mod
        shape = AppStyles.Buttons.Shape,

        // 2. Cyan Neón para que resalte inmediatamente como acción flotante principal
        containerColor = Color(0xFF00E5FF),

        // 3. Tu DarkBackground para el contenido interno (íconos), garantizando máxima legibilidad
        contentColor = Color(0xFF0A0A1A),

        // 4. Elevación integrada basada en las reglas de diseño gamer
        elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = AppStyles.Buttons.Elevation,       // 8.dp oficial
            pressedElevation = AppStyles.Buttons.PressedElevation // 12.dp oficial
        ),
        content = content
    )
}