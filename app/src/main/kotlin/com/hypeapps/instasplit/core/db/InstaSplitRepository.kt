package com.hypeapps.instasplit.core.db

import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.network.InstaSplitApi
import com.hypeapps.instasplit.core.utils.LoginRequest
import com.hypeapps.instasplit.core.utils.RegisterRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Response

@OptIn(DelicateCoroutinesApi::class)
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

    init {
        GlobalScope.launch { // TODO: remove this once API can get data
            populateDb()
        }
    }

    private suspend fun populateDb() {
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
        return userDao.getUserWrapper(userId) ?: throw Exception("User not found")
    }

    suspend fun login(loginRequest: LoginRequest): Result<User> {
        return try {
//            val response = api.loginUser(loginRequest)
            val response = Response.success(User(1, "user1", "test@email", "", "password")) //TODO remove this line
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to login user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun register(registerRequest: RegisterRequest): Result<User> {
        return try {
//            val response = api.registerUser(registerRequest)
            val response = Response.success(User(1, "user1", "test@email", "", "password")) //TODO remove this line
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to register user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeUserFromGroup(userId: Int, groupId: Int) {
        groupMemberDao.delete(GroupMember(groupId = groupId, userId = userId))
    }

    suspend fun getOrAddUserByEmail(email: String): User {
        // if no user with given email already exists, add a new user
        var user = userDao.getUserByEmail(email)
        if (user == null) {
            val newUser = User(userName = email, email = email, phoneNumber = "", password = "")
            userDao.addUser(newUser)
            user = userDao.getUserByEmail(email)
        }
        return user ?: throw Exception("Error creating new user")
    }

    suspend fun addUserToGroup(userId: Int, groupId: Int) {
        groupMemberDao.insert(GroupMember(groupId = groupId, userId = userId, isAdmin = false))
    }

    private val groups = listOf(
        Group(groupId = 1, groupName = "Test Group 1"), Group(groupId = 2, groupName = "Test Group 2")
    )
    private val users = listOf(
        User(userId = 1, userName = "User 1", email = "", password = "", phoneNumber = ""),
        User(userId = 2, userName = "User 2", email = "", password = "", phoneNumber = ""),
        User(userId = 3, userName = "User 3", email = "", password = "", phoneNumber = "")
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