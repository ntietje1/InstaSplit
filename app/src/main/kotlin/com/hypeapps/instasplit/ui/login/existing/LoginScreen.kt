//package com.cs4520.assignment5.ui.login
//
//import androidx.compose.runtime.Composable
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.material.Button
//import androidx.compose.material.MaterialTheme
//import androidx.compose.material.OutlinedTextField
//import androidx.compose.material.SnackbarHost
//import androidx.compose.material.SnackbarHostState
//import androidx.compose.material.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.hypeapps.instasplit.ui.login.existing.LoginViewModel
//import kotlinx.coroutines.launch
//
//
//@Composable
//fun LoginScreen(goToProducts: () -> Unit, viewModel: LoginViewModel = viewModel()) {
//    val state by viewModel.state.collectAsState()
//    val snackbarHostState = remember { SnackbarHostState() }
//    val coroutineScope = rememberCoroutineScope()
//
//    Text("Login", style = MaterialTheme.typography.h4, modifier = Modifier.padding(16.dp))
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp), verticalArrangement = Arrangement.Center, horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        OutlinedTextField(value = state.username, onValueChange = { viewModel.updateUsername(it) }, label = { Text("Username") }, modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        OutlinedTextField(value = state.password, onValueChange = { viewModel.updatePassword(it) }, label = { Text("Password") }, modifier = Modifier.fillMaxWidth()
//        )
//        Spacer(modifier = Modifier.height(16.dp))
//        Button(onClick = {
//            if (viewModel.validate()) {
//                coroutineScope.launch {
//                    viewModel.clearState()
//                }
//                goToProducts()
//            } else {
//                coroutineScope.launch {
//                    snackbarHostState.showSnackbar("Incorrect username or password")
//                }
//            }
//        }) {
//            Text("Login")
//        }
//    }
//    Column(
//        modifier = Modifier.fillMaxSize(),
//        verticalArrangement = Arrangement.Bottom,
//        horizontalAlignment = Alignment.CenterHorizontally
//    ) {
//        SnackbarHost(hostState = snackbarHostState)
//        Spacer(modifier = Modifier.height(50.dp))
//    }
//
//}