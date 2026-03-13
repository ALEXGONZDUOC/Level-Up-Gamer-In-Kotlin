package com.example.level_up_gamer_android.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.level_up_gamer_android.ui.components.BotonLogOut
import com.example.level_up_gamer_android.ui.components.CustomFloatingActionButton
import com.example.level_up_gamer_android.ui.components.CustomText
import com.example.level_up_gamer_android.ui.components.GradientSurface
import com.example.level_up_gamer_android.ui.components.ProductoCard
import com.example.level_up_gamer_android.viewmodel.FormularioViewModel

@Composable
fun HomeScreen(viewModel: FormularioViewModel = viewModel(), onLogout: () -> Unit, onCartClick: () -> Unit, onProfileClick: () -> Unit) {
    val productos by viewModel.productos.collectAsState()
    val focusRequesterLogout = remember { FocusRequester() }
    val focusRequesterCart = remember { FocusRequester() }

    GradientSurface {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxSize()
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    CustomText(
                        text = "Catálogo de Productos",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Row {
                        IconButton(onClick = onProfileClick) {
                            Icon(Icons.Default.Person, contentDescription = "Perfil")
                        }
                        BotonLogOut(
                            onLogout = {
                                viewModel.logout()
                                onLogout()
                            },
                            focusRequester = focusRequesterLogout
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(productos) { producto ->
                        ProductoCard(
                            producto = producto,
                            onAddToCart = { viewModel.addToCart(producto) }
                        )
                    }
                }
            }

            CustomFloatingActionButton(
                onClick = onCartClick,
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(16.dp),
                focusRequester = focusRequesterCart
            ) {
                Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Ir al Carrito"
                )
            }
        }
    }
}
