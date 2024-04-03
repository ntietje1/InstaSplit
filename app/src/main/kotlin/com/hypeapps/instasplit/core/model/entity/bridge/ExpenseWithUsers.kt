package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.User

data class ExpenseWithUsers(
    @Embedded val user: Expense,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "userId"
    )
    val expenses: List<User>
)
