package com.example.finmate.features.model

enum class TransactionCategory(val label: String, val type: TransactionType?) {
    // Income
    SALARY("Salary", TransactionType.INCOME),
    FREELANCING("Freelancing", TransactionType.INCOME),
    BUSINESS("Business", TransactionType.INCOME),
    INVESTMENT("Investment", TransactionType.INCOME),
//    GIFT("Gift", TransactionType.INCOME),

    // Expense
    FOOD("Food", TransactionType.EXPENSE),
    TRANSPORT("Transport", TransactionType.EXPENSE),
    RENT("Rent", TransactionType.EXPENSE),
    UTILITIES("Utilities", TransactionType.EXPENSE),
    ENTERTAINMENT("Entertainment", TransactionType.EXPENSE),
    SHOPPING("Shopping", TransactionType.EXPENSE),
    HEALTH("Health", TransactionType.EXPENSE),
    EDUCATION("Education", TransactionType.EXPENSE),

    // Common
    OTHERS("Others", null);

    companion object {
        fun getCategoriesByType(type: TransactionType): List<TransactionCategory> {
            return values().filter { it.type == type || it.type == null }
        }
    }
}
