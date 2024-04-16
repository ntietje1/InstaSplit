package com.hypeapps.instasplit.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey

@Entity(
    primaryKeys = ["groupId", "userId"],
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("groupId"),
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = User::class,
            parentColumns = arrayOf("userId"),
            childColumns = arrayOf("userId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class GroupMember(
    @ColumnInfo val groupId: Int,
    @ColumnInfo val userId: Int,
    @ColumnInfo val isAdmin: Boolean = false
)