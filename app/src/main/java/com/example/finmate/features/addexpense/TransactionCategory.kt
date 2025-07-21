package com.example.finmate.features.model

import com.example.finmate.features.model.TransactionType  // import enum

sealed class TransactionCategory(val name: String) {
    object Salary : TransactionCategory("Salary")
    object Business : TransactionCategory("Business")
    object Investment : TransactionCategory("Investment")
    object Gift : TransactionCategory("Gift")
    object Freelancing : TransactionCategory("Freelancing")

    object Food : TransactionCategory("Food")
    object Transport : TransactionCategory("Transport")
    object Rent : TransactionCategory("Rent")
    object Utilities : TransactionCategory("Utilities")
    object Entertainment : TransactionCategory("Entertainment")
    object Shopping : TransactionCategory("Shopping")
    object Health : TransactionCategory("Health")
    object Education : TransactionCategory("Education")
    object Others : TransactionCategory("Others")

    companion object {
        fun getCategoriesByType(type: TransactionType): List<TransactionCategory> {
            return when (type) {
                TransactionType.INCOME -> listOf(Salary, Business, Investment, Gift, Freelancing)
                TransactionType.EXPENSE -> listOf(Food, Transport, Rent, Utilities, Entertainment, Shopping, Health, Education, Others)
                // No else needed because all enum cases covered
            }
        }
    }
}
