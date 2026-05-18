package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Usuario
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserManagementScreen(navController: NavController, viewModel: FormularioViewModel) {
    val usuarios by viewModel.usuarios.collectAsState()
    var selectedUser by remember { mutableStateOf<Usuario?>(null) }
    var expandedList by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) { viewModel.cargarUsuarios() }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { CustomText("Admin: Gestión de Usuarios", fontSize = 24.sp, fontWeight = FontWeight.Bold) },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Selector de Usuario Estilo V0.8
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedList = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.White),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Text(
                            selectedUser?.let { "${it.nombre} (ID: ${it.id})" } ?: "Seleccionar Usuario",
                            color = Color.White
                        )
                        Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
                    }
                    DropdownMenu(
                        expanded = expandedList,
                        onDismissRequest = { expandedList = false },
                        modifier = Modifier.fillMaxWidth(0.9f)
                    ) {
                        usuarios.forEach { user ->
                            DropdownMenuItem(
                                text = { Text("${user.nombre} (ID: ${user.id})") },
                                onClick = {
                                    selectedUser = user
                                    expandedList = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // Formulario de edición Estilo V0.8
                selectedUser?.let { user ->
                    EditUserFormV08(user, viewModel) { updatedUser ->
                        viewModel.adminActualizarUsuario(updatedUser)
                        selectedUser = null
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Acciones de Supervisión Estilo V0.8 (Grid de botones)
                    CustomText("Acciones Administrativas", style = MaterialTheme.typography.titleMedium)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomButton(
                                text = "Ver Carrito",
                                onClick = { navController.navigate("cart?userId=${user.id}") },
                                modifier = Modifier.weight(1f)
                            )
                            CustomButton(
                                text = "Ver Perfil",
                                onClick = { navController.navigate("update_profile?userId=${user.id}") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomButton(
                                text = "Ver Direcciones",
                                onClick = { navController.navigate("address_selection?userId=${user.id}") },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.eliminarUsuario(user.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.7f)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar Cuenta Definitivamente")
                    }
                }
            }
        }
    }
}

@Composable
fun EditUserFormV08(user: Usuario, viewModel: FormularioViewModel, onSave: (Usuario) -> Unit) {
    var tipoUsuarioId by remember(user) { mutableIntStateOf(user.tipo_usuario_id) }
    var activo by remember(user) { mutableStateOf(user.activo) }
    var expandedType by remember { mutableStateOf(false) }

    // Jerarquía correcta: 1:Admin, 2:Supervisor, 3:Usuario
    val tipos = listOf("Admin", "Supervisor", "Usuario")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText("Gestionando cuenta: ${user.nombre}", style = MaterialTheme.typography.titleMedium)
        Spacer(modifier = Modifier.height(16.dp))

        // Selector de Rol
        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expandedType = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.White),
                shape = MaterialTheme.shapes.medium
            ) {
                Text(
                    "Rol: ${tipos.getOrElse(tipoUsuarioId - 1) { "Desconocido" }}",
                    color = Color.White
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
            }
            DropdownMenu(
                expanded = expandedType, 
                onDismissRequest = { expandedType = false },
                modifier = Modifier.fillMaxWidth(0.8f)
            ) {
                tipos.forEachIndexed { index, name ->
                    DropdownMenuItem(
                        text = { Text(name) },
                        onClick = {
                            tipoUsuarioId = index + 1
                            expandedType = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Estado de la cuenta
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomText("Estado: ${if (activo) "Cuenta Activa" else "Cuenta Desactivada"}")
            Switch(
                checked = activo,
                onCheckedChange = { activo = it },
                colors = SwitchDefaults.colors(checkedThumbColor = Color.Green)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton("Aplicar Cambios de Rol/Estado", onClick = {
            onSave(user.copy(tipo_usuario_id = tipoUsuarioId, activo = activo))
        }, Modifier.fillMaxWidth())
    }
}
