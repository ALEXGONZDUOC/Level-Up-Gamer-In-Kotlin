package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.ui.components.CustomText

@Composable
fun SupervisorScreen(viewModel: FormularioViewModel, onLogout: () -> Unit) {
    val productos by viewModel.productos.collectAsState()
    val ventasTotales by viewModel.ventasTotales.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.cargarEstadisticas()
    }

    GradientSurface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                CustomText(
                    text = "Panel de Supervisión",
                    style = MaterialTheme.typography.headlineMedium
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Resumen de ventas
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Card(modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(8.dp)) {
                        CustomText("Día", style = MaterialTheme.typography.labelSmall)
                        CustomText("$${ventasTotales["dia"] ?: 0.0}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(8.dp)) {
                        CustomText("Semana", style = MaterialTheme.typography.labelSmall)
                        CustomText("$${ventasTotales["semana"] ?: 0.0}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                }
                Card(modifier = Modifier.weight(1f)) {
                    Column(Modifier.padding(8.dp)) {
                        CustomText("Mes", style = MaterialTheme.typography.labelSmall)
                        CustomText("$${ventasTotales["mes"] ?: 0.0}", fontWeight = androidx.compose.ui.text.font.FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            CustomText(
                text = "Inventario y Rendimiento",
                style = MaterialTheme.typography.titleLarge
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(productos) { producto ->
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(16.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                CustomText(text = producto.nombre, style = MaterialTheme.typography.bodyLarge)
                                CustomText(text = "Stock: ${producto.cantidad} | Vendidos: ${producto.total_vendido}", style = MaterialTheme.typography.bodySmall)
                            }
                            CustomText(
                                text = "$${producto.precio}",
                                style = MaterialTheme.typography.bodyLarge,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                }
            }
        }
    }
}
