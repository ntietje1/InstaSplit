package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import com.hypeapps.instasplit.ui.login.InputField

//// Mock data class for a group member
//data class GroupMember(
//    val name: String,
//    val email: String,
//    val status: String, // Indicates whether the member owes money or is owed money
//    val balance: String // The amount of money owed or due
//)
//
//// use this mock data class to create instances of GroupMember for previews or tests:
//val mockMembers = listOf(
//    GroupMember("John Doe", "john.doe@example.com", "owes", "$200.00"),
//    GroupMember("Bob Smith", "bob.smith@example.com", "gets back", "$200.00"),
//    // Add more mock members as needed
//)

// ViewModel to handle the state of the group edit screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditScreen(viewModel: GroupEditViewModel = viewModel(factory = GroupEditViewModel.Factory), groupId: Int) {
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
                    "Edit ${groupEditState.group.groupName}",
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
            // TODO: Handle apply changes (in viewmodel)
        }, containerColor = MaterialTheme.colorScheme.onPrimaryContainer, elevation = FloatingActionButtonDefaults.elevation(defaultElevation = 4.dp) // Apply elevation here
        )
    }, floatingActionButtonPosition = FabPosition.Center

    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            Spacer(modifier = Modifier.height(10.dp))
            MemberHeader(groupEditState.users.size)
            MemberList(groupEditState.users, viewModel::removeMember)
            Spacer(modifier = Modifier.height(20.dp))
            AddNewMemberSection(groupEditState.email, viewModel::updateEmailField, viewModel::addMemberByEmail)
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

// Composable to list all the members in the group
@Composable
fun MemberList(members: List<User>, onRemoveMember: (User) -> Unit) {
    Column {
        members.forEach { user ->
            MemberItem(user, onRemoveMember)
        }
    }
}

// Composable for each member item in the list
@Composable
fun MemberItem(member: User, onRemoveMember: (User) -> Unit) {
    Row(
        verticalAlignment = Alignment.Top, modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp)
                .weight(1f), shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically, modifier = Modifier
                    .background(MaterialTheme.colorScheme.primary)
                    .padding(10.dp)
                    .fillMaxWidth()
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
                    modifier = Modifier
                        .weight(1f)
                        .padding(start = 10.dp)
                ) {
                    Text(
                        text = member.userName, style = MaterialTheme.typography.bodyLarge, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
                    )
                    Text(
                        text = member.email, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onPrimary
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 10.dp), horizontalAlignment = Alignment.End
                ) {
                    val statusText = "owes <not yet implemented>"
//                    val statusText = member.status
//                    val statusColor = if (member.status == "owes") MaterialTheme.colorScheme.error
//                    else MaterialTheme.colorScheme.onPrimaryContainer
                    val balance = "balance <not yet implemented>"

                    Text(
                        text = statusText, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        text = balance, style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
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
    emailState: TextFieldValue, onEmailChanged: (TextFieldValue) -> Unit, onAddMemberByEmail: (String) -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.surfaceTint)
                .padding(30.dp), horizontalAlignment = Alignment.CenterHorizontally
        ) {
            InputField(
                fieldValue = emailState,
                onTextChanged = { newValue -> onEmailChanged(TextFieldValue(newValue.text)) },
                placeholder = "Email",
                imageVector = Icons.Default.Email,
                secure = false,
                keyboardType = KeyboardType.Email
            )

            Surface(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.6f),
                shape = MaterialTheme.shapes.extraLarge,
                color = MaterialTheme.colorScheme.primary,
                shadowElevation = 4.dp,
            ) {
                Button(
                    onClick = {
                        onAddMemberByEmail(emailState.text)
                        onEmailChanged(TextFieldValue(""))
                    }, contentPadding = PaddingValues(vertical = 15.dp), modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "ADD A NEW MEMBER", color = MaterialTheme.colorScheme.onPrimary, style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)
                    )
                }
            }

        }
    }
}


//// Preview for GroupEditScreen
//@Preview(showBackground = true)
//@Composable
//fun PreviewGroupEditScreen() {
//    MaterialTheme {
//        GroupEditScreen(groupId = 1)
//    }
//}