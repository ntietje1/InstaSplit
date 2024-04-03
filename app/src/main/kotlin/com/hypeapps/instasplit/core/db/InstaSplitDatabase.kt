package com.hypeapps.instasplit.core.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hypeapps.instasplit.core.db.dao.ExpenseDao
import com.hypeapps.instasplit.core.db.dao.GroupDao
import com.hypeapps.instasplit.core.db.dao.GroupMemberDao
import com.hypeapps.instasplit.core.db.dao.UserDao
import com.hypeapps.instasplit.core.db.dao.UserExpenseDao
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense

@Database(
    entities = [
        Group::class,
        GroupMember::class,
        User::class,
        Expense::class,
        UserExpense::class,
    ], version = 1
)
abstract class InstaSplitDatabase : RoomDatabase() {
    abstract fun groupDao(): GroupDao
    abstract fun groupMemberDao(): GroupMemberDao
    abstract fun userDao(): UserDao
    abstract fun expenseDao(): ExpenseDao
    abstract fun userExpenseDao(): UserExpenseDao
}