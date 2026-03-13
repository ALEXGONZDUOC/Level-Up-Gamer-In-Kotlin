package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "clientes")
data class Clientes(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,
    val usuario_id: Int,
    val nombre_completo: String,
    val telefono: String,
    val direccion: String
)
