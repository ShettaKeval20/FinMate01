package com.example.finmate.features.model

import java.util.Date

data class Transaction(
    var title: String = "",
    var description: String = "",
    var amount: Double = 0.0,
    val date: String = "", // ✅ Now this is a human-readable string
    var type: TransactionType = TransactionType.EXPENSE,
    var category: String = "",
    var subCategory: String = ""

)