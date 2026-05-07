package com.example.level_up_gamer_android.model





data class Producto(
    
    val id: Int = 0,
    val codigo: Double,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int,
    val imagenUrl: String = "",
    val imagenLocal: String = "product_placeholder"
)
