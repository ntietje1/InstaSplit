package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper

data class ExpenseEditState(
    val userWrapper: UserWrapper = UserWrapper.placeholder,
    val expenseWrapper: ExpenseWrapper = ExpenseWrapper.placeholder,
    val descriptionField: TextFieldValue = TextFieldValue(""),
    val amountField: TextFieldValue = TextFieldValue(""),
    val isGroupLocked: Boolean = false
)
