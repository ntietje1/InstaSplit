package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.ui.login.InputField


@Composable
fun ExpenseEditScreen(
    initialGroupId: String? = null,
    initialDesc: String? = null,
    initialAmount: String? = null,
    groupLocked: Boolean = false,
    onDone: () -> Unit,
    onScanReceipt: () -> Unit,
    viewModel: ExpenseEditViewModel = viewModel()
) {
    val expenseEditState: ExpenseEditState by viewModel.state.collectAsState()
    initialGroupId?.let { viewModel.updateGroupName(it) }
    initialDesc?.let { viewModel.updateDescription(it) }
    initialAmount?.let { viewModel.updateAmount(it) }
    if (groupLocked) {
        viewModel.lockGroup()
    }

    Surface(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        modifier = Modifier.fillMaxSize(),
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 8.dp)
            ) {
                IconButton(onClick = { onDone() }, modifier = Modifier.size(48.dp)) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", modifier = Modifier.size(40.dp), tint = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Text(
                    "Expense", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold, color = MaterialTheme.colorScheme.onPrimary
                )
            }
            Spacer(modifier = Modifier.height(24.dp))
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()
            ) {
                Spacer(modifier = Modifier.width(14.dp))
                Icon(
                    imageVector = Icons.Default.EditNote, contentDescription = "Edit Expense", modifier = Modifier
                        .padding(top = 0.dp)
                        .size(30.dp), tint = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Edit Expense", style = MaterialTheme.typography.headlineSmall, color = MaterialTheme.colorScheme.onPrimary
                )
                Spacer(modifier = Modifier.width(115.dp))
                IconButton(onClick = {
                    viewModel.deleteExpense()
                    onDone()
                }) {
                    Icon(
                        imageVector = Icons.Default.RemoveCircle, contentDescription = "Delete Expense", modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.error
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            InputField( //TODO: switch to dropdown menu
                fieldValue = expenseEditState.groupName,
                onTextChanged = { newValue -> viewModel.updateGroupName(newValue.text) },
                placeholder = "Enter Group Name",
                imageVector = Icons.Default.Group,
                secure = false
            )
            InputField(
                fieldValue = expenseEditState.description,
                onTextChanged = { newValue -> viewModel.updateDescription(newValue.text) },
                placeholder = "Enter Description",
                imageVector = Icons.Default.Description,
                secure = false
            )
            InputField(
                fieldValue = expenseEditState.amount,
                onTextChanged = { newValue -> viewModel.updateAmount(newValue.text) },
                placeholder = "0.00",
                imageVector = Icons.Default.AttachMoney,
                keyboardType = KeyboardType.Number  // This will now properly show the numeric keypad
            )
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth() // Ensure the Surface fills the width of its container
                    .padding(horizontal = 80.dp), // Adjust horizontal padding
                shape = RoundedCornerShape(20.dp), // Match the button's shape
                color = Color.Transparent, // Use a transparent color for the Surface
                shadowElevation = 4.dp // Set the shadow elevation
            ) {
                Button(
                    onClick = {
                        viewModel.addExpense()
                        onDone()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 0.dp, vertical = 0.dp),
                    shape = RoundedCornerShape(20.dp),
                    contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp),
                ) {
                    Icon(
                        Icons.Filled.AccountBalanceWallet, contentDescription = null, Modifier.size(36.dp)
                    )
                    Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                    Text(
                        "ADD EXPENSE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = MaterialTheme.colorScheme.onPrimary // Ensure text color is onPrimary for contrast
                    )
                }
            }


            Spacer(modifier = Modifier.height(40.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Column(
                    modifier = Modifier.padding(horizontal = 40.dp, vertical = 30.dp), horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "Try out our AI scanning to add expense faster",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(25.dp))
                    Surface(
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .padding(horizontal = 30.dp), // Match the button's horizontal padding
                        shape = RoundedCornerShape(20.dp), // Match the button's shape
                        color = Color.Transparent, // Transparent surface to only show the shadow
                        shadowElevation = 4.dp
                    ) {
                        Button(
                            onClick = { onScanReceipt() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(0.dp),
                            shape = RoundedCornerShape(20.dp), // Maintain the button's shape
                            contentPadding = PaddingValues(horizontal = 20.dp, vertical = 12.dp), // Maintain content padding
                        ) {
                            Icon(
                                imageVector = Icons.Default.Camera, contentDescription = "Scan Receipt", modifier = Modifier.size(36.dp)
                            )
                            Spacer(Modifier.size(ButtonDefaults.IconSpacing))
                            Text(
                                "SCAN RECEIPT",
                                style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                                color = MaterialTheme.colorScheme.onPrimary // Ensure text color is onPrimary for contrast
                            )
                        }
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ExpenseEditScreenPreview() {
//    ExpenseEditScreen(onBackClick = {}, onDeleteClick = {}, onAddExpense = {}, onScanReceipt = {})
//}
