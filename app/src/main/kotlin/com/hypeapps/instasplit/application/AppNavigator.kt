package com.hypeapps.instasplit.application

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hypeapps.instasplit.ui.camera.CameraMainScreen
import com.hypeapps.instasplit.ui.group_list.GroupListScreen
import com.hypeapps.instasplit.ui.login.existing.LoginScreen
import com.hypeapps.instasplit.ui.login.register.RegisterScreen
import com.hypeapps.instasplit.ui.splash.SplashScreen
import com.hypeapps.instasplit.ui.group_list.Group

private enum class Screen(val route: String) {
    Splash("splash"),
    LoginExisting("login_existing"),
    LoginRegister("login_register"),
    GroupList("group_list"),
    GroupEdit("group_edit"), // also handles group creation
    GroupSingle("group_single"),
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
            val onGroupClick: (Group) -> Unit = { /* TODO: Implement group click action */ }
            val onAddExpense: () -> Unit = { /* TODO: Implement add expense action */ }
            GroupListScreen(groups = sampleGroups, onGroupClick = onGroupClick, onAddExpense = onAddExpense)
        } //yen
        composable(Screen.GroupEdit.route) {} //khoi
        composable(Screen.GroupSingle.route) {} //yen
        composable(Screen.ExpenseEdit.route) {} //khoi
        composable(Screen.Camera.route) { CameraMainScreen() }
    }
}