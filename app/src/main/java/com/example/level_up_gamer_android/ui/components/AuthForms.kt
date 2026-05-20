package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
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
            color = MaterialTheme.colorScheme.tertiary
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
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        if (isLoading) {
            CircularProgressIndicator()
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
            CustomText("¿Olvidaste tu contraseña?", color = MaterialTheme.colorScheme.primary)
        }

        Spacer(modifier = Modifier.height(8.dp))

        TextButton(onClick = onRegisterClick) {
            CustomText("¿No tienes una cuenta? Regístrate")
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
            color = MaterialTheme.colorScheme.tertiary
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
            CircularProgressIndicator()
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
            color = MaterialTheme.colorScheme.tertiary
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
