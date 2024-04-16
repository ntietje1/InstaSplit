package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue

data class ExpenseEditState(
    val groupName: TextFieldValue = TextFieldValue(""),
    val description: TextFieldValue = TextFieldValue(""),
    val amount: TextFieldValue = TextFieldValue(""),
    val isGroupLocked: Boolean = false
)
