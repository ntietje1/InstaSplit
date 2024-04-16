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
    @ColumnInfo val description: String = "",
    @ColumnInfo val date: Long = 0L,
    @ColumnInfo val totalAmount: Double = 0.00,
)

//TODO: USE EMBEDDING FOR USEREXPENSES INSTEAD OF RELATION