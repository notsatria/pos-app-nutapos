package com.notsatria.posapp.ui.edituangmasuk

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.notsatria.posapp.data.local.entity.TransactionEntity
import com.notsatria.posapp.data.local.repository.TransactionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class EditUangMasukViewModel(private val repository: TransactionRepository) : ViewModel() {
    fun updateTransaction(transaction: TransactionEntity) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateTransaction(transaction)
        }
    }
}