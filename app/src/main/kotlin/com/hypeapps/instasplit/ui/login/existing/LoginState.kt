package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.ui.text.input.TextFieldValue

data class LoginState(
    val email: TextFieldValue = TextFieldValue(""), val password: TextFieldValue = TextFieldValue(""), var rememberMe: Boolean = false, var loginResult: LoginResult = LoginResult.NONE
)

enum class LoginResult {
    NONE, SUCCESS, EMPTY_FIELDS, INVALID_CREDENTIALS, NETWORK_ERROR,
}