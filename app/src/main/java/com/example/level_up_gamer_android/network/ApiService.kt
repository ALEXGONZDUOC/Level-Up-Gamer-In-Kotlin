package com.example.level_up_gamer_android.network

import com.example.level_up_gamer_android.model.*
import retrofit2.http.*

interface ApiService {
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body request: Map<String, String>): Map<String, String>

    @POST("usuarios/verificar")
    suspend fun verificarUsuario(@Body request: Map<String, String>): Map<String, String>

    @POST("usuarios/recuperar")
    suspend fun solicitarRecuperacion(@Body request: Map<String, String>): Map<String, String>

    @POST("usuarios/reset-password")
    suspend fun resetPassword(@Body request: Map<String, String>): Map<String, String>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Usuario

    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): Usuario

    @GET("productos")
    suspend fun getProductos(): List<Producto>

    @GET("usuarios/{usuario_id}/direcciones")
    suspend fun getDirecciones(@Path("usuario_id") usuarioId: Int): List<Direccion>

    @POST("direcciones")
    suspend fun crearDireccion(@Body direccion: Direccion): Map<String, Int>

    @PUT("direcciones/{id}/principal")
    suspend fun marcarPrincipal(@Path("id") id: Int, @Query("usuario_id") usuarioId: Int): Map<String, String>

    @POST("pedidos")
    suspend fun crearPedido(@Body request: PedidoRequest): Map<String, Any>

    @GET("estadisticas/ventas-totales")
    suspend fun getVentasTotales(@Query("periodo") periodo: String): Map<String, Double>

    @GET("estadisticas/top-productos")
    suspend fun getTopProductos(): List<Producto>
}
