package com.example.level_up_gamer_android.ui.screen

import android.location.Geocoder
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Direccion
import com.example.level_up_gamer_android.ui.components.CustomButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.CustomTextField
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.Locale

@Composable
fun AddAddressScreen(navController: NavController, viewModel: FormularioViewModel) {
    var nombreEtiqueta by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var referencias by remember { mutableStateOf("") }
    var busquedaTexto by remember { mutableStateOf("") }
    var geocodingError by remember { mutableStateOf<String?>(null) }
    var isGeocoding by remember { mutableStateOf(false) }

    val defaultLatLng = LatLng(-33.4489, -70.6693)
    var pinPosition by remember { mutableStateOf(defaultLatLng) }
    val markerState = rememberMarkerState(position = pinPosition)

    val cameraPositionState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(defaultLatLng, 13f)
    }

    // Sincroniza el pin cuando el marker es arrastrado
    LaunchedEffect(markerState.position) {
        pinPosition = markerState.position
    }

    val context = LocalContext.current
    val user by viewModel.currentUser.collectAsState()

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            CustomText(
                text = "NUEVA DIRECCIÓN",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // PASO 1: Buscador
            CustomText(
                text = "PASO 1: Busca tu dirección",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f)
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomTextField(
                    value = busquedaTexto,
                    onValueChange = { busquedaTexto = it },
                    label = "Ej: Av. Providencia 1234, Santiago",
                    modifier = Modifier.weight(1f)
                )
                IconButton(
                    onClick = {
                        if (busquedaTexto.isBlank() || isGeocoding) return@IconButton
                        isGeocoding = true
                        geocodingError = null
                    },
                    enabled = !isGeocoding
                ) {
                    if (isGeocoding) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            color = MaterialTheme.colorScheme.primary,
                            strokeWidth = 2.dp
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Buscar",
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            }

            // Geocodificación cuando isGeocoding cambia a true
            LaunchedEffect(isGeocoding) {
                if (!isGeocoding || busquedaTexto.isBlank()) return@LaunchedEffect
                try {
                    val resultados = withContext(Dispatchers.IO) {
                        @Suppress("DEPRECATION")
                        Geocoder(context, Locale.getDefault()).getFromLocationName(busquedaTexto, 1)
                    }
                    if (!resultados.isNullOrEmpty()) {
                        val r = resultados[0]
                        val nuevaPos = LatLng(r.latitude, r.longitude)
                        pinPosition = nuevaPos
                        markerState.position = nuevaPos
                        cameraPositionState.animate(CameraUpdateFactory.newLatLngZoom(nuevaPos, 16f))
                        if (calle.isBlank()) calle = r.thoroughfare ?: ""
                        if (ciudad.isBlank()) ciudad = r.locality ?: ""
                    } else {
                        geocodingError = "No se encontró la dirección. Ajusta el pin manualmente."
                    }
                } catch (e: Exception) {
                    geocodingError = "Error al buscar. Intenta de nuevo."
                } finally {
                    isGeocoding = false
                }
            }

            geocodingError?.let {
                Text(text = it, color = Color(0xFFFF0055), fontSize = 12.sp)
            }

            // PASO 2: Mapa interactivo
            CustomText(
                text = "PASO 2: Ajusta el pin en el mapa",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f)
            )
            Text(
                text = "Arrastra el marcador o mantén presionado el mapa para mover el pin.",
                fontSize = 11.sp,
                color = Color.White.copy(alpha = 0.5f)
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(280.dp)
            ) {
                GoogleMap(
                    modifier = Modifier.fillMaxSize(),
                    cameraPositionState = cameraPositionState,
                    onMapLongClick = { latLng ->
                        pinPosition = latLng
                        markerState.position = latLng
                    }
                ) {
                    Marker(
                        state = markerState,
                        title = if (calle.isNotBlank()) calle else "Mi dirección",
                        draggable = true
                    )
                }

                Surface(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp),
                    color = Color.Black.copy(alpha = 0.6f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = Color(0xFFFF0055),
                            modifier = Modifier.size(14.dp)
                        )
                        Text(
                            text = "%.4f, %.4f".format(pinPosition.latitude, pinPosition.longitude),
                            fontSize = 10.sp,
                            color = Color.White
                        )
                    }
                }
            }

            // PASO 3: Formulario
            CustomText(
                text = "PASO 3: Completa los datos",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White.copy(alpha = 0.7f)
            )

            CustomTextField(
                value = nombreEtiqueta,
                onValueChange = { nombreEtiqueta = it },
                label = "Nombre (Ej: Casa, Trabajo)"
            )
            CustomTextField(
                value = calle,
                onValueChange = { calle = it },
                label = "Calle y Número"
            )
            CustomTextField(
                value = ciudad,
                onValueChange = { ciudad = it },
                label = "Ciudad"
            )
            CustomTextField(
                value = referencias,
                onValueChange = { referencias = it },
                label = "Referencias (Opcional)"
            )

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Guardar Dirección",
                enabled = nombreEtiqueta.isNotBlank() && calle.isNotBlank() && ciudad.isNotBlank(),
                onClick = {
                    user?.let {
                        viewModel.agregarDireccion(
                            Direccion(
                                id = 0,
                                usuario_id = it.id,
                                nombre_etiqueta = nombreEtiqueta,
                                calle = calle,
                                ciudad = ciudad,
                                referencias = referencias,
                                latitud = pinPosition.latitude,
                                longitud = pinPosition.longitude,
                                es_principal = false
                            )
                        )
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            CustomButton(
                text = "Cancelar",
                onClick = { navController.popBackStack() },
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
