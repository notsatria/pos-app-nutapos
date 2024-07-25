package com.notsatria.posapp.utils

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.notsatria.posapp.data.local.repository.TransactionRepository
import com.notsatria.posapp.di.Injection
import com.notsatria.posapp.ui.edituangmasuk.EditUangMasukViewModel
import com.notsatria.posapp.ui.inputuangmasuk.InputUangMasukViewModel
import com.notsatria.posapp.ui.main.DaftarUangMasukViewModel

class ViewModelFactory private constructor(
    private val transactionRepository: TransactionRepository
) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when (modelClass) {
            InputUangMasukViewModel::class.java -> InputUangMasukViewModel(transactionRepository) as T
            DaftarUangMasukViewModel::class.java -> DaftarUangMasukViewModel(transactionRepository) as T
            EditUangMasukViewModel::class.java -> EditUangMasukViewModel(transactionRepository) as T

            else -> throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
        }
    }

    companion object {
        fun getInstance(context: Context): ViewModelFactory =
            ViewModelFactory(Injection.provideTransactionRepository(context))
    }
}