package com.hypeapps.instasplit.core.db.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper


@Dao
interface ExpenseDao {

    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseById(expenseId: Int): Expense

    @Query("SELECT * FROM expense WHERE groupId = :groupId")
    suspend fun getExpensesByGroupId(groupId: Int): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExpense(expense: Expense): Long

    @Update
    suspend fun updateExpense(expense: Expense)

    @Query("DELETE FROM expense WHERE expenseId = :expenseId")
    suspend fun deleteExpenseById(expenseId: Int)

    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    suspend fun getExpenseWrapper(expenseId: Int): ExpenseWrapper

    @Transaction
    @Query("SELECT * FROM expense WHERE expenseId = :expenseId")
    fun getExpenseWrapperLiveData(expenseId: Int): LiveData<ExpenseWrapper>
}