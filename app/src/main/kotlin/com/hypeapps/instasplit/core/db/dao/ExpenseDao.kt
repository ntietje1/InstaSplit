package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWithUsers


@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense WHERE expenseId = :expenseId LIMIT 1")
    suspend fun getExpenseById(expenseId: Int): Expense

    @Query("SELECT * FROM expense WHERE groupId = :groupId")
    suspend fun getExpensesByGroupId(groupId: Int): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expense: Expense)

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE expenseId = :expenseId")
    suspend fun deleteExpenseById(expenseId: Int)

    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId LIMIT 1")
    suspend fun getExpenseWithUsers(expenseId: Int): ExpenseWithUsers
}