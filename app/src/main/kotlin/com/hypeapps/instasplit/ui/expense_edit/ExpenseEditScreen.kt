package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Camera
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.EditNote
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.ui.login.LoginField //reuse composable


@Composable
fun ExpenseEditScreen(
    onBackClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onAddExpense: () -> Unit,
    viewModel: ExpenseEditViewModel = viewModel()
) {
    Surface(
        color = MaterialTheme.colorScheme.background,
        modifier = Modifier.fillMaxSize(),
    ) {
        val state by viewModel.state.collectAsState()

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth().padding(top = 8.dp)
            ) {
                IconButton(onClick = { onBackClick() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Back",
                        modifier = Modifier.size(40.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text("Expense", style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold)
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Icon(imageVector = Icons.Default.EditNote, contentDescription = "Edit Expense",
                    modifier = Modifier
                        .padding(top = 0.dp)
                        .size(30.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Expense",
                    style = MaterialTheme.typography.headlineSmall,
                )
                Spacer(modifier = Modifier.width(115.dp))
                IconButton(onClick = { onDeleteClick() }) {
                    Icon(imageVector = Icons.Default.RemoveCircle, contentDescription = "Delete Expense",
                        modifier = Modifier.size(48.dp),
                        tint = MaterialTheme.colorScheme.onPrimaryContainer)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            //Reusing LoginField as input fields in this screen
            LoginField(
                fieldValue = state.groupName,
                onTextChanged = { newValue -> viewModel.updateGroupName(newValue.text) },
                placeholder = "Enter Group Name",
                imageVector = Icons.Default.Group,
                secure = false
            )
            LoginField(
                fieldValue = state.description,
                onTextChanged = { newValue -> viewModel.updateDescription(newValue.text) },
                placeholder = "Enter Description",
                imageVector = Icons.Default.Description,
                secure = false
            )
            LoginField(
                fieldValue = state.amount,
                onTextChanged = { newValue -> viewModel.updateAmount(newValue.text) },
                placeholder = "0.00",
                imageVector = Icons.Default.AttachMoney,
                secure = false
            )
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = { onAddExpense() },
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth() // Ensure the button fills the width of its container
                    .padding(horizontal = 80.dp), // Adjust horizontal padding
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
            ) {
                Icon(
                    Icons.Filled.AccountBalanceWallet, contentDescription = null,
                    Modifier.size(36.dp)
                )
                Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                Text(
                    "ADD EXPENSE",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }

            Spacer(modifier = Modifier.height(80.dp))
            Card(
                modifier = Modifier.fillMaxWidth()
                .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 30.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Try out our AI scanning to add expense faster",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Button(
                        onClick = { /* TODO: Open scanner */ },
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp), // Adjust horizontal padding to match the width of the Add Expense button
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Camera,
                            contentDescription = "Scan Receipt",
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                        Text(
                            "SCAN RECEIPT",
                            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ExpenseEditScreenPreview() {
    ExpenseEditScreen(onBackClick = {}, onDeleteClick = {}, onAddExpense = {})
}
