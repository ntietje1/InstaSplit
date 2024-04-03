package com.hypeapps.instasplit.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Group(
    @PrimaryKey(autoGenerate = true) val groupId: Int,
    @ColumnInfo val groupName: String,
)