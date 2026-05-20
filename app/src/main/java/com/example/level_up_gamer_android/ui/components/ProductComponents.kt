package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.EditNote
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
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
import com.example.level_up_gamer_android.utils.format3
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
                        fontSize = 20.sp,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))

                    CustomText(
                        text = "Categoría: ${producto.categoria}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    CustomText(
                        text = producto.descripcion,
                        style = MaterialTheme.typography.bodyMedium,
                        color = Color.White.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        CustomText(
                            text = "Precio: $${producto.precio.format3()}",
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.titleMedium
                        )
                        val stockColor = if (producto.cantidad < 5) Color.Red else Color.White
                        CustomText(
                            text = "Stock: ${producto.cantidad}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = stockColor,
                            fontWeight = if (producto.cantidad < 5) FontWeight.Bold else FontWeight.Normal
                        )
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
                        modifier = Modifier.weight(1f),
                        icon = Icons.Default.Assessment
                    )
                    CustomButton(
                        text = "Editar",
                        onClick = onEditClick,
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.EditNote
                    )
                    CustomButton(
                        text = "Eliminar",
                        onClick = onDeleteClick,
                        modifier = Modifier.weight(1f),
                        icon = Icons.Outlined.Delete
                    )
                } else {
                    val hayStock = producto.cantidad > 0
                    var count by remember { mutableIntStateOf(0) }
                    
                    CustomButton(
                        text = if (hayStock) "Agregar" else "Agotado",
                        onClick = {
                            if (hayStock) {
                                count++
                                onAddToCart()
                            }
                        },
                        modifier = Modifier.weight(1f),
                        enabled = hayStock,
                        icon = Icons.Default.ShoppingCart,
                        count = count
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
    
    if (!fullImageUrl.isNullOrEmpty()) {
        AsyncImage(
            model = ImageRequest.Builder(context).data(fullImageUrl).crossfade(true).build(),
            contentDescription = "Imagen de ${producto.nombre}",
            modifier = modifier,
            contentScale = ContentScale.Crop,
            error = painterResource(id = R.drawable.product_placeholder)
        )
    } 
    else {
        val localImageRes = getLocalImageResource(context, producto.codigo)
        if (localImageRes != 0) {
            Image(painter = painterResource(id = localImageRes), contentDescription = null, modifier = modifier, contentScale = ContentScale.Crop)
        } else {
            Image(painter = painterResource(id = R.drawable.product_placeholder), contentDescription = null, modifier = modifier, contentScale = ContentScale.Crop)
        }
    }
}
