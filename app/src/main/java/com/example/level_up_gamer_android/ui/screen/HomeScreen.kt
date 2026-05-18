package com.example.level_up_gamer_android.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer_android.ui.components.BotonLogOut
import com.example.level_up_gamer_android.ui.components.CustomFloatingActionButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.ui.components.ProductoCard
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FormularioViewModel = viewModel(),
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    onTotalSalesClick: () -> Unit, // Nuevo callback para Supervisor
    onProfileClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Int) -> Unit,
    onViewProductSales: (Int) -> Unit // Nuevo callback para detalle de producto
) {
    val productos by viewModel.productos.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()
    val tipoUsuarioId = currentUser?.tipo_usuario_id ?: 3 // Por defecto Usuario (Invitado)

    val snackbarHostState = remember { SnackbarHostState() }
    val focusRequesterLogout = remember { FocusRequester() }
    val focusRequesterCart = remember { FocusRequester() }

    // Manejo de errores mediante Snackbar
    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        containerColor = Color.Transparent, // El fondo lo da GradientSurface
    ) { paddingValues ->
        GradientSurface {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                // TÍTULO LIMPIO (Estilo V0.8)
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = "Level Up Gamer",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                CustomText(
                    text = "Ofertas Destacadas",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.SemiBold
                )

                Spacer(modifier = Modifier.height(12.dp))

                if (isLoading && productos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        itemsIndexed(productos) { index, producto ->
                            ProductoCard(
                                producto = producto,
                                tipoUsuarioId = tipoUsuarioId,
                                onAddToCart = {
                                    viewModel.addToCart(producto)
                                },
                                onEditClick = { onEditProductClick(producto.id) },
                                onDeleteClick = { viewModel.eliminarProducto(producto.id) },
                                onViewSales = { onViewProductSales(producto.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}
