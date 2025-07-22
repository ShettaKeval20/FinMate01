package com.example.finmate.util

import com.example.finmate.features.model.TransactionCategory
import com.example.finmate.features.model.TransactionCategory.Companion.subCategoryMap
import com.example.finmate.features.model.TransactionType

data class ParsedTransaction(
    val amount: Double?,
    val type: TransactionType?,
    val category: TransactionCategory?,
    val subcategory: String?,
    val description: String
)

val categoryKeywords = mapOf(
    "gifted" to TransactionCategory.GIFT,
    "received" to TransactionCategory.FREELANCING,
    "upwork" to TransactionCategory.FREELANCING,
    "fiverr" to TransactionCategory.FREELANCING,
    "groceries" to TransactionCategory.FOOD,
    "dining" to TransactionCategory.FOOD,
    "snack" to TransactionCategory.FOOD,
    "rent" to TransactionCategory.RENT,
    "salary" to TransactionCategory.SALARY,
    "shopping" to TransactionCategory.SHOPPING
    // Add more as needed
)


fun parseTransactionFromText(input: String): ParsedTransaction {
    val lower = input.lowercase()

    // Extract amount
    val amountRegex = Regex("""\d+(\.\d+)?""")
    val amount = amountRegex.find(lower)?.value?.toDoubleOrNull()

    // Guess type
    val type = when {
        listOf("received", "credited", "gifted", "salary", "income").any { it in lower } -> TransactionType.INCOME
        listOf("paid", "spent", "gave", "debited", "bought").any { it in lower } -> TransactionType.EXPENSE
        else -> null
    }

    // Guess category
    val category = categoryKeywords.entries.find { (keyword, _) ->
        keyword in lower
    }?.value

    // Guess subcategory
    val subcategory = subCategoryMap[category]?.find { it.lowercase() in lower }

    // Cleaned description
    val description = input

    return ParsedTransaction(
        amount = amount,
        type = type,
        category = category,
        subcategory = subcategory,
        description = description
    )
}
