package com.notsatria.posapp.di

import android.content.Context
import com.notsatria.posapp.data.local.repository.TransactionRepository
import com.notsatria.posapp.data.local.room.TransactionDatabase
import com.notsatria.posapp.utils.AppExecutors

object Injection {
    fun provideTransactionRepository(context: Context): TransactionRepository {
        val db = TransactionDatabase.getInstance(context)
        val dao = db.dao()
        val appExecutors = AppExecutors()

        return TransactionRepository.getInstance(dao, appExecutors)
    }
}