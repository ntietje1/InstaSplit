package com.hypeapps.instasplit.ui.grouplist

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import com.hypeapps.instasplit.ui.group_list.GroupListViewModel
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.Assert.*


fun createUserWrapper(): UserWrapper {
    val user = User(userId = 1, userName = "John Doe", email = "john.doe@example.com", phoneNumber = "123456789", password = "password123")
    val groups = listOf(
        Group(groupId = 1, groupName = "Family"),
        Group(groupId = 2, groupName = "Friends")
    )
    val expenses = listOf(
        Expense(expenseId = 1, groupId = 1, description = "Dinner Out", date = System.currentTimeMillis(), totalAmount = 120.0),
        Expense(expenseId = 2, groupId = 2, description = "Camping Trip", date = System.currentTimeMillis(), totalAmount = 180.0)
    )
    val userExpenses = listOf(
        UserExpense(userId = 1, expenseId = 1, balance = 60.0),
        UserExpense(userId = 1, expenseId = 2, balance = 90.0)
    )

    return UserWrapper(
        user = user,
        groups = groups,
        expenses = expenses,
        userExpenses = userExpenses
    )
}

@ExperimentalCoroutinesApi
class GroupListViewModelTest {



}

