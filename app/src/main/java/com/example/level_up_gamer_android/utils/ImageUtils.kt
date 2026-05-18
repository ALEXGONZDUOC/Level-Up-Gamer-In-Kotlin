package com.example.level_up_gamer_android.utils

import android.content.Context
import com.example.level_up_gamer_android.R

fun getLocalImageResource(context: Context, codigoProducto: Double): Int {
    val resourceName = "i${codigoProducto.toInt()}"
    val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    // Fallback a una imagen por defecto si no se encuentra el recurso
    return if (resourceId != 0) resourceId else R.drawable.product_placeholder
}

fun getFullImageUrl(imagenUrl: String?): String? {
    if (imagenUrl.isNullOrEmpty()) return null
    if (imagenUrl.startsWith("http")) {
        android.util.Log.d("ImageUtils", "URL Completa detectada: $imagenUrl")
        return imagenUrl
    }
    
    val baseUrl = com.example.level_up_gamer_android.data.network.RetrofitClient.BASE_URL
    val fullUrl = "${baseUrl.trimEnd('/')}/${imagenUrl.removePrefix("/")}"
    android.util.Log.d("ImageUtils", "URL Relativa formateada: $fullUrl (Original: $imagenUrl)")
    return fullUrl
}
