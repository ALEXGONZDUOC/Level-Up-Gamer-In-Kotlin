package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.UpdateProfileForm
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import com.example.level_up_gamer_android.ui.components.GradientSurface

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

    GradientSurface {
        Scaffold(
            snackbarHost = { SnackbarHost(snackbarHostState) },
            containerColor = Color.Transparent
        ) { padding ->
            UpdateProfileForm(
                viewModel = viewModel,
                onUpdateSuccess = {
                    // Si es el propio perfil, tal vez no queremos salir? 
                    // Pero la navegación dice popBackStack.
                    // navController.popBackStack() 
                },
                modifier = Modifier.padding(padding),
                targetUserId = targetUserId
            )
        }
    }
}
