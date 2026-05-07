package com.example.level_up_gamer_android.model





data class Reseñas(
    
    val id: Int = 0,
    val cliente_id: Int,
    val producto_id: Int,
    val calificacion: Int,
    val comentario: String,
    val fecha: String
)
