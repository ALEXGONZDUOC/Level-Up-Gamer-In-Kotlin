package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

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

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Título Visual V0.8 (Sin botón volver, ya está en la barra)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "Mi Carrito",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (targetUserId != null) {
                val targetUser = viewModel.getUsuarioById(targetUserId)
                CustomText(
                    text = "Viendo carrito de: ${targetUser?.nombre ?: "Usuario $targetUserId"}",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }

            // Carrito vacío
            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CustomText("Tu carrito está vacío", fontSize = 18.sp)
                }
            } else {
                // Lista del carrito
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(cartItems) { (producto, quantity) ->
                        CartItemCard(
                            producto = producto,
                            quantity = quantity,
                            onIncrease = { if (targetUserId == null) viewModel.addToCart(producto) },
                            onDecrease = { if (targetUserId == null) viewModel.removeFromCart(producto) },
                            onRemove = { if (targetUserId == null) viewModel.removeItemFromCart(producto) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Totales y acciones Visual V0.8
                CustomCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(text = "Subtotal", fontSize = 16.sp)
                            CustomText(text = "$${String.format("%.2f", total)}", fontSize = 16.sp)
                        }
                        
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f))
                        
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(text = "Total", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            CustomText(
                                text = "$${String.format("%.2f", total)}",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                        
                        if (targetUserId == null) {
                            Spacer(modifier = Modifier.height(16.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                CustomButton(
                                    text = "Vaciar",
                                    onClick = { viewModel.clearCart() },
                                    modifier = Modifier.weight(0.4f)
                                )
                                CustomButton(
                                    text = "Finalizar Compra",
                                    onClick = onCheckoutClick,
                                    modifier = Modifier.weight(0.6f)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
