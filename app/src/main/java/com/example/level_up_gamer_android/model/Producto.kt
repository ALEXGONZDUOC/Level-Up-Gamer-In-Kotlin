package com.example.level_up_gamer_android.model

import com.google.gson.annotations.SerializedName

data class Producto(
    val id: Int = 0,
    val codigo: Double,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int, // Este es el STOCK
    val imagenUrl: String? = "",
    val imagenLocal: String? = "product_placeholder",
    @SerializedName("total_vendido") val total_vendido: Int = 0 // NUEVO: Cantidad real de ventas
)
