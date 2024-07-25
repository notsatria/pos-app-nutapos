package com.notsatria.posapp.data.local.repository

import androidx.lifecycle.LiveData
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.data.local.room.TransactionDao
import com.notsatria.posapp.utils.AppExecutors
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class TransactionRepository private constructor(
    private val dao: TransactionDao,
    private val appExecutors: AppExecutors
) {
    suspend fun insertTransaction(entity: TransactionEntity) {
        withContext(Dispatchers.IO) {
            dao.insert(entity)
        }
    }

    fun getAllTransaction(): LiveData<List<TransactionEntity>> {
        return dao.getAllTransactions()
    }

    fun deleteTransaction(transactionId: Int) {
        appExecutors.diskIO.execute {
            dao.deleteTransactionById(transactionId)
        }
    }

    suspend fun updateTransaction(transaction: TransactionEntity) {
        withContext(Dispatchers.IO) {
            dao.update(transaction)
        }
    }

    companion object {
        fun getInstance(
            dao: TransactionDao,
            appExecutors: AppExecutors,
        ): TransactionRepository {
            return TransactionRepository(dao, appExecutors)
        }
    }
}