package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.db.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupEditViewModel(private val repository: InstaSplitRepository) : ViewModel() {
    private val _state = MutableStateFlow(GroupEditState())
    val state: StateFlow<GroupEditState> = _state.asStateFlow()

//    init {
//        viewModelScope.launch {
//
//        }
//    }

    private fun updateGroupWrapper(groupWrapper: GroupWrapper) {
        _state.value = GroupEditState(groupWrapper)
    }

    fun updateEmailField(email: TextFieldValue) {
        _state.value = _state.value.copy(email = email)
    }

    fun updateGroupId(groupId: Int) {
        viewModelScope.launch {
            val groupWithUsersAndExpenses = repository.getGroupWrapper(groupId)
            updateGroupWrapper(groupWithUsersAndExpenses)
        }
    }

    fun removeMember(user: User) {
        viewModelScope.launch {
            val userId = user.userId ?: throw Exception("User does not have an ID")
            val groupWrapper = _state.value.groupWrapper
            val groupId = groupWrapper.group.groupId ?: throw Exception("Group does not have an ID")
            val newUsers = groupWrapper.users.filter { it.userId != userId }
            repository.removeUserFromGroup(userId, groupId)
            _state.value = _state.value.copy(groupWrapper = groupWrapper.copy(users = newUsers))
        }
    }

    fun addMemberByEmail(email: String) {
        viewModelScope.launch {
            val groupWrapper = _state.value.groupWrapper
            val newUser = repository.getOrAddUserByEmail(email)
            repository.addUserToGroup(newUser.userId!!, groupWrapper.group.groupId!!)
            val newUsers = groupWrapper.users + newUser
            _state.value = _state.value.copy(groupWrapper = groupWrapper.copy(users = newUsers))
        }
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return GroupEditViewModel(
                    app.appContainer.repository,
                ) as T
            }
        }
    }
}
