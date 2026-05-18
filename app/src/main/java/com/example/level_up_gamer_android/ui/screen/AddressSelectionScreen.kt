package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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

@OptIn(ExperimentalMaterial3Api::class)
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
    
    LaunchedEffect(targetUserId) {
        viewModel.cargarDirecciones(targetUserId)
    }

    val santiago = LatLng(-33.4489, -70.6693)
    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(santiago, 12f)
    }

    LaunchedEffect(selectedDireccion) {
        selectedDireccion?.let {
            cameraPositionState.position = CameraPosition.fromLatLngZoom(LatLng(it.latitud, it.longitud), 15f)
        }
    }

    GradientSurface {
        Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
            // Cabecera Visual V0.8
            CustomText(
                text = if (isTargetingOther) "Direcciones: Usuario $targetUserId" else "Confirmar Entrega",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            // Mapa Visual V0.8
            Surface(
                modifier = Modifier.height(300.dp).fillMaxWidth(),
                shape = MaterialTheme.shapes.large,
                border = BorderStroke(2.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.5f))
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState
                ) {
                    selectedDireccion?.let {
                        Marker(
                            state = MarkerState(LatLng(it.latitud, it.longitud)),
                            title = it.nombre_etiqueta
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomText("Selecciona una dirección", fontSize = 16.sp, fontWeight = FontWeight.Medium)
            
            Spacer(modifier = Modifier.height(8.dp))

            // Selector Horizontal V0.8
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                item {
                    OutlinedButton(
                        onClick = { navController.navigate("add_address") },
                        modifier = Modifier.height(48.dp),
                        shape = MaterialTheme.shapes.medium,
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null)
                        Spacer(Modifier.width(4.dp))
                        Text("Nueva")
                    }
                }
                items(direcciones) { dir ->
                    Button(
                        onClick = { selectedDireccion = dir },
                        modifier = Modifier.height(48.dp),
                        colors = if (selectedDireccion?.id == dir.id) 
                            ButtonDefaults.buttonColors() 
                        else 
                            ButtonDefaults.filledTonalButtonColors(),
                        shape = MaterialTheme.shapes.medium
                    ) { 
                        Text(dir.nombre_etiqueta) 
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))
            
            // Acciones Finales V0.8
            if (!isTargetingOther) {
                val isAdmin = currentUser?.tipo_usuario_id == 1
                CustomButton(
                    text = if (isAdmin) "Flujo verificado (Admin)" 
                           else if (selectedDireccion != null) "Confirmar y Pagar" 
                           else "Seleccione Dirección", 
                    onClick = {
                        if (!isAdmin) {
                            navController.navigate("payment/${selectedDireccion?.calle}, ${selectedDireccion?.ciudad}")
                        }
                    }, 
                    enabled = selectedDireccion != null || isAdmin,
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )
            } else {
                CustomButton(
                    text = "Regresar", 
                    onClick = { navController.popBackStack() }, 
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                )
            }
        }
    }
}
