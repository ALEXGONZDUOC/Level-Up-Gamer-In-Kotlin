package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
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
    targetUserId: Int? = null
) {
    val cartMap by viewModel.cart.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    
    val isTargetingOtherUser = targetUserId != null && targetUserId != currentUser?.id
    val cartItems = if (isTargetingOtherUser) emptyList() else cartMap.toList()
    val total = cartItems.sumOf { (producto, qty) -> producto.precio * qty }

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {

            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = if (isTargetingOtherUser) "Carrito de Usuario ID: $targetUserId" else "Carrito de Compras",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Carrito",
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (isTargetingOtherUser) {
                Box(modifier = Modifier.weight(1f), contentAlignment = Alignment.Center) {
                    CustomText(
                        text = "Los carritos de otros usuarios no son persistentes en esta versión (V0.6).",
                        fontSize = 18.sp,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            } else if (cartItems.isEmpty()) {
                EmptyCartView()
            } else {
                // Lista del carrito
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.weight(1f)
                ) {
                    items(count = cartItems.size) { index ->
                        val (producto, quantity) = cartItems[index]
                        CartItemCard(
                            producto = producto,
                            quantity = quantity,
                            onIncrease = { viewModel.addToCart(producto) },
                            onDecrease = { viewModel.removeFromCart(producto) },
                            onRemove = { viewModel.removeItemFromCart(producto) }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Totales y acciones
                CustomCard(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        CustomText(
                            text = "Total: $${String.format("%.2f", total)}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            val context = androidx.compose.ui.platform.LocalContext.current
                            CustomButton(
                                text = "Vaciar Carrito",
                                onClick = { viewModel.clearCart() },
                                modifier = Modifier.weight(1f)
                            )
                            CustomButton(
                                text = "Comprar",
                                onClick = {
                                    viewModel.comprar(
                                        onSuccess = {
                                            android.widget.Toast.makeText(context, "Compra realizada con éxito", android.widget.Toast.LENGTH_SHORT).show()
                                        },
                                        onError = { error ->
                                            android.widget.Toast.makeText(context, error, android.widget.Toast.LENGTH_LONG).show()
                                        }
                                    )
                                },
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Botón volver
            CustomButton(
                text = "Volver al Catálogo",
                onClick = onBackToHome
            )
        }
    }
}