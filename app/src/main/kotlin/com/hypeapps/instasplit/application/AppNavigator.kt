package com.hypeapps.instasplit.application

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.hypeapps.instasplit.ui.camera.CameraMainScreen
import com.hypeapps.instasplit.ui.expense_edit.ExpenseEditScreen
import com.hypeapps.instasplit.ui.group_edit.GroupEditScreen
import com.hypeapps.instasplit.ui.group_list.GroupListScreen
import com.hypeapps.instasplit.ui.login.existing.LoginScreen
import com.hypeapps.instasplit.ui.login.register.RegisterScreen
import com.hypeapps.instasplit.ui.splash.SplashScreen
import com.hypeapps.instasplit.ui.group_list.Group
import com.hypeapps.instasplit.ui.group_single.Expense
import com.hypeapps.instasplit.ui.group_single.GroupSingleScreen

private enum class Screen(val route: String) {
    Splash("splash"),
    LoginExisting("login_existing"),
    LoginRegister("login_register"),
    GroupList("group_list"),
    GroupEdit("group_edit/{groupName}"), // also handles group creation
    GroupSingle("group_single"), // TODO /{groupName} something like that
    ExpenseEdit("expense_edit"), // also handles expense creation
    Camera("camera"),
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen { navController.navigate(Screen.LoginExisting.route) } }
        composable(Screen.LoginExisting.route) { LoginScreen(
            onLogin = { navController.navigate(Screen.GroupList.route) },
            goToRegister  = { navController.navigate(Screen.LoginRegister.route) },
            goToForgotPassword = { /* TODO */ }
        )}
        composable(Screen.LoginRegister.route) { RegisterScreen(
            onRegister = { navController.navigate(Screen.GroupList.route) },
            goToLogin = { navController.navigate(Screen.LoginExisting.route) }
        )}
        composable(Screen.GroupList.route) {
            // Sample placeholder groups for demonstration
            val sampleGroups = listOf(
                Group(name = "Apartment", status = "you owe $120.00"),
                Group(name = "Co-op Group", status = "no expenses"),
                Group(name = "Friends", status = "you are owed $200.00")
            )
            // Placeholder functions for group click and add expense actions
            val onGroupClick: (Group) -> Unit = { group ->
            // When a group is clicked, navigate to GroupSingleScreen
            // with mock data for that specific group.
            navController.navigate("${Screen.GroupSingle.route}/${group.name}") }
//            val onAddExpense: () -> Unit = { /* TODO: Implement add expense action */ }
            GroupListScreen(groups = sampleGroups,
                onGroupClick = onGroupClick,
                onAddExpense = { navController.navigate(Screen.ExpenseEdit.route) })
        } //yen
        composable(Screen.GroupEdit.route) {

        } //khoi
        composable("${Screen.GroupSingle.route}/{groupName}") {
            //TODO: figure out how to navigate from GroupList to GroupSingle, and
            // from GroupSingle to ExpenseEdit -> NEED TO FINALIZE THE MODEL DATA (USING MOCK DATA CLASSES NOW)
            // Here we're just using a mock group and list of expenses for the preview
            val mockGroup =
                com.hypeapps.instasplit.ui.group_single.Group("Apartment", 2, "$200")
            val mockExpenses = listOf(
                Expense("March Cleaning Supplies", "$100"),
                Expense("March 10 Week Grocery", "$100"),
            )
            GroupSingleScreen(
                group = mockGroup,
                expenses = mockExpenses,
                onAddExpense = { navController.navigate(Screen.ExpenseEdit.route)},
                onEditGroup = {navController.navigate("${Screen.GroupEdit.route}/${mockGroup.name}")} // GROUP NAME
            )
        } //yen

        // This route is for navigating to the GroupEditScreen with a groupName parameter
        composable(
            route = Screen.GroupEdit.route,
            arguments = listOf(navArgument("groupName") { type = NavType.StringType })
        ) { backStackEntry ->
            // Here, we fetch the groupName from the backStackEntry
            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
            // using a mock group here for previewing
            val mockGroup = com.hypeapps.instasplit.ui.group_single.Group(
                name = groupName,
                members = 2,
                totalExpense = "$200"
            )

            GroupEditScreen(group = mockGroup)
        }

        composable("${Screen.GroupSingle.route}/{groupName}") { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
            val mockGroup = com.hypeapps.instasplit.ui.group_single.Group(name = groupName, members = 2, totalExpense = "$200")
            val mockExpenses = listOf(
                Expense("March Cleaning Supplies", "$100"),
                Expense("March 10 Week Grocery", "$100"),
            )
            GroupSingleScreen(
                group = mockGroup,
                expenses = mockExpenses,
                onAddExpense = { navController.navigate(Screen.ExpenseEdit.route) },
                onEditGroup = { navController.navigate("group_edit/$groupName") } // Correct navigation action
            )
        }

        // Setup for GroupEditScreen
        composable(
            route = "group_edit/{groupName}", // Ensure this matches the pattern used in the navigation action
            arguments = listOf(navArgument("groupName") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupName = backStackEntry.arguments?.getString("groupName") ?: ""
            val mockGroup = com.hypeapps.instasplit.ui.group_single.Group(name = groupName, members = 2, totalExpense = "$200")
            GroupEditScreen(group = mockGroup)
        }

        composable(Screen.ExpenseEdit.route) {
            ExpenseEditScreen(
                onBackClick = { navController.popBackStack() }, // Move back 1 screen
                onDeleteClick = { /* Handle delete */ },
                onAddExpense = { /* Handle add expense */ }
            )
        } //khoi
         composable(Screen.Camera.route) { CameraMainScreen() }
    }
}