package com.hypeapps.instasplit.ui.login.register

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
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
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
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.ui.login.LoginButton
import com.hypeapps.instasplit.ui.login.LoginField
import com.hypeapps.instasplit.ui.login.LoginTitle
import com.hypeapps.instasplit.ui.login.RememberMeToggle
import com.hypeapps.instasplit.ui.login.existing.LoginScreen

@Composable
fun RegisterScreen(onRegister: () -> Unit, goToLogin: () -> Unit, viewModel: RegisterViewModel = viewModel()) {
    val registerState : RegisterState by viewModel.state.collectAsState()
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            LoginTitle("Sign Up")
            Spacer(modifier = Modifier.height(24.dp))
            LoginField(
                fieldValue = registerState.name,
                onTextChanged = { viewModel.updateName(it) },
                placeholder = "Full Name",
                imageVector = Icons.Default.Person
            )
            LoginField(
                fieldValue = registerState.email,
                onTextChanged = { viewModel.updateEmail(it) },
                placeholder = "Email",
                imageVector = Icons.Default.Email
            )
            LoginField(
                fieldValue = registerState.password,
                onTextChanged = { viewModel.updatePassword(it) },
                placeholder = "Password",
                imageVector = Icons.Default.Lock,
                secure = true
            )
            LoginField(
                fieldValue = registerState.phoneNumber,
                onTextChanged = { viewModel.updatePhoneNumber(it) },
                placeholder = "Phone Number",
                imageVector = Icons.Default.Phone
            )
            Spacer(modifier = Modifier.height(24.dp))
            LoginButton("SIGN UP") {
                if (viewModel.validate()) {
                    onRegister()
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row {
                Text(
                    text = "Already have an account? ",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f),
                )
                Text(
                    text = "Login",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable { goToLogin() }
                )
            }
        }
    }
}

@Preview
@Composable
private fun RegisterScreenPreview() {
    RegisterScreen(onRegister = {}, goToLogin = {})
}

