package com.hypeapps.instasplit.ui.group_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupListViewModel(
    private val repository: InstaSplitRepository,
    private val userManager : UserManager
) : ViewModel()  {
    private val _state = MutableStateFlow(GroupListState())
    val state: StateFlow<GroupListState> = _state.asStateFlow()

    init {
        setUserToCurrentUser()
    }

    fun updateState(userWrapper: UserWrapper) {
        _state.value = GroupListState(userWrapper)
    }

    private fun setUserToCurrentUser() {
        viewModelScope.launch {
            val userWrapper = repository.getUserWrapper(userManager.getUserId())
            updateState(userWrapper)
        }
    }

    suspend fun addGroup(): Group {
        return viewModelScope.async {
            val groupId = repository.addGroup(Group(groupName = "New Group"))
            repository.getGroup(groupId)
        }.await()
    }

//    fun getGroupStatus(group: Group): String {
//
//    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return GroupListViewModel(
                    app.appContainer.repository,
                    app.appContainer.userManager
                ) as T
            }
        }
    }
}