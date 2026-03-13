package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tipo_usuario")
data class Tipo_Usuarios(
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0,
    var nombre: String,
    var descripcion: String

)
