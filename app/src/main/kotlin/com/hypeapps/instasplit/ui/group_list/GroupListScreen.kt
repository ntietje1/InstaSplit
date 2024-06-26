package com.hypeapps.instasplit.ui.group_list

import android.graphics.Color
import android.widget.ImageView
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.hypeapps.instasplit.R
import com.hypeapps.instasplit.core.model.entity.Group
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun GroupListScreen(
    onGroupClick: (Group) -> Unit, onAddExpense: () -> Unit, onAddGroup: (Group) -> Unit, viewModel: GroupListViewModel = viewModel(factory = GroupListViewModel.Factory)
) {
    val groupListState: GroupListState by viewModel.state.collectAsState()

    Scaffold(topBar = {
        TopAppBar(title = {
            Text(
                "Your Groups",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.ExtraBold,
                modifier = Modifier
                    .padding(start = 20.dp)
                    .padding(top = 24.dp)
            )
        }, actions = {
            IconButton(
                onClick = {
                    viewModel.viewModelScope.launch {
                        onAddGroup(viewModel.addGroup())
                    }
                }, modifier = Modifier
                    .padding(20.dp)
                    .padding(top = 24.dp)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.AddCircle, contentDescription = "Add Group",
                    modifier = Modifier.size(48.dp), tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        })
    }, floatingActionButton = {
        ExtendedFloatingActionButton(icon = {
            Icon(
                Icons.Filled.AccountBalanceWallet, contentDescription = null, Modifier.size(36.dp), tint = MaterialTheme.colorScheme.onPrimary
            )
        }, text = {
            Text(
                "ADD EXPENSE", style = MaterialTheme.typography.labelLarge.copy(fontSize = 14.sp), color = MaterialTheme.colorScheme.onPrimary
            )
        }, onClick = onAddExpense, containerColor = MaterialTheme.colorScheme.onPrimaryContainer, elevation = FloatingActionButtonDefaults.elevation(
            defaultElevation = 4.dp
        )
        )
    }, floatingActionButtonPosition = FabPosition.Center,

        content = { innerPadding ->

            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .padding(top = 16.dp)
            ) {
                items(groupListState.groupWrappers) { groupWrapper ->
                    var imageUrl: String? by remember { mutableStateOf(null) }

                    LaunchedEffect(Unit) {
                       imageUrl =  viewModel.getImage()
                    }
                    GroupCard(
                        group = groupWrapper.group,
                        groupStatus = viewModel.getGroupStatus(groupWrapper),
                        memberCount = groupWrapper.users.size,
                        onClick = { onGroupClick(groupWrapper.group) },
                        imgUrl = imageUrl
                    )
                }
                item {
                    Spacer(modifier = Modifier.height(80.dp))
                }
            }
        })
}

@Composable
fun GroupCard(group: Group, groupStatus: String, memberCount: Int, imgUrl: String?, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(horizontal = 20.dp, vertical = 15.dp)
            .fillMaxWidth()
            .height(150.dp)
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .background(MaterialTheme.colorScheme.primary)
                .padding(16.dp)
                .fillMaxSize(), verticalAlignment = Alignment.Top
        ) {
            AndroidView(factory = { context ->
                ImageView(context).apply {
                    scaleType = ImageView.ScaleType.CENTER_CROP
                    adjustViewBounds = true
                    setBackgroundColor(Color.WHITE)

                }
            }, modifier = Modifier
                .size(120.dp)
                .clip(RoundedCornerShape(12.dp))
            )
            { imageView ->
                Glide.with(imageView.context)
                    .load(imgUrl) //Default image
                    .override(240, 240) // Resize
                    .placeholder(R.drawable.loading)  // Display a loading image while the image loads
                    .into(imageView)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                verticalArrangement = Arrangement.Top, modifier = Modifier.fillMaxHeight()
            ) {
                Text(
                    text = group.groupName, style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onPrimary
                )
                Text(text = "$memberCount members",  style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
                Text(text = groupStatus,  style = MaterialTheme.typography.bodyMedium, fontWeight = FontWeight.SemiBold, color = MaterialTheme.colorScheme.onPrimary)
            }
        }
    }
}

