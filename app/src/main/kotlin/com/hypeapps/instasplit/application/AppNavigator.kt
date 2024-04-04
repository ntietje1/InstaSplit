package com.hypeapps.instasplit.application

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hypeapps.instasplit.ui.camera.CameraMainScreen
import com.hypeapps.instasplit.ui.login.existing.LoginScreen
import com.hypeapps.instasplit.ui.splash.SplashScreen

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
        )} //nick
        composable(Screen.LoginRegister.route) {} //nick
        composable(Screen.GroupList.route) {} //yen
        composable(Screen.GroupEdit.route) {} //khoi
        composable(Screen.GroupSingle.route) {} //yen
        composable(Screen.ExpenseEdit.route) {} //khoi
        composable(Screen.Camera.route) { CameraMainScreen() }
    }
}