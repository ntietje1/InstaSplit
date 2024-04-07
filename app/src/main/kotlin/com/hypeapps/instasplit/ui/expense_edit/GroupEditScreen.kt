package com.hypeapps.instasplit.ui.expense_edit

import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.FabPosition
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlin.system.measureTimeMillis

data class Member(val name: String, val email: String, val owes: Int, val borrows: Int)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupEditScreen(
    groupMembers: List<Member>,
    onMemberDeleteClick: (Member) -> Unit,
    goToSingleGroup: () -> Unit,
    viewModel: GroupEditViewModel = viewModel()) {
Scaffold(
topBar = {
    TopAppBar(
        title = {
            Text(
                "Your Groups",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier.padding(start = 20.dp).padding(top = 24.dp) // left + top padding for the title
            )
        },
        actions = {
            IconButton(
                onClick = {/* TODO: Handle add group */ },
                // Customize the size of the IconButton here
                modifier = Modifier.padding(20.dp).padding(top = 24.dp).size(48.dp) // // right + top padding for the action icon

            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle,
                    contentDescription = "Add Group",
                    // Increase the icon size here if needed
                    modifier = Modifier.size(48.dp),
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    )
},
//floatingActionButton = {
//    ExtendedFloatingActionButton(
//        icon = { Icon(Icons.Filled.AccountBalanceWallet, contentDescription = null,
//            Modifier.size(36.dp)) },
//        text = { Text("ADD EXPENSE",
//            style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp)) },
//        onClick = onAddExpense
//    )
//},
floatingActionButtonPosition = FabPosition.Center, // Position the FAB to the center
content = { innerPadding ->
    Column(modifier = Modifier.padding(innerPadding).padding(top = 16.dp) ) {
        groupMembers.forEach { member ->
            MemberCard(member = member, onClick = { onMemberDeleteClick(member) })
        }
    }
}
)
}

@Composable
fun MemberCard(member: Member, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp) // Custom padding for each card
            .fillMaxWidth()
            .height(150.dp) // Increased height
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxSize(), // Changed to fill the increased height
            verticalAlignment = Alignment.Top // Align the row content to the top
        ) {
            Box(
                modifier = Modifier
                    .size(120.dp) // Square shape with increased size
                    .clip(RoundedCornerShape(12.dp)) // Rounded corners for the square
                    .background(Color.LightGray) // Placeholder for the group image
            ) {
                // TODO: Load actual image here
                // This is a placeholder, replace it with an actual Image composable
//                Image(
//                    painter = painterResource(id = R.drawable.ic_launcher_background),
//                    contentDescription = "Group Image",
//                    modifier = Modifier.matchParentSize()
//                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                verticalArrangement = Arrangement.Top, // Align column content to the top
                modifier = Modifier.fillMaxHeight() // Take up all available height
            ) {
                Text(text = member.name, style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold)
                Text(text = member.email)
                Text(text = member.owes.toString())
                Text(text = member.borrows.toString())
                //TODO: add status for user such  as "Bob owes you $100"
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewGroupListScreen() {
    MaterialTheme {
        GroupEditScreen(
            groupMembers = listOf(
                Member(name = "Bob Smith", email = "bob.smith@gmail.com", owes = 200, borrows = 0),
                Member(name = "John Doe", email = "john.doe@gmail.com", owes = 0, borrows = 200),

            ),
            onMemberDeleteClick = {},
            goToSingleGroup = {}
        )
    }
}
