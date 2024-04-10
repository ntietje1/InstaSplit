package com.hypeapps.instasplit.ui.group_single

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupSingleScreen(group: Group, expenses: List<Expense>, onAddExpense: () -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.padding(top = 24.dp).size(48.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Group Info", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier.padding(start = 10.dp).padding(top = 24.dp) )
                        Spacer(Modifier.weight(1f))
                        IconButton(
                            onClick = {/* TODO: Handle edit group */ },
                            modifier = Modifier.padding(20.dp).padding(top = 20.dp).size(48.dp) // // right + top padding for the action icon

                        ) {
                            Icon(
                                imageVector = Icons.Filled.Edit,
                                contentDescription = "Edit Group",
                                // Increase the icon size here if needed
                                modifier = Modifier.size(36.dp),
                                tint = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null,
                    Modifier.size(36.dp)) },
                text = { Text("ADD EXPENSE",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)) },
                onClick = onAddExpense
            )
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding).padding(top = 32.dp)) {// Increase this value to move everything down
            GroupInfoCard(group = group)
            ExpensesHeader()
            ExpensesList(expenses = expenses)
        }
    }
}

@Composable
fun GroupInfoCard(group: Group) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp)
    ) {
        Row(Modifier.padding(10.dp), verticalAlignment = Alignment.Top) {
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
                Text(group.name, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                Text("${group.members} members")
                Text("Total expense: ${group.totalExpense}")
            }
        }
    }
}

@Composable
fun ExpensesHeader() {
    Row(Modifier.padding(horizontal = 20.dp, vertical = 8.dp).padding(top = 18.dp), verticalAlignment = Alignment.CenterVertically) {
        Icon(Icons.Default.Receipt, contentDescription = "Expenses", Modifier.size(36.dp))
        Spacer(Modifier.width(8.dp))
        Text("Expenses", fontWeight = FontWeight.Medium, style = MaterialTheme.typography.headlineMedium)
    }
}

@Composable
fun ExpensesList(expenses: List<Expense>) {
    Column {
        expenses.forEach { expense ->
            ExpenseItem(expense = expense)
        }
    }
}

@Composable
fun ExpenseItem(expense: Expense) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(20.dp)
    ) {
        Row(
            Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
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
                Text(expense.title, fontWeight = FontWeight.Bold)
                Text("Total: ${expense.total}")
            }
        }
    }
}

data class Group(val name: String, val members: Int, val totalExpense: String)
data class Expense(val title: String, val total: String)

@Preview(showBackground = true)
@Composable
fun PreviewGroupDetailScreen() {
    MaterialTheme {
        GroupSingleScreen(
            group = Group("Apartment", 2, "$200"),
            expenses = listOf(
                Expense("March Cleaning Supplies", "$100"),
                Expense("March 10 Week Grocery", "$100")
            ),
            onAddExpense = {}
        )
    }
}
