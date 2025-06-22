package com.example.finmate.features.model

data class Transaction(
    val title: String = "",
    val amount: Double = 0.0,
    val description: String = "",
    val timestamp: Long = System.currentTimeMillis()
)