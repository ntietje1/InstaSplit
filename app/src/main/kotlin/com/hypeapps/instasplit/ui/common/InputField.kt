package com.hypeapps.instasplit.ui.common

import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InputField(
    modifier: Modifier = Modifier,
    fieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    placeholder: String = "Placeholder",
    imageVector: ImageVector = Icons.Default.QuestionMark,
    secure: Boolean = false,
    readOnly: Boolean = false,
    keyboardType: KeyboardType = KeyboardType.Text // Include this parameter to specify keyboard type
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current

    Surface(
        color = MaterialTheme.colorScheme.onPrimary,
        shape = MaterialTheme.shapes.small,
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .then(
                if (!readOnly) {
                    Modifier.clickable(interactionSource = interactionSource, indication = null, onClick = {
                        focusRequester.requestFocus()
                        keyboardController?.show()
                    })
                } else {
                    Modifier
                }
            )

    ) {
        if (readOnly) {
            Box {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(vertical = 2.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = imageVector, contentDescription = "Icon Description", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(25.dp))
                    Box(modifier = Modifier.padding(2.dp)) {
                        if (fieldValue.text.isNotEmpty()) {
                            Text(text = fieldValue.text, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)))
                        } else {
                            Text(text = placeholder, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)))
                        }
                    }
                }
            }
        } else {
            Box {
                Row(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .fillMaxSize()
                        .padding(vertical = 2.dp, horizontal = 12.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Start
                ) {
                    Spacer(modifier = Modifier.width(4.dp))
                    Icon(
                        imageVector = imageVector, contentDescription = "Icon Description", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(0.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    TextField(
                        value = fieldValue,
                        onValueChange = onTextChanged,
                        textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f)),
                        placeholder = { Text(text = placeholder, style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f))) },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions.Default.copy(keyboardType = keyboardType), // Setting keyboard options
                        visualTransformation = if (secure) PasswordVisualTransformation() else VisualTransformation.None, // Handling password visibility
                        colors = TextFieldDefaults.textFieldColors(
                            containerColor = Color.White,  // Set the container color to white
                            unfocusedIndicatorColor = Color.Transparent, // Hides the underline when not focused
                            focusedIndicatorColor = MaterialTheme.colorScheme.primary, // Shows underline with primary color when focused
                        ),
                        modifier = Modifier
                            .padding(2.dp)
                            .focusRequester(focusRequester)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, name = "LoginField Preview")
@Composable
fun PreviewLoginField() {
    MaterialTheme {
        Column(modifier = Modifier.padding(16.dp)) {
            InputField(
                fieldValue = TextFieldValue(text = "example@example.com"), onTextChanged = {}, placeholder = "Enter your email", imageVector = Icons.Default.Email, keyboardType = KeyboardType.Email
            )
            Spacer(modifier = Modifier.height(16.dp))
            InputField(
                fieldValue = TextFieldValue(text = ""), onTextChanged = {}, placeholder = "Enter your password", imageVector = Icons.Default.Lock, secure = true, keyboardType = KeyboardType.Password
            )
        }
    }
}