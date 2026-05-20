package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.utils.format3

// ─────────────────────────────────────────────────────────────────────────────
//  TRANSFORMACIONES VISUALES (Portadas de V0.9 para estabilidad)
// ─────────────────────────────────────────────────────────────────────────────

class CardNumberTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = raw.chunked(4).joinToString(" ")
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int {
                val spaces = (offset - 1).coerceAtLeast(0) / 4
                return (offset + spaces).coerceAtMost(formatted.length)
            }
            override fun transformedToOriginal(offset: Int): Int {
                val spaces = offset / 5
                return (offset - spaces).coerceAtMost(raw.length)
            }
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

class DateTransformation : VisualTransformation {
    override fun filter(text: AnnotatedString): TransformedText {
        val raw = text.text
        val formatted = if (raw.length <= 2) raw else "${raw.take(2)}/${raw.drop(2)}"
        val offsetMapping = object : OffsetMapping {
            override fun originalToTransformed(offset: Int): Int = if (offset > 2) offset + 1 else offset
            override fun transformedToOriginal(offset: Int): Int = if (offset > 3) offset - 1 else offset
        }
        return TransformedText(AnnotatedString(formatted), offsetMapping)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    direccion: String
) {
    var cardDigits by remember { mutableStateOf("") }
    var nombreTitular by remember { mutableStateOf("") }
    var dateDigits by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    val loading by viewModel.loading.collectAsState()
    val total = viewModel.getTotal()

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = { 
                        Text(
                            text = "PAGO", 
                            style = MaterialTheme.typography.headlineMedium,
                            color = MaterialTheme.colorScheme.tertiary
                        ) 
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, tint = Color.White)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier.padding(padding).padding(16.dp).fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                CustomCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CustomText(
                            text = "Total a Pagar: $${total.format3()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomText(text = "Dirección: $direccion", style = MaterialTheme.typography.bodyMedium)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // CAMPO NÚMERO DE TARJETA (Usando Outlined para soportar VisualTransformation correctamente)
                OutlinedTextField(
                    value = cardDigits,
                    onValueChange = { cardDigits = it.filter { c -> c.isDigit() }.take(16) },
                    label = { Text("Número de tarjeta", color = Color.White.copy(alpha = 0.7f)) },
                    visualTransformation = CardNumberTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = MaterialTheme.colorScheme.primary,
                        unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                        focusedLabelColor = MaterialTheme.colorScheme.primary,
                        cursorColor = MaterialTheme.colorScheme.primary
                    )
                )

                CustomTextField(
                    value = nombreTitular,
                    onValueChange = { nombreTitular = it },
                    label = "Nombre del titular"
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    // CAMPO FECHA EXP
                    OutlinedTextField(
                        value = dateDigits,
                        onValueChange = { dateDigits = it.filter { c -> c.isDigit() }.take(4) },
                        label = { Text("MM/AA", color = Color.White.copy(alpha = 0.7f)) },
                        visualTransformation = DateTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                    
                    // CAMPO CVV
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(3) },
                        label = { Text("CVV", color = Color.White.copy(alpha = 0.7f)) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = Color.White.copy(alpha = 0.3f),
                            focusedLabelColor = MaterialTheme.colorScheme.primary,
                            cursorColor = MaterialTheme.colorScheme.primary
                        )
                    )
                }

                errorMsg?.let {
                    CustomText(text = it, color = MaterialTheme.colorScheme.error, modifier = Modifier.padding(top = 8.dp))
                }

                Spacer(modifier = Modifier.weight(1f))

                CustomButton(
                    text = if (loading) "Procesando..." else "Pagar Ahora",
                    onClick = {
                        val isValid = cardDigits.length >= 13 &&
                                    nombreTitular.isNotBlank() &&
                                    dateDigits.length == 4 &&
                                    cvv.length == 3

                        if (!isValid) {
                            errorMsg = "Completa correctamente los datos"
                            return@CustomButton
                        }
                        errorMsg = null
                        viewModel.finalizarPedido(direccion) { orderNum ->
                            if (orderNum != null) {
                                navController.navigate("order_confirmation/$orderNum") {
                                    popUpTo("cart") { inclusive = true }
                                }
                            } else {
                                errorMsg = "Error al procesar el pago"
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 120.dp),
                    enabled = !loading
                )
            }
        }
    }
}
