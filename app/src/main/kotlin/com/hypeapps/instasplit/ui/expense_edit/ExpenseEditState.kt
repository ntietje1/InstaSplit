package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper

data class ExpenseEditState(
    val userWrapper: UserWrapper = UserWrapper(
        user = User(userId = 0, userName = "", email = "", password = "", phoneNumber = ""),
        groups = emptyList(),
        expenses = emptyList(),
        userExpenses = emptyList(),
    ),
    val expenseWrapper: ExpenseWrapper = ExpenseWrapper(
        expense = Expense(
            expenseId = null,
            groupId = 0,
            description = "",
            totalAmount = 0.0,
        ),
        users = emptyList(),
        userExpenses = emptyList(),
    ),
    val descriptionField: TextFieldValue = TextFieldValue(""),
    val amountField: TextFieldValue = TextFieldValue(""),
    val isGroupLocked: Boolean = false
)
