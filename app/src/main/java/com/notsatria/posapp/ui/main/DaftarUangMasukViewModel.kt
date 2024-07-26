package com.notsatria.posapp.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.data.local.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DaftarUangMasukViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun getAllTransactions() = repository.getAllTransaction()

    fun deleteTransaction(transactionId: Int) = repository.deleteTransaction(transactionId)

    fun getTransactionsByDateRange(startDate: Long, endDate: Long) =
        repository.getTransactionsByDateRange(startDate, endDate)
}