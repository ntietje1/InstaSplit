package com.hypeapps.instasplit.ui.login

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginTitle(value: String) {
    Text(
        text = value,
        style = MaterialTheme.typography.labelLarge,
        fontWeight = FontWeight.ExtraBold,
        fontSize = 36.sp,
        color = MaterialTheme.colorScheme.onSurface,
    )
}

@Composable
fun RememberMeToggle(checked: Boolean = false, onCheckedChange: (Boolean) -> Unit) {
    Switch(checked = checked, onCheckedChange = onCheckedChange)
}

@Composable
fun LoginButton(text: String = "Login", onLogin: () -> Unit) {
    Surface(
        color = MaterialTheme.colorScheme.primary,
        shape = MaterialTheme.shapes.extraLarge,
        shadowElevation = 4.dp,
        modifier = Modifier
            .fillMaxWidth()
            .height(90.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable {
                onLogin()
            }
    ) {
        Box {
            Text(
                text = text, style = MaterialTheme.typography.labelLarge, fontSize = 28.sp, color = MaterialTheme.colorScheme.onPrimary, modifier = Modifier
                    .padding(16.dp)
                    .align(Alignment.Center)
            )
        }
    }
}

@Preview
@Composable
private fun ExampleLoginTitle() {
    LoginTitle("Login")
}

@Preview
@Composable
private fun ExampleLoginField() {
    LoginField(fieldValue = TextFieldValue(""), onTextChanged = {}, placeholder = "Email", imageVector  = Icons.Default.Email)
}

@Preview
@Composable
private fun ExampleToggleButton() {
    RememberMeToggle(checked = true, onCheckedChange = {})
}

@Preview
@Composable
private fun ExampleLoginButton() {
    LoginButton {}
}