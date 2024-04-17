package com.hypeapps.instasplit.ui.group_single

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.core.model.entity.Expense
import com.hypeapps.instasplit.core.model.entity.bridge.ExpenseWrapper
import com.hypeapps.instasplit.core.model.entity.bridge.GroupWrapper
import com.hypeapps.instasplit.core.utils.formatMoney

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSingleScreen(
    viewModel: GroupSingleViewModel = viewModel(factory = GroupSingleViewModel.Factory), groupId: Int, onAddExpense: (Expense) -> Unit, onEditGroup: () -> Unit
) {
    val groupSingleState: GroupSingleState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.updateGroupId(groupId)
    }

    Scaffold(topBar = {
        TopAppBar(title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Group, contentDescription = null, modifier = Modifier
                        .padding(top = 24.dp)
                        .size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(Modifier.width(8.dp))
                Text(
                    "Group Info",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(top = 24.dp)
                )
                Spacer(Modifier.weight(1f))
                IconButton(
                    onClick = {
                        onEditGroup()
                              }, modifier = Modifier
                        .padding(20.dp)
                        .padding(top = 20.dp)
                        .size(48.dp) // // right + top padding for the action icon

                ) {
                    Icon(
                        imageVector = Icons.Filled.Edit, contentDescription = "Edit Group", modifier = Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(icon = {
            Icon(
                Icons.Filled.AccountBalanceWallet, contentDescription = null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimary
            )
        }, text = {
            Text(
                "ADD EXPENSE", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
            )
        }, onClick = {
            if (groupSingleState.group.groupId != null) {
                println("Group ID is not null: ${groupSingleState.group.groupId}")
                onAddExpense(Expense(groupId = groupSingleState.group.groupId!!))
            } else {
                println("Group ID is null")
            }
        }, containerColor = MaterialTheme.colorScheme.onPrimaryContainer, elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp  // Adjust the shadow elevation here
        )
        )
    }, floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(top = 32.dp)
        ) {
            GroupInfoCard(
                modifier = Modifier.clickable { onEditGroup() },
                groupSingleState.groupWrapper, groupSingleState.expenseWrappers.sumOf { viewModel.getBalance(it) })
            ExpensesHeader()
            ExpensesList(groupSingleState.expenseWrappers, onAddExpense, viewModel::getBalance)
        }
    }
}

@Composable
fun GroupInfoCard(modifier: Modifier = Modifier, groupWrapper: GroupWrapper, totalBalance: Double) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),

        shape = RoundedCornerShape(20.dp),
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.onPrimaryContainer)
                .padding(10.dp), verticalAlignment = Alignment.Top
        ) {
            Box(
                modifier = Modifier
                    .size(90.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.LightGray) // Placeholder for group image
            ) {
                // TODO: Load actual image here
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Text(
                    groupWrapper.group.groupName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
                )
                Text("${groupWrapper.users.size} members", color = MaterialTheme.colorScheme.onPrimary)
                Text("Total expenses: ${groupWrapper.expenses.sumOf { it.totalAmount }.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary)
                if (totalBalance > 0) Text("You are owed: ${totalBalance.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary)
                else if (totalBalance <= 0) Text("You owe: ${totalBalance.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

@Composable
fun ExpensesHeader() {
    Row(
        Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .padding(top = 18.dp), verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            Icons.Default.Receipt, contentDescription = "Expenses", Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer
        )
        Spacer(Modifier.width(8.dp))
        Text(
            "Expenses", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.headlineMedium, color = MaterialTheme.colorScheme.onPrimaryContainer
        )
    }
}

@Composable
fun ExpensesList(
    expensesWrappers: List<ExpenseWrapper>, onExpenseClicked: (Expense) -> Unit, getBalanceWithUserId: (ExpenseWrapper) -> Double
) {
    LazyColumn {
        items(expensesWrappers) { expenseWrapper ->
            val balance = getBalanceWithUserId(expenseWrapper)
            ExpenseItem(
                modifier = Modifier.clickable { onExpenseClicked(expenseWrapper.expense) },
                description = expenseWrapper.expense.description,
                date = expenseWrapper.expense.formattedDate,
                balance = balance,
                totalAmount = expenseWrapper.expense.totalAmount
            )

        }
        item {
            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}

@Composable
fun ExpenseItem(modifier: Modifier = Modifier, description: String, date: String, balance: Double, totalAmount: Double) {
    Card(
        modifier = modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(), shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(10.dp)
                .fillMaxWidth(), verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp) // Size of the icon/image placeholder
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color.LightGray) // Placeholder color
            ) {
                // Placeholder for the expense type icon/image
                // Replace with actual Image composable when ready
            }
            Spacer(Modifier.width(16.dp))
            Column {
                Row(
                    Modifier
                        .fillMaxWidth()
                        .padding(end = 10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        description, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
                    )
                    Spacer(Modifier.weight(1f))
                    Text(
                        date, color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Text(
                    "Total: ${totalAmount.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary
                )
                if (balance > 0) Text(
                    "You are owed: ${balance.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary
                )
                else if (balance <= 0) Text(
                    "You owe: ${balance.formatMoney()}", color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

