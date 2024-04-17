package com.hypeapps.instasplit.core.model.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import java.time.LocalDate

@Entity(
    primaryKeys = ["userId", "expenseId"],
    foreignKeys = [
        ForeignKey(
            entity = User::class,
            parentColumns = ["userId"],
            childColumns = ["userId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = Expense::class,
            parentColumns = ["expenseId"],
            childColumns = ["expenseId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class UserExpense(
    @ColumnInfo val userId: Int,
    @ColumnInfo val expenseId: Int,
    @ColumnInfo val date: Long = LocalDate.now().toEpochDay(),
    @ColumnInfo val balance: Double = 0.00, // how much is owed to/by the user
)
