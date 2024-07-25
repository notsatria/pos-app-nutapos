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
    suspend fun insert(note: TransactionEntity)

    @Update
    fun update(note: TransactionEntity)

    @Delete
    fun delete(note: TransactionEntity)

    @Query("SELECT * from `transaction` ORDER BY date ASC")
    fun getAllTransactions(): LiveData<List<TransactionEntity>>
}