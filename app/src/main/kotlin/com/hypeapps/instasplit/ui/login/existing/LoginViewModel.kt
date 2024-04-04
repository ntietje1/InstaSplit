package com.hypeapps.instasplit.ui.login.existing

import androidx.lifecycle.ViewModel
import com.hypeapps.instasplit.ui.login.existing.LoginState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun updateUsername(username: String) {
        _state.value = _state.value.copy(username = username)
    }

    fun updatePassword(password: String) {
        _state.value = _state.value.copy(password = password)
    }

    fun updateRememberMe(rememberMe: Boolean) {
        _state.value = _state.value.copy(rememberMe = rememberMe)
    }

    fun clearState() {
        _state.value = LoginState()
    }

    fun validate(): Boolean {
//        return _state.value.username == "admin" && _state.value.password == "admin"
        return true
    }
}