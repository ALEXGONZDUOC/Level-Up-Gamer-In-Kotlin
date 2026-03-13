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
    onBackToHome: () -> Unit
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

            // Título
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "Carrito de Compras",
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

            // Carrito vacío
            if (cartItems.isEmpty()) {
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
                            CustomButton(
                                text = "Vaciar Carrito",
                                onClick = { viewModel.clearCart() },
                                modifier = Modifier.weight(1f)
                            )
                            CustomButton(
                                text = "Comprar",
                                onClick = { /* TODO: Implementar checkout */ },
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