package com.hypeapps.instasplit.ui.group_list

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper

data class GroupListState(
    val userWrapper: UserWrapper = UserWrapper.placeholder,
    val groupWrappers: List<GroupWrapper> = emptyList(),
) {
    val user: User
        get() = userWrapper.user
    val groups: List<Group>
        get() = userWrapper.groups
    val expenses: List<Expense>
        get() = userWrapper.expenses
}