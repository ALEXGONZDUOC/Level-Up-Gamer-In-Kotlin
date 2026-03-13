package com.example.level_up_gamer_android.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import com.example.level_up_gamer_android.ui.screen.HomeScreen
import com.example.level_up_gamer_android.ui.screen.LoginScreen
import com.example.level_up_gamer_android.ui.screen.SplashScreen
import androidx.navigation.compose.rememberNavController
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import androidx.navigation.compose.composable
import com.example.level_up_gamer_android.ui.screen.CartScreen
import com.example.level_up_gamer_android.ui.screen.RegistroScreen
import com.example.level_up_gamer_android.ui.screen.UpdateProfileScreen


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
                    navController.navigate("home") {
                        popUpTo("login") { inclusive = true }
                    }
                },
                onRegisterClick = {
                    navController.navigate("register")
                }
            )
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
        composable("cart") {
            CartScreen(
                viewModel = viewModel,
                onBackToHome = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                }
            )
        }
        composable("update_profile") {
            UpdateProfileScreen(
                navController = navController,
                viewModel = viewModel
            )
        }
    }
}

