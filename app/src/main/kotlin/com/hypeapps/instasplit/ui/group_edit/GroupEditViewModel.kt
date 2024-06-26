package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.CreationExtras
import com.hypeapps.instasplit.application.App
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GroupEditViewModel(
    private val repository: InstaSplitRepository,
    private val userManager: UserManager
) : ViewModel() {
    private val _state = MutableStateFlow(GroupEditState())
    val state: StateFlow<GroupEditState> = _state.asStateFlow()
    val userId: Int
        get() = userManager.currentUserId


    suspend fun getMemberImages(): String {
      return repository.fetchMemberImage().url
    }
    fun updateGroupNameField(groupName: TextFieldValue) {
        updateGroupWrapper(_state.value.groupWrapper.copy(group = _state.value.groupWrapper.group.copy(groupName = groupName.text)))
        _state.value = _state.value.copy(groupNameField = groupName)

    }

    fun getBalanceBetweenUsers(otherUser: Int): Double {
        val currentUser = userManager.currentUserId
        return state.value.expenseWrappers.sumOf {
            it.getBalanceToUser(currentUser, otherUser)
        }
    }

    private fun updateGroupWrapper(groupWrapper: GroupWrapper) {
        _state.value = GroupEditState(groupWrapper)
        _state.value = _state.value.copy(groupNameField = TextFieldValue(groupWrapper.group.groupName))
    }

    private fun updateExpenseWrappers(expenses: List<Expense>) {
        expenses.forEach { expense ->
            repository.getExpenseWrapper(expense.expenseId!!)
                .observeForever { expenseWrapper ->
                    expenseWrapper?.let { wrapper ->
                        val currentWrappers = _state.value.expenseWrappers.toMutableList()
                        val existingWrapperIndex = currentWrappers.indexOfFirst { it.expense.expenseId == expense.expenseId }
                        if (existingWrapperIndex != -1) {
                            currentWrappers[existingWrapperIndex] = wrapper
                        } else {
                            currentWrappers.add(wrapper)
                        }
                        _state.value = _state.value.copy(expenseWrappers = currentWrappers)
                    }
                }
        }
    }

    fun validateEmailField(): Boolean {
        return _state.value.emailField.text.isNotEmpty()
    }

    fun updateEmailField(email: TextFieldValue) {
        _state.value = _state.value.copy(emailField = email)
    }

    private fun addChange(change: suspend () -> Unit) {
        _state.value = _state.value.copy(changesMade = _state.value.changesMade + change)
    }

    suspend fun applyChanges() {
        _state.value.changesMade.forEach { it() }
        _state.value = _state.value.copy(changesMade = emptyList())
        repository.editGroup(_state.value.group)
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

    fun removeMember(user: User) {
        val userId = user.userId ?: throw Exception("User does not have an ID")
        val groupWrapper = _state.value.groupWrapper
        val groupId = groupWrapper.group.groupId ?: throw Exception("Group does not have an ID")
        val newUsers = groupWrapper.users.filter { it.userId != userId }
        addChange { repository.removeUserFromGroup(userId, groupId) }
        _state.value = _state.value.copy(groupWrapper = newUsers.let { groupWrapper.copy(users = it) })

    }

    fun addMemberByEmail(email: String) {
        val groupWrapper = _state.value.groupWrapper
        val placeholderUser = User(userName = email, email = email, phoneNumber = "", password = "")
        addChange {
            val newUser = repository.addUserByEmail(email)
            repository.addUserToGroup(newUser.userId!!, groupWrapper.group.groupId!!)
        }
        val newUsers = groupWrapper.users + placeholderUser
        _state.value = _state.value.copy(groupWrapper = groupWrapper.copy(users = newUsers))
    }

    companion object {
        val Factory: ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(modelClass: Class<T>, extras: CreationExtras): T {

                val app = checkNotNull(extras[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY]) as App
                return GroupEditViewModel(
                    app.appContainer.repository,
                    app.appContainer.userManager
                ) as T
            }
        }
    }
}
