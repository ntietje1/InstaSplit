package com.hypeapps.instasplit.core.db

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.network.InstaSplitApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class InstaSplitRepository(
    private val database: InstaSplitDatabase, private val api: InstaSplitApi
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

    suspend fun populateDb() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
            groups.forEach { groupDao.addGroup(it) }
            users.forEach { userDao.addUser(it) }
            expenses.forEach { expenseDao.addExpense(it) }
            userExpenses.forEach { userExpenseDao.insert(it) }
            groupMembers.forEach { groupMemberDao.insert(it) }
        }
    }

    suspend fun getGroupWrapper(groupId: Int): GroupWrapper {
        return groupDao.getGroupWrapper(groupId)
    }

    suspend fun getUserWrapper(userId: Int): UserWrapper {
        return userDao.getUserWrapper(userId)
    }

    private val groups = listOf(
        Group(groupId = 1, groupName = "Test Group 1"), Group(groupId = 2, groupName = "Test Group 2")
    )
    private val users = listOf(
        User(userId = 1, userName = "User 1", email = "", password = ""),
        User(userId = 2, userName = "User 2", email = "", password = ""),
        User(userId = 3, userName = "User 3", email = "", password = "")
    )
    private val expenses = listOf(
        Expense(expenseId = 1, groupId = 1, totalAmount = 90.00), Expense(expenseId = 2, groupId = 2, totalAmount = 200.00), Expense(expenseId = 3, groupId = 2, totalAmount = 100.00)
    )

    private val userExpenses = listOf(
        UserExpense(userId = 1, expenseId = 1, amount = -90.0),
        UserExpense(userId = 1, expenseId = 1, amount = 30.0),
        UserExpense(userId = 2, expenseId = 1, amount = 30.0),
        UserExpense(userId = 3, expenseId = 1, amount = 30.0),
        UserExpense(userId = 1, expenseId = 2, amount = 200.0),
        UserExpense(userId = 1, expenseId = 2, amount = -100.0),
        UserExpense(userId = 1, expenseId = 3, amount = 10.0),
        UserExpense(userId = 1, expenseId = 3, amount = -5.0),
        UserExpense(userId = 2, expenseId = 3, amount = -5.0),
    )

    private val groupMembers = listOf(
        GroupMember(groupId = 1, userId = 1, isAdmin = true),
        GroupMember(groupId = 1, userId = 2, isAdmin = false),
        GroupMember(groupId = 1, userId = 3, isAdmin = false),
        GroupMember(groupId = 2, userId = 1, isAdmin = true),
        GroupMember(groupId = 2, userId = 2, isAdmin = false),
    )


}