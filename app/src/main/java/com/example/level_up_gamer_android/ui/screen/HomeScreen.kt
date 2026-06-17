package com.example.level_up_gamer_android.ui.screen

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
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
import com.example.level_up_gamer_android.ui.components.CustomFloatingActionButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.CustomTextField
import com.example.level_up_gamer_android.ui.components.GamerSnackbar
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.ui.components.ProductoCard
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun HomeScreen(
    viewModel: FormularioViewModel = viewModel(),
    onLogout: () -> Unit,
    onCartClick: () -> Unit,
    onTotalSalesClick: () -> Unit,
    onProfileClick: () -> Unit,
    onAddProductClick: () -> Unit,
    onEditProductClick: (Int) -> Unit,
    onViewProductSales: (Int) -> Unit
) {
    val productos by viewModel.filteredProductos.collectAsState()
    val searchText by viewModel.searchText.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoading by viewModel.loading.collectAsState()
    val errorMessage by viewModel.error.collectAsState()

    val tipoUsuarioId = currentUser?.tipo_usuario_id ?: 0 
    val snackbarHostState = remember { SnackbarHostState() }
    remember { FocusRequester() }

    LaunchedEffect(errorMessage) {
        errorMessage?.let {
            snackbarHostState.showSnackbar(it)
            viewModel.clearError()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->

            GradientSurface {
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .padding(16.dp)
                        .fillMaxSize()
                ) {
                    // HEADER CON ACENTO NEÓN (Match Mod)
                    Text(
                        text = "CATÁLOGO",
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(bottom = 16.dp),
                        color = MaterialTheme.colorScheme.tertiary // PurpleNeon
                    )

                    // 🔍 BUSCADOR GAMER
                    CustomTextField(
                        value = searchText,
                        onValueChange = { viewModel.onSearchTextChange(it) },
                        label = "Buscar productos...",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(bottom = 24.dp)
                    )

                    if (isLoading && productos.isEmpty()) {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
                        }
                    } else {
                        LazyColumn(
                            verticalArrangement = Arrangement.spacedBy(12.dp),
                            contentPadding = PaddingValues(bottom = 120.dp), // Espacio estándar para saltar la barra
                            modifier = Modifier.fillMaxSize()
                        ) {
                            itemsIndexed(productos) { index, producto ->
                                var visible by remember { mutableStateOf(false) }
                                LaunchedEffect(Unit) { visible = true }

                                AnimatedVisibility(
                                    visible = visible,
                                    enter = fadeIn() + slideInVertically(initialOffsetY = { 100 * (index + 1) })
                                ) {
                                    ProductoCard(
                                        producto = producto,
                                        tipoUsuarioId = tipoUsuarioId,
                                        onAddToCart = { viewModel.addToCart(producto) },
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

        // NOTIFICACIONES ARRIBA
        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 40.dp), // Debajo de la barra de estado
            snackbar = { GamerSnackbar(it) }
        )
    }
}
