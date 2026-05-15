package com.example.level_up_gamer_android.model





data class Detalle_Pedido(
    
    val Id: Int = 0,
    val Pedido_Id: Int,
    val Producto_Id: Int,
    val Cantidad: Int,
    val Subtotal: Double
)
