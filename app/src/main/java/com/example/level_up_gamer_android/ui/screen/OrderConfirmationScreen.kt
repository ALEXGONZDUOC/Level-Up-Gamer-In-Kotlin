package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.CustomButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.GradientSurface

@Composable
fun OrderConfirmationScreen(navController: NavController, orderNumber: String) {
    GradientSurface {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Default.CheckCircle,
                contentDescription = null,
                modifier = Modifier.size(120.dp),
                tint = Color(0xFF4CAF50)
            )
            Spacer(modifier = Modifier.height(24.dp))
            CustomText("¡Gracias por tu compra!", fontSize = 28.sp, fontWeight = FontWeight.Bold)
            CustomText("Tu pedido #$orderNumber ha sido registrado.", fontSize = 16.sp)
            CustomText("Te hemos enviado un correo de confirmación.", fontSize = 14.sp, color = Color.LightGray)
            
            Spacer(modifier = Modifier.height(48.dp))
            
            CustomButton(
                text = "Volver al inicio",
                onClick = { 
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
