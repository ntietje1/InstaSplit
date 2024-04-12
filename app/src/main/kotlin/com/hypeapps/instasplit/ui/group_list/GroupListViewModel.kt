package com.hypeapps.instasplit.ui.group_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupListViewModel(private val repository: InstaSplitRepository) : ViewModel()  {
    private val _state = MutableStateFlow(GroupListState())
    val state: StateFlow<GroupListState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            repository.populateDb()
            val userWithGroupsAndExpenses = repository.getUserWithGroupsAndExpenses(userId = 1)
            updateState(userWithGroupsAndExpenses)
        }
    }

    fun updateState(userWrapper: UserWrapper) {
        _state.value = GroupListState(userWrapper)
    }

//    fun getGroupStatus(group: Group): String {
//
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val application = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY])
                return GroupListViewModel(
                    (application as App).appContainer.repository
                ) as T
            }
        }
    }
}