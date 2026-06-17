package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Delete // Cambiado para una mejor UX de eliminación
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.level_up_gamer_android.R
import com.example.level_up_gamer_android.model.Producto
// Asegúrate de verificar la ruta exacta de tus AppStyles
import com.example.level_up_gamer_android.ui.theme.AppStyles
import com.example.level_up_gamer_android.utils.format3
import com.example.level_up_gamer_android.utils.getLocalImageResource

@Composable
fun CartItemCard(
    producto: Producto,
    quantity: Int,
    onIncrease: () -> Unit,
    onDecrease: () -> Unit,
    onRemove: () -> Unit
) {
    CustomCard(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            ProductoImage(
                producto = producto,
                modifier = Modifier.size(60.dp)
            )

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 16.dp)
            ) {
                CustomText(
                    text = producto.nombre,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = Color.White
                )
                // Precio unitario: Cyan Neón vibrante
                CustomText(
                    text = "$${producto.precio.format3()}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color(0xFF00E5FF)
                )
                // Subtotal: Enfatizado con el Violeta Neón de las tarjetas/bordes
                CustomText(
                    text = "Subtotal: $${(producto.precio * quantity).format3()}",
                    style = MaterialTheme.typography.bodySmall,
                    color = AppStyles.Cards.BorderColor
                )
            }

            // Botones de control de cantidad
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onDecrease) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Disminuir cantidad",
                        tint = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha)
                    )
                }
                CustomText(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                IconButton(
                    onClick = onIncrease,
                    enabled = quantity < producto.cantidad
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Aumentar cantidad",
                        tint = if (quantity < producto.cantidad)
                            Color(0xFF00E5FF) // Cyan Neón si está habilitado
                        else
                            Color.White.copy(alpha = 0.3f) // Opaco si llegó al límite de stock
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.Delete, // Ícono de basurero más intuitivo
                        contentDescription = "Eliminar del carrito",
                        tint = Color(0xFFFF0055) // Rojo/Rosa eléctrico Cyberpunk
                    )
                }
            }
        }
    }
}


@Composable
fun EmptyCartView() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        // Icono de carrito vacío con el Violeta del Mod atenuado
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito vacío",
            modifier = Modifier.size(64.dp),
            tint = AppStyles.Cards.BorderColor.copy(alpha = 0.4f)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall,
            color = Color.White
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega productos desde el catálogo",
            style = MaterialTheme.typography.bodyMedium,
            color = Color.White.copy(alpha = AppStyles.Inputs.UnfocusedAlpha)
        )
    }
}