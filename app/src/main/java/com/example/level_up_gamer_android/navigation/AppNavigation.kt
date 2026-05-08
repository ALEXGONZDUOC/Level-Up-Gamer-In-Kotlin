package com.example.level_up_gamer_android.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.level_up_gamer_android.ui.screen.*
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: FormularioViewModel = viewModel()

    NavHost(navController = navController, startDestination = "splash") {
        composable("splash") {
            SplashScreen(navController = navController)
        }
        composable("login") {
            LoginScreen(
                viewModel = viewModel,
                onLoginSuccess = {
                    val user = viewModel.currentUser.value
                    if (user?.tipo_usuario_id == 1) {
                        navController.navigate("admin_dashboard") {
                            popUpTo("login") { inclusive = true }
                        }
                    } else {
                        navController.navigate("home") {
                            popUpTo("login") { inclusive = true }
                        }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
        }
        composable("admin_dashboard") {
            AdminDashboardScreen(navController = navController)
        }
        composable("admin_users") {
            AdminUserManagementScreen(navController = navController, viewModel = viewModel)
        }
        composable("home") {
            HomeScreen(
                viewModel = viewModel,
                onLogout = {
                    navController.navigate("login") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onCartClick = {
                    navController.navigate("cart")
                },
                onProfileClick = {
                    navController.navigate("update_profile")
                }
            )
        }
        composable("register") {
            RegistroScreen(viewModel)
        }
        composable(
            route = "cart?userId={userId}",
            arguments = listOf(navArgument("userId") { 
                type = NavType.IntType
                defaultValue = -1 
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId").takeIf { it != -1 }
            CartScreen(
                viewModel = viewModel,
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                targetUserId = userId
            )
        }
        composable(
            route = "update_profile?userId={userId}",
            arguments = listOf(navArgument("userId") { 
                type = NavType.IntType
                defaultValue = -1 
            })
        ) { backStackEntry ->
            val userId = backStackEntry.arguments?.getInt("userId").takeIf { it != -1 }
            UpdateProfileScreen(
                navController = navController,
                viewModel = viewModel,
                targetUserId = userId
            )
        }
    }
}
