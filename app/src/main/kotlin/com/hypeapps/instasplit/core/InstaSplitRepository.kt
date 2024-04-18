package com.hypeapps.instasplit.core

import com.hypeapps.instasplit.core.db.InstaSplitDatabase
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.GroupMember
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.network.ImageResponse
import com.hypeapps.instasplit.core.network.InstaSplitApi
import com.hypeapps.instasplit.core.utils.LoginRequest
import com.hypeapps.instasplit.core.utils.RegisterRequest
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
            val groups = api.getGroups()
            val users = api.getUsers()
            val expenses = api.getExpenses()
            val userExpenses = api.getUserExpenses()
            val groupMembers = api.getGroupMembers()
            groups.forEach { groupDao.addGroup(it) }
            users.forEach { userDao.addUser(it) }
            expenses.forEach { expenseDao.addExpense(it) }
            userExpenses.forEach { userExpenseDao.insert(it) }
            groupMembers.forEach { groupMemberDao.insert(it) }
        }
    }

    fun getGroupWrapper(groupId: Int) = groupDao.getGroupWrapperLiveData(groupId)

    fun getUserWrapper(userId: Int) = userDao.getUserWrapperLiveData(userId)

    fun getExpenseWrapper(expenseId: Int) = expenseDao.getExpenseWrapperLiveData(expenseId)

    suspend fun login(loginRequest: LoginRequest): Result<User> {
        return try {
            val response = api.loginUser(loginRequest)
//            val response = Response.success(User(1, "user1", "test@email", "", "password")) //TODO remove this line
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
            val response = api.registerUser(registerRequest)
//            val response = Response.success(User(1, "user1", "test@email", "", "password")) //TODO remove this line
            if (response.isSuccessful && response.body() != null) {
                userDao.addUser(response.body()!!)
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Failed to register user"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun removeUserFromGroup(userId: Int, groupId: Int) {
       try {
           val response = api.deleteGroupMember(groupId, userId)
           if (response.isSuccessful) {
               groupMemberDao.delete(GroupMember(groupId = groupId, userId = userId))
           }
       }  catch (_: Exception) {

       }
    }

    suspend fun addUserByEmail(email: String): User {
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
        val member = GroupMember(groupId = groupId, userId = userId, isAdmin = false)
        api.addGroupMembers(groupId,member)
        groupMemberDao.insert(member)
    }

    suspend fun editGroup(group: Group) {
        val updatedGroup = group.groupId?.let { api.updateGroupName(it, group) }
        if (updatedGroup != null) {
            groupDao.updateGroup(updatedGroup)
        }
    }


//    suspend fun getBalanceInGroup(userId: Int, groupId: Int): Double {
//        return groupDao.getBalanceInGroup(userId, groupId)
//    }

    suspend fun addOrUpdateExpense(currentUserId: Int, expense: Expense): ExpenseWrapper {
        val expenseId = if (expense.expenseId != null) {
            api.updateExpense(expense.expenseId, expense)
            expenseDao.updateExpense(expense)
            expense.expenseId
        } else {
            val response = api.addExpenses(expense)
            expenseDao.addExpense(response).toInt()
        }
        // get all other users in the group from groupId in expensse
        val otherUserIds = groupMemberDao.getGroupMembers(expense.groupId).map { it.userId }.filter { it != currentUserId }
        val amountOwedPerUser = expense.totalAmount / (otherUserIds.size + 1)
        for (otherUserId in otherUserIds) {
            val userExpense = UserExpense(userId = otherUserId, expenseId = expenseId, balance = 0.00 - amountOwedPerUser)
            api.addUserExpenses(userExpense)
            userExpenseDao.insert(userExpense)
        }
        val userExpense = UserExpense(userId = currentUserId, expenseId = expenseId, balance = expense.totalAmount - amountOwedPerUser)
        api.addUserExpenses(userExpense)
        userExpenseDao.insert(userExpense)
        return expenseDao.getExpenseWrapper(expenseId)
    }

    suspend fun deleteExpense(expenseId: Int) {
        api.deleteExpense(expenseId)
        expenseDao.deleteExpenseById(expenseId)
    }

    suspend fun getUser(userId: Int): User? {
        return userDao.getUserById(userId)
    }

    suspend fun addGroup(group: Group): Int {
        val response = api.addGroups(group)
        return groupDao.addGroup(response).toInt()
    }

    suspend fun getGroup(groupId: Int): Group {
        return groupDao.getGroupById(groupId)
    }

    suspend fun fetchGroupImage() : ImageResponse {
        return api.getRandomGroupImage()
    }

    suspend fun fetchMemberImage() : ImageResponse {
        return api.getRandomMemberImage()
    }

    suspend fun fetchExpenseImage(): ImageResponse {
        return api.getRandomExpenseImage()
    }


}