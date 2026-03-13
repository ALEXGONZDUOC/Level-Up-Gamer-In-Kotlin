package com.example.level_up_gamer_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester

@Composable
fun BotonLogOut(onLogout: () -> Unit, focusRequester: FocusRequester) {
    IconButton(
        onClick = onLogout,
        modifier = Modifier.focusRequester(focusRequester)
    ) {
        Icon(
            imageVector = Icons.AutoMirrored.Filled.ExitToApp,
            contentDescription = "Cerrar sesión"
        )
    }
}
