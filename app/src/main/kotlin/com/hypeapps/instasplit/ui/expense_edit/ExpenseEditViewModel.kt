package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ExpenseEditViewModel(
    private val repository: InstaSplitRepository, private val userManager: UserManager
) : ViewModel() {
    private val _state = MutableStateFlow(ExpenseEditState())
    val state: StateFlow<ExpenseEditState> = _state.asStateFlow()

    init {
        setUserToCurrentUser()
    }

    fun acceptInitialValues(
        initialGroupId: Int? = null,
        initialDesc: String? = null,
        initialAmount: Double? = null,
        groupLocked: Boolean = false,
    ) {
        viewModelScope.launch {
            initialGroupId?.let { updateGroupId(it) }
            initialDesc?.let { updateDescriptionField(TextFieldValue(it)) }
            if (initialAmount != null) updateAmountField(TextFieldValue(initialAmount.toString()))
            if (groupLocked) {
                lockGroup()
            }
        }
    }

    fun setUserToCurrentUser() {
        viewModelScope.launch {
            repository.getUserWrapper(userManager.currentUserId).observeForever { userWrapper ->
                updateUserWrapper(userWrapper)
            }
        }
    }

    fun deleteExpense() {
        viewModelScope.launch {
            _state.value.expenseWrapper.expense.expenseId?.let { repository.deleteExpense(it) }
            resetState()
        }
    }

    fun addExpense() {
        //TODO: add way to "settle up"
        viewModelScope.launch {
            repository.addOrUpdateExpense(
                userManager.currentUserId, _state.value.expenseWrapper.expense.copy(
                    date = System.currentTimeMillis()
                )
            )
            resetState()
        }
    }

    //TODO: add validation with toast popup
    fun validateExpense(): Boolean {
        return _state.value.expenseWrapper.expense.totalAmount > 0.0 && _state.value.expenseWrapper.expense.description.isNotBlank()
    }

    fun updateExpenseId(expenseId: Int) {
        viewModelScope.launch {
            repository.getExpenseWrapper(expenseId).observeForever { expenseWrapper ->
                expenseWrapper?.let {
                    updateExpense(it)
                }
            }
        }
    }

    fun updateExpense(expense: ExpenseWrapper) {
        _state.value = _state.value.copy(expenseWrapper = expense)
    }

    fun updateGroupId(groupId: Int) {
        _state.value = _state.value.copy(
            expenseWrapper = _state.value.expenseWrapper.copy(expense = _state.value.expenseWrapper.expense.copy(groupId = groupId))
        )
    }

    fun updateDescriptionField(newDescriptionFieldValue: TextFieldValue) {
        val description = newDescriptionFieldValue.text
        _state.value = _state.value.copy(descriptionField = newDescriptionFieldValue)
        updateDescription(description)
    }

    private fun updateDescription(description: String) {
        _state.value = _state.value.copy(
            expenseWrapper = _state.value.expenseWrapper.copy(expense = _state.value.expenseWrapper.expense.copy(description = description))
        )
    }

    fun updateAmountField(newAmountFieldValue: TextFieldValue) {
        val newAmount = newAmountFieldValue.text
        _state.value = _state.value.copy(amountField = newAmountFieldValue)
        updateAmount(newAmount)
    }

    private fun updateAmount(amount: String) {
        val convertedAmount = amount.toDoubleOrNull()
        //TODO validate here too
        if (convertedAmount != null) {
            _state.value = _state.value.copy(
                expenseWrapper = _state.value.expenseWrapper.copy(expense = _state.value.expenseWrapper.expense.copy(totalAmount = convertedAmount))
            )
        }
    }

    fun resetState() {
        _state.value = ExpenseEditState()
    }

    fun lockGroup() { //TODO this is currently unused
        _state.value = _state.value.copy(isGroupLocked = true)
    }

    fun updateUserWrapper(user: UserWrapper) {
        _state.value = _state.value.copy(userWrapper = user)
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return ExpenseEditViewModel(
                    app.appContainer.repository, app.appContainer.userManager
                ) as T
            }
        }
    }
}


