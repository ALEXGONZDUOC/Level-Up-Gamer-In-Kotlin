package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reseñas")
data class Reseñas(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val cliente_id: Int,
    val producto_id: Int,
    val calificacion: Int,
    val comentario: String,
    val fecha: String
)
