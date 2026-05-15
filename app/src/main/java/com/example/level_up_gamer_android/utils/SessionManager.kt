package com.example.level_up_gamer_android.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.level_up_gamer_android.model.Usuario
import com.google.gson.Gson

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("LevelUpSession", Context.MODE_PRIVATE)
    private val gson = Gson()

    fun saveUser(user: Usuario) {
        val userJson = gson.toJson(user)
        prefs.edit().putString("user_data", userJson).apply()
    }

    fun getUser(): Usuario? {
        val userJson = prefs.getString("user_data", null) ?: return null
        return try {
            gson.fromJson(userJson, Usuario::class.java)
        } catch (e: Exception) {
            null
        }
    }

    fun clearSession() {
        prefs.edit().remove("user_data").apply()
    }

    fun isLoggedIn(): Boolean = prefs.contains("user_data")
}
