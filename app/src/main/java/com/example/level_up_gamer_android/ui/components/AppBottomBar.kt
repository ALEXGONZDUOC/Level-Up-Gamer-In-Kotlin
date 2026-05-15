package com.example.level_up_gamer_android.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun AppBottomBar(
    navController: NavController,
    tipoUsuarioId: Int,
    isLoggedIn: Boolean,
    onLogoutAction: () -> Unit
) {
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route

    // Identificar el "Home" de cada rol
    val homeRoute = when (tipoUsuarioId) {
        1 -> "admin_dashboard"
        2 -> "supervisor"
        else -> "home"
    }

    val isAtHome = currentRoute == homeRoute

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface.copy(alpha = 0.95f),
        contentColor = MaterialTheme.colorScheme.primary
    ) {
        // --- SECCIÓN IZQUIERDA: FUNCIONES SEGÚN ROL ---
        when {
            tipoUsuarioId == 1 && isLoggedIn -> { // ADMIN
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Dashboard, "Panel") },
                    label = { Text("Panel") },
                    selected = currentRoute == "admin_dashboard",
                    onClick = { navController.navigate("admin_dashboard") { popUpTo(0) } }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Group, "Usuarios") },
                    label = { Text("Usuarios") },
                    selected = currentRoute == "admin_users",
                    onClick = { navController.navigate("admin_users") }
                )
            }
            tipoUsuarioId == 2 && isLoggedIn -> { // SUPERVISOR
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Assessment, "Ventas") },
                    label = { Text("Ventas") },
                    selected = currentRoute == "supervisor",
                    onClick = { navController.navigate("supervisor") { popUpTo(0) } }
                )
            }
            else -> { // USUARIO o INVITADO (ID 3 o no logueado)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, "Tienda") },
                    label = { Text("Tienda") },
                    selected = currentRoute == "home",
                    onClick = { navController.navigate("home") { popUpTo(0) } }
                )
                // El carro es útil incluso para invitados (local)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.ShoppingCart, "Carro") },
                    label = { Text("Carro") },
                    selected = currentRoute?.startsWith("cart") == true,
                    onClick = { navController.navigate("cart") }
                )
            }
        }

        // --- SECCIÓN DERECHA: EL BOTÓN DINÁMICO (VOLVER / PERFIL / SALIR) ---
        
        if (!isAtHome) {
            // 1. SI NO ESTOY EN MI HOME -> BOTÓN VOLVER
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
        } else {
            // 2. SI ESTOY EN MI HOME -> DEPENDE DE SI ESTOY LOGUEADO
            if (isLoggedIn) {
                // USUARIO LOGUEADO -> Botón Perfil (donde está el logout real)
                NavigationBarItem(
                    icon = { Icon(Icons.Default.AccountCircle, "Perfil") },
                    label = { Text("Perfil") },
                    selected = currentRoute?.startsWith("update_profile") == true,
                    onClick = { navController.navigate("update_profile") }
                )
            } else {
                // INVITADO -> Botón Salir (lo lleva al Login)
                NavigationBarItem(
                    icon = { Icon(Icons.AutoMirrored.Filled.Login, "Salir") },
                    label = { Text("Salir") },
                    selected = false,
                    onClick = onLogoutAction,
                    colors = NavigationBarItemDefaults.colors(
                        unselectedIconColor = Color.Red.copy(alpha = 0.8f),
                        unselectedTextColor = Color.Red.copy(alpha = 0.8f)
                    )
                )
            }
        }
    }
}
