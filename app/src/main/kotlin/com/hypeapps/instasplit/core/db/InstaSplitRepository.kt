package com.hypeapps.instasplit.core.db

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.network.InstaSplitApi

class InstaSplitRepository(
    private val database: InstaSplitDatabase,
    private val api: InstaSplitApi
    ) {
    private val expenseDao = database.expenseDao()
    private val userExpenseDao = database.userExpenseDao()
    private val groupDao = database.groupDao()
    private val groupMemberDao = database.groupMemberDao()
    private val userDao = database.userDao()

    // implement methods to interact with the database and network here
    // on loading a page, call API to get data, then save to database
    // on updating/deleting data, call (fake) API to update data, then also make change to database
    // UI observes livedata from database

    suspend fun getExpenseById(expenseId: Int): Expense {
        return try {
            val expense = api.getExpenseById(expenseId)
            expenseDao.addExpense(expense)
            expense
        } catch (e: Exception) {
            expenseDao.getExpenseById(expenseId)
        }
    }
}