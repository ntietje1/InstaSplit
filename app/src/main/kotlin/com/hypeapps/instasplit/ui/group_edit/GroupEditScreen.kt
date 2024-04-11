package com.hypeapps.instasplit.ui.group_edit

import androidx.compose.foundation.background
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.ui.unit.dp
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Button
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import com.hypeapps.instasplit.ui.group_single.Group
import com.hypeapps.instasplit.ui.login.LoginField
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Mock data class for a group member
data class GroupMember(
    val name: String,
    val email: String,
    val status: String, // Indicates whether the member owes money or is owed money
    val balance: String // The amount of money owed or due
)

// use this mock data class to create instances of GroupMember for previews or tests:
val mockMembers = listOf(
    GroupMember("John Doe", "john.doe@example.com", "owes", "$200.00"),
    GroupMember("Bob Smith", "bob.smith@example.com", "gets back", "$200.00"),
    // Add more mock members as needed
)

// ViewModel to handle the state of the group edit screen
class GroupEditViewModel : ViewModel() {
    // StateFlow for group members list
    private val _members = MutableStateFlow(mockMembers) //TODO: not a real data model
    val members: StateFlow<List<GroupMember>> = _members.asStateFlow()

    // StateFlow for new member email input
    private val _email = MutableStateFlow(TextFieldValue(""))
    val email: StateFlow<TextFieldValue> = _email.asStateFlow()

    // Function to handle new email input
    fun onEmailChanged(newEmail: String) {
        _email.value = TextFieldValue(newEmail)
    }

    // Function to add a new member to the group
    fun addMember() {
        if (_email.value.text.isNotEmpty()) {
            // Just a placeholder logic for adding a member
            val newMember = GroupMember(
                name = "New Member",
                email = _email.value.text,
                status = "owes", // or "gets back", based on your actual logic
                balance = "$0.00"
            )
            _members.value += newMember
            // Reset email input
            _email.value = TextFieldValue("")
        }
    }

    // Function to apply changes to the group
    fun applyChanges() {
        // TODO: Implement your logic to apply changes to the group
    }

    // Function to remove a member from the group
    fun removeMember(member: GroupMember) {
        _members.value = _members.value.filterNot { it.email == member.email }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditScreen(viewModel: GroupEditViewModel = viewModel(), group: Group) {
    val members by viewModel.members.collectAsState()
    val emailState by viewModel.email.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier
                            .padding(top = 24.dp)
                            .size(48.dp))
                        Spacer(Modifier.width(8.dp))
                        Text("Edit ${group.name}", style = MaterialTheme.typography.headlineLarge, fontWeight = FontWeight.ExtraBold,
                            modifier = Modifier
                                .padding(start = 10.dp)
                                .padding(top = 24.dp) )
                        Spacer(Modifier.weight(1f))
                    }
                }
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = { Icon(Icons.Filled.Done, contentDescription = null,
                    Modifier.size(36.dp)) },
                text = { Text("APPLY CHANGES",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)) },
                onClick = {
                /* TODO: Handle apply changes  (in viewmodel)*/ })
        },
        floatingActionButtonPosition = FabPosition.Center
    ) { innerPadding ->
        Column(modifier = Modifier.padding(innerPadding)) {
            MemberHeader(members.size)
            MemberList(members, viewModel::removeMember)
            Spacer(modifier = Modifier.height(20.dp))
            AddNewMemberSection(emailState, viewModel::onEmailChanged, viewModel::addMember)
        }
    }
}

// Composable to display the header with the count of group members
@Composable
fun MemberHeader(memberCount: Int) {
    Text(
        text = "Group Members ($memberCount)",
        style = MaterialTheme.typography.headlineSmall,
        modifier = Modifier
            .padding(start = 16.dp, top = 16.dp, end = 16.dp, bottom = 0.dp) // Reduced bottom padding
    )
}

// Composable to list all the members in the group
@Composable
fun MemberList(members: List<GroupMember>, onRemoveMember: (GroupMember) -> Unit) {
    Column {
        members.forEach { member ->
            MemberItem(member, onRemoveMember)
        }
    }
}

// Composable for each member item in the list
@Composable
fun MemberItem(member: GroupMember, onRemoveMember: (GroupMember) -> Unit) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .fillMaxWidth().padding(horizontal = 16.dp)
    ) {
        Card(
            modifier = Modifier
                .padding(horizontal = 0.dp, vertical = 8.dp)
                .weight(1f),
            shape = RoundedCornerShape(20.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
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
                Column(modifier = Modifier.weight(1f).padding(start = 10.dp)) {
                    Text(
                        text = member.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = member.email,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
                Column(
                    modifier = Modifier.padding(start = 10.dp),
                    horizontalAlignment = Alignment.End
                ) {
                    val statusText = member.status
                    val statusColor = if (member.status == "owes") MaterialTheme.colorScheme.error
                    else MaterialTheme.colorScheme.primary

                    Text(
                        text = statusText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColor
                    )
                    Text(
                        text = member.balance,
                        style = MaterialTheme.typography.bodyMedium,
                        color = statusColor
                    )
                }
            }
        }
        IconButton(onClick = { onRemoveMember(member) }) { //TODO: implement action
            Icon(Icons.Default.RemoveCircle, contentDescription = "Remove")
        }
    }
}


@Composable
fun AddNewMemberSection(
    emailState: TextFieldValue,
    onEmailChanged: (String) -> Unit,
    onAddMember: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Column(modifier = Modifier.padding(30.dp),
            horizontalAlignment = Alignment.CenterHorizontally) {
            LoginField(
                fieldValue = emailState,
                onTextChanged = { newValue -> onEmailChanged(newValue.text) },
                placeholder = "Email",
                imageVector = Icons.Default.Email,
                secure = false,
            )
            Button(
                onClick = onAddMember,
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth(0.6f), // 80% of the width
                contentPadding = PaddingValues(vertical = 15.dp) // Increase the vertical padding here

            ) {
                Text("ADD A NEW MEMBER",
                    style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp))
            }
        }
    }
}



// Preview for GroupEditScreen
@Preview(showBackground = true)
@Composable
fun PreviewGroupEditScreen() {
    MaterialTheme {
        val currentGroup = Group("Apartment", 2, "$200")
        GroupEditScreen(group = currentGroup)
    }
}