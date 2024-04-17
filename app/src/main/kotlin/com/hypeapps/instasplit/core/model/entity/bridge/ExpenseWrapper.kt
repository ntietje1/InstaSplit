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
    val evenlySplitAmount: Double
        get() = expense.totalAmount / users.size

    val paidBy: Int?
        get() = userExpenses.firstOrNull { it.balance == expense.totalAmount - evenlySplitAmount}?.userId

    fun getBalanceToUser(currentUser: Int, otherUser: Int): Double {
        val currentUserBalance = getBalance(currentUser)
        val otherUserBalance = getBalance(otherUser)
        if (currentUser == paidBy) {
            return -otherUserBalance
            // other user OWES money to current user
        } else if (otherUser == paidBy) {
            return currentUserBalance
            // current user OWES money to other user
        } else {
            // current user and other user don't owe eachother anything here
            return 0.0
        }
    }

    fun getBalance(userId: Int): Double {
        return userExpenses.firstOrNull { it.userId == userId }?.balance ?: 0.0
    }

}
