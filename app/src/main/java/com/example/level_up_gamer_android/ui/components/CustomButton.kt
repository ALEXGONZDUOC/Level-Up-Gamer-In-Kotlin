package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
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

@Composable
fun CustomButton(
    text: String,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    icon: ImageVector? = null,
    count: Int = 0
) {
    val gradient = if (enabled) {
        Brush.horizontalGradient(
            colors = listOf(
                Color(0xFF8E2DE2), // Violeta brillante
                Color(0xFF4A00E0)  // Azul eléctrico
            )
        )
    } else {
        Brush.horizontalGradient(
            colors = listOf(Color(0xFF434343), Color(0xFF000000))
        )
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(52.dp)
            .shadow(
                elevation = if (enabled) 12.dp else 0.dp,
                shape = RoundedCornerShape(14.dp),
                ambientColor = Color(0xFF8E2DE2),
                spotColor = Color(0xFF4A00E0)
            )
            .background(gradient, shape = RoundedCornerShape(14.dp))
            .clip(RoundedCornerShape(14.dp))
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxSize(),
            enabled = enabled,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent,
                contentColor = Color.White,
                disabledContainerColor = Color.Transparent,
                disabledContentColor = Color.White.copy(alpha = 0.5f)
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
                        CartIconInsideNeon(count)
                    } else {
                        Icon(
                            imageVector = icon,
                            contentDescription = null,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                }

                Text(
                    text = text,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
            }
        }
    }
}

@Composable
fun CartIconInsideNeon(count: Int) {
    // Gradiente violeta → cyan
    val neonBrush = Brush.linearGradient(
        colors = listOf(
            Color(0xFF7A00FF), // violeta neon
            Color(0xFF00E5FF)  // cyan neon
        )
    )

    Box(contentAlignment = Alignment.Center) {
        // Glow suave detrás del ícono
        Canvas(modifier = Modifier.matchParentSize()) {
            drawCircle(
                brush = neonBrush,
                radius = size.minDimension / 1.8f,
                alpha = 0.25f
            )
        }

        // Ícono con gradiente
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = Color.Unspecified // permite usar el brush
        )

        // Número dentro del carrito
        if (count > 0) {
            Text(
                text = count.toString(),
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}
