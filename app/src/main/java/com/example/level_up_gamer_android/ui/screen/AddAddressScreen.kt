package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Direccion
import com.example.level_up_gamer_android.ui.components.CustomButton
import com.example.level_up_gamer_android.ui.components.CustomTextField
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun AddAddressScreen(navController: NavController, viewModel: FormularioViewModel) {
    var nombreEtiqueta by remember { mutableStateOf("") }
    var calle by remember { mutableStateOf("") }
    var ciudad by remember { mutableStateOf("") }
    var referencias by remember { mutableStateOf("") }
    
    val user by viewModel.currentUser.collectAsState()

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Nueva Dirección",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.padding(bottom = 8.dp)
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

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = "Guardar Dirección",
                enabled = nombreEtiqueta.isNotBlank() && calle.isNotBlank() && ciudad.isNotBlank(),
                onClick = {
                    user?.let {
                        val nuevaDir = Direccion(
                            id = 0,
                            usuario_id = it.id,
                            nombre_etiqueta = nombreEtiqueta,
                            calle = calle,
                            ciudad = ciudad,
                            referencias = referencias,
                            latitud = -33.4489, // Default Santiago
                            longitud = -70.6693,
                            es_principal = false
                        )
                        viewModel.agregarDireccion(nuevaDir)
                        navController.popBackStack()
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
