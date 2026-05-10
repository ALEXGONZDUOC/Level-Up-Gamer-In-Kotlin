package com.example.level_up_gamer_android.network

import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import retrofit2.http.*

interface ApiService {
    @GET("usuarios")
    suspend fun getUsuarios(): List<Usuario>

    @POST("usuarios")
    suspend fun registrarUsuario(@Body usuario: Usuario): Usuario

    @PUT("usuarios/{id}")
    suspend fun actualizarUsuario(@Path("id") id: Int, @Body usuario: Usuario): Usuario

    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): Usuario

    @GET("productos")
    suspend fun getProductos(): List<Producto>

    @POST("productos/comprar")
    suspend fun comprarProductos(@Body request: @JvmSuppressWildcards Map<String, Any>): Map<String, String>

    @GET("estadisticas/ventas-totales")
    suspend fun getVentasTotales(@Query("periodo") periodo: String): Map<String, Double>

    @GET("estadisticas/top-productos")
    suspend fun getTopProductos(): List<Producto>
}
