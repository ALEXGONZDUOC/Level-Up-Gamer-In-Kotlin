package com.example.level_up_gamer_android.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DireccionesScreen(
    navController: NavController,
    viewModel: FormularioViewModel
) {
    val direcciones by viewModel.direcciones.collectAsState()
    var expandedId by remember { mutableStateOf<Int?>(null) }
    var direccionEliminar by remember { mutableStateOf<Direccion?>(null) }
    var direccionEditar by remember { mutableStateOf<Direccion?>(null) }

    LaunchedEffect(Unit) { viewModel.cargarDirecciones() }

    // ── Diálogo EDITAR con switch de principal ───────────────────────────────
    direccionEditar?.let { dir ->
        var etiqueta    by remember(dir.id) { mutableStateOf(dir.nombre_etiqueta) }
        var calle       by remember(dir.id) { mutableStateOf(dir.calle) }
        var ciudad      by remember(dir.id) { mutableStateOf(dir.ciudad) }
        var referencias by remember(dir.id) { mutableStateOf(dir.referencias ?: "") }
        var esPrincipal by remember(dir.id) { mutableStateOf(dir.es_principal) }

        AlertDialog(
            onDismissRequest = { direccionEditar = null },
            containerColor = Color(0xFF1A1A2E),
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Edit, null, tint = MaterialTheme.colorScheme.tertiary, modifier = Modifier.size(20.dp))
                    Spacer(Modifier.width(8.dp))
                    CustomText("Editar dirección", fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.tertiary)
                }
            },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    CustomTextField(value = etiqueta, onValueChange = { etiqueta = it },
                        label = "Etiqueta (ej: Casa, Trabajo)", modifier = Modifier.fillMaxWidth())
                    CustomTextField(value = calle, onValueChange = { calle = it },
                        label = "Calle y número", modifier = Modifier.fillMaxWidth())
                    CustomTextField(value = ciudad, onValueChange = { ciudad = it },
                        label = "Ciudad", modifier = Modifier.fillMaxWidth())
                    CustomTextField(value = referencias, onValueChange = { referencias = it },
                        label = "Referencia (opcional)", modifier = Modifier.fillMaxWidth())

                    // Switch booleano: ¿es dirección principal?
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        color = if (esPrincipal) Color(0xFFFFD700).copy(alpha = 0.1f)
                        else Color.White.copy(alpha = 0.05f),
                        border = BorderStroke(
                            1.dp,
                            if (esPrincipal) Color(0xFFFFD700).copy(alpha = 0.6f)
                            else Color.White.copy(alpha = 0.15f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                if (esPrincipal) Icons.Default.Star else Icons.Outlined.StarOutline,
                                null,
                                tint = if (esPrincipal) Color(0xFFFFD700) else Color.White.copy(alpha = 0.4f),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                CustomText(
                                    "Dirección principal",
                                    fontWeight = FontWeight.Medium,
                                    fontSize = 14.sp,
                                    color = if (esPrincipal) Color(0xFFFFD700) else Color.White.copy(alpha = 0.7f)
                                )
                                CustomText(
                                    if (esPrincipal) "Se usará por defecto al pagar"
                                    else "No es la dirección por defecto",
                                    fontSize = 11.sp,
                                    color = Color.White.copy(alpha = 0.4f)
                                )
                            }
                            Switch(
                                checked = esPrincipal,
                                onCheckedChange = { esPrincipal = it },
                                colors = SwitchDefaults.colors(
                                    checkedThumbColor = Color(0xFFFFD700),
                                    checkedTrackColor = Color(0xFFFFD700).copy(alpha = 0.3f),
                                    uncheckedThumbColor = Color.White.copy(alpha = 0.5f),
                                    uncheckedTrackColor = Color.White.copy(alpha = 0.1f)
                                )
                            )
                        }
                    }
                }
            },
            confirmButton = {
                CustomButton(
                    text = "Guardar cambios",
                    enabled = etiqueta.isNotBlank() && calle.isNotBlank() && ciudad.isNotBlank(),
                    onClick = {
                        viewModel.actualizarDireccion(
                            dir.copy(
                                nombre_etiqueta = etiqueta.trim(),
                                calle = calle.trim(),
                                ciudad = ciudad.trim(),
                                referencias = referencias.trim(),
                                es_principal = esPrincipal
                            )
                        )
                        direccionEditar = null
                        expandedId = null
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { direccionEditar = null }) {
                    CustomText("Cancelar", color = Color.White.copy(alpha = 0.6f))
                }
            }
        )
    }

    // Diálogo confirmar eliminar
    direccionEliminar?.let { dir ->
        AlertDialog(
            onDismissRequest = { direccionEliminar = null },
            containerColor = Color(0xFF1A1A2E),
            title = {
                CustomText("Eliminar dirección", color = Color(0xFFFF4757), fontWeight = FontWeight.Bold)
            },
            text = { CustomText("¿Eliminar \"${dir.nombre_etiqueta}\"? Esta acción no se puede deshacer.") },
            confirmButton = {
                CustomButton(text = "Eliminar", onClick = {
                    viewModel.eliminarDireccion(dir.id)
                    if (expandedId == dir.id) expandedId = null
                    direccionEliminar = null
                })
            },
            dismissButton = {
                TextButton(onClick = { direccionEliminar = null }) {
                    CustomText("Cancelar", color = Color.White)
                }
            }
        )
    }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        CustomText(
                            "MIS DIRECCIONES", fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, null, tint = Color.White)
                        }
                    },
                    actions = {
                        IconButton(onClick = { navController.navigate("add_address") }) {
                            Icon(Icons.Default.AddLocation, "Agregar", tint = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                )
            }
        ) { padding ->
            if (direcciones.isEmpty()) {
                // Estado vacío
                Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(16.dp)) {
                        Icon(Icons.Default.LocationOff, null,
                            tint = Color.White.copy(alpha = 0.25f), modifier = Modifier.size(80.dp))
                        CustomText("No tienes direcciones guardadas",
                            color = Color.White.copy(alpha = 0.5f), fontSize = 16.sp)
                        CustomButton(
                            text = "Agregar dirección",
                            onClick = { navController.navigate("add_address") }
                        )
                    }
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize().padding(padding).padding(horizontal = 16.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    contentPadding = PaddingValues(top = 12.dp, bottom = 140.dp, start = 0.dp, end = 0.dp)
                ) {
                    items(direcciones, key = { it.id }) { dir ->
                        DireccionExpandibleCard(
                            direccion = dir,
                            isExpanded = expandedId == dir.id,
                            onToggle = { expandedId = if (expandedId == dir.id) null else dir.id },
                            onMarcarPrincipal = {
                                viewModel.establecerPrincipal(dir.id)
                                expandedId = null
                            },
                            onEditar = { direccionEditar = dir },
                            onEliminar = { direccionEliminar = dir }
                        )
                    }
                    // Botón agregar al final de la lista
                    item {
                        OutlinedButton(
                            onClick = { navController.navigate("add_address") },
                            modifier = Modifier.fillMaxWidth(),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.5f))
                        ) {
                            Icon(Icons.Default.Add, null, tint = MaterialTheme.colorScheme.tertiary)
                            Spacer(Modifier.width(8.dp))
                            Text("Agregar nueva dirección", color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                }
            }
        }
    }
}

