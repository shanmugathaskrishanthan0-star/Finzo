package com.myexpenseanalyzer.app.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "budgets")
data class BudgetEntity(

    @PrimaryKey
    val month: String,

    val amount: Double
)