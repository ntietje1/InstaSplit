package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// ViewModel to handle the state of the group edit screen
class GroupEditViewModel : ViewModel() {
    // StateFlow for group members list
    private val _members = MutableStateFlow(mockMembers) //TODO: not a real data model
    val members: StateFlow<List<GroupMember>> = _members.asStateFlow()

    // StateFlow for new member email input
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    // Function to handle new email input
    fun onEmailChanged(newEmail: String) {
        _email.value = TextFieldValue(newEmail)
    }

    // Function to add a new member to the group
    fun addMember() {
        if (_email.value.text.isNotEmpty()) {
            // Just a placeholder logic for adding a member
            val newMember = GroupMember(
                name = "New Member",
                email = _email.value.text,
                status = "owes", // or "gets back", based on your actual logic
                balance = "$0.00"
            )
            _members.value += newMember
            // Reset email input
            _email.value = TextFieldValue("")
        }
    }

    // Function to apply changes to the group
    fun applyChanges() {
        // TODO: Implement your logic to apply changes to the group
    }

    // Function to remove a member from the group
    fun removeMember(member: GroupMember) {
        _members.value = _members.value.filterNot { it.email == member.email }
    }
}

