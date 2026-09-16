package com.myexpenseanalyzer.app.data.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.myexpenseanalyzer.app.data.dao.BudgetDao
import com.myexpenseanalyzer.app.data.dao.MemberDao
import com.myexpenseanalyzer.app.data.dao.TransactionDao
import com.myexpenseanalyzer.app.data.entity.BudgetEntity
import com.myexpenseanalyzer.app.data.entity.MemberEntity
import com.myexpenseanalyzer.app.data.entity.TransactionEntity

@Database(
    entities = [
        TransactionEntity::class,
        MemberEntity::class,
        BudgetEntity::class
    ],
    version = 3,
    exportSchema = false
)
abstract class ExpenseDatabase : RoomDatabase() {

    abstract fun transactionDao(): TransactionDao

    abstract fun memberDao(): MemberDao

    abstract fun budgetDao(): BudgetDao

    companion object {

        @Volatile
        private var INSTANCE: ExpenseDatabase? = null

        fun getInstance(context: Context): ExpenseDatabase =
            INSTANCE ?: synchronized(this) {

                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    ExpenseDatabase::class.java,
                    "expense_analyzer.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also {
                        INSTANCE = it
                    }
            }
    }
}