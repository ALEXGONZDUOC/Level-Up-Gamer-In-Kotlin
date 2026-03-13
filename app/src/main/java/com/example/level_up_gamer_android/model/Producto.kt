package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Producto(
    @PrimaryKey(autoGenerate = true)
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
