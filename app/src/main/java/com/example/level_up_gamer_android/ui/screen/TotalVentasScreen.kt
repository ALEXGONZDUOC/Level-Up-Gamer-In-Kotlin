package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TotalVentasScreen(navController: NavController, viewModel: FormularioViewModel) {
    val top5 by viewModel.topProductos.collectAsState()
    val ventasTotales by viewModel.ventasTotales.collectAsState()
    val topUsuario by viewModel.usuarioTopVentas.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarUsuarios() // Para tener los nombres
        viewModel.cargarTopProductos()
        viewModel.cargarPedidos() // Esto dispara el cálculo del usuario top
        viewModel.cargarVentasTotales("dia")
        viewModel.cargarVentasTotales("semana")
        viewModel.cargarVentasTotales("mes")
    }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = { CustomText("Estadísticas de Ventas") },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(modifier = Modifier.fillMaxSize().padding(padding).padding(16.dp).verticalScroll(rememberScrollState())) {
                // RESUMEN ARRIBA
                CustomText("Resumen de Ventas", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                VentaRow("Ventas del Día", ventasTotales["dia"] ?: 0.0)
                VentaRow("Ventas de la Semana", ventasTotales["semana"] ?: 0.0)
                VentaRow("Ventas del Mes", ventasTotales["mes"] ?: 0.0)

                Spacer(modifier = Modifier.height(32.dp))

                // TOP USUARIO
                topUsuario?.let { user ->
                    CustomText("Cliente con más compras", style = MaterialTheme.typography.headlineSmall)
                    Spacer(modifier = Modifier.height(16.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.2f)),
                        border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary)
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(user.nombre, fontWeight = FontWeight.Bold, fontSize = 20.sp, color = Color.White)
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                                Text("Total Compras: ${user.totalCompras}", color = Color.LightGray)
                                Text("Valor Total: $${String.format("%.2f", user.valorTotal)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // RANKING COMPLETO
                CustomText("Ranking de Productos", style = MaterialTheme.typography.headlineSmall)
                Spacer(modifier = Modifier.height(16.dp))
                top5.forEachIndexed { index, producto ->
                    ProductoCardSimplified(index + 1, producto)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ProductoCardSimplified(rank: Int, producto: Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.3f))
    ) {
        Row(modifier = Modifier.padding(16.dp), verticalAlignment = Alignment.CenterVertically) {
            Text("#$rank", fontSize = 24.sp, fontWeight = FontWeight.Bold, color = Color.White)
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(producto.nombre, fontWeight = FontWeight.Bold, color = Color.White)
                Text("Precio: $${producto.precio}", color = Color.LightGray)
            }
            Text("Vendidos: ${producto.total_vendido}", color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun VentaRow(label: String, total: Double) {
    Row(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = Color.White)
        Text("$${String.format("%.2f", total)}", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.primary)
    }
}
