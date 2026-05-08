package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun LoginForm(
    viewModel: FormularioViewModel,
    onLoginSuccess: () -> Unit,
    onRegisterClick: () -> Unit
) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var mensajeError by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        CustomText(
            text = "Inicio de Sesión",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
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

        if (mensajeError.isNotEmpty()) {
            CustomText(
                text = mensajeError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        CustomButton(
            text = "Iniciar Sesión",
            onClick = {
                viewModel.login(nombre, contrasena) { esValido ->
                    if (esValido) {
                        onLoginSuccess()
                    } else {
                        mensajeError = "Usuario o contraseña incorrectos"
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onRegisterClick) {
            CustomText("¿No tienes una cuenta? Regístrate")
        }
    }
}

@Composable
fun RegistroForm(viewModel: FormularioViewModel) {
    var nombre by remember { mutableStateOf("") }
    var contrasena by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var mensajeRegistro by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        CustomText(
            text = "Registro de Usuario",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
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
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton(
            text = "Registrar",
            onClick = {
                if (nombre.isNotBlank() && contrasena.isNotBlank() && email.isNotBlank()) {
                    viewModel.agregarUsuario(nombre, contrasena, email)
                    mensajeRegistro = "Usuario registrado correctamente"
                    nombre = ""
                    contrasena = ""
                    email = ""
                } else {
                    mensajeRegistro = "Por favor complete todos los campos"
                }
            },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (mensajeRegistro.isNotEmpty()) {
            CustomText(
                text = mensajeRegistro,
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
    targetUserId: Int? = null,
    modifier: Modifier = Modifier
) {
    val currentUser by viewModel.currentUser.collectAsState()
    val targetUser = targetUserId?.let { viewModel.getUsuarioById(it) } ?: currentUser
    
    var nombre by remember(targetUser) { mutableStateOf(targetUser?.nombre ?: "") }
    var email by remember(targetUser) { mutableStateOf(targetUser?.email ?: "") }
    var contrasena by remember(targetUser) { mutableStateOf("") }
    var mensaje by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .padding(32.dp)
    ) {
        CustomText(
            text = if (targetUserId != null) "Editar Perfil (Admin)" else "Actualizar Perfil",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 24.dp)
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
            label = "Nueva contraseña (dejar en blanco para no cambiar)",
            isPassword = true,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (mensaje.isNotEmpty()) {
            CustomText(
                text = mensaje,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(bottom = 16.dp)
            )
        }

        CustomButton(
            text = "Actualizar",
            onClick = {
                targetUser?.let { user ->
                    val updatedUser = user.copy(
                        nombre = nombre,
                        email = email,
                        contrasena = if (contrasena.isNotBlank()) contrasena else user.contrasena
                    )
                    viewModel.actualizarUsuario(updatedUser)
                    mensaje = "Perfil actualizado correctamente"
                    onUpdateSuccess()
                }
            },
            modifier = Modifier.fillMaxWidth()
        )
    }
}
