package com.hypeapps.instasplit.application

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.gson.Gson
import com.hypeapps.instasplit.ui.camera.CameraMainScreen
import com.hypeapps.instasplit.ui.expense_edit.ExpenseEditScreen
import com.hypeapps.instasplit.ui.group_edit.GroupEditScreen
import com.hypeapps.instasplit.ui.group_list.GroupListScreen
import com.hypeapps.instasplit.ui.group_single.GroupSingleScreen
import com.hypeapps.instasplit.ui.login.existing.LoginScreen
import com.hypeapps.instasplit.ui.login.register.RegisterScreen
import com.hypeapps.instasplit.ui.splash.SplashScreen

private enum class Screen(val route: String) {
    Splash("splash"), LoginExisting("login_existing"), LoginRegister("login_register"), GroupList("group_list"), GroupEdit("group_edit"), // also handles group creation
    GroupSingle("group_single"), ExpenseEdit("expense_edit"), // also handles expense creation
    Camera("camera"),
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(Screen.Splash.route) { SplashScreen { navController.navigate(Screen.LoginExisting.route) } }
        composable(Screen.LoginExisting.route) {
            LoginScreen(onLogin = { navController.navigate(Screen.GroupList.route) }, goToRegister = { navController.navigate(Screen.LoginRegister.route) })
        }
        composable(Screen.LoginRegister.route) {
            RegisterScreen(onRegister = { navController.navigate(Screen.GroupList.route) }, goToLogin = { navController.navigate(Screen.LoginExisting.route) })
        }
        composable(Screen.GroupList.route) {
            GroupListScreen(
                onGroupClick = { group ->
                navController.navigate("${Screen.GroupSingle.route}/${group.groupId}")
            }, onAddExpense = {
                val args = ExpenseEditArgs()
                navController.navigate("${Screen.ExpenseEdit.route}/${Gson().toJson(args)}")
            }, onAddGroup = { group ->
                    navController.navigate("${Screen.GroupEdit.route}/${group.groupId}")
            }
            )
        }
        composable(
            route = Screen.GroupSingle.route + "/{groupId}", arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toIntOrNull() ?: 0
            GroupSingleScreen(groupId = groupId, onAddExpense = { expense ->
                val args = ExpenseEditArgs(
                    expenseId = expense.expenseId,
                    initialDesc = expense.description,
                    initialAmount = expense.totalAmount.toString(),
                    initialGroupId = expense.groupId,
                    groupLocked = true
                )
                navController.navigate("${Screen.ExpenseEdit.route}/${Gson().toJson(args)}")
            }, onEditGroup = { navController.navigate("${Screen.GroupEdit.route}/$groupId") })
        }
        composable(
            route = Screen.GroupEdit.route + "/{groupId}", arguments = listOf(navArgument("groupId") { type = NavType.StringType })
        ) { backStackEntry ->
            val groupId = backStackEntry.arguments?.getString("groupId")?.toIntOrNull() ?: 0
            GroupEditScreen(groupId = groupId) {
                navController.popBackStack()
            }
        }
        composable(
            route = Screen.ExpenseEdit.route + "/{args}", arguments = listOf(navArgument("args") { type = NavType.StringType })
        ) { backStackEntry ->
            val argsJson = backStackEntry.arguments?.getString("args") ?: ""
            val args = Gson().fromJson(argsJson, ExpenseEditArgs::class.java)
            var initialAmount = args.initialAmount?.toDoubleOrNull()
            if (initialAmount != null && initialAmount <= 0) {
                initialAmount = null
            }
            println("initialAmount: $initialAmount")
            ExpenseEditScreen(expenseId = args.expenseId, initialGroupId = args.initialGroupId, initialDesc = args.initialDesc,
                initialAmount = initialAmount,
                groupLocked = args.groupLocked, onDone = { navController.popBackStack() }, onScanReceipt = {
                navController.navigate("${Screen.Camera.route}/${Gson().toJson(args)}")
            })
        }
        composable(
            route = Screen.Camera.route + "/{args}", arguments = listOf(navArgument("args") { type = NavType.StringType })
        ) { backStackEntry ->
            val argsJson = backStackEntry.arguments?.getString("args") ?: ""
            val args = Gson().fromJson(argsJson, ExpenseEditArgs::class.java)
            CameraMainScreen(onResult = { result ->
                args.copy(initialAmount = result.toString()).also {
                    navController.popBackStack()
                    navController.popBackStack()
                    navController.navigate("${Screen.ExpenseEdit.route}/${Gson().toJson(it)}")
                }
            }, onBack = { navController.popBackStack() })
        }
    }
}

private data class ExpenseEditArgs(
    val expenseId: Int? = null, val initialGroupId: Int? = null, val initialDesc: String? = null, val initialAmount: String? = null, val groupLocked: Boolean = false
)
