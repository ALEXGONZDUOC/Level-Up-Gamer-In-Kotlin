package com.example.level_up_gamer_android.model

import com.google.gson.annotations.SerializedName

data class Pedidos(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("usuario_id") val usuario_id: Int,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("total") val total: Double,
    @SerializedName("fecha") val fecha: String,
    @SerializedName("detalles") val detalles: List<DetallePedido>? = emptyList()
)

data class DetallePedido(
    @SerializedName("producto_id") val producto_id: Int,
    @SerializedName("nombre_producto") val nombre_producto: String? = null,
    @SerializedName("cantidad") val cantidad: Int,
    @SerializedName("precio_unitario") val precio_unitario: Double
)
