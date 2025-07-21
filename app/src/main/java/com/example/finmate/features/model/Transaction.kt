package com.example.finmate.features.model

import java.util.Date

data class Transaction(
    var title: String = "",
    var description: String = "",
    var amount: Double = 0.0,
    var date: Long = System.currentTimeMillis(),
    var type: TransactionType = TransactionType.EXPENSE,
    var category: String = "",
    var subCategory: String = ""

)