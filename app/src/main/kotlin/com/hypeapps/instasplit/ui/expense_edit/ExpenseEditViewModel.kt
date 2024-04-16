package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseEditViewModel: ViewModel() {
    private val _state = MutableStateFlow(ExpenseEditState())
    val state: StateFlow<ExpenseEditState> = _state.asStateFlow()

    fun deleteExpense() {
        //TODO: delete expense from db
        resetState()
    }

    fun addExpense() {
        //TODO: add expense to db
        resetState()
    }

    fun updateGroupName(newGroupName: String) {
        val newTextFieldValue = TextFieldValue(
            text = newGroupName,
            selection = androidx.compose.ui.text.TextRange(newGroupName.length)
        )
        _state.value = _state.value.copy(groupName = newTextFieldValue)
    }

    fun updateDescription(newDescription: String) {
        val newTextFieldValue = TextFieldValue(
            text = newDescription,
            selection = androidx.compose.ui.text.TextRange(newDescription.length)
        )
        _state.value = _state.value.copy(description = newTextFieldValue)
    }

    fun updateAmount(newAmount: String) {
        val newTextFieldValue = TextFieldValue(
            text = newAmount,
            selection = androidx.compose.ui.text.TextRange(newAmount.length)
        )
        _state.value = _state.value.copy(amount = newTextFieldValue)
    }

    fun resetState() {
        _state.value = ExpenseEditState()
    }

    fun lockGroup() {
        _state.value = _state.value.copy(isGroupLocked = true)
    }
}


