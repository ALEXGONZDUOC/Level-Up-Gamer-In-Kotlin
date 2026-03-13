package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.UpdateProfileForm
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun UpdateProfileScreen(
    navController: NavController,
    viewModel: FormularioViewModel
) {
    Scaffold { padding ->
        UpdateProfileForm(
            viewModel = viewModel,
            onUpdateSuccess = {
                navController.popBackStack()
            },
            modifier = Modifier.padding(padding)
        )
    }
}
