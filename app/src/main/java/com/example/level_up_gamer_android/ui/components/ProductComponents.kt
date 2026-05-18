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
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.example.level_up_gamer_android.R
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.utils.getLocalImageResource

@Composable
fun ProductoCard(
    producto: Producto, 
    tipoUsuarioId: Int,
    onAddToCart: () -> Unit,
    onEditClick: () -> Unit = {},
    onDeleteClick: () -> Unit = {},
    onViewSales: () -> Unit = {}
) {
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
                        val stockColor = if (producto.cantidad < 5) Color.Red else MaterialTheme.colorScheme.onSurface
                        Column(horizontalAlignment = Alignment.End) {
                            CustomText(
                                text = "Stock: ${producto.cantidad}",
                                style = MaterialTheme.typography.bodyMedium,
                                color = stockColor,
                                fontWeight = if (producto.cantidad < 5) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (tipoUsuarioId == 2) { // SOLO Supervisor tiene poderes sobre productos
                    CustomButton(
                        text = "Ventas",
                        onClick = onViewSales,
                        modifier = Modifier.weight(0.7f)
                    )
                    CustomButton(
                        text = "Editar",
                        onClick = onEditClick,
                        modifier = Modifier.weight(0.7f)
                    )
                    CustomButton(
                        text = "Eliminar",
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(0.7f)
                    )
                } else { // Usuario, Invitado o Admin (el Admin no gestiona productos aquí)
                    val hayStock = producto.cantidad > 0
                    CustomButton(
                        text = if (hayStock) "Agregar al Carrito" else "Agotado",
                        onClick = onAddToCart,
                        modifier = Modifier.weight(1f),
                        enabled = hayStock
                    )
                }
            }
        }
    }
}

@Composable
fun ProductoImage(producto: Producto, modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val fullImageUrl = com.example.level_up_gamer_android.utils.getFullImageUrl(producto.imagenUrl)
    
    android.util.Log.d("ProductoImage", "Producto: ${producto.nombre}, URL Final: $fullImageUrl")

    if (!fullImageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data(fullImageUrl)
                .crossfade(true)
                .listener(
                    onStart = { android.util.Log.d("Coil", "Iniciando carga: $fullImageUrl") },
                    onSuccess = { _, _ -> android.util.Log.d("Coil", "Carga exitosa: $fullImageUrl") },
                    onError = { _, result -> 
                        android.util.Log.e("Coil", "Error cargando $fullImageUrl: ${result.throwable.message}")
                        result.throwable.printStackTrace()
                    }
                )
                .build(),
            contentDescription = "Imagen de ${producto.nombre}",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.product_placeholder)
        )
    } 
    else {
        val localImageRes = getLocalImageResource(context, producto.codigo)
        if (localImageRes != 0 && localImageRes != R.drawable.product_placeholder) {
            Image(
                painter = painterResource(id = localImageRes),
                contentDescription = "Imagen local de ${producto.nombre}",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        } 
        else {
            Image(
                painter = painterResource(id = R.drawable.product_placeholder),
                contentDescription = "Sin imagen disponible",
                modifier = modifier,
                contentScale = ContentScale.Crop
            )
        }
    }
}
