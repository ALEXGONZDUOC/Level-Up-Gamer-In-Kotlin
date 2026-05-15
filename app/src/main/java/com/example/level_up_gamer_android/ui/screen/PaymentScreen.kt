package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CreditCard
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

// ─────────────────────────────────────────────────────────────────────────────
//  TRANSFORMACIONES VISUALES
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

@Composable
fun PaymentScreen(navController: NavController, viewModel: FormularioViewModel, direccion: String) {
    var cardDigits by remember { mutableStateOf("") }
    var nombreTitular by remember { mutableStateOf("") }
    var dateDigits by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    
    val loading by viewModel.loading.collectAsState()
    val total = viewModel.getTotal()

    GradientSurface {
        Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            CustomText("Pago y Facturación", fontSize = 24.sp, fontWeight = FontWeight.Bold)
            
            CustomCard {
                Column(modifier = Modifier.padding(16.dp)) {
                    CustomText("Envío a:", fontWeight = FontWeight.Bold)
                    CustomText(direccion, style = MaterialTheme.typography.bodySmall)
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText("Total a Pagar:", fontWeight = FontWeight.Bold)
                    CustomText("$${String.format("%.2f", total)}", color = MaterialTheme.colorScheme.primary, fontSize = 20.sp)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = cardDigits,
                onValueChange = { cardDigits = it.filter { c -> c.isDigit() }.take(16) },
                label = { Text("Número de tarjeta") },
                leadingIcon = { Icon(Icons.Default.CreditCard, null) },
                visualTransformation = CardNumberTransformation(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            CustomTextField(nombreTitular, { nombreTitular = it }, "Nombre en la tarjeta")

            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = dateDigits,
                    onValueChange = { dateDigits = it.filter { c -> c.isDigit() }.take(4) },
                    label = { Text("MM/AA") },
                    visualTransformation = DateTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
                OutlinedTextField(
                    value = cvv,
                    onValueChange = { cvv = it.filter { c -> c.isDigit() }.take(3) },
                    label = { Text("CVV") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            CustomButton(
                text = if (loading) "Procesando Pedido..." else "Confirmar Pago",
                enabled = !loading,
                onClick = {
                    if (cardDigits.length >= 13 && nombreTitular.isNotBlank() && dateDigits.length == 4 && cvv.length == 3) {
                        viewModel.finalizarPedido(direccion) { orderId ->
                            if (orderId != null) {
                                navController.navigate("success/$orderId") {
                                    popUpTo("home") { inclusive = false }
                                }
                            }
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}
