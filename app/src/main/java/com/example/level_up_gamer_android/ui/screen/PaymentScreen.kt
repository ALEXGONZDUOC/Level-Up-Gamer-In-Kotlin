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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PaymentScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    direccion: String
) {
    // Variables de Estado de Entrada
    var cardDigits by remember { mutableStateOf("") }
    var nombreTitular by remember { mutableStateOf("") }
    var dateDigits by remember { mutableStateOf("") }
    var cvv by remember { mutableStateOf("") }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    // Estados para controlar si el usuario ya tocó los campos (Disparadores de error visual)
    var cardTouched by remember { mutableStateOf(false) }
    var nameTouched by remember { mutableStateOf(false) }
    var dateTouched by remember { mutableStateOf(false) }
    var cvvTouched by remember { mutableStateOf(false) }

    val loading by viewModel.loading.collectAsState()
    val total = viewModel.getTotal()

    // Paleta de Colores Cyberpunk Directos
    val neonCian = Color(0xFF00F0FF)
    val neonPurple = Color(0xFFBD00FF)
    val neonRed = Color(0xFFFF0055) // Rojo glitch / advertencia del sistema
    val neonGreen = Color(0xFF39FF14)
    val inputUnfocused = Color.White.copy(alpha = 0.3f)

    // Evaluaciones de error en tiempo real para activar el borde rojo
    val isCardError = cardTouched && cardDigits.length < 13
    val isNameError = nameTouched && nombreTitular.isBlank()
    val isDateError = dateTouched && dateDigits.length < 4
    val isCvvError = cvvTouched && cvv.length < 3

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent),
                    title = {
                        Text(
                            text = "TERMINAL DE PAGO //",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = neonPurple
                        )
                    },
                    navigationIcon = {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Atrás", tint = Color.White)
                        }
                    }
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .padding(padding)
                    .padding(16.dp)
                    .fillMaxSize(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tarjeta de Resumen del Pedido
                CustomCard(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        CustomText(
                            text = "TOTAL A PAGAR: $${total.format3()}",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = neonCian
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        CustomText(
                            text = "RED DE ENVÍO: $direccion",
                            fontSize = 14.sp,
                            color = Color.White.copy(alpha = 0.7f)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // CAMPO NÚMERO DE TARJETA (Se marca rojo si está incompleto)
                OutlinedTextField(
                    value = cardDigits,
                    onValueChange = {
                        cardDigits = it.filter { c -> c.isDigit() }.take(16)
                        cardTouched = true
                    },
                    label = { Text("NÚMERO DE TARJETA", fontWeight = FontWeight.Bold) },
                    visualTransformation = CardNumberTransformation(),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    isError = isCardError,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = neonCian,
                        unfocusedBorderColor = inputUnfocused,
                        focusedLabelColor = neonCian,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        cursorColor = neonCian,
                        errorBorderColor = neonRed,
                        errorLabelColor = neonRed
                    )
                )

                // CAMPO NOMBRE DEL TITULAR
                OutlinedTextField(
                    value = nombreTitular,
                    onValueChange = {
                        nombreTitular = it
                        nameTouched = true
                    },
                    label = { Text("NOMBRE DEL TITULAR", fontWeight = FontWeight.Bold) },
                    isError = isNameError,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.White,
                        focusedBorderColor = neonCian,
                        unfocusedBorderColor = inputUnfocused,
                        focusedLabelColor = neonCian,
                        unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                        cursorColor = neonCian,
                        errorBorderColor = neonRed,
                        errorLabelColor = neonRed
                    )
                )

                Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                    // CAMPO FECHA EXP (MM/AA)
                    OutlinedTextField(
                        value = dateDigits,
                        onValueChange = {
                            dateDigits = it.filter { c -> c.isDigit() }.take(4)
                            dateTouched = true
                        },
                        label = { Text("MM/AA", fontWeight = FontWeight.Bold) },
                        visualTransformation = DateTransformation(),
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isDateError,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = neonCian,
                            unfocusedBorderColor = inputUnfocused,
                            focusedLabelColor = neonCian,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = neonCian,
                            errorBorderColor = neonRed,
                            errorLabelColor = neonRed
                        )
                    )

                    // CAMPO CVV
                    OutlinedTextField(
                        value = cvv,
                        onValueChange = {
                            cvv = it.filter { c -> c.isDigit() }.take(3)
                            cvvTouched = true
                        },
                        label = { Text("CVV", fontWeight = FontWeight.Bold) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        isError = isCvvError,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedTextColor = Color.White,
                            unfocusedTextColor = Color.White,
                            focusedBorderColor = neonCian,
                            unfocusedBorderColor = inputUnfocused,
                            focusedLabelColor = neonCian,
                            unfocusedLabelColor = Color.White.copy(alpha = 0.6f),
                            cursorColor = neonCian,
                            errorBorderColor = neonRed,
                            errorLabelColor = neonRed
                        )
                    )
                }

                // Mensaje de Error global de la Red
                errorMsg?.let {
                    CustomText(
                        text = "CRITICAL_ERROR // $it",
                        color = neonRed,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Botón de Acción Principal
                CustomButton(
                    text = if (loading) "PROCESANDO TRANSACCIÓN..." else "EJECUTAR PAGO",
                    onClick = {
                        // Forzamos el encendido de todas las alertas si intentan presionar sin rellenar
                        cardTouched = true
                        nameTouched = true
                        dateTouched = true
                        cvvTouched = true

                        val isValid = cardDigits.length >= 13 &&
                                nombreTitular.isNotBlank() &&
                                dateDigits.length == 4 &&
                                cvv.length == 3

                        if (!isValid) {
                            errorMsg = "DATOS DE TARJETA INVÁLIDOS"
                            return@CustomButton
                        }
                        errorMsg = null
                        viewModel.finalizarPedido(direccion) { orderNum ->
                            if (orderNum != null) {
                                navController.navigate("order_confirmation/$orderNum") {
                                    popUpTo("cart") { inclusive = true }
                                }
                            } else {
                                errorMsg = "FALLO EN EL NÚCLEO DE PAGO"
                            }
                        }
                    },
                    modifier = Modifier.padding(bottom = 40.dp), // Ajustado para evitar colisiones visuales
                    enabled = !loading
                )
            }
        }
    }
}