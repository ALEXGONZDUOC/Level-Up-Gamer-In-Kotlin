package com.example.level_up_gamer_android.data.network

import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import com.example.level_up_gamer_android.model.Direccion
import retrofit2.Response
import retrofit2.http.*
import retrofit2.http.Body
import retrofit2.http.POST
interface ApiService {
    @GET("productos")
    suspend fun getProductos(): Response<List<Producto>>

    @POST("productos")
    suspend fun crearProducto(@Body producto: Producto): Response<Producto>

    @PUT("productos/{id}")
    suspend fun editarProducto(@Path("id") id: Int, @Body producto: Producto): Response<Producto>

    @DELETE("productos/{id}")
    suspend fun eliminarProducto(@Path("id") id: Int): Response<Unit>

    @GET("usuarios/{usuario_id}/direcciones")
    suspend fun getDirecciones(@Path("usuario_id") usuarioId: Int): Response<List<Direccion>>

    @POST("direcciones")
    suspend fun crearDireccion(@Body direccion: Direccion): Response<Direccion>

    @PUT("direcciones/{id}/principal")
    suspend fun marcarPrincipal(
        @Path("id") id: Int,
        @Query("usuario_id") usuarioId: Int,
        @Body body: Map<String, Boolean>
    ): Response<Map<String, Any>>

    @PUT("direcciones/{id}")
    suspend fun actualizarDireccion(@Path("id") id: Int, @Body direccion: Direccion): Response<Map<String, String>>

    @DELETE("direcciones/{id}")
    suspend fun eliminarDireccion(@Path("id") id: Int): Response<Map<String, String>>

    @POST("pedidos")
    suspend fun crearPedido(@Body pedido: Map<String, @JvmSuppressWildcards Any>): Response<Map<String, Any>>

    @GET("pedidos")
    suspend fun getPedidos(): Response<List<com.example.level_up_gamer_android.model.Pedidos>>

    @GET("pedidos/{pedido_id}/detalles")
    suspend fun getDetallesPedido(@Path("pedido_id") pedidoId: Int): Response<List<com.example.level_up_gamer_android.model.Detalle_Pedido>>

    @GET("usuarios")
    suspend fun getUsuarios(): Response<List<Usuario>>

    @PUT("usuarios/{id}")
    suspend fun adminActualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Response<Usuario>

    @DELETE("usuarios/{id}")
    suspend fun eliminarUsuario(@Path("id") id: Int): Response<Unit>

    @POST("login")
    suspend fun login(@Body credenciales: Map<String, String>): Response<Usuario>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Response<Usuario>

    @POST("usuarios/verificar")
    suspend fun verificarUsuario(@Body request: Map<String, String>): Response<Map<String, String>>

    @POST("usuarios/recuperar")
    suspend fun solicitarRecuperacion(@Body request: Map<String, String>): Response<Map<String, String>>

    @POST("usuarios/reset-password")
    suspend fun resetPassword(@Body request: Map<String, String>): Response<Map<String, String>>

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Response<Usuario>

    // Estadísticas
    @GET("estadisticas/ventas-totales")
    suspend fun getVentasTotales(@Query("periodo") periodo: String): Response<Map<String, Double>>

    @GET("estadisticas/top-productos")
    suspend fun getTopProductos(): Response<List<Producto>>

    @GET("productos/{id}/ventas-por-dia")
    suspend fun getVentasProductoPorDia(@Path("id") id: Int): Response<Map<String, Int>>
}
