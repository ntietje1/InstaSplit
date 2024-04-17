package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense

data class UserWrapper(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "groupId",
        associateBy = Junction(GroupMember::class)
    )
    val groups: List<Group>,
    @Relation(
        parentColumn = "userId",
        entityColumn = "expenseId",
        associateBy = Junction(UserExpense::class)
    )
    val expenses: List<Expense>,
    @Relation(
        parentColumn = "userId",
        entityColumn = "expenseId",
    )
    val userExpenses: List<UserExpense>
) {
    companion object {
        val placeholder: UserWrapper = UserWrapper(
            user = User(userName = "PLACEHOLDER USER", email = "test@email.com", phoneNumber = "", password = ""),
            groups = emptyList(),
            expenses = emptyList(),
            userExpenses = emptyList(),
        )
    }
}