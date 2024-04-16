package com.hypeapps.instasplit.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class User(
    @PrimaryKey(autoGenerate = true) val userId: Int,
    @ColumnInfo val userName: String,
    @ColumnInfo val email: String,
    @ColumnInfo val phoneNumber: String,
    @ColumnInfo val password: String
)