package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
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
fun ProductoCard(producto: Producto, onAddToCart: () -> Unit) {
    CustomCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth()
            ) {
                ProductoImage(
                    producto = producto,
                    modifier = Modifier
                        .size(100.dp)
                        .padding(end = 16.dp)
                )

                Column(
                    modifier = Modifier.weight(1f)
                ) {
                    CustomText(
                        text = producto.nombre,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CustomText(
                        text = "Código: ${producto.codigo}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    CustomText(
                        text = "Categoría: ${producto.categoria}",
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomText(
                            text = "Precio: $${producto.precio}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        CustomText(
                            text = "Stock: ${producto.cantidad}",
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            CustomButton(
                text = "Agregar al Carrito",
                onClick = onAddToCart
            )
        }
    }
}

@Composable
fun ProductoImage(producto: Producto, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fullImageUrl = com.example.level_up_gamer_android.utils.getFullImageUrl(producto.imagenUrl)

    if (!fullImageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .crossfade(true)
                .build(),
            contentDescription = "Imagen de ${producto.nombre}",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.product_placeholder)
        )
    } else {
        val localImageRes = getLocalImageResource(context, producto.codigo)
        if (localImageRes != 0 && localImageRes != R.drawable.product_placeholder) {
            Image(
                painter = painterResource(id = localImageRes),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        } else {
            Image(
                painter = painterResource(id = R.drawable.product_placeholder),
                contentDescription = "Imagen de ${producto.nombre}",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    }
}
