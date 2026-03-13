package com.example.level_up_gamer_android.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "pedidos")
data class Pedidos(
    @PrimaryKey(autoGenerate = true)
    val Id: Int = 0,
    val ClienteId: Int,
    val FechaPedido: String,
    val Estado: String,
    val Total: Double
)
