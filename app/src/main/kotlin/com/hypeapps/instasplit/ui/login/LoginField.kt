package com.hypeapps.instasplit.ui.login

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text2.BasicSecureTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LoginField(
    modifier: Modifier = Modifier,
    fieldValue: TextFieldValue,
    onTextChanged: (TextFieldValue) -> Unit,
    placeholder: String = "Placeholder",
    imageVector: ImageVector = Icons.Default.QuestionMark,
    secure: Boolean = false
) {
    val focusRequester = remember { FocusRequester() }
    val interactionSource = remember { MutableInteractionSource() }
    val keyboardController = LocalSoftwareKeyboardController.current
    Surface(color = MaterialTheme.colorScheme.onPrimary,
        shape = MaterialTheme.shapes.small,
        shadowElevation = 4.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(80.dp)
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .clickable(interactionSource = interactionSource, indication = null, onClick = {
                focusRequester.requestFocus()
                keyboardController?.show()
            })) {
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
                    imageVector = imageVector, contentDescription = "Email", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(0.dp)
                )
                Spacer(modifier = Modifier.width(10.dp))
                Box {
                    if (secure) {
                        BasicSecureTextField(
                            modifier = Modifier
                                .padding(2.dp)
                                .focusRequester(focusRequester),
                            value = fieldValue.text, onValueChange = {
                                onTextChanged(TextFieldValue(it))
                            }, textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        )
                    } else {
                        BasicTextField(
                            modifier = Modifier
                                .padding(2.dp)
                                .focusRequester(focusRequester),
                            value = fieldValue,
                            onValueChange = onTextChanged,
                            textStyle = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.8f))
                        )
                    }
                    if (fieldValue.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.bodyLarge.copy(color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .padding(2.dp)
                                .focusable(false)
                        )
                    }
                }
            }
        }
    }
}
