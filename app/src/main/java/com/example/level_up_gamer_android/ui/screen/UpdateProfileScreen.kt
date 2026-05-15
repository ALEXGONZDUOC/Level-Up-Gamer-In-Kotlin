package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.UpdateProfileForm
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    targetUserId: Int? = null
) {
    Scaffold { padding ->
        UpdateProfileForm(
            viewModel = viewModel,
            onUpdateSuccess = {
                // No necesitamos volver manualmente, el botón dinámico de la barra nos guiará
            },
            targetUserId = targetUserId,
            modifier = Modifier.padding(padding)
        )
    }
}
