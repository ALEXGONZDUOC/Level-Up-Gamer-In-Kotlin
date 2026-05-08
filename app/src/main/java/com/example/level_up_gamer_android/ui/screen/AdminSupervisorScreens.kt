package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Usuario
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

/* =========================
   ADMIN DASHBOARD
========================= */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(navController: NavController) {
    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { CustomText("Panel de Administrador") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(24.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CustomButton(
                    text = "Ver Funcionamiento App",
                    onClick = { navController.navigate("home") },
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
                Spacer(modifier = Modifier.height(24.dp))
                CustomButton(
                    text = "Gestionar Usuarios",
                    onClick = { navController.navigate("admin_users") },
                    modifier = Modifier.fillMaxWidth().height(80.dp)
                )
            }
        }
    }
}

/* =========================
   USER MANAGEMENT (Simplified for V0.5.2)
========================= */

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
                    title = { CustomText("Gestión de Usuarios") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    }
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
                // Selector de Usuarios
                Box(modifier = Modifier.fillMaxWidth()) {
                    OutlinedButton(
                        onClick = { expandedList = true },
                        modifier = Modifier.fillMaxWidth(),
                        border = BorderStroke(1.dp, Color.White)
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

                selectedUser?.let { user ->
                    EditUserTypeForm(user, viewModel) { updatedUser ->
                        viewModel.actualizarUsuario(updatedUser)
                        selectedUser = null
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    // Botones para ver carrito e info (como pidió el usuario)
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
                }
            }
        }
    }
}

@Composable
fun EditUserTypeForm(user: Usuario, viewModel: FormularioViewModel, onSave: (Usuario) -> Unit) {
    var tipoUsuarioId by remember(user) { mutableStateOf(user.tipo_usuario_id) }
    var expandedType by remember { mutableStateOf(false) }

    val tipos = listOf("Admin", "Supervisor", "Usuario")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText("Modificando tipo de: ${user.nombre}", fontSize = 18.sp)
        Spacer(modifier = Modifier.height(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            OutlinedButton(
                onClick = { expandedType = true },
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(1.dp, Color.White)
            ) {
                Text(
                    "Tipo: ${tipos.getOrElse(tipoUsuarioId - 1) { "Desconocido" }}",
                    color = Color.White
                )
                Icon(Icons.Default.ArrowDropDown, contentDescription = null, tint = Color.White)
            }
            DropdownMenu(expanded = expandedType, onDismissRequest = { expandedType = false }) {
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

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton("Guardar Tipo", onClick = {
            onSave(user.copy(tipo_usuario_id = tipoUsuarioId))
        }, Modifier.fillMaxWidth())
    }
}
