package com.hypeapps.instasplit.ui.group_single

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupSingleViewModel(private val repository: InstaSplitRepository): ViewModel() {
    private val _state = MutableStateFlow(GroupSingleState())
    val state: StateFlow<GroupSingleState> = _state.asStateFlow()

    fun updateState(groupWrapper: GroupWrapper) {
        _state.value = GroupSingleState(groupWrapper)
    }

    fun updateGroupId(groupId: Int) {
        viewModelScope.launch {
            val groupWithUsersAndExpenses = repository.getGroupWrapper(groupId)
            updateState(groupWithUsersAndExpenses)
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return GroupSingleViewModel(
                    app.appContainer.repository
                ) as T
            }
        }
    }
}