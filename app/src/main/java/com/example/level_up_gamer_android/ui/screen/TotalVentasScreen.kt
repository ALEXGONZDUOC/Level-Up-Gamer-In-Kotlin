package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.utils.format3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalVentasScreen(navController: NavController, viewModel: FormularioViewModel) {
    val top5 by viewModel.topProductos.collectAsState()
    val ventasTotales by viewModel.ventasTotales.collectAsState()
    val topUsuario by viewModel.usuarioTopVentas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios()
        viewModel.cargarTopProductos()
        viewModel.cargarPedidos()
        viewModel.cargarVentasTotales("dia")
        viewModel.cargarVentasTotales("semana")
        viewModel.cargarVentasTotales("mes")
    }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { 
                        CustomText(
                            text = "ESTADÍSTICAS VENTAS", 
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.tertiary
                        ) 
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                CustomText("RESUMEN DE VENTAS", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                VentaRow("Hoy", ventasTotales["dia"] ?: 0.0)
                VentaRow("Esta Semana", ventasTotales["semana"] ?: 0.0)
                VentaRow("Este Mes", ventasTotales["mes"] ?: 0.0)

                Spacer(modifier = Modifier.height(32.dp))

                topUsuario?.let { user ->
                    CustomText("CLIENTE TOP", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.1f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            CustomText(user.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                CustomText("Total Compras: ${user.totalCompras}", color = Color.LightGray)
                                CustomText("Valor Total: $${user.valorTotal.format3()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                CustomText("RANKING DE PRODUCTOS", style = MaterialTheme.typography.titleLarge, color = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.height(16.dp))
                top5.forEachIndexed { index, producto ->
                    ProductoCardSimplified(index + 1, producto)
                    Spacer(modifier = Modifier.height(8.dp))
                }
                Spacer(modifier = Modifier.height(120.dp)) // Espacio estándar para saltar la barra
            }
        }
    }
}

@Composable
fun ProductoCardSimplified(rank: Int, producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.05f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            CustomText("#$rank", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.tertiary)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                CustomText(producto.nombre, fontWeight = FontWeight.Bold, color = Color.White)
                CustomText("Precio: $${producto.precio.format3()}", color = Color.LightGray)
            }
            CustomText("${producto.total_vendido} VENDIDOS", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
    }
}

@Composable
fun VentaRow(label: String, total: Double) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        CustomText(label, color = Color.White)
        CustomText("$${total.format3()}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
