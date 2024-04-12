package com.hypeapps.instasplit.ui.group_single

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper

data class GroupSingleState(
    val groupWrapper: GroupWrapper = GroupWrapper(
        group = Group(groupId = 0, groupName = ""),
        users = emptyList(),
        expenses = emptyList(),
    ),
) {
    val group: Group
        get() = groupWrapper.group
    val users: List<User>
        get() = groupWrapper.users
    val expenses: List<Expense>
        get() = groupWrapper.expenses
}