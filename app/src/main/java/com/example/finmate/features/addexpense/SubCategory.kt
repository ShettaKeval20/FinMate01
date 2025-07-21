package com.example.finmate.features.addexpense

import com.example.finmate.features.model.TransactionCategory

data class SubCategory(
    val name: String,
    val parent: TransactionCategory
)
