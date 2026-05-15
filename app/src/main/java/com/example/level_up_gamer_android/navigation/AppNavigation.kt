package com.example.level_up_gamer_android.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navArgument
import com.example.level_up_gamer_android.ui.screen.*
import com.example.level_up_gamer_android.ui.components.AppBottomBar
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: FormularioViewModel = viewModel()
    
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route
    val currentUser by viewModel.currentUser.collectAsState()
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()

    // Pantallas donde NO se muestra la barra inferior
    val hideBottomBarRoutes = listOf("splash", "login", "register", "verification", "forgot_password")
    val shouldShowBottomBar = currentRoute != null && !hideBottomBarRoutes.any { currentRoute.startsWith(it) }

    Scaffold(
        bottomBar = {
            if (shouldShowBottomBar) {
                AppBottomBar(
                    navController = navController,
                    tipoUsuarioId = currentUser?.tipo_usuario_id ?: 3,
                    isLoggedIn = isLoggedIn,
                    onLogoutAction = {
                        if (isLoggedIn) {
                            viewModel.logout()
                            navController.navigate("login") {
                                popUpTo(0) { inclusive = true }
                            }
                        } else {
                            navController.navigate("login")
                        }
                    }
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController, 
            startDestination = "home", // CAMBIO: Inicia en la Tienda (Modo Invitado)
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("splash") { SplashScreen(navController) }
            
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        val user = viewModel.currentUser.value
                        val route = when (user?.tipo_usuario_id) {
                            1 -> "admin_dashboard"
                            2 -> "supervisor"
                            else -> "home"
                        }
                        navController.navigate(route) {
                            popUpTo("login") { inclusive = true }
                        }
                    },
                    onRegisterClick = { navController.navigate("register") },
                    onForgotPassClick = { navController.navigate("forgot_password") } // Nuevo
                )
            }

            composable("register") {
                RegistroScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = { email -> 
                        navController.navigate("verification/$email") 
                    }
                )
            }

            composable(
                route = "verification/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                VerificationScreen(navController, viewModel, email)
            }

            composable("forgot_password") {
                ForgotPasswordScreen(navController, viewModel)
            }

            composable("home") {
                HomeScreen(
                    viewModel = viewModel,
                    onLogout = { /* Manejado por la barra */ },
                    onCartClick = { navController.navigate("cart") },
                    onProfileClick = { navController.navigate("update_profile") }
                )
            }

            composable("admin_dashboard") { AdminDashboardScreen(navController) }
            
            composable("admin_users") { AdminUserManagementScreen(navController, viewModel) }
            
            composable("supervisor") {
                SupervisorScreen(viewModel, onLogout = { viewModel.logout(); navController.navigate("login") })
            }

            composable("add_address") { AddAddressScreen(navController, viewModel) }

            composable(
                route = "cart?userId={userId}",
                arguments = listOf(navArgument("userId") { 
                    type = NavType.IntType
                    defaultValue = -1 
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                CartScreen(
                    viewModel = viewModel,
                    onBackToHome = { navController.popBackStack() },
                    onCheckoutClick = { 
                        if (isLoggedIn) navController.navigate("address_selection") 
                        else navController.navigate("login")
                    },
                    targetUserId = if (userId != -1) userId else null
                )
            }

            composable(
                route = "update_profile?userId={userId}",
                arguments = listOf(navArgument("userId") { 
                    type = NavType.IntType
                    defaultValue = -1 
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                UpdateProfileScreen(navController, viewModel, if (userId != -1) userId else null)
            }

            composable(
                route = "address_selection?userId={userId}",
                arguments = listOf(navArgument("userId") { 
                    type = NavType.IntType
                    defaultValue = -1 
                })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                AddressSelectionScreen(navController, viewModel, if (userId != -1) userId else null)
            }

            composable("payment/{direccion}") { backStackEntry ->
                val dir = backStackEntry.arguments?.getString("direccion") ?: ""
                PaymentScreen(navController, viewModel, dir)
            }

            composable("success/{order}") { backStackEntry ->
                val order = backStackEntry.arguments?.getString("order") ?: ""
                OrderConfirmationScreen(navController, order)
            }
        }
    }
}
