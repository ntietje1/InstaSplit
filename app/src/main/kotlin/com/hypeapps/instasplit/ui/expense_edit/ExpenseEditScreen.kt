package com.hypeapps.instasplit.ui.expense_edit

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.core.model.entity.bridge.UserWrapper
import com.hypeapps.instasplit.ui.common.InputField


@Composable
fun ExpenseEditScreen(
    expenseId: Int?,
    initialGroupId: Int? = null,
    initialDesc: String? = null,
    initialAmount: Double? = null,
    groupLocked: Boolean = false,
    onDone: () -> Unit,
    onScanReceipt: () -> Unit,
    viewModel: ExpenseEditViewModel = viewModel(factory = ExpenseEditViewModel.Factory)
) {
    val expenseEditState: ExpenseEditState by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        expenseId?.let { viewModel.updateExpenseId(expenseId) }
        viewModel.setUserToCurrentUser()
        viewModel.acceptInitialValues(initialGroupId, initialDesc, initialAmount, groupLocked)
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
            GroupDropdownField(
                userWrapper = expenseEditState.userWrapper,
                initialGroupId = expenseEditState.expenseWrapper.expense.groupId,
                onGroupSelected = { groupId -> viewModel.updateGroupId(groupId) },
                locked = expenseEditState.isGroupLocked
            )
            InputField(
                fieldValue = expenseEditState.descriptionField,
                onTextChanged = { newValue -> viewModel.updateDescriptionField(newValue) },
                placeholder = "Enter Description",
                imageVector = Icons.Default.Description,
                secure = false
            )
            InputField(
                fieldValue = expenseEditState.amountField,
                onTextChanged = { newValue -> viewModel.updateAmountField(newValue) },
                placeholder = "0.00",
                imageVector = Icons.Default.AttachMoney,
                keyboardType = KeyboardType.Number
            )
            Spacer(modifier = Modifier.height(24.dp))
            Surface(
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .fillMaxWidth()
                    .padding(horizontal = 80.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.Transparent,
                shadowElevation = 4.dp
            ) {
                Button(
                    onClick = {
                        if (viewModel.validateExpense()) {
                            viewModel.addExpense()
                            onDone()
                        }
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
                        "ADD EXPENSE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = MaterialTheme.colorScheme.onPrimary
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

@Composable
private fun GroupDropdownField(
    userWrapper: UserWrapper,
    initialGroupId: Int,
    onGroupSelected: (Int) -> Unit,
    locked: Boolean
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedGroupId by remember { mutableIntStateOf(initialGroupId) }
    var selectedGroupName by remember { mutableStateOf("") }

    LaunchedEffect(initialGroupId, userWrapper.groups) {
        selectedGroupId = initialGroupId
        selectedGroupName = userWrapper.groups.find { it.groupId == initialGroupId }?.groupName ?: ""
    }

    Box {
        InputField(
            fieldValue = TextFieldValue(selectedGroupName),
            onTextChanged = {},
            readOnly = true,
            disabled = locked || userWrapper.groups.isEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .clickable { if (!locked) expanded = true },
            placeholder = if (userWrapper.groups.isNotEmpty()) "Select Group" else "You aren't in any groups!",
            imageVector = Icons.Default.Group,
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier.fillMaxWidth()
        ) {
            userWrapper.groups.forEach { group ->
                DropdownMenuItem(
                    text = { Text(group.groupName) },
                    onClick = {
                        selectedGroupId = group.groupId!!
                        selectedGroupName = group.groupName
                        onGroupSelected(group.groupId)
                        expanded = false
                    })
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun ExpenseEditScreenPreview() {
//    ExpenseEditScreen(onBackClick = {}, onDeleteClick = {}, onAddExpense = {}, onScanReceipt = {})
//}
