package com.myexpenseanalyzer.app.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.myexpenseanalyzer.app.data.database.ExpenseDatabase
import com.myexpenseanalyzer.app.data.entity.BudgetEntity
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import com.myexpenseanalyzer.app.repository.ExpenseRepository
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate

class ExpenseViewModel(app: Application) : AndroidViewModel(app) {

    private val database =
        ExpenseDatabase.getInstance(app)

    private val repo =
        ExpenseRepository(
            database.transactionDao()
        )

    // -----------------------------
    // Transactions
    // -----------------------------

    val transactions =
        repo.transactions.stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            emptyList()
        )

    private val _message =
        MutableSharedFlow<String>(
            extraBufferCapacity = 1
        )

    val message =
        _message.asSharedFlow()

    fun save(
        t: TransactionEntity
    ) = viewModelScope.launch {

        repo.save(t)

        _message.emit(
            "Transaction saved successfully"
        )
    }

    fun delete(
        t: TransactionEntity
    ) = viewModelScope.launch {

        repo.delete(t)

        _message.emit(
            "Transaction deleted"
        )
    }

    // -----------------------------
    // Month
    // -----------------------------

    fun monthOf(
        t: TransactionEntity
    ) = t.date.substring(0, 7)

    fun currentMonth() =
        LocalDate.now()
            .toString()
            .substring(0, 7)

    // -----------------------------
    // Budget
    // -----------------------------

    private val budgetDao =
        database.budgetDao()

    fun getBudget(
        month: String
    ): Flow<BudgetEntity?> {

        return budgetDao.getBudget(month)
    }

    fun saveBudget(
        month: String,
        amount: Double
    ) = viewModelScope.launch {

        budgetDao.saveBudget(
            BudgetEntity(
                month = month,
                amount = amount
            )
        )

        _message.emit(
            "Monthly budget saved"
        )
    }

    fun deleteBudget(
        month: String
    ) = viewModelScope.launch {

        budgetDao.deleteBudget(month)

        _message.emit(
            "Monthly budget deleted"
        )
    }
}