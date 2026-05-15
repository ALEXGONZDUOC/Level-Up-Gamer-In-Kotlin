package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.level_up_gamer_android.ui.components.RegistroForm
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun RegistroScreen(
    viewModel: FormularioViewModel,
    onRegisterSuccess: (String) -> Unit
) {
    GradientSurface {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            RegistroForm(
                viewModel = viewModel,
                onRegisterSuccess = onRegisterSuccess
            )
        }
    }
}
