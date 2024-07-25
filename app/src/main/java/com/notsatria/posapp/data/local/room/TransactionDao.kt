package com.notsatria.posapp.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.notsatria.posapp.data.local.entity.TransactionEntity

@Dao
interface TransactionDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(transaction: TransactionEntity)

    @Update
    fun update(transaction: TransactionEntity)

    @Query("DELETE FROM `transaction` WHERE id = :transactionId")
    fun deleteTransactionById(transactionId: Int)

    @Query("SELECT * from `transaction` ORDER BY date ASC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>
}