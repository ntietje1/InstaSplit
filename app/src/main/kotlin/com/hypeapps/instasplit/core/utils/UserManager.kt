package com.hypeapps.instasplit.core.utils

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserManager", Context.MODE_PRIVATE)

    var currentUserId: Int
        get() = sharedPreferences.getInt("userId", -1)
        set(value) {
            sharedPreferences.edit().putInt("userId", value).apply()
        }

    var rememberMe: Boolean
        get() = sharedPreferences.getBoolean("rememberMe", false)
        set(value) {
            sharedPreferences.edit().putBoolean("rememberMe", value).apply()
        }

}

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(val email: String, val phoneNumber: String, val password: String, val username: String)