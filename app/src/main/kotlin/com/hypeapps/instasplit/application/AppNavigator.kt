package com.hypeapps.instasplit.application

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hypeapps.instasplit.ui.camera.CameraMainScreen

private enum class Screen(val route: String) {
    LoginBegin("login_begin"),
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
    NavHost(navController = navController, startDestination = Screen.LoginBegin.route) {
        composable(Screen.LoginBegin.route) {}
        composable(Screen.LoginExisting.route) {}
        composable(Screen.LoginRegister.route) {}
        composable(Screen.GroupList.route) {}
        composable(Screen.GroupEdit.route) {}
        composable(Screen.GroupSingle.route) {}
        composable(Screen.ExpenseEdit.route) {}
        composable(Screen.Camera.route) { CameraMainScreen() }
    }
}