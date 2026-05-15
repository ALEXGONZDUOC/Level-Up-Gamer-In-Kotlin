package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun LoginForm(
    viewModel: FormularioViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPassClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val loading by viewModel.loading.collectAsState()

    Column(
        modifier = Modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CustomText("Bienvenido Gamer", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(nombre, { nombre = it }, "Usuario")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(contrasena, { contrasena = it }, "Contraseña", isPassword = true)
        
        errorMsg?.let {
            Spacer(modifier = Modifier.height(8.dp))
            CustomText(it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = if (loading) "Entrando..." else "Iniciar Sesión",
            enabled = !loading && nombre.isNotBlank() && contrasena.isNotBlank(),
            onClick = {
                viewModel.login(nombre, contrasena) { success, error ->
                    if (success) onLoginSuccess()
                    else errorMsg = error ?: "Error al entrar"
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        TextButton(onClick = onForgotPassClick) {
            Text("¿Olvidaste tu contraseña?", color = Color.White.copy(alpha = 0.7f))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Text("¿No tienes cuenta?", color = Color.White)
            TextButton(onClick = onRegisterClick) {
                Text("Regístrate", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
        }
    }
}

@Composable
fun RegistroForm(
    viewModel: FormularioViewModel,
    onRegisterSuccess: (String) -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val loading by viewModel.loading.collectAsState()

    Column(
        modifier = Modifier.padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText("Crear Cuenta", fontSize = 28.sp, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(nombre, { nombre = it }, "Nombre de Usuario")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(email, { email = it }, "Correo Electrónico")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(contrasena, { contrasena = it }, "Contraseña", isPassword = true)

        errorMsg?.let {
            Spacer(modifier = Modifier.height(8.dp))
            CustomText(it, color = Color.Red, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = if (loading) "Registrando..." else "Registrarse",
            enabled = !loading && nombre.isNotBlank() && email.contains("@") && contrasena.length >= 6,
            onClick = {
                viewModel.agregarUsuario(nombre, contrasena, email) { error ->
                    if (error == null) onRegisterSuccess(email)
                    else errorMsg = error
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
fun UpdateProfileForm(
    viewModel: FormularioViewModel,
    onUpdateSuccess: () -> Unit,
    modifier: Modifier = Modifier,
    targetUserId: Int? = null
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val allUsers by viewModel.usuarios.collectAsState()
    
    val targetUser = if (targetUserId != null) {
        allUsers.find { it.id == targetUserId }
    } else {
        currentUser
    }

    var nombre by remember(targetUser) { mutableStateOf(targetUser?.nombre ?: "") }
    var email by remember(targetUser) { mutableStateOf(targetUser?.email ?: "") }
    var contrasena by remember { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText(
            text = if (targetUserId != null) "Editar Perfil (ID: ${targetUser?.id})" else "Mi Perfil",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(32.dp))

        CustomTextField(nombre, { nombre = it }, "Nombre")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(email, { email = it }, "Email")
        Spacer(modifier = Modifier.height(16.dp))
        CustomTextField(contrasena, { contrasena = it }, "Nueva Contraseña (opcional)", isPassword = true)

        if (mensaje.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            CustomText(mensaje, color = Color.Green)
        }

        Spacer(modifier = Modifier.height(32.dp))

        CustomButton(
            text = "Guardar Cambios",
            onClick = {
                targetUser?.let {
                    val updated = it.copy(
                        nombre = nombre,
                        email = email,
                        contrasena = if (contrasena.isNotBlank()) contrasena else it.contrasena
                    )
                    viewModel.actualizarUsuario(updated)
                    mensaje = "¡Actualizado!"
                    onUpdateSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        if (targetUserId == null) {
            Spacer(modifier = Modifier.weight(1f))
            
            // BOTÓN LOGOUT (Solo para el dueño de la cuenta)
            Button(
                onClick = { viewModel.logout() },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.7f)),
                modifier = Modifier.fillMaxWidth(),
                shape = MaterialTheme.shapes.medium
            ) {
                Icon(Icons.AutoMirrored.Filled.ExitToApp, null)
                Spacer(Modifier.width(8.dp))
                Text("Cerrar Sesión")
            }
        }
    }
}
