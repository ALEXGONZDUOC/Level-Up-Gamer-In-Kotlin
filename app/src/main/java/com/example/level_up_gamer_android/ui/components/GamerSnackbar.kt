package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun GamerSnackbar(snackbarData: SnackbarData) {
    Card(
        shape = RoundedCornerShape(12.dp),
        border = BorderStroke(
            width = 1.dp,
            brush = Brush.linearGradient(
                colors = listOf(Color(0xFF8831E7), Color(0xFF00E5FF))
            )
        ),
        colors = CardDefaults.cardColors(
            containerColor = Color(0xFF16162B).copy(alpha = 0.95f)
        ),
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.Center
        ) {
            CustomText(
                text = snackbarData.visuals.message,
                color = Color.White,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}
