package com.example.level_up_gamer_android.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.level_up_gamer_android.data.network.RetrofitClient
import com.example.level_up_gamer_android.model.Direccion
import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import com.example.level_up_gamer_android.model.Pedidos
import com.example.level_up_gamer_android.utils.SessionManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.time.LocalDate

class FormularioViewModel(application: Application) : AndroidViewModel(application) {
    private val apiService = RetrofitClient.apiService
    private val sessionManager = SessionManager(application)

    private val _productos = MutableStateFlow<List<Producto>>(emptyList())
    val productos: StateFlow<List<Producto>> = _productos

    private val _searchText = MutableStateFlow("")
    val searchText: StateFlow<String> = _searchText

    private val _filteredProductos = MutableStateFlow<List<Producto>>(emptyList())
    val filteredProductos: StateFlow<List<Producto>> = _filteredProductos

    // ...
    fun onSearchTextChange(text: String) {
        _searchText.value = text
        filtrarProductos()
    }

    private fun filtrarProductos() {
        val query = _searchText.value.lowercase()
        _filteredProductos.value = if (query.isBlank()) {
            _productos.value
        } else {
            _productos.value.filter { 
                it.nombre.lowercase().contains(query) || 
                it.categoria.lowercase().contains(query) ||
                it.descripcion.lowercase().contains(query)
            }
        }
    }

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn

    private val _currentUser = MutableStateFlow<Usuario?>(null)
    val currentUser: StateFlow<Usuario?> = _currentUser

    private val _cart = MutableStateFlow<Map<Producto, Int>>(emptyMap())
    val cart: StateFlow<Map<Producto, Int>> = _cart

    private val _loading = MutableStateFlow(false)
    val loading: StateFlow<Boolean> = _loading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _direcciones = MutableStateFlow<List<Direccion>>(emptyList())
    val direcciones: StateFlow<List<Direccion>> = _direcciones

    // --- Admin/Supervisor States ---
    private val _usuarios = MutableStateFlow<List<Usuario>>(emptyList())
    val usuarios: StateFlow<List<Usuario>> = _usuarios

    private val _pedidos = MutableStateFlow<List<Pedidos>>(emptyList())
    val pedidos: StateFlow<List<Pedidos>> = _pedidos

    data class UsuarioStats(val nombre: String, val totalCompras: Int, val valorTotal: Double)
    private val _usuarioTopVentas = MutableStateFlow<UsuarioStats?>(null)
    val usuarioTopVentas: StateFlow<UsuarioStats?> = _usuarioTopVentas

    private val _topProductos = MutableStateFlow<List<Producto>>(emptyList())
    val topProductos: StateFlow<List<Producto>> = _topProductos

    private val _ventasTotales = MutableStateFlow<Map<String, Double>>(emptyMap())
    val ventasTotales: StateFlow<Map<String, Double>> = _ventasTotales

    private val _productoVentasPorDia = MutableStateFlow<Map<String, Int>>(emptyMap())
    val productoVentasPorDia: StateFlow<Map<String, Int>> = _productoVentasPorDia

    init {
        sessionManager.getUser()?.let {
            _currentUser.value = it
            _isLoggedIn.value = true
        }
        cargarProductos()
    }

    fun clearError() { _error.value = null }

