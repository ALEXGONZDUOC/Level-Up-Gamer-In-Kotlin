package com.example.level_up_gamer_android.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_android.model.*
import com.example.level_up_gamer_android.network.RetrofitClient
import com.example.level_up_gamer_android.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class FormularioViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.instance
    private val sessionManager = SessionManager(application)

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

    private val _direcciones = MutableStateFlow<List<Direccion>>(emptyList())
    val direcciones: StateFlow<List<Direccion>> = _direcciones

    private val _topProductos = MutableStateFlow<List<Producto>>(emptyList())
    val topProductos: StateFlow<List<Producto>> = _topProductos

    private val _ventasTotales = MutableStateFlow<Map<String, Double>>(emptyMap())
    val ventasTotales: StateFlow<Map<String, Double>> = _ventasTotales

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    init {
        // Cargar sesión guardada
        sessionManager.getUser()?.let {
            _currentUser.value = it
            _isLoggedIn.value = true
        }
        cargarDatos()
    }

    private fun cargarDatos() {
        viewModelScope.launch {
            try {
                _productos.value = apiService.getProductos()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al cargar productos", e)
            }
        }
    }

    fun cargarEstadisticas() {
        viewModelScope.launch {
            try {
                _topProductos.value = apiService.getTopProductos()
                val vDia = apiService.getVentasTotales("dia")
                val vSem = apiService.getVentasTotales("semana")
                val vMes = apiService.getVentasTotales("mes")
                
                _ventasTotales.value = mapOf(
                    "dia" to (vDia["total"] ?: 0.0),
                    "semana" to (vSem["total"] ?: 0.0),
                    "mes" to (vMes["total"] ?: 0.0)
                )
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al cargar estadisticas", e)
            }
        }
    }

    fun cargarUsuarios() {
        viewModelScope.launch {
            try {
                _usuarios.value = apiService.getUsuarios()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al cargar usuarios", e)
            }
        }
    }

    fun getUsuarioById(id: Int): Usuario? {
        return _usuarios.value.find { it.id == id }
    }

    fun cargarDirecciones(usuarioId: Int? = null) {
        val userId = usuarioId ?: _currentUser.value?.id ?: return
        viewModelScope.launch {
            try {
                _direcciones.value = apiService.getDirecciones(userId)
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al cargar direcciones", e)
            }
        }
    }

    fun agregarDireccion(direccion: Direccion) {
        viewModelScope.launch {
            try {
                apiService.crearDireccion(direccion)
                cargarDirecciones()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al guardar dirección", e)
            }
        }
    }

    fun establecerPrincipal(direccionId: Int) {
        val userId = _currentUser.value?.id ?: return
        viewModelScope.launch {
            try {
                apiService.marcarPrincipal(direccionId, userId)
                cargarDirecciones()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al actualizar principal", e)
            }
        }
    }

    fun finalizarPedido(direccion: String, onResult: (String?) -> Unit) {
        val user = _currentUser.value ?: return
        val items = getCartItems()
        if (items.isEmpty()) return

        val request = PedidoRequest(
            usuario_id = user.id,
            direccion = direccion,
            detalles = items.map { (p, qty) -> ItemPedido(p.id, qty) }
        )

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.crearPedido(request)
                val orderNum = response["pedido_id"]?.toString() ?: "ERR"
                clearCart()
                cargarDatos()
                onResult(orderNum)
            } catch (e: Exception) {
                android.util.Log.e("API", "Error en Pedido", e)
                onResult(null)
            } finally {
                _loading.value = false
            }
        }
    }

    fun agregarUsuario(nombre: String, contrasena: String, email: String, onResult: (String?) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val request = mapOf("nombre" to nombre, "contrasena" to contrasena, "email" to email)
                apiService.registrarUsuario(request)
                onResult(null) // Éxito
            } catch (e: Exception) {
                onResult(e.localizedMessage)
            } finally {
                _loading.value = false
            }
        }
    }

    fun verificarCuenta(email: String, codigo: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val request = mapOf("email" to email, "codigo" to codigo)
                val res = apiService.verificarUsuario(request)
                onResult(true, res["mensaje"] ?: "Éxito")
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun solicitarRecuperacion(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val request = mapOf("email" to email)
                val res = apiService.solicitarRecuperacion(request)
                onResult(true, res["mensaje"] ?: "Código enviado")
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun resetPassword(email: String, codigo: String, nueva: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val request = mapOf("email" to email, "codigo" to codigo, "nueva_contrasena" to nueva)
                val res = apiService.resetPassword(request)
                onResult(true, res["mensaje"] ?: "Contraseña cambiada")
            } catch (e: Exception) {
                onResult(false, e.localizedMessage ?: "Error")
            } finally {
                _loading.value = false
            }
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                val updated = apiService.actualizarUsuario(usuario.id, usuario)
                if (usuario.id == _currentUser.value?.id) {
                    _currentUser.value = updated
                    sessionManager.saveUser(updated)
                }
                cargarUsuarios()
            } catch (e: Exception) {
                android.util.Log.e("API", "Error al actualizar usuario", e)
            }
        }
    }

    fun login(nombre: String, contrasena: String, onResult: (Boolean, String?) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val credentials = mapOf("nombre" to nombre, "contrasena" to contrasena)
                val usuario = apiService.login(credentials)
                _isLoggedIn.value = true
                _currentUser.value = usuario
                sessionManager.saveUser(usuario)
                onResult(true, null)
            } catch (e: Exception) {
                onResult(false, e.localizedMessage)
            } finally {
                _loading.value = false
            }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
        sessionManager.clearSession()
        clearCart()
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
