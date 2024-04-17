package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.ui.text.input.TextFieldValue
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper

data class GroupEditState(
    val groupWrapper: GroupWrapper = GroupWrapper.placeholder,
    val email: TextFieldValue = TextFieldValue(""),
    val changesMade: List<suspend () -> Unit> = emptyList(),
) {
    val group: Group
        get() = groupWrapper.group
    val users: List<User>
        get() = groupWrapper.users
    val expenses: List<Expense>
        get() = groupWrapper.expenses
}