package com.example.level_up_gamer_android.ui.screen

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.sin
import kotlin.random.Random


data class Particle(
    val x: Float,
    val y: Float,
    val size: Int,
    val speed: Float
)

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(3000)
        navController.navigate("home") {
            popUpTo("splash") { inclusive = true }
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF02010A))
    ) {
        // --- 1. Sistema de Partículas Flotantes ---
        val particleCount = 30
        val particles = remember {
            List(particleCount) {
                Particle(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    size = Random.nextInt(4, 10),
                    speed = Random.nextFloat() * 0.2f + 0.1f
                )
            }
        }

        val infiniteTransition = rememberInfiniteTransition(label = "GamerTransition")

        // Progreso infinito para mover las partículas y la retícula
        val animProgress by infiniteTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            ),
            label = "Progress"
        )

        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Dibujar Partículas
            particles.forEach { p ->
                val currentY = (p.y * h - (animProgress * p.speed * h)) % h
                val finalY = if (currentY < 0) currentY + h else currentY
                drawCircle(
                    color = Color(0xFF7FE7FF).copy(alpha = 0.4f),
                    radius = p.size.toFloat(),
                    center = androidx.compose.ui.geometry.Offset(p.x * w, finalY)
                )
            }

            // --- 2. Retícula Neon en Perspectiva ---
            val gridCount = 8
            val horizon = h * 0.4f // Línea de horizonte simulada

            // Líneas de fuga (Perspectiva)
            for (i in 0..gridCount) {
                val startX = w * (i.toFloat() / gridCount)
                drawLine(
                    color = Color(0xFF4A2CFF).copy(alpha = 0.3f),
                    start = androidx.compose.ui.geometry.Offset(w / 2, horizon),
                    end = androidx.compose.ui.geometry.Offset(startX, h),
                    strokeWidth = 3f
                )
            }

            // Líneas horizontales en movimiento
            val horizontalLines = 6
            for (i in 0 until horizontalLines) {
                // Movimiento exponencial para simular velocidad en perspectiva
                val lineProgress = (i.toFloat() / horizontalLines + animProgress) % 1f
                val currentLineY = horizon + (h - horizon) * (lineProgress * lineProgress)

                drawLine(
                    color = Color(0xFF003CFF).copy(alpha = (1f - lineProgress) * 0.5f),
                    start = androidx.compose.ui.geometry.Offset(0f, currentLineY),
                    end = androidx.compose.ui.geometry.Offset(w, currentLineY),
                    strokeWidth = 4f
                )
            }
        }

        // --- 3. Animación de Entrada del Logo (Scale Up + Fade In) ---
        val scale = remember { Animatable(0.3f) }
        val alpha = remember { Animatable(0f) }

        LaunchedEffect(Unit) {
            launch { scale.animateTo(1.0f, tween(800, easing = androidx.compose.animation.core.FastOutSlowInEasing)) }
            launch { alpha.animateTo(1.0f, tween(600)) }
        }

        // Glow pulsante arreglado (usando gráficos en vez de shadow de caja para mejor performance)
        val logoGlow by infiniteTransition.animateFloat(
            initialValue = 0.95f,
            targetValue = 1.05f,
            animationSpec = infiniteRepeatable(
                animation = tween(1000, easing = LinearEasing),
                repeatMode = RepeatMode.Reverse
            ),
            label = "LogoGlow"
        )

        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo Level Up Gamer",
            modifier = Modifier
                .size(280.dp)
                .graphicsLayer(
                    scaleX = scale.value * logoGlow,
                    scaleY = scale.value * logoGlow,
                    alpha = alpha.value
                )
        )
    }
}