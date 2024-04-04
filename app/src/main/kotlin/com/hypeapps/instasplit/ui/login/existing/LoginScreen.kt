package com.hypeapps.instasplit.ui.login.existing

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.ui.login.LoginButton
import com.hypeapps.instasplit.ui.login.LoginField
import com.hypeapps.instasplit.ui.login.LoginFieldSecure
import com.hypeapps.instasplit.ui.login.LoginTitle
import com.hypeapps.instasplit.ui.login.RememberMeToggle

@Composable
fun LoginScreen(onLogin: () -> Unit, goToRegister: () -> Unit, goToForgotPassword: () -> Unit, viewModel: LoginViewModel = viewModel()) {
    val loginState : LoginState by viewModel.state.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginTitle("Login")
            Spacer(modifier = Modifier.height(24.dp))
            LoginField(
                fieldValue = loginState.username,
                onTextChanged = { viewModel.updateUsername(it) },
                placeholder = "Email",
                imageVector = Icons.Default.Email
            )
            LoginFieldSecure(
                fieldValue = loginState.password,
                onTextChanged = { viewModel.updatePassword(it) },
                placeholder = "Password"
            )
            Spacer(modifier = Modifier.height(2.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Spacer(modifier = Modifier.width(24.dp))
                RememberMeToggle {
                    viewModel.updateRememberMe(it)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = "Remember Me",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
                Spacer(modifier = Modifier.weight(0.4f))
                Text(
                    text = "Forgot Password?",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                    modifier = Modifier.clickable { goToForgotPassword() }
                )
                Spacer(modifier = Modifier.width(24.dp))
            }
            Spacer(modifier = Modifier.height(12.dp))
            LoginButton {
                if (viewModel.validate()) {
                    onLogin()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "Don't have an account? ",
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
                Text(
                    text = "Register now",
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { goToRegister() }
                )
            }
        }
    }
}

@Preview
@Composable
fun LoginScreenPreview() {
    LoginScreen(onLogin = {}, goToRegister = {}, goToForgotPassword = {})
}
