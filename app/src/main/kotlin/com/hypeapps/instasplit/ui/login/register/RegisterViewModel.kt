package com.hypeapps.instasplit.ui.login.register

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class RegisterViewModel : ViewModel() {
    private val _state = MutableStateFlow(RegisterState())
    val state = _state.asStateFlow()

    fun updateName(name: TextFieldValue) {
        _state.value = _state.value.copy(name = name)
    }

    fun updateEmail(email: TextFieldValue) {
        _state.value = _state.value.copy(email = email)
    }

    fun updatePassword(password: TextFieldValue) {
        _state.value = _state.value.copy(password = password)
    }

    fun updatePhoneNumber(phoneNumber: TextFieldValue) {
        _state.value = _state.value.copy(phoneNumber = phoneNumber)
    }

    fun clearState() {
        _state.value = RegisterState()
    }

    fun validate(): Boolean {
//        return _state.value.username == "admin" && _state.value.password == "admin"
        return true
    }
}