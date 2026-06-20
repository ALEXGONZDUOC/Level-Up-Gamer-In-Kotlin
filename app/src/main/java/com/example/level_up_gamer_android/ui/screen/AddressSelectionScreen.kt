package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Direccion
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddressSelectionScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    targetUserId: Int? = null
) {
    val direcciones by viewModel.direcciones.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isTargetingOther = targetUserId != null && targetUserId != currentUser?.id

    var selectedDireccion by remember { mutableStateOf<Direccion?>(null) }
    var showDialog by remember { mutableStateOf(false) }

    val santiago = LatLng(-33.4489, -70.6693)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiago, 12f)
    }

    LaunchedEffect(targetUserId) {
        viewModel.cargarDirecciones(targetUserId)
    }

    LaunchedEffect(selectedDireccion) {
        selectedDireccion?.let {
            val tieneCoords = it.latitud != 0.0 && it.longitud != 0.0
            val destino = if (tieneCoords) LatLng(it.latitud, it.longitud) else santiago
            val zoom = if (tieneCoords) 15f else 12f
            cameraPositionState.position = CameraPosition.fromLatLngZoom(destino, zoom)
        }
    }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = { 
                        CustomText(
                            text = if (isTargetingOther) "DIRECCIÓN USUARIO $targetUserId" else "DIRECCIÓN ENVÍO", 
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    }
                )
            }
        ) { padding ->
            Column(modifier = Modifier.padding(padding).fillMaxSize()) {
                Box(modifier = Modifier.height(250.dp).fillMaxWidth().padding(16.dp)) {
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        GoogleMap(modifier = Modifier.fillMaxSize(), cameraPositionState = cameraPositionState) {
                            selectedDireccion?.let {
                                Marker(state = MarkerState(LatLng(it.latitud, it.longitud)), title = it.nombre_etiqueta)
                            }
                        }
                    }
                }

                CustomText("Tus direcciones", modifier = Modifier.padding(horizontal = 16.dp), fontSize = 18.sp)

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    direcciones.forEach { dir ->
                        AddressChip(
                            direccion = dir,
                            isSelected = selectedDireccion?.id == dir.id,
                            onSelect = { selectedDireccion = dir }
                        )
                    }
                    if (!isTargetingOther) {
                        AddAddressChip { navController.navigate("add_address") }
                    }
                }

                // Botón "Marcar como principal" cuando hay una dirección seleccionada
                selectedDireccion?.let { dir ->
                    if (!isTargetingOther) {
                        val esPrincipal = dir.es_principal
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (esPrincipal) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                contentDescription = null,
                                tint = if (esPrincipal) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(6.dp))
                            if (esPrincipal) {
                                CustomText(
                                    text = "Dirección principal",
                                    fontSize = 14.sp,
                                    color = Color(0xFFFFD700)
                                )
                            } else {
                                TextButton(
                                    onClick = { viewModel.establecerPrincipal(dir.id) }
                                ) {
                                    CustomText(
                                        text = "Marcar como principal",
                                        fontSize = 14.sp,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.weight(1f))

                if (!isTargetingOther) {
                    CustomButton(
                        text = "Continuar",
                        onClick = {
                            selectedDireccion?.let { navController.navigate("payment/${it.calle}, ${it.ciudad}") }
                        },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 120.dp),
                        enabled = selectedDireccion != null
                    )
                } else {
                    CustomButton(
                        text = "Regresar",
                        onClick = { navController.popBackStack() },
                        modifier = Modifier.fillMaxWidth().padding(16.dp).padding(bottom = 120.dp)
                    )
                }
            }
        }
    }

    if (showDialog) {
        AddAddressDialog(
            onDismiss = { showDialog = false },
            onSave = { nueva ->
                val userId = targetUserId ?: currentUser?.id ?: 0
                viewModel.agregarDireccion(nueva.copy(usuario_id = userId, latitud = 0.0, longitud = 0.0))
                showDialog = false
            }
        )
    }
}

@Composable
fun AddressChip(direccion: Direccion, isSelected: Boolean, onSelect: () -> Unit) {
    Surface(
        onClick = onSelect,
        shape = MaterialTheme.shapes.medium,
        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
        border = if (isSelected) BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            if (direccion.es_principal) {
                Icon(
                    imageVector = Icons.Filled.Star,
                    contentDescription = "Principal",
                    tint = Color(0xFFFFD700),
                    modifier = Modifier.size(16.dp)
                )
                Spacer(Modifier.width(4.dp))
            }
            CustomText(direccion.nombre_etiqueta)
        }
    }
}

@Composable
fun AddAddressChip(onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = MaterialTheme.shapes.medium,
        color = MaterialTheme.colorScheme.surfaceVariant,
        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
    ) {
        Row(Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.Add, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
            Spacer(Modifier.width(4.dp))
            CustomText("Nueva")
        }
    }
}

@Composable
fun AddAddressDialog(onDismiss: () -> Unit, onSave: (Direccion) -> Unit) {
    var etiqueta by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Nueva Dirección") },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                CustomTextField(etiqueta, { etiqueta = it }, "Etiqueta (ej: Casa, Trabajo)")
                CustomTextField(calle, { calle = it }, "Calle y Número")
                CustomTextField(ciudad, { ciudad = it }, "Ciudad")
            }
        },
        confirmButton = {
            TextButton(
                onClick = {
                    if (etiqueta.isNotBlank() && calle.isNotBlank() && ciudad.isNotBlank()) {
                        onSave(Direccion(usuario_id = 0, nombre_etiqueta = etiqueta, calle = calle, ciudad = ciudad, latitud = 0.0, longitud = 0.0, es_principal = false))
                    }
                }
            ) { Text("Guardar") }
        },
        dismissButton = { TextButton(onClick = onDismiss) { Text("Cancelar") } }
    )
}
