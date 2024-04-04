package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class LoginViewModel : ViewModel() {
    private val _state = MutableStateFlow(LoginState())
    val state = _state.asStateFlow()

    fun updateEmail(email: TextFieldValue) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: TextFieldValue) {
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