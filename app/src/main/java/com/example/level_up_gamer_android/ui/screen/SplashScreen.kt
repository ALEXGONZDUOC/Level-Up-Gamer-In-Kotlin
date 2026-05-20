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
            .background(Color(0xFF02010A)) // negro azulado gamer
    ) {
        // --- Fondo animado de partículas gamer ---
        val particleCount = 40
        val particles = remember {
            List(particleCount) {
                Particle(
                    x = Random.nextFloat(),
                    y = Random.nextFloat(),
                    size = Random.nextInt(4, 10),
                    speed = Random.nextFloat() * 0.5f + 0.2f
                )
            }
        }

        val particleTransition = rememberInfiniteTransition()
        val time by particleTransition.animateFloat(
            initialValue = 0f,
            targetValue = 1f,
            animationSpec = infiniteRepeatable(
                animation = tween(4000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        // Animación infinita para el movimiento ondulante
        val waveTransition = rememberInfiniteTransition()
        val waveOffset by waveTransition.animateFloat(
            initialValue = 0f,
            targetValue = 2 * Math.PI.toFloat(),
            animationSpec = infiniteRepeatable(
                animation = tween(3000, easing = LinearEasing),
                repeatMode = RepeatMode.Restart
            )
        )

        val random = Random(System.currentTimeMillis())

        Canvas(modifier = Modifier.fillMaxSize()) {

            val spacing = 120f
            val strokeWidth = 6f

            // Gradiente violeta → cyan
            val gradient = Brush.horizontalGradient(
                colors = listOf(
                    Color(0xFF4A2CFF), // violeta profundo
                    Color(0xFF003CFF), // azul intenso
                    Color(0xFF3FA4FF), // azul medio
                    Color(0xFF7FE7FF)  // cyan suave neon
                )
            )

            var y = 0f
            while (y < size.height) {

                val path = Path().apply {
                    moveTo(0f, y)

                    // Curva ondulante animada
                    cubicTo(
                        size.width * 0.25f,
                        y + 40f * sin(waveOffset + y / 200f),
                        size.width * 0.75f,
                        y - 40f * sin(waveOffset + y / 200f),
                        size.width,
                        y
                    )
                }

                drawPath(
                    path = path,
                    brush = gradient,
                    style = Stroke(width = strokeWidth)
                )

                y += spacing
            }
        }



        // Animaciones base (fade + zoom + movimiento)
        val scale = remember { Animatable(0.8f) }
        val alpha = remember { Animatable(1f) }
        val offsetY = remember { Animatable(40f) }

        LaunchedEffect(Unit) {
            launch { scale.animateTo(1f, tween(600)) }
            launch { alpha.animateTo(1f, tween(500)) }
            launch { offsetY.animateTo(1f, tween(900)) }
        }

        // Animación infinita para el glow pulsante
        val infiniteTransition = rememberInfiniteTransition()
        val glow by infiniteTransition.animateFloat(
            initialValue = 20f,
            targetValue = 50f,
            animationSpec = infiniteRepeatable(
                animation = tween(1200),
                repeatMode = RepeatMode.Reverse
            )
        )

        Image(
            painter = painterResource(id = R.drawable.logo_app),
            contentDescription = "Logo Level Up Gamer",
            modifier = Modifier
                .size(300.dp)
                .graphicsLayer(
                    scaleX = scale.value,
                    scaleY = scale.value,
                    alpha = alpha.value,
                    translationY = offsetY.value
                )
                .shadow(
                    elevation = glow.dp,
                    shape = RectangleShape, // ← Glow sin fondo circular
                    ambientColor = Color(0xFF8831E7),
                    spotColor = Color(0xFF9D4BFF)
                )
        )
    }
}
