package com.example.level_up_gamer_android.model

data class Producto(
    val id: Int = 0,
    val codigo: Double,
    val nombre: String,
    val categoria: String,
    val descripcion: String,
    val precio: Double,
    val cantidad: Int,
    val imagenUrl: String? = "",
    val imagenLocal: String? = "product_placeholder",
    val total_vendido: Int = 0
)

data class PedidoRequest(
    val usuario_id: Int,
    val direccion: String,
    val detalles: List<ItemPedido>
)

data class ItemPedido(
    val producto_id: Int,
    val cantidad: Int
)
