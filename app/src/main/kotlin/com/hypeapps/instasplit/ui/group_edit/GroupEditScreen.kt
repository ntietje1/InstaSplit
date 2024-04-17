package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardColors
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.hypeapps.instasplit.core.model.entity.User
import com.hypeapps.instasplit.ui.common.InputField
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditScreen(viewModel: GroupEditViewModel = viewModel(factory = GroupEditViewModel.Factory), groupId: Int, onBack: () -> Unit) {
    val groupEditState: GroupEditState by viewModel.state.collectAsState()

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
                    "Edit ${groupEditState.group?.groupName}",
                    style = MaterialTheme.typography.headlineLarge,
                    fontWeight = FontWeight.ExtraBold,
                    color = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier
                        .padding(start = 10.dp)
                        .padding(top = 24.dp)
                )
                Spacer(Modifier.weight(1f))
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(icon = {
            Icon(
                Icons.Filled.Done, contentDescription = null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimary
            )
        }, text = {
            Text(
                "APPLY CHANGES", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = MaterialTheme.colorScheme.onPrimary
            )
        }, onClick = {
            CoroutineScope(Dispatchers.IO).launch {
                viewModel.applyChanges()
            }
            onBack()
        }, containerColor = MaterialTheme.colorScheme.onPrimaryContainer, elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp) // Apply elevation here
        )
    }, floatingActionButtonPosition = FabPosition.Center

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(10.dp))
            MemberHeader(groupEditState.users.size)
            LazyColumn(Modifier.fillMaxSize()) {
                item {
                    groupEditState.users.find { it.userId == viewModel.userId }?.let { user ->
                        MemberItem(
                            user,
                            "You",
                            "You are owed $",
                            "You owe total $",
                            viewModel::removeMember,
                            viewModel::getBalanceBetweenUsers
                        )
                    }
                }
                items(groupEditState.users.filter {
                    it.userId != viewModel.userId
                }) { user ->
                    MemberItem(
                        user,
                        null,
                        "You owe $",
                        "Owes you $",
                        viewModel::removeMember,
                        viewModel::getBalanceBetweenUsers
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    AddNewMemberSection(
                        modifier = Modifier.weight(1.0f),
                        emailState = groupEditState.email,
                        onEmailChanged = viewModel::updateEmailField,
                        onAddMemberByEmail = viewModel::addMemberByEmail,
                        validateEmail = viewModel::validateEmailField
                    )
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        }
    }
}

// Composable to display the header with the count of group members
@Composable
fun MemberHeader(memberCount: Int) {
    Text(
        color = MaterialTheme.colorScheme.onPrimaryContainer,
        text = "Group Members ($memberCount)",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier.padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp) // Reduced bottom padding
    )
}

// Composable for each member item in the list
@Composable
fun MemberItem(
    member: User,
    nameOverride: String?,
    negativeBalanceString: String,
    positiveBalanceString: String,
    onRemoveMember: (User) -> Unit,
    getBalanceToCurrentUser: (Int) -> Double
) {
    Row(
        verticalAlignment = Alignment.Top, modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(3.5f)
            .padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp)
                .weight(1f), shape = MaterialTheme.shapes.small,
            colors = CardColors(
                containerColor = MaterialTheme.colorScheme.primary,
                contentColor = MaterialTheme.colorScheme.onPrimary,
                disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
            ),
            elevation = CardDefaults.elevatedCardElevation()
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(0.5f)
                    .padding(10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
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
                Column(
                    modifier = Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
                ) {
                    Text(
                        text = nameOverride ?: member.userName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = member.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    val balance = member.userId?.let { getBalanceToCurrentUser(it) } ?: 0.0
                    if (balance > 0) {
                        val formattedBalance = String.format("%.2f", balance)
                        Text(
                            text = positiveBalanceString + formattedBalance, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        val formattedBalance = String.format("%.2f", -balance)
                        Text(
                            text = negativeBalanceString + formattedBalance, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimary
                        )
                    }
                }
            }
        }
        IconButton(onClick = { onRemoveMember(member) }) {
            Icon(
                Icons.Default.RemoveCircle, contentDescription = "Remove", tint = MaterialTheme.colorScheme.error
            )
        }
    }
}


@Composable
fun AddNewMemberSection(
    modifier: Modifier = Modifier,
    emailState: TextFieldValue, onEmailChanged: (TextFieldValue) -> Unit, onAddMemberByEmail: (String) -> Unit, validateEmail: () -> Boolean
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(1.9f)
            .padding(horizontal = 16.dp),
        colors = CardColors(
            containerColor = MaterialTheme.colorScheme.primary,
            contentColor = MaterialTheme.colorScheme.onPrimary,
            disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
            disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
        ),
        elevation = CardDefaults.elevatedCardElevation()
    ) {
        Column(
            modifier = Modifier
                .padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                fieldValue = emailState,
                onTextChanged = { onEmailChanged(it) },
                placeholder = "Email",
                imageVector = Icons.Default.Email,
                secure = false,
                keyboardType = KeyboardType.Email
            )
            Button(
                colors = ButtonColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    disabledContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                    disabledContentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                ),
                elevation = ButtonDefaults.elevatedButtonElevation(defaultElevation = 8.dp),
                onClick = {
                    if (validateEmail()) {
                        onAddMemberByEmail(emailState.text)
                        onEmailChanged(TextFieldValue(""))
                    }
                },
                contentPadding = PaddingValues(vertical = 15.dp),
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.6f),
            ) {
                Text(
                    "ADD A NEW MEMBER", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                )
            }

        }
    }
}
//
//@Preview
//@Composable
//fun PreviewGroupMemberItem() {
//    MemberItem(User(userName = "very long testing name", email = "someone@gmail.com", phoneNumber = "12894612945", password = "12398567189367"), onRemoveMember = {})
//}
//
//@Preview
//@Composable
//fun PreviewGroupMemberItem2() {
//    MemberItem(User(userName = "name", email = "email", phoneNumber = "1234567890", password = "password"), onRemoveMember = {})
//}
//
//@Preview
//@Composable
//fun PreviewAddNewMemberSection() {
//    MaterialTheme {
//        AddNewMemberSection(emailState = TextFieldValue(""), onEmailChanged = {}, onAddMemberByEmail = {}, validateEmail = { true })
//    }
//}


//// Preview for GroupEditScreen
//@Preview(showBackground = true)
//@Composable
//fun PreviewGroupEditScreen() {
//    MaterialTheme {
//        GroupEditScreen(groupId = 1)
//    }
//}