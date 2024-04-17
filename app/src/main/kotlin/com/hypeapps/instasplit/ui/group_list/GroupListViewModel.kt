package com.hypeapps.instasplit.ui.group_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import com.hypeapps.instasplit.core.utils.formatMoney
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

    fun getGroupStatus(groupWrapper: GroupWrapper): String {
        val totalExpense = groupWrapper.expenses.sumOf { it.totalAmount }
        return "Total Expenses: ${totalExpense.formatMoney()}"
    }

    private fun updateState(userWrapper: UserWrapper) {
        _state.value = GroupListState(userWrapper)
        updateGroupWrappers(userWrapper.groups)
    }

    private fun setUserToCurrentUser() {
        viewModelScope.launch {
            repository.getUserWrapper(userManager.currentUserId).observeForever { userWrapper ->
                updateState(userWrapper)
            }
        }
    }

    suspend fun addGroup(): Group {
        return viewModelScope.async {
            val groupId = repository.addGroup(Group(groupName = "New Group"))
            repository.addUserToGroup(userManager.currentUserId, groupId)
            repository.getGroup(groupId)
        }.await()
    }

    private fun updateGroupWrappers(groups: List<Group>) {
        groups.forEach { group ->
            repository.getGroupWrapper(group.groupId!!)
                .observeForever { groupWrapper ->
                    val currentWrappers = _state.value.groupWrappers.toMutableList()
                    val existingWrapperIndex = currentWrappers.indexOfFirst { it.group.groupId == group.groupId }
                    if (existingWrapperIndex != -1) {
                        currentWrappers[existingWrapperIndex] = groupWrapper
                    } else {
                        groupWrapper?.let {
                            currentWrappers.add(it)
                        }
                    }
                    _state.value = _state.value.copy(groupWrappers = currentWrappers)
                }
        }
    }

//    fun getGroupStatus(group: Group): String { TODO
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