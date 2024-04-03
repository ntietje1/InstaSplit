package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.User

data class UserWithExpenses(
    @Embedded val user: User,
    @Relation(
        parentColumn = "userId",
        entityColumn = "expenseId"
    )
    val expenses: List<Expense>
)