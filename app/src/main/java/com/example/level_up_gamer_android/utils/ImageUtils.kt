package com.example.level_up_gamer_android.utils

import android.content.Context
import com.example.level_up_gamer_android.R

fun getLocalImageResource(context: Context, codigoProducto: Double): Int {
    val resourceName = "i${codigoProducto.toInt()}"
    val resourceId = context.resources.getIdentifier(resourceName, "drawable", context.packageName)
    // Fallback a una imagen por defecto si no se encuentra el recurso
    return if (resourceId != 0) resourceId else R.drawable.product_placeholder
}
