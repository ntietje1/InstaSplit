package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper

data class GroupEditState(
    val groupWrapper: GroupWrapper = GroupWrapper(
        group = Group(groupId = 0, groupName = ""),
        users = emptyList(),
        expenses = emptyList(),
    ),
    val email: TextFieldValue = TextFieldValue("")
) {
    val group: Group
        get() = groupWrapper.group
    val users: List<User>
        get() = groupWrapper.users
    val expenses: List<Expense>
        get() = groupWrapper.expenses
}