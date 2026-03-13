package com.example.level_up_gamer_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_android.data.LevelUpDatabase
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.max

class FormularioViewModel(application: Application) : AndroidViewModel(application) {
    private val database = LevelUpDatabase.getDatabase(application)

    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    private val _cart = MutableStateFlow<Map<Producto, Int>>(emptyMap())
    val cart: StateFlow<Map<Producto, Int>> = _cart

    init {
        cargarDatosMock()
    }

    private fun cargarDatosMock() {
        viewModelScope.launch {
            try {
                val usuariosExistentes = database.usuarioDao().obtenerUsuarios()
                if (usuariosExistentes.isEmpty()) {
                    LevelUpDatabase.MOCK_TIPO_USUARIOS.forEach { tipo ->
                        database.tipoUsuariosDao().insertar(tipo)
                    }
                    LevelUpDatabase.MOCK_USUARIOS.forEach { usuario ->
                        database.usuarioDao().insertar(usuario)
                    }
                    LevelUpDatabase.MOCK_PRODUCTOS.forEach { producto ->
                        database.productoDao().insertar(producto)
                    }
                }
                _usuarios.value = database.usuarioDao().obtenerUsuarios()
                _productos.value = database.productoDao().obtenerProductos()
            } catch (e: Exception) {
                _usuarios.value = LevelUpDatabase.MOCK_USUARIOS
                _productos.value = LevelUpDatabase.MOCK_PRODUCTOS
            }
        }
    }

    fun agregarUsuario(nombre: String, contrasena: String, email: String) {
        val nuevoUsuario = Usuario(
            nombre = nombre,
            contrasena = contrasena,
            email = email,
            tipo_usuario_id = 2,
            activo = true,
            fecha_creacion = java.time.LocalDate.now().toString()
        )
        viewModelScope.launch {
            try {
                android.util.Log.d("DB", "Intentando insertar usuario: $nuevoUsuario")
                database.usuarioDao().insertar(nuevoUsuario)
                _usuarios.value = database.usuarioDao().obtenerUsuarios()
                android.util.Log.d("DB", "Usuarios después de insertar: ${_usuarios.value}")
            } catch (e: Exception) {
                android.util.Log.e("DB", "Error al insertar usuario", e)
                e.printStackTrace()
            }
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                database.usuarioDao().actualizar(usuario)
                _usuarios.value = database.usuarioDao().obtenerUsuarios()
                _currentUser.value = usuario
            } catch (e: Exception) {
                android.util.Log.e("DB", "Error al actualizar usuario", e)
                e.printStackTrace()
            }
        }
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            _usuarios.value = database.usuarioDao().obtenerUsuarios()
        }
    }

    fun login(nombre: String, contrasena: String): Boolean {
        val usuario = _usuarios.value.find { it.nombre == nombre && it.contrasena == contrasena }
        if (usuario != null) {
            _isLoggedIn.value = true
            _currentUser.value = usuario
            return true
        }
        return false
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

    fun getCartItems(): List<Pair<Producto, Int>> {
        return _cart.value.map { it.key to it.value }
    }

    fun getTotal(): Double {
        return _cart.value.entries.sumOf { it.key.precio * it.value }
    }

    fun clearCart() {
        _cart.value = emptyMap()
    }
}
