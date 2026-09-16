package com.myexpenseanalyzer.app.repository

import com.myexpenseanalyzer.app.data.dao.TransactionDao
import com.myexpenseanalyzer.app.data.entity.TransactionEntity

class ExpenseRepository(private val dao: TransactionDao) {
    val transactions = dao.observeAll()
    suspend fun save(t: TransactionEntity) = if (t.id == 0L) dao.insert(t) else { dao.update(t); t.id }
    suspend fun delete(t: TransactionEntity) = dao.delete(t)
}
