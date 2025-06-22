package com.example.finmate.features.model


import androidx.lifecycle.ViewModel
import com.example.finmate.TransactionRepository
import com.example.finmate.features.model.Transaction

class TransactionViewModel : ViewModel() {

    private val repo = TransactionRepository()

    fun saveTransaction(
        transaction: Transaction,
        onSuccess: () -> Unit,
        onError: (String) -> Unit
    ) {
        repo.addTransaction(transaction, onSuccess, { ex ->
            onError(ex.message ?: "Unknown Error")
        })
    }
}

