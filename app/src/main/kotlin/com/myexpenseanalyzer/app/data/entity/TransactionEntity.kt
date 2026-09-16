package com.myexpenseanalyzer.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "transactions")
data class TransactionEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0,
    val type: String,
    val date: String,
    val amount: Double,
    val description: String,
    val category: String,
    val paymentMethod: String,
    val notes: String = ""
)
