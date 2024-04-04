package com.hypeapps.instasplit.ui.login.existing

data class LoginState(
    var username: String = "",
    var password: String = "",
    var rememberMe: Boolean = false
)