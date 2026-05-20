package com.example.level_up_gamer_android.navigation

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import androidx.navigation.compose.composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.example.level_up_gamer_android.ui.screen.AddressSelectionScreen
import com.example.level_up_gamer_android.ui.screen.PaymentScreen
import com.example.level_up_gamer_android.ui.screen.OrderConfirmationScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel
import com.example.level_up_gamer_android.ui.screen.*

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.level_up_gamer_android.ui.components.AppBottomBar

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val viewModel: FormularioViewModel = viewModel()
    
    val isLoggedIn by viewModel.isLoggedIn.collectAsState()
    val currentUser by viewModel.currentUser.collectAsState()
    val tipoUsuarioId = currentUser?.tipo_usuario_id ?: 0

    Box(modifier = Modifier.fillMaxSize()) {
        // --- CAPA 1: CONTENIDO (Detrás) ---
        NavHost(
            navController = navController,
            startDestination = "splash",
            modifier = Modifier.fillMaxSize(),

            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { 1000 },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -1000 },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -1000 },
                    animationSpec = tween(400)
                ) + fadeIn(animationSpec = tween(400))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { 1000 },
                    animationSpec = tween(400)
                ) + fadeOut(animationSpec = tween(400))
            }
        ) {
            // Splash
            composable("splash") {
                SplashScreen(navController = navController)
            }

            // Login
            composable("login") {
                LoginScreen(
                    viewModel = viewModel,
                    onLoginSuccess = {
                        val user = viewModel.currentUser.value
                        when (user?.tipo_usuario_id) {
                            1 -> navController.navigate("admin_users") {
                                popUpTo("login") { inclusive = true }
                            }
                            2 -> navController.navigate("total_ventas") {
                                popUpTo("login") { inclusive = true }
                            }
                            else -> navController.navigate("home") {
                                popUpTo("login") { inclusive = true }
                            }
                        }
                    },
                    onRegisterClick = {
                        navController.navigate("register")
                    },
                    onForgotPasswordClick = {
                        navController.navigate("forgot_password")
                    }
                )
            }

            // Admin Dashboard
            composable("admin_dashboard") {
                if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2)) {
                    AdminDashboardScreen(navController = navController)
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("admin_dashboard") { inclusive = true }
                        }
                    }
                }
            }

            // Gestión de Usuarios (Admin)
            composable("admin_users") {
                if (isLoggedIn && tipoUsuarioId == 1) {
                    AdminUserManagementScreen(navController = navController, viewModel = viewModel)
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("admin_users") { inclusive = true }
                        }
                    }
                }
            }

            // Estadísticas de Ventas (Supervisor)
            composable("total_ventas") {
                if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2)) {
                    TotalVentasScreen(navController = navController, viewModel = viewModel)
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("total_ventas") { inclusive = true }
                        }
                    }
                }
            }

            // Detalle de Ventas por Producto
            composable(
                route = "product_sales_detail/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.IntType })
            ) { backStackEntry ->
                if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2)) {
                    val productId = backStackEntry.arguments?.getInt("productId") ?: -1
                    ProductSalesDetailScreen(navController, viewModel, productId)
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("product_sales_detail/{productId}") { inclusive = true }
                        }
                    }
                }
            }

            // Home
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
                    onTotalSalesClick = {
                        navController.navigate("total_ventas")
                    },
                    onProfileClick = {
                        navController.navigate("update_profile")
                    },
                    onAddProductClick = {
                        navController.navigate("product_editor/-1")
                    },
                    onEditProductClick = { productId ->
                        navController.navigate("product_editor/$productId")
                    },
                    onViewProductSales = { productId ->
                        navController.navigate("product_sales_detail/$productId")
                    }
                )
            }

            // Registro
            composable("register") {
                RegistroScreen(
                    viewModel = viewModel,
                    onRegisterSuccess = { email ->
                        navController.navigate("verification/$email")
                    }
                )
            }

            // Verificación
            composable(
                route = "verification/{email}",
                arguments = listOf(navArgument("email") { type = NavType.StringType })
            ) { backStackEntry ->
                val email = backStackEntry.arguments?.getString("email") ?: ""
                VerificationScreen(navController, viewModel, email)
            }

            // Olvidé mi contraseña
            composable("forgot_password") {
                ForgotPasswordScreen(navController, viewModel)
            }

            // 🔥 Cart
            composable(
                route = "cart?userId={userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType; defaultValue = -1 })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                val targetUserId = if (userId != -1) userId else null
                
                // Si intenta ver el carrito de otro usuario, debe ser Admin
                if (targetUserId != null && targetUserId != currentUser?.id && tipoUsuarioId != 1) {
                    LaunchedEffect(Unit) {
                        navController.navigate("home") {
                            popUpTo("cart") { inclusive = true }
                        }
                    }
                } else {
                    CartScreen(
                        viewModel = viewModel,
                        onBackToHome = { navController.popBackStack() },
                        onCheckoutClick = { 
                            if (isLoggedIn) {
                                navController.navigate("address_selection") 
                            } else {
                                navController.navigate("login")
                            }
                        },
                        targetUserId = targetUserId
                    )
                }
            }

            // Dirección
            composable(
                route = "address_selection?userId={userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType; defaultValue = -1 })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                val targetUserId = if (userId != -1) userId else null
                
                // Seguridad: Solo el dueño o Admin
                if (isLoggedIn && (targetUserId == null || targetUserId == currentUser?.id || tipoUsuarioId == 1)) {
                    AddressSelectionScreen(
                        navController = navController,
                        viewModel = viewModel,
                        targetUserId = targetUserId
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("login") {
                            popUpTo("address_selection") { inclusive = true }
                        }
                    }
                }
            }

            // Pago
            composable(
                route = "payment/{direccion}",
                arguments = listOf(navArgument("direccion") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->
                if (isLoggedIn) {
                    val direccion =
                        backStackEntry.arguments?.getString("direccion") ?: ""

                    PaymentScreen(
                        navController = navController,
                        viewModel = viewModel,
                        direccion = direccion
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("login")
                    }
                }
            }

            // Confirmación
            composable(
                route = "order_confirmation/{orderNumber}",
                arguments = listOf(navArgument("orderNumber") {
                    type = NavType.StringType
                })
            ) { backStackEntry ->

                val orderNumber = backStackEntry.arguments?.getString("orderNumber") ?: ""

                OrderConfirmationScreen(
                    navController = navController,
                    orderNumber = orderNumber
                )
            }

            // Perfil
            composable(
                route = "update_profile?userId={userId}",
                arguments = listOf(navArgument("userId") { type = NavType.IntType; defaultValue = -1 })
            ) { backStackEntry ->
                val userId = backStackEntry.arguments?.getInt("userId") ?: -1
                val targetUserId = if (userId != -1) userId else null
                
                // Seguridad: Solo el dueño o Admin
                if (isLoggedIn && (targetUserId == null || targetUserId == currentUser?.id || tipoUsuarioId == 1)) {
                    UpdateProfileScreen(
                        navController = navController,
                        viewModel = viewModel,
                        targetUserId = targetUserId
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("login")
                    }
                }
            }

            // Editor de producto
            composable(
                route = "product_editor/{productId}",
                arguments = listOf(navArgument("productId") {
                    type = NavType.IntType
                })
            ) { backStackEntry ->
                if (isLoggedIn && (tipoUsuarioId == 1 || tipoUsuarioId == 2)) {
                    val productId =
                        backStackEntry.arguments?.getInt("productId")

                    ProductEditorScreen(
                        navController = navController,
                        viewModel = viewModel,
                        productId = productId
                    )
                } else {
                    LaunchedEffect(Unit) {
                        navController.navigate("home")
                    }
                }
            }

            // Nueva Dirección
            composable("add_address") {
                AddAddressScreen(navController, viewModel)
            }

            // Mis Pedidos (Solo Usuario)
            composable("user_orders") {
                UserOrdersScreen(navController, viewModel)
            }
        }

        // --- CAPA 2: BARRA INFERIOR (En frente) ---
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route
        
        val hideBarRoutes = listOf("splash", "login", "register", "verification", "forgot_password")
        val shouldShowBar = currentRoute != null && !hideBarRoutes.any { currentRoute.startsWith(it) }

        if (shouldShowBar) {
            Box(
                modifier = Modifier.align(Alignment.BottomCenter)
            ) {
                AppBottomBar(
                    navController = navController,
                    tipoUsuarioId = tipoUsuarioId,
                    isLoggedIn = isLoggedIn,
                    onLogout = {
                        viewModel.logout()
                        navController.navigate("home") {
                            popUpTo(0) { inclusive = true }
                        }
                    }
                )
            }
        }
    }
}
