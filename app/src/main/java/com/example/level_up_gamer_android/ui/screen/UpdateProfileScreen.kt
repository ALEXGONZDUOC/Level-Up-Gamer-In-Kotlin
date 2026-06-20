package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    targetUserId: Int? = null
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMsg by viewModel.error.collectAsState()

    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GradientSurface {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { 
                            CustomText(
                                text = if (targetUserId != null) "EDITAR USUARIO" else "MI PERFIL",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            ) 
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )                }
            ) { padding ->
                Column(modifier = Modifier.padding(padding)) {
                    UpdateProfileForm(
                        viewModel = viewModel,
                        onUpdateSuccess = { },
                        targetUserId = targetUserId
                    )
                    // Botón Mis Direcciones — solo visible para el propio perfil
                    if (targetUserId == null) {
                        OutlinedButton(
                            onClick = { navController.navigate("direcciones") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 32.dp, vertical = 8.dp),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.tertiary.copy(alpha = 0.7f))
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.tertiary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            CustomText(
                                text = "Mis Direcciones",
                                color = MaterialTheme.colorScheme.tertiary,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }
            }
        }

        // NOTIFICACIONES ARRIBA
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
            snackbar = { GamerSnackbar(it) }
        )
    }
}
