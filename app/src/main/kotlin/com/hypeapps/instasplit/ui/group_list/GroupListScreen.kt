package com.hypeapps.instasplit.ui.group_list

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
import androidx.compose.material3.FloatingActionButtonDefaults
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


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    groups: List<Group>,
    onGroupClick: (Group) -> Unit,
    onAddExpense: () -> Unit,
    viewModel: GroupListViewModel = viewModel()
) { //TODO: Add Navigation bar for the app later
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        "Your Groups",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
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
        floatingActionButton = {
            ExtendedFloatingActionButton(
                icon = {
                    Icon(
                        Icons.Filled.AccountBalanceWallet,
                        contentDescription = null,
                        Modifier.size(36.dp),
                        tint = MaterialTheme.colorScheme.onPrimary
                    )
                },
                text = {
                    Text(
                        "ADD EXPENSE",
                        style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                },
                onClick = onAddExpense,
                containerColor = MaterialTheme.colorScheme.onPrimaryContainer,
                // Apply elevation to create the shadow
                elevation = FloatingActionButtonDefaults.elevation(
                    defaultElevation = 4.dp  // You can adjust this value to increase or decrease the shadow
                )
            )
        },
        floatingActionButtonPosition = FabPosition.Center,  // Position the FAB to the center

        content = { innerPadding ->
            Column(modifier = Modifier.padding(innerPadding).padding(top = 16.dp) ) {
                groups.forEach { group ->
                    GroupCard(group = group, onClick = { onGroupClick(group) })
                }
            }
        }
    )
}

@Composable
fun GroupCard(group: Group, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp) // Custom padding for each card
            .fillMaxWidth()
            .height(150.dp) // Increased height
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary) // Set the background color here
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
                Text(text = group.name, style = MaterialTheme.typography.headlineMedium,
                    fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary)
                Text(text = group.status, color = MaterialTheme.colorScheme.onPrimaryContainer)
                //TODO: add status for user such  as "Bob owes you $100"
            }
        }
    }
}


// TODO: Replace with our actual data model (+ logic to generate status)
// package com.hypeapps.instasplit.core.model.entity.Group

data class Group(val name: String, val status: String)

@Preview(showBackground = true)
@Composable
fun PreviewGroupListScreen() {
    MaterialTheme {
        GroupListScreen(
            groups = listOf(
                Group(name = "Apartment", status = "you owe $120.00"),
                Group(name = "Co-op Group", status = "no expenses"),
                Group(name = "Friends", status = "you are owed $200.00")
            ),
            onGroupClick = {},
            onAddExpense = {}
        )
    }
}
