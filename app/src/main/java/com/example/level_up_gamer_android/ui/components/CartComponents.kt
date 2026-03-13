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
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
            CartItemImage(
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
                    fontSize = 16.sp
                )
                CustomText(
                    text = "$${producto.precio}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                CustomText(
                    text = "Subtotal: $${String.format("%.2f", producto.precio * quantity)}",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            // Botones
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton(onClick = onDecrease) {
                    Icon(
                        imageVector = Icons.Default.Remove,
                        contentDescription = "Disminuir cantidad"
                    )
                }
                CustomText(
                    text = quantity.toString(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = onIncrease) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Aumentar cantidad"
                    )
                }
                IconButton(onClick = onRemove) {
                    Icon(
                        imageVector = Icons.Default.ShoppingCart,
                        contentDescription = "Eliminar del carrito",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
fun CartItemImage(producto: Producto, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val localImageRes = getLocalImageResource(context, producto.codigo)

    when {
        localImageRes != 0 && localImageRes != R.drawable.product_placeholder ->
            Image(
                painter = painterResource(id = localImageRes),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )

        producto.imagenUrl.isNotEmpty() ->
            AsyncImage(
                model = ImageRequest.Builder(context)
                    .data(producto.imagenUrl)
                    .crossfade(true)
                    .build(),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = modifier,
                contentScale = ContentScale.Crop,
                error = painterResource(id = R.drawable.product_placeholder)
            )

        else ->
            Image(
                painter = painterResource(id = R.drawable.product_placeholder),
                contentDescription = "Imagen de placeholder",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
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
        Icon(
            imageVector = Icons.Default.ShoppingCart,
            contentDescription = "Carrito vacío",
            modifier = Modifier.size(64.dp),
            tint = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Tu carrito está vacío",
            style = MaterialTheme.typography.headlineSmall,
            color = MaterialTheme.colorScheme.outline
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Agrega productos desde el catálogo",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.outline
        )
    }
}
