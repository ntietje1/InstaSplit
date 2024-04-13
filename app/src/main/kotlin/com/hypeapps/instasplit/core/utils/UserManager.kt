package com.hypeapps.instasplit.core.utils

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("UserManager", Context.MODE_PRIVATE)

    fun getUserId(): Int {
        return sharedPreferences.getInt("userId", -1)
    }

    fun setUserId(userId: Int) {
        sharedPreferences.edit().putInt("userId", userId).apply()
    }

    fun clearUserId() {
        sharedPreferences.edit().remove("userId").apply()
    }
}

data class LoginRequest(val email: String, val password: String)

data class RegisterRequest(val email: String, val phoneNumber: String, val password: String, val username: String)