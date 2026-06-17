package com.example.level_up_gamer_android.ui.screen

import androidx.compose.animation.*
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.level_up_gamer_android.model.Pedidos
import com.example.level_up_gamer_android.ui.components.CustomCard
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.utils.format3

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserOrdersScreen(navController: NavController, viewModel: FormularioViewModel) {
    val pedidos by viewModel.pedidos.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()

    // Paleta de Colores Cyberpunk Fijos
    val neonCian = Color(0xFF00F0FF)
    val neonPurple = Color(0xFFBD00FF)
    val neonGreen = Color(0xFF39FF14)
    val textMuted = Color.White.copy(alpha = 0.5f)

    // Filtrar pedidos solo para el usuario actual
    val misPedidos = remember(pedidos, currentUser) {
        pedidos.filter { it.usuario_id == currentUser?.id }
    }

    LaunchedEffect(Unit) {
        viewModel.cargarPedidos()
    }

    GradientSurface {
        Scaffold(
            containerColor = Color.Transparent,
            topBar = {
                TopAppBar(
                    title = {
                        Text(
                            text = "REGISTRO DE MISIONES // PEDIDOS",
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
                    .padding(horizontal = 16.dp)
            ) {
                if (misPedidos.isEmpty()) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = null,
                                modifier = Modifier.size(64.dp),
                                tint = Color.White.copy(alpha = 0.1f)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            CustomText("LOG // No se encontraron registros en la red.", color = textMuted)
                        }
                    }
                } else {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(14.dp),
                        contentPadding = PaddingValues(bottom = 120.dp)
                    ) {
                        items(misPedidos) { pedido ->
                            OrderExpandableCard(pedido, neonCian, neonPurple, neonGreen, textMuted)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun OrderExpandableCard(
    pedido: Pedidos,
    neonCian: Color,
    neonPurple: Color,
    neonGreen: Color,
    textMuted: Color
) {
    var expanded by remember { mutableStateOf(false) }
    val borderAlpha = Color.White.copy(alpha = 0.1f)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { expanded = !expanded },
        colors = CardDefaults.cardColors(containerColor = Color(0xFF16162B).copy(alpha = 0.6f)),
        border = BorderStroke(1.dp, if (expanded) neonPurple.copy(alpha = 0.5f) else borderAlpha)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    CustomText(
                        text = "ORDEN #LVL-${pedido.id}",
                        fontWeight = FontWeight.Black,
                        fontSize = 16.sp,
                        color = neonCian
                    )
                    CustomText(
                        text = "TIMESTAMP // ${pedido.fecha.split("T").firstOrNull() ?: ""}",
                        fontSize = 11.sp,
                        color = textMuted
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    CustomText(
                        text = "$${pedido.total.format3()}",
                        fontWeight = FontWeight.Black,
                        color = neonGreen,
                        fontSize = 18.sp
                    )
                    Icon(
                        imageVector = if (expanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = neonCian,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 16.dp)) {
                    HorizontalDivider(color = borderAlpha)
                    Spacer(modifier = Modifier.height(12.dp))

                    CustomText(
                        text = "DIRECCIÓN DE DESPLIEGUE //",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = neonPurple
                    )
                    CustomText(
                        text = pedido.direccion.uppercase(),
                        fontSize = 13.sp,
                        color = Color.White.copy(alpha = 0.8f)
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    CustomText(
                        text = "MANIFIESTO DE HARDWARE //",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = neonPurple
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    pedido.detalles?.forEach { detalle ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.Start
                        ) {
                            CustomText(
                                text = "█  ${detalle.nombre_producto ?: "Ítem Desconocido"}",
                                fontSize = 13.sp,
                                color = Color.White.copy(alpha = 0.9f)
                            )
                        }
                    }
                }
            }
        }
    }
}