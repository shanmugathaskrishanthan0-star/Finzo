package com.myexpenseanalyzer.app.data.dao

import androidx.room.*
import com.myexpenseanalyzer.app.data.entity.TransactionEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface TransactionDao {
    @Query("SELECT * FROM transactions ORDER BY date DESC, id DESC") fun observeAll(): Flow<List<TransactionEntity>>
    @Insert(onConflict = OnConflictStrategy.REPLACE) suspend fun insert(transaction: TransactionEntity): Long
    @Update suspend fun update(transaction: TransactionEntity)
    @Delete suspend fun delete(transaction: TransactionEntity)
    @Query("SELECT * FROM transactions WHERE id = :id LIMIT 1") suspend fun getById(id: Long): TransactionEntity?
}
