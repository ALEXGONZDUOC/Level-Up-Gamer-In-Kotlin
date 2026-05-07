package com.example.level_up_gamer_android.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import com.example.level_up_gamer_android.network.RetrofitClient
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class FormularioViewModel : ViewModel() {
    private val apiService = RetrofitClient.instance

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    private val _cart = MutableStateFlow<Map<Producto, Int>>(emptyMap())
    val cart: StateFlow<Map<Producto, Int>> = _cart

    init {
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            try {
                _productos.value = apiService.getProductos()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al cargar datos", e)
            }
        }
    }

    fun login(nombre: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            try {
                android.util.Log.d("API", "Iniciando login para: $nombre")
                val credentials = mapOf("nombre" to nombre, "contrasena" to contrasena)
                val usuario = apiService.login(credentials)
                android.util.Log.d("API", "Login exitoso: ${usuario.nombre}")
                _isLoggedIn.value = true
                _currentUser.value = usuario
                onResult(true)
            } catch (e: Exception) {
                android.util.Log.e("API", "Error en login", e)
                onResult(false)
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
    }

    // Cart methods
    fun addToCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        val currentQuantity = currentCart[producto] ?: 0
        currentCart[producto] = currentQuantity + 1
        _cart.value = currentCart
    }

    fun removeFromCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        val currentQuantity = currentCart[producto] ?: 0
        if (currentQuantity > 1) {
            currentCart[producto] = currentQuantity - 1
        } else {
            currentCart.remove(producto)
        }
        _cart.value = currentCart
    }

    fun updateQuantity(producto: Producto, quantity: Int) {
        val currentCart = _cart.value.toMutableMap()
        if (quantity > 0) {
            currentCart[producto] = quantity
        } else {
            currentCart.remove(producto)
        }
        _cart.value = currentCart
    }

    fun removeItemFromCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        currentCart.remove(producto)
        _cart.value = currentCart
    }

    fun getCartItems(): List<Pair<Producto, Int>> = _cart.value.map { it.key to it.value }
    fun getTotal(): Double = _cart.value.entries.sumOf { it.key.precio * it.value }
    fun clearCart() { _cart.value = emptyMap() }
}
