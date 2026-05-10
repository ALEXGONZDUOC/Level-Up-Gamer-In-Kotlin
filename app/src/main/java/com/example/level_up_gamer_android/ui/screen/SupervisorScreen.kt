package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Inventory
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.BotonLogOut
import androidx.compose.ui.focus.FocusRequester

@Composable
fun SupervisorScreen(viewModel: FormularioViewModel, onLogout: () -> Unit) {
    val productos by viewModel.productos.collectAsState()
    val topProductos by viewModel.topProductos.collectAsState()
    val ventasTotales by viewModel.ventasTotales.collectAsState()
    val focusRequester = remember { FocusRequester() }

    LaunchedEffect(Unit) {
        viewModel.cargarEstadisticas()
    }

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // Header con Icono (Inspiración V0.9)
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.BarChart, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    CustomText(
                        text = "Panel de Supervisor",
                        style = MaterialTheme.typography.headlineMedium
                    )
                }
                BotonLogOut(
                    onLogout = onLogout,
                    focusRequester = focusRequester
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Resumen de ventas con mejor diseño (V0.6 / V0.7 Evolution)
            CustomText("Resumen de Ventas", style = MaterialTheme.typography.titleMedium, color = Color.LightGray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                StatCard("Día", "$${ventasTotales["dia"] ?: 0.0}", Icons.Default.TrendingUp, Modifier.weight(1f))
                StatCard("Semana", "$${ventasTotales["semana"] ?: 0.0}", Icons.Default.TrendingUp, Modifier.weight(1f))
                StatCard("Mes", "$${ventasTotales["mes"] ?: 0.0}", Icons.Default.TrendingUp, Modifier.weight(1f))
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Inventario con Icono
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Inventory, contentDescription = null, tint = Color.LightGray, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                CustomText(
                    text = "Inventario y Rendimiento",
                    style = MaterialTheme.typography.titleLarge
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos) { producto ->
                    InventoryCard(producto)
                }
            }
        }
    }
}

@Composable
fun StatCard(label: String, value: String, icon: ImageVector, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(containerColor = Color.White.copy(alpha = 0.1f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.2f))
    ) {
        Column(Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                Spacer(modifier = Modifier.width(4.dp))
                CustomText(label, style = MaterialTheme.typography.labelSmall, color = Color.LightGray)
            }
            Spacer(modifier = Modifier.height(4.dp))
            CustomText(value, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}

@Composable
fun InventoryCard(producto: com.example.level_up_gamer_android.model.Producto) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = Color.White.copy(alpha = 0.05f)
        ),
        border = BorderStroke(0.5.dp, Color.White.copy(alpha = 0.1f))
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                CustomText(text = producto.nombre, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.SemiBold)
                Row(verticalAlignment = Alignment.CenterVertically) {
                    val stockColor = if (producto.cantidad < 5) Color.Red else Color.Green
                    Box(modifier = Modifier.size(8.dp).background(stockColor, shape = MaterialTheme.shapes.small))
                    Spacer(modifier = Modifier.width(6.dp))
                    CustomText(text = "Stock: ${producto.cantidad}", style = MaterialTheme.typography.bodySmall, color = Color.LightGray)
                    Spacer(modifier = Modifier.width(12.dp))
                    CustomText(text = "Vendidos: ${producto.total_vendido}", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.primary)
                }
            }
            CustomText(
                text = "$${producto.precio}",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
