package com.example.level_up_gamer_android.model

import com.google.gson.annotations.SerializedName

data class Pedidos(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("usuario_id") val usuario_id: Int,
    @SerializedName("direccion") val direccion: String,
    @SerializedName("total") val total: Double,
    @SerializedName("fecha") val fecha: String
)