// ── Tarjeta expandible ────────────────────────────────────────────────────────
@Composable
fun DireccionExpandibleCard(
    direccion: Direccion,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    onMarcarPrincipal: () -> Unit,
    onEditar: () -> Unit,
    onEliminar: () -> Unit
) {
    val tieneCoords = direccion.latitud != 0.0 && direccion.longitud != 0.0
    val ubicacion = if (tieneCoords) LatLng(direccion.latitud, direccion.longitud) else LatLng(-33.4489, -70.6693)

    val borderColor = when {
        direccion.es_principal -> Color(0xFFFFD700)
        isExpanded             -> MaterialTheme.colorScheme.primary
        else                   -> MaterialTheme.colorScheme.primary.copy(alpha = 0.25f)
    }

    Surface(
        shape = MaterialTheme.shapes.medium,
        color = Color(0xFF1A1A2E),
        border = BorderStroke(if (direccion.es_principal || isExpanded) 2.dp else 1.dp, borderColor),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column {
            // ── Fila principal (siempre visible, clickeable para expandir) ──
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onToggle() }
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.Home,
                    contentDescription = null,
                    tint = if (direccion.es_principal) Color(0xFFFFD700) else MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(Modifier.width(10.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        CustomText(
                            text = direccion.nombre_etiqueta,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = if (direccion.es_principal) Color(0xFFFFD700) else Color.White
                        )
                        if (direccion.es_principal) {
                            Spacer(Modifier.width(6.dp))
                            Surface(
                                shape = MaterialTheme.shapes.small,
                                color = Color(0xFFFFD700).copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(11.dp))
                                    Spacer(Modifier.width(3.dp))
                                    CustomText("Principal", fontSize = 10.sp, color = Color(0xFFFFD700))
                                }
                            }
                        }
                    }
                    CustomText(
                        text = "${direccion.calle}, ${direccion.ciudad}",
                        fontSize = 12.sp,
                        color = Color.White.copy(alpha = 0.55f)
                    )
                }
                Icon(
                    imageVector = if (isExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                    contentDescription = null,
                    tint = Color.White.copy(alpha = 0.5f),
                    modifier = Modifier.size(22.dp)
                )
            }

            // ── Detalle expandible ──────────────────────────────────────────
            AnimatedVisibility(
                visible = isExpanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 16.dp)) {
                    HorizontalDivider(color = Color.White.copy(alpha = 0.08f))
                    Spacer(Modifier.height(12.dp))

                    // Datos completos
                    InfoFila(Icons.Default.Place, "Calle", direccion.calle)
                    Spacer(Modifier.height(6.dp))
                    InfoFila(Icons.Default.LocationCity, "Ciudad", direccion.ciudad)
                    if (!direccion.referencias.isNullOrBlank()) {
                        Spacer(Modifier.height(6.dp))
                        InfoFila(Icons.Default.Info, "Referencia", direccion.referencias)
                    }
                    if (tieneCoords) {
                        Spacer(Modifier.height(6.dp))
                        InfoFila(Icons.Default.MyLocation, "Coordenadas",
                            "%.5f, %.5f".format(direccion.latitud, direccion.longitud))
                    }

                    Spacer(Modifier.height(12.dp))

                    // Mapa
                    val cameraState = rememberCameraPositionState {
                        position = CameraPosition.fromLatLngZoom(ubicacion, if (tieneCoords) 15f else 12f)
                    }
                    Surface(
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                        modifier = Modifier.fillMaxWidth().height(170.dp)
                    ) {
                        GoogleMap(
                            modifier = Modifier.fillMaxSize(),
                            cameraPositionState = cameraState,
                            uiSettings = MapUiSettings(
                                zoomControlsEnabled = false,
                                scrollGesturesEnabled = false,
                                tiltGesturesEnabled = false,
                                rotationGesturesEnabled = false
                            )
                        ) {
                            if (tieneCoords) {
                                Marker(state = MarkerState(ubicacion), title = direccion.nombre_etiqueta)
                            }
                        }
                    }
                    if (!tieneCoords) {
                        CustomText(
                            "Sin coordenadas exactas — mostrando Santiago de Chile",
                            fontSize = 11.sp,
                            color = Color.White.copy(alpha = 0.35f),
                            modifier = Modifier.padding(top = 4.dp)
                        )
                    }

                    Spacer(Modifier.height(14.dp))

                    // Botones de acción
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        // Editar
                        OutlinedButton(
                            onClick = onEditar,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f))
                        ) {
                            Icon(Icons.Default.Edit, null, modifier = Modifier.size(15.dp), tint = MaterialTheme.colorScheme.tertiary)
                            Spacer(Modifier.width(4.dp))
                            Text("Editar", fontSize = 12.sp, color = MaterialTheme.colorScheme.tertiary)
                        }
                    }
                    Spacer(Modifier.height(8.dp))
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                        if (!direccion.es_principal) {
                            OutlinedButton(
                                onClick = onMarcarPrincipal,
                                modifier = Modifier.weight(1f),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.8f))
                            ) {
                                Icon(Icons.Default.Star, null, modifier = Modifier.size(16.dp), tint = Color(0xFFFFD700))
                                Spacer(Modifier.width(4.dp))
                                Text("Marcar principal", fontSize = 12.sp, color = Color(0xFFFFD700))
                            }
                        } else {
                            Surface(
                                modifier = Modifier.weight(1f),
                                shape = MaterialTheme.shapes.small,
                                color = Color(0xFFFFD700).copy(alpha = 0.1f),
                                border = BorderStroke(1.dp, Color(0xFFFFD700).copy(alpha = 0.4f))
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(Icons.Default.Star, null, tint = Color(0xFFFFD700), modifier = Modifier.size(16.dp))
                                    Spacer(Modifier.width(4.dp))
                                    Text("Dirección principal", fontSize = 12.sp, color = Color(0xFFFFD700))
                                }
                            }
                        }

                        OutlinedButton(
                            onClick = onEliminar,
                            modifier = Modifier.weight(1f),
                            border = BorderStroke(1.dp, Color(0xFFFF4757).copy(alpha = 0.7f))
                        ) {
                            Icon(Icons.Default.Delete, null, modifier = Modifier.size(16.dp), tint = Color(0xFFFF4757))
                            Spacer(Modifier.width(4.dp))
                            Text("Eliminar", fontSize = 12.sp, color = Color(0xFFFF4757))
                        }
                    }
                }
            }
        }
    }
}

// ── Fila de info ──────────────────────────────────────────────────────────────
@Composable
fun InfoFila(icon: ImageVector, label: String, valor: String) {
    Row(verticalAlignment = Alignment.Top) {
        Icon(icon, null, tint = MaterialTheme.colorScheme.secondary, modifier = Modifier.size(17.dp).padding(top = 2.dp))
        Spacer(Modifier.width(8.dp))
        Column {
            CustomText(label, fontSize = 11.sp, color = Color.White.copy(alpha = 0.4f))
            CustomText(valor, fontSize = 14.sp, color = Color.White.copy(alpha = 0.88f))
        }
    }
}


