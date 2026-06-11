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

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // 🧾 HEADER (Model Look)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "CARRITO",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.tertiary
                )
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = null,
                    modifier = Modifier.size(32.dp),
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            if (targetUserId != null) {
                val targetUser = viewModel.getUsuarioById(targetUserId)
                CustomText(
                    text = "Gestionando carrito de: ${targetUser?.nombre ?: "Usuario $targetUserId"}",
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(bottom = 16.dp),
                    style = MaterialTheme.typography.bodyMedium
                )
            }

            if (cartItems.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.padding(bottom = 120.dp) // Alineación centrada real evitando la barra
                    ) {
                        Icon(
                            Icons.Default.ShoppingCart,
                            contentDescription = null,
                            modifier = Modifier.size(80.dp),
                            tint = Color.White.copy(alpha = 0.2f)
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        CustomText(
                            text = "Tu carrito está vacío",
                            fontSize = 20.sp,
                            color = Color.White.copy(alpha = 0.5f),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            } else {
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

                CustomCard(
                    modifier = Modifier.fillMaxWidth().padding(bottom = 140.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            CustomText(text = "TOTAL A PAGAR:", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            CustomText(
                                text = "$${total.format3()}",
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
                                    modifier = Modifier.weight(1f)
                                )
                                CustomButton(
                                    text = "Comprar",
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
// COMPONENTE INTERNO CON BASURERO EN LA ESQUINA Y SUBTOTAL ABAJO
// ==========================================
@Composable
fun CartItemCard(
    producto: Producto,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
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
            verticalAlignment = Alignment.Top, // Alineación arriba para que la papelera quede en la esquina
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // 1. Imagen del Producto (Centrada verticalmente respecto a su espacio)
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

            // 2. Columna Central: Nombre, Precio Base y Controles Compactos
            Column(
                modifier = Modifier
                    .weight(1f)
                    .align(Alignment.CenterVertically) // Centrado vertical para equilibrar el diseño
            ) {
                CustomText(
                    text = producto.nombre,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White
                )

                CustomText(
                    text = "$${producto.precio.format3()}",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                // Controles de cantidad compactos colocados de manera inteligente abajo del nombre
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    modifier = Modifier.wrapContentWidth()
                ) {
                    IconButton(
                        onClick = onDecrease,
                        modifier = Modifier.size(24.dp)
                    ) {
                        CustomText(text = "—", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = Color.White)
                    }

                    CustomText(
                        text = quantity.toString(),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )

                    IconButton(
                        onClick = onIncrease,
                        modifier = Modifier.size(24.dp)
                    ) {
                        CustomText(text = "+", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                    }
                }
            }

            // 3. Columna Derecha: Basurero arriba y Subtotal abajo [Esquina Superior Derecha]
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .align(Alignment.Top)
                    .padding(top = 4.dp)
            ) {
                // Tacho de basura en la pura esquina superior
                IconButton(
                    onClick = onRemove,
                    modifier = Modifier.size(28.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Eliminar",
                        tint = Color(0xFFFF0055) // Color glitch fucsia neón
                    )
                }

                Spacer(modifier = Modifier.height(16.dp)) // Separación vertical limpia

                // Valor total calculado abajo del basurero
                CustomText(
                    text = "Subtotal:",
                    fontSize = 10.sp,
                    color = Color.White.copy(alpha = 0.5f)
                )
                CustomText(
                    text = "$${(producto.precio * quantity).format3()}",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.secondary // Tu tono morado neón
                )
            }
        }
    }
}