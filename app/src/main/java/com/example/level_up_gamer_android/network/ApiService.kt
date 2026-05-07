package com.example.level_up_gamer_android.network

import com.example.level_up_gamer_android.model.Producto
import com.example.level_up_gamer_android.model.Usuario
import retrofit2.http.*

interface ApiService {
    @POST("login")
    suspend fun login(@Body credentials: Map<String, String>): Usuario

    @GET("productos")
    suspend fun getProductos(): List<Producto>
}
