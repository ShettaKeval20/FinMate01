package com.example.finmate.features.home

import com.example.finmate.features.model.Transaction

interface TransactionRepository {
    suspend fun getRecentTransactions(limit: Int): List<Transaction>
    suspend fun getNetIncome(): Double
    suspend fun getTotalIncome(): Double
    suspend fun getTotalExpense(): Double
}
