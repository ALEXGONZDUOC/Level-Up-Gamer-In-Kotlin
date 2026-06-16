package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import com.example.level_up_gamer_android.ui.theme.AppStyles

@Composable
fun CustomTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    isPassword: Boolean = false,
    trailingIcon: @Composable (() -> Unit)? = null // 🌟 Agregamos el parámetro opcional para el ojito
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = {
            Text(
                text = label,
                color = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha)
            )
        },
        modifier = modifier,
        singleLine = true,
        shape = AppStyles.Inputs.Shape,
        visualTransformation = if (isPassword) PasswordVisualTransformation() else VisualTransformation.None,
        keyboardOptions = KeyboardOptions(
            keyboardType = if (isPassword) KeyboardType.Password else KeyboardType.Text
        ),
        trailingIcon = trailingIcon, // 🌟 Conectamos el ícono al OutlinedTextField nativo
        colors = OutlinedTextFieldDefaults.colors(
            // --- TEXTO ---
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha),

            // --- BORDES (Brillo Neón al enfocar) ---
            focusedBorderColor = Color(0xFF00E5FF), // Cyan Neón al escribir
            unfocusedBorderColor = AppStyles.Cards.BorderColor.copy(alpha = 0.3f), // Violeta apagado de fondo

            // --- ETIQUETAS (Labels) ---
            focusedLabelColor = Color(0xFF00E5FF), // La etiqueta cambia a Cyan al activarse
            unfocusedLabelColor = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha),

            // --- CURSOR Y CONTENEDOR ---
            cursorColor = Color(0xFF00E5FF), // Cursor Cyan sintonizado
            focusedContainerColor = AppStyles.Cards.BackgroundColor.copy(alpha = 0.5f), // Fondo semi-transparente de tarjeta
            unfocusedContainerColor = AppStyles.Cards.BackgroundColor.copy(alpha = 0.2f),

            // --- PLACEHOLDERS ---
            focusedPlaceholderColor = Color.White.copy(alpha = 0.5f),
            unfocusedPlaceholderColor = Color.White.copy(alpha = 0.3f)
        )
    )
}