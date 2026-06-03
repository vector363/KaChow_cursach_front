package com.example.kachow_cursach.data.local

import android.content.Context
import android.content.SharedPreferences


class TokenManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("auth", Context.MODE_PRIVATE)

    fun saveAuthData(token: String, userId: Int, username: String, email: String, role: String) {
        prefs.edit().apply {
            putString("token", token)
            putInt("userId", userId)
            putString("username", username)
            putString("email", email)
            putString("role", role)
            apply()
        }
    }

    fun getToken(): String? {
        return prefs.getString("token", null)
    }

    fun getUserRole(): String {
        return prefs.getString("role", "user") ?: "user"
    }

    fun clearToken() {
        prefs.edit().clear().apply()
    }

    fun clearAuth() {
        prefs.edit().apply {
            remove("token")
            remove("isLoggedIn")
            remove("loginTimestamp")
            apply()
        }
    }

    fun isLoggedIn(): Boolean {
        return getToken() != null
    }

    fun isAdmin(): Boolean {
        return getUserRole() == "admin"
    }

}