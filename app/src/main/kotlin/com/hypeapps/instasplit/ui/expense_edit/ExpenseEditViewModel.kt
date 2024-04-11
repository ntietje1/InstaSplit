package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ExpenseEditViewModel: ViewModel() {
    private val _state = MutableStateFlow(ExpenseEditState())
    val state: StateFlow<ExpenseEditState> = _state.asStateFlow()

    fun updateGroupName(newGroupName: String) {
        _state.value = _state.value.copy(groupName = TextFieldValue(newGroupName))
    }

    fun updateDescription(newDescription: String) {
        _state.value = _state.value.copy(description = TextFieldValue(newDescription))
    }

    fun updateAmount(newAmount: String) {
        _state.value = _state.value.copy(amount = TextFieldValue(newAmount))
    }
}

