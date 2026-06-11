package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun GamerSnackbar(snackbarData: SnackbarData) {
    Card(
        // 1. Usamos la forma de los botones/inputs (12.dp) para notificaciones compactas
        shape = AppStyles.Buttons.Shape,

        // 2. Borde neón unificado con el color insignia del Mod (Violeta Neón)
        border = BorderStroke(
            width = AppStyles.Cards.BorderWidth, // 1.dp oficial
            color = AppStyles.Cards.BorderColor  // Color(0xFF7A00FF)
        ),

        // 3. Fondo oscuro con el alpha oficial para superficies flotantes
        colors = CardDefaults.cardColors(
            containerColor = AppStyles.Cards.BackgroundColor.copy(
                alpha = AppStyles.Backgrounds.SurfaceAlpha // 0.85f oficial
            )
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Mensaje de la notificación (izquieda)
            CustomText(
                text = snackbarData.visuals.message,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )

            // 4. Si el snackbar incluye un botón de acción (ej: "Ver"), lo pintamos en Cyan Neón
            snackbarData.visuals.actionLabel?.let { actionLabel ->
                Spacer(modifier = Modifier.width(8.dp))
                TextButton(
                    onClick = { snackbarData.performAction() },
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    CustomText(
                        text = actionLabel,
                        color = Color(0xFF00E5FF), // Cyan Neón interactivo
                        style = MaterialTheme.typography.labelLarge,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}