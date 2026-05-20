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
                    title = { 
                        CustomText(
                            "GESTIÓN USUARIOS", 
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        ) 
                    },
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

                selectedUser?.let { user ->
                    EditUserForm(user, viewModel) { updatedUser ->
                        viewModel.adminActualizarUsuario(updatedUser)
                        selectedUser = null
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))
                    
                    CustomText("Acciones Administrativas", style = MaterialTheme.typography.titleMedium, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomButton(
                                text = "Carrito",
                                onClick = { navController.navigate("cart?userId=${user.id}") },
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.ShoppingCart
                            )
                            CustomButton(
                                text = "Perfil",
                                onClick = { navController.navigate("update_profile?userId=${user.id}") },
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.Person
                            )
                        }
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            CustomButton(
                                text = "Direcciones",
                                onClick = { navController.navigate("address_selection?userId=${user.id}") },
                                modifier = Modifier.weight(1f),
                                icon = Icons.Default.LocationOn
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { viewModel.eliminarUsuario(user.id) },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFFF4757)),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Icon(Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text("Eliminar Cuenta Definitivamente", fontWeight = FontWeight.Bold)
                    }
                }
                Spacer(modifier = Modifier.height(120.dp)) // Espacio estándar para saltar la barra
            }
        }
    }
}

@Composable
fun EditUserForm(user: Usuario, viewModel: FormularioViewModel, onSave: (Usuario) -> Unit) {
    var tipoUsuarioId by remember(user) { mutableIntStateOf(user.tipo_usuario_id) }
    var activo by remember(user) { mutableStateOf(user.activo) }
    var expandedType by remember { mutableStateOf(false) }

    val tipos = listOf("Admin", "Supervisor", "Usuario")

    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CustomText("Gestionando cuenta: ${user.nombre}", style = MaterialTheme.typography.titleMedium, color = Color.White)
        Spacer(modifier = Modifier.height(16.dp))

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

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            CustomText("Estado: ${if (activo) "Activa" else "Desactivada"}", color = Color.White)
            Switch(
                checked = activo,
                onCheckedChange = { activo = it },
                colors = SwitchDefaults.colors(
                    checkedThumbColor = Color.Green,
                    checkedTrackColor = Color.Green.copy(alpha = 0.5f)
                )
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        CustomButton("Aplicar Cambios", onClick = {
            onSave(user.copy(tipo_usuario_id = tipoUsuarioId, activo = activo))
        }, Modifier.fillMaxWidth())
    }
}
