//package com.hypeapps.instasplit.ui.groupsingle
//
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import androidx.lifecycle.MutableLiveData
//import com.hypeapps.instasplit.core.InstaSplitRepository
//import com.hypeapps.instasplit.core.model.entity.Expense
//import com.hypeapps.instasplit.core.model.entity.User
//import com.hypeapps.instasplit.core.model.entity.UserExpense
//import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
//import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
//import com.hypeapps.instasplit.core.utils.UserManager
//import com.hypeapps.instasplit.ui.group_single.GroupSingleViewModel
//import io.mockk.coEvery
//import io.mockk.mockk
//import kotlinx.coroutines.test.StandardTestDispatcher
//import kotlinx.coroutines.test.runTest
//import org.junit.*
//import org.junit.Assert.assertEquals
//import org.junit.runner.RunWith
//import org.robolectric.RobolectricTestRunner
//import org.robolectric.annotation.Config
//
//@RunWith(RobolectricTestRunner::class)
//@Config(sdk = [28])
//class GroupSingleViewModelTest {
//
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()
//
//    private lateinit var repository: InstaSplitRepository
//    private lateinit var userManager: UserManager
//    private lateinit var viewModel: GroupSingleViewModel
//    private val testDispatcher = StandardTestDispatcher()
//
//    @Before
//    fun setup() {
//        repository = mockk(relaxed = true)
//        userManager = mockk(relaxed = true)
//        viewModel = GroupSingleViewModel(repository, userManager)
//    }
//
//    // verify that when the updateGroupId method is called on your GroupSingleViewModel, it triggers a fetch for group details based on the provided group ID,
//    // and subsequently updates the ViewModel's state with the fetched data.
//    @Test
//    fun `updateGroupId should fetch group details and update state`() = runTest {
//        val groupId = 1
//        val groupWrapper = GroupWrapper(group = mockk(), users = emptyList(), expenses = emptyList())
//        val groupWrapperLiveData = MutableLiveData(groupWrapper)
//
//        coEvery { repository.getGroupWrapper(groupId) } returns groupWrapperLiveData
//
//        viewModel.updateGroupId(groupId)
//
//        groupWrapperLiveData.postValue(groupWrapper) // Simulating the repository updating its data
//
//        assertEquals(groupWrapper, viewModel.state.value.groupWrapper)
//    }
//
//    //TODO: come back later to this when everything is done
////    @Test
////    fun `state should correctly calculate total expense balances`() = runTest {
////        val currentUser = 1
////        val otherUser = 2
////        val users = listOf(
////            User(userId = currentUser, userName = "User1", email = "user1@example.com", phoneNumber = "1234567890", password = "pass1"),
////            User(userId = otherUser, userName = "User2", email = "user2@example.com", phoneNumber = "0987654321", password = "pass2")
////        )
////
////        val expense1 = Expense(expenseId = 1, totalAmount = 100.0, groupId = 1)
////        val userExpenses1 = listOf(
////            UserExpense(userId = currentUser, expenseId = 1, balance = 70.0),
////            UserExpense(userId = otherUser, expenseId = 1, balance = 30.0)
////        )
////
////        val expense2 = Expense(expenseId = 2, totalAmount = 200.0, groupId = 1)
////        val userExpenses2 = listOf(
////            UserExpense(userId = currentUser, expenseId = 2, balance = 150.0),
////            UserExpense(userId = otherUser, expenseId = 2, balance = 50.0)
////        )
////
////        val expenseWrapper1 = ExpenseWrapper(expense = expense1, users = users, userExpenses = userExpenses1)
////        val expenseWrapper2 = ExpenseWrapper(expense = expense2, users = users, userExpenses = userExpenses2)
////
////        val groupWrapper = GroupWrapper(
////            group = mockk(),
////            users = users,
////            expenses = listOf(expense1, expense2)
////        )
////
////        viewModel.updateState(groupWrapper)
////
////        val expenseWrapperLiveData1 = MutableLiveData(expenseWrapper1)
////        val expenseWrapperLiveData2 = MutableLiveData(expenseWrapper2)
////
////        coEvery { repository.getExpenseWrapper(expense1.expenseId!!) } returns expenseWrapperLiveData1
////        coEvery { repository.getExpenseWrapper(expense2.expenseId!!) } returns expenseWrapperLiveData2
////
////        viewModel.updateGroupId(1) // This triggers the internal LiveData to update
////
////        val totalBalances = viewModel.state.value.expenseBalances.values.sum()
////        assertEquals(300.0, totalBalances, 0.01)
////    }
//}
