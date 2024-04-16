package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.utils.LoginRequest
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class LoginViewModel(
    private val instaSplitRepository: InstaSplitRepository, private val userManager: UserManager
) : ViewModel() {
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

    fun updateLoginResult(result: LoginResult): LoginResult {
        _state.value = _state.value.copy(loginResult = result)
        return result
    }

    fun clearState() {
        _state.value = LoginState()
    }

    private fun validateEmptyFields(): Boolean {
        return _state.value.email.text.isNotEmpty() && _state.value.password.text.isNotEmpty()
    }

    suspend fun login(): LoginResult {
        if (!validateEmptyFields()) {
            return updateLoginResult(LoginResult.EMPTY_FIELDS)
        }
        val res = instaSplitRepository.login(
            LoginRequest(
                _state.value.email.text, _state.value.password.text
            )
        )
        val user = res.getOrNull()
        return if (res.isSuccess && user != null) {
            val userId = user.userId ?: throw Exception("User does not have an ID")
            userManager.setUserId(userId)
            updateLoginResult(LoginResult.SUCCESS)
        } else if (res.exceptionOrNull() is IOException) {
            updateLoginResult(LoginResult.NETWORK_ERROR)
        } else {
            updateLoginResult(LoginResult.INVALID_CREDENTIALS)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return LoginViewModel(
                    app.appContainer.repository, app.appContainer.userManager
                ) as T
            }
        }
    }
}