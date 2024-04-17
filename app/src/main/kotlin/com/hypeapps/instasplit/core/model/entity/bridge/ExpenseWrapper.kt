package com.hypeapps.instasplit.core.model.entity.bridge

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense


data class ExpenseWrapper(
    @Embedded val expense: Expense,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "userId",
        associateBy = Junction(UserExpense::class)
    )
    val users: List<User>,
    @Relation(
        parentColumn = "expenseId",
        entityColumn = "expenseId",
    )
    val userExpenses: List<UserExpense>
) {
    fun getBalance(userId: Int): Double {
        return userExpenses.firstOrNull { it.userId == userId }?.balance ?: 0.0
    }
}
