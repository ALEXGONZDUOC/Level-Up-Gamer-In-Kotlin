package com.example.level_up_gamer_android.model





data class Pedidos(
    
    val Id: Int = 0,
    val ClienteId: Int,
    val FechaPedido: String,
    val Estado: String,
    val Total: Double
)
