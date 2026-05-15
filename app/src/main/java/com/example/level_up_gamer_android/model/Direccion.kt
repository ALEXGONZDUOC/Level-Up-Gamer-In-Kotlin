package com.example.level_up_gamer_android.model

data class Direccion(
    val id: Int = 0,
    val usuario_id: Int,
    val nombre_etiqueta: String,
    val calle: String,
    val ciudad: String,
    val referencias: String = "",
    val latitud: Double,
    val longitud: Double,
    val es_principal: Boolean = false
)
