package com.hypeapps.instasplit.ui.login.register

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.utils.RegisterRequest
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.io.IOException

class RegisterViewModel(
    private val instaSplitRepository: InstaSplitRepository, private val userManager: UserManager
) : ViewModel() {
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

    fun updateRegisterResult(result: RegisterResult): RegisterResult {
        _state.value = _state.value.copy(registerResult = result)
        return result
    }

    fun clearState() {
        _state.value = RegisterState()
    }

    private fun validateEmptyFields(): Boolean {
        return _state.value.email.text.isNotEmpty() && _state.value.password.text.isNotEmpty()
    }

    suspend fun login(): RegisterResult {
        if (!validateEmptyFields()) {
            return updateRegisterResult(RegisterResult.EMPTY_FIELDS)
        }

        val res = instaSplitRepository.register(
            RegisterRequest(
                _state.value.name.text, _state.value.email.text, _state.value.password.text, _state.value.phoneNumber.text
            )
        )
        val user = res.getOrNull()
        return if (res.isSuccess && user != null) {
            val userId = user.userId ?: throw Exception("User ID is null")
            userManager.setUserId(userId)
            updateRegisterResult(RegisterResult.SUCCESS)
        } else if (res.exceptionOrNull() is IOException) {
            updateRegisterResult(RegisterResult.NETWORK_ERROR)
        } else {
            updateRegisterResult(RegisterResult.ALREADY_EXISTS)
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return RegisterViewModel(
                    app.appContainer.repository, app.appContainer.userManager
                ) as T
            }
        }
    }
}