package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.utils.format3

@Composable
fun CartScreen(
    viewModel: FormularioViewModel,
    onBackToHome: () -> Unit,
    onCheckoutClick: () -> Unit,
    targetUserId: Int? = null
) {
    val cartMap by viewModel.cart.collectAsState()
    val cartItems = cartMap.toList()
    val total = cartItems.sumOf { (producto, qty) -> producto.precio * qty }

    // Paleta Cyberpunk Compartida
    val neonCian = Color(0xFF00F0FF)
    val neonPurple = Color(0xFFBD00FF)
    val neonRed = Color(0xFFFF0055)
    val neonGreen = Color(0xFF39FF14)
    val textMuted = Color.White.copy(alpha = 0.5f)

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 🧾 HEADER (Cyberpunk Look)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "CARRITO DE COMPRAS",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = neonPurple
                )
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                    tint = neonCian
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (targetUserId != null) {
                val targetUser = viewModel.getUsuarioById(targetUserId)
                CustomText(
                    text = "ACCESO PERMITIDO  Carrito de: ${targetUser?.nombre?.uppercase() ?: "USER_$targetUserId"}",
                    color = neonCian,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 120.dp)
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White.copy(alpha = 0.1f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomText(
                            text = "CARRITO VACIO",
                            fontSize = 16.sp,
                            color = textMuted,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { (producto, quantity) ->
                        CartItemCard(
                            producto = producto,
                            quantity = quantity,
                            onIncrease = { if (targetUserId == null) viewModel.addToCart(producto) },
                            onDecrease = { if (targetUserId == null) viewModel.removeFromCart(producto) },
                            onRemove = { if (targetUserId == null) viewModel.removeItemFromCart(producto) },
                            neonCian = neonCian,
                            neonPurple = neonPurple,
                            neonRed = neonRed,
                            textMuted = textMuted
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                CustomCard(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 140.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CustomText(
                                text = "TOTAL A PAGAR:",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = Color.White
                            )
                            CustomText(
                                text = "$${total.format3()}",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Black,
                                color = neonGreen // Resalta la caja final en verde radiactivo
                            )
                        }

                        if (targetUserId == null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(12.dp)
                            ) {
                                CustomButton(
                                    text = "VACIAR",
                                    onClick = { viewModel.clearCart() },
                                    modifier = Modifier.weight(1f)
                                )
                                CustomButton(
                                    text = "PROCESAR PAGO",
                                    onClick = onCheckoutClick,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

// ==========================================
// COMPONENTE INTERNO AJUSTADO CON MÁXIMA CLARIDAD
// ==========================================
@Composable
fun CartItemCard(
    producto: Producto,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit,
    neonCian: Color,
    neonPurple: Color,
    neonRed: Color,
    textMuted: Color
) {
    CustomCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp, vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Imagen del Producto (Contenedor Arcade)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .align(Alignment.CenterVertically)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = Color.White.copy(alpha = 0.05f)
                ) {}
            }

            // 2. Columna Central: Datos y Controles
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically)
            ) {
            CustomText(
                text = producto.nombre,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(2.dp))

            CustomText(
                text = "$${producto.precio.format3()}",
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium,
                color = textMuted
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Selectores de cantidad compactos [cite: 15, 20]
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.wrapContentWidth()
            ) {
                IconButton(
                    onClick = onDecrease,
                    modifier = Modifier.size(24.dp)
                ) {
                CustomText(text = "—", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
            }

                CustomText(
                    text = quantity.toString(),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                IconButton(
                    onClick = onIncrease,
                    modifier = Modifier.size(24.dp)
                ) {
                CustomText(text = "+", fontSize = 13.sp, fontWeight = FontWeight.Black, color = neonCian)
            }
            }
        }

            // 3. Columna Derecha: Basurero [Arriba] y Subtotal [Abajo]
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 2.dp)
            ) {
            // Tacho de basura en la esquina
            IconButton(
                onClick = onRemove,
                modifier = Modifier.size(28.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Eliminar",
                    tint = neonRed
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // INDICADORES DE SUBVALOR CORREGIDOS (Máxima iluminación neón)
            CustomText(
                text = "SUBTOTAL",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = textMuted // Color de apoyo discreto
            )
            CustomText(
                text = "$${(producto.precio * quantity).format3()}",
                fontSize = 15.sp, // Aumentado ligeramente para legibilidad
                fontWeight = FontWeight.Black,
                color = neonCian // ⚡ ¡Cian Eléctrico ultrabrillante! Ya no se pierde con el fondo.
            )
        }
        }
    }
}