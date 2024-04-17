package com.hypeapps.instasplit.ui.group_single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
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

    private fun updateState(groupWrapper: GroupWrapper) {
        val expenseBalances = groupWrapper.expenses.associateWith { expense ->
            println("MAPPING EXPENSE: : $expense")
            repository.getExpenseWrapper(expense.expenseId!!).value?.getBalance(userManager.getUserId()) ?: 0.0
        }
        _state.value = GroupSingleState(groupWrapper, expenseBalances)
    }

    fun updateGroupId(groupId: Int) {
        viewModelScope.launch {
            repository.getGroupWrapper(groupId).observeForever { groupWrapper ->
                updateState(groupWrapper)
            }
        }
    }

//    suspend fun expenseToBalance(expense: Expense): Double {
//        val expenseWrapper = repository.getExpenseWrapper(expense.expenseId!!)
//        return expenseWrapper.getBalance(userManager.getUserId())
//    }

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