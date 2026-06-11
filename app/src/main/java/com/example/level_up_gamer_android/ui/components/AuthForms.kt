package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
// Asegúrate de cambiar esta importación por la ruta real de tu AppStyles si está en otro paquete
import com.example.level_up_gamer_android.ui.theme.AppStyles
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun LoginForm(
    viewModel: FormularioViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit,
    onForgotPasswordClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    val errorMsg by viewModel.error.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            color = AppStyles.Cards.BorderColor // Violeta Neón para los títulos principales
        )

        CustomTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = "Nombre de usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = "Contraseña",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        errorMsg?.let {
            CustomText(
                text = it,
                color = Color(0xFFFF0055), // Un rojo/rosa neón Cyberpunk para los errores
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (isLoading) {
            // El indicador de carga ahora usa el Cyan de los botones
            CircularProgressIndicator(color = Color(0xFF00E5FF))
        } else {
            CustomButton(
                text = "Iniciar Sesión",
                onClick = {
                    isLoading = true
                    viewModel.login(nombre, contrasena) { success ->
                        isLoading = false
                        if (success) {
                            onLoginSuccess()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onForgotPasswordClick) {
            // Cyan Neón para que resalte como enlace interactivo secundario
            CustomText("¿Olvidaste tu contraseña?", color = Color(0xFF00E5FF))
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegisterClick) {
            // Texto de registro sutil utilizando la opacidad unfocused de tus inputs
            CustomText(
                text = "¿No tienes una cuenta? Regístrate",
                color = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha)
            )
        }
    }
}

@Composable
fun RegistroForm(viewModel: FormularioViewModel, onRegisterSuccess: (String) -> Unit) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    val mensajeRegistro by viewModel.error.collectAsState()
    val isLoading by viewModel.loading.collectAsState()

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            color = AppStyles.Cards.BorderColor // Violeta Neón
        )

        CustomTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = "Ingrese nombre de usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Ingrese su correo electrónico",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = "Ingrese su contraseña",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (isLoading) {
            CircularProgressIndicator(color = Color(0xFF00E5FF))
        } else {
            CustomButton(
                text = "Registrar",
                onClick = {
                    if (nombre.isNotBlank() && contrasena.isNotBlank() && email.isNotBlank()) {
                        viewModel.agregarUsuario(nombre, contrasena, email) { error ->
                            if (error == null) {
                                onRegisterSuccess(email)
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        mensajeRegistro?.let {
            CustomText(
                text = it,
                color = Color(0xFFFF0055), // Error en tono Cyberpunk
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(top = 24.dp)
            )
        }
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
    val userToEdit = if (targetUserId != null) {
        viewModel.getUsuarioById(targetUserId)
    } else {
        currentUser
    }

    val mensaje by viewModel.error.collectAsState()

    var nombre by remember(userToEdit) { mutableStateOf(userToEdit?.nombre ?: "") }
    var email by remember(userToEdit) { mutableStateOf(userToEdit?.email ?: "") }
    var contrasena by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        Text(
            text = if (targetUserId != null) "Editar Usuario: ${userToEdit?.nombre}" else "Actualizar Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp),
            color = AppStyles.Cards.BorderColor // Violeta Neón
        )

        CustomTextField(
            value = nombre,
            onValueChange = { nombre = it },
            label = "Nombre de usuario",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = email,
            onValueChange = { email = it },
            label = "Correo electrónico",
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        CustomTextField(
            value = contrasena,
            onValueChange = { contrasena = it },
            label = "Nueva contraseña (opcional)",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        mensaje?.let {
            CustomText(
                text = it,
                color = Color(0xFFFF0055),
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        CustomButton(
            text = "Actualizar",
            onClick = {
                userToEdit?.let { user ->
                    val updatedUser = user.copy(
                        nombre = nombre,
                        email = email,
                        contrasena = if (contrasena.isNotBlank()) contrasena else user.contrasena
                    )
                    viewModel.actualizarUsuario(updatedUser)
                    onUpdateSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}