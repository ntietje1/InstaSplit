package com.hypeapps.instasplit.ui.groupsingle

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.hypeapps.instasplit.core.InstaSplitRepository
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.Group
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.core.model.entity.UserExpense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.utils.UserManager
import com.hypeapps.instasplit.ui.group_single.GroupSingleViewModel
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [28])
class GroupSingleViewModelTest {

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private lateinit var repository: InstaSplitRepository
    private lateinit var userManager: UserManager
    private lateinit var viewModel: GroupSingleViewModel
    private val testDispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        repository = mockk(relaxed = true)
        userManager = mockk(relaxed = true)
        viewModel = GroupSingleViewModel(repository, userManager)
    }

   // verifies that when the updateGroupId method is called on the GroupSingleViewModel, it triggers a fetch for group
   // details based on the provided group ID.
    @Test
    fun `updateGroupId should fetch group details and update state`() = runTest {
        val groupId = 1
        val groupWrapper = GroupWrapper(group = mockk(), users = emptyList(), expenses = emptyList())
        val groupWrapperLiveData = MutableLiveData(groupWrapper)

        coEvery { repository.getGroupWrapper(groupId) } returns groupWrapperLiveData

        viewModel.updateGroupId(groupId)

        groupWrapperLiveData.postValue(groupWrapper) // Simulating the repository updating its data

        assertEquals(groupWrapper, viewModel.state.value.groupWrapper)
    }


    // Define the user objects for the tests
    val currentUser = User(
        userId = 1,
        userName = "User1",
        email = "user1@example.com",
        phoneNumber = "1234567890",
        password = "pass1"
    )

    val otherUser = User(
        userId = 2,
        userName = "User2",
        email = "user2@example.com",
        phoneNumber = "0987654321",
        password = "pass2"
    )

    val users = listOf(currentUser, otherUser)

    // checks whether the ViewModel's state is correctly updated with the new user details after the group
    // details have been updated
    @Test
    fun `update group details updates user data`() = runTest {
        val groupId = 1
        val initialGroupWrapper = GroupWrapper(
            group = Group(groupId = groupId, groupName = "Initial Group"),
            users = users,
            expenses = emptyList()
        )
        val groupWrapperLiveData = MutableLiveData(initialGroupWrapper)

        coEvery { repository.getGroupWrapper(groupId) } returns groupWrapperLiveData

        // Simulate updating the group ID which should trigger a state update
        viewModel.updateGroupId(groupId)

        // Now let's assume that the group details including user details are updated
        val updatedUsers = listOf(
            currentUser.copy(email = "new_email@example.com"), // changing the email for the current user
            otherUser
        )
        val updatedGroupWrapper = initialGroupWrapper.copy(users = updatedUsers)
        groupWrapperLiveData.postValue(updatedGroupWrapper)

        // Verify that the state has been updated with the new user details
        assertEquals(updatedUsers, viewModel.state.value.groupWrapper.users)
    }

    //TODO: come back later to this when everything is done
//    @Test
//    fun `state should correctly calculate total expense balances`() = runTest {
//        val currentUser = 1
//        val otherUser = 2
//        val users = listOf(
//            User(userId = currentUser, userName = "User1", email = "user1@example.com", phoneNumber = "1234567890", password = "pass1"),
//            User(userId = otherUser, userName = "User2", email = "user2@example.com", phoneNumber = "0987654321", password = "pass2")
//        )
//
//        val expense1 = Expense(expenseId = 1, totalAmount = 100.0, groupId = 1)
//        val userExpenses1 = listOf(
//            UserExpense(userId = currentUser, expenseId = 1, balance = 70.0),
//            UserExpense(userId = otherUser, expenseId = 1, balance = 30.0)
//        )
//
//        val expense2 = Expense(expenseId = 2, totalAmount = 200.0, groupId = 1)
//        val userExpenses2 = listOf(
//            UserExpense(userId = currentUser, expenseId = 2, balance = 150.0),
//            UserExpense(userId = otherUser, expenseId = 2, balance = 50.0)
//        )
//
//        val expenseWrapper1 = ExpenseWrapper(expense = expense1, users = users, userExpenses = userExpenses1)
//        val expenseWrapper2 = ExpenseWrapper(expense = expense2, users = users, userExpenses = userExpenses2)
//
//        val groupWrapper = GroupWrapper(
//            group = mockk(),
//            users = users,
//            expenses = listOf(expense1, expense2)
//        )
//
//        viewModel.updateState(groupWrapper)
//
//        val expenseWrapperLiveData1 = MutableLiveData(expenseWrapper1)
//        val expenseWrapperLiveData2 = MutableLiveData(expenseWrapper2)
//
//        coEvery { repository.getExpenseWrapper(expense1.expenseId!!) } returns expenseWrapperLiveData1
//        coEvery { repository.getExpenseWrapper(expense2.expenseId!!) } returns expenseWrapperLiveData2
//
//        viewModel.updateGroupId(1) // This triggers the internal LiveData to update
//
//        val totalBalances = viewModel.state.value.expenseBalances.values.sum()
//        assertEquals(300.0, totalBalances, 0.01)
//    }
}
