package com.example.level_up_gamer_android.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel // Asegúrate de importar el ViewModel

@Composable
fun AppBottomBar(
    navController: NavController,
    viewModel: FormularioViewModel, // 1. INYECTAMOS EL VIEWMODEL AQUÍ
    tipoUsuarioId: Int,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showLogoutDialog by remember { mutableStateOf(false) }

    // 2. ESCUCHAMOS EL ESTADO DEL CARRITO EN TIEMPO REAL
    val cartMap by viewModel.cart.collectAsState()
    val totalItemsInCart = cartMap.values.sum()

    // Identificar el "Home" de cada rol (Lógica V0.9)
    val homeRoute = when (tipoUsuarioId) {
        1 -> "admin_users"
        2 -> "total_ventas"
        else -> "home"
    }

    val isAtHome = currentRoute == homeRoute

    // Diálogo de Confirmación de Logout
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { CustomText("Cerrar Sesión", color = MaterialTheme.colorScheme.primary) },
            text = { CustomText("¿Estás seguro de que deseas salir de tu cuenta?") },
            confirmButton = {
                CustomButton(
                    text = "Salir",
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    }
                )
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    CustomText("Cancelar", color = Color.White)
                }
            },
            containerColor = Color(0xFF1A1A2E)
        )
    }

    Surface(
        modifier = Modifier
            .padding(horizontal = 24.dp, vertical = 20.dp)
            .navigationBarsPadding(),
        shape = RoundedCornerShape(40.dp),
        color = Color(0xFF16162B).copy(alpha = 0.85f),
        tonalElevation = 0.dp
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            contentColor = MaterialTheme.colorScheme.primary,
            tonalElevation = 0.dp
        ) {
            // --- SECCIÓN IZQUIERDA: FUNCIONES SEGÚN ROL ---
            when (tipoUsuarioId) {
                1 -> { // ADMIN
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Group, "Usuarios") },
                        label = { Text("Usuarios") },
                        selected = currentRoute == "admin_users",
                        onClick = {
                            if (currentRoute != "admin_users") {
                                navController.navigate("admin_users") {
                                    popUpTo("admin_users") { inclusive = true }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Storefront, "Tienda") },
                        label = { Text("Tienda") },
                        selected = currentRoute == "home",
                        onClick = { if (currentRoute != "home") navController.navigate("home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )

                    // CARRITO DEL ADMIN CON NOTIFICACIONES
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalItemsInCart > 0) {
                                        Badge(
                                            containerColor = Color(0xFF00F0FF), // Cian Neón
                                            contentColor = Color.Black
                                        ) {
                                            Text(text = totalItemsInCart.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, "Carrito")
                            }
                        },
                        label = { Text("Carrito") },
                        selected = currentRoute?.startsWith("cart") == true,
                        onClick = { navController.navigate("cart") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                }
                2 -> { // SUPERVISOR
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Assessment, "Ventas") },
                        label = { Text("Ventas") },
                        selected = currentRoute == "total_ventas",
                        onClick = {
                            if (currentRoute != "total_ventas") {
                                navController.navigate("total_ventas") {
                                    popUpTo("total_ventas") { inclusive = true }
                                }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Storefront, "Tienda") },
                        label = { Text("Tienda") },
                        selected = currentRoute == "home",
                        onClick = { if (currentRoute != "home") navController.navigate("home") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.AddCircle, "Agregar") },
                        label = { Text("Agregar") },
                        selected = currentRoute?.startsWith("product_editor") == true,
                        onClick = { navController.navigate("product_editor/-1") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                }
                else -> { // USUARIO o INVITADO
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Storefront, "Tienda") },
                        label = { Text("Tienda") },
                        selected = currentRoute == "home",
                        onClick = {
                            if (currentRoute != "home") {
                                navController.navigate("home") { popUpTo(0) }
                            }
                        },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )

                    if (isLoggedIn && tipoUsuarioId == 3) {
                        NavigationBarItem(
                            icon = { Icon(Icons.Default.ShoppingBag, "Pedidos") },
                            label = { Text("Pedidos") },
                            selected = currentRoute == "user_orders",
                            onClick = {
                                if (currentRoute != "user_orders") navController.navigate("user_orders")
                            },
                            colors = NavigationBarItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.tertiary,
                                selectedTextColor = MaterialTheme.colorScheme.tertiary,
                                unselectedIconColor = Color.White.copy(alpha = 0.5f),
                                indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                            )
                        )
                    }

                    // CARRITO DEL CLIENTE CON NOTIFICACIONES
                    NavigationBarItem(
                        icon = {
                            BadgedBox(
                                badge = {
                                    if (totalItemsInCart > 0) {
                                        Badge(
                                            containerColor = Color(0xFF00F0FF), // Cian Neón
                                            contentColor = Color.Black
                                        ) {
                                            Text(text = totalItemsInCart.toString(), fontSize = 10.sp, fontWeight = FontWeight.Bold)
                                        }
                                    }
                                }
                            ) {
                                Icon(Icons.Default.ShoppingCart, "Carrito")
                            }
                        },
                        label = { Text("Carrito") },
                        selected = currentRoute?.startsWith("cart") == true,
                        onClick = { navController.navigate("cart") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                }
            }

            // --- SECCIÓN DERECHA: BOTÓN DINÁMICO ---
            if (!isAtHome && currentRoute != "home" && tipoUsuarioId != 1 && tipoUsuarioId != 2) {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") },
                    label = { Text("Volver") },
                    selected = false,
                    onClick = {
                        navController.navigate(homeRoute) {
                            popUpTo(homeRoute) { inclusive = true }
                        }
                    },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.White.copy(alpha = 0.5f),
                        unselectedTextColor = Color.White.copy(alpha = 0.5f)
                    )
                )
            } else if ((tipoUsuarioId == 3 || !isLoggedIn)) {
                if (isLoggedIn) {
                    NavigationBarItem(
                        icon = { Icon(Icons.Default.Person, "Perfil") },
                        label = { Text("Perfil") },
                        selected = currentRoute == "update_profile",
                        onClick = { navController.navigate("update_profile") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                } else {
                    NavigationBarItem(
                        icon = { Icon(Icons.AutoMirrored.Filled.Login, "Login") },
                        label = { Text("Login") },
                        selected = currentRoute == "login",
                        onClick = { navController.navigate("login") },
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = MaterialTheme.colorScheme.tertiary,
                            selectedTextColor = MaterialTheme.colorScheme.tertiary,
                            unselectedIconColor = Color.White.copy(alpha = 0.5f),
                            indicatorColor = MaterialTheme.colorScheme.tertiary.copy(alpha = 0.15f)
                        )
                    )
                }
            }

            // BOTÓN SALIR
            if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2 || isAtHome)) {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir") },
                    label = { Text("Salir") },
                    selected = false,
                    onClick = { showLogoutDialog = true },
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color(0xFFFF4757),
                        unselectedTextColor = Color(0xFFFF4757)
                    )
                )
            }
        }
    }
}