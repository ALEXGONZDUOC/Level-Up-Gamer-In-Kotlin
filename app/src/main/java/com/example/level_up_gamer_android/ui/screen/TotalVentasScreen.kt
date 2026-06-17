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

    // Paleta de Colores Cyberpunk Fijos
    val neonCian = Color(0xFF00F0FF)
    val neonPurple = Color(0xFFBD00FF)
    val neonGreen = Color(0xFF39FF14)
    val borderAlpha = Color.White.copy(alpha = 0.15f)
    val textMuted = Color.White.copy(alpha = 0.6f)

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
                            text = "CENTRAL DE MONITOREO // VENTAS",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = neonPurple
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.Transparent)
                )
            }
        ) { padding ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState())
            ) {
                // --- SECCIÓN 1: RESUMEN FINANCIERO ---
                CustomText(
                    text = "RESUMEN DE LOGS //",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = neonCian
                )
                Spacer(modifier = Modifier.height(12.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF16162B).copy(alpha = 0.6f)),
                    border = BorderStroke(1.dp, borderAlpha)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        VentaRow("Ciclo Diario (Hoy)", ventasTotales["dia"] ?: 0.0, neonGreen)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = borderAlpha)
                        VentaRow("Ciclo Semanal", ventasTotales["semana"] ?: 0.0, neonCian)
                        HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = borderAlpha)
                        VentaRow("Ciclo Mensual", ventasTotales["mes"] ?: 0.0, neonPurple)
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- SECCIÓN 2: NETRUNNER / CLIENTE TOP ---
                topUsuario?.let { user ->
                    CustomText(
                        text = "NETRUNNER DESTACADO //",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = neonCian
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFF16162B).copy(alpha = 0.6f)),
                        border = BorderStroke(1.dp, neonPurple.copy(alpha = 0.4f))
                    ) {
                        Column(modifier = Modifier.padding(16.dp)) {
                            CustomText(
                                text = user.nombre.uppercase(),
                                fontWeight = FontWeight.Black,
                                fontSize = 18.sp,
                                color = Color.White
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                CustomText("Transacciones: ${user.totalCompras}", color = textMuted, fontSize = 14.sp)
                                CustomText(
                                    text = "$${user.valorTotal.format3()}",
                                    fontWeight = FontWeight.Black,
                                    fontSize = 16.sp,
                                    color = neonGreen
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                // --- SECCIÓN 3: RANKING DE HARDWARE ---
                CustomText(
                    text = "RANKING DE HARDWARE IMPLANTADO //",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    color = neonCian
                )
                Spacer(modifier = Modifier.height(12.dp))

                top5.forEachIndexed { index, producto ->
                    ProductoCardSimplified(index + 1, producto, neonCian, neonGreen, textMuted)
                    Spacer(modifier = Modifier.height(8.dp))
                }

                Spacer(modifier = Modifier.height(120.dp)) // Salto de resguardo para la AppBottomBar
            }
        }
    }
}

@Composable
fun ProductoCardSimplified(rank: Int, producto: Producto, rankColor: Color, unitsColor: Color, subtitleColor: Color) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16162B).copy(alpha = 0.4f)),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.08f))
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            CustomText(
                text = "#$rank",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                color = rankColor
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                CustomText(text = producto.nombre, fontWeight = FontWeight.Bold, color = Color.White)
                CustomText(text = "Costo: $${producto.precio.format3()}", color = subtitleColor, fontSize = 13.sp)
            }
            CustomText(
                text = "${producto.total_vendido} UNIDADES",
                color = unitsColor,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 12.sp
            )
        }
    }
}

@Composable
fun VentaRow(label: String, total: Double, valueColor: Color) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CustomText(text = label, color = Color.White, fontWeight = FontWeight.Medium)
        CustomText(text = "$${total.format3()}", fontWeight = FontWeight.Black, fontSize = 18.sp, color = valueColor)
    }
}