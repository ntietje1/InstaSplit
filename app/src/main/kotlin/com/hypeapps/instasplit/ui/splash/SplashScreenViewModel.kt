package com.hypeapps.instasplit.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.utils.UserManager

class SplashScreenViewModel(
    private val userManager: UserManager
) : ViewModel() {

    fun checkRememberMe(): Boolean {
        return userManager.rememberMe
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return SplashScreenViewModel(
                     app.appContainer.userManager
                ) as T
            }
        }
    }
}
