package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddShoppingCart
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    count: Int = 0
) {
    // 1. Vinculamos el gradiente al NeonGradient oficial de AppStyles
    val gradient = if (enabled) {
        AppStyles.Buttons.NeonGradient
    } else {
        // Modo deshabilitado Cyberpunk: Escala de grises oscuros integrados
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF252538),
                Color(0xFF14142B)
            )
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(AppStyles.Buttons.Height) // 50.dp oficiales
            .shadow(
                // Control dinámico de elevaciones basado en tus AppStyles
                elevation = if (enabled) AppStyles.Buttons.Elevation else 0.dp,
                shape = AppStyles.Buttons.Shape, // Redondeado de 12.dp oficial
                ambientColor = Color(0xFF7A00FF), // Sombra dispersa Violeta Neón
                spotColor = Color(0xFF00E5FF)     // Punto de luz Cyan Neón
            )
            .background(gradient, shape = AppStyles.Buttons.Shape)
            .clip(AppStyles.Buttons.Shape)
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha)
            ),
            contentPadding = PaddingValues(0.dp),
            elevation = null
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                if (icon != null) {
                    if (icon == Icons.Default.ShoppingCart || icon == Icons.Default.AddShoppingCart) {
                        CartIconInsideNeon(count, enabled)
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp),
                            tint = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1,
                    color = if (enabled) Color.White else Color.White.copy(alpha = 0.4f)
                )
            }
        }
    }
}

@Composable
fun CartIconInsideNeon(count: Int, enabled: Boolean) {
    // Usamos el gradiente oficial de botones para el aura de brillo
    val neonBrush = AppStyles.Buttons.NeonGradient

    Box(contentAlignment = Alignment.Center) {
        // Solo dibuja el destello/glow si el botón está activo
        if (enabled) {
            Canvas(modifier = Modifier.matchParentSize()) {
                drawCircle(
                    brush = neonBrush,
                    radius = size.minDimension / 1.8f,
                    alpha = AppStyles.Buttons.GlowAlpha // 0.25f oficial
                )
            }
        }

        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = if (enabled) Color.White else Color.White.copy(alpha = 0.3f)
        )

        // Contador numérico sobrepuesto de forma limpia
        if (count > 0 && enabled) {
            Text(
                text = count.toString(),
                color = Color(0xFF0A0A1A), // Tu DarkBackground para que resalte por contraste dentro del ícono blanco
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier
                    .align(Alignment.Center)
                    .padding(bottom = 24.dp / 6) // Pequeño offset para centrar el número en el espacio de la cesta
            )
        }
    }
}