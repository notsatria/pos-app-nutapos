package com.notsatria.posapp.ui.inputuangmasuk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.data.local.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Date

class InputUangMasukViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun insertTransaction(
        time: String,
        to: String,
        from: String,
        description: String,
        amount: Int,
        date: Long,
        type: String,
    ) {
        val transactionEntity = TransactionEntity(
            time = time,
            to = to,
            from = from,
            description = description,
            amount = amount,
            date = date,
            type = type
        )

        viewModelScope.launch(Dispatchers.IO) {
            repository.insertTransaction(transactionEntity)
        }
    }

    fun updateTransaction(transaction: TransactionEntity) = repository.updateTransaction(transaction)
}