    // --- Admin/Supervisor Methods ---
    fun cargarUsuarios() {
        if (_currentUser.value?.tipo_usuario_id != 1) return
        
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.getUsuarios()
                if (response.isSuccessful) {
                    _usuarios.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) { _error.value = "Error al cargar usuarios" }
            finally { _loading.value = false }
        }
    }

    fun adminActualizarUsuario(usuario: Usuario) {
        if (_currentUser.value?.tipo_usuario_id != 1) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.adminActualizarUsuario(usuario.id, usuario)
                if (response.isSuccessful) {
                    _error.value = "Usuario actualizado"
                    cargarUsuarios()
                }
            } catch (e: Exception) { _error.value = "Error al actualizar usuario" }
            finally { _loading.value = false }
        }
    }

    fun eliminarUsuario(id: Int) {
        if (_currentUser.value?.tipo_usuario_id != 1) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.eliminarUsuario(id)
                if (response.isSuccessful) {
                    _error.value = "Usuario eliminado"
                    cargarUsuarios()
                }
            } catch (e: Exception) { _error.value = "Error al eliminar usuario" }
            finally { _loading.value = false }
        }
    }

    fun cargarPedidos() {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2 && role != 3) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.getPedidos()
                if (response.isSuccessful) {
                    val pedidosList = response.body() ?: emptyList()
                    _pedidos.value = pedidosList
                    calcularUsuarioTop(pedidosList)
                }
            } catch (e: Exception) { _error.value = "Error al cargar pedidos" }
            finally { _loading.value = false }
        }
    }

    private fun calcularUsuarioTop(pedidos: List<Pedidos>) {
        if (pedidos.isEmpty()) return
        val statsMap = pedidos.groupBy { it.usuario_id }.mapValues { entry ->
            val userPedidos = entry.value
            val totalValue = userPedidos.sumOf { it.total }
            val count = userPedidos.size
            Pair(count, totalValue)
        }
        val topUserId = statsMap.maxByOrNull { it.value.first }?.key
        if (topUserId != null) {
            val stats = statsMap[topUserId]!!
            val userName = _usuarios.value.find { it.id == topUserId }?.nombre ?: "Usuario #$topUserId"
            _usuarioTopVentas.value = UsuarioStats(userName, stats.first, stats.second)
        }
    }

    fun cargarTopProductos() {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                val response = apiService.getTopProductos()
                if (response.isSuccessful) {
                    _topProductos.value = response.body() ?: emptyList()
                }
            } catch (e: Exception) { _error.value = "Error al cargar top productos" }
        }
    }

    fun cargarVentasTotales(periodo: String) {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                val response = apiService.getVentasTotales(periodo)
                if (response.isSuccessful) {
                    val currentMap = _ventasTotales.value.toMutableMap()
                    val total = response.body()?.get("total") ?: 0.0
                    currentMap[periodo] = total
                    _ventasTotales.value = currentMap
                }
            } catch (e: Exception) { _error.value = "Error al cargar ventas" }
        }
    }

    fun cargarVentasProductoPorDia(productoId: Int) {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                val response = apiService.getVentasProductoPorDia(productoId)
                if (response.isSuccessful) {
                    _productoVentasPorDia.value = response.body() ?: emptyMap()
                }
            } catch (e: Exception) { _error.value = "Error al cargar ventas por día" }
        }
    }

    fun getUsuarioById(id: Int): Usuario? {
        if (_currentUser.value?.tipo_usuario_id != 1 && _currentUser.value?.id != id) return null
        return _usuarios.value.find { it.id == id }
    }

    fun cargarDirecciones(usuarioId: Int? = null) {
        val currentUserId = _currentUser.value?.id ?: return
        val role = _currentUser.value?.tipo_usuario_id
        
        // Solo el dueño o Admin pueden cargar direcciones
        val targetUserId = usuarioId ?: currentUserId
        if (targetUserId != currentUserId && role != 1) return

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.getDirecciones(targetUserId)
                if (response.isSuccessful) { _direcciones.value = response.body() ?: emptyList() }
            } catch (e: Exception) { _error.value = "Error al cargar direcciones" }
            finally { _loading.value = false }
        }
    }

    fun agregarDireccion(direccion: Direccion) {
        val currentUserId = _currentUser.value?.id ?: return
        if (direccion.usuario_id != currentUserId) return // Solo el dueño

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.crearDireccion(direccion)
                if (response.isSuccessful) {
                    cargarDirecciones()
                    _error.value = "Dirección guardada con éxito"
                }
            } catch (e: Exception) { _error.value = "Error al guardar dirección" }
            finally { _loading.value = false }
        }
    }

    fun establecerPrincipal(direccionId: Int) {
        val userId = _currentUser.value?.id ?: return
        viewModelScope.launch {
            try {
                if (apiService.marcarPrincipal(direccionId, userId).isSuccessful) { cargarDirecciones() }
            } catch (e: Exception) { _error.value = "Error al actualizar principal" }
        }
    }

    fun finalizarPedido(direccion: String, onResult: (String?) -> Unit) {
        val user = _currentUser.value ?: return
        val items = getCartItems()
        if (items.isEmpty()) return
        val pedidoData = mapOf(
            "usuario_id" to user.id,
            "direccion" to direccion,
            "total" to getTotal(),
            "detalles" to items.map { (p, qty) ->
                mapOf("producto_id" to p.id, "cantidad" to qty, "precio_unitario" to p.precio)
            }
        )
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.crearPedido(pedidoData)
                if (response.isSuccessful) {
                    val orderNum = response.body()?.get("pedido_id")?.toString() ?: "LVL-ERROR"
                    clearCart()
                    cargarProductos()
                    onResult("LVL-$orderNum")
                } else {
                    onResult(null)
                    _error.value = "No se pudo procesar el pedido"
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
                onResult(null)
            } finally { _loading.value = false }
        }
    }

    fun cargarProductos() {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.getProductos()
                if (response.isSuccessful) {
                    val freshList = response.body() ?: emptyList()
                    _productos.value = freshList
                    filtrarProductos() // Actualizar lista filtrada
                    val currentCart = _cart.value.toMutableMap()
                    val newCart = mutableMapOf<Producto, Int>()
                    freshList.forEach { p ->
                        val oldEntry = currentCart.entries.find { it.key.id == p.id }
                        if (oldEntry != null) { newCart[p] = oldEntry.value }
                    }
                    _cart.value = newCart
                }
            } catch (e: Exception) { _error.value = "Error de conexión: ${e.message}" }
            finally { _loading.value = false }
        }
    }

    fun crearProducto(producto: Producto) {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                if (apiService.crearProducto(producto).isSuccessful) {
                    _error.value = "Producto creado"
                    cargarProductos()
                }
            } catch (e: Exception) { _error.value = "Error: ${e.message}" }
        }
    }

    fun editarProducto(producto: Producto) {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                if (apiService.editarProducto(producto.id, producto).isSuccessful) {
                    _error.value = "Producto actualizado"
                    cargarProductos()
                }
            } catch (e: Exception) { _error.value = "Error: ${e.message}" }
        }
    }

    fun eliminarProducto(id: Int) {
        val role = _currentUser.value?.tipo_usuario_id
        if (role != 1 && role != 2) return

        viewModelScope.launch {
            try {
                if (apiService.eliminarProducto(id).isSuccessful) {
                    _error.value = "Producto eliminado"
                    cargarProductos()
                }
            } catch (e: Exception) { _error.value = "Error: ${e.message}" }
        }
    }

    fun login(nombre: String, contrasena: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.login(mapOf("nombre" to nombre, "contrasena" to contrasena))
                if (response.isSuccessful) {
                    val user = response.body()
                    if (user != null && user.activo) {
                        _isLoggedIn.value = true
                        _currentUser.value = user
                        sessionManager.saveUser(user)
                        onResult(true)
                    } else {
                        _error.value = if (user?.activo == false) "Cuenta desactivada" else "Error de datos"
                        onResult(false)
                    }
                } else {
                    _error.value = "Credenciales incorrectas"
                    onResult(false)
                }
            } catch (e: Exception) {
                _error.value = "Error de conexión"
                onResult(false)
            } finally { _loading.value = false }
        }
    }

    fun agregarUsuario(nombre: String, contrasena: String, email: String, onResult: (String?) -> Unit) {
        val nuevo = Usuario(
            nombre = nombre,
            contrasena = contrasena,
            email = email,
            tipo_usuario_id = 3,
            activo = true,
            fecha_creacion = LocalDate.now().toString()
        )

        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.registrarUsuario(nuevo)

                if (response.isSuccessful) {
                    _error.value = "Registro exitoso"
                    onResult(null) // Éxito total
                } else {
                    // 🔍 Extraemos el cuerpo del error enviado por FastAPI
                    val errorBody = response.errorBody()?.string()

                    // Intentamos capturar si el backend especificó el problema en el mensaje
                    val mensajeError = when {
                        errorBody == null -> "Error al registrar"
                        errorBody.contains("email", ignoreCase = true) || errorBody.contains("correo", ignoreCase = true) -> {
                            "El correo electrónico ya se encuentra registrado"
                        }
                        errorBody.contains("nombre", ignoreCase = true) || errorBody.contains("usuario", ignoreCase = true) -> {
                            "El nombre de usuario ya está en uso"
                        }
                        else -> {
                            // Si FastAPI arroja un mensaje estructurado como {"detail": "..."}
                            if (errorBody.contains("detail")) {
                                errorBody.substringAfter("\"detail\":\"").substringBefore("\"")
                            } else {
                                "Error en el servidor (${response.code()})"
                            }
                        }
                    }

                    _error.value = mensajeError
                    onResult(mensajeError) // Se lo mandamos al formulario para que lo pinte en pantalla
                }
            } catch (e: Exception) {
                val errorNet = "Error de conexión: ${e.localizedMessage}"
                _error.value = errorNet
                onResult(errorNet)
            } finally {
                _loading.value = false
            }
        }
    }

    fun verificarCuenta(email: String, codigo: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.verificarUsuario(mapOf("email" to email, "codigo" to codigo))

                if (response.isSuccessful) {
                    // El backend devuelve un mapa con la clave "mensaje" (ej: "Cuenta verificada con éxito")
                    val msgExito = response.body()?.get("mensaje") ?: "¡Cuenta verificada con éxito!"
                    _error.value = null
                    onResult(true, msgExito)
                } else {
                    // 🔍 Extraemos el JSON de error de FastAPI
                    val errorBody = response.errorBody()?.string()

                    val msgError = when {
                        errorBody == null -> "Error al verificar la cuenta"
                        errorBody.contains("incorrecto", ignoreCase = true) || errorBody.contains("valido", ignoreCase = true) -> {
                            "El código ingresado es incorrecto"
                        }
                        errorBody.contains("expirado", ignoreCase = true) || errorBody.contains("tiempo", ignoreCase = true) -> {
                            "El código ha expirado. Solicita uno nuevo"
                        }
                        else -> {
                            if (errorBody.contains("detail")) {
                                errorBody.substringAfter("\"detail\":\"").substringBefore("\"")
                            } else {
                                "Código inválido o error de validación"
                            }
                        }
                    }

                    _error.value = msgError
                    onResult(false, msgError)
                }
            } catch (e: Exception) {
                val errorNet = "Error de red: No se pudo conectar con el servidor"
                _error.value = errorNet
                onResult(false, errorNet)
            } finally {
                _loading.value = false
            }
        }
    }

    fun solicitarRecuperacion(email: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.solicitarRecuperacion(mapOf("email" to email))
                onResult(response.isSuccessful, response.body()?.get("mensaje") ?: "")
            } catch (e: Exception) { onResult(false, "Error") }
            finally { _loading.value = false }
        }
    }

    fun resetPassword(email: String, codigo: String, nueva: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            _loading.value = true
            try {
                val response = apiService.resetPassword(mapOf("email" to email, "codigo" to codigo, "nueva_contrasena" to nueva))
                onResult(response.isSuccessful, response.body()?.get("mensaje") ?: "")
            } catch (e: Exception) { onResult(false, "Error") }
            finally { _loading.value = false }
        }
    }

    fun actualizarUsuario(usuario: Usuario) {
        viewModelScope.launch {
            try {
                val response = apiService.actualizarUsuario(usuario.id, usuario)
                if (response.isSuccessful) {
                    val updated = response.body()
                    if (updated != null && usuario.id == _currentUser.value?.id) {
                        _currentUser.value = updated
                        sessionManager.saveUser(updated)
                    }
                    _error.value = "Perfil actualizado"
                    cargarUsuarios()
                }
            } catch (e: Exception) { _error.value = "Error" }
        }
    }

    fun logout() {
        _isLoggedIn.value = false
        _currentUser.value = null
        sessionManager.clearSession()
        clearCart()
    }

    fun addToCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        val cartItem = currentCart.keys.find { it.id == producto.id }
        val qty = (cartItem?.let { currentCart[it] } ?: 0) + 1
        if (qty > producto.cantidad) { _error.value = "Sin stock"; return }
        if (cartItem != null) currentCart[cartItem] = qty else currentCart[producto] = 1
        _cart.value = currentCart
    }

    fun removeFromCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        val cartItem = currentCart.keys.find { it.id == producto.id }
        if (cartItem != null) {
            val qty = currentCart[cartItem] ?: 0
            if (qty > 1) currentCart[cartItem] = qty - 1 else currentCart.remove(cartItem)
        }
        _cart.value = currentCart
    }

    fun updateQuantity(producto: Producto, quantity: Int) {
        val currentCart = _cart.value.toMutableMap()
        val cartItem = currentCart.keys.find { it.id == producto.id }
        if (quantity > 0) {
            if (cartItem != null) currentCart[cartItem] = quantity else currentCart[producto] = quantity
        } else cartItem?.let { currentCart.remove(it) }
        _cart.value = currentCart
    }

    fun removeItemFromCart(producto: Producto) {
        val currentCart = _cart.value.toMutableMap()
        val cartItem = currentCart.keys.find { it.id == producto.id }
        cartItem?.let { currentCart.remove(it) }
        _cart.value = currentCart
    }

    fun getCartItems(): List<Pair<Producto, Int>> = _cart.value.map { it.key to it.value }
    fun getTotal(): Double = _cart.value.entries.sumOf { it.key.precio * it.value }
    fun clearCart() { _cart.value = emptyMap() }
}
