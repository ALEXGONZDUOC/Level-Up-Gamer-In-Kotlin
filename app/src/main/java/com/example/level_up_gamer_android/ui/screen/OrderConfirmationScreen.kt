package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.*

@Composable
fun OrderConfirmationScreen(
    navController: NavController,
    orderNumber: String
) {
    // Definición de colores Cyberpunk para consistencia visual
    val neonCian = Color(0xFF00F0FF)
    val neonPurple = Color(0xFFBD00FF)
    val neonGreen = Color(0xFF39FF14) // Verde Radiactivo / Éxito
    val textMuted = Color.White.copy(alpha = 0.7f)

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Ícono de éxito en Verde Neón Radiactivo
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = "Éxito",
                tint = neonGreen,
                modifier = Modifier.size(120.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Título principal con un Púrpura Neón eléctrico y vibrante
            CustomText(
                text = "¡PEDIDO EXITOSO!",
                fontSize = 32.sp,
                fontWeight = FontWeight.ExtraBold,
                color = neonPurple
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Texto secundario suavizado para no competir con los neones
            CustomText(
                text = "Tu orden ha sido procesada correctamente en la red.",
                fontSize = 16.sp,
                color = textMuted,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Tarjeta contenedora del ID de la orden
            CustomCard(modifier = Modifier.fillMaxWidth()) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    CustomText(
                        text = "SISTEMA DE RASTREO // NÚMERO DE ORDEN",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = textMuted
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    // El número de orden resalta por completo en Cian Neón futurista
                    CustomText(
                        text = "#$orderNumber",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black,
                        color = neonCian
                    )
                }
            }

            Spacer(modifier = Modifier.height(48.dp))

            // Botón de retorno al inicio
            CustomButton(
                text = "VOLVER AL LOGIN / HOME",
                onClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)  // Controla izquierda y derecha
                    .padding(bottom = 40.dp)      // Controla el margen inferior
            )
        }
    }
}