package com.hypeapps.instasplit.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Group::class,
            parentColumns = arrayOf("groupId"),
            childColumns = arrayOf("groupId"),
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class Expense(
    @PrimaryKey(autoGenerate = true) val expenseId: Int,
    @ColumnInfo val groupId: Int,
    @ColumnInfo val totalAmount: Int,
)