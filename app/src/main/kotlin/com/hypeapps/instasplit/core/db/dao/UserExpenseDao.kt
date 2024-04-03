package com.hypeapps.instasplit.core.db.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import com.hypeapps.instasplit.core.model.entity.UserExpense

@Dao
interface UserExpenseDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(userExpense: UserExpense)

    @Delete
    suspend fun delete(userExpense: UserExpense)
}