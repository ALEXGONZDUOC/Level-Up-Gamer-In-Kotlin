package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "usuario")
data class Usuario(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val nombre: String,
    val contrasena: String,
    val email: String,
    val tipo_usuario_id: Int,
    val activo: Boolean = true,
    val fecha_creacion: String
)
