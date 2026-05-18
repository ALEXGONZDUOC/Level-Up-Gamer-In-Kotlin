package com.example.level_up_gamer_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.*
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(
    navController: NavController,
    tipoUsuarioId: Int,
    isLoggedIn: Boolean,
    onLogout: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Identificar el "Home" de cada rol para la V0.9
    val homeRoute = when (tipoUsuarioId) {
        1 -> "admin_users" // Home del Admin es directamente Usuarios
        2 -> "total_ventas" // Home del Supervisor es Ventas
        else -> "home"
    }

    val isAtHome = currentRoute == homeRoute

    // Diálogo de Confirmación de Logout (Para los 3 roles)
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = { Text("Cerrar Sesión") },
            text = { Text("¿Estás seguro de que deseas salir de tu cuenta?") },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Red.copy(alpha = 0.7f))
                ) { Text("Salir", color = Color.White) }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Cancelar")
                }
            }
        )
    }

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        // --- SECCIÓN IZQUIERDA: FUNCIONES SEGÚN ROL ---
        when (tipoUsuarioId) {
            1 -> { // ADMIN: Gestión de Usuarios, Tienda y Carrito (para pruebas)
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
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Storefront, "Tienda") },
                    label = { Text("Tienda") },
                    selected = currentRoute == "home",
                    onClick = { if (currentRoute != "home") navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, "Carrito") },
                    label = { Text("Carrito") },
                    selected = currentRoute?.startsWith("cart") == true,
                    onClick = { navController.navigate("cart") }
                )
            }
            2 -> { // SUPERVISOR: Ventas + Tienda + Agregar
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
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Storefront, "Tienda") },
                    label = { Text("Tienda") },
                    selected = currentRoute == "home",
                    onClick = { if (currentRoute != "home") navController.navigate("home") }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AddCircle, "Agregar") },
                    label = { Text("Agregar") },
                    selected = currentRoute?.startsWith("product_editor") == true,
                    onClick = { navController.navigate("product_editor/-1") }
                )
            }
            else -> { // USUARIO o INVITADO: Tienda + Carrito
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Storefront, "Tienda") },
                    label = { Text("Tienda") },
                    selected = currentRoute == "home",
                    onClick = { 
                        if (currentRoute != "home") {
                            navController.navigate("home") { popUpTo(0) }
                        }
                    }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, "Carrito") },
                    label = { Text("Carrito") },
                    selected = currentRoute?.startsWith("cart") == true,
                    onClick = { navController.navigate("cart") }
                )
            }
        }

        // --- SECCIÓN DERECHA: BOTÓN DINÁMICO ---
        // Admin (1) y Supervisor (2) NO tienen botón Volver (tienen sus accesos directos permanentes)
        if (!isAtHome && currentRoute != "home" && tipoUsuarioId != 1 && tipoUsuarioId != 2) { 
            // Botón Volver solo para Usuarios (3) si no están en su Home
            NavigationBarItem(
                icon = { Icon(Icons.AutoMirrored.Filled.ArrowBack, "Volver") },
                label = { Text("Volver") },
                selected = false,
                onClick = { 
                    navController.navigate(homeRoute) {
                        popUpTo(homeRoute) { inclusive = true }
                    }
                }
            )
        } else if ((tipoUsuarioId == 3 || !isLoggedIn)) {
            // Perfil o Login SOLO para usuarios normales o invitados
            if (isLoggedIn) {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Person, "Perfil") },
                    label = { Text("Perfil") },
                    selected = currentRoute == "update_profile",
                    onClick = { navController.navigate("update_profile") }
                )
            } else {
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Login, "Login") },
                    label = { Text("Login") },
                    selected = currentRoute == "login",
                    onClick = { navController.navigate("login") }
                )
            }
        }

        // BOTÓN SALIR: Siempre visible para Admin/Super, o en Home para User
        if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2 || isAtHome)) {
            NavigationBarItem(
                icon = { Icon(Icons.AutoMirrored.Filled.ExitToApp, "Salir") },
                label = { Text("Salir") },
                selected = false,
                onClick = { showLogoutDialog = true },
                colors = NavigationBarItemDefaults.colors(
                    unselectedIconColor = Color.Red.copy(alpha = 0.7f),
                    unselectedTextColor = Color.Red.copy(alpha = 0.7f)
                )
            )
        }
    }
}
