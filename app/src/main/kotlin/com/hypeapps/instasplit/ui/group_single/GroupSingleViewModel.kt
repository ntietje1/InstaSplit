package com.hypeapps.instasplit.ui.group_single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupSingleViewModel(
    private val repository: InstaSplitRepository,
    private val userManager: UserManager
) : ViewModel() {
    private val _state = MutableStateFlow(GroupSingleState())
    val state: StateFlow<GroupSingleState> = _state.asStateFlow()

    fun getBalance(expenseWrapper: ExpenseWrapper): Double {
        val user = userManager.getUserId()
        return expenseWrapper.getBalance(user)
    }

    private fun updateExpenseWrappers(expenses: List<Expense>) {
        expenses.forEach { expense ->
            repository.getExpenseWrapper(expense.expenseId!!)
                .observeForever { expenseWrapper ->
                    val currentWrappers = _state.value.expenseWrappers.toMutableList()
                    val existingWrapperIndex = currentWrappers.indexOfFirst { it.expense.expenseId == expense.expenseId }
                    if (existingWrapperIndex != -1) {
                        currentWrappers[existingWrapperIndex] = expenseWrapper
                    } else {
                        expenseWrapper?.let {
                            currentWrappers.add(it)
                        }
                    }
                    _state.value = _state.value.copy(expenseWrappers = currentWrappers)
                }
        }
    }

    private fun updateGroupWrapper(groupWrapper: GroupWrapper) {
        _state.value = GroupSingleState(groupWrapper)
    }

    fun updateGroupId(groupId: Int) {
        println("updateGroupId($groupId)")
        viewModelScope.launch {
            repository.getGroupWrapper(groupId).observeForever { groupWrapper ->
                println("updateGroupId($groupId) -> groupWrapper: $groupWrapper")
                updateGroupWrapper(groupWrapper)
                updateExpenseWrappers(groupWrapper.expenses)
            }
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return GroupSingleViewModel(
                    app.appContainer.repository,
                    app.appContainer.userManager
                ) as T
            }
        }
    }
}