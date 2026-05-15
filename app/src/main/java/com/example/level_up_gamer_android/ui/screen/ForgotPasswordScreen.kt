package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
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
fun ForgotPasswordScreen(navController: NavController, viewModel: FormularioViewModel) {
    var step by remember { mutableIntStateOf(1) } // 1: Email, 2: Code & New Pass
    var email by remember { mutableStateOf("") }
    var codigo by remember { mutableStateOf("") }
    var nuevaPass by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }
    val loading by viewModel.loading.collectAsState()
    val scope = rememberCoroutineScope()

    GradientSurface {
        Column(
            modifier = Modifier.fillMaxSize().padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
        ) {
            CustomText(
                text = "Recuperar Acceso",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )

            if (step == 1) {
                CustomText("Ingresa tu correo para recibir un código de seguridad.")
                CustomTextField(email, { email = it }, "Correo electrónico")
                
                CustomButton(
                    text = if (loading) "Enviando..." else "Enviar Código",
                    enabled = email.contains("@") && !loading,
                    onClick = {
                        viewModel.solicitarRecuperacion(email) { success, msg ->
                            mensaje = msg
                            if (success) step = 2
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            } else {
                CustomText("Ingresa el código de 6 dígitos y tu nueva contraseña.")
                CustomTextField(codigo, { if (it.length <= 6) codigo = it }, "Código de 6 dígitos")
                CustomTextField(nuevaPass, { nuevaPass = it }, "Nueva Contraseña", isPassword = true)
                
                CustomButton(
                    text = if (loading) "Restableciendo..." else "Cambiar Contraseña",
                    enabled = codigo.length == 6 && nuevaPass.length >= 6 && !loading,
                    onClick = {
                        viewModel.resetPassword(email, codigo, nuevaPass) { success, msg ->
                            mensaje = msg
                            if (success) {
                                scope.launch {
                                    delay(2000)
                                    navController.popBackStack()
                                }
                            }
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            if (mensaje.isNotEmpty()) {
                CustomText(text = mensaje, color = if (mensaje.contains("Éxito") || mensaje.contains("enviado")) Color.Green else Color.Yellow)
            }

            TextButton(onClick = { navController.popBackStack() }) {
                Text("Volver", color = Color.White)
            }
        }
    }
}
