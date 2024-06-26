package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.ui.common.InputField
import com.hypeapps.instasplit.ui.login.LoginButton
import com.hypeapps.instasplit.ui.login.LoginTitle
import com.hypeapps.instasplit.ui.login.RememberMeToggle
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    onLogin: () -> Unit,
    goToRegister: () -> Unit,
    viewModel: LoginViewModel = viewModel(factory = LoginViewModel.Factory)) {
    val loginState: LoginState by viewModel.state.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(snackbarHost = {
        SnackbarHost(hostState = snackbarHostState)
    }) { paddingValues ->

        LaunchedEffect(loginState.loginResult) {
            scope.launch {
                when (loginState.loginResult) {
                    LoginResult.EMPTY_FIELDS -> {
                        snackbarHostState.showSnackbar("One or more fields empty")
                    }

                    LoginResult.INVALID_CREDENTIALS -> {
                        snackbarHostState.showSnackbar("Invalid credentials")
                    }

                    LoginResult.NETWORK_ERROR -> {
                        snackbarHostState.showSnackbar("Network error")
                    }

                    else -> {
                        // Do nothing
                    }
                }
            }
        }

        Surface(
            color = MaterialTheme.colorScheme.background, modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.Center
            ) {
                LoginTitle("Login")
                Spacer(modifier = Modifier.height(24.dp))
                InputField(
                    fieldValue = loginState.email, onTextChanged = { viewModel.updateEmail(it) }, placeholder = "Email", imageVector = Icons.Default.Email,  keyboardType = KeyboardType.Email
                )
                InputField(
                    fieldValue = loginState.password, onTextChanged = { viewModel.updatePassword(it) }, placeholder = "Password", imageVector = Icons.Default.Lock, secure = true
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
                ) {
                    Spacer(modifier = Modifier.width(24.dp))
                    RememberMeToggle(checked = loginState.rememberMe, onCheckedChange = {
                        viewModel.updateRememberMe(it)
                    })
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(
                        text = "Remember Me",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                    Spacer(modifier = Modifier.weight(0.4f))
//                Text(
//                    text = "Forgot Password?",
//                    fontSize = 12.sp,
//                    color = MaterialTheme.colorScheme.primary,
//                    modifier = Modifier.clickable { goToForgotPassword() }
//                )
//                Spacer(modifier = Modifier.width(24.dp))
                }
                Spacer(modifier = Modifier.height(12.dp))
                LoginButton {
                    viewModel.viewModelScope.launch {
                        val res = viewModel.login()
                        if (res == LoginResult.SUCCESS) {
                            onLogin()
                            viewModel.clearState()
                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row {
                    Text(
                        text = "Don't have an account? ",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    )
                    Text(text = "Register now", fontSize = 14.sp, color = MaterialTheme.colorScheme.primary, modifier = Modifier.clickable { goToRegister() })
                }
            }
        }
    }
}

@Preview
@Composable
private fun LoginScreenPreview() {
    LoginScreen(onLogin = {}, goToRegister = {})
}
