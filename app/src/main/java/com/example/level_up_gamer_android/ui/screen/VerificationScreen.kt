package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.CustomButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.CustomTextField
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun VerificationScreen(navController: NavController, viewModel: FormularioViewModel, email: String) {
    var codigo by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val scope = rememberCoroutineScope()

    // ⏱️ Variables de estado para el temporizador
    var tiempoRestante by remember { mutableIntStateOf(30) }
    var puedeReenviar by remember { mutableStateOf(false) }

    // 🔄 Efecto que maneja la cuenta regresiva de 30 segundos
    LaunchedEffect(tiempoRestante) {
        if (tiempoRestante > 0) {
            puedeReenviar = false
            delay(1000L)
            tiempoRestante -= 1
        } else {
            puedeReenviar = true
        }
    }

    GradientSurface {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            CustomText(
                text = "VERIFICA TU CUENTA",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary
            )

            Spacer(modifier = Modifier.height(16.dp))

            CustomText(
                text = "Hemos enviado un código de 6 dígitos a:\n$email",
                textAlign = TextAlign.Center,
                fontSize = 16.sp,
                color = Color.White.copy(alpha = 0.8f)
            )

            Spacer(modifier = Modifier.height(32.dp))

            CustomTextField(
                value = codigo,
                onValueChange = { if (it.length <= 6) codigo = it.filter { c -> c.isDigit() } },
                label = "Código de 6 dígitos",
                modifier = Modifier.fillMaxWidth()
            )

            if (mensaje.isNotEmpty()) {
                Spacer(modifier = Modifier.height(16.dp))
                val esExito = mensaje.contains("éxito", ignoreCase = true) || mensaje.contains("correctamente", ignoreCase = true)
                CustomText(
                    text = mensaje,
                    color = if (esExito) Color.Green else Color.Yellow,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // 🌟 SECCIÓN DE REENVÍO CON TEMPORIZADOR CYBERPUNK
            if (puedeReenviar) {
                TextButton(
                    onClick = {
                        viewModel.solicitarRecuperacion(email) { success, msg ->
                            mensaje = msg
                            if (success) tiempoRestante = 30
                        }
                    }
                ) {
                    CustomText(
                        text = "Reenviar código",
                        color = MaterialTheme.colorScheme.secondary,
                        fontWeight = FontWeight.Bold
                    )
                }
            } else {
                CustomText(
                    text = "Reenviar código en ${tiempoRestante}s",
                    fontSize = 14.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            CustomButton(
                text = if (loading) "Verificando..." else "Verificar Ahora",
                enabled = codigo.length == 6 && !loading,
                onClick = {
                    viewModel.verificarCuenta(email, codigo) { success, msg ->
                        mensaje = msg
                        if (success) {
                            scope.launch {
                                delay(2000)
                                navController.navigate("login") {
                                    popUpTo("register") { inclusive = true }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextButton(onClick = { navController.popBackStack() }) {
                CustomText("Volver al Login", color = MaterialTheme.colorScheme.primary)
            }
        }
    }
}