package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.ui.components.*
import com.example.level_up_gamer_android.utils.getFullImageUrl
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductEditorScreen(
    navController: NavController,
    viewModel: FormularioViewModel,
    productId: Int? = null
) {
    val productoAEditar = remember(productId) {
        if (productId != null && productId != -1) {
            viewModel.productos.value.find { it.id == productId }
        } else {
            null
        }
    }

    var nombre by remember { mutableStateOf(productoAEditar?.nombre ?: "") }
    var codigo by remember { mutableStateOf(productoAEditar?.codigo?.toString() ?: "") }
    var categoria by remember { mutableStateOf(productoAEditar?.categoria ?: "") }
    var descripcion by remember { mutableStateOf(productoAEditar?.descripcion ?: "") }
    var precio by remember { mutableStateOf(productoAEditar?.precio?.toString() ?: "") }
    var cantidad by remember { mutableStateOf(productoAEditar?.cantidad?.toString() ?: "") }
    var imagenUrl by remember { mutableStateOf(productoAEditar?.imagenUrl ?: "") }
    
    val snackbarHostState = remember { SnackbarHostState() }
    val errorMsg by viewModel.error.collectAsState()

    LaunchedEffect(errorMsg) {
        errorMsg?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        GradientSurface {
            Scaffold(
                containerColor = Color.Transparent,
                topBar = {
                    TopAppBar(
                        title = { 
                            CustomText(
                                text = if (productoAEditar == null) "NUEVO PRODUCTO" else "EDITAR PRODUCTO",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.tertiary
                            ) 
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                    )
                },
                bottomBar = {
                    Box(modifier = Modifier.padding(16.dp).padding(bottom = 120.dp).navigationBarsPadding()) {
                        CustomButton(
                            text = if (productoAEditar == null) "Crear Producto" else "Guardar Cambios",
                            onClick = {
                                val p = Producto(
                                    id = productoAEditar?.id ?: 0,
                                    nombre = nombre,
                                    codigo = if (codigo.isBlank()) 0.0 else codigo.toDoubleOrNull() ?: 0.0,
                                    categoria = categoria,
                                    descripcion = descripcion,
                                    precio = if (precio.isBlank()) 0.0 else precio.toDoubleOrNull() ?: 0.0,
                                    cantidad = if (cantidad.isBlank()) 0 else cantidad.toIntOrNull() ?: 0,
                                    imagenUrl = imagenUrl,
                                    imagenLocal = productoAEditar?.imagenLocal ?: "product_placeholder"
                                )

                                if (productoAEditar == null) {
                                    viewModel.crearProducto(p)
                                } else {
                                    viewModel.editarProducto(p)
                                }
                                navController.popBackStack()
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            ) { padding ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(padding)
                        .padding(horizontal = 16.dp)
                        .imePadding()
                        .verticalScroll(rememberScrollState()),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CustomTextField(value = nombre, onValueChange = { nombre = it }, label = "Nombre del Producto")
                    CustomTextField(value = codigo, onValueChange = { codigo = it }, label = "Código (Numérico)")
                    CustomTextField(value = categoria, onValueChange = { categoria = it }, label = "Categoría")
                    CustomTextField(value = descripcion, onValueChange = { descripcion = it }, label = "Descripción", modifier = Modifier.height(100.dp))
                    CustomTextField(value = precio, onValueChange = { precio = it }, label = "Precio")
                    CustomTextField(value = cantidad, onValueChange = { cantidad = it }, label = "Stock (Cantidad)")
                    CustomTextField(value = imagenUrl, onValueChange = { imagenUrl = it }, label = "URL de Imagen (Opcional)")
                    
                    val previewUrl = getFullImageUrl(imagenUrl)
                    if (!previewUrl.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(Color.Gray.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            AsyncImage(
                                model = previewUrl,
                                contentDescription = "Vista previa",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(140.dp))
                }
            }
        }

        // NOTIFICACIONES ARRIBA
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp),
            snackbar = { GamerSnackbar(it) }
        )
    }
}
